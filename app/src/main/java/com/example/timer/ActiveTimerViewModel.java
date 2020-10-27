package com.example.timer;
import android.content.Context;
import android.content.Intent;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

public class ActiveTimerViewModel extends ViewModel {
    MutableLiveData<Integer> preparingTime;
    MutableLiveData<Integer> workingTime;
    MutableLiveData<Integer> restTime;
    MutableLiveData<Integer> cyclesAmount;
    MutableLiveData<Integer> setsAmount;
    MutableLiveData<Integer> restBetweenSets;
    MutableLiveData<Integer> cooldownTime;
    MutableLiveData<Integer> counterValue;
    MutableLiveData<Integer> currentPhase;
    Boolean isRunning;
    private final ArrayList<Integer> taskArray;

    public ActiveTimerViewModel() {
        taskArray = new ArrayList<>();
        preparingTime = new MutableLiveData<>();
        workingTime = new MutableLiveData<>();
        restTime = new MutableLiveData<>();
        cyclesAmount = new MutableLiveData<>();
        setsAmount = new MutableLiveData<>();
        restBetweenSets = new MutableLiveData<>();
        cooldownTime = new MutableLiveData<>();
        counterValue = new MutableLiveData<>();
        currentPhase = new MutableLiveData<>();
    }

    public void setTimer(int id, Context context) {
        SQLiteHelper dbHelper = new SQLiteHelper(context);
        TimerSequence timer = dbHelper.getTimerByID(id);
        preparingTime.setValue(timer.getPreparationTime());
        workingTime.setValue(timer.getWorkingTime());
        restTime.setValue(timer.getRestTime());
        cyclesAmount.setValue(timer.getCyclesAmount());
        restBetweenSets.setValue(timer.getBetweenSetsRest());
        cooldownTime.setValue(timer.getCooldownTime());
        setsAmount.setValue(timer.getSetsAmount());
        counterValue.setValue(preparingTime.getValue());
        currentPhase.setValue(0);
        isRunning = true;

        for (int k = 0; k < cyclesAmount.getValue(); k++) {
            taskArray.add(preparingTime.getValue());
            for (int i = 0; i < setsAmount.getValue(); i++) {
                taskArray.add(workingTime.getValue());
                taskArray.add(restTime.getValue());
            }
            taskArray.add(cooldownTime.getValue());
        }
    }

    public void changePhase(int position){
        currentPhase.setValue(position);
        counterValue.postValue(taskArray.get(currentPhase.getValue()));
    }

    public void Step() {
        if (counterValue.getValue() > 0) {
            counterValue.postValue(counterValue.getValue() - 1);
        } else {
            currentPhase.postValue(currentPhase.getValue() + 1);
            if (currentPhase.getValue() == taskArray.size()) {
                isRunning = false;
            } else {
                counterValue.postValue(taskArray.get(currentPhase.getValue()));
            }
        }
    }
}
