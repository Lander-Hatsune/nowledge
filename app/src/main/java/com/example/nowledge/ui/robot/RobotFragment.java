package com.example.nowledge.ui.robot;

import java.sql.Array;
import java.util.*;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;

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
import com.example.nowledge.R;
import com.example.nowledge.data.Course;
import com.example.nowledge.data.Singleton;
import com.example.nowledge.data.Uris;
import com.example.nowledge.data.User;
import com.example.nowledge.databinding.FragmentRobotBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class RobotFragment extends Fragment {

    private RobotViewModel robotViewModel;
    private FragmentRobotBinding binding;
    private List<Message> msg_list = new ArrayList<> ();
    private RecyclerView msgRecyclerView;
    private EditText inputText;
    private Spinner spinner;
    private Button send;
    private LinearLayoutManager layoutManager;
    private RobotMessage adapter;
    private RequestQueue queue;
    private String id;
    private String[] courses;
    private List<String> courseNames;

    private void updateID() {
        new Thread(new Runnable() {
            @Override
            public void run() {
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
        }).start();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        id = User.getID();
        courses = Course.getCourses();
        courseNames = Course.getCourseNames();
        robotViewModel =
                new ViewModelProvider(this).get(RobotViewModel.class);

        binding = FragmentRobotBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        msgRecyclerView = root.findViewById(R.id.robotDialog);
        inputText = root.findViewById(R.id.search_robot);
        spinner = root.findViewById(R.id.robot_spinner);
        send = root.findViewById(R.id.button_robotSend);
        layoutManager = new LinearLayoutManager(getContext());
        adapter = new RobotMessage((msg_list = getData()));

        msgRecyclerView.setLayoutManager(layoutManager);
        msgRecyclerView.setAdapter(adapter);
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(getContext(), R.layout.support_simple_spinner_dropdown_item, courseNames);
        spinner.setAdapter(spinnerAdapter);

        queue = Singleton.getInstance(getContext()).getRequestQueue();

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
                    String courseName = spinner.getSelectedItem().toString();
                    int pos = courseNames.indexOf(courseName);
                    String course = courses[pos];

                    JSONObject params = new JSONObject();
                    try{
                        params.put("id", id);
                        params.put("inputQuestion", content);
                        params.put("course", course);
                    } catch (JSONException e) {}
                    JsonObjectRequest askRequest = new JsonObjectRequest(Request.Method.POST, Uris.getRobotSearch(), params,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    Log.d("robot response", response.toString());
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