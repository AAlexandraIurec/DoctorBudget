package com.example.doctorBudget.Entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import static androidx.room.ForeignKey.CASCADE;

@Entity(foreignKeys = @ForeignKey(entity = ExpenseCategory.class,
        parentColumns = "ID_cat_expense",
        childColumns = "expense_cat_id",
        onDelete = CASCADE))
public class ExpenseSubcategory {
    @NonNull
    @PrimaryKey(autoGenerate = true)
    private int ID_subCat_expense;
    private String expenseSubcatName;
    private int expense_cat_id;

    public ExpenseSubcategory(String expenseSubcatName, int expense_cat_id) {
        this.expenseSubcatName = expenseSubcatName;
        this.expense_cat_id = expense_cat_id;
    }

    public int getID_subCat_expense() {
        return ID_subCat_expense;
    }

    public void setID_subCat_expense(int ID_subCat_expense) {
        this.ID_subCat_expense = ID_subCat_expense;
    }

    public String getExpenseSubcatName() {
        return expenseSubcatName;
    }

    public void setExpenseSubcatName(String expenseSubcatName) {
        this.expenseSubcatName = expenseSubcatName;
    }

    public int getExpense_cat_id() {
        return expense_cat_id;
    }

    public void setExpense_cat_id(int expense_cat_id) {
        this.expense_cat_id = expense_cat_id;
    }
}
