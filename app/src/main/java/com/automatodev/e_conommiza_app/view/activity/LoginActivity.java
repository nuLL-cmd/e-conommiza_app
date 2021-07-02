package com.automatodev.e_conommiza_app.view.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.automatodev.e_conommiza_app.R;
import com.automatodev.e_conommiza_app.database.firebase.callback.FirestoreSaveCallback;
import com.automatodev.e_conommiza_app.database.firebase.firestore.FirestoreService;
import com.automatodev.e_conommiza_app.entity.model.UserEntity;
import com.automatodev.e_conommiza_app.entity.modelBuild.UserEntityBuilder;
import com.automatodev.e_conommiza_app.preferences.UserPreferences;
import com.automatodev.e_conommiza_app.security.firebaseAuth.Authentication;
import com.automatodev.e_conommiza_app.security.callback.FirebaseAuthCallback;
import com.automatodev.e_conommiza_app.databinding.ActivityLoginBinding;
import com.automatodev.e_conommiza_app.databinding.LayoutDialogProgressBinding;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthEmailException;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.auth.User;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private LayoutDialogProgressBinding bindingProgress;
    private AlertDialog dialogProgress;
    private GoogleSignInOptions options;
    private final String LOG_X = "logx";
    private GoogleSignInClient client;
    private FirebaseAuth firebaseAuth;
    private UserPreferences userPreferences;
    private UserEntity userEntity;
    private FirestoreService firestoreService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        View viewLogin = binding.getRoot();
        setContentView(viewLogin);

        bindingProgress = DataBindingUtil.inflate(getLayoutInflater().from(this), R.layout.layout_dialog_progress, binding.relativeDaddyLogin, false);
        dialogProgress = new AlertDialog.Builder(this).create();
        dialogProgress.setCancelable(false);
        dialogProgress.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialogProgress.setView(bindingProgress.getRoot());

        firebaseAuth = FirebaseAuth.getInstance();
        firestoreService = new FirestoreService();

        userPreferences = new UserPreferences(this, "user");
        options = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
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
        Authentication auth = new Authentication();

        String email = binding.edtEmailLogin.getText().toString().trim();
        String password = binding.edtPasswordLogin.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Snackbar.make(binding.relativeDaddyLogin, "Necessário o preenchimento de todos os campos", Snackbar.LENGTH_LONG).show();
        } else {
            dialogProgress.show();
            bindingProgress.setIsLoading(true);
            bindingProgress.setInformation("Um momento...");
            auth.loginWithEmail(email, password, new FirebaseAuthCallback() {
                @Override
                public void onSuccess(Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        returnSuccess();

                    } else {
                        bindingProgress.setIsLoading(false);
                        try {
                            throw task.getException();
                        } catch (FirebaseAuthInvalidCredentialsException e) {
                            bindingProgress.setInformation("Email ou senha invalidos!");
                        } catch (FirebaseAuthEmailException e) {
                            bindingProgress.setInformation("Email com formato invalido!");
                        } catch (FirebaseAuthInvalidUserException e) {
                            bindingProgress.setInformation("Usuario não encontrado!");
                        } catch (Exception e) {
                            bindingProgress.setInformation("Ops! Algo deu errado!");
                            Snackbar.make(binding.relativeDaddyLogin, "Algo deu errado, verifique seu email e/ou senha.\nDa uma checada na sua conexão :D", Snackbar.LENGTH_LONG).show();


                        }
                        new Thread() {
                            @Override
                            public void run() {
                                try {
                                    sleep(1200);
                                    dialogProgress.dismiss();
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }.start();

                    }
                }

                @Override
                public void onFailure(Exception e) {
                    Log.e(LOG_X, "OnFailure actLoginMain: " + e.getMessage());
                    e.printStackTrace();
                }
            });
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
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


    public void loginGoogle(View view) {
        Intent intent = client.getSignInIntent();
        startActivityForResult(intent, 100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d("logx", "Account id: " + account.getId());
                userEntity = new UserEntityBuilder().userEmail(account.getEmail()).userName(account.getDisplayName()).urlPhoto(Objects.requireNonNull(account.getPhotoUrl()).toString()).build();
                firebaseAuthWithGoogle(account.getIdToken(), userEntity);
            } catch (ApiException e) {
                Log.e("logx", "Error login Google: " + e.getMessage());
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken, UserEntity userEntity) {
        dialogProgress.show();
        bindingProgress.setIsLoading(true);
        bindingProgress.setInformation("Um momento...");
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    bindingProgress.setInformation("Verificando seu sdados...");
                    userEntity.setUserUid(firebaseAuth.getUid());
                    firestoreService.saveUser(userEntity, new FirestoreSaveCallback() {
                        @Override
                        public void onSuccess() {
                            userPreferences.setUser(userEntity);
                            returnSuccess();
                        }

                        @Override
                        public void onFailure(Exception e) {

                        }
                    });


                } else {
                    bindingProgress.setIsLoading(false);
                    try {
                        throw task.getException();
                    } catch (Exception e) {
                        bindingProgress.setInformation("Ops! Algo deu errado!");
                        Snackbar.make(binding.relativeDaddyLogin, "Algo deu errado em seu login, \nDa uma checada na sua conexão :D", Snackbar.LENGTH_LONG).show();
                    }
                    new Thread() {
                        @Override
                        public void run() {
                            try {
                                sleep(1200);
                                dialogProgress.dismiss();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }.start();

                }
            }
        });

    }
}