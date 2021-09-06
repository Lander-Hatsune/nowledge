package com.example.nowledge.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.nowledge.R;

import java.util.List;

public class question_adapter extends ArrayAdapter<question> {
    private int resourceId;
    public String Choose;

    public question_adapter(Context context,int textViewResourceId,List<question> objects){
        super(context,textViewResourceId,objects);
        resourceId=textViewResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        question q=getItem(position);

        View view;
        ViewHolder viewHolder;
        if(convertView==null){
            view=LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
            viewHolder=new ViewHolder();
            viewHolder.QuestionText=view.findViewById(R.id.QuestionText);
            viewHolder.AnswerList=view.findViewById(R.id.AnswerList);
            viewHolder.A=view.findViewById(R.id.A);
            viewHolder.B=view.findViewById(R.id.B);
            viewHolder.C=view.findViewById(R.id.C);
            viewHolder.D=view.findViewById(R.id.D);
            viewHolder.CheckAnswer=view.findViewById(R.id.CheckAnswer);
            viewHolder.Judge=view.findViewById(R.id.Judge);
        }
        else{
            view=convertView;
            viewHolder=(ViewHolder) view.getTag();
        }

        viewHolder.QuestionText.setText(q.getQuestionText());
        viewHolder.A.setText(q.getA());
        viewHolder.B.setText(q.getB());
        viewHolder.C.setText(q.getC());
        viewHolder.D.setText(q.getD());
        viewHolder.CheckAnswer.setTag(position);
        viewHolder.CheckAnswer.setOnClickListener(new View.OnClickListener() {
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
                        viewHolder.Judge.setText("回答正确");
                    }
                    else {
                        viewHolder.Judge.setText("回答错误，正确答案是："+q.getAnswer());
                    }
                }
                else {
                    viewHolder.Judge.setText("请选择你的答案");
                }
            }
        });
        return view;
    }

    class ViewHolder{
        public TextView QuestionText;
        public RadioGroup AnswerList;
        public RadioButton A;
        public RadioButton B;
        public RadioButton C;
        public RadioButton D;
        public Button CheckAnswer;
        public TextView Judge;
    }
}
