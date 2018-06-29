package com.example.shubham.todolist;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Calendar;

public class add extends AppCompatActivity {
    EditText et1,et2;
    TextView et3;
    Button btnsave;
    final static int ADD_RESULT_CODE=7;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        et1=findViewById(R.id.gettitle);
        et2=findViewById(R.id.getdesc);
        et3=findViewById(R.id.getdate);
        btnsave=findViewById(R.id.saveitem);
        final Calendar c = Calendar.getInstance();
       et3.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               int mYear = c.get(Calendar.YEAR);
               int mMonth = c.get(Calendar.MONTH);
               int mDay = c.get(Calendar.DAY_OF_MONTH);
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

        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle=new Bundle();
                bundle.putString(MainActivity.TITLE,et1.getText().toString());
                bundle.putString(MainActivity.DESC,et2.getText().toString());
                bundle.putString(MainActivity.DATE,et3.getText().toString());
                Intent data=new Intent();
                data.putExtras(bundle);
                setResult(ADD_RESULT_CODE,data);
                finish();
            }
        });
    }
}
