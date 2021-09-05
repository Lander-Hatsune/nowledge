package com.example.nowledge;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.ActionMenuItemView;
import androidx.core.content.res.ResourcesCompat;

import android.content.ClipData;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.nowledge.data.Singleton;
import com.example.nowledge.data.Uris;
import com.example.nowledge.data.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class EntityDetailActivity extends AppCompatActivity {
    private String id = User.getID();
    private Boolean starred = false;
    private String name;
    private String course;

    protected void updateId() {
        RequestQueue reqQue = Singleton.getInstance
                (getApplicationContext()).getRequestQueue();
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        id = User.getID();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entity_detail);

        Bundle bundle = this.getIntent().getExtras();
        name = bundle.getString("name");
        course = bundle.getString("course");

        this.setTitle(name);

        ListView listViewProp = findViewById(R.id.listAtDetail);
        ListView listViewSuperCont = findViewById(R.id.listSuperContentAtDetail);
        ListView listViewChildCont = findViewById(R.id.listChildContentAtDetail);

        String url = Uris.getDetail() + "?";
        url += "name=" + name;
        url += "&course=" + course;
        url += "&id=" + id;

        Log.d("detailurl:", url);

        RequestQueue reqQue = Singleton.getInstance(getApplicationContext()).getRequestQueue();

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        List<character> character_list = new ArrayList<>();
                        List<super_relation>  super_relation_list = new ArrayList<>();
                        List<child_relation>  child_relation_list = new ArrayList<>();
                        try {
                            JSONObject dataobj = response.getJSONObject("data");
                            Log.d("dataobj", dataobj.toString());

                            JSONArray properties = (JSONArray) dataobj.get("property");
                            for (int i = 0; i < properties.length(); i++) {
                                if (i > 10) {
                                    break;
                                }
                                JSONObject obj = properties.getJSONObject(i);
                                String predicate = obj.getString("predicateLabel");
                                String object = obj.getString("object");
                                character_list.add(new character(predicate+":","  "+object));
                            }
                            character_adapter adapter = new character_adapter(EntityDetailActivity.this,R.layout.character_item,character_list);
                            listViewProp.setAdapter(adapter);

                            JSONArray contents = (JSONArray) dataobj.get("content");
                            for (int i = 0,j = 0; i < contents.length(); i++) {
                                if (j > 10) {
                                    break;
                                }
                                JSONObject obj = contents.getJSONObject(i);
                                String type = obj.getString("predicate_label");
                                if (obj.has("subject_label")) {
                                    String detail = obj.getString("subject_label");
                                    j++;
                                    super_relation_list.add(new super_relation(type,detail,course));
                                }
                            }
                            super_relation_adapter adapterSC = new super_relation_adapter(EntityDetailActivity.this,R.layout.super_relation_item,super_relation_list);
                            listViewSuperCont.setAdapter(adapterSC);

                            for (int i = 0,j = 0; i < contents.length(); i++) {
                                if (j > 10) {
                                    break;
                                }
                                JSONObject obj = contents.getJSONObject(i);
                                String type = obj.getString("predicate_label");
                                if (obj.has("object_label")) {
                                    String detail = obj.getString("object_label");
                                    j++;
                                    child_relation_list.add(new child_relation(type,detail,course));
                                }
                            }
                            child_relation_adapter adapterCC = new child_relation_adapter(EntityDetailActivity.this,R.layout.child_relation_item,child_relation_list);
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

        // star
        url = Uris.getStarlist() + "?username=" + User.getUsername();
        req = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String code = response.getString("id");
                            if (!code.equals("0")) {
                                return;
                            }
                            JSONArray starlist = response.getJSONArray("payload");
                            for (int i = 0; i < starlist.length(); i++) {
                                JSONObject star = starlist.getJSONObject(i);
                                if (star.getString("course").equals(course) &&
                                        star.getString("name").equals(name)) {
                                    starred = true;
                                    ActionMenuItemView starmark = findViewById(R.id.actionStar);
                                    MenuItem item = starmark.getItemData();
                                    item.setIcon(R.drawable.star);
                                }
                            }
                        } catch (JSONException e) {
                            Log.e("Starlist error", e.toString());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        reqQue.add(req);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.actionStar:
                String url;
                if (starred) {
                    url = Uris.getUnstar();
                } else {
                    url = Uris.getStar();
                }
                RequestQueue reqQue = Singleton.getInstance(getApplicationContext()).getRequestQueue();

                JSONObject obj = new JSONObject();
                try {
                    obj.put("username", User.getUsername());
                    obj.put("course", course);
                    obj.put("name", name);
                } catch (JSONException e) {
                    Log.e("Star/Unstar obj err", e.toString());
                }

                JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, url, obj,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    if (response.getString("id").equals("0")) {
                                        starred = !starred;
                                        ActionMenuItemView starmark = findViewById(R.id.actionStar);
                                        MenuItem item = starmark.getItemData();
                                        if (starred) {
                                            item.setIcon(R.drawable.star);
                                        } else {
                                            item.setIcon(R.drawable.star_outline);
                                        }
                                    }
                                    Toast.makeText(EntityDetailActivity.this,
                                            response.getString("msg"),
                                            Toast.LENGTH_SHORT).show();
                                } catch (JSONException e) {
                                    Log.e("Error parsing star resp obj", e.toString());
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
                reqQue.add(req);

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}