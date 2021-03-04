package com.automatodev.e_conommiza_app.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.automatodev.e_conommiza_app.R;

import java.util.List;

public class PerspectiveAdapter extends RecyclerView.Adapter<PerspectiveAdapter.DataHandler>{
    private List<String> list;
    private int position;

    public PerspectiveAdapter(List<String> list){
        this.list = list;
    }
    @NonNull
    @Override
    public DataHandler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_perspectives, parent, false);
        return new DataHandler(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DataHandler holder, int position) {

    }

    public int getItem(){
        return position;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class DataHandler extends RecyclerView.ViewHolder {
        public DataHandler(@NonNull View itemView) {
            super(itemView);

            position = getAdapterPosition();

        }
    }
}
