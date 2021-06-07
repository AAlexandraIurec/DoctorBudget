package com.example.doctorBudget.RecyclerViews;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doctorBudget.Entities.Income;
import com.example.doctorBudget.R;
import com.example.doctorBudget.RoomDB;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class RecyclerViewIncomeAdaptor extends RecyclerView.Adapter<RecyclerViewIncomeAdaptor.ViewHolder> {
    private List<Income> incomeList;
    private Activity context;
    private View up_activity_layout;
    private View form;
    private Date today,sevenBefore;
    private RoomDB database;



    public RecyclerViewIncomeAdaptor(Activity context, List<Income> incomeList) {
        this.context = context;
        this.incomeList = incomeList;
        this.up_activity_layout =context.findViewById(R.id.up_income_activity_layout);
        this.form = context.findViewById(R.id.form_add_income);;
        this.today= new Date();
        long ltime=today.getTime()-7*24*60*60*1000;
        this.sevenBefore = new Date(ltime);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerViewIncomeAdaptor.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_row_basic, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewIncomeAdaptor.ViewHolder holder, int position) {
        //Initialize main data
        Income income = incomeList.get(position);
        //Initialize database
        database = RoomDB.getInstance(context);
        //Set text on text view

        double incomeAmount = income.getAmount_inc();
        String sIncomeAmount = String.valueOf(incomeAmount);
        holder.txt_view_list_basicTitle.setText(sIncomeAmount);

        int psfID = income.getFinance_source_id_inc();
        holder.txt_view_list_basicSubtitle.setText(database.personalFinanceSourceDao().getFinanceSourceNameById(psfID));

        Date regDate = income.getDate_of_registration_inc();
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String sRegDate = dateFormat.format(regDate);
        holder.txt_view_list_basicNote.setText(sRegDate);

        holder.btn_edit_bsc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                up_activity_layout.setVisibility(View.GONE);
                form.setVisibility(View.VISIBLE);

                Income inc = incomeList.get(holder.getAdapterPosition());

                int incID = inc.getIncome_id();
                int incUserID = inc.getUser_id_inc();

                double incAmount = inc.getAmount_inc();
                String sIncAmount = String.valueOf(incAmount);

                Date incRDate = inc.getDate_of_registration_inc();
                DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyy");
                String sIncRDate = dateFormat.format(incRDate);

                int incRecurrent = inc.getRecurrent_inc();

                String incNote = inc.getNote_inc();

                Bitmap incAttachment = inc.getAttachment_inc();

                int incSubcatIncomeId = inc.getSubcat_income_id_inc();
                String cat = database.incomeCategoryDao().getIncomeCategoryName(incSubcatIncomeId);

                EditText amount = context.findViewById(R.id.edt_txt_amount_income);
                amount.setText(sIncAmount);
                EditText regDate = context.findViewById(R.id.edt_txt_reg_date_income);
                regDate.setText(sIncRDate);

                RadioButton rdoCatFix = context.findViewById(R.id.rdo_fix_income);
                RadioButton rdoCatVar = context.findViewById(R.id.rdo_variable_income);
                Spinner spinnerSubcategoryIncome = context.findViewById(R.id.spinner_subcategory_income);
                List<String> subcategoryIncomeList;
                if(cat.equals("Venit fix")){
                    rdoCatFix.setChecked(true);
                    subcategoryIncomeList = database.incomeSubcategoryDao().getAllIncomeSubcategoryNames(1);
                    ArrayAdapter<String> adapterSpinner = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, subcategoryIncomeList);
                    adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerSubcategoryIncome.setAdapter(adapterSpinner);
                    spinnerSubcategoryIncome.setSelection(incSubcatIncomeId);
                }else
                if(cat.equals("Venit variabil")){
                    rdoCatVar.setChecked(true);
                    subcategoryIncomeList = database.incomeSubcategoryDao().getAllIncomeSubcategoryNames(2);
                    ArrayAdapter<String> adapterSpinner = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, subcategoryIncomeList);
                    adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerSubcategoryIncome.setAdapter(adapterSpinner);
                    if(incSubcatIncomeId==6)
                        spinnerSubcategoryIncome.setSelection(incSubcatIncomeId-incSubcatIncomeId);
                    else  if(incSubcatIncomeId==7)
                        spinnerSubcategoryIncome.setSelection(incSubcatIncomeId-6);
                    else spinnerSubcategoryIncome.setSelection(incSubcatIncomeId-6);
                }

                Spinner spinnerPSFIncome = context.findViewById(R.id.spinner_choose_pfs);

                EditText note =context.findViewById(R.id.edt_txt_income_note);
                note.setText(incNote);

                RadioGroup radio_grp_recurrent_income = context.findViewById( R.id.radio_grp_recurrent_income);
                RadioButton recYes = context.findViewById(R.id.rdo_rec_yes_income);
                RadioButton recNo =context.findViewById(R.id.rdo_rec_no_income);
                if(incRecurrent==1)
                    recYes.setChecked(true);
                else
                    recNo.setChecked(true);

                ImageView img_doc_income=context.findViewById(R.id.img_doc_income);
                img_doc_income.setImageBitmap(incAttachment );

                Button btn_add_income = context.findViewById(R.id.btn_add_income);
                Button btn_update_income = context.findViewById(R.id.btn_update_income);
                btn_add_income.setVisibility(View.GONE);
                btn_update_income.setVisibility(View.VISIBLE);

                btn_update_income.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        form.setVisibility(View.GONE);
                        up_activity_layout.setVisibility(View.VISIBLE);

                        String sIncomeAmount = amount.getText().toString();
                        double incomeAmount =0;
                        try {
                            incomeAmount = (double)Double.parseDouble(sIncomeAmount);
                        }catch (Exception e){
                            e.printStackTrace();
                        }

                        String sRegDate = regDate.getText().toString();
                        Date regDateInc = null;
                        try {
                            regDateInc = new SimpleDateFormat("dd-MM-yyyy").parse(sRegDate);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        int subcatID=0;
                        String sSubcategoryIncome ="";
                        try {
                            sSubcategoryIncome = spinnerSubcategoryIncome.getSelectedItem().toString();
                            subcatID = database.incomeSubcategoryDao().getIDsubCatIncome(sSubcategoryIncome);
                        }catch (Exception i) {
                            i.printStackTrace();
                        }

                        int psfID=0;
                        String sPersFinSource="";
                        try {
                            sPersFinSource = spinnerPSFIncome.getSelectedItem().toString();
                            psfID = database.personalFinanceSourceDao().getIDFinanceSource(sPersFinSource);
                        }catch (Exception i) {
                            i.printStackTrace();
                        }

                        int radioID = radio_grp_recurrent_income.getCheckedRadioButtonId();
                        String sRecurrency = "";
                        int intRecurrecy = 0;
                        try {
                            RadioButton radio_gen = context.findViewById(radioID);
                            sRecurrency = radio_gen.getText().toString();
                            if(sRecurrency.equals("Da")){
                                intRecurrecy=1;
                            }
                        }catch (Exception e) {
                            e.printStackTrace();
                        }

                        String incomeNote = note.getText().toString();

                        Bitmap bitmap_income_proof = null;
                        try {
                            bitmap_income_proof = ((BitmapDrawable) img_doc_income.getDrawable()).getBitmap();
                        }catch (Exception e) {
                            e.printStackTrace();
                        }
                        //Update test in database
                        database.incomeDao().updateIncome(incomeAmount,regDateInc,intRecurrecy,
                                incomeNote,bitmap_income_proof,subcatID,psfID,incUserID,incID);
                        //Notify when data is updated
                        incomeList.clear();
                        //data:
                        List<Income> upIncomeList=database.incomeDao().getAllIncomesByDates(incUserID,sevenBefore,today);
                        incomeList.addAll(upIncomeList);
                        notifyDataSetChanged();
                    }
                });

            }
        });
        holder.btn_delete_bsc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Initialize main data
                Income inc = incomeList.get(holder.getAdapterPosition());

                //Create dialog
                Dialog dialog = new Dialog(context);
                // Set content view
                dialog.setContentView(R.layout.dialog_delete);
                //Initialize width
                int width = WindowManager.LayoutParams.MATCH_PARENT;
                //Initialize height
                int height = WindowManager.LayoutParams.WRAP_CONTENT;
                //Set layout
                dialog.getWindow().setLayout(width, height);
                //Show dialog
                dialog.show();
                //Initialize and assign variable

                TextView textViewDelete = dialog.findViewById(R.id.delete_view_text);
                Button btnYes = dialog.findViewById(R.id.btn_yes_delete);
                Button btnNo = dialog.findViewById(R.id.btn_no_delete);

                btnYes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Delete text from database
                        database.incomeDao().deleteIncome(inc);
                        //Notify when data is deleted
                        int position = holder.getAdapterPosition();
                        incomeList.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, incomeList.size());
                        dialog.dismiss();
                    }
                });

                btnNo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
            }
        });

    }

    @Override
    public int getItemCount() {
        return incomeList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        //Initialize variable
        TextView txt_view_list_basicTitle,txt_view_list_basicSubtitle,txt_view_list_basicNote;
        ImageView btn_delete_bsc, btn_edit_bsc;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txt_view_list_basicTitle = itemView.findViewById(R.id.txt_view_list_basicTitle);
            txt_view_list_basicSubtitle = itemView.findViewById(R.id.txt_view_list_basicSubtitle);
            txt_view_list_basicNote = itemView.findViewById(R.id.txt_view_list_basicNote);
            btn_delete_bsc = itemView.findViewById(R.id.btn_delete_bsc);
            btn_edit_bsc = itemView.findViewById(R.id.btn_edit_bsc);
        }

    }
}
