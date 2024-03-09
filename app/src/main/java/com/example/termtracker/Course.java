package com.example.termtracker;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;
import org.jetbrains.annotations.NotNull;

@Entity(tableName = "courses",
        foreignKeys = @ForeignKey(entity = Term.class, parentColumns = "id", childColumns = "termID"))
public class Course {
    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "title")
    private String courseTitle;
    private String start;
    private String end;
    private final String status;
    private String instructorName;
    private String instructorPhone;
    private String instructorEmail;
    private String note;
    private final int termID;
    private String termName;

    public Course(String courseTitle, String start, String end, String status, String instructorName, String instructorPhone, String instructorEmail, int termID) {
        this.courseTitle = courseTitle;
        this.start = start;
        this.end = end;
        this.status = status;
        this.instructorName = instructorName;
        this.instructorPhone = instructorPhone;
        this.instructorEmail = instructorEmail;
        this.termID = termID;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCourseTitle() {
        return courseTitle;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public String getStatus() {
        return status;
    }

    public String getInstructorName() {
        return instructorName;
    }

    public void setInstructorName(String instructorName) {
        this.instructorName = instructorName;
    }

    public String getInstructorPhone() {
        return instructorPhone;
    }

    public void setInstructorPhone(String instructorPhone) {
        this.instructorPhone = instructorPhone;
    }

    public String getInstructorEmail() {
        return instructorEmail;
    }

    public void setInstructorEmail(String instructorEmail) {
        this.instructorEmail = instructorEmail;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public int getTermID() {
        return termID;
    }

    public String getTermName() {
        return termName;
    }

    public void setTermName(String termName) {
        this.termName = termName;
    }

    @NotNull
    @Override
    public String toString() {
        return courseTitle + " " + start + " " + end + " " + status + " " + instructorName +
                " " + instructorPhone + " " + instructorEmail + " " + termID;
    }
}
