package com.example.firstmobileapp.models;

public class Service {
    private String service;
    private double price;

    public Service() {
    }

    public Service(String service, double price) {
        this.service = service;
        this.price = price;
    }

    public Service(String service) {
        this.service = service;
    }

    public String getService() { return service; }

    public void setService(String service) { this.service = service; }

    public double getPrice() { return price; }

    public void setPrice(double price) { this.price = price; }
}
