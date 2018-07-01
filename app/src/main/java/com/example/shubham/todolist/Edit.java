package com.example.shubham.todolist;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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

public class Edit extends AppCompatActivity {
EditText et1,et2;
TextView et3,tv4;Button btnsave;
Bundle bundle;
public final static int EDIT_RESULT_CODE=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        et1=findViewById(R.id.gettitle);
        et2=findViewById(R.id.getdesc);
        et3=findViewById(R.id.getdate);
        tv4=findViewById(R.id.gettime);
        btnsave=findViewById(R.id.save);
        Intent intent=getIntent(),data=new Intent();
        bundle=intent.getExtras();
        data.putExtras(bundle);
        setResult(EDIT_RESULT_CODE,data);
        Long id=bundle.getLong(MainActivity.ID);
        String[] arrray={""+id};
        ToDoOpenHelper openHelper=ToDoOpenHelper.getOpenHelper(this);
        SQLiteDatabase db=openHelper.getReadableDatabase();
        Cursor cursor=db.query(Contract.todo.Todo_TABLE_NAME,null,Contract.todo.Todo_COLOUMN_ID+"=?",arrray,null,null,null);
        cursor.moveToNext();
        et1.setText(cursor.getString(cursor.getColumnIndex(Contract.todo.Todo_COLOUMN_NAME)));
        et2.setText(cursor.getString(cursor.getColumnIndex(Contract.todo.Todo_COLOUMN_DESCRIPTION)));
        et3.setText(cursor.getString(cursor.getColumnIndex(Contract.todo.Todo_COLOUMN_DATE)));
        tv4.setText(cursor.getString(cursor.getColumnIndex(Contract.todo.Todo_COLOUMN_TIME)));
        cursor.close();
        final Calendar c=Calendar.getInstance();
        et3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dpd = new DatePickerDialog(Edit.this, new DatePickerDialog.OnDateSetListener() {

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
                int mhours=c.get(Calendar.HOUR_OF_DAY);
                int mmin=c.get(Calendar.MINUTE);
                TimePickerDialog tpd=new TimePickerDialog(Edit.this, new TimePickerDialog.OnTimeSetListener() {
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
                String title,descr,date,time;
                if(et1.getText().toString()!=""&&et2.getText().toString()!=""&&et3.getText().toString()!=""&&tv4.getText().toString()!=""){
                    title=et1.getText().toString();
                    descr=et2.getText().toString();
                    date=et3.getText().toString();
                    time=tv4.getText().toString();
                    ToDoOpenHelper openHelper=ToDoOpenHelper.getOpenHelper(Edit.this);
                    SQLiteDatabase database=openHelper.getWritableDatabase();
                    ContentValues contentValues=new ContentValues();
                    String[] array={""+bundle.getLong(MainActivity.ID)};
                    contentValues.put(Contract.todo.Todo_COLOUMN_NAME,title);
                    contentValues.put(Contract.todo.Todo_COLOUMN_DESCRIPTION,descr);
                    contentValues.put(Contract.todo.Todo_COLOUMN_DATE,date);
                    contentValues.put(Contract.todo.Todo_COLOUMN_TIME,time);
                    database.update(Contract.todo.Todo_TABLE_NAME,contentValues,Contract.todo.Todo_COLOUMN_ID+" = ?",array);
                    finish();
                }
                else
                {
                    Toast.makeText(Edit.this,"Fields Can't Be Empty",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
