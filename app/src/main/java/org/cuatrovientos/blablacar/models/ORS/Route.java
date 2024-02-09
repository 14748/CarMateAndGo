package org.cuatrovientos.blablacar.models.ORS;

import com.google.gson.annotations.SerializedName;

public class Route {
    @SerializedName("geometry")
    private String geometry;

    public String getGeometry() {
        return geometry;
    }
}
