package com.automatodev.e_conommiza_app.view.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import androidx.databinding.DataBindingUtil;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.automatodev.e_conommiza_app.R;
import com.automatodev.e_conommiza_app.entity.model.UserEntity;
import com.automatodev.e_conommiza_app.entity.modelBuild.UserEntityBuilder;
import com.automatodev.e_conommiza_app.security.FacebookAuthentication;
import com.automatodev.e_conommiza_app.security.FirebaseAuthentication;
import com.automatodev.e_conommiza_app.databinding.ActivityRegisterBinding;
import com.automatodev.e_conommiza_app.databinding.LayoutDialogProgressBinding;
import com.automatodev.e_conommiza_app.security.GoogleAuthentication;
import com.automatodev.e_conommiza_app.security.callback.FacebookAuthCallback;
import com.automatodev.e_conommiza_app.security.callback.FirebaseAuthCallback;
import com.automatodev.e_conommiza_app.security.callback.GoogleAuthCallback;
import com.automatodev.e_conommiza_app.utils.ComponentUtils;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;

public class RegisterActivity extends AppCompatActivity {

    public static boolean status;

    private ActivityRegisterBinding binding;
    private AlertDialog dialogProgress;
    private LayoutDialogProgressBinding bindingProgress;
    private ComponentUtils componentUtils;
    FirebaseAuthentication firebaseAuthentication;
    private GoogleSignInClient client;
    private GoogleAuthentication googleAuthentication;
    private FacebookAuthentication facebookAuthentication;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        View viewRegister = binding.getRoot();
        setContentView(viewRegister);

        firebaseAuthentication = new FirebaseAuthentication(this);

        componentUtils = new ComponentUtils(this);
        bindingProgress = DataBindingUtil.inflate(getLayoutInflater().from(this), R.layout.layout_dialog_progress, binding.relativeDaddyRegister, false);

        dialogProgress = new AlertDialog.Builder(this).create();
        dialogProgress.setCancelable(false);
        dialogProgress.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialogProgress.setView(bindingProgress.getRoot());

        facebookAuthentication = new FacebookAuthentication(this);

        googleAuthentication = new GoogleAuthentication(this);

        GoogleSignInOptions options = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        client = GoogleSignIn.getClient(this, options);


    }

    @Override
    protected void onStart() {
        super.onStart();
        status = true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        status = false;
    }

    public void actRegisteLogin(View view) {
        NavUtils.navigateUpFromSameTask(this);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 100:
                Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                try {
                    GoogleSignInAccount account = task.getResult(ApiException.class);
                    startGoogleLogin(account.getIdToken());
                } catch (ApiException e) {
                    Log.e("logx", "Error login Google: " + e.getMessage());
                }
                break;
            case 64206:
                facebookAuthentication.getCallbackManager().onActivityResult(requestCode, resultCode, data);
                dialogProgress.show();
                break;
        }
    }

    public void loginGoogle(View view) {
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

    public void actRegisterMain(View view) {

        String user = binding.edtUserRegister.getText().toString().trim();
        String email = binding.edtEmailRegister.getText().toString().trim();
        String password = binding.edtPasswordRegister.getText().toString().trim();

        if (user.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Snackbar.make(binding.relativeDaddyRegister, "Todos os campos devem ser preenchidos", Snackbar.LENGTH_LONG).show();
        } else {

            dialogProgress.show();
            bindingProgress.setIsLoading(true);
            bindingProgress.setInformation("Criando conta...");
            UserEntity userEntity = new UserEntityBuilder().userName(user)
                    .userEmail(email).build();

            firebaseAuthentication.registerWithEmail(userEntity, bindingProgress, email, password, new FirebaseAuthCallback() {
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
                        startActivity(new Intent(RegisterActivity.this, MainActivity.class));
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

}