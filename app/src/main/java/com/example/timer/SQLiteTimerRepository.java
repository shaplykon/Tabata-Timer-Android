package com.example.timer;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;

import java.util.ArrayList;
import java.util.Random;

public class SQLiteTimerRepository implements TimerRepository {
    private final SQLiteHelper dbHelper;
    private final SQLiteDatabase db;
    public SQLiteTimerRepository(Context context){
        dbHelper = new SQLiteHelper(context);
        db = dbHelper.getWritableDatabase();
    }

    @Override
    public int update(TimerSequence timer) {
        return db.update(SQLiteHelper.TABLE_TIMER_NAME, getContentValues(timer), SQLiteHelper.COLUMN_ID + "=" + timer.getId(), null);
    }

    @Override
    public int insert(TimerSequence timer) {
        return (int) db.insert(SQLiteHelper.TABLE_TIMER_NAME, null, getContentValues(timer));
    }

    @Override
    public void delete(int Id) {
        dbHelper.deleteTimer(String.valueOf(Id));
    }

    @Override
    public ArrayList<TimerSequence> get() {
        return dbHelper.getList();
    }

    @Override
    public void clear() {
        dbHelper.onUpgrade(
                dbHelper.getWritableDatabase(),
                dbHelper.getWritableDatabase().getVersion(),
                dbHelper.getWritableDatabase().getVersion() + 1);
    }

    private ContentValues getContentValues(TimerSequence timer){
        Random rnd = new Random();
        int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));

        if(timer.getColor() == 0){
            timer.setColor(color);
        }

        ContentValues contentValues = new ContentValues();
        contentValues.put(SQLiteHelper.TRAINING_TITLE, timer.getTitle());
        contentValues.put(SQLiteHelper.PREPARATION_TIME, String.valueOf(timer.getPreparationTime()));
        contentValues.put(SQLiteHelper.WORKING_TIME, String.valueOf(timer.getWorkingTime()));
        contentValues.put(SQLiteHelper.REST_TIME, String.valueOf(timer.getRestTime()));
        contentValues.put(SQLiteHelper.CYCLES_AMOUNT, String.valueOf(timer.getCyclesAmount()));
        contentValues.put(SQLiteHelper.SETS_AMOUNT, String.valueOf(timer.getSetsAmount()));
        contentValues.put(SQLiteHelper.BETWEEN_SETS_REST, String.valueOf(timer.getBetweenSetsRest()));
        contentValues.put(SQLiteHelper.COOLDOWN_TIME, String.valueOf(timer.getCooldownTime()));
        contentValues.put(SQLiteHelper.TIMER_COLOR, String.valueOf(timer.getColor()));
        return contentValues;
    }
}
