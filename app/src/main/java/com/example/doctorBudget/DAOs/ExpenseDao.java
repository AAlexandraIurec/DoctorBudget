package com.example.doctorBudget.DAOs;

import android.graphics.Bitmap;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;


import com.example.doctorBudget.Entities.Expense;
import com.example.doctorBudget.TopExpenses;

import java.util.Date;
import java.util.List;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface ExpenseDao {
    @Insert(onConflict = REPLACE)
    void insertExpense(Expense expense);

    @Delete
    void deleteExpense(Expense expense);

    @Query("UPDATE Expense SET amount_exp= :expAmount, date_of_registration_exp =:expRegDate, " +
            "recurrent_exp=:expReccurent, note_exp=:expNote, attachment_exp=:expAttachment," +
            "subcat_expense_id_exp=:expSubCat,  finance_source_id_exp=:expFinaceSource " +
            "WHERE user_id_exp =:expUserID AND expense_id=:expID")
    void updateExpense(double expAmount, Date expRegDate, int expReccurent, String expNote,
                      Bitmap expAttachment, int expSubCat, int expFinaceSource, int expUserID, int expID);

    @Query("SELECT expense_id, amount_exp,date_of_registration_exp,finance_source_id_exp,user_id_exp," +
            " recurrent_exp ,subcat_expense_id_exp FROM Expense")
    List<Expense> getAllExpenses();

    @Query("SELECT * FROM Expense WHERE user_id_exp=:userID and " +
            "date_of_registration_exp between :date1 and :date2 ORDER BY date_of_registration_exp")
    List<Expense> getAllExpensesByDates(int userID, Date date1, Date date2);

    @Query("SELECT SUM(amount_exp) FROM Expense WHERE user_id_exp=:userID and date_of_registration_exp between :date1 and :date2")
    double getExpenseSumByDatesAndUser(int userID, Date date1, Date date2);

    @Query("SELECT SUM(amount_exp) FROM Expense WHERE date_of_registration_exp between :date1 and :date2")
    double getExpenseSumByDates( Date date1, Date date2);

    @Query("SELECT expenseSubcatName , SUM(amount_exp) AS sumAmountExpCat FROM Expense JOIN ExpenseSubcategory ON  " +
            "subcat_expense_id_exp=ID_subCat_expense WHERE user_id_exp=:userID GROUP BY (subcat_expense_id_exp) ORDER BY  SUM(amount_exp) DESC LIMIT 5")
    List<TopExpenses> getExpensesTop(int userID);
}

