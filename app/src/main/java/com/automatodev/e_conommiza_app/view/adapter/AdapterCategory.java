package com.automatodev.e_conommiza_app.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.automatodev.e_conommiza_app.R;
import com.automatodev.e_conommiza_app.databinding.ActivityCategoryBinding;
import com.automatodev.e_conommiza_app.databinding.LayoutItemsCategoryBinding;
import com.automatodev.e_conommiza_app.entity.model.CategoryEntity;
import com.automatodev.e_conommiza_app.listener.ItemContract;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class AdapterCategory extends RecyclerView.Adapter<AdapterCategory.DataHandler> implements Filterable {

    private final List<CategoryEntity> categories;
    private final List<CategoryEntity> categoriesAll;
    private LayoutInflater layoutInflater;
    private ItemContract itemContract;
    private LayoutItemsCategoryBinding binding;
    private ActivityCategoryBinding bindingCategory;

    public AdapterCategory(ActivityCategoryBinding bindingCategory,List<CategoryEntity> categories, ItemContract itemContract) {
        this.categories = categories;
        this.categoriesAll = new ArrayList<>(categories);
        this.itemContract = itemContract;
        this.bindingCategory = bindingCategory;
    }


    @NonNull
    @Override
    public AdapterCategory.DataHandler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }

        binding = DataBindingUtil.inflate(layoutInflater, R.layout.layout_items_category, parent, false);

        return new DataHandler(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterCategory.DataHandler holder, int position) {
        holder.setBinding(categories.get(position));
    }

    @Override
    public int getItemCount() {
        if(categories.size() == 0){

        }

        return categories.size();
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<CategoryEntity> categoriesFilter = new ArrayList<>();

            if(charSequence.toString().isEmpty()){
                categoriesFilter.addAll(categoriesAll);
            }else{
                for (CategoryEntity category: categoriesAll){
                    if (category.getName().toLowerCase().contains(charSequence.toString().toLowerCase())){
                        categoriesFilter.add(category);
                    }
                }
            }

            FilterResults filterResults = new FilterResults();
            filterResults.values = categoriesFilter;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            categories.clear();
            categories.addAll((Collection<? extends CategoryEntity>) results.values);
            notifyDataSetChanged();
        }
    };

    public class DataHandler extends RecyclerView.ViewHolder {

        private LayoutItemsCategoryBinding binding;

        public DataHandler(@NonNull LayoutItemsCategoryBinding binding) {
            super(binding.getRoot());
            this.binding = binding;


        }

        public void setBinding(CategoryEntity categoryEntity) {

            binding.setCategory(categoryEntity);
            binding.executePendingBindings();
            binding.getRoot().setOnClickListener(view -> itemContract.itemCategory(categoryEntity));
        }

    }
}
