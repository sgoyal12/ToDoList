package com.example.shubham.todolist;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Calendar;

public class Edit extends AppCompatActivity {
EditText et1,et2;
TextView et3;Button btnsave;
Bundle bundle;
public final static int EDIT_RESULT_CODE=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        et1=findViewById(R.id.gettitle);
        et2=findViewById(R.id.getdesc);
        et3=findViewById(R.id.getdate);
        btnsave=findViewById(R.id.save);
        Intent intent=getIntent();
        bundle=intent.getExtras();
        et1.setText(bundle.getString(MainActivity.TITLE));
        et2.setText(bundle.getString(MainActivity.DESC));
        et3.setText(bundle.getString(MainActivity.DATE));
        et3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c=Calendar.getInstance();
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
        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title,descr,date;
                if(et1.getText().toString()!=null||et2.getText().toString()!=null||et3.getText().toString()!=null){
                title=et1.getText().toString();
                descr=et2.getText().toString();
                date=et3.getText().toString();
                    Intent data=new Intent();
                    bundle.putString(MainActivity.TITLE,title);
                    bundle.putString(MainActivity.DESC,descr);
                    bundle.putString(MainActivity.DATE,date);
                    data.putExtras(bundle);
                    setResult(EDIT_RESULT_CODE,data);
                    finish();
                }
            }
        });
    }
}
