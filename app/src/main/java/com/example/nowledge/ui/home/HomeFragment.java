package com.example.nowledge.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.text.Layout;
import android.text.style.TabStopSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.example.nowledge.EntityDetailActivity;
import com.example.nowledge.MainActivity;
import com.example.nowledge.R;
import com.example.nowledge.data.Singleton;
import com.example.nowledge.data.Uris;
import com.example.nowledge.data.User;
import com.example.nowledge.databinding.FragmentHomeBinding;
import com.example.nowledge.volley.MyJsonObjectRequest;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;
    private String id = User.getID();
    private RequestQueue requestQueue;
    private boolean initial = true;

//    private void updateIdSync() {
//        Log.i("info", "update ID sync start");
//        Thread updateId = new Thread(new Runnable() {
//            @Override
//            public void run() {
//
//                RequestFuture<JSONObject> future = RequestFuture.newFuture();
//                JSONObject obj = new JSONObject();
//                try {
//                    obj.put("username", "0");
//                    obj.put("password", "0");
//                } catch (JSONException e) {
//                    Log.e("UpdateId error:", e.toString());
//                }
//                Log.d("UpdateId obj Sync", obj.toString());
//                JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, Uris.getLogin(), obj, future, future);
//                requestQueue.add(request);
//                Log.i("login sync request", request.toString());
//                try {
//                    JSONObject response = future.get();
//                    Log.e("Sync Login Response", response.toString());
//                    String msg = response.getString("msg");
//                    id = response.getString("id");
//
//                } catch (Exception e) {
//                    Log.d("Sync Request Error", e.toString());
//                }
//            }
//        });
//        updateId.start();
//        Log.i("new thread", String.valueOf(updateId.getId()));
////        try {
////            updateId.join();
////        } catch (InterruptedException e) {
////            Log.e("Interruption in UpdateIdSync", e.toString());
////        }
//
//
//    }

    protected void updateId() {
        requestQueue = Singleton.getInstance
                (getActivity().getApplicationContext()).getRequestQueue();
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
                        Log.d("Login request success.", "login initially!" + String.valueOf(initial));
                        try {
                            id = response.getString("id");
                            User.setID(id);
                            Log.d("ID", id);
                            if (initial == true) {
                                sendCourseRequest("chinese", "文", binding.tabLayout.getTabAt(0));
                                initial = false;
                            }
                        } catch (JSONException e) {
                            Log.e("Login request msg error", e.toString());
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

    private void sendCourseRequest(String course, String searchkey, TabLayout.Tab tab) {
        ListView listView = binding.listView;
        RequestQueue reqQue = Singleton.getInstance
                (getActivity().getApplicationContext()).getRequestQueue();

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
                JSONArray list = null;
                try {
                    Log.d("resp:", response.toString());
                    Log.d("data", response.get("data").toString());
                    Log.d("code", response.getString("code"));
                    String code = response.getString("code");
                    if (!code.equals("0")) {
                        updateId();
                    } else {
                        list = (JSONArray) response.get("data");
                        for (int i = 0; i < list.length(); i++) {
                            JSONObject obj = list.getJSONObject(i);
                            String label = obj.get("label").toString();
                            String category = obj.get("category").toString();
                            String uri = obj.get("uri").toString();
                            entityNameList.add(label);
                        }
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>
                            (getActivity().getApplicationContext(), R.layout.entity_short_item, entityNameList);
                    listView.setAdapter(adapter);
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            Log.d("click", "number " + i);
                            Intent intentDetail = new Intent(getActivity(), EntityDetailActivity.class);
                            intentDetail.putExtra("name", entityNameList.get(i));
                            intentDetail.putExtra("course", tab.getText().toString());
                            startActivity(intentDetail);
                        }
                    });
                } catch (JSONException e) {}
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error", error.toString());
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
        requestQueue = Singleton.getInstance(getActivity().getApplicationContext()).getRequestQueue();

        initial = true;
        updateId();
        TabLayout tabLayout = binding.tabLayout;


        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                Log.d("Tab selected", tab.getText().toString());
                String[] courses = {"chinese", "math", "english",
                        "geo", "history", "politics",
                        "physics", "chemistry", "biology"};
                String[] searchKeys = {"文", "实", "时",
                        "流", "代", "发",
                        "学", "反", "物"};
                List<String> courseNames = new ArrayList<String>(Arrays.asList("语文", "数学", "英语",
                        "地理", "历史", "政治",
                        "物理", "化学", "生物"));

                Integer pos = courseNames.indexOf(tab.getText().toString());
                Log.d("pos", pos.toString());
                sendCourseRequest(courses[pos], searchKeys[pos], tab);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


//        Log.i("TabLayout", "select Tab 0");
//        tabLayout.selectTab(tabLayout.getTabAt(0));

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}