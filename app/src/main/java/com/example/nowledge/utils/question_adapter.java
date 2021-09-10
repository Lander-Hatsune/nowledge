package com.example.nowledge.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.nowledge.R;
import com.example.nowledge.data.Singleton;
import com.example.nowledge.data.Uris;
import com.example.nowledge.data.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class question_adapter extends RecyclerView.Adapter<question_adapter.ViewHolder> {
    private int resourceId;
    public String Choose;
    private List<question> questionList;
    private ViewGroup parent;
    private Map<Integer,String> choosedAnswer;

    public question_adapter(List<question> objects){
        this.questionList = objects;
        choosedAnswer=new HashMap<>();
    }

    public void setChoosedAnswer(Map<Integer,String> object){
        this.choosedAnswer=object;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.parent=parent;
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.question_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        question q = questionList.get(position);
        viewHolder.QuestionText.setText(q.getQuestionText());
        viewHolder.A.setText(q.getA());
        viewHolder.B.setText(q.getB());
        viewHolder.C.setText(q.getC());
        viewHolder.D.setText(q.getD());

        viewHolder.AnswerList.setTag(viewHolder.getAdapterPosition());

        viewHolder.AnswerList.setTag(viewHolder.getAdapterPosition());

        viewHolder.AnswerList.setOnCheckedChangeListener(null);

        viewHolder.AnswerList.clearCheck();

        switch (q.getState()){
            case 1:
                viewHolder.A.setChecked(true);
                Log.e("set","A");
                break;
            case 2:
                viewHolder.B.setChecked(true);
                Log.e("set","B");
                break;
            case 3:
                viewHolder.C.setChecked(true);
                Log.e("set","C");
                break;
            case 4:
                viewHolder.D.setChecked(true);
                Log.e("set","D");
                break;
        }

        viewHolder.AnswerList.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                Log.e("check button",i+"#"+viewHolder.getAdapterPosition());
                if(i==viewHolder.A.getId()){
                    choosedAnswer.put(viewHolder.getAdapterPosition(),"A");
                    q.setState(1);
                }
                else if(i==viewHolder.B.getId()){
                    choosedAnswer.put(viewHolder.getAdapterPosition(),"B");
                    q.setState(2);
                }
                else if(i==viewHolder.C.getId()){
                    choosedAnswer.put(viewHolder.getAdapterPosition(),"C");
                    q.setState(3);
                }
                else if(i==viewHolder.D.getId()){
                    choosedAnswer.put(viewHolder.getAdapterPosition(),"D");
                    q.setState(4);
                }
            }
        });

        viewHolder.CheckAnswer.setTag(position);

        viewHolder.CheckAnswer.setOnClickListener(null);

        Log.e("position", String.valueOf(position));

        if(!q.getChoose()){
            Log.e(position+"choose state", String.valueOf(q.getState()));
            viewHolder.CheckAnswer.setEnabled(true);
        }
        else {
            viewHolder.CheckAnswer.setEnabled(false);
        }
        viewHolder.Judge.setText(q.getText());
        viewHolder.Judge.setTextColor(Color.parseColor(q.getColor()));

        viewHolder.CheckAnswer.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View view) {
                if(viewHolder.A.isChecked()||viewHolder.B.isChecked()||viewHolder.C.isChecked()||viewHolder.D.isChecked()){
                    if(viewHolder.A.isChecked()){
                        Choose="A";
                        q.setChoose(true);
                    }
                    else if(viewHolder.B.isChecked()){
                        Choose="B";
                        q.setChoose(true);
                    }
                    else if(viewHolder.C.isChecked()){
                        Choose="C";
                        q.setChoose(true);
                    }
                    else if(viewHolder.D.isChecked()){
                        Choose="D";
                        q.setChoose(true);
                    }
                    RequestQueue reqQue = Singleton.getInstance(parent.getContext()).getRequestQueue();
                    if(Choose.equals(q.getAnswer())){
                        viewHolder.Judge.setTextColor(Color.parseColor("#99CC33"));
                        viewHolder.Judge.setText("回答正确");
                        viewHolder.CheckAnswer.setEnabled(false);
                        q.setText("回答正确");
                        q.setColor("#99CC33");

                        //add answer state to backend
                        if (User.isLoggedin()) {
                            String addstateurL = Uris.getLoadquestionstate();
                            JSONObject obj = new JSONObject();
                            try {
                                obj.put("username", User.getUsername());
                                obj.put("param1", 1);
                                obj.put("param2", 1);
                            } catch (JSONException e) {
                                Log.e("add answer state error", e.toString());
                            }
                            Log.d("add answer state obj", obj.toString());
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
                    else {
                        viewHolder.Judge.setTextColor(Color.parseColor("#CC0000"));
                        viewHolder.Judge.setText("回答错误，正确答案是："+q.getAnswer());
                        viewHolder.CheckAnswer.setEnabled(false);
                        q.setText("回答错误，正确答案是："+q.getAnswer());
                        q.setColor("#CC0000");

                        //add answer state to backend
                        if (User.isLoggedin()) {
                            String addstateurL = Uris.getLoadquestionstate();
                            JSONObject obj = new JSONObject();
                            try {
                                obj.put("username", User.getUsername());
                                obj.put("param1", 1);
                                obj.put("param2", 0);
                            } catch (JSONException e) {
                                Log.e("add answer state error", e.toString());
                            }
                            Log.d("add answer state obj", obj.toString());
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
                else {
                    viewHolder.Judge.setText("请选择你的答案");
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return questionList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView QuestionText;
        public RadioGroup AnswerList;
        public RadioButton A;
        public RadioButton B;
        public RadioButton C;
        public RadioButton D;
        public Button CheckAnswer;
        public TextView Judge;

        public ViewHolder(View view) {
            super(view);
            QuestionText = view.findViewById(R.id.QuestionText);
            AnswerList=view.findViewById(R.id.AnswerList);
            A=view.findViewById(R.id.A);
            B=view.findViewById(R.id.B);
            C=view.findViewById(R.id.C);
            D=view.findViewById(R.id.D);
            CheckAnswer=view.findViewById(R.id.CheckAnswer);
            Judge=view.findViewById(R.id.Judge);
        }
    }
}
