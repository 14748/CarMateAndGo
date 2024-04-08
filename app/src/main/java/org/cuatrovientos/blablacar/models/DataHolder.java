package org.cuatrovientos.blablacar.models;

import java.util.List;

public class DataHolder {
    private static final DataHolder instance = new DataHolder();
    private DriverTrips yourData;

    private DataHolder() {}

    public static DataHolder getInstance() {
        return instance;
    }

    public DriverTrips getYourData() {
        return yourData;
    }

    public void setYourData(DriverTrips yourData) {
        this.yourData = yourData;
    }
}

