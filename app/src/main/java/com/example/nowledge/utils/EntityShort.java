package com.example.nowledge.utils;

import java.util.Comparator;

public class EntityShort {
    private String label;
    private String category;
    private String course;

    public EntityShort(String $label, String $category, String $course) {
        this.label = $label;
        this.category = $category;
        this.course = $course;
    }
    public String getLabel() { return label; }
    public String getCategory() { return category; }
    public String getCourse() { return course; }

    public static Comparator<EntityShort> getComparator(boolean ifCategory, boolean ifReverse) {
        return new Comparator<EntityShort>() {
            @Override
            public int compare(EntityShort entityShort, EntityShort t1) {
                int a = ifReverse == true ? -1 : 1;
                if (ifCategory) {
                    return a * entityShort.category.compareTo(t1.category);
                } else {
                    return a * entityShort.label.compareTo(t1.label);
                }
            }
        };
    }
}
