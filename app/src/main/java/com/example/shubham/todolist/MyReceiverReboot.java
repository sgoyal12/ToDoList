package com.example.shubham.todolist;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Toast;

public class MyReceiverReboot extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        ToDoOpenHelper openHelper=ToDoOpenHelper.getOpenHelper(context);
        SQLiteDatabase database=openHelper.getReadableDatabase();
        String[] array={Contract.todo.Todo_COLOUMN_TIMEINMILLIES,Contract.todo.Todo_COLOUMN_ID};
        Cursor cursor=database.query(Contract.todo.Todo_TABLE_NAME,array,null,null,null,null,null);
        while (cursor.moveToNext())
        {
            Bundle bundle=new Bundle();
            long timeInMillies=cursor.getLong(cursor.getColumnIndex(Contract.todo.Todo_COLOUMN_TIMEINMILLIES));
            long id=cursor.getLong(cursor.getColumnIndex(Contract.todo.Todo_COLOUMN_ID));
            int a=(int) id;
            bundle.putLong(MainActivity.ID,id);
            Intent intent1=new Intent(context.getApplicationContext(),MyReceiver2.class);
            intent1.putExtras(bundle);
            PendingIntent pendingIntent= PendingIntent.getBroadcast(context.getApplicationContext(),a,intent1,PendingIntent.FLAG_UPDATE_CURRENT);
            AlarmManager alarmManager= (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
            alarmManager.set(AlarmManager.RTC_WAKEUP, timeInMillies,pendingIntent);
        }
        cursor.close();
    }
}
