package com.example.mahmo.sanai3y.Activities;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mahmo.sanai3y.R;
import com.example.mahmo.sanai3y.response.ClientResponse;
import com.example.mahmo.sanai3y.util.Snai3yApplication;

import java.io.File;
import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ClientRegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int CAMERA_REQUEST = 1;
    private static final int GALLERY_REQUEST = 2;
    EditText etName;
    TextView etPhoneNumber;
    Button btnRegister;
    ImageView ivImage;
    Uri selectedImage;
    File imageFile;

    @Override
    public void onBackPressed() {
        Intent i = new Intent(this, LoginActivity.class);
        startActivity(i);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_register);

        etName = findViewById(R.id.etName);
        etPhoneNumber = findViewById(R.id.etRegisterPhoneNumber);
        etPhoneNumber.setText(getIntent().getStringExtra("PHONE"));

        btnRegister = findViewById(R.id.btnRegister);
        ivImage = findViewById(R.id.ivImage);

        btnRegister.setOnClickListener(this);
        ivImage.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivImage:
                showChooserDialog();
                break;
            case R.id.btnRegister:
                registerNewUser();
                break;
        }
    }

    private void registerNewUser() {
        String name = etName.getText().toString();
        String phoneNumber = etPhoneNumber.getText().toString();

        if (name.isEmpty() || phoneNumber.isEmpty()) {
            Toast.makeText(this, "Complete All Data", Toast.LENGTH_SHORT).show();
        } else {
            Call<ClientResponse> call = Snai3yApplication.getClientRequests().create(name, phoneNumber, (imageFile != null) ? imageFile.toString() : "");
            call.enqueue(new Callback<ClientResponse>() {
                @Override
                public void onResponse(Call<ClientResponse> call, Response<ClientResponse> response) {
                    if (response.isSuccessful()) {
                        ClientResponse client = response.body();
                        Snai3yApplication.setUserId(client.getId());
                        Intent i = new Intent(ClientRegisterActivity.this, ClientMapActivity.class);
                        startActivity(i);
                        finish();
                    } else {
                        try {
                            Toast.makeText(ClientRegisterActivity.this, response.code() + " " + response.message() + " " + response.errorBody().string(), Toast.LENGTH_SHORT).show();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<ClientResponse> call, Throwable t) {
                    t.printStackTrace();
                }
            });


        }

    }

    private void showChooserDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose One Option");
        builder.setItems(new String[]{"Gallery", "Camera"}, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                if (i == 0) {
                    pickImageFromGallery();
                } else {
                    captureImageFromCamera();
                }
            }
        });
        builder.show();
    }

    private void captureImageFromCamera() {
        //Open Camera by Intent
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //Calls start activity for result as camera will opens and send Result to me
        startActivityForResult(cameraIntent, CAMERA_REQUEST);

    }

    private void pickImageFromGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        // Start the Intent
        startActivityForResult(galleryIntent, GALLERY_REQUEST);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (resultCode == RESULT_OK && null != data) {
                // When an Image is picked
                if (requestCode == GALLERY_REQUEST) {
                    // The URI of image to get from gallery content provider
                    selectedImage = data.getData();

                    readImageFromFile();

                } else {
                    //Get image bitmap from the intent that camera create and send it to me
                    Bitmap photo = (Bitmap) data.getExtras().get("data");
                    ivImage.setImageBitmap(photo);
                }
            } else {
                Toast.makeText(this, "You haven't picked Image", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show();
        }

    }

    private void readImageFromFile() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 12);
        } else {
            // query and Get result
            Cursor cursor = getContentResolver().query(selectedImage, new String[]{MediaStore.Images.Media.DATA}, null, null, null);

            // Move to first row
            cursor.moveToFirst();

            //Get image path
            String imagePath = cursor.getString(0);

            imageFile = new File(imagePath);

            //close the cursor
            cursor.close();

            //Set the image (File) to image view using bitmap factory
            ivImage.setImageBitmap(BitmapFactory.decodeFile(imagePath));
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 12) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                readImageFromFile();
            }
        }
    }

}
