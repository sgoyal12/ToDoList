package com.example.shubham.todolist;

import java.util.Calendar;

/**
 * Created by shubham on 6/26/2018.
 */

public class ToDoItem {
    private String name;
    private String description;
    private String date;
    private String time;
    private long timeInMillies;
    private long id;

    public long getTimeInMillies() {
        return timeInMillies;
    }

    public void setTimeInMillies(String date,String time) {
        String[] dateC=date.split("/");
        String[] timeC=time.split(":");
        int day,month,year,hours,min;
        day=Integer.parseInt(dateC[0]);
        month=Integer.parseInt(dateC[1]);
        year=Integer.parseInt(dateC[2]);
        hours=Integer.parseInt(timeC[0]);
        min=Integer.parseInt(timeC[1]);
        Calendar calendar=Calendar.getInstance();
        calendar.clear();
        calendar.set(year,month-1,day,hours,min);
        long timeInMillies=calendar.getTimeInMillis();
        this.timeInMillies = timeInMillies;
    }

    public ToDoItem(String name, String description, String date, String time) {
        this.name = name;
        this.description = description;
        this.date=date;
        this.time = time;
        setTimeInMillies(date,time);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
