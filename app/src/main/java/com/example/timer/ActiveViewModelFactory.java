package com.example.timer;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class ActiveViewModelFactory implements ViewModelProvider.Factory {

    private final TimerSequence timer;

    public ActiveViewModelFactory(TimerSequence timer){
        this.timer = timer;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if(modelClass.isAssignableFrom(ActiveTimerViewModel.class)){
            return (T) new ActiveTimerViewModel(timer);
        }
        throw new IllegalArgumentException("Incorrect ViewModel class");
    }
}
