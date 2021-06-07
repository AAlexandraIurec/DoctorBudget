package com.example.doctorBudget.Activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;

import com.example.doctorBudget.MyCalendar;
import com.example.doctorBudget.RoomDB;
import com.example.doctorBudget.Entities.User;
import com.example.doctorBudget.R;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    RoomDB database;

    EditText edt_txt_last_name, edt_txt_first_name, edt_txt_email, edt_txt_birthDay, edt_txt_occupation;
    Button btn_add_user;
    ImageView img_profile_user_upload;
    Spinner country_spinner;
    TextView txt_view_lstNme_req, txt_view_fstNme_req, txt_view_country_req, txt_view_reg_email, txt_view_reg_birthDay;
    MyCalendar calendar;

    List<String> countriesList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        edt_txt_last_name = findViewById(R.id.edt_txt_last_name);
        edt_txt_first_name = findViewById(R.id.edt_txt_first_name);
        edt_txt_email = findViewById(R.id.edt_txt_email);
        edt_txt_birthDay = findViewById(R.id.edt_txt_birthDay);
        edt_txt_occupation = findViewById(R.id.edt_txt_occupation);

        country_spinner = findViewById(R.id.spinner_Country);

        img_profile_user_upload = findViewById(R.id.img_profile_user_upload);

        btn_add_user = findViewById(R.id.btn_add_user);

        txt_view_lstNme_req =  findViewById(R.id.txt_view_required_lstNme);
        txt_view_fstNme_req =findViewById(R.id.txt_view_required_fstNme);
        txt_view_country_req=findViewById(R.id.txt_view_required_country);
        txt_view_reg_email = findViewById(R.id.txt_view_reg_email);
        txt_view_reg_birthDay = findViewById(R.id.txt_view_reg_birthDay);

        database = RoomDB.getInstance(this);
        countriesList = database.countryDao().getAllCountryNames();


        img_profile_user_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage(img_profile_user_upload);
            }
        });

        btn_add_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getContentFromInputFieldsAndInsertUser();
            }
        });

        calendar = new MyCalendar();
        calendar.prepareCalendar(edt_txt_birthDay, UserActivity.this);
        populateCountrySpinner();

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

        String regexEmail = "^(.+)@(.+)$";
        String regexBirthDate = "^[0-9]?[0-9]-[0-9][0-9]-[0-9][0-9][0-9][0-9]$";

        Pattern patternDate = Pattern.compile(regexBirthDate);
        Pattern patternEmail = Pattern.compile(regexEmail);
        Matcher matchEmail = patternEmail.matcher(sEmail);
        Matcher matchDate = patternDate.matcher(sBirthDay);

        if(!sLastName.equals("") && !sFirstName.equals("") && !sCountry.equals(" ")) {
            if(matchEmail.matches()) {
                if(matchDate.matches() ) {
                    if (hasDrawable) {
                        insertUserWithProfilePicture(sLastName, sFirstName, sEmail, bDate1, sCountry, sOccupation, bitmap_user_profile);
                    } else {
                        insertUser(sLastName, sFirstName, sEmail, bDate1, sCountry, sOccupation);
                    }
                }else{
                    txt_view_reg_birthDay.setText(getResources().getString(R.string.txt_view_reg_birthDay));
                }
            }else{
                txt_view_reg_email.setText(getResources().getString(R.string.txt_view_reg_email));
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

        database.userDao().inserUser(user);

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

        database.userDao().inserUser(user);

        Intent main_activity_intent = new Intent(UserActivity.this, MainActivity.class);
        startActivity(main_activity_intent);
    }

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
            Intent main_activity_intent = new Intent(UserActivity.this, MainActivity.class);
            startActivity(main_activity_intent);

        }
        return super.onOptionsItemSelected(item);
    }

}