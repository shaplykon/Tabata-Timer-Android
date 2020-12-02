package com.example.timer;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;

import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;

public class SQLiteHelper extends SQLiteOpenHelper {
    // Database version
    public static final String DATABASE_NAME = "timer.db";
    private static final int SCHEMA = 1;

    // Tables names
    public static final String TABLE_TIMER_NAME = "TIMER";
    public static final String TABLE_PHASES_NAME = "PHASES";

    // TIMER table column names
    public static final String TRAINING_TITLE = "TRAINING_TITLE";
    public static final String PREPARATION_TIME = "PREPARATION_TIME";
    public static final String WORKING_TIME = "WORKING_TIME";
    public static final String REST_TIME = "REST_TIME";
    public static final String CYCLES_AMOUNT = "CYCLES_AMOUNT";
    public static final String SETS_AMOUNT = "SETS_AMOUNT";
    public static final String BETWEEN_SETS_REST = "BETWEEN_SETS_REST";
    public static final String COOLDOWN_TIME = "COOLDOWN_TIME";

    // SETTINGS table column names
    public static final String TIMER_COLOR = "COLOR";

    // PHASES table column names
    public static final String TIMER_ID = "TIMER_ID";
    public static final String PHASE_NAME = "PHASE_NAME";
    public static final String TIME = "TIME";

    // Common tables names
    public static final String COLUMN_ID = "_id";


    public SQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, SCHEMA);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_TIMER_NAME + " (" + COLUMN_ID +
                " INTEGER PRIMARY KEY AUTOINCREMENT, " + TRAINING_TITLE + " TEXT NOT NULL, " +
                PREPARATION_TIME + " INT NOT NULL, " + WORKING_TIME + " INT NOT NULL, " +
                REST_TIME + " INT NOT NULL, " + CYCLES_AMOUNT + " INT NOT NULL, " + SETS_AMOUNT +
                " INT NOT NULL, " + BETWEEN_SETS_REST + " INT NOT NULL, " + COOLDOWN_TIME +
                " INT NOT NULL, " + TIMER_COLOR + " INT NOT NULL);";
        db.execSQL(createTable);
        createTable = "CREATE TABLE " + TABLE_PHASES_NAME + " (" + COLUMN_ID +
                " INTEGER PRIMARY KEY AUTOINCREMENT, " + TIMER_ID + " INT NOT NULL, " +
                PHASE_NAME + " TEXT NOT NULL, " + TIME + " INT NOT NULL);";
        db.execSQL(createTable);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TIMER_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PHASES_NAME);
        onCreate(db);
    }


    public ArrayList<TimerSequence> getTimers() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_TIMER_NAME, null);
        ArrayList<TimerSequence> timerList = new ArrayList<>();
        if (cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                do {
                    TimerSequence timer =
                            new TimerSequence(cursor.getInt(cursor.getColumnIndex(SQLiteHelper.COLUMN_ID)),
                                    cursor.getString(cursor.getColumnIndex(SQLiteHelper.TRAINING_TITLE)),
                                    cursor.getInt(cursor.getColumnIndex(SQLiteHelper.PREPARATION_TIME)),
                                    cursor.getInt(cursor.getColumnIndex(SQLiteHelper.WORKING_TIME)),
                                    cursor.getInt(cursor.getColumnIndex(SQLiteHelper.REST_TIME)),
                                    cursor.getInt(cursor.getColumnIndex(SQLiteHelper.CYCLES_AMOUNT)),
                                    cursor.getInt(cursor.getColumnIndex(SQLiteHelper.SETS_AMOUNT)),
                                    cursor.getInt(cursor.getColumnIndex(SQLiteHelper.BETWEEN_SETS_REST)),
                                    cursor.getInt(cursor.getColumnIndex(SQLiteHelper.COOLDOWN_TIME)),
                                    cursor.getInt(cursor.getColumnIndex(SQLiteHelper.TIMER_COLOR)));
                    timerList.add(timer);

                } while (cursor.moveToNext());
            }

        }
        cursor.close();
        return timerList;
    }

    public int insertTimer(TimerSequence timer){
        SQLiteDatabase db = this.getWritableDatabase();
        return (int)db.insert(SQLiteHelper.TABLE_TIMER_NAME, null, getContentValues(timer));
    }

    public void updateTimer(TimerSequence timer){
        SQLiteDatabase db = this.getWritableDatabase();
        db.update(SQLiteHelper.TABLE_TIMER_NAME, getContentValues(timer), SQLiteHelper.COLUMN_ID + "=" + timer.getId(), null);
    }

    public void deleteTimer(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("DELETE  FROM " + TABLE_TIMER_NAME + " WHERE " +
                SQLiteHelper.COLUMN_ID + " = " + id, null);
        db.delete(TABLE_TIMER_NAME, COLUMN_ID + " = " + id, null);
        cursor.close();
    }


    public ArrayList<Phase> getPhasesById(int timerId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_PHASES_NAME + " WHERE "
                + TIMER_ID + " = " + timerId + ";", null);

        ArrayList<Phase> phaseList = new ArrayList<>();
        if (cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                do {
                    Phase phase =
                            new Phase(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)),
                                    cursor.getInt(cursor.getColumnIndex(TIME)),
                                    cursor.getString(cursor.getColumnIndex(PHASE_NAME)));
                    phaseList.add(phase);

                } while (cursor.moveToNext());
            }

        }
        cursor.close();
        return phaseList;
    }

    public void savePhases(ArrayList<Phase> phaseList, int timerId) {
        SQLiteDatabase db = this.getWritableDatabase();
        for (Phase phase : phaseList) {
            db.insert(SQLiteHelper.TABLE_PHASES_NAME, null, getContentValues(phase, timerId));
        }
    }

    public void deletePhases(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("DELETE  FROM " + TABLE_PHASES_NAME + " WHERE " +
                SQLiteHelper.TIMER_ID + " = " + id, null);
        db.delete(TABLE_PHASES_NAME, TIMER_ID + " = " + id, null);
        cursor.close();
    }


    private ContentValues getContentValues(Phase phase, int timerId) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(SQLiteHelper.TIMER_ID, timerId);
        contentValues.put(SQLiteHelper.PHASE_NAME, phase.getName());
        contentValues.put(SQLiteHelper.TIME, phase.getTime());
        return contentValues;
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
