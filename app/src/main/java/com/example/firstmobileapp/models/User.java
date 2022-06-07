package com.example.firstmobileapp.models;

public class User {
    private String username, age, email, phone;
    private Object appointments;

    public User(){

    }

    public User(String username, String age, String email, String phone, Object appointments) {
        this.username = username;
        this.age = age;
        this.email = email;
        this.phone = phone;
        this.appointments = appointments;
    }

    public void setUsername(String username){ this.username = username; }

    public String getUsername(){ return this.username; }

    public String getAge() { return age; }

    public void setAge(String age) { this.age = age; }

    public String getEmail() { return email;}

    public void setEmail(String email) { this.email = email;}

    public String getPhone() { return phone;}

    public void setPhone(String phone) {    this.phone = phone;}

    public void setAppointments(Object appointments) { this.appointments = appointments; }

    public Object getAppointments() { return appointments; }
}