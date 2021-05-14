package com.example.doctorBudget.DAOs;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;


import com.example.doctorBudget.Entities.PersonalFinanceSource;

import java.util.List;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface PersonalFinanceSourceDao {
    @Insert(onConflict = REPLACE)
    void insertPersonalFinanceSource(PersonalFinanceSource personalFinanceSource);

    //Delete query
    @Delete
    void deletePersonalFinanceSource(PersonalFinanceSource personalFinanceSource);

    @Query("SELECT finance_source_id, user_id_pfs,finance_source_name, note,finance_source_cat_id_pfs " +
            "FROM PersonalFinanceSource WHERE user_id_pfs=:userID")
    List<PersonalFinanceSource> getAllPersonalFinanceSources(int userID);

    @Query("SELECT finance_source_name FROM PersonalFinanceSource WHERE user_id_pfs=:userID")
    List<String> getFinanceSourceByUser(int userID);

    @Query("SELECT finance_source_name FROM PersonalFinanceSource WHERE finance_source_id=:psf_id")
    String getFinanceSourceNameById(int psf_id);

    @Query("SELECT finance_source_id FROM PersonalFinanceSource WHERE finance_source_name=:psf_name")
    int getIDFinanceSource (String psf_name);

}
