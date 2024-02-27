package org.cuatrovientos.blablacar.services;

import org.cuatrovientos.blablacar.models.ORS.Route;
import org.cuatrovientos.blablacar.models.PlaceOpenStreetMap;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface OpenStreetMap {

    @GET("/search")
    Call<List<PlaceOpenStreetMap>> searchPlaces(
            @Query("q") String query,
            @Query("format") String format
    );

}

