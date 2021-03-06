package com.automatodev.e_conommiza_app.view.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.automatodev.e_conommiza_app.R;
import com.automatodev.e_conommiza_app.view.adapter.ItemsAdapter;

public class FragmentHost extends Fragment {

    private int position;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layout_perspectives,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        position = getArguments().getInt("position");
        RecyclerView recyclerView = view.findViewById(R.id.recyclerItens_layoutPerspective);
        recyclerView.hasFixedSize();
        recyclerView.setAdapter(new ItemsAdapter(MainActivity.perspectiveEntities.get(position).getItemsPerspective()));

    }
}
