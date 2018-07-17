package com.example.shubham.todolist;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class add extends AppCompatActivity {
    EditText et1,et2;
    TextView et3,tv4;
    Button btnsave;
    long time;
    final static int ADD_RESULT_CODE=7;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        et1=findViewById(R.id.gettitle);
        et2=findViewById(R.id.getdesc);
        et3=findViewById(R.id.getdate);
        tv4=findViewById(R.id.gettime);
        btnsave=findViewById(R.id.saveitem);
        final Calendar c = Calendar.getInstance();
        final int mYear = c.get(Calendar.YEAR);
        final int mMonth = c.get(Calendar.MONTH);
        final int mDay = c.get(Calendar.DAY_OF_MONTH);
        final int mhours=c.get(Calendar.HOUR_OF_DAY);
        final int mmin=c.get(Calendar.MINUTE);
        time=System.currentTimeMillis();
        Intent intent=getIntent();
        String message=intent.getStringExtra(Intent.EXTRA_TEXT);
        et2.setText(message);
        et3.setText(String.format("%d/%d/%d",mDay,(mMonth + 1),mYear));
        tv4.setText(String.format("%d:%d",mhours,mmin));
        et3.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

               DatePickerDialog dpd = new DatePickerDialog(add.this, new DatePickerDialog.OnDateSetListener() {

                   @Override
                   public void onDateSet(DatePicker view, int year,
                                         int monthOfYear, int dayOfMonth) {
                       // Display Selected date in textbox
                       et3.setText(String.format("%d/%d/%d",dayOfMonth,(monthOfYear + 1),year));

                   }
               }, mYear, mMonth, mDay);
               dpd.show();
           }
       });
       tv4.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

               TimePickerDialog tpd=new TimePickerDialog(add.this, new TimePickerDialog.OnTimeSetListener() {
                   @Override
                   public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                       tv4.setText(String.format("%d:%d",hourOfDay,minute));
                   }
               },mhours,mmin,false);
               tpd.show();
           }
       });

        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!(et1.getText().toString().equals("")||et2.getText().toString().equals("")))
                {

                    Bundle bundle=new Bundle();
                    String date,time;
                    date=et3.getText().toString();
                    time =tv4.getText().toString();
                    ToDoOpenHelper openHelper=ToDoOpenHelper.getOpenHelper(add.this);
                    SQLiteDatabase database=openHelper.getWritableDatabase();
                    ContentValues contentValues=new ContentValues();
                    contentValues.put(Contract.todo.Todo_COLOUMN_NAME,et1.getText().toString());
                    contentValues.put(Contract.todo.Todo_COLOUMN_DESCRIPTION,et2.getText().toString());
                    contentValues.put(Contract.todo.Todo_COLOUMN_DATE,date);
                    contentValues.put(Contract.todo.Todo_COLOUMN_TIME,time);
                    ToDoItem toDoItem=new ToDoItem(et1.getText().toString(),et2.getText().toString(),date,time);
                    long timeInMillies=toDoItem.getTimeInMillies();
                    contentValues.put(Contract.todo.Todo_COLOUMN_TIMEINMILLIES,timeInMillies);
                    long id=database.insert(Contract.todo.Todo_TABLE_NAME,null,contentValues);
                    int a=(int) id;
                    bundle.putLong(MainActivity.ID,id);
                    Intent intent1=new Intent(getApplicationContext(),MyReceiver2.class);
                    intent1.putExtras(bundle);
                    PendingIntent pendingIntent= PendingIntent.getBroadcast(getApplicationContext(),a,intent1,0);
                    AlarmManager alarmManager= (AlarmManager) getSystemService(ALARM_SERVICE);
                    alarmManager.set(AlarmManager.RTC_WAKEUP, timeInMillies,pendingIntent);
                    Intent data=new Intent();
                    data.putExtras(bundle);
                    setResult(ADD_RESULT_CODE,data);
                    finish();
                }
                else
                {
                    Toast.makeText(add.this,"Fields Can't Be Empty",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
