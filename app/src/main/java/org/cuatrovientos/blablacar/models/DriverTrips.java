package org.cuatrovientos.blablacar.models;

import java.io.Serializable;
import java.util.Date;

public class DriverTrips implements Serializable {
    private User user;
    private RouteEntity route;

    private Date date;

    public DriverTrips() {
    }

    public DriverTrips(User user, RouteEntity route) {
        this.user = user;
        this.route = route;
    }

    public DriverTrips(User user, RouteEntity route, Date date) {
        this.user = user;
        this.route = route;
        this.date = date;
    }

    

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

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

    
    @Override
    public String toString() {
        return "DriverTrips{" +
                "user=" + user +
                ", route=" + route +
                '}';
    }
}
