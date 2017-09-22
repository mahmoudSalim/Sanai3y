package com.example.mahmo.sanai3y.util;

import android.app.Application;
import android.content.SharedPreferences;

import com.example.mahmo.sanai3y.request.ClientRequests;
import com.example.mahmo.sanai3y.request.WorkerRequests;
import com.example.mahmo.sanai3y.response.ClientResponse;
import com.example.mahmo.sanai3y.response.WorkerResponse;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Ahmed on 12-Sep-17.
 */

public class Snai3yApplication extends Application {

    private static final String PREF_NAME = "snai3y";
    private static final String USER_ID = "userId";
    private static final String USER_TYPE = "userType";
    private static SharedPreferences preferences;
    private static SharedPreferences.Editor editor;
    private static Retrofit retrofit;
    private static ClientRequests clientRequests;
    private static WorkerRequests workerRequests;
    private static ClientResponse clientResponse;
    private static WorkerResponse workerResponse;

    public static WorkerResponse getWorkerResponse() {
        return workerResponse;
    }

    public static void setWorkerResponse(WorkerResponse workerResponse) {
        Snai3yApplication.workerResponse = workerResponse;
    }

    public static ClientResponse getClientResponse() {
        return clientResponse;
    }

    public static void setClientResponse(ClientResponse clientResponse) {
        Snai3yApplication.clientResponse = clientResponse;
    }

    public static Retrofit getRetrofit() {
        return retrofit;
    }

    public static void setRetrofit(Retrofit retrofit) {
        Snai3yApplication.retrofit = retrofit;
    }

    public static ClientRequests getClientRequests() {
        return clientRequests;
    }

    public static WorkerRequests getWorkerRequests() {
        return workerRequests;
    }

    public static int getUserId() {
        return preferences.getInt(USER_ID, -1);
    }

    public static void setUserId(int id) {
        editor.putInt(USER_ID, id);
        editor.commit();
    }

    public static int getUserType() {
        return preferences.getInt(USER_TYPE, -1);
    }

    public static void setUserType(int userType) {
        editor.putInt(USER_TYPE, userType);
        editor.commit();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        preferences = getSharedPreferences(PREF_NAME, 0);
        editor = preferences.edit();

        retrofit = new Retrofit.Builder()
                .baseUrl("http://snai3y.azurewebsites.net/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        clientRequests = retrofit.create(ClientRequests.class);
        workerRequests = retrofit.create(WorkerRequests.class);

    }

}
