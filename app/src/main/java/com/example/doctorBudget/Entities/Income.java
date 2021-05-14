package com.example.doctorBudget.Entities;

import android.graphics.Bitmap;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.util.Date;

import static androidx.room.ForeignKey.CASCADE;

@Entity(foreignKeys = {
        @ForeignKey(entity = User.class,
                parentColumns = "userID",
                childColumns ="user_id_inc",
                onDelete = CASCADE),
        @ForeignKey(entity = PersonalFinanceSource.class,
                parentColumns = "finance_source_id",
                childColumns ="finance_source_id_inc",
                onDelete = CASCADE),
        @ForeignKey(entity = IncomeSubcategory.class,
                parentColumns = "ID_subCat_income",
                childColumns ="subcat_income_id_inc",
                onDelete = CASCADE),
        })
public class Income {
    @NonNull
    @PrimaryKey(autoGenerate = true)
    private int income_id;
    private int user_id_inc;
    private double amount_inc;
    @TypeConverters({Converters.class})
    private Date date_of_registration_inc;
    private int recurrent_inc;
    private String note_inc;
    @TypeConverters({Converters.class})
    private Bitmap attachment_inc;
    private int subcat_income_id_inc;
    private int finance_source_id_inc;


    public int getIncome_id() {
        return income_id;
    }

    public void setIncome_id(int income_id) {
        this.income_id = income_id;
    }

    public int getUser_id_inc() {
        return user_id_inc;
    }

    public void setUser_id_inc(int user_id_inc) {
        this.user_id_inc = user_id_inc;
    }

    public double getAmount_inc() {
        return amount_inc;
    }

    public void setAmount_inc(double amount_inc) {
        this.amount_inc = amount_inc;
    }

    public Date getDate_of_registration_inc() {
        return date_of_registration_inc;
    }

    public void setDate_of_registration_inc(Date date_of_registration_inc) {
        this.date_of_registration_inc = date_of_registration_inc;
    }

    public int getRecurrent_inc() {
        return recurrent_inc;
    }

    public void setRecurrent_inc(int recurrent_inc) {
        this.recurrent_inc = recurrent_inc;
    }

    public String getNote_inc() {
        return note_inc;
    }

    public void setNote_inc(String note_inc) {
        this.note_inc = note_inc;
    }

    public Bitmap getAttachment_inc() {
        return attachment_inc;
    }

    public void setAttachment_inc(Bitmap attachment_inc) {
        this.attachment_inc = attachment_inc;
    }

    public int getSubcat_income_id_inc() {
        return subcat_income_id_inc;
    }

    public void setSubcat_income_id_inc(int subcat_income_id_inc) {
        this.subcat_income_id_inc = subcat_income_id_inc;
    }

    public int getFinance_source_id_inc() {
        return finance_source_id_inc;
    }

    public void setFinance_source_id_inc(int finance_source_id_inc) {
        this.finance_source_id_inc = finance_source_id_inc;
    }
}


