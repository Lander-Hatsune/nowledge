package com.example.nowledge.utils;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;


import com.example.nowledge.NewEntityActivity;
import com.example.nowledge.R;

import java.util.List;

public class super_relation_adapter extends ArrayAdapter<super_relation> {
    private int resourceId;
    private boolean ifConnected;

    public super_relation_adapter(Context context,int textViewResourceId,List<super_relation> objects, boolean _ifC){
        super(context,textViewResourceId,objects);
        resourceId=textViewResourceId;
        ifConnected = _ifC;
    }

    @Override
    public View getView(int position, View convertView,ViewGroup parent){
        super_relation sr=getItem(position);

        View view;
        ViewHolder viewHolder;
        if(convertView==null){
            view=LayoutInflater.from(getContext()).inflate(resourceId,parent,false);

            viewHolder=new ViewHolder();
            viewHolder.super_type=view.findViewById(R.id.super_relation_type);
            viewHolder.super_detail=view.findViewById(R.id.super_relation_detail);
            viewHolder.super_search=view.findViewById(R.id.super_relation_search);
            view.setTag(viewHolder);
        }else{
            view=convertView;
            viewHolder=(ViewHolder) view.getTag();
        }

        viewHolder.super_type.setText(sr.getType());
        viewHolder.super_detail.setText(sr.getDetail());
        viewHolder.super_search.setTag(position);
        if (ifConnected) {
            viewHolder.super_search.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d("click",sr.getDetail());
                    Intent intent_next_item=new Intent(getContext(), NewEntityActivity.class);
                    intent_next_item.putExtra("name",sr.getDetail());
                    intent_next_item.putExtra("course",sr.getCourse());
                    view.getContext().startActivity(intent_next_item);
                }
            });
        } else {
            viewHolder.super_search.setClickable(false);
        }
        return view;
    }

    class ViewHolder{
        public TextView super_type;
        public TextView super_detail;
        public Button super_search;
    }
}
