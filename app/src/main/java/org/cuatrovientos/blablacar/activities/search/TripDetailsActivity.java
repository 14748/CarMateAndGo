package org.cuatrovientos.blablacar.activities.search;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
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
import org.cuatrovientos.blablacar.activities.chat.ChatActivity;
import org.cuatrovientos.blablacar.activities.chat.util.AndroidUtil;
import org.cuatrovientos.blablacar.activities.search.adapters.PreferencesAdapter;
import org.cuatrovientos.blablacar.activities.search.adapters.RecyclerTripsDetailsAdapter;
import org.cuatrovientos.blablacar.models.CustomLatLng;
import org.cuatrovientos.blablacar.models.DataHolder;
import org.cuatrovientos.blablacar.models.DriverTrips;
import org.cuatrovientos.blablacar.models.RouteEntity;
import org.cuatrovientos.blablacar.models.User;
import org.cuatrovientos.blablacar.models.Utils;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class TripDetailsActivity extends AppCompatActivity {
    public final CustomLatLng CUATROVIENTOS = new CustomLatLng(42.824851, -1.660318);
    
    private RelativeLayout header;
    private ImageView backArrow;

    
    private ScrollView scrollView;
    private TextView timeText;
    private ImageView timelineTopDot, timelineBottomDot;
    private View timeline;
    private TextView startTime, arrivalTime;
    private TextView originCity, destinationCity;

    
    private View separatorBeforePrice, separatorAfterPrice;
    private TextView textPricePerPassenger, textPrice;
    private TextView imageProfile;
    private LinearLayout containerNameRating;
    private TextView textName, textRating, textCancel, textQuestion, textSmoke, textEating, textCar, textCarColor;
    private View separatorBeforeQuestion, separatorAfterQuestion, separatorBeforePassengers, separatorAfterPassengers;
    private RelativeLayout container_driver_info;
    
    private RecyclerView recyclerViewTrayectos, preferencesRecyclerView;

    
    private RelativeLayout footer;
    private Button btnReservar;
    private TextView noElements;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_details);

        /*
        Intent intent = getIntent();
        DriverTrips driverTrips = (DriverTrips) intent.getSerializableExtra("EXTRA_DRIVER_TRIPS");
         */
        DriverTrips driverTrips = DataHolder.getInstance().getYourData();

        
        header = findViewById(R.id.header);
        backArrow = findViewById(R.id.back_arrow);

        
        scrollView = findViewById(R.id.scrollView);
        timeText = findViewById(R.id.timeText);
        timelineTopDot = findViewById(R.id.timeline_top_dot);
        timeline = findViewById(R.id.timeline);
        timelineBottomDot = findViewById(R.id.timeline_bottom_dot);
        startTime = findViewById(R.id.start_time);
        arrivalTime = findViewById(R.id.arrival_time);
        originCity = findViewById(R.id.origin_city);
        destinationCity = findViewById(R.id.destination_city);

        
        separatorBeforePrice = findViewById(R.id.separator_before_price);
        textPricePerPassenger = findViewById(R.id.text_price_per_passenger);
        textPrice = findViewById(R.id.text_price);
        separatorAfterPrice = findViewById(R.id.separator_after_price);
        imageProfile = findViewById(R.id.image_profile);
        containerNameRating = findViewById(R.id.container_name_rating);
        textName = findViewById(R.id.text_name);
        textRating = findViewById(R.id.text_rating);
        textQuestion = findViewById(R.id.text_question);
        separatorBeforeQuestion = findViewById(R.id.separator_before_question);
        separatorAfterQuestion = findViewById(R.id.separator_after_question);
        textCar = findViewById(R.id.text_car);
        textCarColor = findViewById(R.id.text_carColor);
        separatorBeforePassengers = findViewById(R.id.separator_before_passengers);
        container_driver_info = findViewById(R.id.container_driver_info);

        
        recyclerViewTrayectos = findViewById(R.id.recyclerViewTrayectos);

        
        footer = findViewById(R.id.footer);
        btnReservar = findViewById(R.id.btnReservar);

        
        noElements = findViewById(R.id.noElements);


        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());

        
        String startTimeStr = timeFormat.format(driverTrips.getRoute().getDate());
        startTime.setText(startTimeStr);

        
        String durationStr = driverTrips.getRoute().getDuration();
        

        try {
            String[] parts = durationStr.split(":");
            int hours = 0;
            int minutes = 0;

            try {
                hours = Integer.parseInt(parts[0]);
            } catch (NumberFormatException e) {
                System.err.println("Invalid hours format, setting to 0");
            }

            try {
                minutes = Integer.parseInt(parts[1]);
            } catch (NumberFormatException e) {
                System.err.println("Invalid minutes format, setting to 0");
            }

            long durationMillis = (hours * 3600 + minutes * 60) * 1000;

            long startTimeMillis = driverTrips.getRoute().getDate().getTime();
            long endTimeMillis = startTimeMillis + durationMillis;

            Date endTime = new Date(endTimeMillis);

            SimpleDateFormat timeFormatEnd = new SimpleDateFormat("HH:mm");
            String endTimeStr = timeFormatEnd.format(endTime);
            arrivalTime.setText(endTimeStr);
        } catch (Exception e) {
            e.printStackTrace();
            arrivalTime.setText("Error");
        }

        container_driver_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TripDetailsActivity.this, ShowRatingsActivity.class);
                intent.putExtra("ratingsList", (Serializable) driverTrips.getUser().getRatings());
                intent.putExtra("user", (Serializable) driverTrips.getUser());
                startActivity(intent);
            }
        });
        textQuestion.setText("Haz una pregunta a " + driverTrips.getUser().getName());
        textQuestion.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
            AndroidUtil.passUserModelAsIntent(intent,driverTrips.getUser());
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            getApplicationContext().startActivity(intent);
        });


        imageProfile.setText(driverTrips.getUser().getName().charAt(0) + "" + driverTrips.getUser().getLastName().charAt(0));
        imageProfile.getBackground().setColorFilter(Color.parseColor("#" + driverTrips.getUser().getColor()), PorterDuff.Mode.SRC);

        preferencesRecyclerView = findViewById(R.id.recyclerViewPreferences);
        preferencesRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        PreferencesAdapter adapter = new PreferencesAdapter(driverTrips.getUser().getVehicle().getCarPreferences());
        preferencesRecyclerView.setAdapter(adapter);

        originCity.setText(driverTrips.getRoute().getOriginText());
        destinationCity.setText(driverTrips.getRoute().getDestinationText());

        textPrice.setText(String.valueOf(driverTrips.getRoute().getPrice()) + "€");
        
        textName.setText(driverTrips.getUser().getName() + " " + driverTrips.getUser().getLastName());
        textRating.setText("⭐ " + driverTrips.getUser().getRating() + "/5 - " + driverTrips.getUser().getTotalRatings() + " opiniones");

        textCar.setText(driverTrips.getUser().getVehicle().getMake() + " " + driverTrips.getUser().getVehicle().getModel());
        textCarColor.setText(String.valueOf(driverTrips.getUser().getVehicle().getColor()));
        
        recyclerViewTrayectos.setLayoutManager(new LinearLayoutManager(this));

        if (driverTrips.getRoute().getPassengers() != null && driverTrips.getRoute().getPassengers().size() > 0) {
            noElements.setVisibility(View.GONE);
            recyclerViewTrayectos.setVisibility(View.VISIBLE);
            Utils.getUsersByIds(driverTrips.getRoute().getPassengers(), new Utils.UsersCallback() {
                @Override
                public void onCallback(List<User> users) {
                    
                    recyclerViewTrayectos.setAdapter(new RecyclerTripsDetailsAdapter(users, new RecyclerTripsDetailsAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(User user) {
                            
                        }
                    }));
                }
            });
        }else {
            noElements.setVisibility(View.VISIBLE);
            recyclerViewTrayectos.setVisibility(View.GONE);
        }


        btnReservar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserManager.init(getApplicationContext());
                User currentUser = UserManager.getCurrentUser();

                if (currentUser.getId().equals(driverTrips.getUser().getId())) {
                    Toast.makeText(getApplicationContext(), "No puedes reservar tu propio viaje.", Toast.LENGTH_SHORT).show();
                    return; 
                }

                if (currentUser.getBalance() < driverTrips.getRoute().getPrice()){
                    Toast.makeText(getApplicationContext(), "No tienes suficiente dinero", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (driverTrips.getDate().after(new Date())) {
                    Toast.makeText(getApplicationContext(), "No puedes inscribirte en viajes pasados", Toast.LENGTH_SHORT).show();
                    return;
                }


                checkIfUserInAnyRoute(driverTrips.getRoute(), currentUser, isUserInRoute -> {
                    String type  = "";
                    if (driverTrips.getRoute().getDestination().equals(CUATROVIENTOS)){
                        type = "0";
                    }else if (driverTrips.getRoute().getOrigin().equals(CUATROVIENTOS)){
                        type = "1";
                    }else{
                        type = "2";
                    }

                    if (!isUserInRoute) {
                        boolean hasTripToCuatrovientosToday = false;
                        boolean hasTripFromCuatrovientosToday = false;

                        
                        for (RouteEntity route : currentUser.getPassengerRoutes()) {

                            if (isSameDay(route.getDate(), driverTrips.getDate())){
                                boolean toCuatrovientos = route.getDestination().equals(CUATROVIENTOS);
                                boolean fromCuatrovientos = route.getOrigin().equals(CUATROVIENTOS);

                                
                                if (toCuatrovientos && !hasTripToCuatrovientosToday) {
                                    hasTripToCuatrovientosToday = true;
                                } else if (fromCuatrovientos && !hasTripFromCuatrovientosToday) {
                                    hasTripFromCuatrovientosToday = true;
                                }

                                
                                if (hasTripToCuatrovientosToday && hasTripFromCuatrovientosToday) {
                                    Toast.makeText(getApplicationContext(), "Ya estas inscrito en 1 viaje de ida y 1 viaje de vuelta", Toast.LENGTH_SHORT).show();
                                    break;
                                }
                            }
                        }


                        if ((!hasTripFromCuatrovientosToday && type.equals("1")) || (!hasTripToCuatrovientosToday && type.equals("0"))) {
                            for (RouteEntity route : driverTrips.getUser().getCreatedRoutes()) {
                                if (route.getId().equals(driverTrips.getRoute().getId())) { 
                                    List<String> userIDs = route.getPassengers(); 
                                    if (userIDs == null) {
                                        userIDs = new ArrayList<>();
                                    }
                                    userIDs.add(currentUser.getId());
                                    User user = driverTrips.getUser();
                                    user.getCreatedRoutes().get(driverTrips.getUser().getCreatedRoutes().indexOf(route)).setPassengers(userIDs);

                                    float newBalance = currentUser.getBalance() - driverTrips.getRoute().getPrice();
                                    currentUser.setBalance(newBalance);
                                    currentUser.addPassengerRoute(route);
                                    Utils.pushUser(currentUser);
                                    Utils.pushUser(user);
                                    UserManager.setCurrentUser(currentUser);

                                    Intent returnIntent = new Intent();
                                    setResult(Activity.RESULT_OK, returnIntent);
                                    finish();
                                    break;
                                }
                            }
                        }else{
                            Toast.makeText(getApplicationContext(), "Ya estas insctito para un viaje de " + (type.equals("0") ? "ida" : "vuelta") + " hoy.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Ya estas inscrito a esta ruta.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }

    public interface UserRouteCheckCallback {
        void onResult(boolean isUserInRoute);
    }

    public void checkIfUserInAnyRoute(RouteEntity routeGettingIn, User currentUser, UserRouteCheckCallback callback) {
        if (routeGettingIn.getPassengers().isEmpty()) {
            callback.onResult(false);
            return;
        }

        AtomicInteger pendingOperations = new AtomicInteger(routeGettingIn.getPassengers().size());
        AtomicBoolean isUserAlreadyInRoute = new AtomicBoolean(false);

            Utils.getUsersByIds(routeGettingIn.getPassengers(), new Utils.UsersCallback() {
                @Override
                public void onCallback(List<User> users) {
                    
                    for (User user : users) {
                        if (user.getId().equals(currentUser.getId())) {
                            isUserAlreadyInRoute.set(true);
                            
                            break;
                        }
                    }

                    
                    if (pendingOperations.decrementAndGet() == 0) {
                        
                        callback.onResult(isUserAlreadyInRoute.get());
                    }
                }
            });
    }

    public boolean isSameDay(Date date1, Date date2) {
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTime(date1);
        cal2.setTime(date2);
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH) &&
                cal1.get(Calendar.DAY_OF_MONTH) == cal2.get(Calendar.DAY_OF_MONTH);
    }



}
