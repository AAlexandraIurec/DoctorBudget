package com.example.doctorBudget.Entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class ExpenseCategory {
    @NonNull
    @PrimaryKey(autoGenerate = true)
    private int ID_cat_expense;
    private String expenseCatName;

    public ExpenseCategory(String expenseCatName) {
        this.expenseCatName = expenseCatName;
    }

    public int getID_cat_expense() {
        return ID_cat_expense;
    }

    public void setID_cat_expense(int ID_cat_expense) {
        this.ID_cat_expense = ID_cat_expense;
    }

    public String getExpenseCatName() {
        return expenseCatName;
    }

    public void setExpenseCatName(String expenseCatName) {
        this.expenseCatName = expenseCatName;
    }
}
