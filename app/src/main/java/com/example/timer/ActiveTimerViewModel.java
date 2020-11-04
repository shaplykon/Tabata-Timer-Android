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
    MutableLiveData<Boolean> isPaused;

    
    public ActiveTimerViewModel(@NonNull TimerSequence timer, ArrayList<Phase> phaseList) {
        this.timer = timer;
        this.phaseList = phaseList;
        counterValue = new MutableLiveData<>();
        currentPhase = new MutableLiveData<>();
        isRunning = new MutableLiveData<>();
        isPaused = new MutableLiveData<>();
        counterValue.setValue(timer.getPreparationTime());
        currentPhase.setValue(0);
        isRunning.setValue(true);
        isPaused.setValue(false);
    }

    public TimerSequence getTimer(){
        return  timer;
    }

    public ArrayList<Phase> getPhases(){
        return phaseList;
    }

    public void changePhase(int position){
        if(position <  0){
            position = 0;
        }
        else if(position > phaseList.size() - 1){
            position = phaseList.size() - 1;
        }


        currentPhase.setValue(position);
        counterValue.setValue(phaseList.get(position).getTime());
    }

}
