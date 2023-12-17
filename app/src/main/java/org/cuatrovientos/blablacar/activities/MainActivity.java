package org.cuatrovientos.blablacar.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.cuatrovientos.blablacar.R;
import org.cuatrovientos.blablacar.models.ORS.ApiService;
import org.cuatrovientos.blablacar.models.ORS.Route;
import org.cuatrovientos.blablacar.models.ORS.RouteResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {
    private  GoogleMap map;

    private String styleJson = "[" + "{" + "\"elementType\": \"geometry\"," + "\"stylers\": [" + "{" + "\"color\": \"#212121\"" + "}" + "]" + "}," + "{" + "\"elementType\": \"labels.icon\"," + "\"stylers\": [" + "{" + "\"visibility\": \"off\"" + "}" + "]" + "}," + "{" + "\"elementType\": \"labels.text.fill\"," + "\"stylers\": [" + "{" + "\"color\": \"#757575\"" + "}" + "]" + "}," + "{" + "\"elementType\": \"labels.text.stroke\"," + "\"stylers\": [" + "{" + "\"color\": \"#212121\"" + "}" + "]" + "}," + "{" + "\"featureType\": \"administrative\"," + "\"elementType\": \"geometry\"," + "\"stylers\": [" + "{" + "\"color\": \"#757575\"" + "}" + "]" + "}," + "{" + "\"featureType\": \"administrative.country\"," + "\"elementType\": \"labels.text.fill\"," + "\"stylers\": [" + "{" + "\"color\": \"#9e9e9e\"" + "}" + "]" + "}," + "{" + "\"featureType\": \"administrative.land_parcel\"," + "\"stylers\": [" + "{" + "\"visibility\": \"off\"" + "}" + "]" + "}," + "{" + "\"featureType\": \"administrative.locality\"," + "\"elementType\": \"labels.text.fill\"," + "\"stylers\": [" + "{" + "\"color\": \"#bdbdbd\"" + "}" + "]" + "}," + "{" + "\"featureType\": \"poi\"," + "\"elementType\": \"labels.text.fill\"," + "\"stylers\": [" + "{" + "\"color\": \"#757575\"" + "}" + "]" + "}," + "{" + "\"featureType\": \"poi.park\"," + "\"elementType\": \"geometry\"," + "\"stylers\": [" + "{" + "\"color\": \"#181818\"" + "}" + "]" + "}," + "{" + "\"featureType\": \"poi.park\"," + "\"elementType\": \"labels.text.fill\"," + "\"stylers\": [" + "{" + "\"color\": \"#616161\"" + "}" + "]" + "}," + "{" + "\"featureType\": \"poi.park\"," + "\"elementType\": \"labels.text.stroke\"," + "\"stylers\": [" + "{" + "\"color\": \"#1b1b1b\"" + "}" + "]" + "}," + "{" + "\"featureType\": \"road\"," + "\"elementType\": \"geometry.fill\"," + "\"stylers\": [" + "{" + "\"color\": \"#2c2c2c\"" + "}" + "]" + "}," + "{" + "\"featureType\": \"road\"," + "\"elementType\": \"labels.text.fill\"," + "\"stylers\": [" + "{" + "\"color\": \"#8a8a8a\"" + "}" + "]" + "}," + "{" + "\"featureType\": \"road.arterial\"," + "\"elementType\": \"geometry\"," + "\"stylers\": [" + "{" + "\"color\": \"#373737\"" + "}" + "]" + "}," + "{" + "\"featureType\": \"road.highway\"," + "\"elementType\": \"geometry\"," + "\"stylers\": [" + "{" + "\"color\": \"#3c3c3c\"" + "}" + "]" + "}," + "{" + "\"featureType\": \"road.highway.controlled_access\"," + "\"elementType\": \"geometry\"," + "\"stylers\": [" + "{" + "\"color\": \"#4e4e4e\"" + "}" + "]" + "}," + "{" + "\"featureType\": \"road.local\"," + "\"elementType\": \"labels.text.fill\"," + "\"stylers\": [" + "{" + "\"color\": \"#616161\"" + "}" + "]" + "}," + "{" + "\"featureType\": \"transit\"," + "\"elementType\": \"labels.text.fill\"," + "\"stylers\": [" + "{" + "\"color\": \"#757575\"" + "}" + "]" + "}," + "{" + "\"featureType\": \"water\"," + "\"elementType\": \"geometry\"," + "\"stylers\": [" + "{" + "\"color\": \"#000000\"" + "}" + "]" + "}," + "{" + "\"featureType\": \"water\"," + "\"elementType\": \"labels.text.fill\"," + "\"stylers\": [" + "{" + "\"color\": \"#3d3d3d\"" + "}" + "]" + "}" + "]";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        map = googleMap;

        map.setMapStyle(new MapStyleOptions(styleJson));

        LatLng cuatrovientos = new LatLng(42.824851, -1.660318);

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(cuatrovientos);
        markerOptions.title("Instituto CuatroVientos");

        Drawable myDrawable = getResources().getDrawable(R.drawable.markerp);
        float scaleWidth = myDrawable.getIntrinsicWidth() * 0.2f;
        float scaleHeight = myDrawable.getIntrinsicHeight() * 0.2f;
        Bitmap myLogo = Bitmap.createBitmap((int) scaleWidth, (int) scaleHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(myLogo);
        myDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        myDrawable.draw(canvas);

        BitmapDescriptor markerIcon = BitmapDescriptorFactory.fromBitmap(myLogo);

        markerOptions.icon(markerIcon);
        map.addMarker(markerOptions);

        map.moveCamera(CameraUpdateFactory.newLatLngZoom(cuatrovientos, 15.0f));

        createRoute();
    }
    public void createRoute() {
        CompletableFuture.supplyAsync(() -> {
            try {
                Retrofit retrofit = getRetrofit();
                ApiService service = retrofit.create(ApiService.class);

                String rawJson = "{" + "\"coordinates\": [" + "[-1.633844,42.833349]," + "[-1.660318,42.824851]" + "]," + "\"alternative_routes\": {" + "\"target_count\": 3," + "\"weight_factor\": 1.4," + "\"share_factor\": 0.6" + "}" + "}";

                RequestBody body = RequestBody.create(
                        MediaType.parse("application/json; charset=utf-8"),
                        rawJson
                );

                Response<RouteResponse> response = service.createRoute(
                        "5b3ce3597851110001cf6248e44784ffb81c49abb564cc056c247289",
                        body
                ).execute();

                return response;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }).thenAccept(response -> {
            if (response != null && response.isSuccessful() && response.body() != null) {
                drawRoute(response);
            } else {
                // Handle failure
            }
        });
    }

    private void drawRoute(Response<RouteResponse> response) {
        if (response == null || response.body() == null || response.body().getRoutes() == null) {
            return;
        }

        List<Route> routes = response.body().getRoutes();

        for (int i = 0; i < routes.size(); i++) {
            Route route = routes.get(i);

            String encodedPolyline = route.getGeometry();
            List<LatLng> routeCoordinates = decodePolyline(encodedPolyline);

            if (routeCoordinates.isEmpty()) {
                continue;
            }

            int index = routes.indexOf(route);
            int routeColor = index != 0 ? Color.GRAY : Color.WHITE;
            int borderColor = Color.WHITE;

            addPolylineToMap(routeCoordinates, routeColor, borderColor, index);
        }
    }

    private void addPolylineToMap(List<LatLng> routeCoordinates, int routeColor, int borderColor, int index) {
        // Border Polyline
        PolylineOptions borderPolylineOptions = new PolylineOptions();
        borderPolylineOptions.width(40); // Border width
        borderPolylineOptions.color(borderColor);
        borderPolylineOptions.addAll(routeCoordinates);
        borderPolylineOptions.zIndex(index == 0 ? 1 : 0);

        // Actual Route Polyline
        PolylineOptions polylineOptions = new PolylineOptions();
        polylineOptions.width(30);
        polylineOptions.color(routeColor);
        polylineOptions.addAll(routeCoordinates);
        polylineOptions.zIndex(index == 0 ? 1 : 0);

        runOnUiThread(() -> {
            if (map != null) {
                map.addPolyline(borderPolylineOptions);
                map.addPolyline(polylineOptions);
            }
        });
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
}


