package org.cuatrovientos.blablacar.activities;

import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MapHelper {
    public GoogleMap map;
    public final LatLng CUATROVIENTOS = new LatLng(42.824851, -1.660318);
    private final List<Polyline> polylineList = new ArrayList<>();

    public MapHelper(GoogleMap map) {
        this.map = map;
    }

    public void drawRoute(List<List<LatLng>> routes) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {
            List<PolylineOptions> polylineOptionsList = new ArrayList<>();
            for (List<LatLng> route : routes) {
                int index = routes.indexOf(route);
                PolylineOptions borderPolylineOptions = createPolylineOptions(route, index, true);
                PolylineOptions polylineOptions = createPolylineOptions(route, index, false);
                polylineOptionsList.add(borderPolylineOptions);
                polylineOptionsList.add(polylineOptions);
            }

            handler.post(() -> {
                if (map != null) {
                    polylineList.clear(); // Clear previous polylines if you're redrawing them
                    for (PolylineOptions options : polylineOptionsList) {
                        Polyline polyline = map.addPolyline(options);
                        polylineList.add(polyline);
                    }
                }
            });

        });
    }

    public PolylineOptions createPolylineOptions(List<LatLng> routeCoordinates, int index, boolean isBorder) {
        int mainBlue = Color.parseColor("#0F53FF");
        int mainBlueBorder = Color.parseColor("#0F26F5");
        int subtleBlue = Color.parseColor("#BCCEFB");
        int subtleBlueBorder = Color.parseColor("#6A83D7");

        int routeColor = index != 0 ? subtleBlue : mainBlue;
        int borderColor = index != 0 ? subtleBlueBorder : mainBlueBorder;

        PolylineOptions polylineOptions = new PolylineOptions();
        polylineOptions.addAll(routeCoordinates);
        polylineOptions.zIndex(index == 0 ? 1 : 0);
        if (isBorder) {
            polylineOptions.width(30).color(borderColor);
        } else {
            polylineOptions.width(25).color(routeColor);
        }
        return polylineOptions;
    }

    public void highlightRoute(int selectedRouteIndex) {
        final int selectedZIndex = 10; // Higher zIndex for selected route
        final int defaultZIndex = 0; // Default zIndex for unselected routes

        for (int i = 0; i < polylineList.size(); i++) {
            Polyline polyline = polylineList.get(i);
            // Assuming each route has a border and main line, adjust the index calculation as necessary
            int routeIndex = i / 2; // Adjust based on your actual storage logic
            if (routeIndex == selectedRouteIndex) {
                // This is the selected route
                if (i % 2 == 0) { // Assuming even indices are borders
                    polyline.setColor(Color.parseColor("#0F26F5")); // mainBlueBorder for borders
                    polyline.setZIndex(selectedZIndex);
                } else {
                    polyline.setColor(Color.parseColor("#0F53FF")); // mainBlue for the main line
                    polyline.setZIndex(selectedZIndex);
                }
            } else {
                // This is not the selected route
                if (i % 2 == 0) { // Assuming even indices are borders
                    polyline.setColor(Color.parseColor("#6A83D7")); // subtleBlueBorder for borders
                    polyline.setZIndex(defaultZIndex);
                } else {
                    polyline.setColor(Color.parseColor("#BCCEFB")); // subtleBlue for the main line
                    polyline.setZIndex(defaultZIndex);
                }
            }
        }
    }
}
