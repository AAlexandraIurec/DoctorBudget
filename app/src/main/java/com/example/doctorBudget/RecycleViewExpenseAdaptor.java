package com.example.doctorBudget;

import android.app.Activity;
import android.app.Dialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doctorBudget.Entities.Expense;
import com.example.doctorBudget.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class RecycleViewExpenseAdaptor extends RecyclerView.Adapter<RecycleViewExpenseAdaptor.ViewHolder> {
    private List<Expense> expenseList;
    private Activity context;
    private RoomDB database;
    

    public RecycleViewExpenseAdaptor(Activity context,List<Expense> expenseList) {
        this.context = context;
        this.expenseList = expenseList;

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
