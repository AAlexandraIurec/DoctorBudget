package com.example.doctorBudget.Activities;

import android.content.Intent;
import android.os.Bundle;

import com.example.doctorBudget.RecyclerViews.RecycleViewUsersAdaptor;
import com.example.doctorBudget.RoomDB;
import com.example.doctorBudget.Entities.Country;
import com.example.doctorBudget.Entities.Currency;
import com.example.doctorBudget.Entities.ExpenseCategory;
import com.example.doctorBudget.Entities.ExpenseSubcategory;
import com.example.doctorBudget.Entities.IncomeCategory;
import com.example.doctorBudget.Entities.IncomeSubcategory;
import com.example.doctorBudget.Entities.PersonalFinanceSourceCategory;
import com.example.doctorBudget.Entities.User;
import com.example.doctorBudget.R;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;

import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    RoomDB database;

    Button btn_new_user, btn_total_balance;
    TextView  txt_view_message;

    List<Currency>currencyList = new ArrayList<>();
    List<User>userList = new ArrayList<>();

    RecyclerView recyclerViewUsers;
    RecycleViewUsersAdaptor.RecyclerViewClickListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        database = RoomDB.getInstance(this);
        userList = database.userDao().getAllUsers();
        currencyList= database.currencyDao().getAllCurrencies();

        txt_view_message = findViewById(R.id.txt_view_message_mainAC);

        btn_new_user =  findViewById(R.id.btn_new_user);
        btn_total_balance = findViewById(R.id.btn_total_balance);

        recyclerViewUsers = findViewById(R.id.recycler_view_user);

        btn_new_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent user_activity_intent = new Intent(MainActivity.this, UserActivity.class);
                startActivity(user_activity_intent);

            }
        });

        btn_total_balance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent total_bl_activity_intent = new Intent(MainActivity.this, BalanceOfAccountsActivity.class);
                startActivity(total_bl_activity_intent);
            }
        });

        initialInserts();
        setInitialMessages();
        configRecyclerViewUserAndSetAdapter();

    }

    private void configRecyclerViewUserAndSetAdapter (){
        clickOnItemRecycleViewUser();
        RecycleViewUsersAdaptor userAdapter = new RecycleViewUsersAdaptor(MainActivity.this,userList,listener);
        RecyclerView.LayoutManager linearLayoutManager = new LinearLayoutManager(MainActivity.this);
        recyclerViewUsers.setLayoutManager(linearLayoutManager);
        recyclerViewUsers.setAdapter(userAdapter);
    }

    private void clickOnItemRecycleViewUser() {
        listener = new RecycleViewUsersAdaptor.RecyclerViewClickListener() {
            @Override
            public void onClick(View v, int position) {
                Intent balance_activity_intent = new Intent(MainActivity.this,BalanceActivity.class);
                balance_activity_intent.putExtra("id", userList.get(position).getUserID());
                balance_activity_intent.putExtra( "country",userList.get(position).getCountry());
                startActivity(balance_activity_intent);
            }
        };
    }

    private void initialInserts () {
        if(currencyList.size()==0) {
            insertCurrencies ();
            insertCountries ();
            insertIncomeCategories();
            insertIncomeSubcategories();
            insertPersonalFinanceSourceCategories();
            insertExpenseCategories();
            insertExpenseSubcategories();
        }
    }
    private void insertCurrencies (){
        Currency currencyRO = new Currency("RON", "Lei" );
        Currency currencyMD = new Currency("MDL", "Lei moldovenești");

        database.currencyDao().insertCurrency(currencyRO);
        database.currencyDao().insertCurrency(currencyMD);
    }

    private void insertCountries (){
        Country countryMD = new Country("MD", "Republica Moldova", "MDL");
        Country countryRO = new Country("RO", "România", "RON");

        database.countryDao().insertCountry(countryRO);
        database.countryDao().insertCountry(countryMD);

    }

    private void insertIncomeCategories(){
        IncomeCategory fixed_income = new IncomeCategory("Venit fix");
        IncomeCategory variable_income = new IncomeCategory("Venit variabil");

        database.incomeCategoryDao().insertIncomeCategory(fixed_income);
        database.incomeCategoryDao().insertIncomeCategory(variable_income);
    }

    private void insertIncomeSubcategories(){
        IncomeSubcategory salary = new IncomeSubcategory("Salariu", 1);
        IncomeSubcategory scholarship = new IncomeSubcategory("Bursă", 1);
        IncomeSubcategory allowance = new IncomeSubcategory("Alocație", 1);
        IncomeSubcategory meal_vouchers = new IncomeSubcategory("Tichete de masă", 1);
        IncomeSubcategory others = new IncomeSubcategory("Altele", 1);

        IncomeSubcategory loan = new IncomeSubcategory("Împrumut", 2);
        IncomeSubcategory bonus = new IncomeSubcategory("Primă", 2);
        IncomeSubcategory othersV = new IncomeSubcategory("Altele", 2);

        database.incomeSubcategoryDao().insertIncomeSubcategory(salary);
        database.incomeSubcategoryDao().insertIncomeSubcategory(scholarship);
        database.incomeSubcategoryDao().insertIncomeSubcategory(allowance);
        database.incomeSubcategoryDao().insertIncomeSubcategory(meal_vouchers);
        database.incomeSubcategoryDao().insertIncomeSubcategory(others);
        database.incomeSubcategoryDao().insertIncomeSubcategory(loan);
        database.incomeSubcategoryDao().insertIncomeSubcategory(bonus);
        database.incomeSubcategoryDao().insertIncomeSubcategory(othersV);

    }

    private void insertPersonalFinanceSourceCategories(){
        PersonalFinanceSourceCategory cash =new PersonalFinanceSourceCategory("Portofel");
        PersonalFinanceSourceCategory debit_card =new PersonalFinanceSourceCategory("Card de debit");
        PersonalFinanceSourceCategory meal_vouchers =new PersonalFinanceSourceCategory("Tichete de masă");
        PersonalFinanceSourceCategory credit_card =new PersonalFinanceSourceCategory("Card de credit");
        PersonalFinanceSourceCategory othersPFS =new PersonalFinanceSourceCategory("Altele");

        database.personalFinanceSourceCategoryDao().insertPersonalFinanceSourecCategory(cash);
        database.personalFinanceSourceCategoryDao().insertPersonalFinanceSourecCategory(debit_card);
        database.personalFinanceSourceCategoryDao().insertPersonalFinanceSourecCategory(meal_vouchers);
        database.personalFinanceSourceCategoryDao().insertPersonalFinanceSourecCategory(credit_card);
        database.personalFinanceSourceCategoryDao().insertPersonalFinanceSourecCategory(othersPFS);
    }

    private void insertExpenseCategories(){
        ExpenseCategory fixed_expense = new ExpenseCategory("Cheltuială fixă");
        ExpenseCategory variable_expense = new ExpenseCategory("Cheltuială variabilă");
        ExpenseCategory discretionary_expense = new ExpenseCategory("Cheltuială discreționară");

        database.expenseCategoryDao().insertExpenseCategory(fixed_expense);
        database.expenseCategoryDao().insertExpenseCategory(variable_expense);
        database.expenseCategoryDao().insertExpenseCategory(discretionary_expense);
    }

    private void insertExpenseSubcategories(){
        ExpenseSubcategory mortgage = new ExpenseSubcategory("Rată", 1);
        ExpenseSubcategory rent = new ExpenseSubcategory("Chirie", 1);
        ExpenseSubcategory contributionsToTheState = new ExpenseSubcategory("Contribuții la stat", 1);
        ExpenseSubcategory schooling = new ExpenseSubcategory("Școlarizare", 1);
        ExpenseSubcategory others = new ExpenseSubcategory("Altele", 1);

        ExpenseSubcategory food = new ExpenseSubcategory("Alimente", 2);
        ExpenseSubcategory beverages = new ExpenseSubcategory("Băuturi", 2);
        ExpenseSubcategory alcoholicBeverages = new ExpenseSubcategory("Băuturi alcoolice", 2);
        ExpenseSubcategory cigarettes = new ExpenseSubcategory("Țigări", 2);
        ExpenseSubcategory fuel = new ExpenseSubcategory("Carburant", 2);
        ExpenseSubcategory maintenance = new ExpenseSubcategory("Întreținere", 2);
        ExpenseSubcategory TVInternet = new ExpenseSubcategory("TV-Internet", 2);
        ExpenseSubcategory telephony = new ExpenseSubcategory("Telefonie", 2);
        ExpenseSubcategory othersV = new ExpenseSubcategory("Altele", 2);

        ExpenseSubcategory clothing = new ExpenseSubcategory("Îmbracaminte", 3);
        ExpenseSubcategory careProducts = new ExpenseSubcategory("Produse de îngrijire", 3);
        ExpenseSubcategory carService = new ExpenseSubcategory("Service mașina", 3);
        ExpenseSubcategory physicalExamination = new ExpenseSubcategory("Control medical", 3);
        ExpenseSubcategory medicine = new ExpenseSubcategory("Medicamente", 3);
        ExpenseSubcategory transport = new ExpenseSubcategory("Transport", 3);
        ExpenseSubcategory restaurant = new ExpenseSubcategory("Restaurant", 3);
        ExpenseSubcategory entertainment = new ExpenseSubcategory("Divertisment", 3);
        ExpenseSubcategory culture = new ExpenseSubcategory("Cultura", 3);
        ExpenseSubcategory sport = new ExpenseSubcategory("Sport", 3);
        ExpenseSubcategory gifts = new ExpenseSubcategory("Cadouri", 3);
        ExpenseSubcategory vacation = new ExpenseSubcategory("Vacanță", 3);
        ExpenseSubcategory othersD = new ExpenseSubcategory("Altele", 3);

        database.expenseSubcategoryDao().insertExpenseSubcategory(mortgage);
        database.expenseSubcategoryDao().insertExpenseSubcategory(rent);
        database.expenseSubcategoryDao().insertExpenseSubcategory(contributionsToTheState);
        database.expenseSubcategoryDao().insertExpenseSubcategory(schooling);
        database.expenseSubcategoryDao().insertExpenseSubcategory(others);

        database.expenseSubcategoryDao().insertExpenseSubcategory(food);
        database.expenseSubcategoryDao().insertExpenseSubcategory(beverages);
        database.expenseSubcategoryDao().insertExpenseSubcategory(alcoholicBeverages);
        database.expenseSubcategoryDao().insertExpenseSubcategory(cigarettes);
        database.expenseSubcategoryDao().insertExpenseSubcategory(fuel);
        database.expenseSubcategoryDao().insertExpenseSubcategory(maintenance);
        database.expenseSubcategoryDao().insertExpenseSubcategory(TVInternet);
        database.expenseSubcategoryDao().insertExpenseSubcategory(telephony);
        database.expenseSubcategoryDao().insertExpenseSubcategory(othersV);

        database.expenseSubcategoryDao().insertExpenseSubcategory(clothing);
        database.expenseSubcategoryDao().insertExpenseSubcategory(careProducts);
        database.expenseSubcategoryDao().insertExpenseSubcategory(carService);
        database.expenseSubcategoryDao().insertExpenseSubcategory(physicalExamination);
        database.expenseSubcategoryDao().insertExpenseSubcategory(medicine);
        database.expenseSubcategoryDao().insertExpenseSubcategory(transport);
        database.expenseSubcategoryDao().insertExpenseSubcategory(restaurant);
        database.expenseSubcategoryDao().insertExpenseSubcategory(entertainment);
        database.expenseSubcategoryDao().insertExpenseSubcategory(culture);
        database.expenseSubcategoryDao().insertExpenseSubcategory(sport);
        database.expenseSubcategoryDao().insertExpenseSubcategory(gifts);
        database.expenseSubcategoryDao().insertExpenseSubcategory(vacation);
        database.expenseSubcategoryDao().insertExpenseSubcategory(othersD);

    }

   public void setInitialMessages(){
      String firstMessage = getResources().getString(R.string.txt_view_first_message_mainAC);
      String secondMessage = getResources().getString(R.string.txt_view_second_message_mainAC);

       if(userList.size() == 0){
           txt_view_message.setText(firstMessage);
       }else{
           txt_view_message.setText(secondMessage);
       }
   }


}