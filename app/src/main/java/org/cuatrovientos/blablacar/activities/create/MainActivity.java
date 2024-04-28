package org.cuatrovientos.blablacar.activities.create;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.material.snackbar.Snackbar;

import org.cuatrovientos.blablacar.R;
import org.cuatrovientos.blablacar.UserManager;
import org.cuatrovientos.blablacar.activities.profile.ProfileActivity;
import org.cuatrovientos.blablacar.activities.search.SearchRoutes;
import org.cuatrovientos.blablacar.activities.history.UserTripsActivity;
import org.cuatrovientos.blablacar.activities.chat.MainActivityChat;
import org.cuatrovientos.blablacar.models.CustomLatLng;
import org.cuatrovientos.blablacar.models.PlaceOpenStreetMap;
import org.cuatrovientos.blablacar.models.User;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap map;
    private List<Polyline> polylineList = new ArrayList<>();
    private static LatLng CUATROVIENTOS = new LatLng(42.824851, -1.660318);

    private final Executor executor = Executors.newSingleThreadExecutor();
    private ArrayList<User> users = new ArrayList<>();

    private RecyclerView recyclerView;

    private LinearLayout linearLayout;
    private ImageButton btnSearch;
    private ImageButton btnPublish;
    private ImageButton btnHistory;
    private ImageButton btnMessages;
    private ImageButton btnProfile;

    private MapHelper mapHelper;
    private User localUser;

    private RouteService routeService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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


        
        btnSearch.setOnClickListener(view -> {
            Intent searchIntent = new Intent(this, SearchRoutes.class);
            startActivity(searchIntent);
        });

        btnPublish.setOnClickListener(view -> {
            Intent publishIntent = new Intent(this, CreateRoute.class);
            startActivity(publishIntent);
        });


        btnHistory.setOnClickListener(view -> {
            Intent historyIntent = new Intent(this, UserTripsActivity.class);
            startActivity(historyIntent);
        });

        btnMessages.setOnClickListener(view -> {
            Intent chatIntent = new Intent(this, MainActivityChat.class);
            startActivity(chatIntent);
        });

        btnProfile.setOnClickListener(view -> {

            Intent profileIntent = new Intent(this, ProfileActivity.class);
            startActivity(profileIntent);

        });

        UserManager.init(getApplicationContext());
        User currentUser = UserManager.getCurrentUser();
        if (currentUser == null){
            View contentView = findViewById(android.R.id.content);
            Snackbar snackbar = Snackbar.make(contentView, "NO ESTÁ LOGGEADO", Snackbar.LENGTH_SHORT);
            snackbar.setTextColor(Color.WHITE);
            snackbar.setBackgroundTint(Color.RED);
            snackbar.show();
        }
        localUser = currentUser;
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        map = googleMap;
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(CUATROVIENTOS, 13.5f));
        map.addMarker(new MarkerOptions().position(CUATROVIENTOS).title("Cuatrovientos"));

        mapHelper = new MapHelper(map);
        routeService = new RouteService(MainActivity.this, mapHelper);

        if (getIntent() != null) {
            PlaceOpenStreetMap origin = (PlaceOpenStreetMap) getIntent().getSerializableExtra("origin");
            PlaceOpenStreetMap destination = (PlaceOpenStreetMap) getIntent().getSerializableExtra("destination");
            String dateStr = getIntent().getStringExtra("date");
            String originText = getIntent().getStringExtra("originText");
            String destinationText = getIntent().getStringExtra("destinationText");
            int seats = getIntent().getIntExtra("seats", 0);
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            Date date = new Date();
            try {
                date = sdf.parse(dateStr);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }

            Log.d("Womp", origin.getLon() + " " + origin.getLat() + " " + destination.getLon() + " " + destination.getLat());
            CustomLatLng originLocation = new CustomLatLng(Double.parseDouble(origin.getLat()), Double.parseDouble(origin.getLon()));
            CustomLatLng destinationLocation = new CustomLatLng(Double.parseDouble(destination.getLat()), Double.parseDouble(destination.getLon()));
            routeService.routeCreation(this, localUser, originLocation, destinationLocation, date, recyclerView, linearLayout, originText, destinationText, seats);
        }
    }
}