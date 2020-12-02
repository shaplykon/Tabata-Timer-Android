package com.example.timer;

import java.io.Serializable;

public class Phase implements Serializable {
    private int id;
    private String name;
    private int time;

    public Phase(int id, int time, String name){
        this.id = id;
        this.time = time;
        this.name = name;
    }

    public int getId(){return id;}

    public void setId(int Id){this.id = Id;}

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
