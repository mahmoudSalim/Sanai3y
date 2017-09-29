package com.example.mahmo.sanai3y.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.example.mahmo.sanai3y.R;
import com.example.mahmo.sanai3y.response.ClientResponse;
import com.example.mahmo.sanai3y.response.WorkerResponse;
import com.example.mahmo.sanai3y.util.Snai3yApplication;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i;
                if (Snai3yApplication.getUserId() == -1) {
                    i = new Intent(SplashActivity.this, StartActivity.class);
                    startActivity(i);
                } else if (Snai3yApplication.getUserType() == 1) {
                    callClientService(SplashActivity.this, ClientMapActivity.class, StartActivity.class);
                } else {
                    callWorkerService(SplashActivity.this, WorkerMapActivity.class, StartActivity.class);
                }
                finish();
            }
        }, 3000);
    }

    private void callClientService(final Context sourceActivity,
                                   final Class destinationActivity,
                                   final Class elseDestinatioActivity) {
        Call<ClientResponse> call = Snai3yApplication.getClientRequests().get(Snai3yApplication.getUserId());

        call.enqueue(new Callback<ClientResponse>() {
            @Override
            public void onResponse(Call<ClientResponse> call, Response<ClientResponse> response) {
                if (response.isSuccessful()) {
                    Snai3yApplication.setClientResponse(response.body());
                    Intent intent = new Intent(sourceActivity, destinationActivity);
                    startActivity(intent);
                    finish();
                } else {
                    Snai3yApplication.setUserId(-1);
                    Intent intent = new Intent(sourceActivity, elseDestinatioActivity);
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void onFailure(Call<ClientResponse> call, Throwable t) {
                Toast.makeText(SplashActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void callWorkerService(final Context sourceActivity,
                                   final Class destinationActivity,
                                   final Class elseDestinatioActivity) {
        Call<WorkerResponse> call = Snai3yApplication.getWorkerRequests().get(Snai3yApplication.getUserId());

        call.enqueue(new Callback<WorkerResponse>() {

            @Override
            public void onResponse(Call<WorkerResponse> call, Response<WorkerResponse> response) {
                if (response.isSuccessful()) {
                    Snai3yApplication.setWorkerResponse(response.body());
                    Intent intent = new Intent(sourceActivity, destinationActivity);
                    startActivity(intent);
                    finish();
                } else {
                    Snai3yApplication.setUserId(-1);
                    Intent intent = new Intent(sourceActivity, elseDestinatioActivity);
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void onFailure(Call<WorkerResponse> call, Throwable t) {
                Toast.makeText(SplashActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}