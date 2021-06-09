package com.automatodev.e_conommiza_app.view.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.automatodev.e_conommiza_app.R;
import com.automatodev.e_conommiza_app.databinding.LayoutItemsProfileBinding;
import com.automatodev.e_conommiza_app.entidade.model.PerspectiveEntity;

import java.util.List;

public class ItemsProfileAdapter extends RecyclerView.Adapter<ItemsProfileAdapter.DataHandler>{

    private List<PerspectiveEntity> perspectiveEntities;
    private LayoutInflater layoutInflater;
    private OnItemClickListener listener;

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }

    public ItemsProfileAdapter(List<PerspectiveEntity> perspectiveEntities){
        this.perspectiveEntities = perspectiveEntities;
    }
    @NonNull
    @Override
    public DataHandler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (layoutInflater == null){
            layoutInflater = LayoutInflater.from(parent.getContext());
        }

        LayoutItemsProfileBinding binding = DataBindingUtil.inflate(layoutInflater,R.layout.layout_items_profile,parent, false);
        return new DataHandler(binding, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull DataHandler holder, int position) {
        holder.setBinding(perspectiveEntities.get(position));
    }

    @Override
    public int getItemCount() {
        return perspectiveEntities.size();
    }

    public class DataHandler extends RecyclerView.ViewHolder{
        LayoutItemsProfileBinding binding;

        public DataHandler(LayoutItemsProfileBinding binding, OnItemClickListener listener){
            super(binding.getRoot());
            this.binding = binding;

            itemView.setOnClickListener(view ->{
                if (listener != null){
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION){
                        listener.onItemClick(position);
                    }
                }
            });

        }

        public void setBinding(PerspectiveEntity perspectiveEntity) {
            binding.setPerspective(perspectiveEntity);
            binding.executePendingBindings();
        }
    }
}
