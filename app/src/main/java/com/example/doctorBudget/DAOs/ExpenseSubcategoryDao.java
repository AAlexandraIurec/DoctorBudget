package com.example.doctorBudget.DAOs;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;


import com.example.doctorBudget.Entities.ExpenseSubcategory;

import java.util.List;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface ExpenseSubcategoryDao {
    @Insert(onConflict = REPLACE)
    void insertExpenseSubcategory(ExpenseSubcategory expenseSubcategory);

    @Query("SELECT expenseSubcatName FROM ExpenseSubcategory WHERE expense_cat_id =:idCatExpense")
    List<String> getAllExpenseSubcategoryNames(int idCatExpense);

    @Query("SELECT ID_subCat_expense FROM ExpenseSubcategory WHERE expenseSubcatName =:subcatExpense")
    int getIDsubCatExpense (String subcatExpense);

    @Query("SELECT * FROM ExpenseSubcategory")
    List<ExpenseSubcategory> getAllExpenseSubategories();
}
