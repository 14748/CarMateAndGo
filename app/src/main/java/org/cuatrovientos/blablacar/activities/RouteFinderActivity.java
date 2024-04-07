package org.cuatrovientos.blablacar.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import org.cuatrovientos.blablacar.R;
import org.cuatrovientos.blablacar.adapters.RecyclerTripsAdapter;
import org.cuatrovientos.blablacar.models.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class RouteFinderActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private TextView timeText, noElements;
    private TextView searchBox;

    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
    private SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE", Locale.getDefault());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_finder);

        initViewReferences();
        processIntentData();
    }

    private void initViewReferences() {
        timeText = findViewById(R.id.timeText);
        searchBox = findViewById(R.id.searchBox);
        recyclerView = findViewById(R.id.recyclerViewTrayectos);
        noElements = findViewById(R.id.noElements);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 1));
    }

    private void processIntentData() {
        Intent data = getIntent();
        PlaceOpenStreetMap origin = (PlaceOpenStreetMap) data.getSerializableExtra("origin");
        PlaceOpenStreetMap destination = (PlaceOpenStreetMap) data.getSerializableExtra("destination");
        String dateStr = data.getStringExtra("date");
        String textSearchBox = data.getStringExtra("text");
        boolean type = data.getBooleanExtra("type", false);

        Date date = parseDate(dateStr);
        updateDateDisplay(date);
        searchBox.setText(textSearchBox);

        CustomLatLng originLocation = new CustomLatLng(Double.parseDouble(origin.getLat()), Double.parseDouble(origin.getLon()));
        CustomLatLng destinationLocation = new CustomLatLng(Double.parseDouble(destination.getLat()), Double.parseDouble(destination.getLon()));

        fetchAndDisplayRoutes(originLocation, destinationLocation, date, type);
    }

    private Date parseDate(String dateStr) {
        try {
            return sdf.parse(dateStr);
        } catch (ParseException e) {
            Log.e("RouteFinderActivity", "Date parsing error", e);
            throw new RuntimeException("Error parsing date: " + dateStr);
        }
    }

    private void updateDateDisplay(Date date) {
        Calendar calendarDate = Calendar.getInstance();
        calendarDate.setTime(date);
        String textToShow = calculateDateTextToShow(date, calendarDate);
        timeText.setText(textToShow);
    }

    private String calculateDateTextToShow(Date date, Calendar calendarDate) {
        Calendar today = Calendar.getInstance();
        Calendar tomorrow = (Calendar) today.clone();
        tomorrow.add(Calendar.DAY_OF_YEAR, 1);
        Calendar endOfWeek = (Calendar) today.clone();
        int daysUntilEndOfWeek = Calendar.SATURDAY - today.get(Calendar.DAY_OF_WEEK);
        endOfWeek.add(Calendar.DAY_OF_YEAR, daysUntilEndOfWeek);

        Calendar targetDate = Calendar.getInstance();
        targetDate.setTime(date);

        if (targetDate.get(Calendar.WEEK_OF_YEAR) == today.get(Calendar.WEEK_OF_YEAR))
        {
            if (sdf.format(date).equals(sdf.format(today.getTime()))) {
                return "Hoy";
            } else if (sdf.format(date).equals(sdf.format(tomorrow.getTime()))) {
                return "Mañana";
            } else if (today.before(endOfWeek) && calendarDate.before(endOfWeek)) {
                return dayFormat.format(date);
            } else {
                return sdf.format(date);
            }
        } else {
        return sdf.format(date);
        }
    }

    private void fetchAndDisplayRoutes(CustomLatLng originLocation, CustomLatLng destinationLocation, Date date, boolean type) {
        // Simulating fetching users and calculating routes. Replace with actual fetching logic.
        Utils.getUsers(userList -> {
            List<DriverTrips> matchingDriverTrips = findRoutes(userList, date, type ? originLocation : destinationLocation, type);
            updateRecyclerView(matchingDriverTrips);
        });
    }

    public List<DriverTrips> findRoutes(List<User> users, Date selectedDate, CustomLatLng userLocation, boolean type) {
        List<DriverTrips> matchingDriverTrips = new ArrayList<>();
        CustomLatLng cuatrovientosLatLng = new CustomLatLng(42.824851, -1.660318);
        double radiusKm = 5.0;

        for (User user : users) {
            for (RouteEntity route : user.getCreatedRoutes()) {
                boolean isSameDay = isSameDay(route.getDate(), selectedDate);
                CustomLatLng routeOrigin = route.getOrigin();
                CustomLatLng routeDestination = route.getDestination();

                boolean originMatchesCuatrovientos = routeOrigin.equals(cuatrovientosLatLng);
                boolean destinationMatchesCuatrovientos = routeDestination.equals(cuatrovientosLatLng);

                if (isSameDay) {
                    if (originMatchesCuatrovientos && !type) {
                        // If the origin matches Cuatrovientos, check if the destination is within 5 km radius of the user's destination
                        if (isWithinRadius(routeDestination, userLocation, radiusKm)) {
                            matchingDriverTrips.add(new DriverTrips(user, route, selectedDate));
                        }
                    } else if (destinationMatchesCuatrovientos && type) {
                        // If the destination matches Cuatrovientos, check if the origin is within 5 km radius of the user's origin
                        if (isWithinRadius(routeOrigin, userLocation, radiusKm)) {
                            matchingDriverTrips.add(new DriverTrips(user, route, selectedDate));
                        }
                    }
                }
            }
        }

        return matchingDriverTrips;
    }




    private boolean isSameDay(Date date1, Date date2) {
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTime(date1);
        cal2.setTime(date2);
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR);
    }

    private boolean isWithinRadius(CustomLatLng start, CustomLatLng end, double radiusKm) {
        double distanceKm = calculateDistanceInKilometer(start.getLatitude(), start.getLongitude(), end.getLatitude(), end.getLongitude());
        return distanceKm <= radiusKm;
    }

    private double calculateDistanceInKilometer(double lat1, double lon1, double lat2, double lon2) {
        double earthRadiusKm = 6371.0;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.sin(dLon / 2) * Math.sin(dLon / 2) * Math.cos(lat1) * Math.cos(lat2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return earthRadiusKm * c;
    }

    private void updateRecyclerView(List<DriverTrips> driverTrips) {
        if (driverTrips.size() == 0){
            noElements.setVisibility(View.VISIBLE);
        }else{
            noElements.setVisibility(View.GONE);
            RecyclerTripsAdapter adapter = new RecyclerTripsAdapter(driverTrips, this::onTripSelected);
            recyclerView.setAdapter(adapter);
        }
    }

    private void onTripSelected(DriverTrips trip) {
        Toast.makeText(this, "Selected Trip: " + trip.toString(), Toast.LENGTH_LONG).show();
        Intent intent = new Intent(RouteFinderActivity.this, TripDetailsActivity.class);
        startActivity(intent);
        DataHolder.getInstance().setYourData(trip);
    }
}


