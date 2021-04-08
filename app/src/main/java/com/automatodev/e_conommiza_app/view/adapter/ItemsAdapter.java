package com.automatodev.e_conommiza_app.view.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.automatodev.e_conommiza_app.R;
import com.automatodev.e_conommiza_app.databinding.LayoutItemsMainBinding;
import com.automatodev.e_conommiza_app.entidade.model.DataEntryEntity;

import java.util.List;

public class ItemsAdapter  extends RecyclerView.Adapter<ItemsAdapter.DataHandler>{
    private List<DataEntryEntity> dataEntryEntities;
    private LayoutInflater layoutInflater;

    public ItemsAdapter(List<DataEntryEntity> dataEntryEntities){
        this.dataEntryEntities = dataEntryEntities;
    }
    @NonNull
    @Override
    public DataHandler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (layoutInflater == null){
            layoutInflater = LayoutInflater.from(parent.getContext());
        }

        LayoutItemsMainBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.layout_items_main,parent, false);

        return new DataHandler(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull DataHandler holder, int position) {
        holder.setBinding(dataEntryEntities.get(position));
    }

    @Override
    public int getItemCount() {
        return dataEntryEntities.size();
    }

    public class DataHandler extends RecyclerView.ViewHolder {
        LayoutItemsMainBinding binding;
        public DataHandler(@NonNull LayoutItemsMainBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void setBinding(DataEntryEntity dataEntryEntity){
            binding.setDataEntry(dataEntryEntity);
            binding.executePendingBindings();
        }
    }
}
