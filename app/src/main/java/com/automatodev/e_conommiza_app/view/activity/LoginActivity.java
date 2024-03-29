package com.automatodev.e_conommiza_app.view.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.automatodev.e_conommiza_app.R;
import com.automatodev.e_conommiza_app.databinding.ActivityLoginBinding;
import com.automatodev.e_conommiza_app.databinding.LayoutDialogProgressBinding;
import com.automatodev.e_conommiza_app.security.callback.FirebaseAuthCallback;
import com.automatodev.e_conommiza_app.security.callback.GoogleAuthCallback;
import com.automatodev.e_conommiza_app.security.FacebookAuthentication;
import com.automatodev.e_conommiza_app.security.callback.FacebookAuthCallback;
import com.automatodev.e_conommiza_app.security.FirebaseAuthentication;
import com.automatodev.e_conommiza_app.security.GoogleAuthentication;
import com.automatodev.e_conommiza_app.utils.ComponentUtils;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

@SuppressLint("WrongViewCast")
public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private LayoutDialogProgressBinding bindingProgress;
    private AlertDialog dialogProgress;
    private GoogleAuthentication googleAuthentication;
    private GoogleSignInClient client;
    private ComponentUtils componentUtils;
    private FacebookAuthentication facebookAuthentication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        View viewLogin = binding.getRoot();
        setContentView(viewLogin);


        facebookAuthentication = new FacebookAuthentication(this);

        componentUtils = new ComponentUtils(this);

        bindingProgress = DataBindingUtil.inflate(getLayoutInflater().from(this), R.layout.layout_dialog_progress, binding.relativeDaddyLogin, false);

        dialogProgress = new AlertDialog.Builder(this).create();
        dialogProgress.setCancelable(false);
        dialogProgress.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialogProgress.setView(bindingProgress.getRoot());

        googleAuthentication = new GoogleAuthentication(this);

        GoogleSignInOptions options = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        client = GoogleSignIn.getClient(this, options);


    }

    public void actLoginRegister(View view) {
        if (!RegisterActivity.status) {
            startActivity(new Intent(this, RegisterActivity.class));
        }
    }

    public void actLoginMain(View view) {
        FirebaseAuthentication firebaseAuthentication = new FirebaseAuthentication(this);

        String email = binding.edtEmailLogin.getText().toString().trim();
        String password = binding.edtPasswordLogin.getText().toString().trim();

        if (validateFields()) {
            componentUtils.showSnackbar("Necessário o preenchimento de todos os campos", 1200);
        } else {
            dialogProgress.show();
            bindingProgress.setIsLoading(true);
            bindingProgress.setInformation("Aguarde...");
            firebaseAuthentication.loginWithEmail(email, password, new FirebaseAuthCallback() {
                @Override
                public void onSuccess(boolean result) {
                    returnSuccess();
                }

                @Override
                public void onFailure(String message) {
                    returnFailure(message);
                }
            });
        }

    }

    public Boolean validateFields() {
        int count = 0;
        ComponentUtils componentUtils = new ComponentUtils(this);
        EditText fields[] = new EditText[2];
        fields[0] = binding.edtEmailLogin;
        fields[1] = binding.edtPasswordLogin;
        for (EditText e : fields) {
            if (e.getText().toString().trim().isEmpty() || e.getText().toString().equals("R$ 0,00") || e.getText().toString().equals("$0.00")) {
                e.setBackgroundResource(R.drawable.bg_edt_global_error);
                componentUtils.onTextListener(e);
                count++;
            }
        }
        return count != 0;
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 100:
                if (resultCode == Activity.RESULT_OK) {
                    Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                    try {
                        GoogleSignInAccount account = task.getResult(ApiException.class);
                        startGoogleLogin(account.getIdToken());
                    } catch (ApiException e) {
                        Log.e("logx", "Error login Google: " + e.getMessage());
                    }

                } else {
                    binding.btnLoginLogin.setEnabled(true);
                    binding.btnFacebookLogin.setEnabled(true);
                    binding.btnGoogleLogin.setEnabled(true);
                    componentUtils.showSnackbar("Operação cancelada", 1500);
                }

                break;
            case 64206:
                if (resultCode == Activity.RESULT_OK) {
                    facebookAuthentication.getCallbackManager().onActivityResult(requestCode, resultCode, data);
                    dialogProgress.show();

                } else {
                    binding.btnLoginLogin.setEnabled(true);
                    binding.btnFacebookLogin.setEnabled(true);
                    binding.btnGoogleLogin.setEnabled(true);
                    componentUtils.showSnackbar("Operação cancelada", 1500);
                }

                break;
        }
    }


    public void loginGoogle(View view) {
        disableButtons();
        Intent intent = client.getSignInIntent();
        startActivityForResult(intent, 100);
    }

    private void startGoogleLogin(String token) {
        dialogProgress.show();

        googleAuthentication.firebaseAuthWithGoogle(bindingProgress, token, new GoogleAuthCallback() {
            @Override
            public void onSuccess(boolean response) {
                returnSuccess();
            }

            @Override
            public void onFailure(String errorMessage) {
                returnFailure(errorMessage);
            }
        });
    }

    public void loginFacebook(View view) {
        disableButtons();
        facebookAuthentication.loginFacebook(bindingProgress, new FacebookAuthCallback() {
            @Override
            public void onSuccess(boolean response) {
                returnSuccess();
            }

            @Override
            public void onFailure(String errorMessage) {
                returnFailure(errorMessage);
            }

        });

    }

    private void returnSuccess() {
        bindingProgress.setIsLoading(false);
        bindingProgress.setStatus(true);
        bindingProgress.setInformation("Sucesso!!");
        new Thread() {
            @Override
            public void run() {
                try {
                    sleep(1200);
                    if (!MainActivity.status) {
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        finish();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    public void returnFailure(String message) {
        bindingProgress.setIsLoading(false);
        bindingProgress.setInformation("Ops! Algo deu errado!");
        binding.btnLoginLogin.setEnabled(true);
        binding.btnFacebookLogin.setEnabled(true);
        binding.btnGoogleLogin.setEnabled(true);
        new Thread() {
            @Override
            public void run() {
                try {
                    sleep(1200);
                    dialogProgress.dismiss();
                    componentUtils.showSnackbar(message, 1500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();

    }

    public void disableButtons() {
        binding.btnLoginLogin.setEnabled(false);
        binding.btnFacebookLogin.setEnabled(false);
        binding.btnGoogleLogin.setEnabled(false);
    }

    public void actLoginRecovery(View view){
        if (!RecoveryActivity.status){
            startActivity(new Intent(this, RecoveryActivity.class));
        }
    }
}