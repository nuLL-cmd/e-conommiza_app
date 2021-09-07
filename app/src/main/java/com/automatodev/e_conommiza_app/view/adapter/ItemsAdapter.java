package com.automatodev.e_conommiza_app.view.adapter;

import android.annotation.SuppressLint;
import android.graphics.PorterDuff;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.automatodev.e_conommiza_app.R;
import com.automatodev.e_conommiza_app.databinding.LayoutItemsMainBinding;
import com.automatodev.e_conommiza_app.entity.model.CategoryEntity;
import com.automatodev.e_conommiza_app.entity.model.DataEntryEntity;
import com.automatodev.e_conommiza_app.enumarator.TypeEnum;
import com.automatodev.e_conommiza_app.listener.ItemContract;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@SuppressLint("NonConstantResourceId")
public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.DataHandler> {

    private List<DataEntryEntity> dataEntryEntities;
    private List<DataEntryEntity> inputList;
    private List<DataEntryEntity> outputList;
    private LayoutInflater layoutInflater;
    private OnItemClickListener listener;
    private ItemContract itemContract;
    private LayoutItemsMainBinding binding;

    public interface OnItemClickListener {
        void onIitemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public ItemsAdapter(List<DataEntryEntity> dataEntryEntities, ItemContract itemContract) {

        this.dataEntryEntities = dataEntryEntities;
        this.itemContract = itemContract;

        this.inputList = new ArrayList<>();
        this.outputList = new ArrayList<>();

        if (dataEntryEntities.size() != 0) {
            this.inputList = dataEntryEntities.stream().filter(entry -> entry.getTypeEntry().equals(TypeEnum.INPUT)).collect(Collectors.toList());
            this.outputList = dataEntryEntities.stream().filter(entry -> entry.getTypeEntry().equals(TypeEnum.OUTPUT)).collect(Collectors.toList());

            dataEntryEntities.clear();

            if (inputList.size() > 0)
                inputList.get(0).setFirst(true);


            if (inputList.size() > 0 && outputList.size() > 0 ){
                inputList.get(inputList.size() - 1).setViewColor(true);
                outputList.get(0).setLast(true);
            }



            dataEntryEntities.addAll(inputList);
            dataEntryEntities.addAll(outputList);


        }
    }

    @NonNull
    @Override
    public DataHandler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }

        binding = DataBindingUtil.inflate(layoutInflater, R.layout.layout_items_main, parent, false);
        return new DataHandler(binding, listener);
    }


    @Override
    public void onBindViewHolder(@NonNull DataHandler holder, int position) {

        holder.setBinding(dataEntryEntities.get(position));


    }

    @Override
    public int getItemCount() {
        return dataEntryEntities.size();
    }

    public class DataHandler extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {

        private LayoutItemsMainBinding binding;
        private DataEntryEntity dataEntryEntity;


        private MenuItem.OnMenuItemClickListener itemListener = item -> {
            itemContract.itemMenuActions(dataEntryEntity, item.getItemId());
            return true;
        };

        public DataHandler(@NonNull LayoutItemsMainBinding binding, OnItemClickListener listener) {

            super(binding.getRoot());
            this.binding = binding;
            binding.getRoot().setOnCreateContextMenuListener(this);

            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onIitemClick(position);
                    }
                }
            });

        }

        public void setBinding(DataEntryEntity dataEntryEntity) {
            CategoryEntity.getCategories().stream()
                    .filter(category -> category.getName().equals(dataEntryEntity.getCategory()))
                    .findFirst().ifPresent(categoryEntity -> binding.setCategory(categoryEntity));

            binding.setDataEntry(dataEntryEntity);
            this.dataEntryEntity = dataEntryEntity;
            binding.executePendingBindings();
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.setHeaderTitle("Status");
            if (dataEntryEntity.getPayment().equals(2)) {
                MenuItem unFrozen = menu.add(0, 4, 2, "Descongelar");
                unFrozen.setOnMenuItemClickListener(itemListener);
            } else {
                MenuItem pay = dataEntryEntity.getTypeEntry().getCode().equals(TypeEnum.INPUT.getCode())
                        ? menu.add(0, dataEntryEntity.getPayment().equals(1) ? 2 : 1, 1, dataEntryEntity.getPayment().equals(1) ? "Não recebido" : "Recebido")
                        : menu.add(0, dataEntryEntity.getPayment().equals(1) ? 2 : 1, 1, dataEntryEntity.getPayment().equals(1) ? "Não pago" : "Pago");
                MenuItem frozen = menu.add(0, 3, 2, "Congelar");
                MenuItem delete = menu.add(0, 6, 3, "Deletar");

                pay.setOnMenuItemClickListener(itemListener);
                frozen.setOnMenuItemClickListener(itemListener);
                delete.setOnMenuItemClickListener(itemListener);
            }

            MenuItem separator = menu.add(0, 7, 4, "");
            MenuItem subtitle = menu.add(0, 5, 5, "Ver todos");
            subtitle.setOnMenuItemClickListener(itemListener);


        }

    }
}
