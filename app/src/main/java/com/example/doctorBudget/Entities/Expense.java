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
                childColumns ="user_id_exp",
                onDelete = CASCADE),
        @ForeignKey(entity = PersonalFinanceSource.class,
                parentColumns = "finance_source_id",
                childColumns ="finance_source_id_exp",
                onDelete = CASCADE),
        @ForeignKey(entity = ExpenseSubcategory.class,
                parentColumns = "ID_subCat_expense",
                childColumns ="subcat_expense_id_exp",
                onDelete = CASCADE),
})
public class Expense {
    @NonNull
    @PrimaryKey(autoGenerate = true)
    private int expense_id;
    private int user_id_exp;
    private double amount_exp;
    @TypeConverters({Converters.class})
    private Date date_of_registration_exp;
    private int recurrent_exp;
    private String note_exp;
    @TypeConverters({Converters.class})
    private Bitmap attachment_exp;
    private int subcat_expense_id_exp;
    private int finance_source_id_exp;

    public int getExpense_id() {
        return expense_id;
    }

    public void setExpense_id(int expense_id) {
        this.expense_id = expense_id;
    }

    public int getUser_id_exp() {
        return user_id_exp;
    }

    public void setUser_id_exp(int user_id_exp) {
        this.user_id_exp = user_id_exp;
    }

    public double getAmount_exp() {
        return amount_exp;
    }

    public void setAmount_exp(double amount_exp) {
        this.amount_exp = amount_exp;
    }

    public Date getDate_of_registration_exp() {
        return date_of_registration_exp;
    }

    public void setDate_of_registration_exp(Date date_of_registration_exp) {
        this.date_of_registration_exp = date_of_registration_exp;
    }

    public int getRecurrent_exp() {
        return recurrent_exp;
    }

    public void setRecurrent_exp(int recurrent_exp) {
        this.recurrent_exp = recurrent_exp;
    }

    public String getNote_exp() {
        return note_exp;
    }

    public void setNote_exp(String note_exp) {
        this.note_exp = note_exp;
    }

    public Bitmap getAttachment_exp() {
        return attachment_exp;
    }

    public void setAttachment_exp(Bitmap attachment_exp) {
        this.attachment_exp = attachment_exp;
    }

    public int getSubcat_expense_id_exp() {
        return subcat_expense_id_exp;
    }

    public void setSubcat_expense_id_exp(int subcat_expense_id_exp) {
        this.subcat_expense_id_exp = subcat_expense_id_exp;
    }

    public int getFinance_source_id_exp() {
        return finance_source_id_exp;
    }

    public void setFinance_source_id_exp(int finance_source_id_exp) {
        this.finance_source_id_exp = finance_source_id_exp;
    }
}
