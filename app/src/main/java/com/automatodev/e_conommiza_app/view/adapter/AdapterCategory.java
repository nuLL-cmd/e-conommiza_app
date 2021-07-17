package com.automatodev.e_conommiza_app.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.automatodev.e_conommiza_app.R;
import com.automatodev.e_conommiza_app.databinding.LayoutItemsCategoryBinding;
import com.automatodev.e_conommiza_app.entity.model.CategoryEntity;
import com.automatodev.e_conommiza_app.enumarator.TypeEnum;
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
    private ImageView imageNotFoundCategory;

    private TypeEnum typeEnum;

    public AdapterCategory(ImageView imageNotFoundCategory, List<CategoryEntity> categories, ItemContract itemContract, TypeEnum typeEnum) {
        this.imageNotFoundCategory = imageNotFoundCategory;
        this.categories = categories;
        this.categoriesAll = new ArrayList<>(categories);
        this.itemContract = itemContract;
        this.typeEnum = typeEnum;
    }


    @NonNull
    @Override
    public AdapterCategory.DataHandler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }

        this.binding = DataBindingUtil.inflate(layoutInflater, R.layout.layout_items_category, parent, false);

        return new DataHandler(this.binding);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterCategory.DataHandler holder, int position) {

        holder.setBinding(categories.get(position));

    }

    @Override
    public int getItemCount() {
        if (categories.size() == 0) {

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

            if (charSequence.toString().isEmpty()) {
                categoriesFilter.addAll(categoriesAll);
            } else {
                for (CategoryEntity category : categoriesAll) {
                    if (category.getName().toLowerCase().contains(charSequence.toString().toLowerCase())) {
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
            imageNotFoundCategory.setVisibility(categories.size() == 0 ? View.VISIBLE : View.GONE);
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
            binding.setTypeEnum(typeEnum);
            binding.setCategory(categoryEntity);
            binding.executePendingBindings();
            binding.getRoot().setOnClickListener(view -> itemContract.itemCategory(categoryEntity));
        }

    }
}
