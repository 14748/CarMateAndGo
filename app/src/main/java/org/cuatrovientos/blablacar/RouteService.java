package org.cuatrovientos.blablacar;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.model.LatLng;

import org.cuatrovientos.blablacar.activities.MainActivity;
import org.cuatrovientos.blablacar.activities.MapHelper;
import org.cuatrovientos.blablacar.adapters.RecyclerRoutesAdapter;
import org.cuatrovientos.blablacar.models.CustomLatLng;
import org.cuatrovientos.blablacar.models.ORS.ApiService;
import org.cuatrovientos.blablacar.models.ORS.Route;
import org.cuatrovientos.blablacar.models.ORS.RouteInfo;
import org.cuatrovientos.blablacar.models.ORS.RouteResponse;
import org.cuatrovientos.blablacar.models.ORS.Summary;
import org.cuatrovientos.blablacar.models.RouteEntity;
import org.cuatrovientos.blablacar.models.RouteSelectionInfo;
import org.cuatrovientos.blablacar.models.User;
import org.cuatrovientos.blablacar.models.Utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RouteService {
    private final Activity activity;
    private final MapHelper mapHelper;

    public RouteService(Activity activity, MapHelper mapHelper) {
        this.activity = activity;
        this.mapHelper = mapHelper;
    }

    public void routeCreation(User user, CustomLatLng origin, CustomLatLng destination, Date date, RecyclerView recyclerView, LinearLayout linearLayout, String originText, String destinationText) {
        createRoute(origin, destination, new RouteService.RouteCallback() {
            @Override
            public void onRouteReady(RouteInfo routes) {

                mapHelper.drawRoute(Utils.convertListOfCustomLatLngListToListOfLatLngList(routes.getDecodedRoutes()));
                List<RouteSelectionInfo> routeSelectionInfos = new ArrayList<>();
                for (List<String> summary: routes.getSummaries()) {
                    routeSelectionInfos.add(new RouteSelectionInfo("Ruta numero " + routes.getSummaries().indexOf(summary)+1, summary.get(0), summary.get(1)));
                }
                activity.runOnUiThread(() -> {
                    linearLayout.setVisibility(View.VISIBLE);
                    recyclerView.setAdapter(new RecyclerRoutesAdapter(routeSelectionInfos,
                            new RecyclerRoutesAdapter.onItemClickListener() {
                                @Override
                                public void onItemClickListener(RouteSelectionInfo palabra) {
                                    mapHelper.highlightRoute(routeSelectionInfos.indexOf(palabra));
                                }
                            },
                            new RecyclerRoutesAdapter.onLinkClickListener() {
                                @Override
                                public void onLinkClickListener(int position) {
                                    List<String> users = new ArrayList<>();
                                    RouteSelectionInfo routeSelected = routeSelectionInfos.get(position);
                                    RouteEntity r = new RouteEntity(routeSelected.getTime(), routeSelected.getKilometers(), routes.getDecodedRoutes().get(position), 50.00f, users, 5, false, date, origin, destination, originText, destinationText);
                                    mapHelper.map.clear();
                                    user.addCreatedRoute(r);
                                    Utils.pushUser(user);
                                    linearLayout.setVisibility(View.GONE);
                                }
                            }
                    ));
                });

            }

            @Override
            public void onError(Exception e) {
                Log.d("Womp", e.toString());
            }
        });
    }

    public void createRoute(CustomLatLng start, CustomLatLng end, RouteService.RouteCallback callback) {
        CompletableFuture.supplyAsync(() -> {
            try {
                Retrofit retrofit = getRetrofit();
                ApiService service = retrofit.create(ApiService.class);

                String rawJson = String.format(
                        "{" +
                                "\"coordinates\": [" +
                                "[%f,%f]," +
                                "[%f,%f]" +
                                "]," +
                                "\"alternative_routes\": {" +
                                "\"target_count\": 3," +
                                "\"weight_factor\": 1.4," +
                                "\"share_factor\": 0.6" +
                                "}" +
                                "}",
                        start.getLongitude(), start.getLatitude(),
                        end.getLongitude(), end.getLatitude()
                );

                RequestBody body = RequestBody.create(
                        MediaType.parse("application/json; charset=utf-8"),
                        rawJson
                );

                Log.d("Womp", body.toString());
                Log.d("Womp", rawJson);

                Response<RouteResponse> response = service.createRoute(
                        "5b3ce3597851110001cf6248e44784ffb81c49abb564cc056c247289",
                        body
                ).execute();

                if (response.isSuccessful() && response.body() != null) {
                    return response;
                } else {
                    callback.onError(new RuntimeException("Response not successful or empty body"));
                    return null;
                }
            } catch (Exception e) {
                callback.onError(e);
                return null;
            }
        }).thenAccept(response -> {
            if (response != null) {
                RouteInfo route = converRouteResponseToCustomLatLng(response);
                callback.onRouteReady(route);
            }
        });
    }

    private RouteInfo converRouteResponseToCustomLatLng(Response<RouteResponse> response) {
        List<List<CustomLatLng>> allDecodedRoutes = new ArrayList<>();
        List<List<String>> summaries = new ArrayList<>();
        List<Double> distances = new ArrayList<>(); // List to keep track of distances for comparison

        if (response == null || response.body() == null || response.body().getRoutes() == null) {
            return null;
        }

        List<Route> routes = response.body().getRoutes();

        for (Route route : routes) {
            String encodedPolyline = route.getGeometry();
            Summary summary = route.getSummary();
            if (summary != null) {
                double distance = summary.getDistance(); // Assuming this gets the distance in km

                boolean isSimilar = false;
                for (Double d : distances) {
                    if (Math.abs(d - distance) <= 100) { // Check if the distance is within 100 km of any existing route
                        isSimilar = true;
                        break;
                    }
                }

                if (!isSimilar) { // Only add the route if it's not similar to any existing route
                    distances.add(distance); // Add this route's distance for future comparisons

                    List<String> routeSummary = new ArrayList<>();

                    // Ensure the distance is shown up to two decimal places
                    double distanceKm = distance / 1000.0;
                    String formattedDistance = String.format("%.2f", distanceKm);
                    routeSummary.add(formattedDistance);

                    // Assuming summary.getDuration() returns a double value representing seconds
                    double totalSecsDouble = summary.getDuration();
                    int hours = (int) totalSecsDouble / 3600;
                    int minutes = (int) (totalSecsDouble % 3600) / 60;
                    int seconds = (int) totalSecsDouble % 60;

                    // Build the duration string based on the values of hours, minutes, and seconds
                    String formattedDuration;
                    if (hours > 0) {
                        formattedDuration = String.format("%02d:%02d", hours, minutes);
                    } else {
                        formattedDuration = String.format("00:%02d", minutes);
                    }
                    routeSummary.add(formattedDuration);
                    summaries.add(routeSummary);

                    List<LatLng> routeCoordinates = decodePolyline(encodedPolyline);
                    List<CustomLatLng> customRouteCoordinates = new ArrayList<>();
                    for (LatLng latLng : routeCoordinates) {
                        CustomLatLng customLatLng = new CustomLatLng(latLng.latitude, latLng.longitude);
                        customRouteCoordinates.add(customLatLng);
                    }
                    allDecodedRoutes.add(customRouteCoordinates);
                }


            }
        }

        return new RouteInfo(allDecodedRoutes, summaries);
    }

    public List<LatLng> decodePolyline(String encoded) {
        List<LatLng> poly = new ArrayList<>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng(((double) lat / 1E5), ((double) lng / 1E5));
            poly.add(p);
        }
        return poly;
    }
    private Retrofit getRetrofit() {
        return new Retrofit.Builder()
                .baseUrl("https://api.openrouteservice.org/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public interface RouteCallback {
        void onRouteReady(RouteInfo route);
        void onError(Exception e);
    }
}

