package com.example.doctorBudget;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import static androidx.core.app.NotificationCompat.PRIORITY_DEFAULT;

public class ReminderBroadcast  extends BroadcastReceiver {
    String message;

    @Override
    public void onReceive(Context context, Intent intent) {

        Bundle extras =intent.getExtras();
        if(extras != null) {
           message= extras.getString("message");
        }


        NotificationCompat.Builder expenseNotify= new NotificationCompat.Builder(context, "notifyDoctorBudget")
                .setSmallIcon(R.drawable.ic_notifications)
                .setContentTitle("Notificare Doctor Budget")
                .setContentText(message)
                .setPriority(PRIORITY_DEFAULT);


        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(200, expenseNotify.build());

    }

}
