package com.automatodev.e_conommiza_app.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;

import com.automatodev.e_conommiza_app.databinding.ActivitySplashBinding;
import com.automatodev.e_conommiza_app.security.FirebaseAuthentication;

@SuppressLint("SetTextI18n")
public class SplashActivity extends AppCompatActivity {
    private ActivitySplashBinding binding;
    private FirebaseAuthentication auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        binding = ActivitySplashBinding.inflate(getLayoutInflater());
        View viewSplash = binding.getRoot();
        setContentView(viewSplash);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        auth = new FirebaseAuthentication(this);

        showVersion();
        splash();
    }

    public void showVersion(){
        try{
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), 0);
            if (info != null){
                binding.txtVersaoSplash.setText("Versão: "+ info.versionName);
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

    }

    public void splash(){
        new Thread(){
            @Override
            public void run(){
                try{
                    sleep(2300);
                    startActivity(new Intent(SplashActivity.this,auth.getUser() != null ? MainActivity.class : LoginActivity.class));
                    finish();
                }catch(InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();

    }
}