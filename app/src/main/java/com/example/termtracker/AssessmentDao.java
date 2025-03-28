package com.example.termtracker;

import androidx.room.*;

import java.util.List;

@Dao
public interface AssessmentDao {
    @Query("SELECT * FROM assessments WHERE courseID = :courseID")
    List<Assessment> getAssessments(int courseID);

    @Query("SELECT * FROM assessments")
    List<Assessment> getAllAssessments();

    @Insert
    void insertAssessment(Assessment assessment);

    @Update
    void updateAssessment(Assessment assessment);

    @Delete
    void deleteAssessment(Assessment assessment);
}
