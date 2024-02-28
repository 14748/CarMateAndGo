package org.cuatrovientos.blablacar.models;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.SerializedName;

public class PlaceOpenStreetMap implements Serializable {
    @SerializedName("place_id")
    private long placeId;

    private String licence;

    @SerializedName("osm_type")
    private String osmType;

    @SerializedName("osm_id")
    private long osmId;

    private String lat;

    private String lon;

    private String category;

    private String type;

    @SerializedName("place_rank")
    private int placeRank;

    private double importance;

    @SerializedName("addresstype")
    private String addressType;

    private String name;

    @SerializedName("display_name")
    private String displayName;

    private List<String> boundingbox;

    // Getter and setter methods for each field

    public long getPlaceId() {
        return placeId;
    }

    public void setPlaceId(long placeId) {
        this.placeId = placeId;
    }

    public String getLicence() {
        return licence;
    }

    public void setLicence(String licence) {
        this.licence = licence;
    }

    public String getOsmType() {
        return osmType;
    }

    public void setOsmType(String osmType) {
        this.osmType = osmType;
    }

    public long getOsmId() {
        return osmId;
    }

    public void setOsmId(long osmId) {
        this.osmId = osmId;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getPlaceRank() {
        return placeRank;
    }

    public void setPlaceRank(int placeRank) {
        this.placeRank = placeRank;
    }

    public double getImportance() {
        return importance;
    }

    public void setImportance(double importance) {
        this.importance = importance;
    }

    public String getAddressType() {
        return addressType;
    }

    public void setAddressType(String addressType) {
        this.addressType = addressType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public List<String> getBoundingbox() {
        return boundingbox;
    }

    public void setBoundingbox(List<String> boundingbox) {
        this.boundingbox = boundingbox;
    }

}
