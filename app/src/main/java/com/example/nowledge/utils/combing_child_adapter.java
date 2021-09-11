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

public class combing_child_adapter extends ArrayAdapter<combing_child> {
    private int resourceId;

    public combing_child_adapter(Context content, int textViewResourceId, List<combing_child> objects){
        super(content,textViewResourceId,objects);
        resourceId=textViewResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        combing_child cd= getItem(position);

        View view;
        ViewHolder viewHolder;
        if(convertView==null){
            view= LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
            viewHolder = new ViewHolder();
            viewHolder.child_count=view.findViewById(R.id.CombingChildCount);
            viewHolder.child_name=view.findViewById(R.id.CombingChildRelation);
            viewHolder.child_character=view.findViewById(R.id.CombingChildRelationChar);
            view.setTag(viewHolder);
        }else {
            view=convertView;
            viewHolder=(ViewHolder) view.getTag();
        }

        viewHolder.child_count.setText(cd.getCount());
        viewHolder.child_count.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.lightpink));
        viewHolder.child_name.setText(cd.getName());
        viewHolder.child_character.setText(cd.getCharacter());
        return view;
    }

    class ViewHolder{
        public TextView child_count;
        public TextView child_name;
        public TextView child_character;
    }

}
