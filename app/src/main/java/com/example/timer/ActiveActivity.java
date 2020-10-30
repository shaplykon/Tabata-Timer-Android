package com.example.timer;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;


public class ActiveActivity extends AppCompatActivity {
    public final static int  COMMAND_TICK = 100;
    public final static int  COMMAND_STOP = 200;
    public final static int  COMMAND_CHANGE = 300;

    public final static String  ARG_PHASE = "phase";
    public final static String  ARG_COUNTER = "counter";


    public final static String BROADCAST_ACTION = "broadcast";

    public final static String PARAM_COMMAND = "command";

    ActiveTimerViewModel activeViewModel;
    TimerReceiver broadcastReceiver;

    TextView stageNameText;
    TextView stageText;
    ImageButton buttonPrev;
    ImageButton buttonNext;
    ImageButton buttonBack;
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


        stageNameText = findViewById(R.id.stageName);
        stageText = findViewById(R.id.stageText);

        buttonPrev = findViewById(R.id.prevButton);
        buttonNext = findViewById(R.id.nextButton);
        buttonBack = findViewById(R.id.backButton);

        buttonBack.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        buttonPrev.setOnClickListener(navigationButtonOnClick);
        buttonNext.setOnClickListener(navigationButtonOnClick);

        getSupportActionBar().hide();


        broadcastReceiver = new TimerReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                int command = intent.getIntExtra(PARAM_COMMAND, 0);
                handleReceiver(command, intent);
            }
        };

        IntentFilter intentFilter  = new IntentFilter(BROADCAST_ACTION);
        registerReceiver(broadcastReceiver, intentFilter);

        final Intent serviceIntent = new Intent(getBaseContext(), TimerService.class);
        serviceIntent.putExtra("timer", activeViewModel.getTimer());
        serviceIntent.putExtra("phases", activeViewModel.getPhases());
        startService(serviceIntent);

        activeViewModel.isRunning.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isRunning) {
                if(!isRunning){
                    stopService(new Intent(serviceIntent));
                    Toast.makeText(ActiveActivity.this, R.string.training_end, Toast.LENGTH_LONG).show();
                }
            }
        });

        activeViewModel.currentPhase.observe(this, new Observer<Integer>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onChanged(Integer phase) {
                ListView lv = findViewById(R.id.list_view);
                lv.setSelection(phase);

                stageText.setText((phase+1) + "/" + (activeViewModel.phaseList.size()));
                stageNameText.setText(activeViewModel.phaseList.get(phase).getName());
                changeColor(phase);
                Intent serviceIntent = new Intent(getBaseContext(), TimerService.class);
                serviceIntent.putExtra("timer", activeViewModel.getTimer());
                serviceIntent.putExtra("phases", activeViewModel.getPhases());
                serviceIntent.putExtra("phase", phase);
                startService(serviceIntent);
            }
        });


    }

    OnClickListener navigationButtonOnClick = new OnClickListener() {
        @SuppressLint("NonConstantResourceId")
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.prevButton:{
                    activeViewModel.changePhase(activeViewModel.currentPhase.getValue() - 1);
                }

                case R.id.nextButton:{
                    activeViewModel.changePhase(activeViewModel.currentPhase.getValue() + 1);
                }

            }
        }
    };

    private void handleReceiver(int command, Intent intent){

        switch (command){
            case COMMAND_TICK:{
                activeViewModel.counterValue.postValue(activeViewModel.counterValue.getValue() - 1);
                break;
            }

            case COMMAND_STOP:{
                activeViewModel.isRunning.postValue(false);
                break;
            }

            case COMMAND_CHANGE:{
                activeViewModel.currentPhase.
                        postValue(intent.getIntExtra(ActiveActivity.ARG_PHASE, 0));

                activeViewModel.counterValue.
                        postValue(intent.getIntExtra(ActiveActivity.ARG_COUNTER, 0));
                break;
            }
        }
    }

    @Override
    public void onBackPressed() {
        if(activeViewModel.isRunning.getValue()){
            openQuitDialog();
        }
        else{
            super.onBackPressed();
        }

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
                case "Preparing": {
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