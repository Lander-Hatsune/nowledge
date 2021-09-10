package com.example.nowledge.entitydetail;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.security.AppUriAuthenticationPolicy;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.nowledge.R;
import com.example.nowledge.data.Singleton;
import com.example.nowledge.data.Uris;
import com.example.nowledge.data.User;
import com.example.nowledge.utils.question;
import com.example.nowledge.utils.question_adapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class EntityQuestion extends Fragment {

    private static final String ARG_PARAM1 = "name";
    private static final String ARG_PARAM2 = "course";

    private String mname,mcourse;
    private String id = User.getID();
    private View view;
    private RecyclerView listViewQuestion;
    private TextView Uptitle;
    private final int QUESTION_SOCKET_TIMEOUT_MS = 10000;

    public EntityQuestion() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mname = getArguments().getString(ARG_PARAM1);
            mcourse = getArguments().getString(ARG_PARAM2);
        }
        Log.e("Question", getArguments().toString());
    }

    protected void updateId() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                RequestQueue reqQue = Singleton.getInstance
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.e("Question Fragment", "on create view");
        view=inflater.inflate(R.layout.fragment_entity_question,container,false);
        id = User.getID();

        listViewQuestion = view.findViewById(R.id.QuestionList);
        List<question> question_list = new ArrayList<>();
        question_adapter adapter = new question_adapter(question_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        listViewQuestion.setLayoutManager(layoutManager);
        listViewQuestion.setAdapter(adapter);
        Uptitle=view.findViewById(R.id.UpTitle);


        return view;
    }

    private boolean sMatch(String src) {
        String[] a = {".", "、", "．"};
        String[] c = {"A", "B", "C", "D"};
        for (int i = 0; i < 4; ++i) {
            boolean test = false;
            for (int j = 0; j < 3; ++j) {
                String match = c[i] + a[j];
                if (src.contains(match)) {
                    test = true; break;
                }
            }
            if (!test) return false;
        }
        return true;
    }

    @Override
    public void onStart() {
        super.onStart();

        listViewQuestion = view.findViewById(R.id.QuestionList);
        String url = Uris.getQuestion() + "?";
        url += "uriName=" + mname;
        url += "&id=" + id;
        Log.d("questionurl:", url);

        RequestQueue reqQue = Singleton.getInstance(getContext()).getRequestQueue();
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d ("response for Question", response.toString());
                        List<question> question_list = new ArrayList<>();
                        JSONArray questions = new JSONArray();
                        try {
                            questions = response.getJSONArray("data");
                        } catch (JSONException e) {
                            Log.e("Question: Error getting data", e.toString());
                        }
                        int Count=0;
                        for (int i = 0; i < questions.length(); i++) {
//                            if (Count > 10) {
//                                break;
//                            }
                            try {
                                JSONObject obj = questions.getJSONObject(i);
                                String qAnswer = obj.getString("qAnswer");
                                int id= obj.getInt("id");
                                String object = obj.getString("qBody");
                                if(sMatch(object)){
                                    String[] getdetail = object.split("A[\\.．、]|B[\\.．、]|C[\\.．、]|D[\\.．、]");
                                    question newQuestion = new question(User.getUsername(),mcourse,getdetail[0],qAnswer,id,getdetail[1],getdetail[2],getdetail[3],getdetail[4]);

                                    // add question to backend
                                    if(User.isLoggedin()){
                                        String addquestionUrl = Uris.getAddquestion();
                                        JSONObject newobj = newQuestion.package_question();
                                        Log.e("question detail", newobj.toString());
                                        JsonObjectRequest addQuesReq = new JsonObjectRequest(Request.Method.POST, addquestionUrl, newobj,
                                                new Response.Listener<JSONObject>() {
                                                    @Override
                                                    public void onResponse(JSONObject response) {
                                                        Log.d("add question",response.toString());
                                                    }
                                                },new Response.ErrorListener(){
                                            @Override
                                            public void onErrorResponse(VolleyError error){
                                                Log.e("add question error" ,error.toString());
                                            }
                                        });
                                        reqQue.add(addQuesReq);
                                    }

                                    question_list.add(newQuestion);
                                    Count++;
                                }
                            } catch (JSONException e) {
                                Log.e("Error parsing detail obj", e.toString());
                            }
                        }
                        if(Count==0){
                            Uptitle.setText("未搜索到相关习题！");
                        }
                        else {
                            Uptitle.setText("共搜索到"+Count+"题！");
                        }
                        Log.d("Question number", String.valueOf(question_list.size()));
                        question_adapter adapter = new question_adapter(question_list);
                        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
                        listViewQuestion.setLayoutManager(layoutManager);
                        listViewQuestion.setAdapter(adapter);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Question error", error.toString());

            }
        });
        req.setRetryPolicy(new DefaultRetryPolicy(
                QUESTION_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        reqQue.add(req);

    }

}