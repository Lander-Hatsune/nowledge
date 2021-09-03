package com.example.nowledge.ui.robot;

import java.util.*;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

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
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.nowledge.R;
import com.example.nowledge.data.Singleton;
import com.example.nowledge.data.Uris;
import com.example.nowledge.data.User;
import com.example.nowledge.databinding.FragmentRobotBinding;
import com.example.nowledge.volley.MyJsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class RobotFragment extends Fragment {

    private RobotViewModel robotViewModel;
    private FragmentRobotBinding binding;
    private List<Message> msg_list = new ArrayList<> ();
    private RecyclerView msgRecyclerView;
    private EditText inputText;
    private Button send;
    private LinearLayoutManager layoutManager;
    private RobotMessage adapter;
    private RequestQueue queue;
    private String id;
    private String COURSE = "chinese";

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
                            User.setID(id);
                        } catch (JSONException e) { e.printStackTrace();}                        }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                msg_list.add(new Message("[Error] " + "后端登录错误" , false));
                adapter.notifyItemInserted((msg_list.size()-1));
                msgRecyclerView.scrollToPosition(msg_list.size()-1);
            }
        });

        queue.add(id_request);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        robotViewModel =
                new ViewModelProvider(this).get(RobotViewModel.class);

        binding = FragmentRobotBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        msgRecyclerView = root.findViewById(R.id.robotDialog);
        inputText = root.findViewById(R.id.search_robot);
        send = root.findViewById(R.id.button_robotSend);
        layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        adapter = new RobotMessage((msg_list = getData()));

        msgRecyclerView.setLayoutManager(layoutManager);
        msgRecyclerView.setAdapter(adapter);

        queue = Singleton.getInstance(getActivity().getApplicationContext()).getRequestQueue();

        updateID();

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = inputText.getText().toString();
                if (!content.equals("")) {
                    msg_list.add(new Message(content, true));
                    adapter.notifyItemInserted((msg_list.size()-1));
                    msgRecyclerView.scrollToPosition((msg_list.size()-1));
                    inputText.setText("");

                    JSONObject params = new JSONObject();
                    try{
                        params.put("id", id);
                        params.put("inputQuestion", content);
                        params.put("course", COURSE);
                    } catch (JSONException e) {}
                    JsonObjectRequest askRequest = new JsonObjectRequest(Request.Method.POST, Uris.getRobotSearch(), params,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    try{
                                        String code = response.getString("code");
                                        if (code.equals("0")) {
                                            Log.i("Response robot", response.toString());
                                            JSONArray dataArray = response.getJSONArray("data");
                                            String answer = new String();
                                            try{
                                                answer = dataArray.getJSONObject(0).getString("value");
                                            } catch (Exception e) {
                                                answer = "此问题没有找到答案！";
                                            }
                                            if (answer.equals(""))
                                                answer = "此问题没有找到答案！";
                                            msg_list.add(new Message(answer, false));
                                            adapter.notifyItemInserted((msg_list.size()-1));
                                            msgRecyclerView.scrollToPosition(msg_list.size()-1);
                                        } else {
                                            addError();
                                        }
                                    } catch (JSONException e) {
                                        addError();
                                    }

                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            addError();
                        }
                    });

                    queue.add(askRequest);
                }
            }
        });

        return root;
    }

    private void addError() {
        msg_list.add(new Message("接口请求有误，请重新尝试", false));
        adapter.notifyItemInserted((msg_list.size()-1));
        msgRecyclerView.scrollToPosition(msg_list.size()-1);
        updateID();
    }

    private List<Message> getData() {
        List<Message> list = new ArrayList<>();
        list.add(new Message("您好，Nowledge机器人很开心为您服务!", false));
        return list;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}