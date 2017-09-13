package com.example.mahmo.sanai3y.response;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Ahmed on 12-Sep-17.
 */

public class OrderResponse {
    @SerializedName("Id")
    private int id;
    @SerializedName("ClientId")
    private int clientId;
    @SerializedName("WorkerType")
    private int workerType;
    @SerializedName("Location")
    private GeoLocationResponse location;
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

    public int getWorkerType() {
        return workerType;
    }

    public void setWorkerType(int workerType) {
        this.workerType = workerType;
    }

    public GeoLocationResponse getLocation() {
        return location;
    }

    public void setLocation(GeoLocationResponse location) {
        this.location = location;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
