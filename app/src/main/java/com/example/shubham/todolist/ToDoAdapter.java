package com.example.shubham.todolist;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shubham on 6/26/2018.
 */

public class ToDoAdapter extends ArrayAdapter {
    ArrayList<ToDoItem> item;
    LayoutInflater inflater;
    public ToDoAdapter(@NonNull Context context,ArrayList<ToDoItem> item) {
        super(context, 0, item);
        inflater= (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        this.item=item;
    }

    @Override
    public int getCount() {
        return item.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View output= convertView;
        if(output==null){
            output=inflater.inflate(R.layout.row_layout,parent,false);
            TextView tvname,tvdesc,tvdate,tvtime;
            tvname=output.findViewById(R.id.tvtitle);
            tvdesc=output.findViewById(R.id.tvdesc);
            tvdate=output.findViewById(R.id.tvdate);
            tvtime=output.findViewById(R.id.tvtime);
            ToDoViewHolder viewHolder=new ToDoViewHolder();
            viewHolder.title=tvname;
            viewHolder.descr=tvdesc;
            viewHolder.date=tvdate;
            viewHolder.time=tvtime;
            output.setTag(viewHolder);

        }
        ToDoViewHolder viewHolder=(ToDoViewHolder) output.getTag();
        ToDoItem add=item.get(position);
        viewHolder.title.setText(add.getName());
        viewHolder.descr.setText(add.getDescription());
        viewHolder.date.setText(add.getDate());
        viewHolder.time.setText(add.getTime());
        return output;
    }
}
