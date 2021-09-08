package com.example.nowledge.entitydetail;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.appcompat.view.menu.ActionMenuItemView;
import androidx.appcompat.widget.ActionMenuView;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.nowledge.NewEntityActivity;
import com.example.nowledge.R;
import com.example.nowledge.sqlite.UtilData;
import com.example.nowledge.utils.character;
import com.example.nowledge.utils.character_adapter;
import com.example.nowledge.utils.child_relation;
import com.example.nowledge.utils.child_relation_adapter;
import com.example.nowledge.data.Singleton;
import com.example.nowledge.data.Uris;
import com.example.nowledge.data.User;
import com.example.nowledge.utils.super_relation;
import com.example.nowledge.utils.super_relation_adapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class EntityCharacter extends Fragment {

    private static final String ARG_PARAM = "character";

    private String course, name;
    private String id = User.getID();
    private Boolean starred = false;
    private ListView listViewProp, listViewSuperCont, listViewChildCont;

    private final int numC = 20, numP = 50;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.fragment_entity_character, container, false);
        id = User.getID();


        Bundle bundle = getActivity().getIntent().getExtras();
        name = bundle.getString("name");
        course = bundle.getString("course");

        UtilData uData = new UtilData(getContext());
        String[] dataSql = uData.inquireData(name, course);
        uData.getClose();

        listViewProp = view.findViewById(R.id.FlistAtDetail);
        listViewSuperCont = view.findViewById(R.id.FlistSuperContentAtDetail);
        listViewChildCont = view.findViewById(R.id.FlistChildContentAtDetail);

        if (dataSql[3].equals("get")) {
            LoadFromCache(dataSql);
        }
        LoadFromRequest(true);

        return view;

    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
    }

    private void LoadFromCache(String[] data) {
        List<character> character_list = new ArrayList<>();
        List<super_relation>  super_relation_list = new ArrayList<>();
        List<child_relation>  child_relation_list = new ArrayList<>();
        try {

            com.alibaba.fastjson.JSONArray properties = com.alibaba.fastjson.JSONArray.parseArray(data[0]);
            com.alibaba.fastjson.JSONArray subject_content = com.alibaba.fastjson.JSONArray.parseArray(data[1]);
            com.alibaba.fastjson.JSONArray object_content = com.alibaba.fastjson.JSONArray.parseArray(data[1]);

            for (int i = 0; i < properties.size(); i++) {
                if (i > numP) {
                    break;
                }
                com.alibaba.fastjson.JSONObject obj = properties.getJSONObject(i);
                String predicate = obj.getString("predicateLabel");
                String object = obj.getString("object");
                character_list.add(new character(predicate+":","  "+object));
            }
            character_adapter adapter = new character_adapter(getActivity(),R.layout.character_item,character_list);
            listViewProp.setAdapter(adapter);


            for (int i = 0,j = 0; i < subject_content.size(); i++) {
                if (j > numC) {
                    break;
                }
                com.alibaba.fastjson.JSONObject obj = subject_content.getJSONObject(i);
                String type = obj.getString("type");
                String detail = obj.getString("subject_label");
                j++;
                super_relation_list.add(new super_relation(type,detail,course));
            }
            super_relation_adapter adapterSC = new super_relation_adapter(getActivity(),R.layout.super_relation_item,super_relation_list, false);
            listViewSuperCont.setAdapter(adapterSC);

            for (int i = 0,j = 0; i < object_content.size(); i++) {
                if (j > numC) {
                    break;
                }
                com.alibaba.fastjson.JSONObject obj = object_content.getJSONObject(i);
                String type = obj.getString("type");
                String detail = obj.getString("subject_label");
                j++;
                child_relation_list.add(new child_relation(type,detail,course));
            }
            child_relation_adapter adapterCC = new child_relation_adapter(getActivity(),R.layout.child_relation_item,child_relation_list, false);
            listViewChildCont.setAdapter(adapterCC);
            Toast.makeText(getActivity().getApplicationContext(), "从缓存中读取数据", Toast.LENGTH_SHORT).show();

        } catch (com.alibaba.fastjson.JSONException e) {
            Log.e("Error parsing detail from cache", e.toString());
        }
    }

    private void LoadFromRequest(boolean first) {
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
                            String code = "";
                            code = response.getString("code");
                            if (!code.equals("0")) {
                                Log.e("response code", code);
                                if (first)
                                    updateId();
                                return;
                            }
                            org.json.JSONObject dataobj = response.getJSONObject("data");

                            Log.d("dataobj " + dataobj.toString().length(), dataobj.toString());

                            JSONArray properties = (JSONArray) dataobj.get("property");
                            JSONArray contents = (JSONArray) dataobj.get("content");

                            JSONArray db_property = new JSONArray(),
                                    db_subject_content = new JSONArray(),
                                    db_object_content = new JSONArray();

                            for (int i = 0; i < properties.length(); i++) {
                                if (i > numP) {
                                    break;
                                }
                                org.json.JSONObject obj = properties.getJSONObject(i);
                                String predicate = obj.getString("predicateLabel");
                                String object = obj.getString("object");
                                if (object.contains("http")) continue;
                                character_list.add(new character(predicate+":","  "+object));

                                JSONObject tmp_obj = new JSONObject();
                                tmp_obj.put("predicateLabel", predicate);
                                tmp_obj.put("object", object);
                                db_property.put(tmp_obj);
                            }
                            character_adapter adapter = new character_adapter(getActivity(),R.layout.character_item,character_list);
                            listViewProp.setAdapter(adapter);


                            for (int i = 0,j = 0; i < contents.length(); i++) {
                                if (j > numC) {
                                    break;
                                }
                                org.json.JSONObject obj = contents.getJSONObject(i);
                                String type = obj.getString("predicate_label");
                                if (obj.has("subject_label")) {
                                    String detail = obj.getString("subject_label");
                                    j++;
                                    super_relation_list.add(new super_relation(type,detail,course));

                                    JSONObject tmp_obj = new JSONObject();
                                    tmp_obj.put("type", type);
                                    tmp_obj.put("detail", detail);
                                    db_subject_content.put(tmp_obj);
                                }
                            }
                            super_relation_adapter adapterSC = new super_relation_adapter(getActivity(),R.layout.super_relation_item,super_relation_list, true);
                            listViewSuperCont.setAdapter(adapterSC);

                            for (int i = 0,j = 0; i < contents.length(); i++) {
                                if (j > numC) {
                                    break;
                                }
                                org.json.JSONObject obj = contents.getJSONObject(i);
                                String type = obj.getString("predicate_label");
                                if (obj.has("object_label")) {
                                    String detail = obj.getString("object_label");
                                    j++;
                                    child_relation_list.add(new child_relation(type,detail,course));

                                    JSONObject tmp_obj = new JSONObject();
                                    tmp_obj.put("type", type);
                                    tmp_obj.put("detail", detail);
                                    db_object_content.put(tmp_obj);
                                }
                            }
                            child_relation_adapter adapterCC = new child_relation_adapter(getActivity(),R.layout.child_relation_item,child_relation_list, true);
                            listViewChildCont.setAdapter(adapterCC);

//                          write to sqlite
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    String propertyStr = db_property.toString(),
                                            subStr = db_subject_content.toString(),
                                            objStr = db_object_content.toString();
                                    UtilData uData = new UtilData(getContext());
                                    Log.d("entity properties", propertyStr);
                                    Log.d("entity subject", propertyStr);
                                    Log.d("entity object", propertyStr);
                                    uData.addData(name, course, propertyStr, subStr, objStr);
                                    uData.getClose();
                                }
                            }).start();


                        } catch (JSONException e) {
                            Log.e("Error parsing detail obj from request", e.toString());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        reqQue.add(req);

    }

    protected void updateId() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                RequestQueue reqQue = Singleton.getInstance
                        (getContext()).getRequestQueue();
                JSONObject obj = null;
                try {
                    obj = new JSONObject();
                    obj.put("username", "0");
                    obj.put("password", "0");
                } catch (JSONException e) {
                    Log.e("UpdateId error:", e.toString());
                }
                Log.d("UpdateId obj", obj.toString());
                JsonObjectRequest req = new JsonObjectRequest
                        (Request.Method.POST, Uris.getLogin(),
                                obj, new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Log.i("Login request success.", "");
                                String msg = "Unknown Error";
                                String code = "";
                                try {
                                    msg = response.getString("msg");
                                    code = response.getString("id");
                                } catch (JSONException e) {
                                    Log.e("Login request msg/id error", e.toString());
                                }
                                if (!(code.equals("-1") || code.equals("-2"))) {
                                    Log.d("logged in, id", code);
                                    id = code;
                                    User.setID(id);
                                    LoadFromRequest(false);
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.e("Login error:", error.toString());
                            }
                        });
                Log.d("Request:", req.toString());
                reqQue.add(req);
            }
        }).start();
    }

}