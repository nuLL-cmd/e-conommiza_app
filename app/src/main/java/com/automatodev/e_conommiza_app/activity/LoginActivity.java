package com.automatodev.e_conommiza_app.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.automatodev.e_conommiza_app.R;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

       // getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
    }

    public void actLoginRegister(View view){
        if (!RegisterActivity.status){
            startActivity(new Intent(this, RegisterActivity.class));
        }
    }


    public void actLoginMain(View view){
        if (!MainActivity.status){
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }
}