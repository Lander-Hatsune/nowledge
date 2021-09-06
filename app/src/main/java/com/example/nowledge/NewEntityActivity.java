package com.example.nowledge;

import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.nowledge.data.Singleton;
import com.example.nowledge.data.Uris;
import com.example.nowledge.data.User;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

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
    JSONArray properties = new JSONArray();
    JSONArray contents = new JSONArray();
    JSONArray questions = new JSONArray();

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

        binding = ActivityNewEntityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Bundle bundle = this.getIntent().getExtras();
        name = bundle.getString("name");
        course = bundle.getString("course");

        TextView title=(TextView) findViewById(R.id.entity_title);
        title.setText(name);

        String urld = Uris.getDetail() + "?";
        urld += "name=" + name;
        urld += "&course=" + course;
        urld += "&id=" + id;

        String urlq = Uris.getQuestion() + "?";
        urlq += "uriName=" + name;
        urlq += "&id=" + id;

        Log.d("detailurl:", urld);
        Log.d("questionurl",urlq);


        RequestQueue reqQue = Singleton.getInstance(getApplicationContext()).getRequestQueue();

        JsonObjectRequest reqd = new JsonObjectRequest(Request.Method.GET, urld, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("response",response.toString());
                        try {
                            JSONObject dataobj = response.getJSONObject("data");
                            Log.d("dataobj", dataobj.toString());

                            properties = (JSONArray) dataobj.get("property");
                            contents = (JSONArray) dataobj.get("content");
                        } catch (JSONException e) {
                            Log.e("Error parsing detail obj", e.toString());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        reqQue.add(reqd);


        JsonObjectRequest reqq = new JsonObjectRequest(Request.Method.GET, urlq, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            questions = (JSONArray) response.get("data");
                            Log.d("questions", response.toString());
                        } catch (JSONException e) {
                            Log.e("Error parsing detail obj", e.toString());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        reqQue.add(reqd);


        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager(),properties,contents,questions,course);
        ViewPager viewPager = binding.viewPager;
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = binding.tabs;
        tabs.setupWithViewPager(viewPager);
    }
}