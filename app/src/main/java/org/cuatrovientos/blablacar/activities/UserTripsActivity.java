package org.cuatrovientos.blablacar.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_trips);


        UserManager.init(getApplicationContext());
        User currentUser = UserManager.getCurrentUser();
        List<DriverTrips> routesUser = new ArrayList<>();
         Utils.getUsers(new Utils.FirebaseCallback() {
            @Override
            public void onCallback(List<User> userList) {
                for (User user : userList) {
                    if (user.getRoutes() != null) { // Check if user has routes
                        for (RouteEntity route : user.getRoutes()) {
                            if (route.getPassengers() != null) { // Ensure passengers list is not null
                                for (User passenger : route.getPassengers()) {
                                    if (passenger.getId() != null && passenger.getId().equals(currentUser.getId())) {
                                        // If any passenger's ID matches the current user's ID, add the route
                                        routesUser.add(new DriverTrips(user, route));
                                        break; // Stop checking other passengers in this route
                                    }
                                }
                            }
                        }
                    }
                }


                RecyclerView recyclerViewGroupedTrips = findViewById(R.id.recyclerViewGroupedTrips);
                recyclerViewGroupedTrips.setLayoutManager(new LinearLayoutManager(UserTripsActivity.this));

                // Preprocess routeEntities into List<DayTrips> as shown earlier
                RecyclerUserTripsAdapter adapter = new RecyclerUserTripsAdapter(UserTripsActivity.this, routesUser);
                recyclerViewGroupedTrips.setAdapter(adapter);
            }
        });


    }
}