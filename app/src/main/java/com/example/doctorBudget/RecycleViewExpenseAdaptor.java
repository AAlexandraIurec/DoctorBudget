package com.example.doctorBudget;

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

import com.example.doctorBudget.Entities.Expense;
import com.example.doctorBudget.Entities.Income;
import com.example.doctorBudget.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class RecycleViewExpenseAdaptor extends RecyclerView.Adapter<RecycleViewExpenseAdaptor.ViewHolder> {
    private List<Expense> expenseList;
    private Activity context;
    private RoomDB database;
    private View up_activity_layout_expense, form_add_expense;
    private Date today_exp,sevenBefore_exp;
    

    public RecycleViewExpenseAdaptor(Activity context,List<Expense> expenseList) {
        this.context = context;
        this.expenseList = expenseList;
        this.up_activity_layout_expense=context.findViewById(R.id.up_expense_activity_layout);
        this.form_add_expense = context.findViewById(R.id.form_add_expense);
        this.today_exp= new Date();
        long ltime=today_exp.getTime()-7*24*60*60*1000;
        this.sevenBefore_exp = new Date(ltime);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecycleViewExpenseAdaptor.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_row_basic, parent, false);
        return new RecycleViewExpenseAdaptor.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecycleViewExpenseAdaptor.ViewHolder holder, int position) {
        //Initialize main data
        Expense expense = expenseList.get(position);
        //Initialize database
        database = RoomDB.getInstance(context);
        //Set text on text view

        double expenseAmount = expense.getAmount_exp();
        String sIncomeAmount = String.valueOf(expenseAmount);
        holder.txt_view_list_basicTitle.setText(sIncomeAmount);

        int psfID = expense.getFinance_source_id_exp();
        holder.txt_view_list_basicSubtitle.setText(database.personalFinanceSourceDao().getFinanceSourceNameById(psfID));

        Date regDate = expense.getDate_of_registration_exp();
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String sRegDate = dateFormat.format(regDate);
        holder.txt_view_list_basicNote.setText(sRegDate);

        holder.btn_edit_bsc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                up_activity_layout_expense.setVisibility(View.GONE);
                form_add_expense.setVisibility(View.VISIBLE);

                Expense exp = expenseList.get(holder.getAdapterPosition());

                int expID = exp.getExpense_id();
                int expUserID = exp.getUser_id_exp();

                double expAmount = exp.getAmount_exp();
                String sExpAmount = String.valueOf(expAmount);

                Date expRDate = exp.getDate_of_registration_exp();
                DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyy");
                String sExpRDate = dateFormat.format(expRDate);

                int expRecurrent = exp.getRecurrent_exp();

                String expNote = exp.getNote_exp();

                Bitmap expAttachment = exp.getAttachment_exp();

                int expSubcatId = exp.getSubcat_expense_id_exp();
                String cat = database.expenseCategoryDao().getExpenseCategoryName(expSubcatId);

                EditText amount = context.findViewById(R.id.edt_txt_amount_expense);
                amount.setText(sExpAmount);
                EditText regDate = context.findViewById(R.id.edt_txt_reg_date_expense);
                regDate.setText(sExpRDate);

                RadioButton rdoCatFix = context.findViewById(R.id.rdo_fix_expense);
                RadioButton rdoCatVar = context.findViewById(R.id.rdo_variable_expense);
                RadioButton rdoCatDis = context.findViewById(R.id.rdo_discretionary_expense);
                Spinner spinnerSubcategoryExpense = context.findViewById(R.id.spinner_subcategory_expense);
                List<String> subcategoryExpenseList;

                if(cat.equals("Cheltuială fixă")){
                    rdoCatFix.setChecked(true);
                    subcategoryExpenseList = database.expenseSubcategoryDao().getAllExpenseSubcategoryNames(1);
                    ArrayAdapter<String> adapterSpinner = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, subcategoryExpenseList);
                    adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerSubcategoryExpense.setAdapter(adapterSpinner);
                    spinnerSubcategoryExpense.setSelection(expSubcatId);
                }else
                if(cat.equals("Cheltuială variabilă")){
                    rdoCatVar.setChecked(true);
                    subcategoryExpenseList = database.expenseSubcategoryDao().getAllExpenseSubcategoryNames(2);
                    ArrayAdapter<String> adapterSpinner = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, subcategoryExpenseList);
                    adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerSubcategoryExpense.setAdapter(adapterSpinner);
                    if(expSubcatId==6)
                        spinnerSubcategoryExpense.setSelection(expSubcatId-6);
                    else  if(expSubcatId==7)
                        spinnerSubcategoryExpense.setSelection(expSubcatId-6);
                    else if(expSubcatId==8)
                        spinnerSubcategoryExpense.setSelection(expSubcatId-6);
                    else if(expSubcatId==9)
                        spinnerSubcategoryExpense.setSelection(expSubcatId-6);
                    else if(expSubcatId==10)
                        spinnerSubcategoryExpense.setSelection(expSubcatId-6);
                    else if(expSubcatId==11)
                        spinnerSubcategoryExpense.setSelection(expSubcatId-6);
                    else if(expSubcatId==12)
                        spinnerSubcategoryExpense.setSelection(expSubcatId-6);
                    else if(expSubcatId==13)
                        spinnerSubcategoryExpense.setSelection(expSubcatId-6);
                    else if(expSubcatId==14)
                        spinnerSubcategoryExpense.setSelection(expSubcatId-6);
                }else if(cat.equals("Cheltuială discreționară")){
                    rdoCatDis.setChecked(true);
                    subcategoryExpenseList = database.expenseSubcategoryDao().getAllExpenseSubcategoryNames(3);
                    ArrayAdapter<String> adapterSpinner = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, subcategoryExpenseList);
                    adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerSubcategoryExpense.setAdapter(adapterSpinner);
                    if(expSubcatId==15)
                        spinnerSubcategoryExpense.setSelection(expSubcatId-15);
                    else  if(expSubcatId==16)
                        spinnerSubcategoryExpense.setSelection(expSubcatId-15);
                    else if(expSubcatId==17)
                        spinnerSubcategoryExpense.setSelection(expSubcatId-15);
                    else if(expSubcatId==18)
                        spinnerSubcategoryExpense.setSelection(expSubcatId-15);
                    else if(expSubcatId==19)
                        spinnerSubcategoryExpense.setSelection(expSubcatId-15);
                    else if(expSubcatId==20)
                        spinnerSubcategoryExpense.setSelection(expSubcatId-15);
                    else if(expSubcatId==21)
                        spinnerSubcategoryExpense.setSelection(expSubcatId-15);
                    else if(expSubcatId==22)
                        spinnerSubcategoryExpense.setSelection(expSubcatId-15);
                    else if(expSubcatId==23)
                        spinnerSubcategoryExpense.setSelection(expSubcatId-15);
                    else if(expSubcatId==24)
                        spinnerSubcategoryExpense.setSelection(expSubcatId-15);
                    else if(expSubcatId==25)
                        spinnerSubcategoryExpense.setSelection(expSubcatId-15);
                    else if(expSubcatId==26)
                        spinnerSubcategoryExpense.setSelection(expSubcatId-15);
                    else if(expSubcatId==27)
                        spinnerSubcategoryExpense.setSelection(expSubcatId-15);
                }

                Spinner spinnerPSFExpense = context.findViewById(R.id.spinner_choose_pfs_exp);

                EditText note =context.findViewById(R.id.edt_txt_expense_note);
                note.setText(expNote);

                RadioGroup radio_grp_recurrent_expense = context.findViewById( R.id.radio_grp_recurrent_expense);
                RadioButton recYes = context.findViewById(R.id.rdo_rec_yes_expense);
                RadioButton recNo =context.findViewById(R.id.rdo_rec_no_expense);
                if(expRecurrent==1)
                    recYes.setChecked(true);
                else
                    recNo.setChecked(true);

                ImageView img_doc_expense=context.findViewById(R.id.img_doc_expense);
                img_doc_expense.setImageBitmap(expAttachment);

                Button btn_add_expense = context.findViewById(R.id.btn_add_expense);
                Button btn_update_expense = context.findViewById(R.id.btn_update_expense);
                btn_add_expense.setVisibility(View.GONE);
                btn_update_expense.setVisibility(View.VISIBLE);

                btn_update_expense.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        form_add_expense.setVisibility(View.GONE);
                        up_activity_layout_expense.setVisibility(View.VISIBLE);

                        String sExpenseAmount = amount.getText().toString();
                        double expenseAmount =0;
                        try {
                            expenseAmount = (double)Double.parseDouble(sExpenseAmount);
                        }catch (Exception e){
                            e.printStackTrace();
                        }

                        String sRegDate = regDate.getText().toString();
                        Date regDateExp = null;
                        try {
                            regDateExp = new SimpleDateFormat("dd-MM-yyyy").parse(sRegDate);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        int subcatID=0;
                        String sSubcategoryExpense ="";
                        try {
                            sSubcategoryExpense = spinnerSubcategoryExpense.getSelectedItem().toString();
                            subcatID = database.expenseSubcategoryDao().getIDsubCatExpense(sSubcategoryExpense);
                        }catch (Exception i) {
                            i.printStackTrace();
                        }

                        int psfID=0;
                        String sPersFinSource="";
                        try {
                            sPersFinSource = spinnerPSFExpense.getSelectedItem().toString();
                            psfID = database.personalFinanceSourceDao().getIDFinanceSource(sPersFinSource);
                        }catch (Exception i) {
                            i.printStackTrace();
                        }

                        int radioIDexp = radio_grp_recurrent_expense.getCheckedRadioButtonId();
                        String sRecurrency = "";
                        int intRecurrecy = 0;
                        try {
                            RadioButton radio_gen = context.findViewById(radioIDexp);
                            sRecurrency = radio_gen.getText().toString();
                            if(sRecurrency.equals("Da")){
                                intRecurrecy=1;
                            }
                        }catch (Exception e) {
                            e.printStackTrace();
                        }

                        String expenseNote = note.getText().toString();

                        Bitmap bitmap_expense_proof = null;
                        try {
                            bitmap_expense_proof = ((BitmapDrawable) img_doc_expense.getDrawable()).getBitmap();
                        }catch (Exception e) {
                            e.printStackTrace();
                        }

                        //Update test in database
                        database.expenseDao().updateExpense(expenseAmount,regDateExp,intRecurrecy,
                                expenseNote,bitmap_expense_proof,subcatID,psfID,expUserID,expID);
                        //Notify when data is updated
                        expenseList.clear();
                        //data:
                        List<Expense> upExpenseList=database.expenseDao().getAllExpensesByDates(expUserID,sevenBefore_exp,today_exp);
                        expenseList.addAll(upExpenseList);
                        notifyDataSetChanged();
                    }
                });

            }
        });

        holder.btn_delete_bsc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Initialize main data
                Expense exp = expenseList.get(holder.getAdapterPosition());

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
                        database.expenseDao().deleteExpense(exp);
                        //Notify when data is deleted
                        int position = holder.getAdapterPosition();
                        expenseList.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, expenseList.size());
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
        return expenseList.size();
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
