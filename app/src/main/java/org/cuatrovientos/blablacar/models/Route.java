package org.cuatrovientos.blablacar.models;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Date;

public class Route {

    private int id;
    private ArrayList<LatLng> route;
    private float price;
    private ArrayList<User> passengers;
    private int seats;
    private boolean isFull;
    private Date date;

    public Route(int id) {
        this.id = id;
    }
    public Route(int id, ArrayList<LatLng> route, float price, ArrayList<User> passengers, boolean isFull, Date date) {
        this.id = id;
        this.route = route;
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

    public ArrayList<LatLng> getRoute() {
        return route;
    }

    public float getPrice() {
        return price;
    }

    public ArrayList<User> getPassengers() {
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

    public void setRoute(ArrayList<LatLng> route) {
        this.route = route;
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