package com.example.nowledge;

import android.annotation.SuppressLint;
import android.os.Bundle;

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
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.appcompat.view.menu.ActionMenuItemView;
import androidx.appcompat.widget.ActionMenuView;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nowledge.entitydetail.main.SectionsPagerAdapter;
import com.example.nowledge.databinding.ActivityNewEntityBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class NewEntityActivity extends AppCompatActivity {

    private ActivityNewEntityBinding binding;
    private String id = User.getID();
    private Boolean starred = false;
    private String name;
    private String course;
    private ActionMenuView actionMenuView;
    JSONArray properties = new JSONArray();
    JSONArray contents = new JSONArray();
    JSONArray questions = new JSONArray();

    protected void updateId() {
        new Thread(new Runnable() {
            @Override
            public void run() {
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
        }).start();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        id = User.getID();
        super.onCreate(savedInstanceState);
        Log.d ("entity detail", "onCreate!");

        binding = ActivityNewEntityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Bundle bundle = this.getIntent().getExtras();
        name = bundle.getString("name");
        course = bundle.getString("course");


        String urld = Uris.getDetail() + "?";
        urld += "name=" + name;
        urld += "&course=" + course;
        urld += "&id=" + id;

        String urlq = Uris.getQuestion() + "?";
        urlq += "uriName=" + name;
        urlq += "&id=" + id;

        Log.d("detailurl:", urld);
        Log.d("questionurl",urlq);

        TextView titleView = findViewById(R.id.entity_title);
        titleView.setText(name);

        actionMenuView = findViewById(R.id.entity_actionMenu);
        getMenuInflater().inflate(R.menu.detail_menu, actionMenuView.getMenu());
        actionMenuView.setOnMenuItemClickListener(new ActionMenuView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.actionStar:
                        String url;
                        if (starred) {
                            url = Uris.getUnstar();
                        } else {
                            url = Uris.getStar();
                        }
                        RequestQueue reqQue = Singleton.getInstance(getApplicationContext()).getRequestQueue();

                        org.json.JSONObject obj = new org.json.JSONObject();
                        try {
                            obj.put("username", User.getUsername());
                            obj.put("course", course);
                            obj.put("name", name);
                        } catch (JSONException e) {
                            Log.e("Star/Unstar obj err", e.toString());
                        }

                        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, url, obj,
                                new Response.Listener<org.json.JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        try {
                                            if (response.getString("id").equals("0")) {
                                                starred = !starred;
                                                ActionMenuItemView starmark = findViewById(R.id.actionStar);
                                                @SuppressLint("RestrictedApi") MenuItem item = starmark.getItemData();
                                                if (starred) {
                                                    item.setIcon(R.drawable.star);
                                                } else {
                                                    item.setIcon(R.drawable.star_outline);
                                                }
                                            }
                                            String text = starred ? "收藏成功" : "取消收藏成功";
                                            if (User.isLoggedin())
                                                Toast.makeText(NewEntityActivity.this,
                                                    text, Toast.LENGTH_SHORT).show();
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
                        return false;
                }
            }
        });

        RequestQueue reqQue = Singleton.getInstance(getApplicationContext()).getRequestQueue();

        // star
        String url = Uris.getStarlist() + "?username=" + User.getUsername();
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<org.json.JSONObject>() {
                    @Override
                    public void onResponse(org.json.JSONObject response) {
                        try {
                            String code = response.getString("id");
                            if (!code.equals("0")) {
                                return;
                            }
                            JSONArray starlist = response.getJSONArray("payload");
                            for (int i = 0; i < starlist.length(); i++) {
                                org.json.JSONObject star = starlist.getJSONObject(i);
                                if (star.getString("course").equals(course) &&
                                        star.getString("name").equals(name)) {
                                    starred = true;
                                    ActionMenuItemView starmark = findViewById(R.id.actionStar);
                                    @SuppressLint("RestrictedApi") MenuItem item = starmark.getItemData();
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

        // add to history
        if (User.isLoggedin()) {
            UtilHistory util = new UtilHistory(this);
            util.addHistory(name, course);
            String addHisUrl = Uris.getAddHistory();
            JSONObject obj = new JSONObject();
            try {
                obj.put("username", User.getUsername());
                obj.put("course", course);
                obj.put("name", name);
            } catch (JSONException e) {
                Log.e("add history error", e.toString());
            }
            Log.d("add history obj", obj.toString());
            JsonObjectRequest addHisReq = new JsonObjectRequest(Request.Method.POST, addHisUrl, obj,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d("add history", response.toString());
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("add history", error.toString());
                }
            });
            reqQue.add(addHisReq);
        }


        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager(), course, name);
        ViewPager viewPager = binding.viewPager;
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = binding.tabs;
        tabs.setupWithViewPager(viewPager);

        updateId();
    }
}