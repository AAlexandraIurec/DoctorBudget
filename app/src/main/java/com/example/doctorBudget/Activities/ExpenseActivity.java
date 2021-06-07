package com.example.doctorBudget.Activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlarmManager;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.doctorBudget.MyCalendar;
import com.example.doctorBudget.RecyclerViews.RecyclerViewExpenseAdaptor;
import com.example.doctorBudget.ReminderBroadcast;
import com.example.doctorBudget.RoomDB;
import com.example.doctorBudget.Entities.Expense;
import com.example.doctorBudget.R;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ExpenseActivity extends AppCompatActivity {

    RoomDB database;
    List<String> exp_cat_list = new ArrayList<>();
    List<String> subCat_exp_list = new ArrayList<>();
    List<String> user_pfs_list = new ArrayList();
    List<Expense> exp_list = new ArrayList<>();
    List<Expense> new_exp_list = new ArrayList<>();

    EditText edt_txt_exp_date_select1, edt_txt_exp_date_select2, edt_txt_amount_exp,
            edt_txt_reg_date_exp, edt_txt_exp_note;
    TextView txt_view_exp_currency, txt_view_req_amount_exp, txt_view_req_reg_date_exp,
            txt_view_req_cat_exp, txt_view_req_subCat_exp, txt_view_req_psf_exp,
            txt_view_req_recurrency_exp, txt_view_req_dates;
    RadioGroup radio_grp_exp_cat, radio_grp_recurrent_exp;
    RadioButton radio_gen, rdo_fix_exp, rdo_variable_exp, rdo_discretionary_exp, rdo_rec_yes_exp, rdo_rec_no_exp;
    Spinner spinner_subcategory_exp, spinner_choose_pfs;
    ImageView img_doc_exp;
    LinearLayout form_add_exp, up_exp_activity_layout;
    Button btn_reg_new_exp, btn_add_exp, btn_change_date_for_exp, btn_abort_exp, btn_update_exp;

    String user_country,currency;
    MyCalendar calendar;
    int user_id;
    Date today, seven_days_before, select_date1, select_date2;

    RecyclerViewExpenseAdaptor exp_adaptor;
    RecyclerView recycler_view_expense;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recycler_view_expense = findViewById(R.id.recycler_view_expense);
        database = RoomDB.getInstance(this);

        edt_txt_exp_date_select1 = findViewById(R.id.edt_txt_expense_date_selection_1);
        edt_txt_exp_date_select2 = findViewById(R.id.edt_txt_expense_date_selection_2);
        edt_txt_amount_exp = findViewById(R.id.edt_txt_amount_expense);
        edt_txt_reg_date_exp = findViewById(R.id.edt_txt_reg_date_expense);
        edt_txt_exp_note = findViewById(R.id.edt_txt_expense_note);

        radio_grp_exp_cat = findViewById(R.id.radio_grp_expense_cat);
        radio_grp_recurrent_exp = findViewById( R.id.radio_grp_recurrent_expense);

        rdo_rec_yes_exp = findViewById(R.id.rdo_rec_yes_expense);
        rdo_rec_no_exp = findViewById(R.id.rdo_rec_no_expense);
        rdo_fix_exp = findViewById(R.id.rdo_fix_expense);
        rdo_variable_exp =findViewById(R.id.rdo_variable_expense);
        rdo_discretionary_exp =findViewById(R.id.rdo_discretionary_expense);

        img_doc_exp = findViewById(R.id.img_doc_expense);

        txt_view_req_amount_exp =  findViewById(R.id.txt_view_required_amount_expense);
        txt_view_req_reg_date_exp =  findViewById(R.id.txt_view_required_reg_date_expense);
        txt_view_req_cat_exp =  findViewById(R.id.txt_view_required_cat_expense);
        txt_view_req_subCat_exp =  findViewById(R.id.txt_view_required_subCat_expense);
        txt_view_req_psf_exp =  findViewById(R.id.txt_view_required_psf_expense);
        txt_view_req_recurrency_exp = findViewById(R.id.txt_view_required_recurrency_expense);
        txt_view_req_dates = findViewById(R.id.txt_view_required_dates_exp);

        form_add_exp = findViewById(R.id.form_add_expense);
        up_exp_activity_layout = findViewById(R.id.up_expense_activity_layout);

        btn_reg_new_exp = findViewById(R.id.btn_reg_new_expense);
        btn_add_exp = findViewById(R.id.btn_add_expense);
        btn_abort_exp = findViewById(R.id.btn_abort_expense);
        btn_update_exp = findViewById(R.id.btn_update_expense);
        btn_change_date_for_exp = findViewById(R.id.btn_change_date_for_expense);
        btn_change_date_for_exp = findViewById(R.id.btn_change_date_for_expense);

        form_add_exp.setVisibility(View.GONE);

        btn_change_date_for_exp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeDatesForSelectExpense();
            }
        });

        btn_reg_new_exp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                up_exp_activity_layout.setVisibility(View.GONE);
                form_add_exp.setVisibility(View.VISIBLE);
                btn_update_exp.setVisibility(View.GONE);
                btn_add_exp.setVisibility(View.VISIBLE);
                clearInputFields();
            }
        });


        btn_add_exp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getContentFromInputFieldsAndInsertExpense();
            }
        });

        btn_abort_exp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                form_add_exp.setVisibility(View.GONE);
                up_exp_activity_layout.setVisibility(View.VISIBLE);
                clearInputFields();
                resetFormErrorMessages();
            }
        });

        rdo_fix_exp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                populateSubcategoryExpenseSpinner(1);
            }
        });

        rdo_variable_exp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                populateSubcategoryExpenseSpinner(2);
            }
        });

        rdo_discretionary_exp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                populateSubcategoryExpenseSpinner(3);
            }
        });

        img_doc_exp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage(img_doc_exp);
            }
        });

        calendar = new MyCalendar();

        prepareDatesForSelectExpense();
        getValuesFromBundle();
        initializeAndPopulateRadioButtonsForCategoryExpense();
        calendar.prepareCalendar(edt_txt_reg_date_exp, ExpenseActivity.this);
        calendar.prepareCalendar(edt_txt_exp_date_select1, ExpenseActivity.this);
        calendar.prepareCalendar(edt_txt_exp_date_select2, ExpenseActivity.this);

        exp_list = database.expenseDao().getAllExpensesByDates(user_id, seven_days_before,today);
        configRecyclerViewExpenseAndSetAdapter();
        createNotificationChanel();
    }

    private void prepareDatesForSelectExpense (){
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        today = new Date();

        String todayForExpense = dateFormat.format(today);

        long ltime=today.getTime()-7*24*60*60*1000;
        seven_days_before = new Date(ltime);

        String sevenDaysBeforeForExpense = dateFormat.format(seven_days_before);

        edt_txt_exp_date_select2.setHint(todayForExpense);
        edt_txt_exp_date_select1.setHint(sevenDaysBeforeForExpense);

        select_date1 = seven_days_before;
        select_date2 = today;

    }

    private void changeDatesForSelectExpense(){
        String sSelectedDate1 = edt_txt_exp_date_select1.getText().toString();
        select_date1 = null;
        try {
            select_date1 = new SimpleDateFormat("dd-MM-yyyy").parse(sSelectedDate1);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String sSelectedDate2 = edt_txt_exp_date_select2.getText().toString();
        select_date2 = null;
        try {
            select_date2 = new SimpleDateFormat("dd-MM-yyyy").parse(sSelectedDate2);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if(!sSelectedDate1.equals("") && !sSelectedDate2.equals("")){
            new_exp_list = database.expenseDao().getAllExpensesByDates(user_id, select_date1, select_date2);
            if(new_exp_list.isEmpty()){
                Toast.makeText(this, "Nu există cheltuieli inregistrate în această perioadă" , Toast.LENGTH_LONG).show();
                exp_list.clear();
                exp_adaptor.notifyDataSetChanged();
            }
            else{
                exp_list.clear();
                exp_list.addAll(new_exp_list);
                exp_adaptor.notifyDataSetChanged();
            }
            txt_view_req_dates.setText("");
        }
        else
        {
            txt_view_req_dates.setText(getResources().getString(R.string.txt_view_required_dates));
        }

    }

    private void configRecyclerViewExpenseAndSetAdapter (){
        recycler_view_expense = findViewById(R.id.recycler_view_expense);
        exp_adaptor = new RecyclerViewExpenseAdaptor(ExpenseActivity.this, exp_list);
        RecyclerView.LayoutManager linearLayoutManager = new LinearLayoutManager(ExpenseActivity.this);
        recycler_view_expense.setLayoutManager(linearLayoutManager);
        recycler_view_expense.setAdapter(exp_adaptor);
    }

    private void initializeAndPopulateRadioButtonsForCategoryExpense(){
        exp_cat_list = database.expenseCategoryDao().getAllExpenseCategoryNames();

        rdo_fix_exp.setText(exp_cat_list.get(0));
        rdo_variable_exp.setText(exp_cat_list.get(1));
        rdo_discretionary_exp.setText(exp_cat_list.get(2));
    }

    public void populateSubcategoryExpenseSpinner(int catId){
        spinner_subcategory_exp = findViewById(R.id.spinner_subcategory_expense);
        subCat_exp_list = database.expenseSubcategoryDao().getAllExpenseSubcategoryNames(catId);
        ArrayAdapter<String> adapterSpinner = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, subCat_exp_list);
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_subcategory_exp.setAdapter(adapterSpinner);
    }

    public void populatePersonalFinanceSourceSpinnerExp(int userID){
        spinner_choose_pfs = findViewById(R.id.spinner_choose_pfs_exp);
        user_pfs_list = database.personalFinanceSourceDao().getFinanceSourceByUser(userID);
        ArrayAdapter<String> adapterSpinner = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, user_pfs_list);
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_choose_pfs.setAdapter(adapterSpinner);
    }


    public void getValuesFromBundle(){
        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            user_country = extras.getString("userCountry");
            user_id = extras.getInt("userID");
        }
        setCurrencyForExpense (user_country);
        populatePersonalFinanceSourceSpinnerExp(user_id);
    }

    public void setCurrencyForExpense (String userCountry){
        txt_view_exp_currency = findViewById(R.id.txt_view_expense_currency);
        String currecyCode = database.countryDao().getCurrencyCode(userCountry);
        currency = database.currencyDao().getCurrencyName(currecyCode);
        txt_view_exp_currency.setText(currency);
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
            img_doc_exp.setImageBitmap(mBitmap);
        }
    }

    public void getContentFromInputFieldsAndInsertExpense(){
        String sExpenseAmount = edt_txt_amount_exp.getText().toString();
        double expenseAmount =0;
        try {
            expenseAmount = (double)Double.parseDouble(sExpenseAmount);
        }catch (Exception e){
            e.printStackTrace();
        }

        String sRegDate = edt_txt_reg_date_exp.getText().toString();
        Date regDate = null;
        try {
            regDate = new SimpleDateFormat("dd-MM-yyyy").parse(sRegDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        int subcatID=0;
        String sSubcategoryExpense ="";
        try {
            sSubcategoryExpense = spinner_subcategory_exp.getSelectedItem().toString();
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

        int radioID = radio_grp_recurrent_exp.getCheckedRadioButtonId();
        String sRecurrency = "";
        int intRecurrecy = 0;
        try {
            radio_gen = findViewById(radioID);
            sRecurrency = radio_gen.getText().toString();
            if(sRecurrency.equals("Da")){
                intRecurrecy=1;
            }
        }catch (Exception e) {
            e.printStackTrace();
        }

        String expenseNote = edt_txt_exp_note.getText().toString();

        Bitmap bitmap_expense_proof = null;
        try {
            bitmap_expense_proof = ((BitmapDrawable) img_doc_exp.getDrawable()).getBitmap();
        }catch (Exception e) {
            e.printStackTrace();
        }
        boolean hasDrawable = (bitmap_expense_proof!=null);

        if(!sExpenseAmount.equals("") && !sRegDate.equals("") && !sSubcategoryExpense.equals("") &&
                !sPersFinSource.equals("") && !sRecurrency.equals("")) {
            if (hasDrawable) {
                insertExpenseWithPicture(user_id, expenseAmount, regDate,subcatID,psfID, intRecurrecy,
                        expenseNote, bitmap_expense_proof);
            } else {
                insertExpenseWithoutPicture(user_id, expenseAmount, regDate, subcatID, psfID, intRecurrecy,
                        expenseNote);
            }
            if(intRecurrecy==1)
                setExpenseNotification();
            form_add_exp.setVisibility(View.GONE);
            up_exp_activity_layout.setVisibility(View.VISIBLE);
            clearInputFields();
            resetFormErrorMessages();
        } else{
            throwFormErrorMessages();
        }

    }
    public void insertExpenseWithPicture (int userIDforInc, double expAmount, Date regDate,
                                          int subcatID, int psfID, int intRecurrecy,
                                          String exp_note, Bitmap bitmap_exp_proof_doc){
        Expense expense = new Expense();
        expense.setUser_id_exp(userIDforInc);
        expense.setAmount_exp(expAmount);
        expense.setDate_of_registration_exp(regDate);
        expense.setSubcat_expense_id_exp(subcatID);
        expense.setFinance_source_id_exp(psfID);
        expense.setRecurrent_exp(intRecurrecy);
        expense.setNote_exp(exp_note);
        expense.setAttachment_exp(bitmap_exp_proof_doc);

        database.expenseDao().insertExpense(expense);

        int comp1 = regDate.compareTo(today);
        int comp2 = regDate.compareTo(seven_days_before);
        int comp3 =regDate.compareTo(select_date1);
        int comp4 =regDate.compareTo(select_date2);

        if ((comp1 < 0 && comp2 > 0 ) ||(comp4 < 0 && comp3 >0 ))  {
            exp_list.add(expense);
            exp_adaptor.notifyDataSetChanged();
            recycler_view_expense.scrollToPosition(exp_list.size()-1);
        }
        else{
            Toast.makeText(this, "Operațiune de înregistrare a cheltuielii a avut loc cu success!" , Toast.LENGTH_LONG).show();
        }

    }

    public void insertExpenseWithoutPicture (int userIDforInc, double expAmount, Date regDate,
                                             int subcatID, int psfID, int intRecurrecy,
                                             String exp_note){
        Expense expense = new Expense();
        expense.setUser_id_exp(userIDforInc);
        expense.setAmount_exp(expAmount);
        expense.setDate_of_registration_exp(regDate);
        expense.setSubcat_expense_id_exp(subcatID);
        expense.setFinance_source_id_exp(psfID);
        expense.setRecurrent_exp(intRecurrecy);
        expense.setNote_exp(exp_note);

        database.expenseDao().insertExpense(expense);

        int comp1 = regDate.compareTo(today);
        int comp2 = regDate.compareTo(seven_days_before);
        int comp3 =regDate.compareTo(select_date1);
        int comp4 =regDate.compareTo(select_date2);

        if ((comp1 < 0 && comp2 > 0 ) ||(comp4 < 0 && comp3 >0 ))  {
            exp_list.add(expense);
            exp_adaptor.notifyDataSetChanged();
            recycler_view_expense.scrollToPosition(exp_list.size()-1);
        }
        else{
            Toast.makeText(this, "Operațiune de înregistrare a cheltuielii a avut loc cu success!" , Toast.LENGTH_LONG).show();
        }

    }

    public void throwFormErrorMessages(){
        txt_view_req_amount_exp.setText(getResources().getString(R.string.txt_view_required_amount));
        txt_view_req_reg_date_exp.setText(getResources().getString(R.string.txt_view_required_reg_date));
        txt_view_req_cat_exp.setText(getResources().getString(R.string.txt_view_required_cat));
        txt_view_req_subCat_exp.setText(getResources().getString(R.string.txt_view_required_subCat));
        txt_view_req_psf_exp.setText(getResources().getString(R.string.txt_view_required_psf));
        txt_view_req_recurrency_exp.setText(getResources().getString(R.string.txt_view_required_recurrency));
    }

    public void clearInputFields(){
        edt_txt_amount_exp.setText("");
        edt_txt_reg_date_exp.setText("");
        edt_txt_exp_note.setText("");
        radio_grp_exp_cat.clearCheck();
        radio_grp_recurrent_exp.clearCheck();
        img_doc_exp.setImageResource(R.drawable.ic_photo);
    }

    public void resetFormErrorMessages(){
        txt_view_req_amount_exp.setText(getResources().getString(R.string.txt_view_star));
        txt_view_req_reg_date_exp.setText(getResources().getString(R.string.txt_view_star));
        txt_view_req_cat_exp.setText(getResources().getString(R.string.txt_view_star));
        txt_view_req_subCat_exp.setText(getResources().getString(R.string.txt_view_star));
        txt_view_req_psf_exp.setText(getResources().getString(R.string.txt_view_star));
        txt_view_req_recurrency_exp.setText(getResources().getString(R.string.txt_view_star));
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public void createNotificationChanel(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "DoctorBudgetReminderChannel";
            String description = "Channel for Doctor Budget";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("notifyDoctorBudget", name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public void setExpenseNotification(){
        Toast.makeText(this, getResources().getString(R.string.notifyExpenseSet), Toast.LENGTH_SHORT).show();

        Intent intentNotifyExpense = new Intent(ExpenseActivity.this, ReminderBroadcast.class);
        intentNotifyExpense.putExtra("message", getResources().getString(R.string.notifyExpenseText));
        PendingIntent pendingIntentExpense = PendingIntent.getBroadcast(ExpenseActivity.this, 0, intentNotifyExpense,0);
        AlarmManager alarmManagerExpense = (AlarmManager) getSystemService(ALARM_SERVICE);

        long currentTime = System.currentTimeMillis();
        long milisInAHour = 60*60*1000;
        long hoursInAMonth = 24*30;
        alarmManagerExpense.set(AlarmManager.RTC_WAKEUP, (milisInAHour*hoursInAMonth)+currentTime, pendingIntentExpense);
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
            Intent main_activity_intent = new Intent(ExpenseActivity.this, MainActivity.class);
            startActivity(main_activity_intent);

        }

        return super.onOptionsItemSelected(item);
    }

}