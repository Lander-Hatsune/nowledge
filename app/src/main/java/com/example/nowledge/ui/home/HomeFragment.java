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
import com.example.nowledge.databinding.FragmentHomeBinding;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;
    private String id = Uris.id;

    protected void updateId() {
        RequestQueue reqQue = Singleton.getInstance
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

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        updateId();

        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        TabLayout tabLayout = binding.tabLayout;
        ListView listView = binding.listView;

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                Log.d("Tab selected", tab.getText().toString());
                String[] courses = {"chinese", "math", "english",
                        "geo", "history", "politics",
                        "physics", "chemistry", "biology"};
                String[] searchKeys = {"文", "1", "时",
                        "流", "代", "发",
                        "学", "反", "物"};
                List<String> courseNames = new ArrayList<String>(Arrays.asList("语文", "数学", "英语",
                        "地理", "历史", "政治",
                        "物理", "化学", "生物"));

                Integer pos = courseNames.indexOf(tab.getText().toString());
                Log.d("pos", pos.toString());
                RequestQueue reqQue = Singleton.getInstance
                        (getActivity().getApplicationContext()).getRequestQueue();

                String params = "?";
                params += "course=" + courses[pos];
                params += "&searchKey=" + searchKeys[pos];
                params += "&id=" + id;

                List<String> entityNameList = new ArrayList<>();

                String url = Uris.getSearch() + params;

                Log.d("url", params);
                JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, url,
                        null, new Response.Listener<JSONObject>() {
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
                                entityNameList.add(label);
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

                    }
                }) {
                    @Override
                    public String getBodyContentType() {
                        return "application/x-www-form-urlencoded; charset=UTF-8";
                    }
                };
                reqQue.add(req);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        tabLayout.selectTab(tabLayout.getTabAt(2));
        tabLayout.selectTab(tabLayout.getTabAt(0));
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}