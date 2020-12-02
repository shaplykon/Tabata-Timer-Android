package com.example.timer;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;

import java.util.ArrayList;
import java.util.Random;

public class SQLiteTimerRepository implements TimerRepository {
    private final SQLiteHelper dbHelper;

    public SQLiteTimerRepository(Context context){
        dbHelper = new SQLiteHelper(context);
    }

    @Override
    public void update(TimerSequence timer) {
        dbHelper.updateTimer(timer);
    }

    @Override
    public int insert(TimerSequence timer) {
        return dbHelper.insertTimer(timer);
    }

    @Override
    public void delete(int Id) {
        dbHelper.deleteTimer(String.valueOf(Id));
    }

    @Override
    public ArrayList<TimerSequence> get() {
        return dbHelper.getTimers();
    }

    @Override
    public void clear() {
        dbHelper.onUpgrade(
                dbHelper.getWritableDatabase(),
                dbHelper.getWritableDatabase().getVersion(),
                dbHelper.getWritableDatabase().getVersion() + 1);
    }


}
