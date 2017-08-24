package com.example.mahmo.sanai3y;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

public class UserProfileActivity extends AppCompatActivity {

    private static final String TAG = "UserProfileActivity";
    private ImageView userProfilePhoto;
    private TextView userProfileName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        userProfilePhoto = (ImageView) findViewById(R.id.user_profile_photo);
        userProfileName = (TextView) findViewById(R.id.user_profile_name);

        Intent intent = getIntent();
/*
        String id = intent.getStringExtra("id");
        Log.i(TAG, "id = " + id);
        String name = intent.getStringExtra("name");
        Log.i(TAG, "name = " + name);
        Uri imageUri = Uri.parse(intent.getStringExtra("imageUri"));
        Log.i(TAG, "Uri = " + imageUri);

        userProfileName.setText(name);
        Log.i(TAG, "Text View Facebook Name Changed");

        if (imageUri != null) {
            Picasso.with(UserProfileActivity.this).load(imageUri).resize(100, 100).into(userProfilePhoto);
            Log.i(TAG, "Image View Facebook Image Changed");
        } else Toast.makeText(getApplicationContext(), "Null", Toast.LENGTH_LONG).show();
*/
        // from Marker
        {
            String thisName = intent.getStringExtra("infoName");
            Bundle myBundle = this.getIntent().getExtras();
            int pic = myBundle.getInt("infoUserImage");

            userProfileName.setText(thisName);
            userProfilePhoto.setImageResource(pic);
        }
    }
}
