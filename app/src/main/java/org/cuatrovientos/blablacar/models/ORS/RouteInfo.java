package org.cuatrovientos.blablacar.models.ORS;

import org.cuatrovientos.blablacar.models.CustomLatLng;

import java.util.List;

public class RouteInfo {
    private List<List<CustomLatLng>> decodedRoutes;
    private List<List<String>> summaries;

    public RouteInfo(List<List<CustomLatLng>> decodedRoutes, List<List<String>> summaries) {
        this.decodedRoutes = decodedRoutes;
        this.summaries = summaries;
    }

    public List<List<CustomLatLng>> getDecodedRoutes() {
        return decodedRoutes;
    }

    public List<List<String>> getSummaries() {
        return summaries;
    }
}
