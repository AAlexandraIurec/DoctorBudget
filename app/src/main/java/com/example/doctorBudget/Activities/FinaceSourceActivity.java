package com.example.doctorBudget.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.doctorBudget.RecyclerViews.RecyclerViewPFSAdaptor;
import com.example.doctorBudget.RoomDB;
import com.example.doctorBudget.Entities.PersonalFinanceSource;

import com.example.doctorBudget.R;

import java.util.ArrayList;
import java.util.List;

public class FinaceSourceActivity extends AppCompatActivity {

    RoomDB database;
    List<String> pfs_cat_list = new ArrayList<>();
    RadioGroup radio_grp_cat_psf;
    RadioButton radio_gen,radio_pfs_casf,radio_pfs_dbt_card,radio_pfs_meal_tkt,
            radio_pfs_crd_card, radio_pfs_others;
    EditText edt_txt_pfs_name, edt_txt_pfs_note;
    Button btn_add_pfs, btn_update_pfs;
    TextView txt_view_pfs_name_req, txt_view_pfs_cat_req;

    int user_id_pfs=0;

    List<PersonalFinanceSource> pfs_list = new ArrayList<>();
    RecyclerView recycler_View_pfs;
    RecyclerViewPFSAdaptor pfs_rv_adaptor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finace_source);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        database = RoomDB.getInstance(this);
        initializeAndPopulateRadioButtons();

        edt_txt_pfs_name = findViewById(R.id.edt_txt_pfs_name);
        edt_txt_pfs_note = findViewById(R.id.edt_txt_pfs_note);

        btn_add_pfs = findViewById(R.id.btn_add_pfs);
        btn_update_pfs =findViewById(R.id.btn_update_pfs);

        txt_view_pfs_name_req = findViewById(R.id.txt_view_required_pfs_name);
        txt_view_pfs_cat_req = findViewById(R.id.txt_view_required_pfs_category);

        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            user_id_pfs = extras.getInt("userId");
        }

        pfs_list = database.personalFinanceSourceDao().getAllPersonalFinanceSources(user_id_pfs);
        configRecyclerViewPFSAndSetAdapter ();

        btn_add_pfs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getContentFromInputFieldsAndInsertPFS();
            }
        });

        btn_update_pfs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });


    }

    public void configRecyclerViewPFSAndSetAdapter (){
        recycler_View_pfs = findViewById(R.id.recycler_view_personal_finance_source);
        pfs_rv_adaptor = new RecyclerViewPFSAdaptor(FinaceSourceActivity.this, pfs_list);
        RecyclerView.LayoutManager linearLayoutManager = new LinearLayoutManager(FinaceSourceActivity.this);
        recycler_View_pfs.setLayoutManager(linearLayoutManager);
        recycler_View_pfs.setAdapter(pfs_rv_adaptor);
    }

    public void initializeAndPopulateRadioButtons(){
        radio_pfs_casf = findViewById(R.id.rdo_pfs_cash);
        radio_pfs_dbt_card = findViewById(R.id.rdo_pfs_dbt_card);
        radio_pfs_meal_tkt= findViewById(R.id.rdo_pfs_meal_tkt);
        radio_pfs_crd_card= findViewById(R.id.rdo_pfs_crd_card);
        radio_pfs_others= findViewById(R.id.rdo_pfs_others);

        pfs_cat_list = database.personalFinanceSourceCategoryDao().getAllPersonalFinanceSourceCatNames();

        radio_pfs_casf.setText(pfs_cat_list.get(0));
        radio_pfs_dbt_card.setText(pfs_cat_list.get(1));
        radio_pfs_meal_tkt.setText(pfs_cat_list.get(2));
        radio_pfs_crd_card.setText(pfs_cat_list.get(3));
        radio_pfs_others.setText(pfs_cat_list.get(4));
    }


    public void getContentFromInputFieldsAndInsertPFS(){
        String pfs_name = edt_txt_pfs_name.getText().toString();
        String pfs_note = edt_txt_pfs_note.getText().toString();

        radio_grp_cat_psf = findViewById( R.id.radio_grp_pfs_cat);
        int radioID = radio_grp_cat_psf.getCheckedRadioButtonId();
        int pfs_cat_id=0;
        String psf_cat_name = "";
        try {
            radio_gen = findViewById(radioID);
            psf_cat_name = radio_gen.getText().toString();
            pfs_cat_id = database.personalFinanceSourceCategoryDao().obtainPfsCatID(psf_cat_name);
        }catch (Exception e) {
            e.printStackTrace();
        }
        if(!pfs_name.equals("") && !psf_cat_name.equals("")) {
            insertPersonalFinanceSource(user_id_pfs, pfs_name, pfs_note, pfs_cat_id);
            pfs_rv_adaptor.notifyDataSetChanged();
            recycler_View_pfs.scrollToPosition(pfs_list.size()-1);
            clearInputFields();
        }
        else{
            txt_view_pfs_name_req.setText(getResources().getString(R.string.txt_view_pfs_name_req));
            txt_view_pfs_cat_req.setText(getResources().getString(R.string.txt_view_pfs_category_req));
        }

    }

    public void insertPersonalFinanceSource(int user_id, String pfs_name, String pfs_note, int pfs_cat_id){
        PersonalFinanceSource personalFinanceSource = new PersonalFinanceSource();
        personalFinanceSource.setUser_id_pfs(user_id);
        personalFinanceSource.setFinance_source_name(pfs_name);
        personalFinanceSource.setNote(pfs_note);
        personalFinanceSource.setFinance_source_cat_id_pfs(pfs_cat_id);

        //Insert text in database
        database.personalFinanceSourceDao().insertPersonalFinanceSource(personalFinanceSource);
        pfs_list.add(personalFinanceSource);

    }

    public void clearInputFields(){
        edt_txt_pfs_name.setText("");
        edt_txt_pfs_note.setText("");
        radio_grp_cat_psf.clearCheck();
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
            Intent main_activity_intent = new Intent(FinaceSourceActivity.this, MainActivity.class);
            startActivity(main_activity_intent);

        }

        return super.onOptionsItemSelected(item);
    }

}