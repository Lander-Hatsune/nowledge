package com.example.nowledge.ui.link;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.nowledge.EntityDetailActivity;
import com.example.nowledge.NewEntityActivity;
import com.example.nowledge.R;
import com.example.nowledge.data.Course;
import com.example.nowledge.data.Singleton;
import com.example.nowledge.data.Uris;
import com.example.nowledge.data.User;
import com.example.nowledge.databinding.FragmentLinkBinding;
import com.example.nowledge.utils.EntityAdapter;
import com.example.nowledge.utils.EntityShort;
import com.example.nowledge.volley.MyJsonObjectRequest;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.widget.*;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LinkFragment extends Fragment {

    private LinkViewModel linkViewModel;
    private FragmentLinkBinding binding;
    private List<EntityShort> ett_list = new ArrayList<>();
    private String id = User.getID();
    private RequestQueue queue;
    private EditText searchText;
    private Button send, clear;
    private RecyclerView ettRecyclerView;
    private LinearLayoutManager layoutManager;
    private EntityAdapter adapter;
    private String[] courses;
    private List<String> courseNames;
    private Spinner spinner;

    private void updateID() {
        String LOGIN_URL = Uris.getLogin();
        JSONObject params = new JSONObject();
        try {
            params.put("username", "0");
            params.put("password", "0");
        }catch (JSONException e) {}
        JsonObjectRequest id_request = new JsonObjectRequest(Request.Method.POST, LOGIN_URL, params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            id = response.getString("id");
                            Log.i("info", id);
                            User.setID(id);
                        } catch (JSONException e) { e.printStackTrace();}                        }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("ERROR", error.getMessage());
                searchText.setHint("请求出错，请重新输入");
                searchText.setText("");
            }
        });

        queue.add(id_request);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        id = User.getID();
        courses = Course.getCourses();
        courseNames = Course.getCourseNames();
        linkViewModel =
                new ViewModelProvider(this).get(LinkViewModel.class);

        binding = FragmentLinkBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        ettRecyclerView = root.findViewById(R.id.link_entity_circle);
        layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
//        adapter = new EntityAdapter(ett_list, "link");
        adapter = new EntityAdapter(ett_list, "list");
        adapter.setOnItemClickListener(new EntityAdapter.OnItemClickListener() {
            @Override
            public void onItemCLick(String course, String name) {
                Intent intentDetail = new Intent(getActivity(), NewEntityActivity.class);
                intentDetail.putExtra("course", course);
                intentDetail.putExtra("name", name);
                startActivity(intentDetail);
            }
        });
        ettRecyclerView.setLayoutManager(layoutManager);
        ettRecyclerView.setAdapter(adapter);


        clear = root.findViewById(R.id.link_button_clear);
        clear.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                searchText.setText("");
            }
        });

        searchText = root.findViewById(R.id.editTextTextMultiLine2);
        spinner = root.findViewById(R.id.spinner_link);

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(getContext(), R.layout.support_simple_spinner_dropdown_item, courseNames);
        spinner.setAdapter(spinnerAdapter);
        queue = Singleton.getInstance(getActivity().getApplicationContext()).getRequestQueue();

        updateID();

        send = root.findViewById(R.id.link_button_send);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ett_list.size() > 0) {
                    ettRecyclerView.removeAllViews();
                    ett_list.clear();
                    adapter.notifyDataSetChanged();
                }
                String content = searchText.getText().toString();
                Log.d("link search", "[" + content + "]");
                if (!content.equals("")) {
//                    searchText.setText("");
                    InputMethodManager manager = ((InputMethodManager)getContext().getSystemService(Context.INPUT_METHOD_SERVICE));
                    if (manager != null)
                        manager.hideSoftInputFromWindow(view.getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);

                    String courseName = spinner.getSelectedItem().toString();
                    int pos = courseNames.indexOf(courseName);
                    String course = courses[pos];

                    JSONObject params = new JSONObject();
                    try{
                        params.put("id", id);
                        params.put("context", content);
                        params.put("course", course);
                    } catch (JSONException e) {}
                    JsonObjectRequest askRequest = new JsonObjectRequest(Request.Method.POST, Uris.getLinkSearch(), params,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    Log.i("Response", response.toString());
                                    try{
                                        String code = response.getString("code");
                                        if (code.equals("0")) {
                                            JSONObject data = response.getJSONObject("data");
                                            Log.i("Response", "data");
                                            JSONArray link_results = data.getJSONArray("results");
                                            Log.i("Response", "results");
                                            if (link_results.length() == 0) {
                                                searchText.setHint("搜索无结果，请重新搜索");
                                                searchText.setText("");
                                            } else {

                                                searchText.setHint(R.string.search_hint);
                                                for (int i = 0; i < link_results.length(); ++i) {
                                                    JSONObject res = link_results.getJSONObject(i);
                                                    String type = res.getString("entity_type");
                                                    String entity = res.getString("entity");
                                                    ett_list.add(new EntityShort(entity, type, course));
                                                    adapter.notifyItemInserted((ett_list.size() - 1));
//                                                    ettRecyclerView.scrollToPosition(ett_list.size() - 1);
                                                }

                                            }
                                        } else {
                                            addError(code);
                                        }
                                    } catch (JSONException e) {
                                        addError("3" + e.toString());
                                    }

                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            addError("VOlley" + error.getMessage());
                        }
                    });

                    queue.add(askRequest);
                }
            }
        });


        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void addError(String e) {
        searchText.setText("");
        searchText.setHint("请求有误，请重新输入 " + e);
        updateID();
    }
}