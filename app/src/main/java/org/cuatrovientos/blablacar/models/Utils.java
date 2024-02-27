package org.cuatrovientos.blablacar.models;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

public class Utils {
    public static List<LatLng> convertCustomLatLngListToLatLngList(List<CustomLatLng> customLatLngList) {
        List<LatLng> latLngList = new ArrayList<>();
        for (CustomLatLng customLatLng : customLatLngList) {
            LatLng latLng = new LatLng(customLatLng.getLatitude(), customLatLng.getLongitude());
            latLngList.add(latLng);
        }
        return latLngList;
    }
    public static List<CustomLatLng> convertLatLngListToCustomLatLngList(List<LatLng> latLngList) {
        List<CustomLatLng> customLatLngList = new ArrayList<>();
        for (LatLng latLng : latLngList) {
            CustomLatLng customLatLng = new CustomLatLng(latLng.latitude, latLng.longitude);
            customLatLngList.add(customLatLng);
        }
        return customLatLngList;
    }

    public static List<List<LatLng>> convertListOfCustomLatLngListToListOfLatLngList(List<List<CustomLatLng>> customLatLngLists) {
        List<List<LatLng>> latLngLists = new ArrayList<>();
        for (List<CustomLatLng> customLatLngList : customLatLngLists) {
            List<LatLng> latLngList = convertCustomLatLngListToLatLngList(customLatLngList);
            latLngLists.add(latLngList);
        }
        return latLngLists;
    }

}
