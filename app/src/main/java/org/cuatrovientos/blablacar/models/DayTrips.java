package org.cuatrovientos.blablacar.models;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class DayTrips {
    private Date date;
    private List<DriverTrips> trips;

    public DayTrips(Date date, List<DriverTrips> trips) {
        this.date = date;
        this.trips = trips;
    }

    // Getters
    public Date getDate() {
        return date;
    }

    public List<DriverTrips> getTrips() {
        return trips;
    }
}