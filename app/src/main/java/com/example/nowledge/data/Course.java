package com.example.nowledge.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Course {
    private static String[] courses = {"chinese", "math", "english",
            "geo", "history", "politics",
            "physics", "chemistry", "biology"};

    private static List<String> courseNames = new ArrayList(Arrays.asList("语文", "数学", "英语",
            "地理", "历史", "政治",
            "物理", "化学", "生物"));

    public static String[] getCourses() { return courses; }
    public static List<String> getCourseNames() { return courseNames; }
    public static int getCourseID(String course) {
        int res = -1;
        for (int i = 0; i < courses.length; ++i) {
            if (courses[i].equals(course)) {
                res = i; break;
            }
        }
        return res;
    }
}
