package com.automatodev.e_conommiza_app.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;

import android.os.Bundle;
import android.view.View;

import com.automatodev.e_conommiza_app.R;

public class RegisterActivity extends AppCompatActivity {

    public static boolean status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    }

    @Override
    protected void onStart(){
        super.onStart();
        status = true;
    }

    @Override
    protected void onStop(){
        super.onStop();
        status = false;
    }

    public void actRegisteLogin(View view) {
        NavUtils.navigateUpFromSameTask(this);
        finish();
    }
}