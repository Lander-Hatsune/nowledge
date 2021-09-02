package com.example.nowledge;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.nowledge.data.Singleton;
import com.example.nowledge.data.Uris;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class EntityDetailActivity extends AppCompatActivity {
    private String id = Uris.id;

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
        updateId();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entity_detail);

        Bundle bundle = this.getIntent().getExtras();
        String name = bundle.getString("name");
        String course = bundle.getString("course");

        this.setTitle(name);

        ListView listView = findViewById(R.id.listAtDetail);


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
                        List<String> propertiesStr = new ArrayList<>();
                        try {
                            JSONObject dataobj = response.getJSONObject("data");
                            Log.d("dataobj", dataobj.toString());
                            JSONArray properties = (JSONArray) dataobj.get("property");
                            for (int i = 0; i < properties.length(); i++) {
                                if (i > 10) {
                                    break;
                                }
                                JSONObject obj = properties.getJSONObject(i);
                                String predicate = obj.get("predicateLabel").toString();
                                String object = obj.get("object").toString();
                                propertiesStr.add(predicate + ": " + object);
                            }
                            ArrayAdapter<String> adapter = new ArrayAdapter<String>
                                    (getApplicationContext(), R.layout.attr_item, propertiesStr);
                            listView.setAdapter(adapter);
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
    }
}