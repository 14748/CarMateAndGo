package org.cuatrovientos.blablacar.models;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Parcelable;

import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class User implements Serializable {

    private String id;
    private String name;
    private String lastName;
    private Date birthDate;
    private String email;
    private String telephone;
    private String password;
    private List<RouteEntity> createdRoutes;
    private List<RouteEntity> passengerRoutes;
    private Bitmap userIcon;
    private List<Rating> ratings;
    private List<String> bannedUsers;
    private Vehicle vehicle;
    private String color;
    private  float c02Reduction;

    private float balance;
    private String fcmToken;
    public User(){
        this.createdRoutes = new ArrayList<>();
        this.passengerRoutes = new ArrayList<>();
        this.ratings = new ArrayList<>();
        this.bannedUsers = new ArrayList<>();
    }

    public User(String name, String lastName, Date birthDate, String email, String password) {
        this.id = FirebaseDatabase.getInstance().getReference().push().getKey();
        this.name = name;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.email = email;
        this.password = password;
        this.balance = 0;
        this.createdRoutes = new ArrayList<>();
        this.passengerRoutes = new ArrayList<>();
        this.ratings = new ArrayList<>();
        this.color = Utils.getRandomColor();
    }

    public User(String name, String lastName, Date birthDate, String email, String telephone, String password, Bitmap userIcon) {
        this.id = FirebaseDatabase.getInstance().getReference().push().getKey();
        this.name = name;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.email = email;
        this.telephone = telephone;
        this.password = password;
        this.balance = 0;
        this.createdRoutes = new ArrayList<>();
        this.passengerRoutes = new ArrayList<>();
        this.userIcon = userIcon;
        this.ratings = new ArrayList<>();
    }

    

    public float getC02Reduction() {
        return c02Reduction;
    }

    public void setC02Reduction(float c02Reduction) {
        this.c02Reduction = c02Reduction;
    }
    public void addC02Reduction(float additionalC02Reduction) {
        this.c02Reduction += additionalC02Reduction;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLastName() {
        return lastName;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public String getEmail() {
        return email;
    }

    public float getBalance() {
        return balance;
    }

    public void setBalance(float balance) {
        this.balance = balance;
    }

    public String getPassword() {
        return password;
    }

    public List<RouteEntity> getCreatedRoutes() {
        return createdRoutes;
    }
    public String getColor(){ return color; }

    public void setCreatedRoutes(List<RouteEntity> createdRoutes) {
        this.createdRoutes = createdRoutes;
    }

    public List<RouteEntity> getPassengerRoutes() {
        return passengerRoutes;
    }

    public void setPassengerRoutes(List<RouteEntity> passengerRoutes) {
        this.passengerRoutes = passengerRoutes;
    }

    public List<String> getBannedUsers() {
        return bannedUsers;
    }

    public void setBannedUsers(List<String> bannedUsers) {
        this.bannedUsers = bannedUsers;
    }

    public void addBannedUsers(String userId) {
        this.bannedUsers.add(userId);
    }

    public void removeBannedUsers(String userId) {
        this.bannedUsers.remove(userId);
    }

    public List<Rating> getRatings() {
        return ratings;
    }

    public void setRatings(List<Rating> ratings) {
        this.ratings = ratings;
    }

    public Bitmap getUserIcon() {
        return userIcon;
    }

    public String getTelephone(){ return telephone;}

    

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addCreatedRoute(RouteEntity route) {
            this.createdRoutes.add(route);
    }

    public void addRating(Rating rating) {
        this.ratings.add(rating);
    }


    public void addPassengerRoute(RouteEntity route) {
            this.passengerRoutes.add(route);
    }

    public void removePassengerRoute(String routeId) {
        for (RouteEntity route:
             this.passengerRoutes) {
            if (route.getId().equals(routeId)){
                this.passengerRoutes.remove(route);
            }
        }
    }


    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setColor(String color){
        this.color = color;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUserIcon(Bitmap userIcon) {
        this.userIcon = userIcon;
    }
    public void setTelephone(String telephone) { this.telephone = telephone; }

    public float getRating(){
        if (ratings == null || ratings.isEmpty()) {
            return 0;
        }

        float sum = 0;
        for (Rating rating : ratings) {
            sum += rating.getValue();
        }

        return sum / ratings.size();
    }

    public int getTotalRatings(){
        return ratings != null ? ratings.size() : 0;
    }

    public String getFcmToken() {
        return fcmToken;
    }

    public void setFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }
}
