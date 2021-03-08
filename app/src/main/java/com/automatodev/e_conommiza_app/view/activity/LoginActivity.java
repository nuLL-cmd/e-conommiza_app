package com.automatodev.e_conommiza_app.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.automatodev.e_conommiza_app.R;
import com.automatodev.e_conommiza_app.security.firebaseAuth.Authentication;
import com.automatodev.e_conommiza_app.security.callback.FirebaseAuthCallback;
import com.automatodev.e_conommiza_app.databinding.ActivityLoginBinding;
import com.automatodev.e_conommiza_app.databinding.LayoutDialogProgressBinding;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuthEmailException;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private Authentication auth;
    private LayoutDialogProgressBinding bindingProgress;
    private AlertDialog dialogProgress;

    private final String LOG_X = "logx";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        View viewLogin = binding.getRoot();
        setContentView(viewLogin);

        auth = new Authentication();
    }

    public void actLoginRegister(View view){
        if (!RegisterActivity.status){
            startActivity(new Intent(this, RegisterActivity.class));
        }
    }


    public void actLoginMain(View view){
         bindingProgress =
                DataBindingUtil.inflate(getLayoutInflater().from(this), R.layout.layout_dialog_progress, binding.relativeDaddyLogin,false);
        dialogProgress = new AlertDialog.Builder(this).create();
        dialogProgress.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialogProgress.setView(bindingProgress.getRoot());

        String email = binding.edtEmailLogin.getText().toString().trim();
        String password = binding.edtPasswordLogin.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()){
            Snackbar.make(binding.relativeDaddyLogin, "Necessário o preenchimento de todos os campos",Snackbar.LENGTH_LONG).show();
        }else{
            dialogProgress.show();
            bindingProgress.setIsLoading(true);
            auth.loginWithEmail(email, password, new FirebaseAuthCallback() {
                @Override
                public void onSuccess(Task<AuthResult> task) {
                   if (task.isSuccessful()){
                       bindingProgress.setIsLoading(false);
                       bindingProgress.setStatus(true);
                       bindingProgress.setInformation("Sucesso!!");
                       new Thread(){
                           @Override
                           public void run(){
                               try {
                                   sleep(1200);
                                   if (!MainActivity.status){
                                       startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                       finish();
                                   }
                               } catch (InterruptedException e) {
                                   e.printStackTrace();
                               }
                           }
                       }.start();

                   }else{
                       bindingProgress.setIsLoading(false);
                       try {
                           throw task.getException();
                       } catch (FirebaseAuthInvalidCredentialsException e) {
                           bindingProgress.setInformation("Email ou senha invalidos!");
                       } catch (FirebaseAuthEmailException e) {
                           bindingProgress.setInformation("Email com formato invalido!");
                       }catch(FirebaseAuthInvalidUserException e){
                           bindingProgress.setInformation("Usuario não encontrado!");
                       } catch (Exception e) {
                           bindingProgress.setInformation("Ops! Algo deu errado!");
                           Snackbar.make(binding.relativeDaddyLogin, "Algo deu errado, verifique seu email e/ou senha.\nDa uma olhada na sua conexão :D", Snackbar.LENGTH_LONG).show();


                       }
                       new Thread(){
                           @Override
                           public void run(){
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
                    Log.e(LOG_X,"Error actLoginMain: "+e.getMessage());
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
}