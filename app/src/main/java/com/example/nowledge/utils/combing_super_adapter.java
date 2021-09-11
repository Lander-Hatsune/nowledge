package com.example.nowledge.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.android.volley.VolleyError;
import com.example.nowledge.R;

import java.util.List;

public class combing_super_adapter extends ArrayAdapter<combing_super> {
    private int resourceId;

    public combing_super_adapter(Context content, int textViewResourceId, List<combing_super> objects){
        super(content,textViewResourceId,objects);
        resourceId=textViewResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        combing_super cs = getItem(position);

        View view;
        ViewHolder viewHolder;
        if(convertView==null){
            view= LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
            viewHolder = new ViewHolder();
            viewHolder.super_count=view.findViewById(R.id.CombingSuperCount);
            viewHolder.super_name=view.findViewById(R.id.CombingSuperRelation);
            viewHolder.super_character=view.findViewById(R.id.CombingSuperRelationChar);
            view.setTag(viewHolder);
        }else {
            view=convertView;
            viewHolder=(ViewHolder) view.getTag();
        }

        viewHolder.super_count.setText(cs.getCount());
        viewHolder.super_count.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.lightpink));
        viewHolder.super_name.setText(cs.getName());
        viewHolder.super_character.setText(cs.getCharacter());
        return view;
    }

    class ViewHolder{
        public TextView super_count;
        public TextView super_name;
        public TextView super_character;
    }

}
