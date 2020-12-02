package com.example.timer;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;

public class ActiveViewModelFactory implements ViewModelProvider.Factory {

    private final TimerSequence timer;
    private final ArrayList<Phase> phaseList;
    public ActiveViewModelFactory(TimerSequence timer, ArrayList<Phase> phaseList){
        this.timer = timer;
        this.phaseList = phaseList;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if(modelClass.isAssignableFrom(ActiveTimerViewModel.class)){
            return (T) new ActiveTimerViewModel(timer, phaseList);
        }
        throw new IllegalArgumentException("Incorrect ViewModel class");
    }
}
