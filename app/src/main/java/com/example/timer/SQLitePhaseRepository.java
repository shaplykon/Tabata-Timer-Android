package com.example.timer;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Random;

public class SQLitePhaseRepository implements PhaseRepository {
    private final SQLiteHelper dbHelper;

    public SQLitePhaseRepository(Context context){
        dbHelper = new SQLiteHelper(context);
    }

    @Override
    public ArrayList<Phase> get(int timerId) {
        return dbHelper.getPhasesById(timerId);
    }

    @Override
    public void insert(TimerSequence timer) {
        dbHelper.savePhases(getCreatedPhaseList(timer), timer.getId());
    }


    @Override
    public void delete(int timerId) {
        dbHelper.deletePhases(timerId);
    }

    @Override
    public void update(TimerSequence timer) {
        dbHelper.deletePhases(timer.getId());
        dbHelper.savePhases(getCreatedPhaseList(timer), timer.getId());
    }

    private ArrayList<Phase> getCreatedPhaseList(TimerSequence timer){
        ArrayList<Phase> phaseList = new ArrayList<>();
        for (int cycle = 0; cycle < timer.getCyclesAmount(); cycle++) {
            phaseList.add(new Phase(0, timer.getPreparationTime(), "Preparing"));
            for (int set = 0; set < timer.getSetsAmount(); set++) {
                phaseList.add(new Phase(0, timer.getWorkingTime(), "Work"));
                phaseList.add(new Phase(0, timer.getRestTime(), "Rest"));
            }
            phaseList.add(new Phase(0, timer.getCooldownTime(), "Cooldown"));
            phaseList.add(new Phase(0, timer.getBetweenSetsRest(), "Rest"));
        }
        if(phaseList.size() > 0) phaseList.remove(phaseList.size() - 1);
        return phaseList;
    }
}
