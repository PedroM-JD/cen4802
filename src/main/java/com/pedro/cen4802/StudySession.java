package com.pedro.cen4802;

public class StudySession {
    private String courseName;
    private int minutes;

    public StudySession(String courseName, int minutes) {
        this.courseName = courseName;
        this.minutes = minutes;
    }

    public String getCourseName() {
        return courseName;
    }

    public int getMinutes() {
        return minutes;
    }
}