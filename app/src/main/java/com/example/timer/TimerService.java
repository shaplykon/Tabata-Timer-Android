package com.example.timer;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
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

        TimerSequence timer = (TimerSequence) intent.getSerializableExtra("timer");
        ArrayList<Phase> phases = (ArrayList<Phase>) intent.getSerializableExtra("phases");
        TimerRun timerRun = new TimerRun(timer, phases);
        executorService.execute(timerRun);

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
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

    public TimerRun(TimerSequence timer, ArrayList<Phase> phases){
        this.timer = timer;
        this.phases = phases;
    }

    @Override
    public void run() {
        Intent intent = new Intent(ActiveActivity.BROADCAST_ACTION);
        try {
            TimeUnit.SECONDS.sleep(1);
            intent.putExtra(ActiveActivity.PARAM_COMMAND, ActiveActivity.COMMAND_TICK);

            // sendBroadcast(intent);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}