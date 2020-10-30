package com.example.timer;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

public class ActiveTimerViewModel extends ViewModel {
    ArrayList<Phase> phaseList = new ArrayList<>();
    TimerSequence timer;
    MutableLiveData<Integer> counterValue;
    MutableLiveData<Integer> currentPhase;
    MutableLiveData<Boolean> isRunning;


    public ActiveTimerViewModel(@NonNull TimerSequence timer) {
        this.timer = timer;
        counterValue = new MutableLiveData<>();
        currentPhase = new MutableLiveData<>();
        isRunning = new MutableLiveData<>();
        counterValue.setValue(timer.getPreparationTime());
        currentPhase.setValue(0);
        isRunning.setValue(true);

        for (int cycle = 0; cycle < timer.getCyclesAmount(); cycle++) {

            phaseList.add(new Phase(timer.getPreparationTime(), "Preparing"));
            for (int set = 0; set < timer.getSetsAmount(); set++) {
                phaseList.add(new Phase(timer.getWorkingTime(), "Work"));
                phaseList.add(new Phase(timer.getRestTime(), "Rest"));
            }
            phaseList.add(new Phase(timer.getCooldownTime(), "Cooldown"));
            phaseList.add(new Phase(timer.getBetweenSetsRest(), "Rest"));
        }
        phaseList.remove(phaseList.size() - 1);
    }

    public TimerSequence getTimer(){
        return  timer;
    }

    public ArrayList<Phase> getPhases(){
        return phaseList;
    }

    public void changePhase(int position){
        currentPhase.setValue(position);
        counterValue.postValue(phaseList.get(position).getTime());
    }

}
