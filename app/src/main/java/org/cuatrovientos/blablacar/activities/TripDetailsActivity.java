package org.cuatrovientos.blablacar.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import org.cuatrovientos.blablacar.R;
import org.cuatrovientos.blablacar.UserManager;
import org.cuatrovientos.blablacar.adapters.RecyclerTripsDetailsAdapter;
import org.cuatrovientos.blablacar.models.CustomLatLng;
import org.cuatrovientos.blablacar.models.DriverTrips;
import org.cuatrovientos.blablacar.models.RouteEntity;
import org.cuatrovientos.blablacar.models.User;
import org.cuatrovientos.blablacar.models.Utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TripDetailsActivity extends AppCompatActivity {
    public final CustomLatLng CUATROVIENTOS = new CustomLatLng(42.824851, -1.660318);
    // Header
    private RelativeLayout header;
    private ImageView backArrow;

    // ScrollView and its contents
    private ScrollView scrollView;
    private TextView timeText;
    private ImageView timelineTopDot, timelineBottomDot;
    private View timeline;
    private TextView startTime, arrivalTime;
    private TextView originCity, destinationCity;

    // Price and driver details
    private View separatorBeforePrice, separatorAfterPrice;
    private TextView textPricePerPassenger, textPrice;
    private ImageView imageProfile;
    private LinearLayout containerNameRating;
    private TextView textName, textRating, textCancel, textQuestion, textSmoke, textEating, textCar, textCarColor;
    private View separatorBeforeQuestion, separatorAfterQuestion, separatorBeforePassengers, separatorAfterPassengers;

    // RecyclerView for passengers
    private RecyclerView recyclerViewTrayectos;

    // Footer
    private RelativeLayout footer;
    private Button btnReservar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_details);

        Intent intent = getIntent();
        DriverTrips driverTrips = (DriverTrips) intent.getSerializableExtra("EXTRA_DRIVER_TRIPS");

        // Initialize header views
        header = findViewById(R.id.header);
        backArrow = findViewById(R.id.back_arrow);

        // Initialize ScrollView and its contents
        scrollView = findViewById(R.id.scrollView);
        timeText = findViewById(R.id.timeText);
        timelineTopDot = findViewById(R.id.timeline_top_dot);
        timeline = findViewById(R.id.timeline);
        timelineBottomDot = findViewById(R.id.timeline_bottom_dot);
        startTime = findViewById(R.id.start_time);
        arrivalTime = findViewById(R.id.arrival_time);
        originCity = findViewById(R.id.origin_city);
        destinationCity = findViewById(R.id.destination_city);

        // Initialize price and driver details
        separatorBeforePrice = findViewById(R.id.separator_before_price);
        textPricePerPassenger = findViewById(R.id.text_price_per_passenger);
        textPrice = findViewById(R.id.text_price);
        separatorAfterPrice = findViewById(R.id.separator_after_price);
        imageProfile = findViewById(R.id.image_profile);
        containerNameRating = findViewById(R.id.container_name_rating);
        textName = findViewById(R.id.text_name);
        textRating = findViewById(R.id.text_rating);
        textCancel = findViewById(R.id.text_cancel);
        textQuestion = findViewById(R.id.text_question);
        separatorBeforeQuestion = findViewById(R.id.separator_before_question);
        separatorAfterQuestion = findViewById(R.id.separator_after_question);
        textSmoke = findViewById(R.id.text_smoke);
        textEating = findViewById(R.id.text_eating);
        textCar = findViewById(R.id.text_car);
        textCarColor = findViewById(R.id.text_carColor);
        separatorBeforePassengers = findViewById(R.id.separator_before_passengers);
        separatorAfterPassengers = findViewById(R.id.separator_after_passengers);

        // Initialize RecyclerView for passengers
        recyclerViewTrayectos = findViewById(R.id.recyclerViewTrayectos);

        // Initialize footer and its contents
        footer = findViewById(R.id.footer);
        btnReservar = findViewById(R.id.btnReservar);


        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());

        // Format the start time to a String
        String startTimeStr = timeFormat.format(driverTrips.getRoute().getDate());
        startTime.setText(startTimeStr);

        // Get the duration as a String
        String durationStr = driverTrips.getRoute().getDuration();
        //timeDiff.setText(durationStr);

        try {
            // Split the duration string into hours and minutes
            String[] parts = durationStr.split(":");
            int hours = Integer.parseInt(parts[0]);
            int minutes = Integer.parseInt(parts[1]);

            // Convert the duration to milliseconds
            long durationMillis = (hours * 3600 + minutes * 60) * 1000;

            // Add the duration to the start date's time
            long startTimeMillis = driverTrips.getRoute().getDate().getTime();
            long endTimeMillis = startTimeMillis + durationMillis;

            // Create a new Date object for the end time
            Date endTime = new Date(endTimeMillis);

            // Format the end time to a String
            String endTimeStr = timeFormat.format(endTime);
            arrivalTime.setText(endTimeStr);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            arrivalTime.setText("Error");
        }


        originCity.setText(driverTrips.getRoute().getOriginText());
        destinationCity.setText(driverTrips.getRoute().getDestinationText());

        textPrice.setText(String.valueOf(driverTrips.getRoute().getPrice()) + "€");
        //uselogo
        textName.setText(driverTrips.getUser().getName());
        textRating.setText("⭐ " + driverTrips.getUser().getRating() + "/5 - " + driverTrips.getUser().getTotalRatings() + " opiniones");


        // Set up listeners or further initializations here
        recyclerViewTrayectos.setLayoutManager(new LinearLayoutManager(this));
        if (driverTrips.getRoute().getPassengers() != null){
            recyclerViewTrayectos.setAdapter(new RecyclerTripsDetailsAdapter(driverTrips.getRoute().getPassengers(), new RecyclerTripsDetailsAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(User user) {

                }
            }));
        }


        btnReservar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserManager.init(getApplicationContext());
                User currentUser = UserManager.getCurrentUser();

                if (currentUser.getId().equals(driverTrips.getUser().getId())) {
                    Toast.makeText(getApplicationContext(), "You cannot reserve your own trip.", Toast.LENGTH_SHORT).show();
                    return; // Exit the method early
                }

                boolean isUserAlreadyInRoute = false;
                for (RouteEntity route : driverTrips.getUser().getRoutes()) {
                    if (route.getPassengers() != null && route.getPassengers().contains(currentUser)) {
                        isUserAlreadyInRoute = true;
                        break;
                    }
                }

                if (!isUserAlreadyInRoute) {
                    boolean hasTripToDestinationToday = false;
                    Calendar today = Calendar.getInstance();
                    for (RouteEntity route : currentUser.getRoutes()) {
                        Calendar routeDate = Calendar.getInstance();
                        routeDate.setTime(route.getDate());

                        boolean sameDay = today.get(Calendar.YEAR) == routeDate.get(Calendar.YEAR) &&
                                today.get(Calendar.DAY_OF_YEAR) == routeDate.get(Calendar.DAY_OF_YEAR);

                        if (sameDay && route.getDestination().equals(CUATROVIENTOS)) {
                            hasTripToDestinationToday = true;
                            break;
                        }
                    }

                    if (!hasTripToDestinationToday) {
                        for (RouteEntity route : driverTrips.getUser().getRoutes()) {
                            if (route.getId() == driverTrips.getRoute().getId()) {
                                List<User> users = route.getPassengers();
                                if (users == null) {
                                    users = new ArrayList<>();
                                }
                                users.add(currentUser);
                                route.setPassengers(users);
                                Utils.updateUser(driverTrips.getUser());
                                finish();
                                break;
                            }
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "You already have a trip to this destination today.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "You are already part of this route.", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
