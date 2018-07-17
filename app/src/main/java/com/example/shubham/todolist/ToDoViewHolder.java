package com.example.shubham.todolist;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/**
 * Created by shubham on 7/1/2018.
 */

public class ToDoViewHolder extends RecyclerView.ViewHolder{
    public TextView title,descr,date,time;
    public View itemView;
    public ToDoViewHolder(View itemView) {
        super(itemView);
        this.itemView=itemView;
        title=itemView.findViewById(R.id.tvtitle);
        descr=itemView.findViewById(R.id.tvdesc);
        date=itemView.findViewById(R.id.tvdate);
        time=itemView.findViewById(R.id.tvtime);
    }
}
