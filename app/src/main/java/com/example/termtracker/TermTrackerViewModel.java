package com.example.termtracker;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class TermTrackerViewModel extends AndroidViewModel {
    private final TermTrackerRepository repository;
    private final List<Term> allTerms;
    private final List<Course> allTermCourses;
    private final List<Course> allCourses;
    private final List<Assessment> allCourseAssessments;
    private final List<Assessment> allAssessments;
    public TermTrackerViewModel(@NotNull Application application) {
        super(application);
        repository = new TermTrackerRepository(application);
        allTerms = repository.getAllTerms();
        allTermCourses = repository.getAllTermCourses();
        allCourses = repository.getAllCourses();
        allCourseAssessments = repository.getAllCourseAssessments();
        allAssessments = repository.getAllAssessments();
    }

    public List<Term> getAllTerms() {
        return allTerms;
    }
    public List<Course> getAllTermCourses() {
        return allTermCourses;
    }
    public List<Course> getAllCourses() {
        return allCourses;
    }
    public List<Assessment> getAllCourseAssessments() {
        return allCourseAssessments;
    }
    public List<Assessment> getAllAssessments() {
        return allAssessments;
    }

    public void insertTerm(Term term) {
        repository.insertTerm(term);
    }

    public void updateTerm(Term term) {
        repository.updateTerm(term);
    }

    public void deleteTerm(Term term) {
        repository.deleteTerm(term);
    }

    public void insertCourse(Course course) {
        repository.insertCourse(course);
    }

    public void updateCourse(Course course) {
        repository.updateCourse(course);
    }

    public void deleteCourse(Course course) {
        repository.deleteCourse(course);
    }

    public void insertAssessment(Assessment assessment) {
        repository.insertAssessment(assessment);
    }

    public void updateAssessment(Assessment assessment) {
        repository.updateAssessment(assessment);
    }

    public void deleteAssessment(Assessment assessment) {
        repository.deleteAssessment(assessment);
    }



}
