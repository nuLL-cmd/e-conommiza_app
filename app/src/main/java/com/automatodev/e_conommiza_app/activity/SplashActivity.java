package com.automatodev.e_conommiza_app.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.automatodev.e_conommiza_app.R;
import com.automatodev.e_conommiza_app.databinding.ActivitySplashBinding;

@SuppressLint("SetTextI18n")
public class SplashActivity extends AppCompatActivity {
    ActivitySplashBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySplashBinding.inflate(getLayoutInflater());
        View viewSplash = binding.getRoot();
        setContentView(viewSplash);


        showVersion();
        splash();
    }



    public void showVersion(){
        try{
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), 0);
            if (info != null){
                binding.txtVersaoSplash.setText("Versão : "+ info.versionName);
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
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    finish();
                }catch(InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();

    }
}