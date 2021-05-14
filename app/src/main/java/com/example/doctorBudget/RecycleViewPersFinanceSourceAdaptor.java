package com.example.doctorBudget;

import android.app.Activity;
import android.app.Dialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doctorBudget.Entities.Expense;
import com.example.doctorBudget.Entities.PersonalFinanceSource;
import com.example.doctorBudget.R;

import java.util.List;

public class RecycleViewPersFinanceSourceAdaptor extends RecyclerView.Adapter<RecycleViewPersFinanceSourceAdaptor.ViewHolder> {

    //Initialize variable
    private List<PersonalFinanceSource> personalFinanceSourceList;
    private Activity context;
    private RoomDB database;


    public RecycleViewPersFinanceSourceAdaptor(Activity context,List<PersonalFinanceSource> personalFinanceSourceList) {
        this.context = context;
        this.personalFinanceSourceList = personalFinanceSourceList;
    }

    @NonNull
    @Override
    public RecycleViewPersFinanceSourceAdaptor.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_row_basic, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecycleViewPersFinanceSourceAdaptor.ViewHolder holder, int position) {

        //Initialize main data
        PersonalFinanceSource personalFinanceSource = personalFinanceSourceList.get(position);
        //Initialize database
        database = RoomDB.getInstance(context);
        //Set text on text view

        holder.txt_view_list_basicTitle.setText(personalFinanceSource.getFinance_source_name());
        int id_cat = personalFinanceSource.getFinance_source_cat_id_pfs();
        holder.txt_view_list_basicSubtitle.setText(database.personalFinanceSourceCategoryDao().obtainPfsCatName(id_cat));
        holder.txt_view_list_basicNote.setText(personalFinanceSource.getNote());

        holder.btn_edit_bsc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PersonalFinanceSource pfs = personalFinanceSourceList.get(holder.getAdapterPosition());

                int pfsID = pfs.getFinance_source_id();
                int pfsUserID = pfs.getUser_id_pfs();
                String pfsName =pfs.getFinance_source_name();
                String pfsNote = pfs.getNote();
                int pfsCatID = pfs.getFinance_source_cat_id_pfs();

                String pfsCatName = database.personalFinanceSourceCategoryDao().obtainPfsCatName(pfsCatID);

                EditText txtPfsName = context.findViewById(R.id.edt_txt_pfs_name);
                txtPfsName.setText(pfsName);
                EditText txtPfsNote = context.findViewById(R.id.edt_txt_pfs_note);
                txtPfsNote.setText(pfsNote);

                RadioButton pfsCash = context.findViewById(R.id.rdo_pfs_cash);
                RadioButton pfsDbtCard = context.findViewById(R.id.rdo_pfs_dbt_card);
                RadioButton pfsMealTkt = context.findViewById(R.id.rdo_pfs_meal_tkt);
                RadioButton pfsCrdCard = context.findViewById(R.id.rdo_pfs_crd_card);
                RadioButton pfsOthers = context.findViewById(R.id.rdo_pfs_others);

                if(pfsCatName.equals("Portofel"))
                    pfsCash.setChecked(true);
                else if(pfsCatName.equals("Card de debit"))
                    pfsDbtCard.setChecked(true);
                else if(pfsCatName.equals("Tichete de masă"))
                    pfsMealTkt.setChecked(true);
                else if(pfsCatName.equals("Card de credit"))
                    pfsCrdCard.setChecked(true);
                else if(pfsCatName.equals("Altele"))
                    pfsOthers.setChecked(true);

                Button btn_update_pfs = context.findViewById(R.id.btn_update_pfs);
                Button btn_add_pfs = context.findViewById(R.id.btn_add_pfs);
                btn_add_pfs.setVisibility(View.GONE);
                btn_update_pfs.setVisibility(View.VISIBLE);

                btn_update_pfs.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String sPfsName = txtPfsName.getText().toString();
                        String sPfsNote = txtPfsNote.getText().toString();

                        RadioGroup radio_grp_pfs_cat =context.findViewById(R.id.radio_grp_pfs_cat);
                        int radioIDpfs = radio_grp_pfs_cat.getCheckedRadioButtonId();
                        String sPfsCat = "";
                        int intPfsCat = 0;
                        try {
                            RadioButton radio_gen = context.findViewById(radioIDpfs);
                            sPfsCat = radio_gen.getText().toString();
                            if(sPfsCat.equals("Portofel"))
                                intPfsCat=1;
                            else if(sPfsCat.equals("Card de debit"))
                                intPfsCat=2;
                            else if(sPfsCat.equals("Tichete de masă"))
                                intPfsCat=3;
                            else if(sPfsCat.equals("Card de credit"))
                                intPfsCat=4;
                            else if(sPfsCat.equals("Altele"))
                                intPfsCat=5;
                        }catch (Exception e) {
                            e.printStackTrace();
                        }

                        database.personalFinanceSourceDao().updatePFS(sPfsName, sPfsNote,intPfsCat,pfsUserID,pfsID);
                        personalFinanceSourceList.clear();
                        //data:
                        List<PersonalFinanceSource> upPFS=database.personalFinanceSourceDao().getAllPersonalFinanceSources(pfsUserID);
                        personalFinanceSourceList.addAll(upPFS);
                        notifyDataSetChanged();
                    }
                });

            }
        });


        holder.btn_delete_bsc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Initialize main data
               PersonalFinanceSource pfs = personalFinanceSourceList.get(holder.getAdapterPosition());

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
                        database.personalFinanceSourceDao().deletePersonalFinanceSource(pfs);
                        //Notify when data is deleted
                        int position = holder.getAdapterPosition();
                        personalFinanceSourceList.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, personalFinanceSourceList.size());
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
        return personalFinanceSourceList.size();
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
