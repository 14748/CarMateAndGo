package org.cuatrovientos.blablacar.models;

import java.io.Serializable;

public class DriverTrips implements Serializable {
    private User user;
    private RouteEntity route;

    public DriverTrips() {
    }

    public DriverTrips(User user, RouteEntity route) {
        this.user = user;
        this.route = route;
    }

    // Getters y Setters
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public RouteEntity getRoute() {
        return route;
    }

    public void setRoute(RouteEntity route) {
        this.route = route;
    }

    // Método toString
    @Override
    public String toString() {
        return "DriverTrips{" +
                "user=" + user +
                ", route=" + route +
                '}';
    }
}
