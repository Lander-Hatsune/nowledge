package com.example.nowledge.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.nowledge.R;

import java.util.List;

public class character_adapter extends ArrayAdapter<character> {
    private int resourceId;

    public character_adapter(Context content,int textViewResourceId,List<character> objects){
        super(content,textViewResourceId,objects);
        resourceId=textViewResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        character c=getItem(position);

        View view;
        ViewHolder viewHolder;
        if(convertView==null){
            view=LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
            viewHolder=new ViewHolder();
            viewHolder.character_type=view.findViewById(R.id.character_type);
            viewHolder.character_detail=view.findViewById(R.id.character_detail);
            view.setTag(viewHolder);
        }else {
            view=convertView;
            viewHolder=(ViewHolder) view.getTag();
        }

        viewHolder.character_type.setText(c.getType());
        viewHolder.character_detail.setText(c.getDetail());
        return view;
    }

    class ViewHolder{
        public TextView character_type;
        public TextView character_detail;
    }
}
