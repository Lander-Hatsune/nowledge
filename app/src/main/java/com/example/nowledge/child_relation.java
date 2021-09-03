package com.example.nowledge;

public class child_relation {
    private String type;
    private String detail;

    public child_relation(String type,String detail){
        this.type=type;
        this.detail=detail;
    }

    public String getType(){
        return type;
    }

    public String getDetail(){
        return detail;
    }

}
