package org.cuatrovientos.blablacar.models.ORS;

import com.google.gson.annotations.SerializedName;

import org.cuatrovientos.blablacar.models.ORS.Feature;

import java.util.List;

public class RouteResponse {

    @SerializedName("features")
    private List<Feature> features;

    public RouteResponse(List<Feature> features) {
        this.features = features;
    }

    public List<Feature> getFeatures() {
        return features;
    }
}
