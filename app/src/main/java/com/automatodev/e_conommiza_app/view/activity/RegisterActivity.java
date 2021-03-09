package com.automatodev.e_conommiza_app.view.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.automatodev.e_conommiza_app.R;
import com.automatodev.e_conommiza_app.database.firebase.callback.FirestoreSaveCallback;
import com.automatodev.e_conommiza_app.database.firebase.firestore.FirestoreService;
import com.automatodev.e_conommiza_app.database.room.controller.UserController;
import com.automatodev.e_conommiza_app.model.UserEntity;
import com.automatodev.e_conommiza_app.security.firebaseAuth.Authentication;
import com.automatodev.e_conommiza_app.security.callback.FirebaseAuthCallback;
import com.automatodev.e_conommiza_app.databinding.ActivityRegisterBinding;
import com.automatodev.e_conommiza_app.databinding.LayoutDialogProgressBinding;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuthEmailException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class RegisterActivity extends AppCompatActivity {

    private final String LOG_X = "logx";
    public static boolean status;

    private AlertDialog dialogProgress;
    private LayoutDialogProgressBinding bindingProgress;
    private Authentication auth;
    private FirestoreService firestoreService;
    private ActivityRegisterBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        View viewRegister = binding.getRoot();
        setContentView(viewRegister);

        auth = new Authentication();
        firestoreService = new FirestoreService();
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

    public void actRegisterMain(View view) {
        dialogProgress = new AlertDialog.Builder(this).create();
        dialogProgress.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        bindingProgress = DataBindingUtil.inflate(getLayoutInflater().from(this), R.layout.layout_dialog_progress, binding.relativeDaddyRegister, false);
        dialogProgress.setView(bindingProgress.getRoot());

        String user = binding.edtUserRegister.getText().toString().trim();
        String email = binding.edtEmailRegister.getText().toString().trim();
        String password = binding.edtPasswordRegister.getText().toString().trim();

        if (user.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Snackbar.make(binding.relativeDaddyRegister, "Necessário o preenchimento de todos os campos", Snackbar.LENGTH_LONG).show();
        } else {
            UserEntity userEntity = new UserEntity();
            userEntity.setUserName(user);
            userEntity.setUserEmail(email);
            dialogProgress.show();
            bindingProgress.setIsLoading(true);
            bindingProgress.setInformation("Criando conta...");
            auth.registerWithEmail(email, password, new FirebaseAuthCallback() {
                @Override
                public void onSuccess(Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        userEntity.setUserUid(auth.getUser().getUid());
                        bindingProgress.setInformation("Salvando dados...");
                        firestoreService.saveUser(userEntity, new FirestoreSaveCallback() {
                            @Override
                            public void onSuccess() {
                                UserController userController = new ViewModelProvider(RegisterActivity.this).get(UserController.class);
                                new CompositeDisposable().add(userController.addUser(userEntity).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(()->{
                                    bindingProgress.setIsLoading(false);
                                    bindingProgress.setStatus(true);
                                    bindingProgress.setInformation("Sucesso!!");
                                    new Thread() {
                                        @Override
                                        public void run() {
                                            try {
                                                sleep(1200);
                                                startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                                                finish();
                                            } catch (InterruptedException e) {
                                                e.printStackTrace();
                                                Log.e(LOG_X, "Error actReisterMain thread: " + e.getMessage());
                                            }
                                        }
                                    }.start();
                                }));

                            }

                            @Override
                            public void onFailure(Exception e) {

                            }
                        });

                    } else {
                        bindingProgress.setIsLoading(false);
                        bindingProgress.setStatus(false);
                        try {
                            throw task.getException();
                        } catch (FirebaseAuthWeakPasswordException e) {
                            bindingProgress.setInformation("Escolha uma senha mais forte");
                        } catch (FirebaseAuthEmailException e) {
                            bindingProgress.setInformation("Email com formato invalido!");
                        } catch (FirebaseAuthUserCollisionException e) {
                            bindingProgress.setInformation("Email ja cadastrado em outra conta!");
                        } catch (Exception e) {
                            e.printStackTrace();
                            bindingProgress.setInformation("Ops! Algo deu errado!");
                            Snackbar.make(binding.relativeDaddyRegister, "Algo deu errado, verifique seu email e/ou senha.\nDa uma olhada na sua conexão :D", Snackbar.LENGTH_LONG).show();
                        }
                        new Thread() {
                            @Override
                            public void run() {
                                try {
                                    sleep(2000);
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
                    Log.e(LOG_X, "Error actRegisterMain: " + e.getMessage());
                    e.printStackTrace();
                }
            });
        }


    }

}