package com.example.doctorBudget.Activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.doctorBudget.MyCalendar;
import com.example.doctorBudget.RoomDB;
import com.example.doctorBudget.R;
import com.example.doctorBudget.TopExpenses;
import com.example.doctorBudget.TopPFSIncome;
import com.example.doctorBudget.TopPSFExpense;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BalanceActivity extends AppCompatActivity {

    RoomDB database;
    Button btn_new_income, btn_finance_source, btn_new_expense, btn_change_date_for_balance,
            btn_details_balance, btn_switch_barChart;
    TextView txt_view_message_balance,txt_view_last_month_bal, txt_view_this_month_bal,
            txt_view_message_expense_sum, txt_view_message_income_sum, txt_view_expense_sum,
            txt_view_income_sum, txt_view_required_dates_bl,txt_view_expense_result,
            txt_view_top_title;
    EditText edt_txt_balance_date_select_1, edt_txt_balance_date_select_2;
    LinearLayout up_lin_lay_balance, down_lin_lay_balance;
    BarChart bar_chart_exp, bar_chart_PFS;
    ImageView btn_close_details;

    int user_id = 0, current_month, next_month, current_year;;
    String user_country ="country" , s_select_date1 ="", s_select_date2 ="", currency;
    LocalDate today, date_next_month;
    MyCalendar calendar;
    Date this_month_q, next_month_q, select_date1, select_date2;
    double inc_sum, exp_sum_for_period, ramaining_sum, exp_sum_from_beginning;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_balance);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        database = RoomDB.getInstance(this);

        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            user_id = extras.getInt("id");
            user_country = extras.getString("country");
        }

        btn_new_income = findViewById(R.id.btn_new_income);
        btn_finance_source=findViewById(R.id.btn_finance_sourse_config);
        btn_new_expense=findViewById(R.id.btn_new_expense);
        btn_change_date_for_balance = findViewById(R.id.btn_change_date_for_balance);
        btn_details_balance = findViewById(R.id.btn_details_balance);
        btn_switch_barChart= findViewById(R.id.btn_switch_barChart);
        btn_close_details = findViewById(R.id.btn_close_details);

        txt_view_message_balance = findViewById(R.id.txt_view_message_balance);
        txt_view_this_month_bal=findViewById(R.id.txt_view_this_month_bal);
        txt_view_last_month_bal=findViewById(R.id.txt_view_last_month_bal);
        txt_view_message_income_sum = findViewById(R.id.txt_view_message_income_sum);
        txt_view_message_expense_sum = findViewById(R.id.txt_view_message_expense_sum);
        txt_view_income_sum = findViewById(R.id.txt_view_income_sum);
        txt_view_expense_sum = findViewById(R.id.txt_view_expense_sum);
        txt_view_required_dates_bl = findViewById(R.id.txt_view_required_dates_bl);
        txt_view_expense_result = findViewById(R.id.txt_view_expense_result);
        txt_view_top_title = findViewById(R.id.txt_view_top_title);

        edt_txt_balance_date_select_1 = findViewById(R.id.edt_txt_balance_date_selection_1);
        edt_txt_balance_date_select_2 = findViewById(R.id.edt_txt_balance_date_selection_2);

        up_lin_lay_balance = findViewById(R.id.up_lin_lay_balance);
        down_lin_lay_balance =findViewById(R.id.down_lin_lay_balance);

        bar_chart_exp = findViewById(R.id.barChartExp);
        bar_chart_PFS = findViewById(R.id.barChartPFS);

        down_lin_lay_balance.setVisibility(View.GONE);

        calendar = new MyCalendar() ;

        prepareDatesForInitialBalance();
        getTheSums(this_month_q, this_month_q, next_month_q);
        calendar.prepareCalendar(edt_txt_balance_date_select_1,BalanceActivity.this);
        calendar.prepareCalendar(edt_txt_balance_date_select_2, BalanceActivity.this);
        currency = getCurrency();

        btn_new_income.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent income_activity_intent = new Intent(BalanceActivity.this,IncomeActivity.class);
                income_activity_intent.putExtra("userID", user_id);
                income_activity_intent.putExtra("userCountry", user_country);
                startActivity(income_activity_intent);
            }
        });

        btn_finance_source.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent finance_source_activity_intent = new Intent(BalanceActivity.this,FinaceSourceActivity.class);
                finance_source_activity_intent.putExtra("userId", user_id);
                startActivity(finance_source_activity_intent);
            }
        });

        btn_new_expense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent expense_activity_intent = new Intent(BalanceActivity.this,ExpenseActivity.class);
                expense_activity_intent.putExtra("userCountry", user_country);
                expense_activity_intent.putExtra("userID", user_id);
                startActivity(expense_activity_intent);
            }
        });

        btn_change_date_for_balance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeDatesForBalance();
            }
        });

        btn_details_balance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                up_lin_lay_balance.setVisibility(View.GONE);
                down_lin_lay_balance.setVisibility(View.VISIBLE);
                detailBalance();
            }
        });

        btn_close_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                down_lin_lay_balance.setVisibility(View.GONE);
                up_lin_lay_balance.setVisibility(View.VISIBLE);
            }
        });

        btn_switch_barChart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(btn_switch_barChart.getText().equals(getResources().getString(R.string.btn_top_exp))){
                    btn_switch_barChart.setText(getResources().getString(R.string.btn_top_psf));
                    txt_view_top_title.setText(getResources().getString(R.string.txt_view_expense_top));
                    bar_chart_PFS.setVisibility(View.GONE);
                    bar_chart_exp.setVisibility(View.VISIBLE);
                }else
                {
                    btn_switch_barChart.setText(getResources().getString(R.string.btn_top_exp));
                    txt_view_top_title.setText(getResources().getString(R.string.txt_view_pfs_top));
                    bar_chart_exp.setVisibility(View.GONE);
                    bar_chart_PFS.setVisibility(View.VISIBLE);
                }

            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void prepareDatesForInitialBalance(){
        today = LocalDate.now();
        date_next_month = today.plusMonths(1);
        current_month = today.getMonthValue();
        current_year = today.getYear();
        next_month = date_next_month.getMonthValue();

        String sThisMonth = String.valueOf(current_month);
        String sNextMonth = String.valueOf(next_month);
        String sThisYear = String.valueOf(current_year);
        String sDateThisMonth ="";
        String sDateNextMonth ="";

        if(current_month <10) {
            sDateThisMonth = "01-" +"0"+ sThisMonth+"-"+sThisYear;
        }
        else
            sDateThisMonth = "01-" + sThisMonth+"-"+sThisYear;

        if(next_month < 10){
            sDateNextMonth = "01-" +"0"+ sNextMonth+"-"+sThisYear;
        }
        else
            sDateNextMonth = "01-" + sNextMonth+"-"+sThisYear;

        txt_view_this_month_bal.setText(sDateNextMonth);
        txt_view_last_month_bal.setText(sDateThisMonth);

        edt_txt_balance_date_select_1.setHint(sDateThisMonth);
        edt_txt_balance_date_select_2.setHint(sDateNextMonth);

        try {
            next_month_q = new SimpleDateFormat("dd-MM-yyyy").parse(sDateNextMonth);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        try {
            this_month_q = new SimpleDateFormat("dd-MM-yyyy").parse(sDateThisMonth);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        select_date1 = this_month_q;
        select_date2 = next_month_q;
    }

    public void getTheSums(Date firstDayOfMontth, Date date1, Date date2){
        inc_sum = database.incomeDao().getIncomeSumByDatesAndUser(user_id,firstDayOfMontth,date2);
        exp_sum_for_period = database.expenseDao().getExpenseSumByDatesAndUser(user_id,date1,date2);
        exp_sum_from_beginning = database.expenseDao().getExpenseSumByDatesAndUser(user_id,firstDayOfMontth,date2);
        ramaining_sum = inc_sum - exp_sum_from_beginning;
        String sExpenseSum=String.valueOf(exp_sum_for_period)+" "+ currency;
        String sRamainingSum = String.valueOf(ramaining_sum) +" "+ currency;
        txt_view_income_sum.setText(sRamainingSum);
        txt_view_expense_sum.setText(sExpenseSum);
    }

    public String getCurrency(){
        String currecyCode = database.countryDao().getCurrencyCode(user_country);
        String currencyName = database.currencyDao().getCurrencyName(currecyCode);
        return currencyName;
    }



    public void changeDatesForBalance(){
        s_select_date1 = edt_txt_balance_date_select_1.getText().toString();
        try {
            select_date1 = new SimpleDateFormat("dd-MM-yyyy").parse(s_select_date1);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        s_select_date2 = edt_txt_balance_date_select_2.getText().toString();
        try {
            select_date2 = new SimpleDateFormat("dd-MM-yyyy").parse(s_select_date2);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if(!s_select_date1.equals("") && !s_select_date2.equals("")){
            getTheSums(this_month_q, select_date1, select_date2);
            txt_view_required_dates_bl.setText("");
        }
        else
        {
            txt_view_required_dates_bl.setText(getResources().getString(R.string.txt_view_required_dates));
        }

        txt_view_this_month_bal.setText(s_select_date2);
        txt_view_last_month_bal.setText(s_select_date1);
    }


    public void detailBalance (){
        List<TopExpenses> expensesTopList;
        List<TopPFSIncome> pfsIncomeTopList;
        List<TopPSFExpense> pfsExpenseTopList;
        expensesTopList= database.expenseDao().getExpensesTopByUser(user_id, select_date1, select_date2);
        pfsIncomeTopList=database.personalFinanceSourceDao().getPFSIncomeTopByUser(user_id, this_month_q, select_date2);
        pfsExpenseTopList=database.personalFinanceSourceDao().getPFSExpenseTopByUser(user_id, this_month_q, select_date2);
        createBarChartExpUser(expensesTopList,getResources().getString(R.string.expChart_label));
        createBarChartPFSUser (pfsIncomeTopList,pfsExpenseTopList,getResources().getString(R.string.pfsChart_label));

    }

    public void createBarChartExpUser(List<TopExpenses> expensesTopList, String chartLabel){
        ArrayList<BarEntry> barEntryArrayList = new ArrayList<>();
        ArrayList<String> labelsNames = new ArrayList<>();
        for(int i=0;i<expensesTopList.size();i++) {
            barEntryArrayList.add(new BarEntry(i, expensesTopList.get(i).getSumAmountExpCat().floatValue()));
            labelsNames.add(expensesTopList.get(i).getExpenseSubcatName());
        }
        BarDataSet barDataSet = new BarDataSet(barEntryArrayList, chartLabel+"  ("+currency+")");
        barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        Description description = new Description();
        description.setText("");
        bar_chart_exp.setDescription(description);
        BarData barData = new BarData(barDataSet);
        bar_chart_exp.setData(barData);

        XAxis xAxis = bar_chart_exp.getXAxis();
        xAxis.setValueFormatter( new IndexAxisValueFormatter(labelsNames));

        xAxis.setPosition(XAxis.XAxisPosition.TOP);
        xAxis.setDrawAxisLine(false);
        xAxis.setDrawAxisLine(false);
        xAxis.setGranularity(1f);
        xAxis.setLabelCount(labelsNames.size());
        xAxis.setLabelRotationAngle(320);

        bar_chart_exp.getXAxis().setTextSize(13f);
        barDataSet.setValueTextSize(16f);
    }

    public void createBarChartPFSUser(List<TopPFSIncome> pfsIncomeTopList, List<TopPSFExpense> pfsExpenseTopList, String chartLabel){
        ArrayList<BarEntry> barEntryArrayList = new ArrayList<>();
        ArrayList<String> labelsNames = new ArrayList<>();
        double restSum=0;
        int ok=0;
        if(pfsExpenseTopList.size()>0)
            for(int i=0;i<pfsIncomeTopList.size();i++) {
               for (int j=0;j<pfsExpenseTopList.size();j++){
                if(pfsIncomeTopList.get(i).getPsfID()==pfsExpenseTopList.get(j).getPsfID()) {
                    restSum = pfsIncomeTopList.get(i).getSumAmountInc() - pfsExpenseTopList.get(j).getSumAmountExp();
                    ok=1;
                }else if(ok==0)
                    restSum =pfsIncomeTopList.get(i).getSumAmountInc()-0;
               }
                barEntryArrayList.add(new BarEntry(i, (float) restSum));
                labelsNames.add(pfsIncomeTopList.get(i).getPfsName());
            }
        else
            for (int i=0;i<pfsIncomeTopList.size();i++) {
                double sum  =  pfsIncomeTopList.get(i).getSumAmountInc();
                barEntryArrayList.add(new BarEntry(i, (float) sum ));
                labelsNames.add(pfsIncomeTopList.get(i).getPfsName());
            }
        BarDataSet barDataSet = new BarDataSet(barEntryArrayList, chartLabel+"  ("+currency+")");
        barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        Description description = new Description();
        description.setText("");
        bar_chart_PFS.setDescription(description);
        BarData barData = new BarData(barDataSet);
        bar_chart_PFS.setData(barData);

        XAxis xAxis = bar_chart_PFS.getXAxis();
        xAxis.setValueFormatter( new IndexAxisValueFormatter(labelsNames));

        xAxis.setPosition(XAxis.XAxisPosition.TOP);
        xAxis.setDrawAxisLine(false);
        xAxis.setDrawAxisLine(false);
        xAxis.setGranularity(1f);
        xAxis.setLabelCount(labelsNames.size());
        xAxis.setLabelRotationAngle(320);

        bar_chart_PFS.getXAxis().setTextSize(13f);
        barDataSet.setValueTextSize(16f);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!s_select_date1.equals("") && !s_select_date2.equals("")){
            getTheSums(this_month_q, select_date1, select_date2);
       }
        else
        {
            getTheSums(this_month_q, this_month_q, next_month_q);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home_button, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        if (id == R.id.btn_home) {
            Intent main_activity_intent = new Intent(BalanceActivity.this, MainActivity.class);
            startActivity(main_activity_intent);

        }

        return super.onOptionsItemSelected(item);
    }


}