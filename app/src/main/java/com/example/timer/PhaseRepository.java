package com.example.timer;

import java.util.ArrayList;

public interface PhaseRepository {
    ArrayList<Phase> get(int timerId);
    void insert(TimerSequence timerSequence);
    void delete(int timerId);
    void update(TimerSequence timerSequence);
}
