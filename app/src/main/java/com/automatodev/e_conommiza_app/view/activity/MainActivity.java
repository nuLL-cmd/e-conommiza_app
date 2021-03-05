package com.automatodev.e_conommiza_app.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.automatodev.e_conommiza_app.database.seed.MockFile;
import com.automatodev.e_conommiza_app.databinding.ActivityMainBinding;
import com.automatodev.e_conommiza_app.model.DataEntryEntity;
import com.automatodev.e_conommiza_app.model.PerspectiveEntity;
import com.automatodev.e_conommiza_app.view.adapter.PerspectiveAdapter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static boolean status;
    private ActivityMainBinding binding;
    private PerspectiveAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View viewBinding = binding.getRoot();
        setContentView(viewBinding);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        showData();
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

    public void actMainProfile(View view) {
        if (!ProfileActivity.status) {
            startActivity(new Intent(this, ProfileActivity.class));
            binding.menu.close(true);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }

    public void showData() {

        List<PerspectiveEntity> perspectiveEntities = new ArrayList<>(MockFile.getPerspectiveEntityLIst());


        adapter = new PerspectiveAdapter(perspectiveEntities);
        binding.viewPagerMain.setAdapter(adapter);


        if (perspectiveEntities.size() == 0)
            binding.txtCashMain.setText("Comece adicionando uma perspectiva!");
        else
            binding.txtCashMain.setText(perspectiveEntities.get(adapter.getItem()).getNamePespective());

        binding.viewPagerMain.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
                binding.txtCashMain.setText(perspectiveEntities.get(position).getNamePespective());

            }
        });
    }

}