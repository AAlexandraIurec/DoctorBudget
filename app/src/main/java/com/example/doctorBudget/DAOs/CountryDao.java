package com.example.doctorBudget.DAOs;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.doctorBudget.Entities.Country;


import java.util.List;

import static androidx.room.OnConflictStrategy.REPLACE;
@Dao
public interface CountryDao {

    @Insert(onConflict = REPLACE)
    void insertCountry(Country country);

    @Query("SELECT countryName FROM Country ORDER BY countryName desc")
    List<String> getAllCountryNames();

    @Query("SELECT * FROM Country")
    List<Country> getAllCountries();

    @Query("SELECT currencyCodeC FROM Country WHERE countryName =:userCountry")
    String getCurrencyCode (String userCountry);
}
