package com.example.termtracker;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;


@Entity(tableName = "assessments",
        foreignKeys = @ForeignKey(entity = Course.class, parentColumns = "id", childColumns = "courseID"))
public class Assessment {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private String assessmentTitle;
    private String type;
    private String date;
    private int courseID;
    private String course;

    public Assessment(String assessmentTitle, String date, String type, int courseID) {
        this.assessmentTitle = assessmentTitle;
        this.date = date;
        this.type = type;
        this.courseID = courseID;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAssessmentTitle() {
        return assessmentTitle;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getCourseID() {
        return courseID;
    }

    public void setCourseID(int courseID) {
        this.courseID = courseID;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }
}
