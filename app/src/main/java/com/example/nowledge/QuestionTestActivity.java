package com.example.nowledge;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.nowledge.data.Singleton;
import com.example.nowledge.data.Uris;
import com.example.nowledge.data.User;
import com.example.nowledge.databinding.ActivityNewEntityBinding;
import com.example.nowledge.databinding.ActivityQuestionTestBinding;
import com.example.nowledge.utils.question;
import com.example.nowledge.utils.question_adapter;
import com.example.nowledge.utils.question_test;
import com.example.nowledge.utils.question_test_adapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class QuestionTestActivity extends AppCompatActivity {

    private ActivityQuestionTestBinding binding;
    private String id = User.getID();
    private View view;
    private Button Upload;
    private TextView Grade;
    private int Count;
    private question_test_adapter adapter;
    private List<String> right_answer;
//    private List<Boolean> answer_state;
    private int RightCount;
    private JSONArray questions = new JSONArray();
    private final int Num = 10;
    private List<question_test> question_list;

    public QuestionTestActivity() {
    }

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
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_test);
        Log.d("Question Test", "on create view");
        id = User.getID();

        binding = ActivityQuestionTestBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Grade=findViewById(R.id.Grade);
        Upload=findViewById(R.id.CheckTotalAnswer);

        setTitle("试题推荐");

        right_answer=new ArrayList<>();

        RecyclerView listViewQuestion = binding.getRoot().findViewById(R.id.QuestionTestList);
        listViewQuestion.setItemViewCacheSize(15);


        RequestQueue reqQue = Singleton.getInstance(getApplicationContext()).getRequestQueue();
        String url = Uris.getPickquestion()+ "?username="+User.getUsername()+"&number="+Num;
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("get question list",response.toString());
                        try {
                            String code = null;
                            try {
                                code = response.getString("id");
                            } catch (JSONException e) {
                                Log.e("get question list id error", e.toString());
                            }
                            if (!code.equals("0")) {
                                return;
                            }
                            try {
                                questions = response.getJSONArray("payload");
                            } catch (JSONException e) {
                                Log.e("get question list error", e.toString());
                            }

                            question_list = new ArrayList<>();
                            right_answer = new ArrayList<>();
                            String Question,A,B,C,D,Answer;
                            Count=0;
                            for (int i = 0; i < questions.length(); i++) {
                                try {
                                    JSONObject obj = questions.getJSONObject(i);
                                    Answer=obj.getString("ans");
                                    right_answer.add(Answer);
                                    Question=obj.getString("text");
                                    A=obj.getString("optionA");
                                    B=obj.getString("optionB");
                                    C=obj.getString("optionC");
                                    D=obj.getString("optionD");
                                    question_list.add(new question_test(Question,Answer,A,B,C,D));
                                    ++Count;
                                    if (i > 1)
                                        break;
                                    Log.e("Question detail", obj.toString());
                                } catch (JSONException e) {
                                    Log.e("Error parsing detail obj", e.toString());
                                }
                            }
                            adapter = new question_test_adapter(getApplicationContext(), question_list, false,
                                    new ArrayList<String>(), new ArrayList<String>());
                            LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                            listViewQuestion.setLayoutManager(layoutManager);
                            listViewQuestion.setAdapter(adapter);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("question list error",error.toString());
            }
        });
        reqQue.add(req);




        Upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RightCount = 0;
                List<String> myAnswers = new ArrayList<>();
                if (adapter.getChoosedAnswer().size() < Count) {
                    Grade.setText("你还未选择所有答案");
                } else if (adapter.getChoosedAnswer().size() == Count) {
                    for (int i = 0; i < Count; ++i) {
                        String myAns = adapter.getChoosedAnswer().get(i);
                        myAnswers.add(myAns);
                        if (myAns.equals(right_answer.get(i))) {
                            RightCount++;
                        }
                    }
                    Grade.setText("你的总分是：" + 100 * RightCount / Count);
                    adapter = new question_test_adapter(getApplicationContext(), question_list,
                            true, myAnswers, right_answer);
                    listViewQuestion.setAdapter(adapter);
                    Upload.setEnabled(false);

                    if (User.isLoggedin()) {
                        String addstateurL = Uris.getLoadquestionstate();
                        JSONObject obj = new JSONObject();
                        try {
                            obj.put("username", User.getUsername());
                            obj.put("param1", Count);
                            obj.put("param2", RightCount);
                        } catch (JSONException e) {
                            Log.e("add answer state error", e.toString());
                        }
                        JsonObjectRequest addHisReq = new JsonObjectRequest(Request.Method.POST, addstateurL, obj,
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        Log.d("add answer state", response.toString());
                                    }
                                }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.e("add history", error.toString());
                            }
                        });
                        reqQue.add(addHisReq);
                    }
                }
            }
        });

    }

    private JSONArray get_tmp_json() throws JSONException {
        JSONObject a=new JSONObject();
        a.put("Answer","D");
        a.put("Question","   There are no apples ______ the tree but there’re some birds _____ it.");
        a.put("A","in; on");
        a.put("B","on; on");
        a.put("C","in; in");
        a.put("D","on; in");
        JSONObject b=new JSONObject();
        b.put("Answer","B");
        b.put("Question","   Would you like _______apples?");
        b.put("A","any");
        b.put("B","some");
        b.put("C","an");
        b.put("D","much");
        JSONArray c=new JSONArray();
        c.put(a);
        c.put(b);
        return c;
    }

}