package com.example.doctorBudget;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.doctorBudget.DAOs.CurrencyDao;
import com.example.doctorBudget.DAOs.ExpenseCategoryDao;
import com.example.doctorBudget.DAOs.ExpenseDao;
import com.example.doctorBudget.DAOs.ExpenseSubcategoryDao;
import com.example.doctorBudget.DAOs.IncomeCategoryDao;
import com.example.doctorBudget.DAOs.IncomeDao;
import com.example.doctorBudget.DAOs.IncomeSubcategoryDao;
import com.example.doctorBudget.DAOs.PersonalFinanceSourceCategoryDao;
import com.example.doctorBudget.DAOs.PersonalFinanceSourceDao;
import com.example.doctorBudget.DAOs.UserDao;
import com.example.doctorBudget.DAOs.CountryDao;
import com.example.doctorBudget.Entities.Converters;
import com.example.doctorBudget.Entities.Country;
import com.example.doctorBudget.Entities.Currency;
import com.example.doctorBudget.Entities.Expense;
import com.example.doctorBudget.Entities.ExpenseCategory;
import com.example.doctorBudget.Entities.ExpenseSubcategory;
import com.example.doctorBudget.Entities.Income;
import com.example.doctorBudget.Entities.IncomeCategory;
import com.example.doctorBudget.Entities.IncomeSubcategory;
import com.example.doctorBudget.Entities.PersonalFinanceSource;
import com.example.doctorBudget.Entities.PersonalFinanceSourceCategory;
import com.example.doctorBudget.Entities.User;

// Add database entities
@Database(entities = {User.class, Country.class, Currency.class, IncomeCategory.class,
                    IncomeSubcategory.class, PersonalFinanceSource.class, PersonalFinanceSourceCategory.class,
                    Income.class, ExpenseCategory.class, ExpenseSubcategory.class, Expense.class},  version = 2)
@TypeConverters({Converters.class})
public abstract class RoomDB extends RoomDatabase {
    //Create database instance
    private static RoomDB database;
    private static String DATABASE_NAME ="LicentaDB";

    public synchronized static RoomDB getInstance(Context context){
        //Check condition
        if(database==null){
            //When database is null
            // Inistialize database
            database = Room.databaseBuilder(context.getApplicationContext(),
                    RoomDB.class,DATABASE_NAME)
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration().build();
        }
        return database;

    }

    //Create Dao as an abstract method
    public abstract UserDao userDao();
    public abstract CountryDao countryDao();
    public abstract CurrencyDao currencyDao();
    public abstract IncomeCategoryDao incomeCategoryDao();
    public abstract IncomeSubcategoryDao incomeSubcategoryDao();
    public abstract PersonalFinanceSourceDao personalFinanceSourceDao();
    public abstract PersonalFinanceSourceCategoryDao personalFinanceSourceCategoryDao();
    public abstract IncomeDao incomeDao();
    public abstract ExpenseCategoryDao expenseCategoryDao();
    public abstract ExpenseSubcategoryDao expenseSubcategoryDao();
    public abstract ExpenseDao expenseDao();
}
