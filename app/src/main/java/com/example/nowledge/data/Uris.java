package com.example.nowledge.data;

public class Uris {
    private static final String host = "http://47.93.5.8:8080";
    private static final String login = host + "/user/login";
    private static final String register = host + "/user/register";
    private static final String star = host + "/star";
    private static final String unstar = host + "/unstar";
    private static final String starlist = host + "/starlist";
    private static final String robotSearch = "http://open.edukg.cn/opedukg/api/typeOpen/open/inputQuestion";
    private static final String linkSearch = "http://open.edukg.cn/opedukg/api/typeOpen/open/linkInstance";
    private static final String edukg = "http://open.edukg.cn/opedukg/api/typeOpen/open";
    private static final String search = edukg + "/instanceList";
    private static final String detail = edukg + "/infoByInstanceName";
    private static final String question = edukg + "/questionListByUriName";


    public static String getLogin() {
        return login;
    }

    public static String getRegister() {
        return register;
    }

    public static String getStar() {
        return star;
    }

    public static String getUnstar() {
        return unstar;
    }

    public static String getStarlist() {
        return starlist;
    }

    public static String getRobotSearch() { return robotSearch; }

    public static String getLinkSearch() { return linkSearch; }

    public static String getSearch() { return search; }

    public static String getDetail() { return detail; }

    public static String getQuestion() {return question; }
}
