package org.cuatrovientos.blablacar.models;

import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;
import java.util.List;

public class Vehicle implements Serializable {
    private String id; // Unique identifier for the vehicle
    private String make; // Brand or manufacturer of the vehicle, e.g., Toyota, Ford
    private String model; // Specific model of the vehicle, e.g., Camry, F-150
    private int year; // Year of manufacture
    private String color; // Color of the vehicle
    private String licensePlate; // Vehicle's license plate number
    private List<String> carPreferences;

    // Default no-argument constructor
    public Vehicle() {
    }

    // Constructor with parameters
    public Vehicle(String make, String model, int year, String color, String licensePlate, List<String> carPreferences) {
        this.make = make;
        this.model = model;
        this.year = year;
        this.color = color;
        this.licensePlate = licensePlate;
        this.carPreferences = carPreferences;
        this.id = FirebaseDatabase.getInstance().getReference().push().getKey();
    }

    public List<String> getCarPreferences() {
        return carPreferences;
    }

    public void setCarPreferences(List<String> carPreferences) {
        this.carPreferences = carPreferences;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }
}

