package com.automatodev.e_conommiza_app.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.automatodev.e_conommiza_app.database.seed.MockFile;
import com.automatodev.e_conommiza_app.databinding.ActivityMainBinding;
import com.automatodev.e_conommiza_app.model.PerspectiveEntity;
import com.automatodev.e_conommiza_app.view.adapter.FragmentPageAdapter;
import com.automatodev.e_conommiza_app.view.adapter.PerspectiveAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static boolean status;

    private ActivityMainBinding binding;
    private FragmentPageAdapter fragmentAdapter;
    public static List<PerspectiveEntity> perspectiveEntities;
    //private PerspectiveAdapter adapter;

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
        MockFile mockFile = new MockFile();
        perspectiveEntities = new ArrayList<>();
        perspectiveEntities.addAll(mockFile.getPerspectiveEntityLIst());


       // adapter = new PerspectiveAdapter(perspectiveEntities);
        fragmentAdapter = new FragmentPageAdapter(getSupportFragmentManager(), 0,perspectiveEntities);
        binding.viewPagerMain.setAdapter(fragmentAdapter);


        if (perspectiveEntities.size() == 0){
            binding.txtPerspectiveMain.setText("Nenhuma perspectiva");
            binding.txtCreditMain.setText("R$ 0.00");
            binding.txtDebitMain.setText("R$ 0.00");

        }
        else{
            binding.txtPerspectiveMain.setText(perspectiveEntities.get(fragmentAdapter.getIntemCount()).getNamePespective());
            binding.txtCreditMain.setText("R$ "+perspectiveEntities.get(fragmentAdapter.getIntemCount()).getTotalCredit());
            binding.txtDebitMain.setText("R$ "+perspectiveEntities.get(fragmentAdapter.getIntemCount()).getTotaldDebit());
        }

        binding.viewPagerMain.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                binding.txtPerspectiveMain.setText(perspectiveEntities.get(position).getNamePespective());
                binding.txtCreditMain.setText("R$ "+perspectiveEntities.get(position).getTotalCredit());
                binding.txtDebitMain.setText("R$ "+perspectiveEntities.get(position).getTotaldDebit());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    /*    binding.viewPagerMain.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
                binding.txtPerspectiveMain.setText(perspectiveEntities.get(position).getNamePespective());
                binding.txtCreditMain.setText("R$ "+perspectiveEntities.get(position).getTotalCredit());
                binding.txtDebitMain.setText("R$ "+perspectiveEntities.get(position).getTotaldDebit());

            }
        });*/
    }

}