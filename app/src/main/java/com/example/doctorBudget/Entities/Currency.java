package com.example.doctorBudget.Entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Currency {
    @NonNull
    @PrimaryKey
    private String currencyCode;
    private String currencyName;

    public Currency(@NonNull String currencyCode, String currencyName) {
        this.currencyCode = currencyCode;
        this.currencyName = currencyName;
    }

    @NonNull
    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(@NonNull String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getCurrencyName() {
        return currencyName;
    }

    public void setCurrencyName(String currencyName) {
        this.currencyName = currencyName;
    }
}