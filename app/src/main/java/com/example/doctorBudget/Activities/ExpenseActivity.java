package com.example.doctorBudget.Activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.doctorBudget.RecycleViews.RecycleViewExpenseAdaptor;
import com.example.doctorBudget.ReminderBroadcast;
import com.example.doctorBudget.RoomDB;
import com.example.doctorBudget.Entities.Expense;
import com.example.doctorBudget.R;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ExpenseActivity extends AppCompatActivity {

    RoomDB database;
    List<String> expenseCategoryList = new ArrayList<>();
    List<String> subcategoryExpenseList = new ArrayList<>();
    List<String> usersPFSList = new ArrayList();
    List<Expense> expenseList = new ArrayList<>();
    List<Expense> newExpenseList = new ArrayList<>();

    EditText edt_txt_expense_date_selection_1,edt_txt_expense_date_selection_2, edt_txt_amount_expense,
            edt_txt_reg_date_expense, edt_txt_expense_note;
    TextView txt_view_expense_currency, txt_view_required_amount_expense, txt_view_required_reg_date_expense,
            txt_view_required_cat_expense, txt_view_required_subCat_expense, txt_view_required_psf_expense,
            txt_view_required_recurrency_expense, txt_view_required_dates;
    RadioGroup radio_grp_expense_cat, radio_grp_recurrent_expense;
    RadioButton radio_gen, rdo_fix_expense, rdo_variable_expense,rdo_discretionary_expense, rdo_rec_yes_expense, rdo_rec_no_expense;
    Spinner spinner_subcategory_expense, spinner_choose_pfs;
    ImageView img_doc_expense;
    LinearLayout form_add_expense, up_expense_activity_layout;
    Button btn_reg_new_expense, btn_add_expense, btn_change_date_for_expense, btn_abort_expense, btn_update_expense;

    String userCountry;
    int userID;
    Date today,sevenDaysBefore, selectedDate1, selectedDate2;

    RecycleViewExpenseAdaptor expenseAdaptor;
    RecyclerView recyclerViewExpense;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerViewExpense = findViewById(R.id.recycler_view_expense);
        database = RoomDB.getInstance(this);

        edt_txt_expense_date_selection_1 = findViewById(R.id.edt_txt_expense_date_selection_1);
        edt_txt_expense_date_selection_2 = findViewById(R.id.edt_txt_expense_date_selection_2);
        edt_txt_amount_expense = findViewById(R.id.edt_txt_amount_expense);
        edt_txt_reg_date_expense = findViewById(R.id.edt_txt_reg_date_expense);
        edt_txt_expense_note = findViewById(R.id.edt_txt_expense_note);

        radio_grp_expense_cat = findViewById(R.id.radio_grp_expense_cat);
        radio_grp_recurrent_expense = findViewById( R.id.radio_grp_recurrent_expense);

        rdo_rec_yes_expense = findViewById(R.id.rdo_rec_yes_expense);
        rdo_rec_no_expense = findViewById(R.id.rdo_rec_no_expense);
        rdo_fix_expense= findViewById(R.id.rdo_fix_expense);
        rdo_variable_expense=findViewById(R.id.rdo_variable_expense);
        rdo_discretionary_expense=findViewById(R.id.rdo_discretionary_expense);

        img_doc_expense = findViewById(R.id.img_doc_expense);

        txt_view_required_amount_expense =  findViewById(R.id.txt_view_required_amount_expense);
        txt_view_required_reg_date_expense =  findViewById(R.id.txt_view_required_reg_date_expense);
        txt_view_required_cat_expense =  findViewById(R.id.txt_view_required_cat_expense);
        txt_view_required_subCat_expense =  findViewById(R.id.txt_view_required_subCat_expense);
        txt_view_required_psf_expense =  findViewById(R.id.txt_view_required_psf_expense);
        txt_view_required_recurrency_expense = findViewById(R.id.txt_view_required_recurrency_expense);
        txt_view_required_dates = findViewById(R.id.txt_view_required_dates_exp);

        form_add_expense = findViewById(R.id.form_add_expense);
        up_expense_activity_layout = findViewById(R.id.up_expense_activity_layout);

        btn_reg_new_expense = findViewById(R.id.btn_reg_new_expense);
        btn_add_expense = findViewById(R.id.btn_add_expense);
        btn_abort_expense = findViewById(R.id.btn_abort_expense);
        btn_update_expense = findViewById(R.id.btn_update_expense);
        btn_change_date_for_expense = findViewById(R.id.btn_change_date_for_expense);
        btn_change_date_for_expense = findViewById(R.id.btn_change_date_for_expense);

        form_add_expense.setVisibility(View.GONE);

        btn_change_date_for_expense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeDatesForSelectExpense();
            }
        });

        btn_reg_new_expense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                up_expense_activity_layout.setVisibility(View.GONE);
                form_add_expense.setVisibility(View.VISIBLE);
                btn_update_expense.setVisibility(View.GONE);
                btn_add_expense.setVisibility(View.VISIBLE);
                clearInputFields();
            }
        });


        btn_add_expense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getContentFromInputFieldsAndInsertExpense();
            }
        });

        btn_abort_expense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                form_add_expense.setVisibility(View.GONE);
                up_expense_activity_layout.setVisibility(View.VISIBLE);
                clearInputFields();
                resetFormErrorMessages();
            }
        });

        rdo_fix_expense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                populateSubcategoryExpenseSpinner(1);
            }
        });

        rdo_variable_expense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                populateSubcategoryExpenseSpinner(2);
            }
        });

        rdo_discretionary_expense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                populateSubcategoryExpenseSpinner(3);
            }
        });

        img_doc_expense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage(img_doc_expense);
            }
        });

        prepareDatesForSelectExpense();
        getValuesFromBundle();
        initializeAndPopulateRadioButtonsForCategoryExpense();
        prepareCalendar(edt_txt_reg_date_expense);
        prepareCalendar(edt_txt_expense_date_selection_1);
        prepareCalendar(edt_txt_expense_date_selection_2);

        expenseList = database.expenseDao().getAllExpensesByDates(userID,sevenDaysBefore,today);
        configRecyclerViewExpenseAndSetAdapter();
        createNotificationChanel();


    }

    private void prepareDatesForSelectExpense (){
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        today = new Date();

        String todayForExpense = dateFormat.format(today);

        long ltime=today.getTime()-7*24*60*60*1000;
        sevenDaysBefore = new Date(ltime);

        String sevenDaysBeforeForExpense = dateFormat.format(sevenDaysBefore);

        edt_txt_expense_date_selection_2.setHint(todayForExpense);
        edt_txt_expense_date_selection_1.setHint(sevenDaysBeforeForExpense);

        selectedDate1 = sevenDaysBefore;
        selectedDate2 = today;

    }

    private void changeDatesForSelectExpense(){
        String sSelectedDate1 = edt_txt_expense_date_selection_1.getText().toString();
        selectedDate1= null;
        try {
            selectedDate1 = new SimpleDateFormat("dd-MM-yyyy").parse(sSelectedDate1);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String sSelectedDate2 = edt_txt_expense_date_selection_2.getText().toString();
        selectedDate2= null;
        try {
            selectedDate2 = new SimpleDateFormat("dd-MM-yyyy").parse(sSelectedDate2);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if(!sSelectedDate1.equals("") && !sSelectedDate2.equals("")){
            newExpenseList = database.expenseDao().getAllExpensesByDates(userID,selectedDate1,selectedDate2);
            if(newExpenseList.isEmpty()){
                Toast.makeText(this, "Nu există cheltuieli inregistrate în această perioadă" , Toast.LENGTH_LONG).show();
                expenseList.clear();
                expenseAdaptor.notifyDataSetChanged();
            }
            else{
                expenseList.clear();
                expenseList.addAll(newExpenseList);
                expenseAdaptor.notifyDataSetChanged();
            }
            txt_view_required_dates.setText("");
        }
        else
        {
            txt_view_required_dates.setText(getResources().getString(R.string.txt_view_required_dates));
        }

    }

    private void configRecyclerViewExpenseAndSetAdapter (){
        recyclerViewExpense = findViewById(R.id.recycler_view_expense);
        expenseAdaptor = new RecycleViewExpenseAdaptor(ExpenseActivity.this, expenseList);
        RecyclerView.LayoutManager linearLayoutManager = new LinearLayoutManager(ExpenseActivity.this);
        recyclerViewExpense.setLayoutManager(linearLayoutManager);
        recyclerViewExpense.setAdapter(expenseAdaptor);
    }

    private void initializeAndPopulateRadioButtonsForCategoryExpense(){
        expenseCategoryList = database.expenseCategoryDao().getAllExpenseCategoryNames();

        rdo_fix_expense.setText(expenseCategoryList.get(0));
        rdo_variable_expense.setText(expenseCategoryList.get(1));
        rdo_discretionary_expense.setText(expenseCategoryList.get(2));
    }

    public void populateSubcategoryExpenseSpinner(int catId){
        spinner_subcategory_expense = findViewById(R.id.spinner_subcategory_expense);
        subcategoryExpenseList = database.expenseSubcategoryDao().getAllExpenseSubcategoryNames(catId);
        ArrayAdapter<String> adapterSpinner = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, subcategoryExpenseList);
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_subcategory_expense.setAdapter(adapterSpinner);
    }

    public void populatePersonalFinanceSourceSpinnerExp(int userID){
        spinner_choose_pfs = findViewById(R.id.spinner_choose_pfs_exp);
        usersPFSList = database.personalFinanceSourceDao().getFinanceSourceByUser(userID);
        ArrayAdapter<String> adapterSpinner = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, usersPFSList);
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_choose_pfs.setAdapter(adapterSpinner);
    }

    public void setCurrencyForExpense (String userCountry){
        txt_view_expense_currency = findViewById(R.id.txt_view_expense_currency);
        String currecyCode = database.countryDao().getCurrencyCode(userCountry);
        String currencyName = database.currencyDao().getCurrencyName(currecyCode);
        txt_view_expense_currency.setText(currencyName);
    }

    public void getValuesFromBundle(){
        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            userCountry = extras.getString("userCountry");
            userID = extras.getInt("userID");
        }
        setCurrencyForExpense (userCountry);
        populatePersonalFinanceSourceSpinnerExp(userID);
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
                        ExpenseActivity.this, new DatePickerDialog.OnDateSetListener() {
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

    //Obtain the image from intern memory of smartphone
    public void chooseImage (View objectView){
        Intent photoPickerIntent = new Intent(Intent.ACTION_GET_CONTENT);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, 1);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK)
        {
            Uri chosenImageUri = data.getData();
            Bitmap mBitmap = null;
            try {
                mBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), chosenImageUri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            img_doc_expense.setImageBitmap(mBitmap);
        }
    }

    public void getContentFromInputFieldsAndInsertExpense(){
        String sExpenseAmount = edt_txt_amount_expense.getText().toString();
        double expenseAmount =0;
        try {
            expenseAmount = (double)Double.parseDouble(sExpenseAmount);
        }catch (Exception e){
            e.printStackTrace();
        }

        String sRegDate = edt_txt_reg_date_expense.getText().toString();
        Date regDate = null;
        try {
            regDate = new SimpleDateFormat("dd-MM-yyyy").parse(sRegDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        int subcatID=0;
        String sSubcategoryExpense ="";
        try {
            sSubcategoryExpense = spinner_subcategory_expense.getSelectedItem().toString();
            subcatID = database.expenseSubcategoryDao().getIDsubCatExpense(sSubcategoryExpense);
        }catch (Exception i) {
            i.printStackTrace();
        }

        int psfID=0;
        String sPersFinSource="";
        try {
            sPersFinSource = spinner_choose_pfs.getSelectedItem().toString();
            psfID = database.personalFinanceSourceDao().getIDFinanceSource(sPersFinSource);
        }catch (Exception i) {
            i.printStackTrace();
        }

        int radioID = radio_grp_recurrent_expense.getCheckedRadioButtonId();
        String sRecurrency = "";
        int intRecurrecy = 0;
        try {
            radio_gen = findViewById(radioID);
            sRecurrency = radio_gen.getText().toString();
            if(sRecurrency.equals("Da")){
                intRecurrecy=1;
                Toast.makeText(ExpenseActivity.this, getResources().getString(R.string.notifyExpenseSet), Toast.LENGTH_LONG).show();

                Intent notifyExpenseIntent = new Intent(ExpenseActivity.this, ReminderBroadcast.class);
                notifyExpenseIntent.putExtra("message", getResources().getString(R.string.notifyExpenseText));
                PendingIntent pendingIntent = PendingIntent.getBroadcast(ExpenseActivity.this, 0, notifyExpenseIntent, 0);

                AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

                long timeAtButtonClick = System.currentTimeMillis();

                long tenSecondsInMillis = 1000*30*24*60*60;

                alarmManager.set(AlarmManager.RTC_WAKEUP, timeAtButtonClick + tenSecondsInMillis, pendingIntent);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }

        String expenseNote = edt_txt_expense_note.getText().toString();

        Bitmap bitmap_expense_proof = null;
        try {
            bitmap_expense_proof = ((BitmapDrawable) img_doc_expense.getDrawable()).getBitmap();
        }catch (Exception e) {
            e.printStackTrace();
        }
        boolean hasDrawable = (bitmap_expense_proof!=null);

        if(!sExpenseAmount.equals("") && !sRegDate.equals("") && !sSubcategoryExpense.equals("") &&
                !sPersFinSource.equals("") && !sRecurrency.equals("")) {
            if (hasDrawable) {
                insertExpenseWithPicture(userID, expenseAmount, regDate,subcatID,psfID, intRecurrecy,
                        expenseNote, bitmap_expense_proof);
            } else {
                insertExpenseWithoutPicture(userID, expenseAmount, regDate, subcatID, psfID, intRecurrecy,
                        expenseNote);
            }
            form_add_expense.setVisibility(View.GONE);
            up_expense_activity_layout.setVisibility(View.VISIBLE);
            clearInputFields();
            resetFormErrorMessages();
        } else{
            throwFormErrorMessages();
        }

    }
    public void insertExpenseWithPicture (int userIDforInc, double incomeAmount,Date regDate,
                                         int subcatID, int psfID, int intRecurrecy,
                                         String income_note, Bitmap bitmap_income_proof_doc){
        Expense expense = new Expense();
        expense.setUser_id_exp(userIDforInc);
        expense.setAmount_exp(incomeAmount);
        expense.setDate_of_registration_exp(regDate);
        expense.setSubcat_expense_id_exp(subcatID);
        expense.setFinance_source_id_exp(psfID);
        expense.setRecurrent_exp(intRecurrecy);
        expense.setNote_exp(income_note);
        expense.setAttachment_exp(bitmap_income_proof_doc);

        database.expenseDao().insertExpense(expense);

        int comp1 = regDate.compareTo(today);
        int comp2 = regDate.compareTo(sevenDaysBefore);
        int comp3 =regDate.compareTo(selectedDate1);
        int comp4 =regDate.compareTo(selectedDate2);

        if ((comp1 < 0 && comp2 > 0 ) ||(comp4 < 0 && comp3 >0 ))  {
            expenseList.add(expense);
            expenseAdaptor.notifyDataSetChanged();
            recyclerViewExpense.scrollToPosition(expenseList.size()-1);
        }
        else{
            Toast.makeText(this, "Operațiune de înregistrare a cheltuielii a avut loc cu success!" , Toast.LENGTH_LONG).show();
        }

    }

    public void insertExpenseWithoutPicture (int userIDforInc, double incomeAmount,Date regDate,
                                            int subcatID, int psfID, int intRecurrecy,
                                            String income_note){
        Expense expense = new Expense();
        expense.setUser_id_exp(userIDforInc);
        expense.setAmount_exp(incomeAmount);
        expense.setDate_of_registration_exp(regDate);
        expense.setSubcat_expense_id_exp(subcatID);
        expense.setFinance_source_id_exp(psfID);
        expense.setRecurrent_exp(intRecurrecy);
        expense.setNote_exp(income_note);

        database.expenseDao().insertExpense(expense);

        int comp1 = regDate.compareTo(today);
        int comp2 = regDate.compareTo(sevenDaysBefore);
        int comp3 =regDate.compareTo(selectedDate1);
        int comp4 =regDate.compareTo(selectedDate2);

        if ((comp1 < 0 && comp2 > 0 ) ||(comp4 < 0 && comp3 >0 ))  {
            expenseList.add(expense);
            expenseAdaptor.notifyDataSetChanged();
            recyclerViewExpense.scrollToPosition(expenseList.size()-1);
        }
        else{
            Toast.makeText(this, "Operațiune de înregistrare a cheltuielii a avut loc cu success!" , Toast.LENGTH_LONG).show();
        }

    }

    public void throwFormErrorMessages(){
        txt_view_required_amount_expense.setText(getResources().getString(R.string.txt_view_required_amount));
        txt_view_required_reg_date_expense.setText(getResources().getString(R.string.txt_view_required_reg_date));
        txt_view_required_cat_expense.setText(getResources().getString(R.string.txt_view_required_cat));
        txt_view_required_subCat_expense.setText(getResources().getString(R.string.txt_view_required_subCat));
        txt_view_required_psf_expense.setText(getResources().getString(R.string.txt_view_required_psf));
        txt_view_required_recurrency_expense.setText(getResources().getString(R.string.txt_view_required_recurrency));
    }

    public void clearInputFields(){
        edt_txt_amount_expense.setText("");
        edt_txt_reg_date_expense.setText("");
        edt_txt_expense_note.setText("");
        radio_grp_expense_cat.clearCheck();
        radio_grp_recurrent_expense.clearCheck();
        img_doc_expense.setImageResource(R.drawable.ic_photo);
    }

    public void resetFormErrorMessages(){
        txt_view_required_amount_expense.setText(getResources().getString(R.string.txt_view_star));
        txt_view_required_reg_date_expense.setText(getResources().getString(R.string.txt_view_star));
        txt_view_required_cat_expense.setText(getResources().getString(R.string.txt_view_star));
        txt_view_required_subCat_expense.setText(getResources().getString(R.string.txt_view_star));
        txt_view_required_psf_expense.setText(getResources().getString(R.string.txt_view_star));
        txt_view_required_recurrency_expense.setText(getResources().getString(R.string.txt_view_star));
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("ObsoleteSdkInt")
    public void createNotificationChanel () {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR_0_1) {
            CharSequence name = "DoctorBudgetReminderChannel";
            String description = "Channel for Doctor Budget";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("notifyDoctorBudget", name,importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
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
            Intent main_activity_intent = new Intent(ExpenseActivity.this, MainActivity.class);
            startActivity(main_activity_intent);

        }

        return super.onOptionsItemSelected(item);
    }

}