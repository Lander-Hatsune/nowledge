package com.example.nowledge.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Course {
    private static String[] courses = {"chinese", "math", "english",
            "geo", "history", "politics",
            "physics", "chemistry", "biology"};

    private static List<String> courseNames = new ArrayList(Arrays.asList("语文", "数学", "英语",
            "地理", "历史", "政治",
            "物理", "化学", "生物"));

    private static Map<String, String> cn2eng = new HashMap<>();
    static {
        for (int i = 0; i < courses.length; i++) {
            cn2eng.put(courseNames.get(i), courses[i]);
        }
    }

    private static List<String> selectedCourseNames = new ArrayList(Arrays.asList("语文", "数学", "英语",
            "地理", "历史", "政治",
            "物理", "化学", "生物"));

    public static String[] getCourses() { return courses; }

    public static void setSelectedCourses(List<String> setCourseNames) {
        selectedCourseNames = setCourseNames;
    }

    public static List<String> getSelectedCourses() {
        List<String> ret = new ArrayList<>();
        for (String courseName: selectedCourseNames) {
            ret.add(cn2eng.get(courseName));
        }
        return ret;
    }

    public static List<String> getCourseNames() { return courseNames; }

    public static List<String> getSelectedCourseNames() {
        return selectedCourseNames;
    }

    public static List<String> getUnSelectedCourseNames() {
        List<String> ret = new ArrayList<>();
        for (String name: courseNames) {
            if (!selectedCourseNames.contains(name)) {
                ret.add(name);
            }
        }
        return ret;
    }

    public static int getCourseID(String course) {
        int res = -1;
        for (int i = 0; i < courses.length; ++i) {
            if (courses[i].equals(course)) {
                res = i; break;
            }
        }
        return res;
    }

    public static int getCourseIDByName(String courseName) {
        return courseNames.indexOf(courseName);
    }
}
