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
import com.example.nowledge.NewEntityActivity;
import com.example.nowledge.R;
import com.example.nowledge.character;
import com.example.nowledge.character_adapter;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class EntityCharacter extends Fragment {

    private static final String ARG_PARAM = "character";

    private com.alibaba.fastjson.JSONArray mcharacter=new com.alibaba.fastjson.JSONArray();

    public EntityCharacter() {
        // Required empty public constructor
    }

    public static EntityCharacter newInstance(JSONArray character) {
        EntityCharacter fragment = new EntityCharacter();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM, character.toString());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mcharacter = JSON.parseArray(getArguments().getString(ARG_PARAM));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.fragment_entity_character, container, false);
        ListView listViewProp = (ListView) view.findViewById(R.id.listAtDetail);
        List<character> character_list = new ArrayList<>();
        if(mcharacter!=null){
            for (int i = 0; i < mcharacter.size(); i++) {
                if (i > 10) {
                    break;
                }
                JSONObject obj = mcharacter.getJSONObject(i);
                String predicate = obj.getString("predicateLabel");
                String object = obj.getString("object");
                character_list.add(new character(predicate+":","  "+object));
            }
        }
        character_adapter adapter = new character_adapter(getContext(),R.layout.character_item,character_list);
        listViewProp.setAdapter(adapter);
        return view;
    }
}