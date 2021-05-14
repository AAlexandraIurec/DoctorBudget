package com.example.doctorBudget.Entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import static androidx.room.ForeignKey.CASCADE;

@Entity (foreignKeys = @ForeignKey(entity = Currency.class,
                                    parentColumns = "currencyCode",
                                    childColumns = "currencyCodeC",
                                    onDelete = CASCADE),
        indices = {@Index(value = {"countryName"},
                unique = true)})
public class Country {
    @NonNull
    @PrimaryKey
    private String code;
    private String countryName;
    private String currencyCodeC;

    public Country(@NonNull String code, String countryName, String currencyCodeC) {
        this.code = code;
        this.countryName = countryName;
        this.currencyCodeC = currencyCodeC;
    }

    @NonNull
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getCurrencyCodeC() {
        return currencyCodeC;
    }

    public void setCurrencyCodeC(String currencyCodeC) {
        this.currencyCodeC = currencyCodeC;
    }
}