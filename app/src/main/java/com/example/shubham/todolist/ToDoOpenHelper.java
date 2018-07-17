package com.example.shubham.todolist;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by shubham on 6/29/2018.
 */

public class ToDoOpenHelper extends SQLiteOpenHelper {
    public static  final  String DATABASE_NAME="todo_db";
    public final static  int VERSION=1;
    public  static  ToDoOpenHelper openHelper;
    private ToDoOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }
    public static ToDoOpenHelper getOpenHelper(Context context)
    {
        if(openHelper==null){
           openHelper=new ToDoOpenHelper(context.getApplicationContext());
        }
        return openHelper;
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQLquery="CREATE TABLE "+Contract.todo.Todo_TABLE_NAME+
                " ("+Contract.todo.Todo_COLOUMN_ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"
                +Contract.todo.Todo_COLOUMN_NAME+" TEXT,"
                +Contract.todo.Todo_COLOUMN_DESCRIPTION+" TEXT,"
                +Contract.todo.Todo_COLOUMN_DATE + " Text,"
                +Contract.todo.Todo_COLOUMN_TIME+" Text,"
                +Contract.todo.Todo_COLOUMN_TIMEINMILLIES+" INTEGER)";
        db.execSQL(SQLquery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
