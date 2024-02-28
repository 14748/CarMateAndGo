package org.cuatrovientos.blablacar.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import org.cuatrovientos.blablacar.R;
import org.cuatrovientos.blablacar.adapters.RecyclerRoutesAdapter;
import org.cuatrovientos.blablacar.adapters.RecyclerTripsAdapter;
import org.cuatrovientos.blablacar.models.CustomLatLng;
import org.cuatrovientos.blablacar.models.DriverTrips;
import org.cuatrovientos.blablacar.models.RouteEntity;
import org.cuatrovientos.blablacar.models.RouteSelectionInfo;
import org.cuatrovientos.blablacar.models.User;
import org.cuatrovientos.blablacar.models.Utils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class RouteFinderActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_finder);

        recyclerView = findViewById(R.id.recyclerViewTrayectos);
        recyclerView.setLayoutManager(new GridLayoutManager(RouteFinderActivity.this, 1));

        Utils.getUsers(new Utils.FirebaseCallback() {
            @Override
            public void onCallback(List<User> userList) {
                // Get today's date
                Date today = Calendar.getInstance().getTime();

                // Create a CustomLatLng instance with the specified latitude and longitude
                CustomLatLng latLon = new CustomLatLng(42.824851, -1.660318);

                // Call findRoutes with the specified parameters
                List<DriverTrips> matchingDriverTrips = findRoutes(userList, today, latLon, 10.0); // radius is 10km

                // Assuming you have a way to set or update your RecyclerView adapter with the new data
                recyclerView.setAdapter(new RecyclerTripsAdapter(matchingDriverTrips, new RecyclerTripsAdapter.onItemClickListener() {
                    @Override
                    public void onItemClickListener(DriverTrips palabra) {
                        Toast.makeText(RouteFinderActivity.this, "Selected Trip: " + matchingDriverTrips.indexOf(palabra), Toast.LENGTH_LONG).show();
                    }
                }));
            }
        });

    }

    public List<DriverTrips> findRoutes(List<User> users, Date selectedDate, CustomLatLng latLon, double radiusKm) {
        List<DriverTrips> matchingDriverTrips = new ArrayList<>();

        for (User user : users) {
            for (RouteEntity route : user.getRoutes()) {
                if (isSameDay(route.getDate(), selectedDate) && isWithinRadius(route.getTravelPoint(), latLon, radiusKm)) {
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
