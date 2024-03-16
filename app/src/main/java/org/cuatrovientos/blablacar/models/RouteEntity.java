package org.cuatrovientos.blablacar.models;

import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RouteEntity implements Serializable {

    private String id;
    private CustomLatLng origin;
    private CustomLatLng destination;
    private String originText;
    private String destinationText;
    private String duration;
    private String kms;
    private List<CustomLatLng> points;
    private float price;
    private List<String> passengers;
    private int seats;
    private boolean isFull;
    private Date date;

    public  RouteEntity(){
    }
    public RouteEntity(String id) {
        this.id = id;
    }

    public RouteEntity(List<CustomLatLng> route, float price, ArrayList<String> passengers, boolean isFull, Date date) {
        this.id = FirebaseDatabase.getInstance().getReference().push().getKey();;
        this.points = route;
        this.price = price;
        this.passengers = passengers;
        this.isFull = isFull;
        this.date = date;
    }

    public RouteEntity(String duration, String kms, List<CustomLatLng> points, float price, List<String> passengers, int seats, boolean isFull, Date date, CustomLatLng origin, CustomLatLng destination, String textOrigin, String textDestination) {
        this.id = FirebaseDatabase.getInstance().getReference().push().getKey();;
        this.duration = duration;
        this.kms = kms;
        this.points = points;
        this.price = price;
        this.passengers = passengers;
        this.seats = seats;
        this.isFull = isFull;
        this.date = date;
        this.origin = origin;
        this.destination = destination;
        this.originText = textOrigin;
        this.destinationText = textDestination;
        this.passengers = new ArrayList<>();
    }

    /*
    GETTERS AND SETTERS
     */

    public void setId(String id) {
        this.id = id;
    }

    public CustomLatLng getOrigin() {
        return origin;
    }

    public void setOrigin(CustomLatLng origin) {
        this.origin = origin;
    }

    public CustomLatLng getDestination() {
        return destination;
    }

    public void setDestination(CustomLatLng destination) {
        this.destination = destination;
    }

    public String getOriginText() {
        return originText;
    }

    public void setOriginText(String originText) {
        this.originText = originText;
    }

    public String getDestinationText() {
        return destinationText;
    }

    public void setDestinationText(String destinationText) {
        this.destinationText = destinationText;
    }

    public void setPassengers(List<String> passengers) {
        this.passengers = passengers;
    }

    public String getId() {
        return id;
    }

    public List<CustomLatLng> getPoints() {
        return points;
    }

    public float getPrice() {
        return price;
    }

    public List<String> getPassengers() {
        return passengers;
    }

    public int getSeats() {
        return seats;
    }

    public boolean isFull() {
        if (passengers == null) {
            return false;
        }
        return passengers.size() == seats;
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