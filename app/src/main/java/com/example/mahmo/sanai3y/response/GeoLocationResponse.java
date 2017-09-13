package com.example.mahmo.sanai3y.response;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Ahmed on 12-Sep-17.
 */

public class GeoLocationResponse {
    @SerializedName("Id")
    private int id;
    @SerializedName("Longitude")
    private double longitude;
    @SerializedName("Latitude")
    private double latitude;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
}
