package com.ridm.eduRIDM.model.room.Eval;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface EvalDao {

    @Insert
    void insertEval(Eval eval);

    @Delete
    void deleteEval(Eval eval);

    @Query("SELECT * FROM Eval")
    List<Eval> getAllEvals();
}