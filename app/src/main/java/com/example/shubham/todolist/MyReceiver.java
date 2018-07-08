package com.example.shubham.todolist;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import static android.provider.Telephony.Sms.Intents.getMessagesFromIntent;

/**
 * Created by shubham on 7/3/2018.
 */

public class MyReceiver extends BroadcastReceiver {
boolean readOn;
    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences sharedPreferences=context.getSharedPreferences("SharedPreference",Context.MODE_PRIVATE);
        readOn=sharedPreferences.getBoolean(MainActivity.READ_ON,false);
       if(readOn)
       {
           SmsMessage[] ar=getMessagesFromIntent(intent);
           for (int i = 0; i < ar.length; i++) {
               SmsMessage currentMessage = ar[i];
               String message = currentMessage.getDisplayMessageBody();
               String number=currentMessage.getDisplayOriginatingAddress();
               NotificationManager manager= (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
               if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O) { //check android build level
                   NotificationChannel channel = new NotificationChannel("MyChannel", "todoChannel", NotificationManager.IMPORTANCE_HIGH);//creating a new notification channel for api greater than 26
                   manager.createNotificationChannel(channel);// actually creating a notification channel in manager
               }
               NotificationCompat.Builder builder=new NotificationCompat.Builder(context,"MyChannel");//creating a notification builder with a context and channel
               builder.setContentTitle("Create Todo");//set title
               builder.setContentText("Message Received from"+number);//set text
               Intent intent2= new Intent(context,MainActivity.class);
               intent2.putExtra(Intent.EXTRA_TEXT,message);
               intent2.putExtra(MainActivity.NUMBER,number);//creating intent to be performed when notification is clicked
               PendingIntent pendingIntent= PendingIntent.getActivity(context,1,intent2,0);//creating a pending intent for given intent
               builder.setContentIntent(pendingIntent);//set the pending content to  notification
               builder.setSmallIcon(R.drawable.ic_launcher_foreground);//set the icon of notification
               Notification notification=builder.build();//actually building the notification
               manager.notify(1,notification);//showing the notification
           }
       }
    }
}
