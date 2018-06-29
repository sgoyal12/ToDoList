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
    public ToDoOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQLquery="CREATE TABLE "+Contract.todo.Todo_TABLE_NAME+
                " ("+Contract.todo.Todo_COLOUMN_ID+" INTEGER,"
                +Contract.todo.Todo_COLOUMN_NAME+" TEXT,"
                +Contract.todo.Todo_COLOUMN_DESCRIPTION+" TEXT,"+Contract.todo.Todo_COLOUMN_DATE+" Text)";
        db.execSQL(SQLquery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
