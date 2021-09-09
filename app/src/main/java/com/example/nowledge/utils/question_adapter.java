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

import com.example.nowledge.R;

import java.util.List;

public class question_adapter extends RecyclerView.Adapter<question_adapter.ViewHolder> {
    private int resourceId;
    public String Choose;
    private List<question> questionList;

    public question_adapter(List<question> objects){
        this.questionList = objects;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
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

        viewHolder.CheckAnswer.setTag(position);
        viewHolder.CheckAnswer.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View view) {
                if(viewHolder.A.isChecked()||viewHolder.B.isChecked()||viewHolder.C.isChecked()||viewHolder.D.isChecked()){
                    if(viewHolder.A.isChecked()){
                        Choose="A";
                    }
                    else if(viewHolder.B.isChecked()){
                        Choose="B";
                    }
                    else if(viewHolder.C.isChecked()){
                        Choose="C";
                    }
                    else if(viewHolder.D.isChecked()){
                        Choose="D";
                    }
                    if(Choose.equals(q.getAnswer())){
                        viewHolder.Judge.setTextColor(Color.parseColor("#99CC33"));
                        viewHolder.Judge.setText("回答正确");
                        viewHolder.CheckAnswer.setEnabled(false);
                    }
                    else {
                        viewHolder.Judge.setTextColor(Color.parseColor("#CC0000"));
                        viewHolder.Judge.setText("回答错误，正确答案是："+q.getAnswer());
                        viewHolder.CheckAnswer.setEnabled(false);
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
