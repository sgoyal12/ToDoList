package com.example.shubham.todolist;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

public class MyReceiver2 extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel("mychannelid","Expenses Channel",NotificationManager.IMPORTANCE_HIGH);
            manager.createNotificationChannel(channel);

        }


        Bundle bundle;

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context,"mychannelid");
        builder.setContentTitle("ToDo Alarm");
        builder.setContentText("Alarm Received");
        builder.setSmallIcon(R.drawable.ic_launcher_foreground);

        Intent intent1 = new Intent(context.getApplicationContext(),desc.class);
        bundle=intent.getExtras();
        intent1.putExtras(bundle);
        PendingIntent pendingIntent = PendingIntent.getActivity(context.getApplicationContext(),2,intent1,PendingIntent.FLAG_UPDATE_CURRENT);

        builder.setContentIntent(pendingIntent);
        Notification notification = builder.build();
        manager.notify(1,notification);
    }
}
