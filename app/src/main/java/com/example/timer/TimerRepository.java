package com.example.timer;

import java.util.ArrayList;


public interface TimerRepository {
    ArrayList<TimerSequence> get();
    void clear();
    int update(TimerSequence timer);
    int insert(TimerSequence timer);
    void delete(int Id);
}
