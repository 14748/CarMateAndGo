package org.cuatrovientos.blablacar.activities;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.cuatrovientos.blablacar.R;
import org.cuatrovientos.blablacar.adapters.RecyclerRoutesAdapter;
import org.cuatrovientos.blablacar.adapters.RecyclerTripsAdapter;
import org.cuatrovientos.blablacar.models.CustomLatLng;
import org.cuatrovientos.blablacar.models.DriverTrips;
import org.cuatrovientos.blablacar.models.PlaceOpenStreetMap;
import org.cuatrovientos.blablacar.models.RouteEntity;
import org.cuatrovientos.blablacar.models.RouteSelectionInfo;
import org.cuatrovientos.blablacar.models.User;
import org.cuatrovientos.blablacar.models.Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class RouteFinderActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ActivityResultLauncher<Intent> createRouteLauncher;

    private TextView timeText;
    private EditText searchBox;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_finder);
        timeText = findViewById(R.id.timeText);
        searchBox = findViewById(R.id.searchBox);
        recyclerView = findViewById(R.id.recyclerViewTrayectos);
        recyclerView.setLayoutManager(new GridLayoutManager(RouteFinderActivity.this, 1));

        //initCreateRouteLauncher();

        Intent data = getIntent();

        PlaceOpenStreetMap origin = (PlaceOpenStreetMap) data.getSerializableExtra("origin");
        PlaceOpenStreetMap destination = (PlaceOpenStreetMap) data.getSerializableExtra("destination");
        String dateStr = data.getStringExtra("date");
        String textSearchBox = data.getStringExtra("text");
        boolean type = data.getBooleanExtra("type", false);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE", Locale.getDefault());

        final Date date;
        try {
            date = sdf.parse(dateStr);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        Calendar calendarDate = Calendar.getInstance();
        calendarDate.setTime(date);

        Calendar today = Calendar.getInstance();
        Calendar tomorrow = Calendar.getInstance();
        tomorrow.add(Calendar.DAY_OF_YEAR, 1);
        Calendar endOfWeek = Calendar.getInstance();
        int dayOfWeek = today.get(Calendar.DAY_OF_WEEK);
        int daysUntilEndOfWeek = Calendar.SATURDAY - dayOfWeek;
        endOfWeek.add(Calendar.DAY_OF_YEAR, daysUntilEndOfWeek);

        String textToShow;
        if (sdf.format(date).equals(sdf.format(today.getTime()))) {
            textToShow = "Hoy";
        } else if (sdf.format(date).equals(sdf.format(tomorrow.getTime()))) {
            textToShow = "Mañana";
        } else if (today.before(endOfWeek) && calendarDate.before(endOfWeek)) {
            textToShow = dayFormat.format(date);
        } else {
            textToShow = dateStr;
        }

        timeText.setText(textToShow);
        searchBox.setText(textSearchBox);

        Log.d("Womp", origin.getLon() + " " + origin.getLat() + " " + destination.getLon() + " " + destination.getLat());
        CustomLatLng originLocation = new CustomLatLng(Double.parseDouble(origin.getLat()), Double.parseDouble(origin.getLon()));
        CustomLatLng destinationLocation = new CustomLatLng(Double.parseDouble(destination.getLat()), Double.parseDouble(destination.getLon()));
        Utils.getUsers(new Utils.FirebaseCallback() {
            @Override
            public void onCallback(List<User> userList) {
                CustomLatLng latLon = new CustomLatLng();
                if (type){
                    latLon = originLocation;
                }else {
                    latLon = destinationLocation;
                }

                List<DriverTrips> matchingDriverTrips = findRoutes(userList, date, latLon, 50.0, type);

                recyclerView.setAdapter(new RecyclerTripsAdapter(matchingDriverTrips, new RecyclerTripsAdapter.onItemClickListener() {
                    @Override
                    public void onItemClickListener(DriverTrips palabra) {
                        Toast.makeText(RouteFinderActivity.this, "Selected Trip: " + matchingDriverTrips.indexOf(palabra), Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(RouteFinderActivity.this, TripDetailsActivity.class);
                        intent.putExtra("EXTRA_DRIVER_TRIPS", palabra);
                        startActivity(intent);
                    }
                }));
            }
        });


    }

    public List<DriverTrips> findRoutes(List<User> users, Date selectedDate, CustomLatLng latLon, double radiusKm, boolean type) {
        List<DriverTrips> matchingDriverTrips = new ArrayList<>();

        for (User user : users) {
            for (RouteEntity route : user.getRoutes()) {
                if (isSameDay(route.getDate(), selectedDate) && isWithinRadius(type ? route.getOrigin() : route.getDestination(), latLon, radiusKm)) {
                    matchingDriverTrips.add(new DriverTrips(user, route));
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
}
