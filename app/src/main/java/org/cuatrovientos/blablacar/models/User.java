package org.cuatrovientos.blablacar.models;

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
    private int telephone;
    private String password;
    private List<RouteEntity> createdRoutes;
    private List<RouteEntity> passengerRoutes;
    private Drawable userIcon;
    private List<Integer> ratings;

    private int balance;
    public User(){
        this.createdRoutes = new ArrayList<>();
        this.passengerRoutes = new ArrayList<>();
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
    }

    public User(String name, String lastName, Date birthDate, String email, int telephone, String password, Drawable userIcon, List<Integer> ratings) {
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
        this.ratings = ratings;
    }

    /*
    * GETTERS
    * */
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

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public String getPassword() {
        return password;
    }

    public List<RouteEntity> getCreatedRoutes() {
        return createdRoutes;
    }

    public void setCreatedRoutes(List<RouteEntity> createdRoutes) {
        this.createdRoutes = createdRoutes;
    }

    public List<RouteEntity> getPassengerRoutes() {
        return passengerRoutes;
    }

    public void setPassengerRoutes(List<RouteEntity> passengerRoutes) {
        this.passengerRoutes = passengerRoutes;
    }

    public List<Integer> getRatings() {
        return ratings;
    }

    public void setRatings(List<Integer> ratings) {
        this.ratings = ratings;
    }

    public Drawable getUserIcon() {
        return userIcon;
    }

    public int getTelephone(){ return telephone;}

    /*
    * SETTERS (posible necesidad futura de tener que borrar alguno por seguridad)
    * */

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addCreatedRoute(RouteEntity route) {
            this.createdRoutes.add(route);
    }

    public void addPassengerRoute(RouteEntity route) {
            this.passengerRoutes.add(route);
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
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

    public void setUserIcon(Drawable userIcon) {
        this.userIcon = userIcon;
    }
    public void setTelephone(int telephone) { this.telephone = telephone; }

    public float getRating(){
        if (ratings == null || ratings.isEmpty()) {
            return 0;
        }

        float sum = 0;
        for (Integer rating : ratings) {
            sum += rating;
        }

        return sum / ratings.size();
    }

    public int getTotalRatings(){
        return ratings != null ? ratings.size() : 0;
    }
}
