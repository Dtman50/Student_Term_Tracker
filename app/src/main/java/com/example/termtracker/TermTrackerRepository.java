package com.example.termtracker;

import android.app.Application;

import java.util.List;

public class TermTrackerRepository {
    private final TermDao termDao;
    private final CourseDao courseDao;
    private final AssessmentDao assessmentDao;
    private List<Term> allTerms;
    private List<Course> allTermCourses;
    private List<Course> allCourses;
    private List<Assessment> allCourseAssessments;
    private List<Assessment> allAssessments;

    public TermTrackerRepository(Application application) {
        TermTrackerDatabase database = TermTrackerDatabase.getInstance(application);
        termDao = database.termDao();
        courseDao = database.courseDao();
        assessmentDao = database.assessmentDao();
        allTerms = termDao.getTerms();
        allTermCourses = courseDao.getCourses(TermActivity.selectedTermID);
        allCourses = courseDao.getAllCourses();
        allCourseAssessments = assessmentDao.getAssessments(DetailedTermActivity.selectedCourseID);
        allAssessments = assessmentDao.getAllAssessments();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public void insertTerm(Term term) {
        TermTrackerDatabase.databaseWriteExecutor.execute(() -> termDao.insertTerm(term));
    }

    public void updateTerm(Term term) {
        TermTrackerDatabase.databaseWriteExecutor.execute(() -> termDao.updateTerm(term));
    }

    public void deleteTerm(Term term) {
        TermTrackerDatabase.databaseWriteExecutor.execute(() -> termDao.deleteTerm(term));
    }

    public void insertCourse(Course course) {
        TermTrackerDatabase.databaseWriteExecutor.execute(() -> courseDao.insertCourse(course));
    }

    public void updateCourse(Course course) {
        TermTrackerDatabase.databaseWriteExecutor.execute(() -> courseDao.updateCourse(course));
    }

    public void deleteCourse(Course course) {
        TermTrackerDatabase.databaseWriteExecutor.execute(() -> courseDao.deleteCourse(course));
    }

    public void insertAssessment(Assessment assessment) {
        TermTrackerDatabase.databaseWriteExecutor.execute(() -> assessmentDao.insertAssessment(assessment));
    }

    public void updateAssessment(Assessment assessment) {
        TermTrackerDatabase.databaseWriteExecutor.execute(() -> assessmentDao.updateAssessment(assessment));
    }

    public void deleteAssessment(Assessment assessment) {
        TermTrackerDatabase.databaseWriteExecutor.execute(() -> assessmentDao.deleteAssessment(assessment));
    }

    public List<Term> getAllTerms() {
        TermTrackerDatabase.databaseWriteExecutor.execute(() -> allTerms = termDao.getTerms());
        return allTerms;
    }

    public List<Course> getAllTermCourses() {
        TermTrackerDatabase.databaseWriteExecutor.execute(() -> allTermCourses = courseDao.getCourses(TermActivity.selectedTermID));
        return allTermCourses;
    }

    public List<Course> getAllCourses() {
        TermTrackerDatabase.databaseWriteExecutor.execute(() -> allCourses = courseDao.getAllCourses());
        return allCourses;
    }

    public List<Assessment> getAllCourseAssessments() {
        TermTrackerDatabase.databaseWriteExecutor.execute(() -> allCourseAssessments = assessmentDao.getAssessments(DetailedTermActivity.selectedCourseID));
        return allCourseAssessments;
    }

    public List<Assessment> getAllAssessments() {
        TermTrackerDatabase.databaseWriteExecutor.execute(() -> allAssessments = assessmentDao.getAllAssessments());
        return allAssessments;
    }

}
