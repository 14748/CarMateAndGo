package org.cuatrovientos.blablacar.models;

import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;

public class Rating implements Serializable {
    private String id; // Unique ID for the rating
    private String userId;
    private float value; // The rating value, e.g., 4.5
    private String comment; // Optional comment with the rating
    private RouteEntity route; // Timestamp for when the rating was made

    // Default constructor required for Firebase and other ORMs
    public Rating() {
    }

    // Constructor with parameters
    public Rating(float value, String comment, RouteEntity route, String userId) {
        this.id = FirebaseDatabase.getInstance().getReference().push().getKey();
        this.value = value;
        this.comment = comment;
        this.route = route;
        this.userId = userId;
    }

    // Getters
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public RouteEntity getRoute() {
        return route;
    }

    public void setRoute(RouteEntity route) {
        this.route = route;
    }
}

