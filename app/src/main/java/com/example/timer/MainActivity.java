package com.example.timer;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.app.AppCompatDialog;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements
        DataAdapter.OnItemDeleteListener,
        DataAdapter.OnTimerListener,
        DataAdapter.OnLongTimerListener {

    int ADD_DATA_REQUEST = 1;
    int EDIT_DATA_REQUEST = 2;

    RecyclerView recyclerView;
    ArrayList<TimerSequence> timerList = new ArrayList<>();
    SQLiteHelper dbHelper;
    LinearLayoutManager linearLayoutManager;
    FloatingActionButton addButton;
    DataAdapter dataAdapter;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @SuppressLint({"SetTextI18n", "CommitPrefEdits"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        editor = sharedPreferences.edit();

        dbHelper = new SQLiteHelper(getApplicationContext());

        recyclerView = findViewById(R.id.recyclerView);
        linearLayoutManager = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL, false);

        addButton = findViewById(R.id.addButton);
        recyclerView.setLayoutManager(linearLayoutManager);
        timerList = dbHelper.getList();

        dataAdapter = new DataAdapter(MainActivity.this, timerList, this, this);
        recyclerView.setAdapter(dataAdapter);
        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(dataAdapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(recyclerView);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), DetailActivity.class);
                intent.putExtra("timer", new TimerSequence());
                intent.putExtra("type", "add");
                startActivityForResult(intent, ADD_DATA_REQUEST);

            }
        });
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_settings) {
            Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
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
                timerList.add((TimerSequence) data.getSerializableExtra("timer"));
                Objects.requireNonNull(recyclerView.getAdapter()).notifyDataSetChanged();

            }
        } else if (requestCode == EDIT_DATA_REQUEST && resultCode == RESULT_OK && data != null) {
            TimerSequence timer = (TimerSequence) data.getSerializableExtra("timer");
            if (timer != null) {
                for (int i = 0; i < timerList.size(); i++) {
                    if (timerList.get(i).getId() == (timer.getId())) {
                        timerList.set(i, timer);
                        Objects.requireNonNull(recyclerView.getAdapter()).notifyDataSetChanged();
                        break;
                    }
                }
            }

        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }

    }

    @Override
    public void onLongClick(int position) {
        Toast.makeText(this, "long", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onItemDelete(int position) {
        timerList.remove(position);
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocaleHelper.setLocale(MainActivity.this, LocaleHelper.getLanguage(MainActivity.this));
        recyclerView.getAdapter().notifyDataSetChanged();
    }
}
