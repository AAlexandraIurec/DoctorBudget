package com.example.doctorBudget.DAOs;

import android.graphics.Bitmap;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.doctorBudget.Entities.Income;

import java.util.Date;
import java.util.List;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface IncomeDao {
    @Insert(onConflict = REPLACE)
    void insertIncome(Income income);

    @Delete
    void deleteIncome(Income income);

    @Query("UPDATE Income SET amount_inc= :incAmount, date_of_registration_inc =:incRegDate, " +
            "recurrent_inc=:incReccurent, note_inc=:incNote, attachment_inc=:incAttachment," +
            "subcat_income_id_inc=:incSubCat,  finance_source_id_inc=:incFinaceSource " +
            "WHERE user_id_inc = :incUserID AND income_id=:incID")
    void updateIncome(double incAmount, Date incRegDate, int incReccurent, String incNote,
                      Bitmap incAttachment,int incSubCat, int incFinaceSource, int incUserID, int incID);



    @Query("SELECT income_id, amount_inc,date_of_registration_inc,finance_source_id_inc,user_id_inc," +
            " recurrent_inc,subcat_income_id_inc FROM Income")
    List<Income> getAllIncomes();

    @Query("SELECT * FROM Income WHERE user_id_inc=:userID and " +
            "date_of_registration_inc between :date1 and :date2 ORDER BY date_of_registration_inc")
    List<Income> getAllIncomesByDates(int userID, Date date1, Date date2);

    @Query("SELECT SUM(amount_inc) FROM Income WHERE user_id_inc=:userID and date_of_registration_inc between :date1 and :date2")
    double getIncomeSumByDatesAndUser(int userID, Date date1, Date date2);

    @Query("SELECT SUM(amount_inc) FROM Income WHERE date_of_registration_inc between :date1 and :date2")
    double getIncomeSumByDates(Date date1, Date date2);
}
