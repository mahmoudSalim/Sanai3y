package com.example.mahmo.sanai3y.response;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Ahmed on 12-Sep-17.
 */

public class WorkerResponse {
    @SerializedName("Id")
    private int id;
    @SerializedName("Name")
    private String name;
    @SerializedName("PhoneNumber")
    private String phoneNumber;
    @SerializedName("ImageUrl")
    private String imageUrl;
    @SerializedName("Type")
    private int type;
    @SerializedName("CriminalRecordUrl")
    private String criminalRecordUrl;
    @SerializedName("NationalIdUrl")
    private String nationalIdUrl;
    @SerializedName("Rate")
    private int rate;
    @SerializedName("Reviews")
    private ArrayList<ReviewResponse> reviews;
    @SerializedName("Location")
    private GeoLocationResponse location;
    @SerializedName("State")
    private int state;
    @SerializedName("Jobs")
    private ArrayList<JobResponse> jobs;

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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getCriminalRecordUrl() {
        return criminalRecordUrl;
    }

    public void setCriminalRecordUrl(String criminalRecordUrl) {
        this.criminalRecordUrl = criminalRecordUrl;
    }

    public String getNationalIdUrl() {
        return nationalIdUrl;
    }

    public void setNationalIdUrl(String nationalIdUrl) {
        this.nationalIdUrl = nationalIdUrl;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public ArrayList<ReviewResponse> getReviews() {
        return reviews;
    }

    public void setReviews(ArrayList<ReviewResponse> reviews) {
        this.reviews = reviews;
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

    public ArrayList<JobResponse> getJobs() {
        return jobs;
    }

    public void setJobs(ArrayList<JobResponse> jobs) {
        this.jobs = jobs;
    }
}
