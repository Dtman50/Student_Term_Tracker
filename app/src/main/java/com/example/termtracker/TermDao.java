package com.example.termtracker;

import androidx.room.*;

import java.util.List;

@Dao
public interface TermDao {

    @Query("SELECT * FROM terms ORDER BY id")
    List<Term> getTerms();

    @Insert
    void insertTerm(Term term);

    @Update
    void updateTerm(Term term);

    @Delete
    void deleteTerm(Term term);


}
