package com.example.mahmo.sanai3y.Activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mahmo.sanai3y.R;
import com.example.mahmo.sanai3y.response.WorkerResponse;
import com.example.mahmo.sanai3y.util.Snai3yApplication;

import java.io.File;
import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WorkerRegisterActivity extends AppCompatActivity {

    private static final String TAG = "SignupActivity";

    private static final int CAMERA_REQUEST = 1;
    private static final int GALLERY_REQUEST = 2;

    @Bind(R.id.input_name)
    EditText _nameText;
    @Bind(R.id.input_address)
    EditText _addressText;
    @Bind(R.id.input_email)
    EditText _emailText;
    @Bind(R.id.input_mobile)
    TextView _mobileText;
    @Bind(R.id.input_password)
    EditText _passwordText;
    @Bind(R.id.input_reEnterPassword)
    EditText _reEnterPasswordText;
    @Bind(R.id.btn_signup)
    Button _signupButton;
    @Bind(R.id.link_login)
    TextView _loginLink;

    ImageView ivImage;
    ImageView ivNID_Image;
    ImageView ivCreminalRecordImage;

    File imageFile;

    Uri selectedImage;


    @Override
    public void onBackPressed() {
        Intent i = new Intent(this, LoginActivity.class);
        startActivity(i);
        finish();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worker_register);
        ButterKnife.bind(this);

        _signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });

        _loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the registration screen and return to the Login activity
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });

        ivImage = findViewById(R.id.ivImage);
        ivNID_Image = findViewById(R.id.ivNID_Image);
        ivCreminalRecordImage = findViewById(R.id.ivCreminalRecordImage);

        ivImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showChooserDialog();
            }
        });

        ivNID_Image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showChooserDialog();
            }
        });

        ivCreminalRecordImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showChooserDialog();
            }
        });

        _mobileText.setText(getIntent().getStringExtra("PHONE"));

    }

    public void signup() {
        Log.d(TAG, "Signup");

        if (!validate()) {
            onSignupFailed();
            return;
        }

        _signupButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(WorkerRegisterActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");
        progressDialog.show();

        String name = _nameText.getText().toString();
        String address = _addressText.getText().toString();
        String email = _emailText.getText().toString();
        String mobile = _mobileText.getText().toString();
        String password = _passwordText.getText().toString();
        String reEnterPassword = _reEnterPasswordText.getText().toString();

        // TODO: Implement your own signup logic here.
        callService(WorkerRegisterActivity.this,
                WorkerMapActivity.class,
                name,
                mobile);

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onSignupSuccess or onSignupFailed
                        // depending on success
                        onSignupSuccess();
                        // onSignupFailed();
                        progressDialog.dismiss();
                    }
                }, 3000);
    }


    public void onSignupSuccess() {
        _signupButton.setEnabled(true);
        setResult(RESULT_OK, null);
        finish();
    }

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        _signupButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String name = _nameText.getText().toString();
        String address = _addressText.getText().toString();
        String email = _emailText.getText().toString();
        String mobile = _mobileText.getText().toString();
        String password = _passwordText.getText().toString();
        String reEnterPassword = _reEnterPasswordText.getText().toString();

        if (name.isEmpty() || name.length() < 3) {
            _nameText.setError("at least 3 characters");
            valid = false;
        } else {
            _nameText.setError(null);
        }

        if (address.isEmpty()) {
            _addressText.setError("Enter Valid Address");
            valid = false;
        } else {
            _addressText.setError(null);
        }


        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("enter a valid email address");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (mobile.isEmpty() || mobile.length() != 10) {
            _mobileText.setError("Enter Valid Mobile Number");
            valid = false;
        } else {
            _mobileText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        if (reEnterPassword.isEmpty() || reEnterPassword.length() < 4 || reEnterPassword.length() > 10 || !(reEnterPassword.equals(password))) {
            _reEnterPasswordText.setError("Password Do not match");
            valid = false;
        } else {
            _reEnterPasswordText.setError(null);
        }

        return valid;
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

    private void callService(final Context sourceActivity,
                             final Class destinationActivity,
                             String name,
                             String phoneNumber) {
        Call<WorkerResponse> call = Snai3yApplication.getWorkerRequests()
                .create(name,
                        phoneNumber,
                        (imageFile != null) ? imageFile.toString() : "",
                        Snai3yApplication.getUserType(),
                        (imageFile != null) ? imageFile.toString() : "",
                        (imageFile != null) ? imageFile.toString() : "");

        call.enqueue(new Callback<WorkerResponse>() {
            @Override
            public void onResponse(Call<WorkerResponse> call, Response<WorkerResponse> response) {
                if (response.isSuccessful()) {
                    WorkerResponse client = response.body();
                    Snai3yApplication.setUserId(client.getId());
                    Intent i = new Intent(sourceActivity, destinationActivity);
                    startActivity(i);
                    finish();
                } else {
                    try {
                        Toast.makeText(WorkerRegisterActivity.this, response.code() + " " + response.message() + " " + response.errorBody().string(), Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<WorkerResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

}
