package com.automatodev.e_conommiza_app.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import androidx.databinding.DataBindingUtil;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.automatodev.e_conommiza_app.R;
import com.automatodev.e_conommiza_app.databinding.ActivityRecoveryBinding;
import com.automatodev.e_conommiza_app.databinding.LayoutDialogProgressBinding;
import com.automatodev.e_conommiza_app.security.FirebaseAuthentication;
import com.automatodev.e_conommiza_app.security.callback.FirebaseAuthCallback;
import com.automatodev.e_conommiza_app.utils.ComponentUtils;

public class RecoveryActivity extends AppCompatActivity {
    private LayoutDialogProgressBinding bindingProgress;
    private AlertDialog dialogProgress;
    private ActivityRecoveryBinding binding;
    private FirebaseAuthentication auth;
    private ComponentUtils componentUtils;
    public static boolean status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRecoveryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = new FirebaseAuthentication(this);
        componentUtils = new ComponentUtils(this);

        bindingProgress = DataBindingUtil.inflate(getLayoutInflater().from(this), R.layout.layout_dialog_progress, binding.relativeDaddyRecovery, false);

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


    public void actRecoveryLogin(View view){
        NavUtils.navigateUpFromSameTask(RecoveryActivity.this);
    }


    public void sendEmail(View view){
        String email = binding.edtEmailRecovery.getText().toString().trim();
        if (email.isEmpty()){
            componentUtils.onTextListener(binding.edtEmailRecovery);
            componentUtils.showSnackbar("Digite um email válido para a recuperação da senha!",2000);
            binding.edtEmailRecovery.setBackgroundResource(R.drawable.bg_edt_global_error);
        } else{
            bindingProgress.setIsLoading(true);
            dialogProgress.show();
            auth.recoveryPassword(email, new FirebaseAuthCallback() {
                @Override
                public void onSuccess(boolean result) {
                    returnSuccess();
                }

                @Override
                public void onFailure(String message) {
                    switch (message){
                        case "taskFailure":
                            returnFailure("Não foi possivel enviar o e-mail.\nVerifique a ortografia e tente novamente");
                            break;
                        case "cancel":
                            returnFailure("Operação de envio cancelada.\nVerifique o formato do e-mail e e tente novamente.");
                            break;
                        case "emailException":
                            returnFailure("Formato de email inválido. \nInsira o e-mail correto para continuar.");
                            break;
                        case "failure":
                            returnFailure("Erro ao enviar o e-mail. \nVerifique sua conexão e tente novamente");
                    }
                }
            });
        }
    }

    private void returnSuccess() {
        bindingProgress.setIsLoading(false);
        bindingProgress.setStatus(true);
        bindingProgress.setInformation("E-mail enviado com sucesso!");
        new Thread() {
            @Override
            public void run() {
                try {
                    sleep(1200);
                    finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    public void returnFailure(String message) {
        bindingProgress.setIsLoading(false);
        bindingProgress.setInformation("Ops! Algo deu errado!");
        binding.btnLoginRecovery.setEnabled(true);
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