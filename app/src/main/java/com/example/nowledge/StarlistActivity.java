package com.example.nowledge;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

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

public class StarlistActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starlist);
        setTitle("我的收藏");

        ListView lv = findViewById(R.id.starListView);
        List<String> starlistStr = new ArrayList<>();
        List<String> starCourseStr = new ArrayList<>();
        RequestQueue reqQue = Singleton.getInstance(getApplicationContext()).getRequestQueue();
        String url = Uris.getStarlist() + "?username=" + User.getUsername();
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, url, null,
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
                                starlistStr.add(star.getString("name"));
                                starCourseStr.add(star.getString("course"));
                            }
                            ArrayAdapter<String> adapter = new ArrayAdapter<>
                                    (getApplicationContext(), R.layout.entity_item_list, starlistStr);
                            lv.setAdapter(adapter);

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

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d("click", "number " + i);
                Intent intentDetail = new Intent(StarlistActivity.this, EntityDetailActivity.class);
                intentDetail.putExtra("name", starlistStr.get(i));
                intentDetail.putExtra("course", starCourseStr.get(i));
                startActivity(intentDetail);
            }
        });
    }
}