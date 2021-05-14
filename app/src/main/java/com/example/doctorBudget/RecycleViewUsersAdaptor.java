package com.example.doctorBudget;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doctorBudget.Entities.User;
import com.example.doctorBudget.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class RecycleViewUsersAdaptor extends RecyclerView.Adapter<RecycleViewUsersAdaptor.ViewHolder> {

    //Initialize variable
    private List<User> userList;
    private Activity context;
    private RoomDB database;

    private RecyclerViewClickListener listener;

    public RecycleViewUsersAdaptor(Activity context,List<User> userList, RecyclerViewClickListener listener) {
        this.context = context;
        this.userList = userList;
        this.listener = listener;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecycleViewUsersAdaptor.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_row_user, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecycleViewUsersAdaptor.ViewHolder holder, int position) {
        //Initialize main data
        User user = userList.get(position);
        //Initialize database
        database = RoomDB.getInstance(context);
        //Set text on text view

        holder.txt_view_last_name.setText(user.getLastName());
        holder.txt_view_first_name.setText(user.getFirstName());
        try {
            Date birthDay = user.getDate_of_birth();
            DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            String sBirthDay = dateFormat.format(birthDay);
            holder.txt_view_birthDay.setText(sBirthDay);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try{
            holder.img_user_profile.setImageBitmap(user.getProfilePicture());
        } catch (Exception e) {
            e.printStackTrace();
        }


        holder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Initialize main data
                User u = userList.get(holder.getAdapterPosition());
                // Get id
                int uID = u.getUserID();
                // Get LastName
                String uLastName = u.getLastName();
                // Get FirstName
                String uFirstName = u.getFirstName();
                String uEmail = u.getEmail();
                String uOccupation = u.getOccupation();

                //Create dialog
                Dialog dialog = new Dialog(context);
                // Set content view
                dialog.setContentView(R.layout.dialog_update_user);
                //Initialize width
                int width = WindowManager.LayoutParams.MATCH_PARENT;
                //Initialize height
                int height = WindowManager.LayoutParams.WRAP_CONTENT;
                //Set layout
                dialog.getWindow().setLayout(width, height);
                //Show dialog
                dialog.show();
                //Initialize and assign variable

                EditText edt_txt_last_name_upd = dialog.findViewById(R.id.edt_txt_last_name_upd);
                EditText edt_txt_first_name_upd = dialog.findViewById(R.id.edt_txt_first_name_upd);
                EditText edt_txt_email_upd = dialog.findViewById(R.id.edt_txt_email_upd);
                EditText edt_txt_occupation_upd = dialog.findViewById(R.id.edt_txt_occupation_upd);

                Button btnUpdate = dialog.findViewById(R.id.btn_update);
                Button btnCancelUpdate = dialog.findViewById(R.id.btn_cancel_update);

                //Set text on edit text
                edt_txt_last_name_upd.setText(uLastName);
                edt_txt_first_name_upd.setText(uFirstName);
                edt_txt_email_upd.setText(uEmail);
                edt_txt_occupation_upd.setText(uOccupation);

                btnUpdate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Dismiss dialog
                        dialog.dismiss();
                        //Get updated text from edit text
                        String up_txt_last_name = edt_txt_last_name_upd.getText().toString();
                        String up_txt_first_name = edt_txt_first_name_upd.getText().toString();
                        String up_txt_email = edt_txt_email_upd.getText().toString();
                        String up_txt_occupation = edt_txt_occupation_upd.getText().toString();
                        //Update test in database
                        database.userDao().updateUser(uID, up_txt_last_name, up_txt_first_name,up_txt_email,up_txt_occupation);
                        //Notify when data is updated
                        userList.clear();
                        //data:
                        userList.addAll(database.userDao().getAllUsers());
                       notifyDataSetChanged();
                    }
                });

                btnCancelUpdate.setOnClickListener((new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                }));
            }
        });

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Initialize main data
                User u = userList.get(holder.getAdapterPosition());

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
                        database.userDao().deleteUser(u);
                        //Notify when data is deleted
                        int position = holder.getAdapterPosition();
                        userList.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, userList.size());
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
        return userList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        //Initialize variable
        TextView txt_view_last_name, txt_view_first_name, txt_view_birthDay;
        ImageView btnEdit, btnDelete, img_user_profile;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txt_view_birthDay = itemView.findViewById(R.id.txt_view_birthDay);
            txt_view_last_name = itemView.findViewById(R.id.txt_view_last_name);
            txt_view_first_name = itemView.findViewById(R.id.txt_view_first_name);
            btnEdit = itemView.findViewById(R.id.btn_edit);
            btnDelete = itemView.findViewById(R.id.btn_delete);
            img_user_profile = itemView.findViewById(R.id.img_user_profile);
            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View view) {
            listener.onClick(view, getAdapterPosition());
        }
    }

    public interface  RecyclerViewClickListener {
        void onClick(View v, int position);
    }



}
