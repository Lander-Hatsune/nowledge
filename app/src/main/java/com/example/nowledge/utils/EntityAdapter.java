package com.example.nowledge.utils;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.location.GnssAntennaInfo;
import android.util.Log;
import android.util.TypedValue;
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
    private OnItemClickListener listener;

    String mode;
    public EntityAdapter(List<EntityShort> list, String _mode) {
        this.list = list; this.mode = _mode;
    }
    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView entity_name, entity_category;

        public ViewHolder(View view, String mode) {
            super(view);
            if (mode.equals("link")){
                entity_name = view.findViewById(R.id.entity_item_label);
                entity_category = view.findViewById(R.id.entity_item_category);
            } else {
                entity_name = view.findViewById(R.id.entity_iteml_label);
                entity_category = view.findViewById(R.id.entity_iteml_category);
            }

        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder (@NonNull ViewGroup parent, int viewType) {
        View view;
        if (mode.equals("link"))
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.entity_item, parent, false);
        else
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.entity_item_list, parent, false);
        return new ViewHolder(view, mode);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        EntityShort entityShort = list.get(position);
        String name = entityShort.getLabel();
        if (!mode.equals("link")){
            holder.entity_name.setText(normalize(name, 14));
            holder.itemView.findViewById(R.id.entity_iteml_gotoIcon).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.e("click list position" + String.valueOf(position), entityShort.getCourse() + " " + name + " " + entityShort.getCategory());
                    listener.onItemCLick(entityShort.getCourse(), entityShort.getLabel());
                }
            });
        } else {
            holder.entity_name.setText(normalize(name, 12));
            holder.itemView.findViewById(R.id.entity_item_gotoIcon).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.e("click link position" + String.valueOf(position), entityShort.getCourse() + " " + name + " " + entityShort.getCategory());
                    listener.onItemCLick(entityShort.getCourse(), entityShort.getLabel());
                }
            });
        }

        holder.entity_name.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        holder.entity_name.setTextColor(R.color.purple_700);
        holder.entity_name.getPaint().setFakeBoldText(true);
        holder.entity_category.setText(entityShort.getCategory());
        holder.entity_category.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
    }

    public int getItemCount() { return list.size(); }
    private String normalize(String src, int maxl) {
        if (src.length() > maxl) {
            src = src.substring(0, maxl - 1) + "...";
        }
        return src;
    }

    public interface OnItemClickListener {
        void onItemCLick(String course, String name);
    }

    public void setOnItemClickListener (OnItemClickListener onItemClickListener) {
        listener = onItemClickListener;
    }
}
