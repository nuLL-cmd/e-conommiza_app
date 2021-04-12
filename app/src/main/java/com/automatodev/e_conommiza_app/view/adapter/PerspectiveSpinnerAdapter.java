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
import com.automatodev.e_conommiza_app.entidade.model.CategoryEntity;
import com.automatodev.e_conommiza_app.entidade.model.PerspectiveEntity;


import java.util.List;

@SuppressLint("SetTextI18n")
public class PerspectiveSpinnerAdapter extends ArrayAdapter {

    private List<PerspectiveEntity> perspectiveEntities;


    public PerspectiveSpinnerAdapter(@NonNull Context context, @NonNull List<PerspectiveEntity> perspectiveEntities) {
        super(context, 0, perspectiveEntities);
        this.perspectiveEntities = perspectiveEntities;

    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_spinner, parent, false);

        TextView txtItem = convertView.findViewById(R.id.txtCategory_layout);


        txtItem.setText(this.perspectiveEntities.get(position).getMonth() + " / " + this.perspectiveEntities.get(position).getYear());


        return convertView;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_spinner_dropdown, parent, false);


        TextView txtItem = convertView.findViewById(R.id.txtCategory_drop);

        txtItem.setText(this.perspectiveEntities.get(position).getMonth() + " / " + this.perspectiveEntities.get(position).getYear());


        return convertView;
    }

}
