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
import android.widget.Toast;

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
import com.automatodev.e_conommiza_app.databinding.LayoutDialogChoiseGlobalBinding;
import com.automatodev.e_conommiza_app.databinding.LayoutDialogStatusBinding;
import com.automatodev.e_conommiza_app.entity.model.CategoryEntity;
import com.automatodev.e_conommiza_app.entity.model.DataEntryEntity;
import com.automatodev.e_conommiza_app.entity.model.PerspectiveEntity;
import com.automatodev.e_conommiza_app.entity.modelBuild.PerspectiveEntityBuilder;
import com.automatodev.e_conommiza_app.enumarator.TypeEnum;
import com.automatodev.e_conommiza_app.listener.ItemContract;
import com.automatodev.e_conommiza_app.utils.ComponentUtils;
import com.automatodev.e_conommiza_app.view.adapter.ItemsAdapter;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class FragmentHost extends Fragment implements ItemContract {

    private ItemsAdapter itemsAdapter;
    private RecyclerView recyclerView;
    private RelativeLayout relativeNoContent;
    private PerspectiveEntity perspectiveEntity;
    private ComponentUtils componentUtils;
    private CompositeDisposable compositeDisposable;
    private PerspectiveController perspectiveController;
    private DataEntryController dataEntryController;
    private PerspectiveEntity perspectiveEntityFragment;


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
        perspectiveEntityFragment = (PerspectiveEntity) (getArguments().getSerializable("item"));
        recyclerView = view.findViewById(R.id.recyclerItens_layoutPerspective);
        relativeNoContent = view.findViewById(R.id.relativeNoContent_layoutPerspective);

        componentUtils = new ComponentUtils(getActivity());

        compositeDisposable = new CompositeDisposable();
        dataEntryController = new ViewModelProvider(this).get(DataEntryController.class);
        perspectiveController = new ViewModelProvider(this).get(PerspectiveController.class);

        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        try {

            if (perspectiveEntityFragment.getItemsPerspective().size() == 0)
                relativeNoContent.setVisibility(View.VISIBLE);
            else
                relativeNoContent.setVisibility(View.GONE);

            perspectiveEntity = new PerspectiveEntityBuilder()
                    .idPerspective(perspectiveEntityFragment.getIdPerspective())
                    .year(perspectiveEntityFragment.getYear())
                    .month(perspectiveEntityFragment.getMonth())
                    .userUid(perspectiveEntityFragment.getUserUid())
                    .totalCredit(perspectiveEntityFragment.getTotalCredit())
                    .totalDebit(perspectiveEntityFragment.getTotalDebit())
                    .build();


            itemsAdapter = new ItemsAdapter(perspectiveEntityFragment.getItemsPerspective(), this);

            recyclerView.hasFixedSize();
            recyclerView.setAdapter(itemsAdapter);
            itemsAdapter.notifyDataSetChanged();
            itemsAdapter.setOnItemClickListener(position1 -> {
                if (perspectiveEntityFragment.getItemsPerspective().get(position1).getPayment().equals(2)) {
                    componentUtils.showSnackbar("Reative o registro antes de edita-lo!", 1500);
                } else {
                    Intent intent = new Intent(getActivity(), AddItemActivity.class);
                    intent.putExtra("typeIntent", "edit");
                    intent.putExtra("perspective", perspectiveEntity);
                    intent.putExtra("data", perspectiveEntityFragment.getItemsPerspective().get(position1));
                    startActivity(intent);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            Log.e("logx", "Error size: " + e.getMessage());
        }


    }

    public ViewGroup getViewGroup() {
        return this.viewGroup;
    }


    @Override
    public void onResume() {
        super.onResume();
        try {
            if (itemsAdapter != null)
                itemsAdapter.notifyDataSetChanged();
            else {
                itemsAdapter = new ItemsAdapter(perspectiveEntityFragment.getItemsPerspective(), this);
                Toast.makeText(getContext(), "OnResume fragmentHost itemAdaptaer  == null", Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            e.printStackTrace();
            Log.e("logx", "Error onResume FragmenetHost: " + e.getMessage());
        }
    }

    @Override
    public void itemMenuActions(DataEntryEntity dataEntryEntity, int itemId) {

        final int OPTION_PAY = 1;
        final int OPTION_NOT_PAY = 2;
        final int OPTION_FROZEN = 3;
        final int OPTION_UNFROZEN = 4;
        final int OPTION_MORE = 5;
        final int OPTION_DELETE = 6;

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
                updatePerspective(dataEntryEntity, 2, "frozen");
                break;
            case OPTION_UNFROZEN:
                updatePerspective(dataEntryEntity, 0, "unFrozen");
                break;
            case OPTION_MORE:
                AlertDialog dialog = new AlertDialog.Builder(getContext()).create();
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                LayoutDialogStatusBinding binding = DataBindingUtil.inflate(getLayoutInflater().from(getActivity()), R.layout.layout_dialog_status, getViewGroup(), false);
                binding.btnBackLayoutStatus.setOnClickListener(view -> {
                    dialog.dismiss();
                });
                dialog.setView(binding.getRoot());
                dialog.show();
                break;
            case OPTION_DELETE:
                AlertDialog dialogConfirm = new AlertDialog.Builder(getContext()).create();
                LayoutDialogChoiseGlobalBinding dialogBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.layout_dialog_choise_global, getViewGroup(), false);
                dialogConfirm.setCancelable(false);
                dialogConfirm.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                dialogConfirm.setView(dialogBinding.getRoot());
                dialogBinding.lblMessageDialogLogout.setText("Confirma a remoção deste item? \n(Procedimento não pode ser desfeito))");
                dialogBinding.lblTitleDialogLogout.setText("Remover item");
                dialogConfirm.show();
                dialogBinding.btnYesDialogLogout.setOnClickListener(v -> {
                    dialogConfirm.dismiss();
                    deleteItemDatabase(dataEntryEntity);
                });
                dialogBinding.btnCancelDialogLogout.setOnClickListener(v2 -> dialogConfirm.dismiss());
                break;

        }

    }

    @Override
    public void itemCategory(CategoryEntity categoryEntity) {

    }

    public void updatePerspective(DataEntryEntity dataEntryEntity, Integer payment, String operation) {

        compositeDisposable.add(perspectiveController.getPerspectiveById(dataEntryEntity.getIdPersp()).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(perspectiveEntity -> {
                    if (operation.equals("frozen")) {
                        if (dataEntryEntity.getTypeEntry().getCode().equals(TypeEnum.INPUT.getCode()))
                            perspectiveEntity.setTotalCredit(perspectiveEntity.getTotalCredit().subtract(dataEntryEntity.getValueEntry()));
                        else
                            perspectiveEntity.setTotalDebit(perspectiveEntity.getTotalDebit().subtract(dataEntryEntity.getValueEntry()));

                    } else {
                        if (dataEntryEntity.getTypeEntry().getCode().equals(TypeEnum.INPUT.getCode()))
                            perspectiveEntity.setTotalCredit(perspectiveEntity.getTotalCredit().add(dataEntryEntity.getValueEntry()));
                        else
                            perspectiveEntity.setTotalDebit(perspectiveEntity.getTotalDebit().add(dataEntryEntity.getValueEntry()));

                    }

                    dataEntryEntity.setPayment(payment);
                    compositeDisposable.add(dataEntryController.addDataEntry(dataEntryEntity).subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread()).subscribe(() -> {
                                compositeDisposable.add(perspectiveController.updatePerspective(perspectiveEntity)
                                        .subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread()).subscribe(compositeDisposable::dispose));

                            }));

                }));


    }

    public void deleteItemDatabase(DataEntryEntity dataEntryEntity) {

        try {
            compositeDisposable.add(perspectiveController.getPerspectiveById(dataEntryEntity.getIdPersp()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(perspective -> {
                compositeDisposable.add(dataEntryController.deleteDataEntry(dataEntryEntity).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(() -> {

                    boolean typeData = dataEntryEntity.getTypeEntry().equals(TypeEnum.INPUT);
                    if (typeData)
                        perspective.setTotalCredit(perspective.getTotalCredit().subtract(dataEntryEntity.getValueEntry()));
                    else
                        perspective.setTotalDebit(perspective.getTotalDebit().subtract(dataEntryEntity.getValueEntry()));


                    compositeDisposable.add(perspectiveController.updatePerspective(perspective).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(() -> {
                        compositeDisposable.dispose();
                        componentUtils.showSnackbar("Sucesso!", 800);
                    }));

                }));
            }));


        } catch (Exception e) {
            e.printStackTrace();
            Log.e("logx", "Error deleteItemDatabase: " + e.getMessage());

        }

    }


}
