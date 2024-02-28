package org.cuatrovientos.blablacar.models.ORS;

import com.google.gson.annotations.SerializedName;

public class Summary {
    @SerializedName("distance")
    private double distance;

    @SerializedName("duration")
    private double duration;

    // Getters and setters
    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public double getDuration() {
        return duration;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }
}
