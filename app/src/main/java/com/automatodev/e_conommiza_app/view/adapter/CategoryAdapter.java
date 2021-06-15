package com.automatodev.e_conommiza_app.view.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.automatodev.e_conommiza_app.R;
import com.automatodev.e_conommiza_app.entity.model.CategoryEntity;


import java.util.List;

@SuppressLint("SetTextI18n")
public class CategoryAdapter extends ArrayAdapter {

    private List<CategoryEntity> categoryEntities;


    public CategoryAdapter(@NonNull Context context, @NonNull List<CategoryEntity> categoryEntities) {
        super(context, 0, categoryEntities);
        this.categoryEntities = categoryEntities;

    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_spinner, parent, false);

        TextView txtItem = convertView.findViewById(R.id.txtCategory_layout);
        ImageView imgItem = convertView.findViewById(R.id.imgCategory_layout);

        imgItem.setImageResource(this.categoryEntities.get(position).getImage());
        txtItem.setText(this.categoryEntities.get(position).getName());


        return convertView;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_spinner_dropdown, parent, false);


        TextView txtItem = convertView.findViewById(R.id.txtCategory_drop);
        ImageView imgItem = convertView.findViewById(R.id.imgCategory_drop);


        imgItem.setImageResource(this.categoryEntities.get(position).getImage());
        txtItem.setText(this.categoryEntities.get(position).getName());


        return convertView;
    }

}
