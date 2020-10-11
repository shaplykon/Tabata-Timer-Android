package com.example.timer;

import java.io.Serializable;

class TimerSequence implements Serializable {

    private int id;
    private String title;
    private int preparationTime;
    private int workingTime;
    private int restTime;
    private int cyclesAmount;
    private int betweenSetsRest;
    private int setsAmount;
    private int cooldownTime;


    public TimerSequence(int _id, String _title, int _preparationTime, int _workingTime,
                         int _restTime, int _cyclesAmount, int _setsAmount, int _betweenSetsRest,
                         int _cooldownTime) {
        this.id = _id;
        this.title = _title;
        this.preparationTime = _preparationTime;
        this.workingTime = _workingTime;
        this.restTime = _restTime;
        this.cyclesAmount = _cyclesAmount;
        this.setsAmount = _setsAmount;
        this.betweenSetsRest = _betweenSetsRest;
        this.cooldownTime = _cooldownTime;
    }

    public void setId(int _id) {
        id = _id;
    }

    public void setTitle(String _title) {
        title = _title;
    }

    public void setPreparationTime(int _preparationTime) {
        preparationTime = Math.max(_preparationTime, 0);
    }

    public void setWorkingTime(int _workingTime) {
        workingTime = Math.max(_workingTime, 0);
    }

    public void setRestTime(int _restTime) {
        restTime = Math.max(_restTime, 0);
    }

    public void setCyclesAmount(int _cyclesAmount) {
        cyclesAmount = Math.max(_cyclesAmount, 0);
    }

    public void setSetsAmount(int _setsAmount) {
        setsAmount = Math.max(_setsAmount, 0);
    }

    public void setBetweenSetsRest(int _betweenSetsRest) {
        betweenSetsRest = Math.max(_betweenSetsRest, 0);
    }

    public void setCooldownTime(int _cooldownTime) {
        cooldownTime = Math.max(_cooldownTime, 0);
    }

    public int getId() {
        return id;
    }

    public int getPreparationTime() {
        return preparationTime;
    }

    public String getTitle() {
        return title;
    }

    public int getWorkingTime() {
        return workingTime;
    }

    public int getRestTime() {
        return restTime;
    }

    public int getCyclesAmount() {
        return cyclesAmount;
    }

    public int getSetsAmount() {
        return setsAmount;
    }

    public int getBetweenSetsRest() {
        return betweenSetsRest;
    }

    public int getCooldownTime() {
        return cooldownTime;
    }

}

