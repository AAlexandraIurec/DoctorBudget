package com.example.doctorBudget.Entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class PersonalFinanceSourceCategory {
    @NonNull
    @PrimaryKey(autoGenerate = true)
    private int finance_source_cat_id;
    private String finance_source_category_name;

    public PersonalFinanceSourceCategory(String finance_source_category_name) {
        this.finance_source_category_name = finance_source_category_name;
    }

    public int getFinance_source_cat_id() {
        return finance_source_cat_id;
    }

    public void setFinance_source_cat_id(int finance_source_cat_id) {
        this.finance_source_cat_id = finance_source_cat_id;
    }

    public String getFinance_source_category_name() {
        return finance_source_category_name;
    }

    public void setFinance_source_category_name(String finance_source_category_name) {
        this.finance_source_category_name = finance_source_category_name;
    }
}
