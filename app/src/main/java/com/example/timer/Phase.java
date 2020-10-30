package com.example.timer;

import java.io.Serializable;

public class Phase implements Serializable {
    private String name;
    private int time;

    public Phase(int time, String name){
        this.time = time;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }
}
