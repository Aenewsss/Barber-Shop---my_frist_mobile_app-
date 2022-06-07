package com.example.firstmobileapp.models;

public class Appointment {
    private String time, service;

    public Appointment() {
    }

    public Appointment(String time, String service) {
        this.time = time;
        this.service = service;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }
}
