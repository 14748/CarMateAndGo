package org.cuatrovientos.blablacar.activities;

import static android.content.ContentValues.TAG;

import static java.security.AccessController.getContext;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.checkerframework.checker.units.qual.C;
import org.checkerframework.checker.units.qual.Time;
import org.cuatrovientos.blablacar.BalanceActivity;
import org.cuatrovientos.blablacar.R;
import org.cuatrovientos.blablacar.activities.register.LogIn;
import org.cuatrovientos.blablacar.activities.register.MainScreen;
import org.cuatrovientos.blablacar.adapters.RecyclerRoutesAdapter;
import org.cuatrovientos.blablacar.models.CustomLatLng;
import org.cuatrovientos.blablacar.models.ORS.ApiService;
import org.cuatrovientos.blablacar.models.ORS.Route;
import org.cuatrovientos.blablacar.models.ORS.RouteInfo;
import org.cuatrovientos.blablacar.models.ORS.RouteResponse;
import org.cuatrovientos.blablacar.models.ORS.Summary;
import org.cuatrovientos.blablacar.models.PlaceOpenStreetMap;
import org.cuatrovientos.blablacar.models.RouteEntity;
import org.cuatrovientos.blablacar.models.RouteSelectionInfo;
import org.cuatrovientos.blablacar.models.User;
import org.cuatrovientos.blablacar.models.Utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap map;
    private List<Polyline> polylineList = new ArrayList<>();
    private Button button;
    private static LatLng CUATROVIENTOS = new LatLng(42.824851, -1.660318);
    private Button btnCreateRoute;
    private Button btnLogIn;

    private Button btnTest;

    private final Executor executor = Executors.newSingleThreadExecutor();
    private ArrayList<User> users = new ArrayList<>();

    private RecyclerView recyclerView;

    private LinearLayout linearLayout;
    private ActivityResultLauncher<Intent> createRouteLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = findViewById(R.id.button2);
        btnCreateRoute = findViewById(R.id.btnCreateScreen);
        btnLogIn = findViewById(R.id.bntLogIn);
        btnTest = findViewById(R.id.buttontest);
        linearLayout = findViewById(R.id.linearLayout);

        initCreateRouteLauncher();

        recyclerView = findViewById(R.id.routesRecyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this, 1));

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

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
                createRouteLauncher.launch(intent);
            }
        });

        btnLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MainScreen.class);
                startActivity(intent);
            }
        });

        btnTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RouteFinderActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initCreateRouteLauncher() {
        createRouteLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        Intent data = result.getData();
                        // Handle the received data as before
                        PlaceOpenStreetMap origin = (PlaceOpenStreetMap) data.getSerializableExtra("travelPoint");
                        String date = data.getStringExtra("date");
                        Toast.makeText(MainActivity.this, "heyy", Toast.LENGTH_LONG).show();

                        double originLatitude = Double.parseDouble(origin.getLat());
                        double originLongitude = Double.parseDouble(origin.getLon());

                        // Assuming User and RouteEntity classes are set up to handle these types correctly
                        routeCreation(new User(), new RouteEntity(new CustomLatLng(originLatitude, originLongitude)));
                    }
                });
    }

    public void getUsers() {
        executor.execute(() -> {
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
                                    runOnUiThread(() -> drawRoute(routesForDrawing));
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
        });
    }
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        map = googleMap;

        map.moveCamera(CameraUpdateFactory.newLatLngZoom(CUATROVIENTOS, 13.5f));
        map.addMarker(new MarkerOptions().position(CUATROVIENTOS).title("Cuatrovientos"));
        }
    private void highlightRoute(int selectedRouteIndex) {
        final int selectedZIndex = 10; // Higher zIndex for selected route
        final int defaultZIndex = 0; // Default zIndex for unselected routes

        for (int i = 0; i < polylineList.size(); i++) {
            Polyline polyline = polylineList.get(i);
            // Assuming each route has a border and main line, adjust the index calculation as necessary
            int routeIndex = i / 2; // Adjust based on your actual storage logic
            if (routeIndex == selectedRouteIndex) {
                // This is the selected route
                if (i % 2 == 0) { // Assuming even indices are borders
                    polyline.setColor(Color.parseColor("#0F26F5")); // mainBlueBorder for borders
                    polyline.setZIndex(selectedZIndex);
                } else {
                    polyline.setColor(Color.parseColor("#0F53FF")); // mainBlue for the main line
                    polyline.setZIndex(selectedZIndex);
                }
            } else {
                // This is not the selected route
                if (i % 2 == 0) { // Assuming even indices are borders
                    polyline.setColor(Color.parseColor("#6A83D7")); // subtleBlueBorder for borders
                    polyline.setZIndex(defaultZIndex);
                } else {
                    polyline.setColor(Color.parseColor("#BCCEFB")); // subtleBlue for the main line
                    polyline.setZIndex(defaultZIndex);
                }
            }
        }
    }

    public void routeCreation(User user, RouteEntity route) {
        createRoute(route.getTravelPoint(), new RouteCallback() {
            @Override
            public void onRouteReady(RouteInfo routes) {
                drawRoute(Utils.convertListOfCustomLatLngListToListOfLatLngList(routes.getDecodedRoutes()));
                List<RouteSelectionInfo> routeSelectionInfos = new ArrayList<>();
                for (List<String> summary: routes.getSummaries()) {
                    routeSelectionInfos.add(new RouteSelectionInfo("Ruta numero " + routes.getSummaries().indexOf(summary)+1, summary.get(0), summary.get(1)));
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        recyclerView.setAdapter(new RecyclerRoutesAdapter(routeSelectionInfos,
                                new RecyclerRoutesAdapter.onItemClickListener() {
                                    @Override
                                    public void onItemClickListener(RouteSelectionInfo palabra) {
                                        highlightRoute(routeSelectionInfos.indexOf(palabra));
                                    }
                                },
                                new RecyclerRoutesAdapter.onLinkClickListener() {
                                    @Override
                                    public void onLinkClickListener(int position) {
                                        Toast.makeText(MainActivity.this, "test", Toast.LENGTH_SHORT).show();
                                        //TODO: post user route to firebase
                                        List<RouteEntity> userRoutes = new ArrayList<>();
                                        // Add RouteEntity objects to userRoutes as needed

                                        Drawable userIcon = ContextCompat.getDrawable(MainActivity.this, R.drawable.arrowderecha);;
                                        User newUser = new User(
                                                1, // id
                                                "John", // name
                                                "Doe", // lastName
                                                new Date(), // birthDate
                                                "johndoe@example.com", // email
                                                1234567890, // telephone
                                                "securepassword123", // password
                                                userRoutes, // List of RouteEntity objects
                                                userIcon // Drawable for user icon
                                        );
                                        List<User> users = new ArrayList<>();
                                        users.add(new User());
                                        RouteSelectionInfo routeSelected = routeSelectionInfos.get(position);
                                        RouteEntity r = new RouteEntity(0, route.getTravelPoint(), routeSelected.getTime(), routeSelected.getKilometers(), routes.getDecodedRoutes().get(position), 1.0f, users, 5, false, new Date());
                                        map.clear();
                                        user.addRoute(r);
                                        Utils.pushUser(user);
                                    }
                                }
                        ));
                    }
                });

            }

            @Override
            public void onError(Exception e) {
                // Handle error here
            }
        });
    }

    public interface RouteCallback {
        void onRouteReady(RouteInfo route);
        void onError(Exception e);
    }

    public void createRoute(CustomLatLng end, RouteCallback callback) {
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
                        end.getLongitude(), end.getLatitude()
                );

                RequestBody body = RequestBody.create(
                        MediaType.parse("application/json; charset=utf-8"),
                        rawJson
                );

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
                    routeSummary.add("Distancia: " + formattedDistance + " km");

                    // Assuming summary.getDuration() returns a double value representing seconds
                    double totalSecsDouble = summary.getDuration();
                    int hours = (int) totalSecsDouble / 3600;
                    int minutes = (int) (totalSecsDouble % 3600) / 60;
                    int seconds = (int) totalSecsDouble % 60;

                    // Build the duration string based on the values of hours, minutes, and seconds
                    String formattedDuration;
                    if (hours > 0) {
                        formattedDuration = String.format("%d h %02d min %02d sec", hours, minutes, seconds);
                    } else if (minutes > 0) {
                        formattedDuration = String.format("%d min %02d sec", minutes, seconds);
                    } else {
                        formattedDuration = String.format("%d sec", seconds);
                    }
                    routeSummary.add("Duracion: " + formattedDuration);
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



    private void drawRoute(List<List<LatLng>> routes) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {
            List<PolylineOptions> polylineOptionsList = new ArrayList<>();
            for (List<LatLng> route : routes) {
                int index = routes.indexOf(route);
                PolylineOptions borderPolylineOptions = createPolylineOptions(route, index, true);
                PolylineOptions polylineOptions = createPolylineOptions(route, index, false);
                polylineOptionsList.add(borderPolylineOptions);
                polylineOptionsList.add(polylineOptions);
            }

            handler.post(() -> {
                if (map != null) {
                    polylineList.clear(); // Clear previous polylines if you're redrawing them
                    for (PolylineOptions options : polylineOptionsList) {
                        Polyline polyline = map.addPolyline(options);
                        polylineList.add(polyline);
                    }
                }
            });

        });
    }

    private PolylineOptions createPolylineOptions(List<LatLng> routeCoordinates, int index, boolean isBorder) {
        int mainBlue = Color.parseColor("#0F53FF");
        int mainBlueBorder = Color.parseColor("#0F26F5");
        int subtleBlue = Color.parseColor("#BCCEFB");
        int subtleBlueBorder = Color.parseColor("#6A83D7");

        int routeColor = index != 0 ? subtleBlue : mainBlue;
        int borderColor = index != 0 ? subtleBlueBorder : mainBlueBorder;

        PolylineOptions polylineOptions = new PolylineOptions();
        polylineOptions.addAll(routeCoordinates);
        polylineOptions.zIndex(index == 0 ? 1 : 0);
        if (isBorder) {
            polylineOptions.width(30).color(borderColor);
        } else {
            polylineOptions.width(25).color(routeColor);
        }
        return polylineOptions;
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