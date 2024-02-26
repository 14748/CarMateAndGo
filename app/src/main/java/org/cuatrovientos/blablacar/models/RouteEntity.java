package org.cuatrovientos.blablacar.models;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Date;

public class RouteEntity {

    private int id;
    private ArrayList<LatLng> route;
    private float price;
    private ArrayList<User> passengers;
    private boolean isFull;
    private Date date;

    public RouteEntity(int id) {
        this.id = id;
    }
    public RouteEntity(int id, ArrayList<LatLng> route, float price, ArrayList<User> passengers, boolean isFull, Date date) {
        this.id = id;
        this.route = route;
        this.price = price;
        this.passengers = passengers;
        this.isFull = isFull;
        this.date = date;
    }
}