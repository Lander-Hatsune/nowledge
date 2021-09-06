package com.example.nowledge.utils;

import org.json.JSONException;
import org.json.JSONObject;

public class question {

    private String QuestionText;
    private String Answer;
    private int id;
    private String A;
    private String B;
    private String C;
    private String D;

    public question(String QuestionText,String Answer,int id,String A,String B,String C,String D) throws JSONException {
        this.QuestionText=QuestionText;
        this.Answer=Answer;
        this.id=id;
        this.A=A;
        this.B=B;
        this.C=C;
        this.D=D;
        sendMessage();
    }

    public String getQuestionText(){return QuestionText;}

    public String getAnswer(){return Answer;}

    public String getA(){return A;}

    public String getB(){return B;}

    public String getC(){return C;}

    public String getD(){return D;}

    public int getId(){return id;}

    public void sendMessage(){
        JSONObject ques=new JSONObject();
        try {
            ques.put("QuestionText",QuestionText);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            ques.put("Answer",Answer);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            ques.put("A",A);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            ques.put("B",B);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            ques.put("C",C);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            ques.put("D",D);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
