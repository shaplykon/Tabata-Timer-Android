package com.example.timer;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

class SQLiteHelper extends SQLiteOpenHelper {
    // Database version
    public static final String DATABASE_NAME = "timer.db";
    private static final int SCHEMA = 1;


    // Tables names
    public static final String TABLE_TIMER_NAME = "TIMER";
    public static final String TABLE_SETTINGS_NAME = "SETTINGS";


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
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TIMER_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SETTINGS_NAME);
        onCreate(db);
    }

    public void deleteTimer(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("DELETE  FROM " + TABLE_TIMER_NAME + " WHERE " +
                SQLiteHelper.COLUMN_ID + " = " + id, null);
        db.delete(TABLE_TIMER_NAME, COLUMN_ID + " = " + id, null);
        cursor.close();
    }


    public TimerSequence getTimerByID(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_TIMER_NAME + " WHERE "
                + COLUMN_ID + " = " + id  + ";", null);
        cursor.moveToFirst();
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
        cursor.close();
       return timer;

    }

    public ArrayList<TimerSequence> getList() {
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
}
