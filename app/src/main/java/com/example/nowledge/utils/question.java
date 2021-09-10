package com.example.nowledge.utils;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.nowledge.data.Singleton;
import com.example.nowledge.data.Uris;
import com.example.nowledge.data.User;

import org.json.JSONException;
import org.json.JSONObject;

public class question {

    private String username;
    private String course;
    private String QuestionText;
    private String Answer;
    private int id;
    private String A;
    private String B;
    private String C;
    private String D;
    private int state;
    private boolean choose;
    private String text;
    private String color;

    public question(String username,String course,String QuestionText,String Answer,int id,String A,String B,String C,String D) throws JSONException {
        this.QuestionText=QuestionText;
        this.username=username;
        this.course=course;
        this.Answer=Answer;
        this.id=id;
        this.A=A;
        this.B=B;
        this.C=C;
        this.D=D;
        this.choose=false;
        this.state=-1;
        this.text="";
        this.color="#000000";
    }

    public String getQuestionText(){return QuestionText;}

    public String getAnswer(){return Answer;}

    public String getA(){return A;}

    public String getB(){return B;}

    public String getC(){return C;}

    public String getD(){return D;}

    public int getId(){return id;}

    public int getState(){return state;}

    public void setState(int state){this.state=state;}

    public boolean getChoose(){return choose;}

    public void setChoose(boolean choose){this.choose=choose;}

    public String getText(){return text;}

    public void setText(String text){this.text=text;}

    public String getColor(){return color;}

    public void setColor(String color){this.color=color;}

    public String getUsername(){ return username; }

    public String getCourse(){return course;}

    public JSONObject package_question (){
        JSONObject ques=new JSONObject();
        try {
            ques.put("id",id);
        }catch (JSONException e){
            e.printStackTrace();
        }
        try {
            ques.put("course",course);
        }catch (JSONException e){
            e.printStackTrace();
        }
        try {
            ques.put("text",QuestionText);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            ques.put("optionA",A);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            ques.put("optionB",B);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            ques.put("optionC",C);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            ques.put("optionD",D);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            ques.put("ans",Answer);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JSONObject obj = new JSONObject();

        try {
            obj.put("username",username);
            Log.e("username:",username);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            obj.put("payload",ques);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return obj;
    }
}
