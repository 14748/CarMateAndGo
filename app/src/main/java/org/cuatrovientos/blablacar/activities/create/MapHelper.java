package org.cuatrovientos.blablacar.activities.create;

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
                    polylineList.clear(); 
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
        final int selectedZIndex = 10; 
        final int defaultZIndex = 0; 

        for (int i = 0; i < polylineList.size(); i++) {
            Polyline polyline = polylineList.get(i);
            
            int routeIndex = i / 2; 
            if (routeIndex == selectedRouteIndex) {
                
                if (i % 2 == 0) { 
                    polyline.setColor(Color.parseColor("#0F26F5")); 
                    polyline.setZIndex(selectedZIndex);
                } else {
                    polyline.setColor(Color.parseColor("#0F53FF")); 
                    polyline.setZIndex(selectedZIndex);
                }
            } else {
                
                if (i % 2 == 0) { 
                    polyline.setColor(Color.parseColor("#6A83D7")); 
                    polyline.setZIndex(defaultZIndex);
                } else {
                    polyline.setColor(Color.parseColor("#BCCEFB")); 
                    polyline.setZIndex(defaultZIndex);
                }
            }
        }
    }
}
