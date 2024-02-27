package org.cuatrovientos.blablacar.activities;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.cuatrovientos.blablacar.BalanceActivity;
import org.cuatrovientos.blablacar.R;
import org.cuatrovientos.blablacar.models.ORS.ApiService;
import org.cuatrovientos.blablacar.models.ORS.Route;
import org.cuatrovientos.blablacar.models.ORS.RouteResponse;
import org.cuatrovientos.blablacar.models.RouteEntity;
import org.cuatrovientos.blablacar.models.User;
import org.cuatrovientos.blablacar.models.Utils;

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
    private Button button;
    private static LatLng CUATROVIENTOS = new LatLng(42.824851, -1.660318);
    private Button btnCreateRoute;
    private ArrayList<User> users = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toast.makeText(MainActivity.this, "yo", Toast.LENGTH_LONG).show();
        button = findViewById(R.id.button2);
        btnCreateRoute = findViewById(R.id.btnCreateScreen);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        getUsers();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, BalanceActivity.class);
                startActivity(intent);
            }
        });

        btnCreateRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CreateRoute.class);
                startActivity(intent);
            }
        });
    }

    public void getUsers() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference usersRef = database.getReference("Users");

        usersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                        User user = userSnapshot.getValue(User.class);
                        if (user != null && user.getRoutes() != null) {
                            List<List<LatLng>> routesForDrawing = new ArrayList<>();
                            for (RouteEntity routeEntity : user.getRoutes()) {
                                if (routeEntity != null && routeEntity.getPoints() != null) {
                                    List<LatLng> singleRouteLatLng = Utils.convertCustomLatLngListToLatLngList(routeEntity.getPoints());
                                    if (singleRouteLatLng != null) {
                                        routesForDrawing.add(singleRouteLatLng);
                                    }
                                }
                            }
                            if (!routesForDrawing.isEmpty()) {
                                drawRoute(routesForDrawing);
                            }
                        }
                        if (user != null) {
                            users.add(user);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "Data could not be retrieved " + databaseError.getMessage());
            }
        });
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        map = googleMap;

        map.moveCamera(CameraUpdateFactory.newLatLngZoom(CUATROVIENTOS, 15.0f));
        map.addMarker(new MarkerOptions().position(CUATROVIENTOS).title("Cuatrovientos"));
    }
    public void createRoute(LatLng end) {
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
                        CUATROVIENTOS.longitude, CUATROVIENTOS.latitude,
                        end.longitude, end.latitude
                );

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
                drawRoute(converRouteResponseToCustomLatLng(response));
            } else {
                // Handle failure
            }
        });
    }


    private List<List<LatLng>> converRouteResponseToCustomLatLng(Response<RouteResponse> response) {
        List<List<LatLng>> allDecodedRoutes = new ArrayList<>();

        if (response == null || response.body() == null || response.body().getRoutes() == null) {
            return null;
        }

        List<Route> routes = response.body().getRoutes();

        for (Route route : routes) {
            String encodedPolyline = route.getGeometry();
            List<LatLng> routeCoordinates = decodePolyline(encodedPolyline);
            allDecodedRoutes.add(routeCoordinates);
        }

        return allDecodedRoutes;
    }


    private void drawRoute(List<List<LatLng>> routes) {
            MarkerOptions markerOptions = new MarkerOptions();
            //TODO: we need starting point variable on Model
            markerOptions.position(routes.get(0).get(routes.get(0).size() - 1));

            //Modificamos el icono para mostrar
            Drawable myDrawable = getResources().getDrawable(R.drawable.person);
            float scaleWidth = myDrawable.getIntrinsicWidth() * 0.2f;
            float scaleHeight = myDrawable.getIntrinsicHeight() * 0.2f;
            Bitmap myLogo = Bitmap.createBitmap((int) scaleWidth, (int) scaleHeight, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(myLogo);
            myDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            myDrawable.draw(canvas);
            BitmapDescriptor markerIcon = BitmapDescriptorFactory.fromBitmap(myLogo);

            markerOptions.icon(markerIcon).anchor(0.5f, 0.5f);
            map.addMarker(markerOptions);

        for (List<LatLng> route : routes) {

            for (int i = 0; i < route.size(); i++) {

                int mainBlue = Color.parseColor("#0F53FF");
                int mainBlueBorder = Color.parseColor("#0F26F5");
                int subtleBlue = Color.parseColor("#BCCEFB");
                int subtleBlueBorder = Color.parseColor("#6A83D7");
                int index = routes.indexOf(route);
                int routeColor = index != 0 ? subtleBlue : mainBlue;
                int borderColor = index != 0 ? subtleBlueBorder : mainBlueBorder;

                addPolylineToMap(route, routeColor, borderColor, index);
            }
        }
    }



    private void addPolylineToMap(List<LatLng> routeCoordinates, int routeColor, int borderColor, int index) {
        // Border Polyline
        PolylineOptions borderPolylineOptions = new PolylineOptions();
        borderPolylineOptions.width(30);
        borderPolylineOptions.color(borderColor);
        borderPolylineOptions.addAll(routeCoordinates);
        borderPolylineOptions.zIndex(index == 0 ? 1 : 0);

        // Actual Route Polyline
        PolylineOptions polylineOptions = new PolylineOptions();
        polylineOptions.width(25);
        polylineOptions.color(routeColor);
        polylineOptions.addAll(routeCoordinates);
        polylineOptions.zIndex(index == 0 ? 1 : 0);

        if (map != null) {
            map.addPolyline(borderPolylineOptions);
            map.addPolyline(polylineOptions);
        }
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


