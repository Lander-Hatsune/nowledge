package com.example.nowledge.utils;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nowledge.R;

import java.util.List;

public class EntityAdapter extends RecyclerView.Adapter<EntityAdapter.ViewHolder>{
    private List<EntityShort> list;
    public EntityAdapter(List<EntityShort> list) {
        this.list = list;
    }
    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView entity_name, entity_category;

        public ViewHolder(View view) {
            super(view);
            entity_name = view.findViewById(R.id.entity_item_label);
            entity_category = view.findViewById(R.id.entity_item_category);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder (@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.entity_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        EntityShort entityShort = list.get(position);
        holder.entity_name.setText(entityShort.getLabel());
        holder.entity_category.setText(entityShort.getCategory());
    }
    public int getItemCount() { return list.size(); }
}
