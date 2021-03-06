package com.example.shubham.todolist;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.telephony.SmsMessage;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.Toolbar;


import java.util.ArrayList;
import java.util.Calendar;

import static android.provider.Telephony.Sms.Intents.getMessagesFromIntent;

public class MainActivity extends AppCompatActivity implements TodoListener {
    RecyclerView rv;
    ArrayList<ToDoItem> toDoItems=new ArrayList<>();

    public final static String ID="ItemID",POS="position",NUMBER="number",READ_ON="readon";
    public final static  int DESC_REQUEST_CODE=1,ADD_REQUEST_CODE=6;
    public static boolean readOn=false,perm=false;
    SharedPreferences sharedPreferences;
    ToDoAdapter adapter;
    LayoutInflater inflater;
    MenuItem item;
    ToDoItem deleted=null;
    int deletedPosition=0;
    Bundle bundle=new Bundle();
    FloatingActionButton fab;
//    MyReceiver receiver;

    CoordinatorLayout linear;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent=getIntent();
        sharedPreferences=getSharedPreferences("SharedPreference",MODE_PRIVATE);
        readOn=sharedPreferences.getBoolean(READ_ON,false);
        if((checkSelfPermission(Manifest.permission.READ_SMS)== PackageManager.PERMISSION_GRANTED)&&(checkSelfPermission(Manifest.permission.RECEIVE_SMS)== PackageManager.PERMISSION_GRANTED))
            perm=true;
        rv=findViewById(R.id.recyclerView);

        inflater= (LayoutInflater) this.getSystemService(this.LAYOUT_INFLATER_SERVICE);
        linear=findViewById(R.id.linear);
        addMessage(intent);
        ToDoOpenHelper openHelper=ToDoOpenHelper.getOpenHelper(this);
        SQLiteDatabase database=openHelper.getReadableDatabase();
        Cursor cursor=database.query(Contract.todo.Todo_TABLE_NAME,null,null,null,null,null,null);
        while (cursor.moveToNext())
        {
            String name=cursor.getString(cursor.getColumnIndex(Contract.todo.Todo_COLOUMN_NAME));
            String description=cursor.getString(cursor.getColumnIndex(Contract.todo.Todo_COLOUMN_DESCRIPTION));
            String date=cursor.getString(cursor.getColumnIndex(Contract.todo.Todo_COLOUMN_DATE));
            String time=cursor.getString(cursor.getColumnIndex(Contract.todo.Todo_COLOUMN_TIME));
            Long id=cursor.getLong(cursor.getColumnIndex(Contract.todo.Todo_COLOUMN_ID));
            ToDoItem toDoItem =new ToDoItem(name,description,date, time);
            toDoItem.setId(id);
            toDoItems.add(toDoItem);
        }
        cursor.close();

        adapter=new ToDoAdapter(this,toDoItems,this);

        rv.setAdapter(adapter);
        rv.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        rv.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        rv.setItemAnimator(new DefaultItemAnimator());
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP|ItemTouchHelper.DOWN,ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder fromVH, @NonNull RecyclerView.ViewHolder toVH) {
                int from = fromVH.getAdapterPosition();
                int to = toVH.getAdapterPosition();

                ToDoItem toDoItem = toDoItems.get(from);
                toDoItems.remove(from);
                toDoItems.add(to,toDoItem);

                adapter.notifyItemMoved(from,to);
                return true;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                final int position=viewHolder.getAdapterPosition();
                final ToDoItem toDoItem=toDoItems.get(position);
                final String[] array={""+toDoItem.getId()};
                ToDoOpenHelper openHelper=ToDoOpenHelper.getOpenHelper(MainActivity.this);
                final SQLiteDatabase database=openHelper.getWritableDatabase();
                final AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Confirm Delete");
                builder.setMessage("Do You Really Want To Delete");
                builder.setCancelable(false);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Bundle bundle=new Bundle();
                        database.delete(Contract.todo.Todo_TABLE_NAME,Contract.todo.Todo_COLOUMN_ID+"=?",array);
                        Intent intent1 = new Intent(getApplicationContext(),desc.class),intent=new Intent(getApplicationContext(),MyReceiver2.class);
                        bundle.putLong(MainActivity.ID,toDoItems.get(position).getId());
                        int a=(int) toDoItems.get(position).getId();
                        intent1.putExtras(bundle);
                        intent.putExtras(bundle);
                        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(),2,intent1,PendingIntent.FLAG_UPDATE_CURRENT)
                                ,pendingIntent1=PendingIntent.getBroadcast(getApplicationContext(),a,intent1,0);
                        AlarmManager alarmManager= (AlarmManager) getSystemService(ALARM_SERVICE);
                        long timeInMillies=toDoItems.get(position).getTimeInMillies();
                        alarmManager.set(AlarmManager.RTC_WAKEUP, timeInMillies,pendingIntent1);
                        alarmManager.cancel(pendingIntent1);
                        pendingIntent.cancel();
                        deleted=toDoItems.get(position);
                        toDoItems.remove(position);
                        adapter.notifyItemRemoved(position);
                        Snackbar.make(linear,"Todo deleted",Snackbar.LENGTH_LONG).setAction("Undo", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Bundle bundle1=new Bundle();
                                ToDoOpenHelper openHelper=ToDoOpenHelper.getOpenHelper(MainActivity.this);
                                SQLiteDatabase database=openHelper.getWritableDatabase();
                                ContentValues contentValues=new ContentValues();
                                contentValues.put(Contract.todo.Todo_COLOUMN_NAME,deleted.getName());
                                contentValues.put(Contract.todo.Todo_COLOUMN_DESCRIPTION,deleted.getDescription());
                                contentValues.put(Contract.todo.Todo_COLOUMN_DATE,deleted.getDate());
                                contentValues.put(Contract.todo.Todo_COLOUMN_TIME,deleted.getTime());
                                long timeInMillies=deleted.getTimeInMillies();
                                contentValues.put(Contract.todo.Todo_COLOUMN_TIMEINMILLIES,timeInMillies);
                                long id=database.insert(Contract.todo.Todo_TABLE_NAME,null,contentValues);
                                deleted.setId(id);
                                if(timeInMillies>=System.currentTimeMillis())
                                {
                                    int a=(int) id;
                                    Intent intent1=new Intent(getApplicationContext(),MyReceiver2.class);
                                    bundle1.putLong(MainActivity.ID,id);
                                    intent1.putExtras(bundle1);
                                    PendingIntent pendingIntent= PendingIntent.getBroadcast(getApplicationContext(),a,intent1,0);
                                    AlarmManager alarmManager= (AlarmManager) getSystemService(ALARM_SERVICE);
                                    alarmManager.set(AlarmManager.RTC_WAKEUP, timeInMillies,pendingIntent);
                                }
                                toDoItems.add(position,deleted);
                                adapter.notifyItemInserted(position);

                            }
                        }).show();


                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                AlertDialog dialog=builder.create();
                dialog.show();

            }
        });

        itemTouchHelper.attachToRecyclerView(rv);
        fab=findViewById(R.id.Fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent add=new Intent(MainActivity.this,add.class);
                add.putExtra(Intent.EXTRA_TEXT,"");
                startActivityForResult(add,ADD_REQUEST_CODE);
            }
        });
    }

    private void addMessage(Intent intent) {
        String message,number;
        message=intent.getStringExtra(Intent.EXTRA_TEXT);

        number=intent.getStringExtra(NUMBER);
        if(message!=null&&number!=null)
        {

            final Calendar c = Calendar.getInstance();
            final int mYear = c.get(Calendar.YEAR);
            final int mMonth = c.get(Calendar.MONTH);
            final int mDay = c.get(Calendar.DAY_OF_MONTH);
            final int mhours=c.get(Calendar.HOUR_OF_DAY);
            final int mmin=c.get(Calendar.MINUTE);
            ToDoOpenHelper openHelper=ToDoOpenHelper.getOpenHelper(this);
            SQLiteDatabase database=openHelper.getWritableDatabase();
            ContentValues contentValues=new ContentValues();
            contentValues.put(Contract.todo.Todo_COLOUMN_NAME,number);
            contentValues.put(Contract.todo.Todo_COLOUMN_DESCRIPTION,message);
            contentValues.put(Contract.todo.Todo_COLOUMN_DATE,String.format("%d/%d/%d",mDay,mMonth+1,mYear));
            contentValues.put(Contract.todo.Todo_COLOUMN_TIME,String.format("%d:%d",mhours,mmin));
            long timeInMillies=System.currentTimeMillis();
            contentValues.put(Contract.todo.Todo_COLOUMN_TIMEINMILLIES,timeInMillies);
            long id=database.insert(Contract.todo.Todo_TABLE_NAME,null,contentValues);
            bundle.putLong(ID,id);
            int a=(int) id;
            Intent intent1=new Intent(getApplicationContext(),MyReceiver2.class);
            intent1.putExtras(bundle);
            PendingIntent pendingIntent= PendingIntent.getBroadcast(getApplicationContext(),a,intent1,0);
            AlarmManager alarmManager= (AlarmManager) getSystemService(ALARM_SERVICE);
            alarmManager.set(AlarmManager.RTC_WAKEUP, timeInMillies,pendingIntent);

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        item=menu.findItem(R.id.makeSmsTodo);
        if(readOn)
            item.setChecked(true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        if(id==R.id.orderbytitle)
            {
            orderbytitle();

        }
        else if(id==R.id.orderbydesc)
        {
            orderByDesc();
        }
        else if(id==R.id.orderbydate)
        {
            orderByDate();
        }
        else if(id==R.id.makeSmsTodo)
        {
            if( item.isChecked())
            {
                item.setChecked(false);
//                this.unregisterReceiver(this.receiver);
                readOn=false;
            }
            else
            {
                if((checkSelfPermission(Manifest.permission.READ_SMS)!= PackageManager.PERMISSION_GRANTED)||(checkSelfPermission(Manifest.permission.RECEIVE_SMS)!= PackageManager.PERMISSION_GRANTED))
                {
                    String[] permission={Manifest.permission.READ_SMS,Manifest.permission.RECEIVE_SMS};
                    ActivityCompat.requestPermissions(this,permission,1);
                }
                if(perm==true)
                {
                    item.setChecked(true);
                    readOn=true;
                }

//                receiver=new MyReceiver() {
//                    @Override
//                    public void onReceive(Context context, Intent intent) {
//                        SmsMessage[] ar=getMessagesFromIntent(intent);
//                        for (int i = 0; i < ar.length; i++) {
//
//                            SmsMessage currentMessage = ar[i];
//                            String message = currentMessage.getDisplayMessageBody();
//                            Intent intent1=new Intent(MainActivity.this,add.class);
//                            intent1.putExtra(Intent.EXTRA_TEXT,message);
//                            startActivityForResult(intent1,ADD_REQUEST_CODE);
//                        }
//                    }
//                };
//                IntentFilter intentFilter = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
//                intentFilter.setPriority(999);
//                this.registerReceiver(receiver, intentFilter);
            }
            SharedPreferences.Editor editor= sharedPreferences.edit();
            editor.putBoolean(READ_ON,readOn);
            editor.commit();
        }
        else if(id==R.id.Aboutus)
        {
            Intent intent =new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            Uri uri=Uri.parse("https://codingninjas.in/");
            intent.setData(uri);
            startActivity(intent);
        }
        else if(id==R.id.feedback)
        {
            Intent intent =new Intent();
            intent.setAction(Intent.ACTION_SENDTO);
            Uri uri=Uri.parse("mailto:shu12.sg@gmail.com");
            intent.setData(uri);
            startActivity(intent);
        }
        return true;
    }

    private void orderByDate() {
        ToDoOpenHelper openHelper=ToDoOpenHelper.getOpenHelper(this);
        SQLiteDatabase database=openHelper.getReadableDatabase();
        Cursor cursor=database.query(Contract.todo.Todo_TABLE_NAME,null,null,null,null,null,Contract.todo.Todo_COLOUMN_TIMEINMILLIES);
        toDoItems.clear();
        while (cursor.moveToNext())
        {
            String name=cursor.getString(cursor.getColumnIndex(Contract.todo.Todo_COLOUMN_NAME));
            String description=cursor.getString(cursor.getColumnIndex(Contract.todo.Todo_COLOUMN_DESCRIPTION));
            String date=cursor.getString(cursor.getColumnIndex(Contract.todo.Todo_COLOUMN_DATE));
            String time=cursor.getString(cursor.getColumnIndex(Contract.todo.Todo_COLOUMN_TIME));
            Long id=cursor.getLong(cursor.getColumnIndex(Contract.todo.Todo_COLOUMN_ID));
            ToDoItem toDoItem =new ToDoItem(name,description,date, time);
            toDoItem.setId(id);
            toDoItems.add(toDoItem);
        }
        cursor.close();
        adapter.notifyDataSetChanged();
    }

    private void orderByDesc() {
        ToDoOpenHelper openHelper=ToDoOpenHelper.getOpenHelper(this);
        SQLiteDatabase database=openHelper.getReadableDatabase();
        Cursor cursor=database.query(Contract.todo.Todo_TABLE_NAME,null,null,null,null,null,Contract.todo.Todo_COLOUMN_DESCRIPTION);
        toDoItems.clear();
        while (cursor.moveToNext())
        {
            String name=cursor.getString(cursor.getColumnIndex(Contract.todo.Todo_COLOUMN_NAME));
            String description=cursor.getString(cursor.getColumnIndex(Contract.todo.Todo_COLOUMN_DESCRIPTION));
            String date=cursor.getString(cursor.getColumnIndex(Contract.todo.Todo_COLOUMN_DATE));
            String time=cursor.getString(cursor.getColumnIndex(Contract.todo.Todo_COLOUMN_TIME));
            Long id=cursor.getLong(cursor.getColumnIndex(Contract.todo.Todo_COLOUMN_ID));
            ToDoItem toDoItem =new ToDoItem(name,description,date, time);
            toDoItem.setId(id);
            toDoItems.add(toDoItem);
        }
        cursor.close();
        adapter.notifyDataSetChanged();
    }

    private void orderbytitle() {
        ToDoOpenHelper openHelper=ToDoOpenHelper.getOpenHelper(this);
        SQLiteDatabase database=openHelper.getReadableDatabase();
        Cursor cursor=database.query(Contract.todo.Todo_TABLE_NAME,null,null,null,null,null,Contract.todo.Todo_COLOUMN_NAME);
        toDoItems.clear();
        while (cursor.moveToNext())
        {
            String name=cursor.getString(cursor.getColumnIndex(Contract.todo.Todo_COLOUMN_NAME));
            String description=cursor.getString(cursor.getColumnIndex(Contract.todo.Todo_COLOUMN_DESCRIPTION));
            String date=cursor.getString(cursor.getColumnIndex(Contract.todo.Todo_COLOUMN_DATE));
            String time=cursor.getString(cursor.getColumnIndex(Contract.todo.Todo_COLOUMN_TIME));
            Long id=cursor.getLong(cursor.getColumnIndex(Contract.todo.Todo_COLOUMN_ID));
            ToDoItem toDoItem =new ToDoItem(name,description,date, time);
            toDoItem.setId(id);
            toDoItems.add(toDoItem);
        }
        cursor.close();
        adapter.notifyDataSetChanged();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data!=null)
        {
        ToDoOpenHelper openHelper=ToDoOpenHelper.getOpenHelper(this);
        SQLiteDatabase database=openHelper.getReadableDatabase();
        bundle=data.getExtras();
        Long id=bundle.getLong(ID);
        String []array={""+id};
        Cursor cursor=database.query(Contract.todo.Todo_TABLE_NAME,null,Contract.todo.Todo_COLOUMN_ID+"=?",array,null,null,null);
            cursor.moveToNext();
        String title=cursor.getString(cursor.getColumnIndex(Contract.todo.Todo_COLOUMN_NAME));
        String descr=cursor.getString(cursor.getColumnIndex(Contract.todo.Todo_COLOUMN_DESCRIPTION));
        String date=cursor.getString(cursor.getColumnIndex(Contract.todo.Todo_COLOUMN_DATE));
        String time=cursor.getString(cursor.getColumnIndex(Contract.todo.Todo_COLOUMN_TIME));
        cursor.close();
        ToDoItem toDoItem= new ToDoItem(title,descr,date, time);
        toDoItem.setId(bundle.getLong(ID));
        if(resultCode==desc.DESC_RESULT_CODE)
            {
                toDoItems.set(bundle.getInt(POS),toDoItem);
                adapter.notifyItemChanged(bundle.getInt(POS));
            }
            if(resultCode==add.ADD_RESULT_CODE)
            {
                if(id!=-1) {
                    toDoItem.setId(id);
                    toDoItems.add(toDoItem);
                    adapter.notifyItemInserted(toDoItems.size());
                }
            }
    }}

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==1)
        {   int i;
            for (i=0;i<grantResults.length;i++)
            {
                int callGrantResult=grantResults[i];
                if(callGrantResult==PackageManager.PERMISSION_DENIED)
                    break;
            }
            if(i!=grantResults.length)
                perm=false;
            else {
                perm=true;
            }
            if(perm==false)
                 Toast.makeText(this,"Permission Not Granted",Toast.LENGTH_SHORT).show();
            else if(perm)
            {
                readOn=true;
                SharedPreferences.Editor editor= sharedPreferences.edit();
                editor.putBoolean(READ_ON,readOn);
                editor.commit();
                item.setChecked(true);
            }
        }
    }

    @Override
    public void onItemClickListener(View view, int position) {
        ToDoItem item=toDoItems.get(position);
        Intent i=new Intent(MainActivity.this,desc.class);
//        bundle.putString(TITLE,item.getName());
//        bundle.putString(DESC,item.getDescription());
//        bundle.putString(DATE,item.getDate());
        bundle.putLong(ID,item.getId());
        bundle.putInt(POS,position);
        i.putExtras(bundle);
        startActivityForResult(i,DESC_REQUEST_CODE);
    }


}
