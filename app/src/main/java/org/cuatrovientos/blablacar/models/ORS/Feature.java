package org.cuatrovientos.blablacar.models.ORS;

import com.google.gson.annotations.SerializedName;

public class Feature {

    @SerializedName("geometry")
    private Geometry geometry;

    public Feature(Geometry geometry) {
        this.geometry = geometry;
    }

    public Geometry getGeometry() {
        return geometry;
    }
}