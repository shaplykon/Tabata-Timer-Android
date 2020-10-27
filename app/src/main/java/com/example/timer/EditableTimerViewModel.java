package com.example.timer;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.Objects;
import java.util.Random;


public class EditableTimerViewModel extends ViewModel {

    MutableLiveData<Integer> id;
    MutableLiveData<String> title;
    MutableLiveData<Integer> preparingTime;
    MutableLiveData<Integer> workingTime;
    MutableLiveData<Integer> restTime;
    MutableLiveData<Integer> cyclesAmount;
    MutableLiveData<Integer> setsAmount;
    MutableLiveData<Integer> restBetweenSets;
    MutableLiveData<Integer> cooldownTime;
    MutableLiveData<Integer> color;

    public EditableTimerViewModel() {
        id = new MutableLiveData<>();
        title = new MutableLiveData<>();
        preparingTime = new MutableLiveData<>();
        workingTime = new MutableLiveData<>();
        restTime = new MutableLiveData<>();
        cyclesAmount = new MutableLiveData<>();
        setsAmount = new MutableLiveData<>();
        restBetweenSets = new MutableLiveData<>();
        cooldownTime = new MutableLiveData<>();
        color = new MutableLiveData<>();
    }

    public void setTimer(TimerSequence timer, Context context) {
        SQLiteHelper helper = new SQLiteHelper(context);
        id.setValue(timer.getId());
        title.setValue(timer.getTitle());
        preparingTime.setValue(timer.getPreparationTime());
        workingTime.setValue(timer.getWorkingTime());
        restTime.setValue(timer.getRestTime());
        cyclesAmount.setValue(timer.getCyclesAmount());
        setsAmount.setValue(timer.getSetsAmount());
        restBetweenSets.setValue(timer.getBetweenSetsRest());
        cooldownTime.setValue(timer.getCooldownTime());
        color.setValue(helper.getColorByTimerID(id.getValue()));
    }


    public TimerSequence getTimer(){
        return  new TimerSequence(id.getValue(), title.getValue(),
                preparingTime.getValue(), workingTime.getValue(),
                restTime.getValue(), cyclesAmount.getValue(),
                restBetweenSets.getValue(), setsAmount.getValue(),
                cooldownTime.getValue());

    }

    public int saveTimer(String type, Context context){
        ContentValues contentValues = new ContentValues();
        contentValues.put(SQLiteHelper.TRAINING_TITLE, title.getValue());
        contentValues.put(SQLiteHelper.PREPARATION_TIME, Objects.requireNonNull(preparingTime.getValue()).toString());
        contentValues.put(SQLiteHelper.WORKING_TIME, Objects.requireNonNull(workingTime.getValue()).toString());
        contentValues.put(SQLiteHelper.REST_TIME, Objects.requireNonNull(restTime.getValue()).toString());
        contentValues.put(SQLiteHelper.CYCLES_AMOUNT, Objects.requireNonNull(cyclesAmount.getValue()).toString());
        contentValues.put(SQLiteHelper.SETS_AMOUNT, Objects.requireNonNull(setsAmount.getValue()).toString());
        contentValues.put(SQLiteHelper.BETWEEN_SETS_REST, Objects.requireNonNull(restBetweenSets.getValue()).toString());
        contentValues.put(SQLiteHelper.COOLDOWN_TIME, Objects.requireNonNull(cooldownTime.getValue()).toString());

        SQLiteHelper dbHelper = new SQLiteHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        if (type.equals("edit")) {
            db.update(SQLiteHelper.TABLE_TIMER_NAME, contentValues, SQLiteHelper.COLUMN_ID + "=" + id.getValue(), null);
            contentValues = new ContentValues();
            contentValues.put(SQLiteHelper.TIMER_COLOR, String.valueOf(color.getValue()));
            db.update(SQLiteHelper.TABLE_SETTINGS_NAME, contentValues, SQLiteHelper.TIMER_ID + "=" + id.getValue(), null);
            return id.getValue();
        } else {
            int _id = (int) db.insert(SQLiteHelper.TABLE_TIMER_NAME, null, contentValues);
            contentValues.clear();
            Random rnd = new Random();
            int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
            contentValues.put(SQLiteHelper.TIMER_COLOR, color);
            contentValues.put(SQLiteHelper.TIMER_ID, _id);
            db.insert(SQLiteHelper.TABLE_SETTINGS_NAME, null, contentValues);
            return _id;
        }

    }


}
