package com.example.doctorBudget.Activities;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;

import com.example.doctorBudget.RoomDB;
import com.example.doctorBudget.Entities.User;
import com.example.doctorBudget.R;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class UserActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    EditText edt_txt_last_name, edt_txt_first_name, edt_txt_email, edt_txt_birthDay, edt_txt_occupation;
    Button btn_add_user;
    ImageView img_profile_user_upload;
    Spinner country_spinner;
    TextView txt_view_lstNme_req, txt_view_fstNme_req, txt_view_country_req;

    List<String> countriesList = new ArrayList<>();
    List<User> userList = new ArrayList<>();

    RoomDB database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Assign variable
        edt_txt_last_name = findViewById(R.id.edt_txt_last_name);
        edt_txt_first_name = findViewById(R.id.edt_txt_first_name);
        edt_txt_email = findViewById(R.id.edt_txt_email);
        edt_txt_birthDay = findViewById(R.id.edt_txt_birthDay);
        country_spinner = findViewById(R.id.spinner_Country);
        edt_txt_occupation = findViewById(R.id.edt_txt_occupation);
        img_profile_user_upload = findViewById(R.id.img_profile_user_upload);

        btn_add_user = findViewById(R.id.btn_add_user);

        txt_view_lstNme_req =  findViewById(R.id.txt_view_required_lstNme);
        txt_view_fstNme_req =findViewById(R.id.txt_view_required_fstNme);
        txt_view_country_req=findViewById(R.id.txt_view_required_country);

        database = RoomDB.getInstance(this);
        userList = database.userDao().getAllUsers();
        countriesList = database.countryDao().getAllCountryNames();


        img_profile_user_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage(img_profile_user_upload);
            }
        });

        prepareCalendar();
        populateCountrySpinner();

        btn_add_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                  getContentFromInputFieldsAndInsertUser();
            }
        });


    }
    public void getContentFromInputFieldsAndInsertUser(){
        String sLastName = edt_txt_last_name.getText().toString();
        String sFirstName = edt_txt_first_name.getText().toString();
        String sEmail = edt_txt_email.getText().toString();
        String sBirthDay = edt_txt_birthDay.getText().toString();

        Date bDate1 = null;
        try {
            bDate1 = new SimpleDateFormat("dd-MM-yyyy").parse(sBirthDay);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String sCountry = country_spinner.getSelectedItem().toString();
        String sOccupation = edt_txt_occupation.getText().toString();

        Bitmap bitmap_user_profile = null;
        try {
            bitmap_user_profile = ((BitmapDrawable) img_profile_user_upload.getDrawable()).getBitmap();
        }catch (Exception e) {
            e.printStackTrace();
        }
        boolean hasDrawable = (bitmap_user_profile!=null);

        if(!sLastName.equals("") && !sFirstName.equals("") && !sCountry.equals(" ")) {
            if (hasDrawable) {
                insertUserWithProfilePicture(sLastName, sFirstName, sEmail, bDate1, sCountry, sOccupation, bitmap_user_profile);
            } else {
                insertUser(sLastName, sFirstName, sEmail, bDate1, sCountry, sOccupation);
            }
        }
        else{
            txt_view_lstNme_req.setText(getResources().getString(R.string.txt_view_user_lstNme_req));
            txt_view_fstNme_req.setText(getResources().getString(R.string.txt_view_user_fstNme_req));
            txt_view_country_req.setText(getResources().getString(R.string.txt_view_user_country_req));
        }

    }


    public void insertUserWithProfilePicture(String lName,String fName,String email,Date bDay,String country,String occupation, Bitmap proPicture){
        User user = new User();
        user.setLastName(lName);
        user.setFirstName(fName);
        user.setEmail(email);
        user.setDate_of_birth(bDay);
        user.setCountry(country);
        user.setOccupation(occupation);
        user.setProfilePicture(proPicture);

        //Insert text in database
        database.userDao().inserUser(user);

        //Notify when data is inserted
        userList.clear();
        userList.addAll(database.userDao().getAllUsers());
        Intent main_activity_intent = new Intent(UserActivity.this, MainActivity.class);
        startActivity(main_activity_intent);
    }

    public void insertUser(String lName,String fName,String email,Date bDay,String country,String occupation){
        User user = new User();
        user.setLastName(lName);
        user.setFirstName(fName);
        user.setEmail(email);
        user.setDate_of_birth(bDay);
        user.setCountry(country);
        user.setOccupation(occupation);

        //Insert text in database
        database.userDao().inserUser(user);

        //Notify when data is inserted
        userList.clear();
        userList.addAll(database.userDao().getAllUsers());
        Intent main_activity_intent = new Intent(UserActivity.this, MainActivity.class);
        startActivity(main_activity_intent);
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
            img_profile_user_upload.setImageBitmap(mBitmap);
        }
    }

    public void prepareCalendar() {
        //Working with the calendar
        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        edt_txt_birthDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        UserActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        month = month+1;
                        if(month < 10) {
                            String sBirthDay = dayOfMonth + "-" + "0" + month + "-" + year;
                            edt_txt_birthDay.setText(sBirthDay);
                        }
                         else {
                            String sBirthDay = dayOfMonth + "-"  + month + "-" + year;
                            edt_txt_birthDay.setText(sBirthDay);
                        }
                    }
                }, year, month, day);
                datePickerDialog.show();
            }

        });
    }

    public void populateCountrySpinner(){
        countriesList.add(0," ");
        ArrayAdapter <String> adapterSpinner = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, countriesList);
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        country_spinner.setAdapter(adapterSpinner);
        country_spinner.setOnItemSelectedListener(this);
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        if(parent.getItemAtPosition(position).toString().equals("România")){

            Toast.makeText(parent.getContext(), "Moneda de înregistrare a fost stabilită automat: Lei " , Toast.LENGTH_LONG).show();

        }else if(parent.getItemAtPosition(position).toString().equals("Republica Moldova")){

            Toast.makeText(parent.getContext(), "Moneda de înregistrare a fost stabilită automat: Lei Moldovenești " , Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

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
            Intent main_activity_intent = new Intent(UserActivity.this, MainActivity.class);
            startActivity(main_activity_intent);

        }

        return super.onOptionsItemSelected(item);
    }

}