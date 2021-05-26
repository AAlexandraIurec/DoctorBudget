package com.example.doctorBudget.Activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;

import com.example.doctorBudget.RoomDB;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class BalanceOfAccountsActivity extends AppCompatActivity {

    RoomDB database;
    TextView txt_view_message_balance_profiles,txt_view_last_month_bl_prof,txt_view_this_month_bl_prof,
            txt_view_message_expense_sum_bl_prof,txt_view_message_income_sum_bl_prof,
            txt_view_expense_sum_bl_prof,txt_view_income_sum_bl_prof,txt_view_required_dates_bl_prof,
            txt_view_message_change_period_bl_prof, txt_view_firs_message_balance_profiles,
            txt_view_expense_result_bl_prof, txt_view_top_title_prof;
    EditText edt_txt_balance_prof_date_selection_1,edt_txt_balance_prof_date_selection_2;
    Button btn_change_date_for_balance_prof, btn_details_balance_bl_prof, btn_switch_barChart_prof;
    LinearLayout up_lin_lay_balance_profiles, down_lin_lay_balance_prof;
    ImageView btn_close_details_prof;
    BarChart barChartExpProf, barChartPFSProf;

    LocalDate today,dateNextMonth;
    int currentMonth, nextMonth, currentYear;
    Date thisMonthQuery, nextMonthQuery, selectedDate1,selectedDate2;
    double incomeSum,expenseSumForPeriod,ramainingSum, expenseSumFromBeginning;
    String sSelectedDate1="",sSelectedDate2="";

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_balance_of_accounts);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        txt_view_message_balance_profiles = findViewById(R.id.txt_view_message_balance_profiles);
        txt_view_last_month_bl_prof = findViewById(R.id.txt_view_last_month_bl_prof);
        txt_view_this_month_bl_prof = findViewById(R.id.txt_view_this_month_bl_prof);
        txt_view_message_expense_sum_bl_prof = findViewById(R.id.txt_view_message_expense_sum_bl_prof);
        txt_view_message_income_sum_bl_prof = findViewById(R.id.txt_view_message_income_sum_bl_prof);
        txt_view_expense_sum_bl_prof = findViewById(R.id.txt_view_expense_sum_bl_prof);
        txt_view_income_sum_bl_prof = findViewById(R.id.txt_view_income_sum_bl_prof);
        txt_view_required_dates_bl_prof = findViewById(R.id.txt_view_required_dates_bl_prof);
        txt_view_message_change_period_bl_prof = findViewById(R.id.txt_view_required_dates_bl_prof);
        txt_view_firs_message_balance_profiles = findViewById(R.id.txt_view_firs_message_balance_profiles);
        txt_view_expense_result_bl_prof = findViewById(R.id.txt_view_expense_result_bl_prof);
        txt_view_top_title_prof = findViewById(R.id.txt_view_top_title_prof);

        edt_txt_balance_prof_date_selection_1 = findViewById(R.id.edt_txt_balance_prof_date_selection_1);
        edt_txt_balance_prof_date_selection_2 = findViewById(R.id.edt_txt_balance_prof_date_selection_2);

        btn_change_date_for_balance_prof = findViewById(R.id.btn_change_date_for_balance_prof);
        btn_details_balance_bl_prof = findViewById(R.id.btn_details_balance_bl_prof);
        btn_switch_barChart_prof = findViewById(R.id.btn_switch_barChart_prof);
        btn_close_details_prof = findViewById(R.id.btn_close_details_prof);

        up_lin_lay_balance_profiles = findViewById(R.id.up_lin_lay_balance_profiles);
        down_lin_lay_balance_prof = findViewById(R.id.down_lin_lay_balance_prof);

        barChartExpProf = findViewById(R.id.barChartExpProf);
        barChartPFSProf = findViewById(R.id.barChartPFSProf);

        down_lin_lay_balance_prof.setVisibility(View.GONE);

        database = RoomDB.getInstance(this);

        btn_change_date_for_balance_prof.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeDatesForBalanceProf();
            }
        });

        btn_details_balance_bl_prof.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                up_lin_lay_balance_profiles.setVisibility(View.GONE);
                down_lin_lay_balance_prof.setVisibility(View.VISIBLE);
                barChartPFSProf.setVisibility(View.GONE);
                detailBalanceProf();
            }
        });

        btn_close_details_prof.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                down_lin_lay_balance_prof.setVisibility(View.GONE);
                up_lin_lay_balance_profiles.setVisibility(View.VISIBLE);
            }
        });

        btn_switch_barChart_prof.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(btn_switch_barChart_prof.getText().equals(getResources().getString(R.string.btn_top_exp))){
                    btn_switch_barChart_prof.setText(getResources().getString(R.string.btn_top_psf));
                    txt_view_top_title_prof.setText(getResources().getString(R.string.txt_view_expense_top));
                    barChartPFSProf.setVisibility(View.GONE);
                    barChartExpProf.setVisibility(View.VISIBLE);
                }else
                {
                    btn_switch_barChart_prof.setText(getResources().getString(R.string.btn_top_exp));
                    txt_view_top_title_prof.setText(getResources().getString(R.string.txt_view_pfs_top));
                    barChartExpProf.setVisibility(View.GONE);
                    barChartPFSProf.setVisibility(View.VISIBLE);
                }

            }
        });


        prepareDatesForInitialBalanceProf();
        getTheSumsProf(thisMonthQuery,thisMonthQuery, nextMonthQuery);
        prepareCalendar(edt_txt_balance_prof_date_selection_1);
        prepareCalendar(edt_txt_balance_prof_date_selection_2);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void prepareDatesForInitialBalanceProf(){
        today = LocalDate.now();
        dateNextMonth = today.plusMonths(1);
        currentMonth = today.getMonthValue();
        currentYear = today.getYear();
        nextMonth = dateNextMonth.getMonthValue();

        String sThisMonth = String.valueOf(currentMonth);
        String sNextMonth = String.valueOf(nextMonth);
        String sThisYear = String.valueOf(currentYear);
        String sDateThisMonth ="";
        String sDateNextMonth ="";

        if(currentMonth <10) {
            sDateThisMonth = "01-" +"0"+ sThisMonth+"-"+sThisYear;
        }
        else
            sDateThisMonth = "01-" + sThisMonth+"-"+sThisYear;

        if(nextMonth < 10){
            sDateNextMonth = "01-" +"0"+ sNextMonth+"-"+sThisYear;
        }
        else
            sDateNextMonth = "01-" + sNextMonth+"-"+sThisYear;

        txt_view_last_month_bl_prof.setText(sDateThisMonth);
        txt_view_this_month_bl_prof.setText(sDateNextMonth);

        edt_txt_balance_prof_date_selection_1.setHint(sDateThisMonth);
        edt_txt_balance_prof_date_selection_2.setHint(sDateNextMonth);

        try {
            nextMonthQuery = new SimpleDateFormat("dd-MM-yyyy").parse(sDateNextMonth);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        try {
            thisMonthQuery = new SimpleDateFormat("dd-MM-yyyy").parse(sDateThisMonth);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        selectedDate1 = thisMonthQuery;
        selectedDate2 = nextMonthQuery;
    }

    public void getTheSumsProf(Date firstDayOfMonth,Date date1, Date date2){
        incomeSum = database.incomeDao().getIncomeSumByDates(firstDayOfMonth,date2);
        expenseSumForPeriod = database.expenseDao().getExpenseSumByDates(date1,date2);
        expenseSumFromBeginning = database.expenseDao().getExpenseSumByDates(firstDayOfMonth,date2);
        ramainingSum = incomeSum-expenseSumFromBeginning;
        String sExpenseSum=String.valueOf(expenseSumForPeriod)+ " Lei";
        String sRamainingSum = String.valueOf(ramainingSum)+ " Lei";
        txt_view_income_sum_bl_prof.setText(sRamainingSum);
        txt_view_expense_sum_bl_prof.setText(sExpenseSum);
    }

    public void prepareCalendar(EditText edt_txt) {
        //Working with the calendar
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        edt_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        BalanceOfAccountsActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        month = month+1;
                        if(month < 10) {
                            String sDate= dayOfMonth + "-" + "0" + month + "-" + year;
                            edt_txt.setText(sDate);
                        }
                        else {
                            String sDate = dayOfMonth + "-"  + month + "-" + year;
                            edt_txt.setText(sDate);
                        }
                    }
                }, year, month, day);
                datePickerDialog.show();
            }

        });
    }

    private void changeDatesForBalanceProf(){
        sSelectedDate1 = edt_txt_balance_prof_date_selection_1.getText().toString();
        try {
            selectedDate1 = new SimpleDateFormat("dd-MM-yyyy").parse(sSelectedDate1);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        sSelectedDate2 = edt_txt_balance_prof_date_selection_2.getText().toString();
        try {
            selectedDate2 = new SimpleDateFormat("dd-MM-yyyy").parse(sSelectedDate2);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if(!sSelectedDate1.equals("") && !sSelectedDate2.equals("")){
            getTheSumsProf(thisMonthQuery,selectedDate1,selectedDate2);
            txt_view_required_dates_bl_prof.setText("");
        }
        else
        {
            txt_view_required_dates_bl_prof.setText(getResources().getString(R.string.txt_view_required_dates));
        }
        txt_view_this_month_bl_prof.setText(sSelectedDate2);
        txt_view_last_month_bl_prof.setText(sSelectedDate1);

    }

    public void detailBalanceProf(){
        List<TopExpenses> expensesTopList;
        List<TopPFSIncome> pfsIncomeTopList;
        List<TopPSFExpense> pfsExpenseTopList;
        expensesTopList= database.expenseDao().getExpensesTop(selectedDate1, selectedDate2);
        createBarChartExp(expensesTopList,getResources().getString(R.string.expChart_label));
        pfsIncomeTopList=database.personalFinanceSourceDao().getPFSIncomeTop(thisMonthQuery,selectedDate2);
        pfsExpenseTopList=database.personalFinanceSourceDao().getPFSExpenseTop(thisMonthQuery,selectedDate2);
        createBarChartExp(expensesTopList,getResources().getString(R.string.expChart_label));
        createBarChartPFS(pfsIncomeTopList,pfsExpenseTopList,getResources().getString(R.string.pfsChart_label));
    }

    public void createBarChartExp(List<TopExpenses> expensesTopList, String chartLabel){
        ArrayList<BarEntry> barEntryArrayList = new ArrayList<>();
        ArrayList<String> labelsNames = new ArrayList<>();
        for(int i=0;i<expensesTopList.size();i++) {
            barEntryArrayList.add(new BarEntry(i, expensesTopList.get(i).getSumAmountExpCat().floatValue()));
            labelsNames.add(expensesTopList.get(i).getExpenseSubcatName());
        }
        BarDataSet barDataSet = new BarDataSet(barEntryArrayList, chartLabel+"  (Lei)");
        barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        Description description = new Description();
        description.setText("");
        barChartExpProf.setDescription(description);
        BarData barData = new BarData(barDataSet);
        barChartExpProf.setData(barData);

        XAxis xAxis = barChartExpProf.getXAxis();
        xAxis.setValueFormatter( new IndexAxisValueFormatter(labelsNames));

        xAxis.setPosition(XAxis.XAxisPosition.TOP);
        xAxis.setDrawAxisLine(false);
        xAxis.setDrawAxisLine(false);
        xAxis.setGranularity(1f);
        xAxis.setLabelCount(labelsNames.size());
        xAxis.setLabelRotationAngle(320);

        barChartExpProf.getXAxis().setTextSize(13f);
        barDataSet.setValueTextSize(16f);
    }

    public void createBarChartPFS(List<TopPFSIncome> pfsIncomeTopList, List<TopPSFExpense> pfsExpenseTopList, String chartLabel){
        ArrayList<BarEntry> barEntryArrayList = new ArrayList<>();
        ArrayList<String> labelsNames = new ArrayList<>();
        double restSum=0;
        int ok=0;
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

        BarDataSet barDataSet = new BarDataSet(barEntryArrayList, chartLabel+"  (Lei)");
        barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        Description description = new Description();
        description.setText("");
        barChartPFSProf.setDescription(description);
        BarData barData = new BarData(barDataSet);
        barChartPFSProf.setData(barData);

        XAxis xAxis = barChartPFSProf.getXAxis();
        xAxis.setValueFormatter( new IndexAxisValueFormatter(labelsNames));

        xAxis.setPosition(XAxis.XAxisPosition.TOP);
        xAxis.setDrawAxisLine(false);
        xAxis.setDrawAxisLine(false);
        xAxis.setGranularity(1f);
        xAxis.setLabelCount(labelsNames.size());
        xAxis.setLabelRotationAngle(320);

        barChartPFSProf.getXAxis().setTextSize(13f);
        barDataSet.setValueTextSize(16f);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_for_others_activities, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        if (id == R.id.btn_home) {
            Intent main_activity_intent = new Intent(BalanceOfAccountsActivity.this, MainActivity.class);
            startActivity(main_activity_intent);

        }

        return super.onOptionsItemSelected(item);
    }

}