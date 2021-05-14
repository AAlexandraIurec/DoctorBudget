package com.example.doctorBudget.DAOs;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;


import com.example.doctorBudget.Entities.IncomeSubcategory;

import java.util.List;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface IncomeSubcategoryDao {
    @Insert(onConflict = REPLACE)
    void insertIncomeSubcategory(IncomeSubcategory incomeSubcategory);

    @Query("SELECT incomeSubcatName FROM IncomeSubcategory WHERE income_cat_id =:idCatIncome")
    List<String> getAllIncomeSubcategoryNames(int idCatIncome);

    @Query("SELECT ID_subCat_income FROM IncomeSubcategory WHERE incomeSubcatName =:subcatIncome")
    int getIDsubCatIncome (String subcatIncome);

    @Query("SELECT * FROM IncomeSubcategory")
    List<IncomeSubcategory> getAllIncomeSubategories();

    @Query("SELECT incomeSubcatName FROM IncomeSubcategory WHERE ID_subCat_income =:subcatIncomeID")
    String getNameSubCatIncome (int subcatIncomeID);
}
