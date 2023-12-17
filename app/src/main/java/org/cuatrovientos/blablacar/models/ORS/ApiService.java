package org.cuatrovientos.blablacar.models.ORS;

import org.cuatrovientos.blablacar.models.ORS.RouteResponse;

import java.util.concurrent.Executor;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiService {
    @POST("/v2/directions/driving-car")
    Call<RouteResponse> createRoute(
            @Header("Authorization") String apiKey,
            @Body RequestBody routeRequest
    );
}
