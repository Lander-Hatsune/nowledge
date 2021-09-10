package com.example.nowledge.utils;

import org.json.JSONException;
import org.json.JSONObject;

public class question_test {

    private String QuestionText;
    private String Answer;
    private String A;
    private String B;
    private String C;
    private String D;
    private int state;

    public question_test(String QuestionText,String Answer,String A,String B,String C,String D) throws JSONException {
        this.QuestionText=QuestionText;
        this.Answer=Answer;
        this.A=A;
        this.B=B;
        this.C=C;
        this.D=D;
        this.state=-1;
    }

    public String getQuestionText(){return QuestionText;}

    public String getAnswer(){return Answer;};

    public String getA(){return A;}

    public String getB(){return B;}

    public String getC(){return C;}

    public String getD(){return D;}

    public int getState(){return state;}

    public void setState(int state){this.state=state;}

}
