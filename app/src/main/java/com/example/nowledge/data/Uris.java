package com.example.nowledge.data;

public class Uris {
    private static final String host = "http://47.93.5.8:8080";
    private static final String login = host + "/user/login";
    private static final String register = host + "/user/register";
    private static final String star = host + "/star";
    private static final String unstar = host + "/unstar";
    private static final String starlist = host + "/starlist";

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
}
