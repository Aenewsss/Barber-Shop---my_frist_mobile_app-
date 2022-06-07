package com.example.firstmobileapp.models;

public class ScheduleModel {
    private String time;

    public ScheduleModel() {
    }

    public ScheduleModel(String time) {
        this.time = time;
    }

    public void setTime(String time) { this.time = time; }

    public String getTime() { return this.time; }
}
