package com.example.mahmo.sanai3y.response;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Ahmed on 12-Sep-17.
 */

public class ClientResponse {
    @SerializedName("Id")
    private int id;
    @SerializedName("Name")
    private String name;
    @SerializedName("PhoneNumber")
    private String phoneNumber;
    @SerializedName("ImageUrl")
    private String imageUrl;
    @SerializedName("Orders")
    private ArrayList<OrderResponse> orders;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public ArrayList<OrderResponse> getOrders() {
        return orders;
    }

    public void setOrders(ArrayList<OrderResponse> orders) {
        this.orders = orders;
    }
}
