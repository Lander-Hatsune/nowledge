package com.example.nowledge.data;

public class Uris {
    private static final String host = "http://47.93.5.8:8080";
    private static final String login = host + "/user/login";
    private static final String register = host + "/user/register";
    private static final String star = host + "/star";
    private static final String unstar = host + "/unstar";
    private static final String starlist = host + "/starlist";
    private static final String addHistory = host + "/addhistory";
    private static final String clearHistory = host + "/clearhistory";
    private static final String historylist = host + "/historylist";
    private static final String stat = "/stat";
    private static final String inc = host + stat + "/inc";
    private static final String info = host + stat + "/get";


    private static final String loadquestionstate = host + "/stat/question";
    private static final String addquestion = host + "/question/add";
    private static final String pickquestion = host + "/question/pick";
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

    public static String getInc() { return inc; }

    public static String getInfo() {return info;}

    public static String getStarlist() {
        return starlist;
    }

    public static String getAddHistory() { return addHistory; }

    public static String getClearHistory() { return clearHistory; }

    public static String getHistorylist() { return historylist; }

    public static String getRobotSearch() { return robotSearch; }

    public static String getLinkSearch() { return linkSearch; }

    public static String getSearch() { return search; }

    public static String getDetail() { return detail; }

    public static String getQuestion() {return question; }

    public static String getLoadquestionstate() {return loadquestionstate;}

    public static String getAddquestion() {return addquestion;}

    public static String getPickquestion() {return pickquestion;}
}

