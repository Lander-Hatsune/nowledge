package com.example.nowledge.utils;

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
}
