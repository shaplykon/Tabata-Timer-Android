package com.example.timer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

public class ActiveActivity extends AppCompatActivity {
    ActiveTimerViewModel activeViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        activeViewModel = new ViewModelProvider(this).get(ActiveTimerViewModel.class);
        Intent intent = getIntent();
        int id = intent.getIntExtra("id", 0);
        activeViewModel.setTimer(id, this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_active);

        final Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        Intent serviceIntent = new Intent(getBaseContext(), TimerService.class);
        startForegroundService(serviceIntent);

        getSupportActionBar().hide();


        activeViewModel.currentPhase.observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer phase) {
                changeColor(phase);
            }
        });


        @SuppressLint("StaticFieldLeak")
        class ProgressTask extends AsyncTask<Void, Integer, Void> {
            @Override
            protected Void doInBackground(Void... unused) {
                while(activeViewModel.isRunning) {
                    SystemClock.sleep(1000);
                    activeViewModel.Step();

                }
                return(null);
            }
            @Override
            protected void onProgressUpdate(Integer... items) {

            }
            @Override
            protected void onPostExecute(Void unused) {

                Toast.makeText(getApplicationContext(), getResources().getString(R.string.training_end), Toast.LENGTH_SHORT)
                        .show();
            }
        }

        new ProgressTask().execute();

    }

    private void changeColor(Integer phase) {
        final View background = findViewById(android.R.id.content);
        final Window window = this.getWindow();
        if (phase == 0) {
            background.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.green, null));
            window.setStatusBarColor(ResourcesCompat.getColor(getResources(), R.color.darkGreen, null));
        } else if (!activeViewModel.isRunning) {
            background.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.purple, null));
            window.setStatusBarColor(ResourcesCompat.getColor(getResources(), R.color.darkPurple, null));
        } else if (phase % 2 == 1) {
            background.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.red, null));
            window.setStatusBarColor(ResourcesCompat.getColor(getResources(), R.color.darkRed, null));
        } else {
            background.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.blue, null));
            window.setStatusBarColor(ResourcesCompat.getColor(getResources(), R.color.darkBlue, null));
        }
    }

    @Override
    protected void onDestroy() {
        Intent serviceIntent = new Intent(getBaseContext(), TimerService.class);
        stopService(serviceIntent);
        super.onDestroy();
    }
}