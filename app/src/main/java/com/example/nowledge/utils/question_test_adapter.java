package com.example.nowledge.utils;

import android.annotation.SuppressLint;
import android.content.Context;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class question_test_adapter extends RecyclerView.Adapter<question_test_adapter.ViewHolder> {
    private int resourceId;
    private List<question_test> questionList;
    private Map<Integer,String> choosedAnswer;

    public question_test_adapter(List<question_test> objects){
        this.questionList = objects;
        choosedAnswer=new HashMap<>();
    }

    public void setChoosedAnswer(Map<Integer,String> object){
        this.choosedAnswer=object;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.question_test_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        question_test q = questionList.get(viewHolder.getAdapterPosition());
        viewHolder.QuestionText.setText((viewHolder.getAdapterPosition()+1)+"."+q.getQuestionText());
        viewHolder.A.setText(q.getA());
        viewHolder.B.setText(q.getB());
        viewHolder.C.setText(q.getC());
        viewHolder.D.setText(q.getD());

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
    }

    @Override
    public int getItemCount() {
        return questionList.size();
    }

    public Map<Integer,String > getChoosedAnswer(){
        return choosedAnswer;
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView QuestionText;
        public RadioGroup AnswerList;
        public RadioButton A;
        public RadioButton B;
        public RadioButton C;
        public RadioButton D;

        public ViewHolder(View view) {
            super(view);
            QuestionText = view.findViewById(R.id.TestQuestionText);
            AnswerList=view.findViewById(R.id.TestAnswerList);
            A=view.findViewById(R.id.TestA);
            B=view.findViewById(R.id.TestB);
            C=view.findViewById(R.id.TestC);
            D=view.findViewById(R.id.TestD);
        }
    }
}
