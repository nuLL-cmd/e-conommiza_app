package com.automatodev.e_conommiza_app.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.automatodev.e_conommiza_app.ProfileActivity;
import com.automatodev.e_conommiza_app.R;
import com.automatodev.e_conommiza_app.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    public static boolean status;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View viewBinding = binding.getRoot();
        setContentView(viewBinding);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
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

    public void actMainProfile(View view){
        if (!ProfileActivity.status){
            startActivity(new Intent(this, ProfileActivity.class));
            binding.menu.close(true);
        }
    }
}