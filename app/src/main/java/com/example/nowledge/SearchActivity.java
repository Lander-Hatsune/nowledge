package com.example.nowledge;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.nowledge.data.Course;
import com.example.nowledge.data.Singleton;
import com.example.nowledge.data.Uris;
import com.example.nowledge.data.User;
import com.example.nowledge.utils.EntityAdapter;
import com.example.nowledge.utils.EntityShort;
import com.google.android.material.chip.Chip;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class SearchActivity extends AppCompatActivity {
    private String id = User.getID();
    private String[] courses;
    private List<String> courseNames;
    private List<EntityShort> ett_list;
    private EntityAdapter adapter;
    private Chip chip_category, chip_reverse, chip_prime;
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
        setTitle("搜索列表");
        id = User.getID();
        updateId();
        courses = Course.getCourses();
        courseNames = Course.getCourseNames();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        chip_category = findViewById(R.id.chip_category);
        chip_reverse = findViewById(R.id.chip_reverse);
        chip_prime = findViewById(R.id.chip_prime);

        Bundle bundle = this.getIntent().getExtras();
        String key = bundle.getString("key");
        String course = bundle.getString("course");
        Integer pos = Course.getCourseID(course);
        Log.d("pos", pos.toString());
        String courseName = courseNames.get(pos);

        Button searchBackButton = findViewById(R.id.searchBackButton);
        TextView textView = findViewById(R.id.textViewKey);
        textView.setText(key);
        TextView textViewC = findViewById(R.id.textViewCourse);
        textViewC.setText(courseName);

        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentBackSearch = new Intent
                        (SearchActivity.this, SearchTransferActivity.class);
                intentBackSearch.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intentBackSearch.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intentBackSearch);
            }
        };

        textView.setOnClickListener(clickListener);
        textViewC.setOnClickListener(clickListener);
        searchBackButton.setOnClickListener(clickListener);

        TextView infoText = findViewById(R.id.textViewResultInfo);

        RecyclerView recyclerView = findViewById(R.id.recyclerViewSearchResult);

        String[] courses = Course.getCourses();
        List<String> courseNames = Course.getCourseNames();


        RequestQueue reqQue = Singleton.getInstance
                (getApplicationContext()).getRequestQueue();

        String params = "?";
        params += "course=" + course;
        params += "&searchKey=" + key;
        params += "&id=" + id;

        ett_list = new ArrayList<>();

        String url = Uris.getSearch() + params;

        Log.d("url", params);

        JsonObjectRequest req = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        JSONArray list = null;
                        try {
                            Log.d("resp:", response.toString());
                            Log.d("data", response.get("data").toString());
                            list = (JSONArray) response.get("data");
                            for (int i = 0; i < list.length(); i++) {
                                JSONObject obj = list.getJSONObject(i);
                                String label = obj.get("label").toString();
                                String category = obj.get("category").toString();
                                String uri = obj.get("uri").toString();
                                ett_list.add(new EntityShort(label, category, course));
                            }
                            if (ett_list.size() > 0)
                                ett_list.sort(EntityShort.getComparator(false, false));
                            infoText.setText(infoText.getText().toString() + list.length() + "条结果");
                            LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                            recyclerView.setLayoutManager(layoutManager);
                            adapter = new EntityAdapter(ett_list, "list");
                            adapter.setOnItemClickListener(new EntityAdapter.OnItemClickListener() {
                                @Override
                                public void onItemCLick(String course, String name) {
                                    Intent intentDetail = new Intent(SearchActivity.this, NewEntityActivity.class);
                                    intentDetail.putExtra("course", course);
                                    intentDetail.putExtra("name", name);
                                    startActivity(intentDetail);
                                }
                            });
                            recyclerView.setAdapter(adapter);

                        } catch (JSONException e) {}
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
        reqQue.add(req);

        chip_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ett_list.size() > 0) {
                    Comparator<EntityShort> comparator =
                            EntityShort.getComparator(chip_category.isChecked(), chip_reverse.isChecked());
                    ett_list.sort(comparator);
                    adapter.notifyDataSetChanged();
                }
            }
        });

        chip_reverse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ett_list.size() > 0) {
                    Comparator<EntityShort> comparator =
                            EntityShort.getComparator(chip_category.isChecked(), chip_reverse.isChecked());
                    ett_list.sort(comparator);
                    adapter.notifyDataSetChanged();
                }
            }
        });

        chip_prime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (chip_prime.isChecked()) {
                    adapter = new EntityAdapter(ett_list, "prime");
                } else {
                    adapter = new EntityAdapter(ett_list, "list");
                }
                adapter.setOnItemClickListener(new EntityAdapter.OnItemClickListener() {
                    @Override
                    public void onItemCLick(String course, String name) {
                        Intent intentDetail = new Intent(SearchActivity.this, NewEntityActivity.class);
                        intentDetail.putExtra("course", course);
                        intentDetail.putExtra("name", name);
                        startActivity(intentDetail);
                    }
                });
                recyclerView.setAdapter(adapter);
            }
        });
    }


}