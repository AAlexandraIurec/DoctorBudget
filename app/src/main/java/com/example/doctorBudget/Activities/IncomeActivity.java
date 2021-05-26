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

import com.example.doctorBudget.RecycleViews.RecycleViewIncomeAdaptor;
import com.example.doctorBudget.ReminderBroadcast;
import com.example.doctorBudget.RoomDB;
import com.example.doctorBudget.Entities.Income;
import com.example.doctorBudget.R;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class IncomeActivity extends AppCompatActivity {

    RoomDB database;
    List<String> incomeCategoryList = new ArrayList<>();
    List<String> subcategoryIncomeList = new ArrayList<>();
    List<String> usersPFSList = new ArrayList();
    List<Income> incomeList = new ArrayList<>();
    List<Income> newIncomeList = new ArrayList<>();

    EditText edt_txt_income_date_selection_1,edt_txt_income_date_selection_2, edt_txt_amount_income,
            edt_txt_reg_date_income, edt_txt_income_note;
    TextView txt_view_income_currency, txt_view_required_amount_income, txt_view_required_reg_date_income,
            txt_view_required_cat_income, txt_view_required_subCat_income, txt_view_required_psf_income,
            txt_view_required_recurrency_income, txt_view_required_dates;
    RadioGroup radio_grp_income_cat, radio_grp_recurrent_income;
    RadioButton radio_gen, rdo_fix_income, rdo_variable_income, rdo_rec_yes_income, rdo_rec_no_income;
    Spinner spinner_subcategory_income, spinner_choose_pfs;
    ImageView img_doc_income;
    LinearLayout form_add_income, up_income_activity_layout;
    Button btn_reg_new_income, btn_add_income, btn_change_date_for_income, btn_abort_income,  btn_update_income ;

    String userCountry;
    int userID;
    Date today,sevenDaysBefore, selectedDate1, selectedDate2;

    RecycleViewIncomeAdaptor incomeAdaptor;
    RecyclerView recyclerViewIncome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_income);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerViewIncome = findViewById(R.id.recycler_view_income);
        database = RoomDB.getInstance(this);

        edt_txt_income_date_selection_1 = findViewById(R.id.edt_txt_income_date_selection_1);
        edt_txt_income_date_selection_2 = findViewById(R.id.edt_txt_income_date_selection_2);
        edt_txt_amount_income = findViewById(R.id.edt_txt_amount_income);
        edt_txt_reg_date_income = findViewById(R.id.edt_txt_reg_date_income);
        edt_txt_income_note = findViewById(R.id.edt_txt_income_note);

        radio_grp_income_cat = findViewById(R.id.radio_grp_income_cat);
        radio_grp_recurrent_income = findViewById( R.id.radio_grp_recurrent_income);

        rdo_rec_yes_income = findViewById(R.id.rdo_rec_yes_income);
        rdo_rec_no_income = findViewById(R.id.rdo_rec_no_income);
        rdo_fix_income = findViewById(R.id.rdo_fix_income);
        rdo_variable_income=findViewById(R.id.rdo_variable_income);

        img_doc_income = findViewById(R.id.img_doc_income);

        txt_view_required_amount_income =  findViewById(R.id.txt_view_required_amount_income);
        txt_view_required_reg_date_income =  findViewById(R.id.txt_view_required_reg_date_income);
        txt_view_required_cat_income =  findViewById(R.id.txt_view_required_cat_income);
        txt_view_required_subCat_income =  findViewById(R.id.txt_view_required_subCat_income);
        txt_view_required_psf_income =  findViewById(R.id.txt_view_required_psf_income);
        txt_view_required_recurrency_income = findViewById(R.id.txt_view_required_recurrency_income);
        txt_view_required_dates = findViewById(R.id.txt_view_required_dates);

        form_add_income = findViewById(R.id.form_add_income);
        up_income_activity_layout = findViewById(R.id.up_income_activity_layout);

        btn_reg_new_income = findViewById(R.id.btn_reg_new_income);
        btn_add_income = findViewById(R.id.btn_add_income);
        btn_update_income = findViewById(R.id.btn_update_income);
        btn_abort_income = findViewById(R.id.btn_abort_income);
        btn_change_date_for_income = findViewById(R.id.btn_change_date_for_income);
        btn_change_date_for_income = findViewById(R.id.btn_change_date_for_income);

        form_add_income.setVisibility(View.GONE);

        btn_change_date_for_income.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeDatesForSelectIncome();
            }
        });

        btn_reg_new_income.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                up_income_activity_layout.setVisibility(View.GONE);
                form_add_income.setVisibility(View.VISIBLE);
                btn_update_income.setVisibility(View.GONE);
                btn_add_income.setVisibility(View.VISIBLE);
                clearInputFields();
            }
        });


        btn_add_income.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getContentFromInputFieldsAndInsertIncome();
            }
        });

        btn_abort_income.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                form_add_income.setVisibility(View.GONE);
                up_income_activity_layout.setVisibility(View.VISIBLE);
                clearInputFields();
                resetFormErrorMessages();
            }
        });

        rdo_fix_income.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                populateSubcategoryIncomeSpinner(1);
            }
        });

        rdo_variable_income.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                populateSubcategoryIncomeSpinner(2);
            }
        });

        img_doc_income.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage(img_doc_income);
            }
        });

        prepareDatesForSelectIncome ();
        getValuesFromBundle();
        initializeAndPopulateRadioButtonsForCategoryIncome();
        prepareCalendar(edt_txt_reg_date_income);
        prepareCalendar(edt_txt_income_date_selection_1);
        prepareCalendar(edt_txt_income_date_selection_2);

        incomeList = database.incomeDao().getAllIncomesByDates(userID,sevenDaysBefore,today);
        configRecyclerViewIncomeAndSetAdapter();

    }

    private void prepareDatesForSelectIncome (){
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        today = new Date();

        String todayForIncome = dateFormat.format(today);

        long ltime=today.getTime()-7*24*60*60*1000;
        sevenDaysBefore = new Date(ltime);

        String sevenDaysBeforeForIncome = dateFormat.format(sevenDaysBefore);

        edt_txt_income_date_selection_2.setHint(todayForIncome);
        edt_txt_income_date_selection_1.setHint(sevenDaysBeforeForIncome);

        selectedDate1 = sevenDaysBefore;
        selectedDate2 = today;

    }

    private void changeDatesForSelectIncome(){
        String sSelectedDate1 = edt_txt_income_date_selection_1.getText().toString();
        selectedDate1= null;
        try {
            selectedDate1 = new SimpleDateFormat("dd-MM-yyyy").parse(sSelectedDate1);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String sSelectedDate2 = edt_txt_income_date_selection_2.getText().toString();
        selectedDate2= null;
        try {
            selectedDate2 = new SimpleDateFormat("dd-MM-yyyy").parse(sSelectedDate2);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if(!sSelectedDate1.equals("") && !sSelectedDate2.equals("")){
            newIncomeList = database.incomeDao().getAllIncomesByDates(userID,selectedDate1,selectedDate2);
            if(newIncomeList.isEmpty()){
                Toast.makeText(this, "Nu există venituri inregistrate în această perioadă" , Toast.LENGTH_LONG).show();
                incomeList.clear();
                incomeAdaptor.notifyDataSetChanged();
            }
            else{
                incomeList.clear();
                incomeList.addAll(newIncomeList);
                incomeAdaptor.notifyDataSetChanged();
            }
            txt_view_required_dates.setText("");
        }
        else
        {
            txt_view_required_dates.setText(getResources().getString(R.string.txt_view_required_dates));
        }

    }

    private void configRecyclerViewIncomeAndSetAdapter (){
        recyclerViewIncome = findViewById(R.id.recycler_view_income);
        incomeAdaptor = new RecycleViewIncomeAdaptor(IncomeActivity.this, incomeList);
        RecyclerView.LayoutManager linearLayoutManager = new LinearLayoutManager(IncomeActivity.this);
        recyclerViewIncome.setLayoutManager(linearLayoutManager);
        recyclerViewIncome.setAdapter(incomeAdaptor);
    }


    private void initializeAndPopulateRadioButtonsForCategoryIncome(){
        rdo_fix_income = findViewById(R.id.rdo_fix_income);
        rdo_variable_income = findViewById(R.id.rdo_variable_income);

        incomeCategoryList = database.incomeCategoryDao().getAllIncomeCategoryNames();

        rdo_fix_income.setText(incomeCategoryList.get(0));
        rdo_variable_income.setText(incomeCategoryList.get(1));
    }

    public void populateSubcategoryIncomeSpinner(int catId){
        spinner_subcategory_income = findViewById(R.id.spinner_subcategory_income);
        subcategoryIncomeList = database.incomeSubcategoryDao().getAllIncomeSubcategoryNames(catId);
        ArrayAdapter<String> adapterSpinner = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, subcategoryIncomeList);
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_subcategory_income.setAdapter(adapterSpinner);
    }

    public void populatePersonalFinanceSourceSpinner(int userID){
        spinner_choose_pfs = findViewById(R.id.spinner_choose_pfs);
        usersPFSList = database.personalFinanceSourceDao().getFinanceSourceByUser(userID);
        ArrayAdapter<String> adapterSpinner = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, usersPFSList);
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_choose_pfs.setAdapter(adapterSpinner);
    }

    public void setCurrencyForIncome (String userCountry){
        txt_view_income_currency = findViewById(R.id.txt_view_income_currency);
        String currecyCode = database.countryDao().getCurrencyCode(userCountry);
        String currencyName = database.currencyDao().getCurrencyName(currecyCode);
        txt_view_income_currency.setText(currencyName);
    }

    public void getValuesFromBundle(){
        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            userCountry = extras.getString("userCountry");
            userID = extras.getInt("userID");
        }
        setCurrencyForIncome (userCountry);
        populatePersonalFinanceSourceSpinner(userID);
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
                        IncomeActivity.this, new DatePickerDialog.OnDateSetListener() {
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
            img_doc_income.setImageBitmap(mBitmap);
        }
    }

    public void getContentFromInputFieldsAndInsertIncome(){
        String sIncomeAmount = edt_txt_amount_income.getText().toString();
        double incomeAmount =0;
        try {
            incomeAmount = (double)Double.parseDouble(sIncomeAmount);
        }catch (Exception e){
            e.printStackTrace();
        }

        String sRegDate = edt_txt_reg_date_income.getText().toString();
        Date regDate = null;
        try {
            regDate = new SimpleDateFormat("dd-MM-yyyy").parse(sRegDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        int subcatID=0;
        String sSubcategoryIncome ="";
        try {
            sSubcategoryIncome = spinner_subcategory_income.getSelectedItem().toString();
            subcatID = database.incomeSubcategoryDao().getIDsubCatIncome(sSubcategoryIncome);
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

        int radioID = radio_grp_recurrent_income.getCheckedRadioButtonId();
        String sRecurrency = "";
        int intRecurrecy = 0;
        try {
            radio_gen = findViewById(radioID);
            sRecurrency = radio_gen.getText().toString();
            if(sRecurrency.equals("Da")){
                intRecurrecy=1;
                Toast.makeText(IncomeActivity.this, getResources().getString(R.string.notifyIncomeSet), Toast.LENGTH_LONG).show();

                Intent notifyIncomeIntent = new Intent(IncomeActivity.this, ReminderBroadcast.class);
                notifyIncomeIntent.putExtra("message", getResources().getString(R.string.notifyIncomeText));
                PendingIntent pendingIntent = PendingIntent.getBroadcast(IncomeActivity.this, 0, notifyIncomeIntent, 0);

                AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

                long timeAtButtonClick = System.currentTimeMillis();

                long tenSecondsInMillis = 1000*30*24*60*60;

                alarmManager.set(AlarmManager.RTC_WAKEUP, timeAtButtonClick + tenSecondsInMillis, pendingIntent);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }

        String incomeNote = edt_txt_income_note.getText().toString();

        Bitmap bitmap_income_proof = null;
        try {
            bitmap_income_proof = ((BitmapDrawable) img_doc_income.getDrawable()).getBitmap();
        }catch (Exception e) {
            e.printStackTrace();
        }
        boolean hasDrawable = (bitmap_income_proof!=null);

        if(!sIncomeAmount.equals("") && !sRegDate.equals("") && !sSubcategoryIncome.equals("") &&
                !sPersFinSource.equals("") && !sRecurrency.equals("")) {
            if (hasDrawable) {
                insertIncomeWithPicture(userID, incomeAmount, regDate,subcatID,psfID, intRecurrecy,
                        incomeNote,bitmap_income_proof);
            } else {
                insertIncomeWithoutPicture(userID, incomeAmount, regDate, subcatID, psfID, intRecurrecy,
                        incomeNote);
            }
            form_add_income.setVisibility(View.GONE);
            up_income_activity_layout.setVisibility(View.VISIBLE);
            clearInputFields();
            resetFormErrorMessages();
        } else{
            throwFormErrorMessages();
        }

    }

    public void insertIncomeWithPicture (int userIDforInc, double incomeAmount,Date regDate,
                                         int subcatID, int psfID, int intRecurrecy,
                                         String income_note, Bitmap bitmap_income_proof_doc){
        Income income = new Income();
        income.setUser_id_inc(userIDforInc);
        income.setAmount_inc(incomeAmount);
        income.setDate_of_registration_inc(regDate);
        income.setSubcat_income_id_inc(subcatID);
        income.setFinance_source_id_inc(psfID);
        income.setRecurrent_inc(intRecurrecy);
        income.setNote_inc(income_note);
        income.setAttachment_inc(bitmap_income_proof_doc);

        database.incomeDao().insertIncome(income);

        int comp1 = regDate.compareTo(today);
        int comp2 = regDate.compareTo(sevenDaysBefore);
        int comp3 =regDate.compareTo(selectedDate1);
        int comp4 =regDate.compareTo(selectedDate2);

        if ((comp1 < 0 && comp2 > 0 ) ||(comp4 < 0 && comp3 >0 ))  {
            incomeList.add(income);
            incomeAdaptor.notifyDataSetChanged();
            recyclerViewIncome.scrollToPosition(incomeList.size()-1);
        }
        else{
            Toast.makeText(this, "Operațiune de înregistrare a venitului a avut loc cu success!" , Toast.LENGTH_LONG).show();
        }

    }

    public void insertIncomeWithoutPicture (int userIDforInc, double incomeAmount,Date regDate,
                                            int subcatID, int psfID, int intRecurrecy,
                                            String income_note){
        Income income = new Income();
        income.setUser_id_inc(userIDforInc);
        income.setAmount_inc(incomeAmount);
        income.setDate_of_registration_inc(regDate);
        income.setSubcat_income_id_inc(subcatID);
        income.setFinance_source_id_inc(psfID);
        income.setRecurrent_inc(intRecurrecy);
        income.setNote_inc(income_note);

        database.incomeDao().insertIncome(income);

        int comp1 = regDate.compareTo(today);
        int comp2 = regDate.compareTo(sevenDaysBefore);
        int comp3 =regDate.compareTo(selectedDate1);
        int comp4 =regDate.compareTo(selectedDate2);

        if ((comp1 < 0 && comp2 > 0 ) ||(comp4 < 0 && comp3 >0 ))  {
            incomeList.add(income);
            incomeAdaptor.notifyDataSetChanged();
            recyclerViewIncome.scrollToPosition(incomeList.size()-1);
        }
        else{
            Toast.makeText(this, "Operațiune de înregistrare a venitului a avut loc cu success!" , Toast.LENGTH_LONG).show();
        }

    }

    public void throwFormErrorMessages(){
        txt_view_required_amount_income.setText(getResources().getString(R.string.txt_view_required_amount));
        txt_view_required_reg_date_income.setText(getResources().getString(R.string.txt_view_required_reg_date));
        txt_view_required_cat_income.setText(getResources().getString(R.string.txt_view_required_cat));
        txt_view_required_subCat_income.setText(getResources().getString(R.string.txt_view_required_subCat));
        txt_view_required_psf_income.setText(getResources().getString(R.string.txt_view_required_psf));
        txt_view_required_recurrency_income.setText(getResources().getString(R.string.txt_view_required_recurrency));
    }

    public void clearInputFields(){
        edt_txt_amount_income.setText("");
        edt_txt_reg_date_income.setText("");
        edt_txt_income_note.setText("");
        radio_grp_income_cat.clearCheck();
        radio_grp_recurrent_income.clearCheck();
        img_doc_income.setImageResource(R.drawable.ic_photo);
    }

    public void resetFormErrorMessages(){
        txt_view_required_amount_income.setText(getResources().getString(R.string.txt_view_star));
        txt_view_required_reg_date_income.setText(getResources().getString(R.string.txt_view_star));
        txt_view_required_cat_income.setText(getResources().getString(R.string.txt_view_star));
        txt_view_required_subCat_income.setText(getResources().getString(R.string.txt_view_star));
        txt_view_required_psf_income.setText(getResources().getString(R.string.txt_view_star));
        txt_view_required_recurrency_income.setText(getResources().getString(R.string.txt_view_star));
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
            Intent main_activity_intent = new Intent(IncomeActivity.this, MainActivity.class);
            startActivity(main_activity_intent);

        }

        return super.onOptionsItemSelected(item);
    }


}