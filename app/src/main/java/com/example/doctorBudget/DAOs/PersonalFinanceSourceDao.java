package com.example.doctorBudget.DAOs;

import android.graphics.Bitmap;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;


import com.example.doctorBudget.Entities.PersonalFinanceSource;
import com.example.doctorBudget.TopPFSIncome;
import com.example.doctorBudget.TopPSFExpense;

import java.util.Date;
import java.util.List;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface PersonalFinanceSourceDao {
    @Insert(onConflict = REPLACE)
    void insertPersonalFinanceSource(PersonalFinanceSource personalFinanceSource);

    //Delete query
    @Delete
    void deletePersonalFinanceSource(PersonalFinanceSource personalFinanceSource);

    @Query("UPDATE PersonalFinanceSource SET finance_source_name= :pfsName, note =:pfsNote, " +
            "finance_source_cat_id_pfs=:pfsCatID " +
            "WHERE user_id_pfs=:userID  AND finance_source_id=:psfId")
    void updatePFS(String pfsName, String pfsNote, int pfsCatID, int userID, int psfId);

    @Query("SELECT finance_source_id, user_id_pfs,finance_source_name, note,finance_source_cat_id_pfs " +
            "FROM PersonalFinanceSource WHERE user_id_pfs=:userID")
    List<PersonalFinanceSource> getAllPersonalFinanceSources(int userID);

    @Query("SELECT finance_source_name FROM PersonalFinanceSource WHERE user_id_pfs=:userID")
    List<String> getFinanceSourceByUser(int userID);

    @Query("SELECT finance_source_name FROM PersonalFinanceSource WHERE finance_source_id=:psf_id")
    String getFinanceSourceNameById(int psf_id);

    @Query("SELECT finance_source_id FROM PersonalFinanceSource WHERE finance_source_name=:psf_name")
    int getIDFinanceSource (String psf_name);

    @Query("SELECT finance_source_id AS psfID, finance_source_name AS pfsName, SUM(amount_inc) AS sumAmountInc " +
            "FROM PersonalFinanceSource JOIN Income ON finance_source_id = finance_source_id_inc " +
            "WHERE user_id_pfs =:userID AND date_of_registration_inc between :date1 and :date2" +
            " GROUP BY finance_source_id_inc ORDER BY  SUM(amount_inc) DESC LIMIT 5")
    List<TopPFSIncome> getPFSIncomeTopByUser (int userID, Date date1, Date date2);

    @Query("SELECT finance_source_id AS psfID, finance_source_name AS pfsName, SUM(amount_exp) AS sumAmountExp " +
            "FROM PersonalFinanceSource JOIN Expense ON finance_source_id = finance_source_id_exp " +
            "WHERE user_id_pfs =:userID AND date_of_registration_exp between :date1 and :date2 " +
            "GROUP BY finance_source_id_exp ORDER BY  SUM(amount_exp) DESC LIMIT 5")
    List<TopPSFExpense> getPFSExpenseTopByUser (int userID, Date date1, Date date2);

    @Query("SELECT finance_source_id AS psfID, finance_source_name AS pfsName, SUM(amount_inc) AS sumAmountInc " +
            "FROM PersonalFinanceSource JOIN Income ON finance_source_id = finance_source_id_inc " +
            "WHERE date_of_registration_inc between :date1 and :date2 " +
            "GROUP BY finance_source_id_inc ORDER BY  SUM(amount_inc) DESC LIMIT 5")
    List<TopPFSIncome> getPFSIncomeTop(Date date1, Date date2);

    @Query("SELECT finance_source_id AS psfID, finance_source_name AS pfsName, SUM(amount_exp) AS sumAmountExp " +
            "FROM PersonalFinanceSource JOIN Expense ON finance_source_id = finance_source_id_exp " +
            "WHERE date_of_registration_exp between :date1 and :date2" +
            " GROUP BY finance_source_id_exp ORDER BY  SUM(amount_exp) DESC LIMIT 5")
    List<TopPSFExpense> getPFSExpenseTop (Date date1, Date date2);

}
