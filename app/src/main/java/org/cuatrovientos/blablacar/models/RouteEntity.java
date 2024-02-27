package org.cuatrovientos.blablacar.models;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RouteEntity {

    private int id;
    private List<CustomLatLng> points;
    private float price;
    private List<User> passengers;
    private int seats;
    private boolean isFull;
    private Date date;
    private Time time;

    public  RouteEntity(){
    }
    public RouteEntity(int id) {
        this.id = id;
    }
    public RouteEntity(int id, List<CustomLatLng> route, float price, ArrayList<User> passengers, boolean isFull, Date date) {
        this.id = id;
        this.points = route;
        this.price = price;
        this.passengers = passengers;
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

    public List<User> getPassengers() {
        return passengers;
    }

    public int getSeats() {
        return seats;
    }

    public boolean isFull() {
        return isFull;
    }

    public Date getDate() {
        return date;
    }
    public Time getTime(){ return time; }

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

    public void setTimef(Time time){ this.time = time; }
}