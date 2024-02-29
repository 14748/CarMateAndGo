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
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
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
import org.cuatrovientos.blablacar.RouteService;
import org.cuatrovientos.blablacar.activities.login.MainScreen;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.internal.Util;
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

    private ImageButton btnSearch;
    private ImageButton btnPublish;
    private ImageButton btnHistory;
    private ImageButton btnMessages;
    private ImageButton btnProfile;

    private MapHelper mapHelper;

    private RouteService routeService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initCreateRouteLauncher();
        setContentView(R.layout.activity_main);
        button = findViewById(R.id.button2);
        btnCreateRoute = findViewById(R.id.btnCreateScreen);
        btnLogIn = findViewById(R.id.bntLogIn);
        btnTest = findViewById(R.id.buttontest);
        linearLayout = findViewById(R.id.linearLayout);
        recyclerView = findViewById(R.id.routesRecyclerView);
        btnSearch = findViewById(R.id.btnSearch);
        btnPublish = findViewById(R.id.btnPublish);
        btnHistory = findViewById(R.id.btnHistory);
        btnMessages = findViewById(R.id.btnMessages);
        btnProfile = findViewById(R.id.btnProfile);
        recyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this, 1));
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        //Utils.pushUser(new User(0, "pepito", "pepita", new Date(), "mail", "pass"));
        btnSearch.setOnClickListener(view -> {
            /*
            Intent searchIntent = new Intent(this, Search.class);
            startActivity(searchIntent);
             */
        });

        btnPublish.setOnClickListener(view -> {
            Intent publishIntent = new Intent(this, CreateRoute.class);
            startActivity(publishIntent);
        });

        btnHistory.setOnClickListener(view -> {
            /*
            Intent historyIntent = new Intent(this, HistoryActivity.class);
            startActivity(historyIntent);
             */
        });

        btnMessages.setOnClickListener(view -> {
            /*
            Intent messagesIntent = new Intent(this, MessagesActivity.class);
            startActivity(messagesIntent);
             */
        });

        btnProfile.setOnClickListener(view -> {

            Intent profileIntent = new Intent(this, ProfileActivity.class);
            startActivity(profileIntent);

        });


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

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        map = googleMap;
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(CUATROVIENTOS, 13.5f));
        map.addMarker(new MarkerOptions().position(CUATROVIENTOS).title("Cuatrovientos"));

        mapHelper = new MapHelper(map);
        routeService = new RouteService(MainActivity.this, mapHelper);
    }

    private void initCreateRouteLauncher() {
        createRouteLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        map.clear();
                        Intent data = result.getData();

                        PlaceOpenStreetMap origin = (PlaceOpenStreetMap) data.getSerializableExtra("origin");
                        PlaceOpenStreetMap destination = (PlaceOpenStreetMap) data.getSerializableExtra("destination");
                        String dateStr = data.getStringExtra("date");
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                        Date date = new Date();
                        try {
                             date = sdf.parse(dateStr);
                        } catch (ParseException e) {
                            throw new RuntimeException(e);
                        }
                        Log.d("Womp", origin.getLon() + " " + origin.getLat() + " " + destination.getLon() + " " + destination.getLat());
                        CustomLatLng originLocation = new CustomLatLng(Double.parseDouble(origin.getLat()), Double.parseDouble(origin.getLon()));
                        CustomLatLng destinationLocation = new CustomLatLng(Double.parseDouble(destination.getLat()), Double.parseDouble(destination.getLon()));
                        routeService.routeCreation(new User(), originLocation, destinationLocation, date, recyclerView);
                    }
                });
    }









}