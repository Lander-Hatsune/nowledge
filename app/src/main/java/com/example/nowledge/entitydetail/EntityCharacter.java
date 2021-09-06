package com.example.nowledge.entitydetail;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.appcompat.view.menu.ActionMenuItemView;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.nowledge.EntityDetailActivity;
import com.example.nowledge.NewEntityActivity;
import com.example.nowledge.R;
import com.example.nowledge.character;
import com.example.nowledge.character_adapter;
import com.example.nowledge.child_relation;
import com.example.nowledge.child_relation_adapter;
import com.example.nowledge.data.Singleton;
import com.example.nowledge.data.Uris;
import com.example.nowledge.data.User;
import com.example.nowledge.super_relation;
import com.example.nowledge.super_relation_adapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

public class EntityCharacter extends Fragment {

    private static final String ARG_PARAM = "character";

    private String course, name;
    private String id = User.getID();
    private Boolean starred = false;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.fragment_entity_character, container, false);
        id = User.getID();


        Bundle bundle = getActivity().getIntent().getExtras();
        name = bundle.getString("name");
        course = bundle.getString("course");



        ListView listViewProp = view.findViewById(R.id.FlistAtDetail);
        ListView listViewSuperCont = view.findViewById(R.id.FlistSuperContentAtDetail);
        ListView listViewChildCont = view.findViewById(R.id.FlistChildContentAtDetail);

        String url = Uris.getDetail() + "?";
        url += "name=" + name;
        url += "&course=" + course;
        url += "&id=" + id;

        Log.d("detailurl:", url);

        RequestQueue reqQue = Singleton.getInstance(getActivity().getApplicationContext()).getRequestQueue();

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<org.json.JSONObject>() {
                    @Override
                    public void onResponse(org.json.JSONObject response) {
                        List<character> character_list = new ArrayList<>();
                        List<super_relation>  super_relation_list = new ArrayList<>();
                        List<child_relation>  child_relation_list = new ArrayList<>();
                        try {
                            org.json.JSONObject dataobj = response.getJSONObject("data");
                            Log.d("dataobj", dataobj.toString());

                            JSONArray properties = (JSONArray) dataobj.get("property");
                            for (int i = 0; i < properties.length(); i++) {
                                if (i > 10) {
                                    break;
                                }
                                org.json.JSONObject obj = properties.getJSONObject(i);
                                String predicate = obj.getString("predicateLabel");
                                String object = obj.getString("object");
                                character_list.add(new character(predicate+":","  "+object));
                            }
                            character_adapter adapter = new character_adapter(getActivity(),R.layout.character_item,character_list);
                            listViewProp.setAdapter(adapter);

                            JSONArray contents = (JSONArray) dataobj.get("content");
                            for (int i = 0,j = 0; i < contents.length(); i++) {
                                if (j > 10) {
                                    break;
                                }
                                org.json.JSONObject obj = contents.getJSONObject(i);
                                String type = obj.getString("predicate_label");
                                if (obj.has("subject_label")) {
                                    String detail = obj.getString("subject_label");
                                    j++;
                                    super_relation_list.add(new super_relation(type,detail,course));
                                }
                            }
                            super_relation_adapter adapterSC = new super_relation_adapter(getActivity(),R.layout.super_relation_item,super_relation_list);
                            listViewSuperCont.setAdapter(adapterSC);

                            for (int i = 0,j = 0; i < contents.length(); i++) {
                                if (j > 10) {
                                    break;
                                }
                                org.json.JSONObject obj = contents.getJSONObject(i);
                                String type = obj.getString("predicate_label");
                                if (obj.has("object_label")) {
                                    String detail = obj.getString("object_label");
                                    j++;
                                    child_relation_list.add(new child_relation(type,detail,course));
                                }
                            }
                            child_relation_adapter adapterCC = new child_relation_adapter(getActivity(),R.layout.child_relation_item,child_relation_list);
                            listViewChildCont.setAdapter(adapterCC);

                        } catch (JSONException e) {
                            Log.e("Error parsing detail obj", e.toString());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        reqQue.add(req);



        return view;

    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setHasOptionsMenu(true);
    }



}