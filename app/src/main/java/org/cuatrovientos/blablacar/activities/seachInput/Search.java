package org.cuatrovientos.blablacar.activities.seachInput;

import static android.content.ContentValues.TAG;
import static android.widget.Toast.makeText;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.cuatrovientos.blablacar.R;
import org.cuatrovientos.blablacar.activities.create.CreateRoute;
import org.cuatrovientos.blablacar.activities.search.SearchRoutes;
import org.cuatrovientos.blablacar.activities.seachInput.adapter.RouteAdapter;
import org.cuatrovientos.blablacar.models.PlaceOpenStreetMap;
import org.cuatrovientos.blablacar.services.OpenStreetMap;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Search extends AppCompatActivity {

    EditText search;
    LinearLayout setCurrentLocation;
    RouteAdapter adapter;
    private TextView errorTextView;
    String type;
    private FusedLocationProviderClient fusedLocationClient;
    ArrayList<PlaceOpenStreetMap> results = new ArrayList<>();

    RecyclerView searchResults;
    private static final int PERMISSION_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        search = findViewById(R.id.searchbutton);
        setCurrentLocation = findViewById(R.id.btnSetCurrentLocation);
        searchResults = findViewById(R.id.searchResults);
        errorTextView = findViewById(R.id.errorTextView);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);






        setCurrentLocation.setOnClickListener(e -> {
            
            if (checkLocationPermission()) {
                obtenerUbicacionActual();

            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        PERMISSION_REQUEST_CODE);
            }
        });





        
        searchResults.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RouteAdapter(results);
        searchResults.setAdapter(adapter);

        


        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                handler.removeCallbacks(searchRunnable);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                handler.removeCallbacks(searchRunnable);
            }


            @Override
            public void afterTextChanged(Editable s) {
                Log.d(TAG, "afterTextChanged: "+s.toString());
                handler.postDelayed(searchRunnable, 250);
            }
        });


        type = getIntent().getStringExtra("type");

        
        adapter.setOnItemClickListener(position -> {
            PlaceOpenStreetMap place = results.get(position);
            
            
            if (type != null) {
                if (type.equals("origin")) {
                    Intent intent = new Intent(this, CreateRoute.class);
                    intent.putExtra("origin", place);
                    setResult(RESULT_OK, intent);
                    finish();
                } else if (type.equals("destination")) {
                    Intent intent = new Intent(this, CreateRoute.class);
                    intent.putExtra("destination", place);
                    setResult(RESULT_OK, intent);
                    finish();

                } else if (type.equals("origin-search")) {
                    Intent intent = new Intent(this, SearchRoutes.class);
                    intent.putExtra("origin", place);
                    setResult(RESULT_OK, intent);
                    finish();
                } else if (type.equals("destination-search")) {
                    Intent intent = new Intent(this, SearchRoutes.class);
                    intent.putExtra("destination", place);
                    setResult(RESULT_OK, intent);
                    finish();

                }
            }
            
        });







    }


    private void buscarLugares(String query) {
        
        
        
        String apiUrl = "https://nominatim.openstreetmap.org/";


        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(apiUrl)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

         OpenStreetMap service = retrofit.create(OpenStreetMap.class);
         Call<List<PlaceOpenStreetMap>> call = service.searchPlaces(query, "jsonv2");

        call.enqueue(new Callback<List<PlaceOpenStreetMap>>() {
            @Override
            public void onResponse(Call<List<PlaceOpenStreetMap>> call, Response<List<PlaceOpenStreetMap>> response) {
                if (response.isSuccessful()) {
                    List<PlaceOpenStreetMap> routes = response.body();
                    if (routes != null && !routes.isEmpty()) {
                        searchResults.setVisibility(View.VISIBLE);
                        Log.d(TAG, "onResponse() returned: " + routes.toString());
                        results.clear();
                        results.addAll(routes);
                        adapter.notifyDataSetChanged();
                        
                        errorTextView.setVisibility(View.GONE);
                    } else {
                        Log.e(TAG, "Respuesta vacía o no contiene un array de lugares");
                        errorTextView.setText("No se encontraron ubicaciones");
                        searchResults.setVisibility(View.GONE);
                        errorTextView.setVisibility(View.VISIBLE);
                    }
                } else {
                    try {
                        
                        JSONObject errorObject = new JSONObject(response.errorBody().string());
                        String errorMessage = errorObject.getString("message");
                        Log.e(TAG, "Error en la respuesta de la solicitud: " + errorMessage);
                    } catch (Exception e) {
                        
                        Log.e(TAG, "Error en la respuesta de la solicitud: " + response.message());
                    }
                }
            }

            @Override
            public void onFailure(Call<List<PlaceOpenStreetMap>> call, Throwable t) {
                Log.e(TAG, "Error en la solicitud: " + t.getMessage());
                t.printStackTrace();
            }
        });
        
    }

    private final Handler handler = new Handler(Looper.getMainLooper());
    private final Runnable searchRunnable = new Runnable() {
        @Override
        public void run() {
            buscarLugares(search.getText().toString());
        }
    };


    private boolean checkLocationPermission() {
        int permission = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        return permission == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                obtenerUbicacionActual();
            } else {
                
            }
        }
    }

    private void obtenerUbicacionActual() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Permisos de ubicacion denegados. Habilita ubicacion en tu dispositivo", Toast.LENGTH_SHORT).show();
            return;
        }

        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, location -> {
                    if (location != null) {
                        double latitude = location.getLatitude();
                        double longitude = location.getLongitude();
                        buscarLugares(latitude + " " + longitude);
                    } else {
                        requestNewLocationUpdate();
                    }
                })
                .addOnFailureListener(this, e -> {
                    e.printStackTrace();
                    requestNewLocationUpdate();
                });
    }

    private void requestNewLocationUpdate() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(2000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationCallback locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    double latitude = location.getLatitude();
                    double longitude = location.getLongitude();
                    buscarLugares(latitude + " " + longitude);
                }
            }
        };

        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
    }






}