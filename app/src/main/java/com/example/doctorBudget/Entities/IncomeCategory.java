package com.example.doctorBudget.Entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class IncomeCategory {
    @NonNull
    @PrimaryKey(autoGenerate = true)
    private int ID_cat_income;
    private String incomeCatName;

    public IncomeCategory(String incomeCatName) {
        this.incomeCatName = incomeCatName;
    }

    public void setID_cat_income(int ID_cat_income) {
        this.ID_cat_income = ID_cat_income;
    }

    public int getID_cat_income() {
        return ID_cat_income;
    }

    public String getIncomeCatName() {
        return incomeCatName;
    }

    public void setIncomeCatName(String incomeCatName) {
        this.incomeCatName = incomeCatName;
    }
}
