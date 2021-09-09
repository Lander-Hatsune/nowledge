package com.example.nowledge.data;

import android.util.Log;
import android.util.Pair;

import java.util.ArrayList;
import java.util.List;

public class User {
    private static String username = "0";
    private static List<Pair<String, String>> searchHistory = new ArrayList<>();
    private static String ID = "";
    private static String specialState = "";
    private static boolean loggedin = false;

    public static String getID() { return ID;  }

    public static void setID(String id) { ID = id; }

    public static String getUsername() {
        return username;
    }

    public static void setUsername(String username) {
        User.username = username;
    }

    public static List<Pair<String, String>> getSearchHistory() {
        Log.d("history", searchHistory.toString());
        return searchHistory;
    }

    public static void addSearchHistory(String course, String name) {
        if (searchHistory == null) {
            searchHistory = new ArrayList<Pair<String, String>>();
        }
        if (searchHistory.indexOf(new Pair<String, String> (course, name)) == -1){
            searchHistory.add(new Pair<String, String>(course, name));
            Log.d("User add history", course + "/" + name);
        }

    }

    public static boolean isLoggedin() {
        return loggedin;
    }

    public static void setLoggedin(boolean loggedin) {
        User.loggedin = loggedin;
    }

    public static void setSpecialState(String specialState1) {
        specialState = specialState1;
    }

    public static String getSpecialState() { return specialState; }
}
