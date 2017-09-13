package com.example.mahmo.sanai3y.request;

import com.example.mahmo.sanai3y.response.GeoLocationResponse;
import com.example.mahmo.sanai3y.response.WorkerResponse;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by Ahmed on 12-Sep-17.
 */

public interface WorkerRequests {
    @GET("workers")
    Call<ArrayList<WorkerResponse>> getAll();

    @GET("workers/{id}")
    Call<WorkerResponse> get(@Path("id") int id);

    @GET("workers/phone{phoneNumber}")
    Call<WorkerResponse> getByPhoneNumber(@Path("phoneNumber") String phoneNumber);

    @FormUrlEncoded
    @POST("workers")
    Call<WorkerResponse> create(@Field("Name") String name, @Field("PhoneNumber") String phoneNumber, @Field("ImageUrl") String imageUrl, @Field("Type") int type, @Field("CriminalRecordUrl") String criminalRecordUrl, @Field("NationalIdUrl") String nationalIdUrl);

    @FormUrlEncoded
    @POST("workers/{id}")
    Call<Void> update(@Path("id") int id, @Field("Name") String name, @Field("PhoneNumber") String phoneNumber, @Field("ImageUrl") String imageUrl, @Field("Type") int type, @Field("CriminalRecordUrl") String criminalRecordUrl, @Field("NationalIdUrl") String nationalIdUrl);

    @FormUrlEncoded
    @POST("workers/{id}/state")
    Call<Void> setState(@Path("id") int id, @Field("Location") GeoLocationResponse location, @Field("State") int state);

    @FormUrlEncoded
    @POST("workers/{id}/review")
    Call<Void> submitReview(@Path("id") int id, @Field("ClientId") int clientId, @Field("Rate") int rate);

    @DELETE("workers/{id}")
    Call<WorkerResponse> delete(@Path("id") int id);
}
