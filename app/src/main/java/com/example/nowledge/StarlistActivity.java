package com.example.nowledge;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.ActionMenuItemView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class StarlistActivity extends AppCompatActivity {

    private ListView lv;
    private List<String> starlistStr, starCourseStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        setTitle("我的历史");


    }

    @Override
    protected void onStart() {
        super.onStart();
        setContentView(R.layout.activity_starlist);
        setTitle("我的收藏");

        lv = findViewById(R.id.starListView);
        starlistStr = new ArrayList<>();
        starCourseStr = new ArrayList<>();
        RequestQueue reqQue = Singleton.getInstance(getApplicationContext()).getRequestQueue();
        String url = Uris.getStarlist() + "?username=" + User.getUsername();
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            starlistStr = new ArrayList<>();
                            starCourseStr = new ArrayList<>();
                            String code = response.getString("id");
                            if (!code.equals("0")) {
                                return;
                            }
                            JSONArray starlist = response.getJSONArray("payload");
                            for (int i = starlist.length() - 1; i >= 0 ; i--) {
                                JSONObject star = starlist.getJSONObject(i);
                                String name = star.getString("name"), course = star.getString("course");
                                starlistStr.add(name);
                                starCourseStr.add(course);
                            }
                            ArrayAdapter<String> adapter = new ArrayAdapter<>
                                    (getApplicationContext(), R.layout.entity_short_item, starlistStr);
                            lv.setAdapter(adapter);

                        } catch (JSONException e) {
                            Log.e("Starlist error", e.toString());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("starList error", error.toString());


            }
        });
        reqQue.add(req);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d("click", "number " + i);
                Intent intentDetail = new Intent(StarlistActivity.this, NewEntityActivity.class);
                intentDetail.putExtra("name", starlistStr.get(i));
                intentDetail.putExtra("course", starCourseStr.get(i));
                startActivity(intentDetail);
            }
        });
    }
}