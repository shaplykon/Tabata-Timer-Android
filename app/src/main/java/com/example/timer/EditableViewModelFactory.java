package com.example.timer;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class EditableViewModelFactory implements ViewModelProvider.Factory {
    TimerSequence timer;

    public EditableViewModelFactory(TimerSequence timer) {
        this.timer = timer;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new EditableTimerViewModel(timer);
    }
}