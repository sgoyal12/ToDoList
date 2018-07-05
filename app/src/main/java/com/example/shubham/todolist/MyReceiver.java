package com.example.shubham.todolist;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import static android.provider.Telephony.Sms.Intents.getMessagesFromIntent;

/**
 * Created by shubham on 7/3/2018.
 */

public class MyReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
       if(MainActivity.readOn)
       {
           SmsMessage[] ar=getMessagesFromIntent(intent);
           for (int i = 0; i < ar.length; i++) {

               SmsMessage currentMessage = ar[i];
               String message = currentMessage.getDisplayMessageBody();
               String number=currentMessage.getDisplayOriginatingAddress();
               Intent intent1=new Intent(context,MainActivity.class);
               intent1.putExtra(Intent.EXTRA_TEXT,message);
               intent1.putExtra(MainActivity.NUMBER,number);
               context.startActivity(intent1);
           }
       }
    }
}
