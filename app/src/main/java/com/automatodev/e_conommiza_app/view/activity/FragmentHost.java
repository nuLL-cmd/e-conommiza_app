package com.automatodev.e_conommiza_app.view.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.automatodev.e_conommiza_app.R;
import com.automatodev.e_conommiza_app.database.sqlite.controller.DataEntryController;
import com.automatodev.e_conommiza_app.database.sqlite.controller.PerspectiveController;
import com.automatodev.e_conommiza_app.databinding.LayoutDialogStatusBinding;
import com.automatodev.e_conommiza_app.entity.model.DataEntryEntity;
import com.automatodev.e_conommiza_app.entity.model.PerspectiveEntity;
import com.automatodev.e_conommiza_app.entity.modelBuild.PerspectiveEntityBuilder;
import com.automatodev.e_conommiza_app.enumarator.TypeEnum;
import com.automatodev.e_conommiza_app.listener.ItemContract;
import com.automatodev.e_conommiza_app.utils.ComponentUtils;
import com.automatodev.e_conommiza_app.view.adapter.ItemsAdapter;
import com.google.firebase.components.Component;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class FragmentHost extends Fragment implements ItemContract {

    private int position;

    public static ItemsAdapter itemsAdapter;
    private RecyclerView recyclerView;
    private RelativeLayout relativeNoContent;
    private List<DataEntryEntity> dataEntryEntities;
    private PerspectiveEntity perspectiveEntity;
    private ComponentUtils componentUtils;

    private ViewGroup viewGroup;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        viewGroup = container;
        View view = inflater.inflate(R.layout.layout_perspectives, container, false);
        dataEntryEntities = new ArrayList<>();
        position = getArguments().getInt("position");
        recyclerView = view.findViewById(R.id.recyclerItens_layoutPerspective);
        relativeNoContent = view.findViewById(R.id.relativeNoContent_layoutPerspective);
        componentUtils = new ComponentUtils(getActivity());
        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        try{
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
            itemsAdapter = new ItemsAdapter(MainActivity.perspectiveEntities.get(position).getItemsPerspective(),this);

            recyclerView.hasFixedSize();
            recyclerView.setAdapter(itemsAdapter);
            itemsAdapter.notifyDataSetChanged();
            itemsAdapter.setOnItemClickListener(position1 -> {
                if (dataEntryEntities.get(position1).getPayment().equals(2)){
                    componentUtils.showSnackbar("Reative o registro antes de edita-lo!",1500);
                }else{
                    Intent intent = new Intent(getActivity(), AddItemActivity.class);
                    intent.putExtra("typeIntent", "edit");
                    intent.putExtra("perspective", perspectiveEntity);
                    intent.putExtra("data", dataEntryEntities.get(position1));
                    startActivity(intent);
                }
            });
        }catch(Exception e){
            e.printStackTrace();
            Log.e("logx", "Error FragmentHost: "+e.getMessage());
        }


    }

    public ViewGroup getViewGroup(){
        return this.viewGroup;
    }


    @Override
    public void onResume() {
        super.onResume();
        itemsAdapter.notifyDataSetChanged();
    }

    @Override
    public void itemMenuActions(DataEntryEntity dataEntryEntity, int itemId) {

        final int OPTION_PAY = 1;
        final int OPTION_NOT_PAY = 2;
        final int OPTION_FROZEN = 3;
        final int OPTION_UNFROZEN = 4;
        final int OPTION_MORE = 5;

        DataEntryController dataEntryController = new ViewModelProvider(this).get(DataEntryController.class);
        switch (itemId) {
            case OPTION_PAY:
                dataEntryEntity.setPayment(1);
                new CompositeDisposable().add(dataEntryController.addDataEntry(dataEntryEntity).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread()).subscribe());
                break;
            case OPTION_NOT_PAY:
                dataEntryEntity.setPayment(0);
                new CompositeDisposable().add(dataEntryController.addDataEntry(dataEntryEntity).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread()).subscribe());
                break;
            case OPTION_FROZEN:
                updatePerspective(dataEntryEntity,2, "frozen");
                break;
            case OPTION_UNFROZEN:
                updatePerspective(dataEntryEntity,0, "unFrozen");
                break;
            case OPTION_MORE:
                AlertDialog dialog = new AlertDialog.Builder(getContext()).create();
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                LayoutDialogStatusBinding binding =  DataBindingUtil.inflate(getLayoutInflater().from(getActivity()), R.layout.layout_dialog_status,getViewGroup(), false);
                binding.btnBackLayoutStatus.setOnClickListener(view ->{
                    dialog.dismiss();
                });
                dialog.setView(binding.getRoot());
                dialog.show();
                break;
        }

    }

    public void updatePerspective(DataEntryEntity dataEntryEntity, Integer payment, String operation) {
        PerspectiveController perspectiveController = new ViewModelProvider(this).get(PerspectiveController.class);
        DataEntryController dataEntryController = new ViewModelProvider(this).get(DataEntryController.class);
        CompositeDisposable compositeDisposable = new CompositeDisposable();
        compositeDisposable.add(perspectiveController.getPerspectiveById(dataEntryEntity.getIdPersp()).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(perspectiveEntity -> {
                    if (operation.equals("frozen")){
                        if (dataEntryEntity.getTypeEntry().getCode().equals(TypeEnum.INPUT.getCode()))
                            perspectiveEntity.setTotalCredit(perspectiveEntity.getTotalCredit().subtract(dataEntryEntity.getValueEntry()));
                        else
                            perspectiveEntity.setTotalDebit(perspectiveEntity.getTotalDebit().subtract(dataEntryEntity.getValueEntry()));

                    }else{
                        if (dataEntryEntity.getTypeEntry().getCode().equals(TypeEnum.INPUT.getCode()))
                            perspectiveEntity.setTotalCredit(perspectiveEntity.getTotalCredit().add(dataEntryEntity.getValueEntry()));
                        else
                            perspectiveEntity.setTotalDebit(perspectiveEntity.getTotalDebit().add(dataEntryEntity.getValueEntry()));

                    }

                    dataEntryEntity.setPayment(payment);
                    compositeDisposable.add(dataEntryController.addDataEntry(dataEntryEntity).subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread()).subscribe(() ->{
                                   compositeDisposable.add(perspectiveController.updatePerspective(perspectiveEntity)
                                           .subscribeOn(Schedulers.io())
                                           .observeOn(AndroidSchedulers.mainThread()).subscribe());
                            }));

                }));

    }
}
