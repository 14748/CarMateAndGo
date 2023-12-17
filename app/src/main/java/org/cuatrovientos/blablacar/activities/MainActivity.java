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
import org.cuatrovientos.blablacar.models.ORS.RouteResponse;

import java.util.List;
import java.util.concurrent.CompletableFuture;

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

        LatLng cuatrovientos = new LatLng(42.8243912, -1.6609128);

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
                Response<RouteResponse> response = service.getRoute(
                        "5b3ce3597851110001cf6248e44784ffb81c49abb564cc056c247289",
                        "-1.610295,42.830287",
                        "-1.660214,42.824893").execute();

                return response;
            } catch (Exception e) {
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

    private void drawRoute(Response<RouteResponse> response){
        List<List<Double>> routeCoordinates = response.body().getFeatures().get(0).getGeometry().getCoordinates();

        PolylineOptions polylineOptions = new PolylineOptions();
        polylineOptions.width(10);
        polylineOptions.color(Color.RED);

        for (List<Double> coordinatePair : routeCoordinates) {
            LatLng point = new LatLng(coordinatePair.get(1), coordinatePair.get(0));
            polylineOptions.add(point);
        }

        runOnUiThread(() -> {
            map.addPolyline(polylineOptions);
        });
    }
    private Retrofit getRetrofit() {
        return new Retrofit.Builder()
                .baseUrl("https://api.openrouteservice.org/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
}


