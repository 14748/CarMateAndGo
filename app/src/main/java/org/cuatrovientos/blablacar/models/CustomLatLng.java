package org.cuatrovientos.blablacar.models;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;
import java.util.Objects;

public class CustomLatLng implements Serializable {
    private double latitude;
    private double longitude;

    
    public CustomLatLng() {
    }

    public CustomLatLng(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public LatLng toLatLng() {
        return new LatLng(latitude, longitude);
    }

    public static CustomLatLng fromLatLng(LatLng latLng) {
        return new CustomLatLng(latLng.latitude, latLng.longitude);
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CustomLatLng that = (CustomLatLng) o;
        return Double.compare(that.latitude, latitude) == 0 &&
                Double.compare(that.longitude, longitude) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(latitude, longitude);
    }
}

