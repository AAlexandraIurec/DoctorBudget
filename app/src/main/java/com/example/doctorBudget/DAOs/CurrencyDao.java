package com.example.doctorBudget.DAOs;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.doctorBudget.Entities.Currency;

import java.util.List;

@Dao
public interface CurrencyDao {
    @Insert
    void insertCurrency(Currency currecy);


    @Query("SELECT * FROM Currency")
    List<Currency> getAllCurrencies();


    @Query("SELECT currencyName FROM Currency WHERE currencyCode=:currencyCodeC")
    String getCurrencyName (String currencyCodeC);

}
