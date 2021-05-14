package com.example.doctorBudget.DAOs;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;


import com.example.doctorBudget.Entities.PersonalFinanceSourceCategory;

import java.util.List;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface PersonalFinanceSourceCategoryDao {

    @Insert(onConflict = REPLACE)
    void insertPersonalFinanceSourecCategory(PersonalFinanceSourceCategory personalFinanceSourceCategory);


    @Query("SELECT finance_source_category_name FROM PersonalFinanceSourceCategory")
    List<String> getAllPersonalFinanceSourceCatNames();

    @Query("SELECT finance_source_cat_id FROM PersonalFinanceSourceCategory WHERE finance_source_category_name =:catName")
    int obtainPfsCatID(String catName);

    @Query("SELECT finance_source_category_name FROM PersonalFinanceSourceCategory WHERE finance_source_cat_id =:idCat")
    String obtainPfsCatName(int idCat);

    @Query("SELECT * FROM PersonalFinanceSourceCategory")
    List<PersonalFinanceSourceCategory> getAllPersonalFinanceSourceCategories();
}
