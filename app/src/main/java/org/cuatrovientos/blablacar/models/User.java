package org.cuatrovientos.blablacar.models;

import android.graphics.drawable.Drawable;
import android.os.Parcelable;

import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class User {

    private String id;
    private String name;
    private String lastName;
    private Date birthDate;
    private String email;
    private int telephone;
    private String password;
    private List<RouteEntity> routes;
    private Drawable userIcon;
    private List<Integer> ratings;
    public User(){this.routes = new ArrayList<>();} //por si se necesita

    public User(String name, String lastName, Date birthDate, String email, String password) {
        this.id = FirebaseDatabase.getInstance().getReference().push().getKey();
        this.name = name;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.email = email;
        this.password = password;
        this.routes = new ArrayList<>();
        this.ratings = new ArrayList<>();
    }

    public User(String name, String lastName, Date birthDate, String email, int telephone, String password, List<RouteEntity> routes, Drawable userIcon, List<Integer> ratings) {
        this.id = FirebaseDatabase.getInstance().getReference().push().getKey();
        this.name = name;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.email = email;
        this.telephone = telephone;
        this.password = password;
        this.routes = routes;
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

    public String getPassword() {
        return password;
    }

    public List<RouteEntity> getRoutes() {
        return routes;
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

    public void addRoute(RouteEntity route) {
        this.routes.add(route);
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
}
