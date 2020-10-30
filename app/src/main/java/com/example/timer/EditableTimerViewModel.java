package com.example.timer;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


public class EditableTimerViewModel extends ViewModel {

    MutableLiveData<Integer> id = new MutableLiveData<>();
    MutableLiveData<String> title = new MutableLiveData<>();
    MutableLiveData<Integer> preparingTime = new MutableLiveData<>();
    MutableLiveData<Integer> workingTime = new MutableLiveData<>();
    MutableLiveData<Integer> restTime = new MutableLiveData<>();
    MutableLiveData<Integer> cyclesAmount = new MutableLiveData<>();
    MutableLiveData<Integer> setsAmount = new MutableLiveData<>();
    MutableLiveData<Integer> restBetweenSets = new MutableLiveData<>();
    MutableLiveData<Integer> cooldownTime = new MutableLiveData<>();
    MutableLiveData<Integer> color = new MutableLiveData<>();

    public EditableTimerViewModel(TimerSequence timer) {
        id.setValue(timer.getId());
        title.setValue(timer.getTitle());
        preparingTime.setValue(timer.getPreparationTime());
        workingTime.setValue(timer.getWorkingTime());
        restTime.setValue(timer.getRestTime());
        cyclesAmount.setValue(timer.getCyclesAmount());
        setsAmount.setValue(timer.getSetsAmount());
        restBetweenSets.setValue(timer.getBetweenSetsRest());
        cooldownTime.setValue(timer.getCooldownTime());
        color.setValue(timer.getColor());
    }

    public TimerSequence getTimer() {
        return new TimerSequence(id.getValue(), title.getValue(),
                preparingTime.getValue(), workingTime.getValue(),
                restTime.getValue(), cyclesAmount.getValue(),
                restBetweenSets.getValue(), setsAmount.getValue(),
                cooldownTime.getValue(), color.getValue());

    }

    public TimerSequence saveTimer(String type, Context context) {
        SQLiteTimerRepository timerRepository = new SQLiteTimerRepository(context);
        TimerSequence timer = getTimer();
        if (type.equals("edit")) {
            timerRepository.update(timer);
        }
        else {
            timer.setId(timerRepository.insert(timer));
        }
        return timer;

    }


}

