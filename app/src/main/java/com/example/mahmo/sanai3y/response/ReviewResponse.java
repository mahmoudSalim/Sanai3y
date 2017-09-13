package com.example.mahmo.sanai3y.response;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Ahmed on 12-Sep-17.
 */

public class ReviewResponse {
    @SerializedName("Id")
    private int id;
    @SerializedName("ClientId")
    private int clientId;
    @SerializedName("Rate")
    private int rate;

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

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }
}
