package org.cuatrovientos.blablacar.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import org.cuatrovientos.blablacar.R;
import org.cuatrovientos.blablacar.UserManager;
import org.cuatrovientos.blablacar.adapters.RecyclerUserTripsAdapter;
import org.cuatrovientos.blablacar.models.DayTrips;
import org.cuatrovientos.blablacar.models.DriverTrips;
import org.cuatrovientos.blablacar.models.RouteEntity;
import org.cuatrovientos.blablacar.models.User;
import org.cuatrovientos.blablacar.models.Utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import okhttp3.internal.Util;

public class UserTripsActivity extends AppCompatActivity {
    Button buttonRutasPasajero;
    Button buttonRutasConductor;

    private ImageButton btnSearch;
    private ImageButton btnPublish;
    private ImageButton btnHistory;
    private ImageButton btnMessages;
    private ImageButton btnProfile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_trips);

        buttonRutasPasajero = findViewById(R.id.buttonRutasPasajero);
        buttonRutasConductor = findViewById(R.id.buttonRutasConductor);

        btnSearch = findViewById(R.id.btnSearch);
        btnPublish = findViewById(R.id.btnPublish);
        btnHistory = findViewById(R.id.btnHistory);
        btnMessages = findViewById(R.id.btnMessages);
        btnProfile = findViewById(R.id.btnProfile);

        btnPublish.setOnClickListener(view -> {
            Intent publishIntent = new Intent(this, CreateRoute.class);
            startActivity(publishIntent);
        });

        btnSearch.setOnClickListener(view -> {
            Intent searchIntent = new Intent(this, SearchRoutes.class);
            startActivity(searchIntent);
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

        UserManager.init(getApplicationContext());

        buttonRutasPasajero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadUserTrips();
            }
        });

        buttonRutasConductor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadUserTripsDriver();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadUserTrips();
    }

    private void loadUserTrips() {
        User currentUser = UserManager.getCurrentUser();
        List<DriverTrips> routesUser = new ArrayList<>();

        Utils.getUsers(new Utils.FirebaseCallback() {
            @Override
            public void onCallback(List<User> userList) {
                // Process users to find relevant trips
                for (User user : userList) {
                    if (user.getCreatedRoutes() != null) {
                        for (RouteEntity route : user.getCreatedRoutes()) {
                            if (route.getPassengers() != null && !route.getPassengers().isEmpty()) {
                                Utils.getUsersByIds(route.getPassengers(), new Utils.UsersCallback() {
                                    @Override
                                    public void onCallback(List<User> passengers) {
                                        for (User passenger : passengers) {
                                            if (passenger.getId() != null && passenger.getId().equals(currentUser.getId())) {
                                                routesUser.add(new DriverTrips(user, route));
                                                break;
                                            }
                                        }
                                        updateRecyclerView(routesUser);
                                    }
                                });
                            }
                        }
                    }
                }
            }
        });
    }

    private void loadUserTripsDriver() {
        User currentUser = UserManager.getCurrentUser();
        List<DriverTrips> routesUser = new ArrayList<>();


        for (RouteEntity route:
                currentUser.getCreatedRoutes()) {
            routesUser.add(new DriverTrips(null, route));
        }

        updateRecyclerView(routesUser);
    }

    private void updateRecyclerView(List<DriverTrips> routesUser) {
        RecyclerView recyclerViewGroupedTrips = findViewById(R.id.recyclerViewGroupedTrips);
        if (recyclerViewGroupedTrips.getAdapter() != null) {
            RecyclerUserTripsAdapter adapter = (RecyclerUserTripsAdapter) recyclerViewGroupedTrips.getAdapter();
            adapter.updateData(routesUser);
        } else {
            recyclerViewGroupedTrips.setLayoutManager(new LinearLayoutManager(this));
            RecyclerUserTripsAdapter adapter = new RecyclerUserTripsAdapter(this, routesUser);
            recyclerViewGroupedTrips.setAdapter(adapter);
        }
    }



}