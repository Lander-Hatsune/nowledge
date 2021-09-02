package com.example.nowledge.data;

import android.util.Pair;

import java.util.ArrayList;
import java.util.List;

public class User {
    private static String username = "0";
    private static List<Pair<String, String>> history = null;
    private static boolean loggedin = false;

    public static String getUsername() {
        return username;
    }

    public static void setUsername(String username) {
        User.username = username;
    }

    public static List<Pair<String, String>> getHistory() {
        return history;
    }

    public static void addHistory(String course, String name) {
        if (history == null) {
            history = new ArrayList<Pair<String, String>>();
        }
        history.add(new Pair<String, String>(course, name));
    }

    public static boolean isLoggedin() {
        return loggedin;
    }

    public static void setLoggedin(boolean loggedin) {
        User.loggedin = loggedin;
    }
}
