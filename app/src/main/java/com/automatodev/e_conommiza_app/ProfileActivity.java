package com.automatodev.e_conommiza_app;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.automatodev.e_conommiza_app.databinding.ActivityProfileBinding;
import com.automatodev.e_conommiza_app.databinding.LayoutDialogAboutBinding;

public class ProfileActivity extends AppCompatActivity {
    private ActivityProfileBinding binding;
    public static boolean status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        View viewBinding = binding.getRoot();
        setContentView(viewBinding);
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


    public void about(View view){
        LayoutDialogAboutBinding layoutBinding  = DataBindingUtil.inflate(getLayoutInflater().from(this),R.layout.layout_dialog_about,binding.relativeDaddyProfile,false);
        AlertDialog dialog = new AlertDialog.Builder(this).create();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setView(layoutBinding.getRoot());
        dialog.show();
        layoutBinding.btnCloseDialogAbout.setOnClickListener(view1 -> dialog.dismiss());
    }


}