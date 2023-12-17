package org.cuatrovientos.blablacar.models.ORS;

import org.cuatrovientos.blablacar.models.ORS.RouteResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {

    @GET("/v2/directions/driving-car")
    Call<RouteResponse> getRoute(
            @Query("api_key") String key,
            @Query(value = "start", encoded = true) String start,
            @Query(value = "end", encoded = true) String end
    );
}
