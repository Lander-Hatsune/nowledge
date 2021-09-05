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

public class HistoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        setTitle("我的历史");


    }

    @Override
    protected void onStart() {
        super.onStart();
        ListView lv = findViewById(R.id.historyListView);
        List<String> historyStr = new ArrayList<>();
        List<String> historyCourseStr = new ArrayList<>();
        RequestQueue reqQue = Singleton.getInstance(getApplicationContext()).getRequestQueue();
        String url = Uris.getHistorylist() + "?username=" + User.getUsername();
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String code = response.getString("id");
                            if (!code.equals("0")) {
                                return;
                            }
                            JSONArray historylist = response.getJSONArray("payload");
                            for (int i = 0; i < historylist.length(); i++) {
                                JSONObject star = historylist.getJSONObject(i);
                                historyStr.add(star.getString("name"));
                                historyCourseStr.add(star.getString("course"));
                            }
                            ArrayAdapter<String> adapter = new ArrayAdapter<>
                                    (getApplicationContext(), R.layout.entity_short_item, historyStr);
                            lv.setAdapter(adapter);

                        } catch (JSONException e) {
                            Log.e("Hislist error", e.toString());
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
                Intent intentDetail = new Intent(HistoryActivity.this, EntityDetailActivity.class);
                intentDetail.putExtra("name", historyStr.get(i));
                intentDetail.putExtra("course", historyCourseStr.get(i));
                startActivity(intentDetail);
            }
        });
    }
}