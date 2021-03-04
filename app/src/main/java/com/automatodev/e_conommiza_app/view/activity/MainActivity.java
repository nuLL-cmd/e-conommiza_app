package com.automatodev.e_conommiza_app.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.automatodev.e_conommiza_app.databinding.ActivityMainBinding;
import com.automatodev.e_conommiza_app.view.adapter.PerspectiveAdapter;

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
        List<String> list = new ArrayList<>();
        list.add("Janeiro / 2021");
        list.add("Fevereiro / 2021");
        list.add("Março / 2021");
        list.add("Abril / 2021");
        list.add("Maio / 2021");
        list.add("Junho / 2021");
        list.add("Julho / 2021");
        list.add("Agosto / 2021");
        list.add("Setembro / 2021");

        adapter = new PerspectiveAdapter(list);
        binding.viewPagerMain.setAdapter(adapter);


        if (list.size() == 0)
            binding.txtCashMain.setText("Comece adicionando uma perspectiva!");
        else
            binding.txtCashMain.setText(list.get(adapter.getItem()));

        binding.viewPagerMain.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
                binding.txtCashMain.setText(list.get(position));
            }
        });
    }

}