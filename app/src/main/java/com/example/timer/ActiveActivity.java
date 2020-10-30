package com.example.timer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;


public class ActiveActivity extends AppCompatActivity {
    public final static int  COMMAND_TICK = 100;

    public final static String BROADCAST_ACTION = "broadcast";

    public final static String PARAM_COMMAND = "command";

    ActiveTimerViewModel activeViewModel;


    BroadcastReceiver broadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Intent intent = getIntent();
        TimerSequence timer = (TimerSequence) intent.getSerializableExtra("timer");
        activeViewModel = new ViewModelProvider(this, new ActiveViewModelFactory(timer)).get(ActiveTimerViewModel.class);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_active);

        final Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);


        getSupportActionBar().hide();


        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                int command = intent.getIntExtra(PARAM_COMMAND, 0);

                switch (command){
                    case COMMAND_TICK:{
                        activeViewModel.Step();
                        break;
                    }
                }
            }
        };

        IntentFilter intentFilter  = new IntentFilter(BROADCAST_ACTION);
        registerReceiver(broadcastReceiver, intentFilter);

        Intent serviceIntent = new Intent(getBaseContext(), TimerService.class);
        serviceIntent.putExtra("timer", activeViewModel.getTimer());
        serviceIntent.putExtra("phases", activeViewModel.getPhases());
        startService(serviceIntent);

        activeViewModel.currentPhase.observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer phase) {
                changeColor(phase);
            }
        });


    }

    @Override
    public void onBackPressed() {
        openQuitDialog();
    }

    private void openQuitDialog() {
        AlertDialog.Builder quitDialog = new AlertDialog.Builder(
                ActiveActivity.this);
        quitDialog.setTitle(R.string.quit_title);

        quitDialog.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        quitDialog.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        quitDialog.show();
    }

    private void changeColor(Integer phase) {
        final View background = findViewById(android.R.id.content);
        final Window window = this.getWindow();

        if (phase < activeViewModel.phaseList.size()) {
            Phase currentPhase = activeViewModel.phaseList.get(phase);

            switch (currentPhase.getName()) {
                case "Preparation": {
                    background.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.green, null));
                    window.setStatusBarColor(ResourcesCompat.getColor(getResources(), R.color.darkGreen, null));
                    break;
                }
                case "Work": {
                    background.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.red, null));
                    window.setStatusBarColor(ResourcesCompat.getColor(getResources(), R.color.darkRed, null));
                    break;
                }
                case "Rest": {
                    background.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.blue, null));
                    window.setStatusBarColor(ResourcesCompat.getColor(getResources(), R.color.darkBlue, null));
                    break;
                }
                default: {
                    background.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.purple, null));
                    window.setStatusBarColor(ResourcesCompat.getColor(getResources(), R.color.darkPurple, null));
                    break;
                }
            }
        }

    }

    @Override
    protected void onDestroy() {
        Intent serviceIntent = new Intent(getBaseContext(), TimerService.class);
        stopService(serviceIntent);
        super.onDestroy();
    }
}