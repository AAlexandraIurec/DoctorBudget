package com.example.doctorBudget.Entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import static androidx.room.ForeignKey.CASCADE;

@Entity (foreignKeys = @ForeignKey(entity = IncomeCategory.class,
        parentColumns = "ID_cat_income",
        childColumns = "income_cat_id",
        onDelete = CASCADE))
public class IncomeSubcategory {
    @NonNull
    @PrimaryKey(autoGenerate = true)
    private int ID_subCat_income;
    private String incomeSubcatName;
    private int income_cat_id;

    public IncomeSubcategory(String incomeSubcatName, int income_cat_id) {
        this.incomeSubcatName = incomeSubcatName;
        this.income_cat_id = income_cat_id;
    }

    public void setID_subCat_income(int ID_subCat_income) {
        this.ID_subCat_income = ID_subCat_income;
    }

    public int getID_subCat_income() {
        return ID_subCat_income;
    }

    public String getIncomeSubcatName() {
        return incomeSubcatName;
    }

    public void setIncomeSubcatName(String incomeSubcatName) {
        this.incomeSubcatName = incomeSubcatName;
    }

    public int getIncome_cat_id() {
        return income_cat_id;
    }

    public void setIncome_cat_id(int income_cat_id) {
        this.income_cat_id = income_cat_id;
    }
}
