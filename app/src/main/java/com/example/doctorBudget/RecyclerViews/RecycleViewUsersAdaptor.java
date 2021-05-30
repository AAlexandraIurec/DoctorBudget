package com.example.doctorBudget.RecyclerViews;

import android.app.Activity;
import android.app.Dialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doctorBudget.Entities.User;
import com.example.doctorBudget.R;
import com.example.doctorBudget.RoomDB;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class RecycleViewUsersAdaptor extends RecyclerView.Adapter<RecycleViewUsersAdaptor.ViewHolder> {


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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row_user, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecycleViewUsersAdaptor.ViewHolder holder, int position) {

        User user = userList.get(position);
        database = RoomDB.getInstance(context);

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

                User u = userList.get(holder.getAdapterPosition());
                int uID = u.getUserID();
                String uLastName = u.getLastName();
                String uFirstName = u.getFirstName();
                String uEmail = u.getEmail();
                String uOccupation = u.getOccupation();

                Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.dialog_update_user);
                int width = WindowManager.LayoutParams.MATCH_PARENT;
                int height = WindowManager.LayoutParams.WRAP_CONTENT;
                dialog.getWindow().setLayout(width, height);
                dialog.show();

                EditText edt_txt_last_name_upd = dialog.findViewById(R.id.edt_txt_last_name_upd);
                EditText edt_txt_first_name_upd = dialog.findViewById(R.id.edt_txt_first_name_upd);
                EditText edt_txt_email_upd = dialog.findViewById(R.id.edt_txt_email_upd);
                EditText edt_txt_occupation_upd = dialog.findViewById(R.id.edt_txt_occupation_upd);

                Button btnUpdate = dialog.findViewById(R.id.btn_update);
                Button btnCancelUpdate = dialog.findViewById(R.id.btn_cancel_update);

                edt_txt_last_name_upd.setText(uLastName);
                edt_txt_first_name_upd.setText(uFirstName);
                edt_txt_email_upd.setText(uEmail);
                edt_txt_occupation_upd.setText(uOccupation);

                btnUpdate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        String up_txt_last_name = edt_txt_last_name_upd.getText().toString();
                        String up_txt_first_name = edt_txt_first_name_upd.getText().toString();
                        String up_txt_email = edt_txt_email_upd.getText().toString();
                        String up_txt_occupation = edt_txt_occupation_upd.getText().toString();
                        database.userDao().updateUser(uID, up_txt_last_name, up_txt_first_name,up_txt_email,up_txt_occupation);
                        userList.clear();
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
                User u = userList.get(holder.getAdapterPosition());
                Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.dialog_delete);
                int width = WindowManager.LayoutParams.MATCH_PARENT;
                int height = WindowManager.LayoutParams.WRAP_CONTENT;
                dialog.getWindow().setLayout(width, height);
                dialog.show();

                TextView textViewDelete = dialog.findViewById(R.id.delete_view_text);
                Button btnYes = dialog.findViewById(R.id.btn_yes_delete);
                Button btnNo = dialog.findViewById(R.id.btn_no_delete);

                btnYes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        database.userDao().deleteUser(u);
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
