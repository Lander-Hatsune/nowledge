package com.example.nowledge;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
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
import com.example.nowledge.sqlite.UtilData;
import com.example.nowledge.sqlite.UtilHistory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class HistoryActivity extends AppCompatActivity {

    private List<String> historyStr, historyCourseStr;
    private ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        setTitle("我的历史");


    }

    @Override
    protected void onStart() {
        super.onStart();
        lv = findViewById(R.id.historyListView);
        historyStr = new ArrayList<>();
        historyCourseStr = new ArrayList<>();
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
                            UtilHistory udata = new UtilHistory(getApplicationContext());
                            udata.clearHistory();
                            historyStr.clear(); historyCourseStr.clear();
                            for (int i = 0; i < historylist.length(); i++) {
                                JSONObject star = historylist.getJSONObject(i);
                                String name = star.getString("name"), course = star.getString("course");
                                historyStr.add(name);
                                historyCourseStr.add(course);
                                udata.addHistory(name, course);
                            }
                            ArrayAdapter<String> adapter = new ArrayAdapter<>
                                    (getApplicationContext(), R.layout.entity_short_item, historyStr);
                            lv.setAdapter(adapter);
                            udata.getClose();

                        } catch (JSONException e) {
                            Log.e("Hislist error", e.toString());

                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("history error", error.toString());

            }
        });
        reqQue.add(req);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d("click", "number " + i);
                Intent intentDetail = new Intent(HistoryActivity.this, NewEntityActivity.class);
                intentDetail.putExtra("name", historyStr.get(i));
                intentDetail.putExtra("course", historyCourseStr.get(i));
                startActivity(intentDetail);
            }
        });

        UtilHistory uData = new UtilHistory(getApplicationContext());
        List<Pair<String, String>> hisList = uData.getHisList();
        uData.getClose();

        Iterator<Pair<String, String>> iter = hisList.iterator();
        while(iter.hasNext()) {
            Pair<String, String> tmp = iter.next();
            String course = tmp.second, name = tmp.first;
            historyStr.add(name);
            historyCourseStr.add(course);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>
                (getApplicationContext(), R.layout.entity_short_item, historyStr);
        lv.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.history_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.actionDelHistory:
                Log.d("history list", "click clear button");
                String url = Uris.getClearHistory();
                RequestQueue reqQue = Singleton.getInstance(getApplicationContext()).getRequestQueue();

                JSONObject obj = new JSONObject();
                try {
                    obj.put("username", User.getUsername());
                } catch (JSONException e) {
                    Log.e("History", e.toString());
                }

                JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, url, obj,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    String code = response.getString("id");
                                    String msg = response.getString("msg");
                                    if (!code.equals("0")) {
                                        return;
                                    }
                                    UtilHistory utilHistory = new UtilHistory(getApplicationContext());
                                    utilHistory.clearHistory();
                                    utilHistory.getClose();

                                    Toast.makeText(getApplicationContext(), "清空历史记录成功", Toast.LENGTH_SHORT).show();
                                    historyCourseStr.clear(); historyStr.clear();
                                    ArrayAdapter<String> adapter = new ArrayAdapter<>
                                            (getApplicationContext(), R.layout.entity_short_item, historyStr);
                                    lv.setAdapter(adapter);
                                } catch (JSONException e) {
                                    Log.e("response error", e.toString());
                                }

                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
                reqQue.add(req);

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}