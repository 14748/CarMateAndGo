package org.cuatrovientos.blablacar.activities;

import static android.content.ContentValues.TAG;
import static android.widget.Toast.makeText;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.cuatrovientos.blablacar.R;
import org.cuatrovientos.blablacar.adapters.RouteAdapter;
import org.cuatrovientos.blablacar.models.ORS.Route;
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





        setCurrentLocation.setOnClickListener(e -> {
            // Solicitar permisos si no están concedidos
            if (checkLocationPermission()) {
                obtenerUbicacionActual();

            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        PERMISSION_REQUEST_CODE);
            }
        });


        // Configurar RecyclerView y su adaptador
        searchResults.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RouteAdapter(results);
        searchResults.setAdapter(adapter);

        // Configurar el autocompletado


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
                handler.postDelayed(searchRunnable, 1250);
            }
        });


        // Manejar clic en un elemento del RecyclerView
        adapter.setOnItemClickListener(position -> {
            PlaceOpenStreetMap place = results.get(position);
            makeText(this, "Seleccionado: " + place.getDisplayName(), Toast.LENGTH_SHORT).show();
            makeText(this, "Latitud: " + place.getLat() + ", Longitud: " + place.getLon(), Toast.LENGTH_SHORT).show();
            // Aquí puedes hacer lo que necesites con el lugar seleccionado
        });







    }

    private void buscarLugares(String query) {
        // Aquí deberías hacer la búsqueda de lugares y actualizar el RecyclerView
        // con los resultados
        // Ejemplo de autocompletado con OpenStreetMap API
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
                        Log.d(TAG, "onResponse() returned: " + routes.toString());
                        results.clear();
                        results.addAll(routes);
                        adapter.notifyDataSetChanged();
                    } else {
                        Log.e(TAG, "Respuesta vacía o no contiene un array de lugares");
                    }
                } else {
                    try {
                        // Intenta convertir el cuerpo de la respuesta a un objeto JSON
                        JSONObject errorObject = new JSONObject(response.errorBody().string());
                        String errorMessage = errorObject.getString("message");
                        Log.e(TAG, "Error en la respuesta de la solicitud: " + errorMessage);
                    } catch (Exception e) {
                        // Si hay algún problema al convertir a JSON, simplemente registra el mensaje de error
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
        // Nota: Asegúrate de manejar las solicitudes de red de manera asíncrona.
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
                makeText(this, "Permiso de ubicación denegado.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void obtenerUbicacionActual() {
        try {
            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

            // Configurar el listener para recibir actualizaciones de ubicación
            LocationListener locationListener = new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    double latitud = location.getLatitude();
                    double longitud = location.getLongitude();

                    Log.d("Ubicacion", "Latitud: " + latitud + ", Longitud: " + longitud);

                    // Aquí puedes hacer lo que necesites con la ubicación
                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {
                }

                @Override
                public void onProviderEnabled(String provider) {

                }

                @Override
                public void onProviderDisabled(String provider) {
                }
            };

            // Solicitar actualizaciones de ubicación a través del servicio de red y GPS
            locationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER,
                    0,
                    0,
                    locationListener
            );

            locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    0,
                    0,
                    locationListener
            );

        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }



}