package com.example.nowledge.data;

import java.util.List;

public class User {
    private static String username = "0";
    private static List<String> history;
    private static boolean loggedin = false;

    public static String getUsername() {
        return username;
    }

    public static void setUsername(String username) {
        User.username = username;
    }

    public static List<String> getHistory() {
        return history;
    }

    public static void addHistory(String his) {
        history.add(his);
    }

    public static boolean isLoggedin() {
        return loggedin;
    }

    public static void setLoggedin(boolean loggedin) {
        User.loggedin = loggedin;
    }
}
