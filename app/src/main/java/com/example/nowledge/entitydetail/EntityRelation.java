package com.example.nowledge.entitydetail;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.nowledge.EntityDetailActivity;
import com.example.nowledge.R;
import com.example.nowledge.character;
import com.example.nowledge.character_adapter;
import com.example.nowledge.child_relation;
import com.example.nowledge.child_relation_adapter;
import com.example.nowledge.super_relation;
import com.example.nowledge.super_relation_adapter;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class EntityRelation extends Fragment {

    private static final String ARG_PARAM1 = "content";
    private static final String ARG_PARAM2 = "course";

    private com.alibaba.fastjson.JSONArray mcontent=new com.alibaba.fastjson.JSONArray();
    private String mcourse;

    public EntityRelation() {
        // Required empty public constructor
    }

    public static EntityRelation newInstance(JSONArray content, String course) {
        EntityRelation fragment = new EntityRelation();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, content.toString());
        args.putString(ARG_PARAM2, course);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mcontent = JSON.parseArray(getArguments().getString(ARG_PARAM1));
            mcourse=getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.fragment_entity_character, container, false);
        ListView listViewSuperCont = (ListView) view.findViewById(R.id.listSuperContentAtDetail);
        ListView listViewChildCont = (ListView) view.findViewById(R.id.listChildContentAtDetail);
        List<super_relation>  super_relation_list = new ArrayList<>();
        List<child_relation>  child_relation_list = new ArrayList<>();
        if(mcontent!=null){
            for (int i = 0,j = 0; i < mcontent.size(); i++) {
                if (j > 10) {
                    break;
                }
                JSONObject obj = mcontent.getJSONObject(i);
                String type = obj.getString("predicate_label");
                if (obj.containsKey("subject_label")) {
                    String detail = obj.getString("subject_label");
                    j++;
                    super_relation_list.add(new super_relation(type,detail,mcourse));
                }
            }
        }
        super_relation_adapter adapterSC = new super_relation_adapter(getContext(),R.layout.super_relation_item,super_relation_list);
        listViewSuperCont.setAdapter(adapterSC);

        if(mcontent!=null){
            for (int i = 0,j = 0; i < mcontent.size(); i++) {
                if (j > 10) {
                    break;
                }
                JSONObject obj = mcontent.getJSONObject(i);
                String type = obj.getString("predicate_label");
                if (obj.containsKey("object_label")) {
                    String detail = obj.getString("object_label");
                    j++;
                    child_relation_list.add(new child_relation(type,detail,mcourse));
                }
            }
        }
        child_relation_adapter adapterCC = new child_relation_adapter(getContext(),R.layout.child_relation_item,child_relation_list);
        listViewChildCont.setAdapter(adapterCC);
        return view;
    }
}