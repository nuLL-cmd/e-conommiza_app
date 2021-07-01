package com.automatodev.e_conommiza_app.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import com.automatodev.e_conommiza_app.databinding.ActivityCategoryBinding;
import com.automatodev.e_conommiza_app.entity.model.CategoryEntity;
import com.automatodev.e_conommiza_app.entity.model.DataEntryEntity;
import com.automatodev.e_conommiza_app.listener.ItemContract;
import com.automatodev.e_conommiza_app.view.adapter.AdapterCategory;

public class CategoryActivity extends AppCompatActivity implements ItemContract {

    public static boolean status;
    private ActivityCategoryBinding binding;

    private AdapterCategory adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCategoryBinding.inflate(getLayoutInflater());
        View viewBinding = binding.getRoot();
        setContentView(viewBinding);

        binding.txtSearchCategory.requestFocus();

        showData();

        binding.txtSearchCategory.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.getFilter().filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });



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

    public void actCategoryAddItems(View view){
        NavUtils.navigateUpFromSameTask(CategoryActivity.this);
    }

    private void showData(){
        adapter = new AdapterCategory(binding,CategoryEntity.getCategories(), this);

        binding.recyclerCategoryCategory.setHasFixedSize(true);
        binding.recyclerCategoryCategory.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerCategoryCategory.setAdapter(adapter);
        binding.txtAllCategoryCategory.setText("Categorias dispiníveis:  " + CategoryEntity.getCategories().size());

    }

    @Override
    public void itemMenuActions(DataEntryEntity dataEntryEntity, int itemId) {

    }

    @Override
    public void itemCategory(CategoryEntity categoryEntity) {
            AddItemActivity.nameCategory = categoryEntity.getName();
            AddItemActivity.resourceCategory = categoryEntity.getImage();
            finish();
    }
}