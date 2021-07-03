package com.automatodev.e_conommiza_app.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import androidx.databinding.DataBindingUtil;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.automatodev.e_conommiza_app.R;
import com.automatodev.e_conommiza_app.entity.model.UserEntity;
import com.automatodev.e_conommiza_app.entity.modelBuild.UserEntityBuilder;
import com.automatodev.e_conommiza_app.security.FirebaseAuthentication;
import com.automatodev.e_conommiza_app.databinding.ActivityRegisterBinding;
import com.automatodev.e_conommiza_app.databinding.LayoutDialogProgressBinding;
import com.automatodev.e_conommiza_app.security.callback.FirebaseAuthCallback;
import com.automatodev.e_conommiza_app.utils.ComponentUtils;
import com.google.android.material.snackbar.Snackbar;

public class RegisterActivity extends AppCompatActivity {

    public static boolean status;

    private ActivityRegisterBinding binding;
    private AlertDialog dialogProgress;
    private LayoutDialogProgressBinding bindingProgress;
    private ComponentUtils componentUtils;
    FirebaseAuthentication firebaseAuthentication;

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


        String user = binding.edtUserRegister.getText().toString().trim();
        String email = binding.edtEmailRegister.getText().toString().trim();
        String password = binding.edtPasswordRegister.getText().toString().trim();

        if (user.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Snackbar.make(binding.relativeDaddyRegister, "Necessário o preenchimento de todos os campos", Snackbar.LENGTH_LONG).show();
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