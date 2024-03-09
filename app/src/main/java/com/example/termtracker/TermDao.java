package com.example.termtracker;

import androidx.lifecycle.LiveData;
import androidx.room.*;
import com.example.termtracker.Term;

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
