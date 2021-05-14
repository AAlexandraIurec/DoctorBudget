package com.example.doctorBudget.DAOs;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;


import com.example.doctorBudget.Entities.ExpenseCategory;

import java.util.List;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface ExpenseCategoryDao {
    @Insert(onConflict = REPLACE)
    void insertExpenseCategory(ExpenseCategory expenseCategory);


    @Query("SELECT expenseCatName FROM ExpenseCategory")
    List<String> getAllExpenseCategoryNames();

    @Query("SELECT * FROM ExpenseCategory")
    List<ExpenseCategory> getAllExpenseCategories();
}
