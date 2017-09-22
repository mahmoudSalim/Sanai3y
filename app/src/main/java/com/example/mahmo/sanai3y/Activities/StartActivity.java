package com.example.mahmo.sanai3y.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.mahmo.sanai3y.R;
import com.example.mahmo.sanai3y.util.Snai3yApplication;

public class StartActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnClientStart, btnWorkerStart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        btnClientStart = findViewById(R.id.btnClientStart);
        btnClientStart.setOnClickListener(this);
        btnWorkerStart = findViewById(R.id.btnWorkerStart);
        btnWorkerStart.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btnClientStart) {
            Snai3yApplication.setUserType(1);
        } else {
            Snai3yApplication.setUserType(2);
        }
        Intent i = new Intent(StartActivity.this, ActivityPhoneAuth.class);
        startActivity(i);
        finish();
    }
}
