package com.example.doctorBudget.DAOs;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.doctorBudget.Entities.IncomeCategory;

import java.util.List;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface IncomeCategoryDao {
    @Insert(onConflict = REPLACE)
    void insertIncomeCategory(IncomeCategory incomeCategory);


    @Query("SELECT incomeCatName FROM IncomeCategory")
    List<String> getAllIncomeCategoryNames();

    @Query("SELECT * FROM IncomeCategory")
    List<IncomeCategory> getAllIncomeCategories();

    @Query("SELECT incomeCatName FROM IncomeCategory INNER JOIN IncomeSubcategory ON" +
            " IncomeCategory.ID_cat_income =  IncomeSubcategory.income_cat_id " +
            "WHERE IncomeSubcategory.ID_subCat_income =:subcatID")
    String getIncomeCategoryName(int subcatID);
}
