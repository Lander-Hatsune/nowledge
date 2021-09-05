package com.example.nowledge;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;


import java.util.List;

public class child_relation_adapter extends ArrayAdapter<child_relation> {
    private int resourceId;

    public child_relation_adapter(Context context,int textViewResourceId,List<child_relation> objects){
        super(context,textViewResourceId,objects);
        resourceId=textViewResourceId;
    }

    @Override
    public View getView(int position, View convertView,ViewGroup parent){
        child_relation cr=getItem(position);

        View view;
        ViewHolder viewHolder;
        if(convertView==null){
            view=LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
            viewHolder=new ViewHolder();
            viewHolder.child_type=view.findViewById(R.id.child_relation_type);
            viewHolder.child_detail=view.findViewById(R.id.child_relation_detail);
            viewHolder.child_search=view.findViewById(R.id.child_relation_search);
            view.setTag(viewHolder);
        }else{
            view=convertView;
            viewHolder=(ViewHolder) view.getTag();
        }

        viewHolder.child_type.setText(cr.getType());
        viewHolder.child_detail.setText(cr.getDetail());
        viewHolder.child_search.setTag(position);
        viewHolder.child_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("click",cr.getDetail());
                Intent intent_next_item=new Intent(getContext(),EntityDetailActivity.class);
                intent_next_item.putExtra("name",cr.getDetail());
                intent_next_item.putExtra("course",cr.getCourse());
                view.getContext().startActivity(intent_next_item);
            }
        });
        return view;
    }

    class ViewHolder{
        public TextView child_type;
        public TextView child_detail;
        public Button child_search;
    }
}