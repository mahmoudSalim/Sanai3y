package com.example.mahmo.sanai3y.request;

import com.example.mahmo.sanai3y.response.ClientResponse;

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

public interface ClientRequests {
    @GET("clients")
    Call<ArrayList<ClientResponse>> getAll();

    @GET("clients/{id}")
    Call<ClientResponse> get(@Path("id") int id);

    @GET("clients/phone/{phoneNumber}")
    Call<ClientResponse> getByPhoneNumber(@Path("phoneNumber") String phoneNumber);

    @FormUrlEncoded
    @POST("clients")
    Call<ClientResponse> create(@Field("Name") String name, @Field("PhoneNumber") String phoneNumber, @Field("ImageUrl") String imageUrl);

    @FormUrlEncoded
    @POST("clients/{id}")
    Call<Void> update(@Path("id") int id, @Field("Name") String name, @Field("PhoneNumber") String phoneNumber, @Field("ImageUrl") String imageUrl);

    @DELETE("clients/{id}")
    Call<ClientResponse> delete(@Path("id") int id);
}
