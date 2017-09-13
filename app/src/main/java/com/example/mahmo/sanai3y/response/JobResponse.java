package com.example.mahmo.sanai3y.response;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Ahmed on 12-Sep-17.
 */

public class JobResponse {
    @SerializedName("Id")
    private int id;
    @SerializedName("ClientId")
    private int clientId;
    @SerializedName("WorkerId")
    private int workerId;
    @SerializedName("Location")
    private GeoLocationResponse location;
    @SerializedName("State")
    private int state;
    @SerializedName("Date")
    private String date;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    public int getWorkerId() {
        return workerId;
    }

    public void setWorkerId(int workerId) {
        this.workerId = workerId;
    }

    public GeoLocationResponse getLocation() {
        return location;
    }

    public void setLocation(GeoLocationResponse location) {
        this.location = location;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
