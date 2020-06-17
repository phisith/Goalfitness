package com.example.goalfitness;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.goalfitness.Model.ToDo;
import com.example.goalfitness.ViewHolder.ToDoViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class TodoActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private FloatingActionButton fab;

    FirebaseDatabase database;
    DatabaseReference todoDb;

    FirebaseRecyclerOptions<ToDo> options;
    FirebaseRecyclerAdapter<ToDo, ToDoViewHolder> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo);

        recyclerView = findViewById(R.id.recyclerview);
        fab = findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TodoActivity.this, inputActivity.class);
                startActivity(intent);
            }
        });

        database = FirebaseDatabase.getInstance();
        todoDb = database.getReference("ToDo");

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        showTask();
    }

    private void showTask() {
        options = new FirebaseRecyclerOptions.Builder<ToDo>().setQuery(todoDb, ToDo.class).build();

        adapter = new FirebaseRecyclerAdapter<ToDo, ToDoViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ToDoViewHolder holder, int position, @NonNull ToDo model) {
                holder.text_priority.setText(model.getPriority());
                holder.text_task.setText(model.getTask());
                holder.text_Date_or_Time.setText(model.getDate_or_Time());
            }

            @NonNull
            @Override
            public ToDoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.todo_row, parent, false);
                return new ToDoViewHolder(itemView);

            }
        };

        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {

        if(item.getTitle().equals("Update")){
            showUpdateDialog(adapter.getRef(item.getOrder()).getKey(), adapter.getItem(item.getOrder()));
        } else if (item.getTitle().equals("Delete")){
            deleteTask(adapter.getRef(item.getOrder()).getKey());
        }
        return super.onContextItemSelected(item);
    }

    private void showUpdateDialog(String key, ToDo item) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Update");
        builder.setMessage("Please update the fields");

        View update_layout = LayoutInflater.from(this).inflate(R.layout.custom_layout, null);

        EditText edt_update_task = update_layout.findViewById(R.id.edit_update_task);
        EditText edt_update_priority = update_layout.findViewById(R.id.edit_update_priority);
        EditText edt_update_Date_or_Time = update_layout.findViewById(R.id.edit_update_Date_or_Time);

        edt_update_task.setText(item.getTask());
        edt_update_priority.setText(item.getPriority());
        edt_update_Date_or_Time.setText(item.getDate_or_Time());

        builder.setView(update_layout);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String task = edt_update_task.getText().toString();
                String prioroty = edt_update_priority.getText().toString();
                String Date_or_Time = edt_update_Date_or_Time.getText().toString();

                ToDo toDo = new ToDo(task, prioroty, Date_or_Time);
                todoDb.child(key).setValue(toDo);

                Toast.makeText(TodoActivity.this, "Task is updated!", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.todo_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.delete_all){
            todoDb.removeValue();
        }
        return super.onOptionsItemSelected(item);
    }

    private void deleteTask(String key) {

        todoDb.child(key).removeValue();
    }


}
