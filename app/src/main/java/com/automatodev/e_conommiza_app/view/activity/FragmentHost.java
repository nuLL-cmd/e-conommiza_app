package com.automatodev.e_conommiza_app.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.automatodev.e_conommiza_app.R;
import com.automatodev.e_conommiza_app.entidade.model.DataEntryEntity;
import com.automatodev.e_conommiza_app.entidade.model.PerspectiveEntity;
import com.automatodev.e_conommiza_app.entidade.modelBuild.PerspectiveEntityBuilder;
import com.automatodev.e_conommiza_app.view.adapter.ItemsAdapter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class FragmentHost extends Fragment {

    private int position;
    public static ItemsAdapter itemsAdapter;
    private RecyclerView recyclerView;
    private RelativeLayout relativeNoContent;
    private List<DataEntryEntity> dataEntryEntities;
    private PerspectiveEntity perspectiveEntity;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_perspectives, container, false);
        dataEntryEntities = new ArrayList<>();
        position = getArguments().getInt("position");
        recyclerView = view.findViewById(R.id.recyclerItens_layoutPerspective);
        relativeNoContent = view.findViewById(R.id.relativeNoContent_layoutPerspective);
        if (MainActivity.perspectiveEntities.get(position).getItemsPerspective().size() == 0)
            relativeNoContent.setVisibility(View.VISIBLE);
        else
            relativeNoContent.setVisibility(View.GONE);

        perspectiveEntity = new PerspectiveEntityBuilder()
                .idPerspective(MainActivity.perspectiveEntities.get(position).getIdPerspective())
                .year(MainActivity.perspectiveEntities.get(position).getYear())
                .month(MainActivity.perspectiveEntities.get(position).getMonth())
                .userUid(MainActivity.perspectiveEntities.get(position).getUserUid())
                .totalCredit(MainActivity.perspectiveEntities.get(position).getTotalCredit())
                .totalDebit(MainActivity.perspectiveEntities.get(position).getTotalDebit())
                .build();

        dataEntryEntities = MainActivity.perspectiveEntities.get(position).getItemsPerspective();
        itemsAdapter = new ItemsAdapter(MainActivity.perspectiveEntities.get(position).getItemsPerspective());
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView.hasFixedSize();
        recyclerView.setAdapter(itemsAdapter);
        itemsAdapter.notifyDataSetChanged();
        itemsAdapter.setOnItemClickListener(position1 -> {
            Intent intent = new Intent(getActivity(), AddItemActivity.class);
            intent.putExtra("typeIntent","edit");
            intent.putExtra("perspective", perspectiveEntity);
            intent.putExtra("data", dataEntryEntities.get(position1));
            startActivity(intent);
        });

    }


    @Override
    public void onResume() {
        super.onResume();
        itemsAdapter.notifyDataSetChanged();
    }
}
