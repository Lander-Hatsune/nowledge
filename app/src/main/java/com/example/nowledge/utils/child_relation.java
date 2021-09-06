package com.example.nowledge.utils;

public class child_relation {
    private String type;
    private String detail;
    private String course;

    public child_relation(String type,String detail,String course){
        this.type=type;
        this.detail=detail;
        this.course=course;
    }

    public String getType(){
        return type;
    }

    public String getDetail(){
        return detail;
    }

    public String getCourse(){return course;}


}
