package com.example.shubham.todolist;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
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

public class ToDoAdapter extends RecyclerView.Adapter<ToDoViewHolder> {
    ArrayList<ToDoItem> item;
    LayoutInflater inflater;
    Context context;
    TodoListener listener;
    public ToDoAdapter(@NonNull Context context,ArrayList<ToDoItem> item,TodoListener listener) {
        inflater= (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        this.item=item;
        this.context=context;
        this.listener=listener;
    }

    @Override
    public ToDoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View output;
        output=inflater.inflate(R.layout.row_layout,parent,false);
        return new ToDoViewHolder(output);
    }

    @Override
    public void onBindViewHolder(final ToDoViewHolder holder, int position) {
        ToDoItem add=item.get(position);
        holder.title.setText(add.getName());
        holder.descr.setText(add.getDescription());
        holder.date.setText(add.getDate());
        holder.time.setText(add.getTime());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClickListener(v,holder.getAdapterPosition());
            }
        });

    }

    @Override
    public int getItemCount() {
        return item.size();
    }

}
