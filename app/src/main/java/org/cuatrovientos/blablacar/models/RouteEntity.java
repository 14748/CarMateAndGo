package org.cuatrovientos.blablacar.models;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RouteEntity {

    private int id;
    private CustomLatLng travelPoint;
    private String duration;
    private String kms;
    private List<CustomLatLng> points;
    private float price;
    private List<User> passengers;
    private int seats;
    private boolean isFull;
    private Date date;

    public  RouteEntity(){
    }
    public RouteEntity(int id) {
        this.id = id;
    }

    public RouteEntity(CustomLatLng travelPoint) {
        this.travelPoint = travelPoint;
    }
    public RouteEntity(int id, List<CustomLatLng> route, float price, ArrayList<User> passengers, boolean isFull, Date date) {
        this.id = id;
        this.points = route;
        this.price = price;
        this.passengers = passengers;
        this.isFull = isFull;
        this.date = date;
    }

    public RouteEntity(int id, CustomLatLng travelPoint, String duration, String kms, List<CustomLatLng> points, float price, List<User> passengers, int seats, boolean isFull, Date date) {
        this.id = id;
        this.travelPoint = travelPoint;
        this.duration = duration;
        this.kms = kms;
        this.points = points;
        this.price = price;
        this.passengers = passengers;
        this.seats = seats;
        this.isFull = isFull;
        this.date = date;
    }

    /*
    GETTERS AND SETTERS
     */

    public int getId() {
        return id;
    }

    public List<CustomLatLng> getPoints() {
        return points;
    }

    public float getPrice() {
        return price;
    }

    public CustomLatLng getTravelPoint() {
        return travelPoint;
    }

    public void setTravelPoint(CustomLatLng travelPoint) {
        this.travelPoint = travelPoint;
    }

    public List<User> getPassengers() {
        return passengers;
    }

    public int getSeats() {
        return seats;
    }

    public boolean isFull() {
        return isFull;
    }

    public String getDuration() {
        return duration;
    }
    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getKms() {
        return kms;
    }

    public void setKms(String kms) {
        this.kms = kms;
    }

    public Date getDate() {
        return date;
    }

    public void setPoints(List<CustomLatLng> points) {
        this.points = points;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public void setPassengers(ArrayList<User> passengers) {
        this.passengers = passengers;
    }

    public void setSeats(int seats) {
        this.seats = seats;
    }

    public void setFull(boolean full) {
        isFull = full;
    }

    public void setDate(Date date) {
        this.date = date;
    }

}