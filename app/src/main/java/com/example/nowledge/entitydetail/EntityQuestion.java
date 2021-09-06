package com.example.nowledge.entitydetail;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

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

    private static final String ARG_PARAM = "name";

    private String mname;
    private String id = User.getID();

    public EntityQuestion() {
    }

    public static EntityQuestion newInstance(String name) {
        EntityQuestion fragment = new EntityQuestion();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM, name);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mname = getArguments().getString(ARG_PARAM);
        }
    }

    protected void updateId() {
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_entity_question,container,false);

        id = User.getID();

        ListView listViewQuestion = view.findViewById(R.id.QuestionList);

        String url = Uris.getQuestion() + "?";
        url += "uriName=" + mname;
        url += "&id=" + id;

        Log.d("questionurl:", url);

        RequestQueue reqQue = Singleton.getInstance(getContext()).getRequestQueue();

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        List<question> question_list = new ArrayList<>();
                        try {
                            JSONArray questions = (JSONArray) response.get("data");
                            for (int i = 0, j = 0; i < questions.length(); i++) {
                                if (j > 10) {
                                    break;
                                }
                                JSONObject obj = questions.getJSONObject(i);
                                String qAnswer = obj.getString("qAnswer");
                                int id= obj.getInt("id");
                                String object = obj.getString("object");
                                if(object.contains("A.")&&object.contains("B.")&&object.contains("C.")&&object.contains("D.")){
                                    String[] getdetail = object.split("A.|B.|C.|D.");
                                    question_list.add(new question(getdetail[0],qAnswer,id,getdetail[1],getdetail[2],getdetail[3],getdetail[4]));
                                    j++;
                                }
                            }
                            question_adapter adapter = new question_adapter(getActivity(),R.layout.question_item,question_list);
                            listViewQuestion.setAdapter(adapter);

                        } catch (JSONException e) {
                            Log.e("Error parsing detail obj", e.toString());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        reqQue.add(req);

        return view;
    }
}