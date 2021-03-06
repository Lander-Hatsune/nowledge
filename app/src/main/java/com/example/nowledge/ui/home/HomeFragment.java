package com.example.nowledge.ui.home;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.nowledge.NewEntityActivity;
import com.example.nowledge.R;
import com.example.nowledge.data.Course;
import com.example.nowledge.data.Singleton;
import com.example.nowledge.data.Uris;
import com.example.nowledge.data.User;
import com.example.nowledge.databinding.FragmentHomeBinding;
import com.example.nowledge.drag.DragActivity;
import com.example.nowledge.utils.EntityAdapter;
import com.example.nowledge.utils.EntityShort;
import com.example.nowledge.volley.MyJsonObjectRequest;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;
    private String id = User.getID();
    private RequestQueue requestQueue;

    private RecyclerView recyclerView;
    private List<EntityShort> ett_list = new ArrayList<>();

    private String[] courses = Course.getCourses();
    private String[] searchKeys = {"文", "实", "时",
            "流", "代", "发",
            "学", "反", "物"};
    private List<String> courseNames = Course.getCourseNames();

    protected void updateId(int courseID) {
        requestQueue = Singleton.getInstance
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
                        Log.d("Login request success.", "courseID:" + String.valueOf(courseID));
                        try {
                            id = response.getString("id");
                            User.setID(id);
                            Log.d("ID", id);
                            if (courseID >= 0)
                                sendCourseRequest(courseID, binding.tabLayout.getTabAt(courseID), false);
                        } catch (JSONException e) {
                            Log.e("Login request msg error", e.toString());
                            sendCourseRequest(courseID, binding.tabLayout.getTabAt(0), false);
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Login error:", error.toString());
                    }
                });
        Log.d("Request:", req.toString());
        requestQueue.add(req);
    }


    private void sendCourseRequest(int courseID, TabLayout.Tab tab, boolean first) {
        String course = courses[courseID], searchkey = searchKeys[courseID];

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("data", Context.MODE_PRIVATE);
        Log.e("shared preferences", sharedPreferences.toString());
        Set<String> cache_content = sharedPreferences.getStringSet(courses[courseID], new HashSet<String>());

        if (cache_content.size() > 0) {
            getContentFromCache(cache_content, course);
            return;
        }

        RequestQueue reqQue = Singleton.getInstance
                (getContext()).getRequestQueue();


        String params = "?";
        params += "course=" + course;
        params += "&searchKey=" + searchkey;
        params += "&id=" + id;

        List<String> entityNameList = new ArrayList<>();

        String url = Uris.getSearch() + params;

        Log.d("url", "*" + url + "*");
        MyJsonObjectRequest req = new MyJsonObjectRequest(Request.Method.GET, url,
                null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JSONArray list = new JSONArray();
                try {
                    Log.d("resp:", response.toString());
                    Log.d("data", response.get("data").toString());
                    Log.d("code", response.getString("code"));
                    String code = response.getString("code");
                    ett_list.clear();
                    if (!code.equals("0")) {
                        if (first)
                            updateId(courseID);
                    } else {
                        list = (JSONArray) response.get("data");
                        HashSet<String> strSet = new HashSet<>();

                        for (int i = 0; i < list.length(); i++) {
                            JSONObject obj = list.getJSONObject(i);
                            String label = obj.get("label").toString();
                            String category = obj.get("category").toString();
                            String uri = obj.get("uri").toString();
                            ett_list.add(new EntityShort(label, category, course));
                            strSet.add(obj.toString());

                        }



                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putStringSet(course, strSet);
                        editor.apply();


                    }


                } catch (JSONException e) {
                    Log.e("JsonError", e.toString());
                }
                LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
                recyclerView.setLayoutManager(layoutManager);
                EntityAdapter adapter = new EntityAdapter(ett_list, "list");
                adapter.setOnItemClickListener(new EntityAdapter.OnItemClickListener() {
                    @Override
                    public void onItemCLick(String course, String name) {
                        Intent intentDetail = new Intent(getActivity(), NewEntityActivity.class);
                        intentDetail.putExtra("course", course);
                        intentDetail.putExtra("name", name);
                        startActivity(intentDetail);
                    }
                });
                recyclerView.setAdapter(adapter);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error", error.toString());
                ett_list.clear();
                LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
                recyclerView.setLayoutManager(layoutManager);
                EntityAdapter adapter = new EntityAdapter(ett_list, "list");
                adapter.setOnItemClickListener(new EntityAdapter.OnItemClickListener() {
                    @Override
                    public void onItemCLick(String course, String name) {
                        Intent intentDetail = new Intent(getActivity(), NewEntityActivity.class);
                        intentDetail.putExtra("course", course);
                        intentDetail.putExtra("name", name);
                        startActivity(intentDetail);
                    }
                });
                recyclerView.setAdapter(adapter);
            }
        });
        reqQue.add(req);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        requestQueue = Singleton.getInstance(getContext()).getRequestQueue();
        recyclerView = root.findViewById(R.id.recycleViewHome);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("data", Context.MODE_PRIVATE);
        Log.e("shared preferences", sharedPreferences.toString());
        String init_course = "chinese";
        Set<String> cache_content = sharedPreferences.getStringSet(init_course, new HashSet<String>());

        if (cache_content.size() > 0) {
            getContentFromCache(cache_content, init_course);
            updateId(-1);
        } else {
            updateId(0);
        }


        Button floatTagButton = binding.homeEditTagButton;
        floatTagButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), DragActivity.class);
//                Intent intent = new Intent(getActivity(), FloatTagActivity.class);
                startActivity(intent);
            }
        });

        TabLayout tabLayout = binding.tabLayout;

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                Log.d("Tab selected", tab.getText().toString());

                Integer pos = courseNames.indexOf(tab.getText().toString());
                Log.d("pos", pos.toString());
                String course = courses[pos];

                sendCourseRequest(pos, tab, true);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        TabLayout tabLayout = binding.tabLayout;

        tabLayout.removeAllTabs();

        Log.d("onStart, generate tags", Course.getSelectedCourseNames().toString());
        for (String courseName: Course.getSelectedCourseNames()) {
            TabLayout.Tab tab = tabLayout.newTab();
            tab.setText(courseName);
            tab.setTag(Course.getCourseIDByName(courseName));
            tabLayout.addTab(tab);
        }

        Integer pos = tabLayout.getSelectedTabPosition();
        Integer id = courseNames.indexOf(tabLayout.getTabAt(pos).getText().toString());
        sendCourseRequest(id, tabLayout.getTabAt(pos), true);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void getContentFromCache(Set<String> str_response, String course) {
        Log.d("Cache!", course + ":" + str_response.toString());


        JSONArray list = new JSONArray();
        Iterator<String> iter = str_response.iterator();
        while (iter.hasNext()) {
            String tmp = iter.next();
            JSONObject tmp_obj = new JSONObject();
            com.alibaba.fastjson.JSONObject ali_json = com.alibaba.fastjson.JSONObject.parseObject(tmp);
            for (String key : ali_json.keySet()) {
                Object value = ali_json.get(key);
                try{
                    tmp_obj.put(key, value);
                } catch (JSONException e){
                    Log.e("create cache for home error", key + " " + value.toString());
                }
            }
            list.put(tmp_obj);
        }
        ett_list.clear();
        try{
            for (int i = 0; i < list.length(); i++) {
                JSONObject obj = list.getJSONObject(i);
                String label = obj.get("label").toString();
                String category = obj.get("category").toString();
                String uri = obj.get("uri").toString();
                ett_list.add(new EntityShort(label, category, course));

            }
        } catch (JSONException e) {

        }
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        EntityAdapter adapter = new EntityAdapter(ett_list, "list");
        adapter.setOnItemClickListener(new EntityAdapter.OnItemClickListener() {
            @Override
            public void onItemCLick(String course, String name) {
                Intent intentDetail = new Intent(getActivity(), NewEntityActivity.class);
                //Intent intentDetail = new Intent(getActivity(), QuestionTestActivity.class);
                intentDetail.putExtra("course", course);
                intentDetail.putExtra("name", name);
                startActivity(intentDetail);
            }
        });
        recyclerView.setAdapter(adapter);

    }
}