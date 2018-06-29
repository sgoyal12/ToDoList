package com.example.shubham.todolist;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class desc extends AppCompatActivity {
TextView tvname,tvdesc,tvdate;
Bundle bundle;
public final static int EDIT_REQUEST_CODE=2,DESC_RESULT_CODE=3;

//Button back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_desc);
        tvname=findViewById(R.id.nametv1);
        tvdesc=findViewById(R.id.desctv1);
        tvdate=findViewById(R.id.datetv1);
//        back=findViewById(R.id.back);
        Intent intent=getIntent();
        bundle=intent.getExtras();
        setTAD();
//        back.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i=new Intent(desc.this,MainActivity.class);
//                startActivity(i);
//            }
//        });
    }

    private void setTAD() {
        String name=bundle.getString(MainActivity.TITLE);
        String desc=bundle.getString(MainActivity.DESC);
        String date=bundle.getString(MainActivity.DATE);
        tvname.setText(name);
        tvdesc.setText(desc);
        tvdate.setText(date);
        Intent data=new Intent();
        data.putExtras(bundle);
        setResult(DESC_RESULT_CODE,data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu2,menu);
        return  true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        long id=item.getItemId();
        if(id==R.id.edit);
        {
            Intent i=new Intent(this,Edit.class);
            i.putExtras(bundle);
            startActivityForResult(i,EDIT_REQUEST_CODE);
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data!=null)
        {
            if(resultCode==Edit.EDIT_RESULT_CODE)
            {
               bundle=data.getExtras();
               setTAD();
            }
        }
    }
}
