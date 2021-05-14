package com.example.doctorBudget.Entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import static androidx.room.ForeignKey.CASCADE;

@Entity(foreignKeys = {
            @ForeignKey(entity = PersonalFinanceSourceCategory.class,
                        parentColumns = "finance_source_cat_id",
                        childColumns = "finance_source_cat_id_pfs",
                        onDelete = CASCADE),
            @ForeignKey(entity = User.class,
                        parentColumns = "userID",
                        childColumns ="user_id_pfs",
                        onDelete = CASCADE)
        })
public class PersonalFinanceSource {
    @NonNull
    @PrimaryKey(autoGenerate = true)
    private int finance_source_id;
    private int user_id_pfs;
    private String finance_source_name;
    private String note;
    private int finance_source_cat_id_pfs;


    public int getFinance_source_id() {
        return finance_source_id;
    }

    public void setFinance_source_id(int finance_source_id) {
        this.finance_source_id = finance_source_id;
    }

    public int getUser_id_pfs() {
        return user_id_pfs;
    }

    public void setUser_id_pfs(int user_id_pfs) {
        this.user_id_pfs = user_id_pfs;
    }

    public String getFinance_source_name() {
        return finance_source_name;
    }

    public void setFinance_source_name(String finance_source_name) {
        this.finance_source_name = finance_source_name;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public int getFinance_source_cat_id_pfs() {
        return finance_source_cat_id_pfs;
    }

    public void setFinance_source_cat_id_pfs(int finance_source_cat_id_s) {
        this.finance_source_cat_id_pfs = finance_source_cat_id_s;
    }


}
