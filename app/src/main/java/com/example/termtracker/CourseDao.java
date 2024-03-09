package com.example.termtracker;

import androidx.lifecycle.LiveData;
import androidx.room.*;
import com.example.termtracker.Course;

import java.util.List;

@Dao
public interface CourseDao {
    @Query("SELECT * FROM courses WHERE termID = :termID")
    List<Course> getCourses(int termID);

    @Query("SELECT * FROM courses")
    List<Course> getAllCourses();

    @Query("SELECT note FROM courses WHERE id = :courseID")
    String getNote(int courseID);

    @Query("UPDATE courses SET note = :note WHERE id = :courseID")
    void updateNote(String note, int courseID);

    @Query("UPDATE courses SET note = NULL WHERE id = :courseID")
    void deleteNote(int courseID);

    @Insert
    void insertCourse(Course course);

    @Update
    void updateCourse(Course course);

    @Delete
    void deleteCourse(Course course);
}
