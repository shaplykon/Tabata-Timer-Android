package com.example.timer;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements DataAdapter.OnItemDelete, DataAdapter.OnTimerListener, DataAdapter.OnLongTimerListener {

    int ADD_DATA_REQUEST = 1;
    int EDIT_DATA_REQUEST = 2;

    RecyclerView recyclerView;
    ArrayList<TimerSequence> timerList = new ArrayList<>();
    SQLiteHelper dbHelper;
    LinearLayoutManager linearLayoutManager;
    FloatingActionButton addButton;
    DataAdapter dataAdapter;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbHelper = new SQLiteHelper(getApplicationContext());
        recyclerView = findViewById(R.id.recyclerView);
        linearLayoutManager = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL, false);
        addButton = findViewById(R.id.addButton);
        recyclerView.setLayoutManager(linearLayoutManager);
        timerList = dbHelper.getList();
        dataAdapter = new DataAdapter(MainActivity.this, timerList, this, this, this);
        recyclerView.setAdapter(dataAdapter);
        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(dataAdapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(recyclerView);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), DetailActivity.class);
                intent.putExtra("timer", (Bundle) null);
                intent.putExtra("type", "add");
                startActivityForResult(intent, ADD_DATA_REQUEST);
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onTimerClick(int position) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra("timer", timerList.get(position));
        intent.putExtra("type", "edit");
        startActivityForResult(intent, EDIT_DATA_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (requestCode == ADD_DATA_REQUEST) {
            if (resultCode == RESULT_OK && data != null) {
                timerList.add((TimerSequence) data.getSerializableExtra("list"));
            }
        } else if (requestCode == EDIT_DATA_REQUEST && resultCode == RESULT_OK && data != null) {
            TimerSequence timer = (TimerSequence) data.getSerializableExtra("timer");
            if (timer != null) {
                for (int i = 0; i < timerList.size(); i++) {
                    if (timerList.get(i).getId() == (timer.getId())) {
                        timerList.set(i, timer);
                        break;
                    }
                }
            }

        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
        if (recyclerView.getAdapter() != null) {
            recyclerView.getAdapter().notifyDataSetChanged();
        }
    }

    @Override
    public void onLongClick(int position) {
        //      Toast.makeText(this, "long", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onItemDelete(int position) {
        dbHelper.deleteTimer(String.valueOf(timerList.get(position).getId()));
        timerList.remove(position);
        if (recyclerView.getAdapter() != null) {
            recyclerView.getAdapter().notifyDataSetChanged();

        }
    }
}
