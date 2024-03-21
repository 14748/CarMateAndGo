package org.cuatrovientos.blablacar.models;

public class RouteSelectionInfo {
    private String title;
    private String kilometers;
    private String time;

    public RouteSelectionInfo(String title, String kilometers, String time) {
        this.title = title;
        this.kilometers = kilometers;
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public String getKilometers() {
        return kilometers;
    }

    public String getTime() {
        return time;
    }
}

