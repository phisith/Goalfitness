package com.example.goalfitness.ViewHolder;

import android.view.ContextMenu;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.goalfitness.R;

public class ToDoViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener{

    public TextView text_task, text_priority, text_Date_or_Time;
    public ToDoViewHolder(@NonNull View itemView) {
        super(itemView);

        text_task = itemView.findViewById(R.id.text_task);
        text_priority = itemView.findViewById(R.id.text_priority);
        text_Date_or_Time = itemView.findViewById(R.id.text_Date_or_Time);

        itemView.setOnCreateContextMenuListener(this);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.setHeaderTitle("Select menu");
        menu.add(0, 0, getAdapterPosition(), "Update");
        menu.add(0, 1, getAdapterPosition(), "Delete");
    }
}
