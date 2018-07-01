package com.example.shubham.todolist;

/**
 * Created by shubham on 6/26/2018.
 */

public class ToDoItem {
    private String name;
    private String description;
    private String date;
    private String time;
    private long id;

    public ToDoItem(String name, String description, String date, String time) {
        this.name = name;
        this.description = description;
        this.date=date;
        this.time = time;
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
