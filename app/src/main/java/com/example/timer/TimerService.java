package com.example.timer;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

import androidx.core.app.NotificationCompat;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


public class TimerService extends Service {


    ExecutorService executorService;

    @Override
    public void onCreate() {
        super.onCreate();
        executorService = Executors.newFixedThreadPool(1);
    }

    public TimerService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        createNotificationChannel();
        Intent intent1 = new Intent(this, ActiveActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent1, 0);
        Notification notification = new NotificationCompat.Builder(this, "TimerChannel")
                .setContentTitle("Timer notification")
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentIntent(pendingIntent).build();

        startForeground(1, notification);
        getApplicationContext();
        TimerSequence timer = (TimerSequence) intent.getSerializableExtra("timer");
        ArrayList<Phase> phases = (ArrayList<Phase>) intent.getSerializableExtra("phases");
      //  int phase = intent.getIntExtra("phase", 0);
        TimerRun timerRun = new TimerRun(timer, phases, getApplicationContext());
        executorService.execute(timerRun);

        return START_STICKY;
    }



    @Override
    public void onDestroy() {
        executorService.shutdownNow();
        stopForeground(true);
        stopSelf();
        super.onDestroy();
    }

    private void createNotificationChannel() {
        NotificationChannel notificationChannel = new NotificationChannel(
                "TimerChannel",
                "Timer notification",
                NotificationManager.IMPORTANCE_DEFAULT);

        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(notificationChannel);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}

class TimerRun implements Runnable{
    TimerSequence timer;
    ArrayList<Phase> phases;
    Context context;
    MediaPlayer mediaPlayer;
    int currentPhase;
    int counterValue;

    public TimerRun(TimerSequence timer, ArrayList<Phase> phases,  Context context){
        this.timer = timer;
        this.phases = phases;
        this.context = context;
        currentPhase = 0;
        counterValue = phases.get(currentPhase).getTime();

    }

    @Override
    public void run() {
        try {
            startCycle();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void startCycle() throws InterruptedException {
        Intent intent = new Intent(ActiveActivity.BROADCAST_ACTION);
        intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
        while (currentPhase <= phases.size()) {
            TimeUnit.SECONDS.sleep(1);
            int command = Step();
            handleCommand(command, intent);
            intent.putExtra(ActiveActivity.PARAM_COMMAND, command);
            context.sendBroadcast(intent);
        }

    }

    private void handleCommand(int command, Intent intent){
        switch (command){
            case ActiveActivity.COMMAND_TICK:{
                break;
            }


            case ActiveActivity.COMMAND_CHANGE:{
                playSound();
                intent.putExtra(ActiveActivity.ARG_PHASE, currentPhase);
                intent.putExtra(ActiveActivity.ARG_COUNTER, counterValue);
            }

            case ActiveActivity.COMMAND_STOP:{
                break;
            }

        }
    }

    private void playSound(){
        switch (phases.get(currentPhase).getName()){
            case "Work":{
                mediaPlayer = MediaPlayer.create(context, R.raw.whistling);
                mediaPlayer.start();
                break;
            }
            case "Cooldown":
            case "Rest":{
                mediaPlayer = MediaPlayer.create(context, R.raw.gong);
                mediaPlayer.start();
                break;
            }

        }
    }

    private int Step(){
        if (counterValue > 1) {
            counterValue--;
            return ActiveActivity.COMMAND_TICK;
        } else {
            currentPhase++;
            if (currentPhase == phases.size()) {
                mediaPlayer = MediaPlayer.create(context, R.raw.finish);
                mediaPlayer.start();
                return ActiveActivity.COMMAND_STOP;
            } else {
                counterValue = phases.get(currentPhase).getTime();
                return ActiveActivity.COMMAND_CHANGE;
            }
        }
    }
}