package com.example.mahmo.sanai3y.Activities;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mahmo.sanai3y.DetectedActivitiesIntentService;
import com.example.mahmo.sanai3y.R;
import com.example.mahmo.sanai3y.response.ClientResponse;
import com.example.mahmo.sanai3y.response.GeoLocationResponse;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.ActivityRecognition;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class ClientMapActivity extends FragmentActivity implements
        OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        //LocationListener,
        com.google.android.gms.location.LocationListener {

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    public static final int MY_PERMISSIONS_REQUEST_NETWORK = 33;
    private static final String REQUESTING_LOCATION_UPDATES_KEY = "requestLocationUpdateKey";
    private static final String LATITUDE_KEY = "latitudeKey";
    private static final String LONGITUDE_KEY = "longitudeKey";
    private static final String GPS_PRVIDER = LocationManager.GPS_PROVIDER;
    private static final String NETWORK_PROVIDER = LocationManager.NETWORK_PROVIDER;

    public boolean permissionIsGranted = false;
    Location location;
    ArrayAdapter<CharSequence> spinnerAdapter;
    private boolean networkPermissionIsGranted = false;
    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private Location mLastLocation;
    private Marker mCurrLocationMarker;
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationManager mLocationManager;
    private LocationListener mLocationListener;
    private LocationCallback mLocationCallback;

    //    private GPSTracker mGPSTracker;
    private boolean mRequestingLocationUpdates = false;
    private Spinner mapTypeSpinner;
    private double myLatitude;
    private double myLongitude;

    private GeoLocationResponse geoLocationResponse;

    private ClientResponse client;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            } else {
                permissionIsGranted = true;
            }
            return;
        }

        buildGoogleApiClient();
        locationCallbackInit();
        locationPermissionCheck();


        //mLocationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);
        getMyLocation();
//        updateUI(myLatitude, myLongitude);
        chooseMap();

        Log.v("test", "OCR myLatitude = " + myLatitude);
        Log.v("test", "OCR myLongitude = " + myLongitude);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (permissionIsGranted)
            mGoogleApiClient.disconnect();
        if (networkPermissionIsGranted)
            mGoogleApiClient.disconnect();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (permissionIsGranted) {
            if (mGoogleApiClient.isConnected()) {
                if (mRequestingLocationUpdates) {
                    startLocationUpdates();
                }
            }
        }
        if (networkPermissionIsGranted) {
            if (mGoogleApiClient.isConnected()) {
                if (mRequestingLocationUpdates) {
                    startLocationUpdates();
                }
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (permissionIsGranted)
            stopLocationUpdates();
        if (networkPermissionIsGranted)
            stopLocationUpdates();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(REQUESTING_LOCATION_UPDATES_KEY,
                mRequestingLocationUpdates);
        outState.putDouble(LATITUDE_KEY, myLatitude);
        outState.putDouble(LONGITUDE_KEY, myLongitude);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mRequestingLocationUpdates = savedInstanceState.getBoolean(REQUESTING_LOCATION_UPDATES_KEY);
        myLatitude = savedInstanceState.getDouble(LATITUDE_KEY);
        myLongitude = savedInstanceState.getDouble(LONGITUDE_KEY);

        updateUI(myLatitude, myLongitude);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {

        Log.v("test", "i'm in MAP_READY myLatitude = " + myLatitude);
        Bundle bundle = new Bundle();


        Log.v("test", "i'm in MAP_READY after excuting onConnected");

        mMap = googleMap;

        //Initialize Google Play Services
        buildGoogleApiClient();

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            } else {
                permissionIsGranted = true;
            }
            return;
        }
        mMap.setMyLocationEnabled(true);


    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(30000);
        mLocationRequest.setFastestInterval(30000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

//        setMyLocationUsingGPSTrackerClass();
        getMyLocation();
        //callActivityRecognitionIntentService();
        if (mGoogleApiClient.isConnected()) {
            Log.v("test", "OCON mGoogleApiClient is connected");
//            onMapReady(mMap);
            updateUI(myLatitude, myLongitude);
            Log.v("test", "OCON uiUpdated");
            Log.v("test", "IF OCON myLatitude IF = " + myLatitude);
            Log.v("test", "IF OCON myLongitude IF = " + myLongitude);
        }
        Log.v("test", "OCON myLatitude = " + myLatitude);
        Log.v("test", "OCON myLongitude = " + myLongitude);

//        geoLocationResponse.setLatitude(myLatitude);
//        geoLocationResponse.setLongitude(myLongitude);

        if (mGoogleApiClient.isConnected()) {
            Log.v("test", "Here !!!!!");
            addDummyMarkers();
        }


    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }

    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }

        myLatitude = location.getLatitude();
        myLongitude = location.getLongitude();

        Log.v("test", "OCH myLatitude = " + myLatitude);
        Log.v("test", "OCH myLongitude = " + myLongitude);
        Log.v("test", "OCH location.getLatitude() = " + location.getLatitude());
        Log.v("test", "OCH location.getLongitude() = " + location.getLongitude());


        LatLng myLatLng = new LatLng(location.getLatitude(), location.getLongitude());

        updateUI(location.getLatitude(), location.getLongitude());

     /*   //Place current location marker
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Current Position");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
        mCurrLocationMarker = mMap.addMarker(markerOptions);*/

        //move map camera
        mMap.moveCamera(CameraUpdateFactory.newLatLng(myLatLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(11));

        //stop location updates
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If com.example.mahmo.sanai3y.request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    permissionIsGranted = true;

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            android.Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {
                        //Request location updates:
                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);
                        mLocationManager.requestLocationUpdates(GPS_PRVIDER, 400, 1, (android.location.LocationListener) this);
                    }

                } else {
                    permissionIsGranted = false;
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                }
            }
            case MY_PERMISSIONS_REQUEST_NETWORK: {
                // If com.example.mahmo.sanai3y.request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    networkPermissionIsGranted = true;

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.CHANGE_NETWORK_STATE)
                            == PackageManager.PERMISSION_GRANTED) {
                        //Request location updates:
                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);
                        mLocationManager.requestLocationUpdates(NETWORK_PROVIDER, 400, 1, (android.location.LocationListener) this);
                    }

                } else {
                    networkPermissionIsGranted = false;
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        mGoogleApiClient.connect();
    }

    private void flyTo(CameraPosition target) {
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(target), 5000, null);
    }

    public void getMyLocation() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            } else {
                permissionIsGranted = true;
            }
            //return;
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation != null) {
            myLatitude = mLastLocation.getLatitude();
            myLongitude = mLastLocation.getLongitude();
        }
    }

    private void chooseMap() {
        mapTypeSpinner = findViewById(R.id.spinner);
        spinnerAdapter = ArrayAdapter.createFromResource(this, R.array.mapType, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mapTypeSpinner.setAdapter(spinnerAdapter);
        mapTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                        break;
                    case 1:
                        mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                        break;
                    case 2:
                        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            } else {
                permissionIsGranted = true;
            }
            return;
        }
        mFusedLocationClient.requestLocationUpdates(mLocationRequest,
                mLocationCallback,
                null/* Looper*/);
    }

    private void stopLocationUpdates() {
        mFusedLocationClient.removeLocationUpdates(mLocationCallback);
    }

    private void locationCallbackInit() {
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                for (Location location : locationResult.getLocations()) {
                    // Update UI with location data
                    myLatitude = location.getLatitude();
                    myLongitude = location.getLongitude();
                    updateUI(myLatitude, myLongitude);
                }
            }

        };
    }

    private void updateUI(double latitude, double longitude) {
        myLatitude = latitude;
        myLongitude = longitude;
        LatLng myLocation = new LatLng(latitude, longitude);
        Log.v("myTest", "lat = " + latitude + "\nlong = " + longitude);
        mMap.addMarker(new MarkerOptions().position(myLocation).title("Marker in My Location"));
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(22.0014, 35.4893))
                .title("Custom Marker")
                .icon(BitmapDescriptorFactory.fromResource(
                        R.mipmap.ic_launcher_round)));
        CameraPosition target = CameraPosition.builder()
                .target(myLocation)
                .zoom(15)
                .bearing(30)
                .tilt(45)
                .build();
        //mMap.moveCamera(CameraUpdateFactory.newCameraPosition(target));
        flyTo(target);
    }

    private void callActivityRecognitionIntentService() {
        Intent intent = new Intent(this, DetectedActivitiesIntentService.class);
        PendingIntent pendingIntent = PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        ActivityRecognition.ActivityRecognitionApi.requestActivityUpdates(mGoogleApiClient, 10000, pendingIntent);
    }

    private void locationPermissionCheck() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            } else {
                permissionIsGranted = true;
            }
            return;
        }
    }

    private void addDummyMarkers() {
        double[] markerLat = {myLatitude + 0.001, myLatitude + 0.003, myLatitude + 0.004, myLatitude + 0.005,
                myLatitude + 0.007, myLatitude + 0.009, myLatitude - 0.0011, myLatitude - 0.0013};
        double[] markerLong = {myLongitude + 0.001, myLongitude + 0.003, myLongitude + 0.005, myLongitude + 0.007,
                myLongitude + 0.009, myLongitude + 0.0011, myLongitude + 0.0013, myLongitude - 0.0015};
        String[] markerName = {"Constructor", "Miner", "Factory", "Plastering",
                "Electrical", "Engineer", "Warehouse", "Water Filter"};

        int[] markerIcon = {R.drawable.construction, R.drawable.mine, R.drawable.museum_industry,
                R.drawable.plastering_icon, R.drawable.poweroutage, R.drawable.surveying,
                R.drawable.warehouse, R.drawable.waterfilter};
        String snippet = "This is the description snippet of the Sanai3y";

        for (int i = 0; i < 8; i++) {
            MarkerOptions dummyMarkerOptions = new MarkerOptions()
                    .title(markerName[i])
                    .position(new LatLng(markerLat[i], markerLong[i]))
                    .icon(BitmapDescriptorFactory.fromResource(markerIcon[i]))
                    .snippet(snippet);

            mMap.addMarker(dummyMarkerOptions);
        }

        final String[] mInfoName = new String[1];
        if (mMap != null) {
            mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
                @Override
                public View getInfoWindow(Marker marker) {
                    return null;
                }

                @Override
                public View getInfoContents(Marker marker) {


                    View v = getLayoutInflater().inflate(R.layout.marker_info_window, null);

                    final TextView infoName = v.findViewById(R.id.infoName);
                    TextView infoSnippet = v.findViewById(R.id.infoSnippet);
                    TextView infoProfileLink = v.findViewById(R.id.infoProfileLink);
                    final ImageView infoImage = v.findViewById(R.id.infoImage);

                    infoName.setText(marker.getTitle());
                    infoSnippet.setText(marker.getSnippet());
                    infoImage.setImageResource(R.drawable.logo2);

                    mInfoName[0] = marker.getTitle();

                    return v;
                }
            });

            mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                @Override
                public void onInfoWindowClick(Marker marker) {
                    Intent intent = new Intent(ClientMapActivity.this, UserProfileActivity.class);
                    Bundle bundle = new Bundle();
                    intent.putExtra("infoName", mInfoName[0]);
                    bundle.putInt("infoUserImage", R.drawable.logo2);
                    intent.putExtras(bundle);

                    startActivity(intent);
                }
            });
        }
    }

    /*private void setMyLocationUsingGPSTrackerClass() {
        mGPSTracker = new GPSTracker(this);
        mLastLocation = mGPSTracker.getLocation();
        myLatitude = mLastLocation.getLatitude();
        myLongitude = mLastLocation.getLongitude();
    }*/

    // Lat-long coorditates for cities in Egypt are in range: Latitude from 24.09082 to 31.5084 and longitude from 25.51965 to 34.89005.


/*
    public class GPSTracker extends Service {

        private final Context mContext;

        // flag for GPS status
        boolean isGPSEnabled = false;

        // flag for network status
        boolean isNetworkEnabled = false;

        // flag for GPS status
        boolean canGetLocation = false;

        private double latitude;
        private double longitude;

        // The minimum distance to change Updates in meters
        private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters

        // The minimum time between updates in milliseconds
        private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 minute


        public GPSTracker(Context context) {
            this.mContext = context;
            getLocation();
        }

        public Location getLocation() {
            try {
                mLocationManager = (LocationManager) mContext
                        .getSystemService(LOCATION_SERVICE);

                // getting GPS status
                isGPSEnabled = mLocationManager
                        .isProviderEnabled(LocationManager.GPS_PROVIDER);

                // getting network status
                isNetworkEnabled = mLocationManager
                        .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

                if (!isGPSEnabled && !isNetworkEnabled) {
                    // no network provider is enabled
                    showSettingsAlert();
                    this.canGetLocation = true;
                }
                //else {
                this.canGetLocation = true;
                // First get location from Network Provider
                if (isNetworkEnabled) {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CHANGE_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.CHANGE_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            requestPermissions(new String[]{Manifest.permission.CHANGE_NETWORK_STATE},
                                    MY_PERMISSIONS_REQUEST_NETWORK);
                        } else {
                            networkPermissionIsGranted = true;
                        }
                    }
                    mLocationManager.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES,  (android.location.LocationListener) this);
                    Log.d("Network", "Network");
                    if (mLocationManager != null) {
                        location = mLocationManager
                                .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (location != null) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                        }
                    }
                }
                // if GPS Enabled get lat/long using GPS Services
                if (isGPSEnabled) {
                    if (location == null) {
                        mLocationManager.requestLocationUpdates(
                                LocationManager.GPS_PROVIDER,
                                MIN_TIME_BW_UPDATES,
                                MIN_DISTANCE_CHANGE_FOR_UPDATES, (android.location.LocationListener) this);
                        Log.d("GPS Enabled", "GPS Enabled");
                        if (mLocationManager != null) {
                            location = mLocationManager
                                    .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if (location != null) {
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                            }
                        }
                    }
                }
                //}

            } catch (Exception e) {
                e.printStackTrace();
            }

            return location;
        }

        *//**
     * Stop using GPS listener
     * Calling this function will stop using GPS in your app
     * *//*
        public void stopUsingGPS() {
            if (mLocationManager != null) {
                mLocationManager.removeUpdates((android.location.LocationListener) GPSTracker.this);
            }
        }

        public void requestLocationManagerUpdates() {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                            MY_PERMISSIONS_REQUEST_LOCATION);
                } else {
                    permissionIsGranted = true;
                }
                return;
            }
            mLocationManager.requestLocationUpdates(
                    GPS_PRVIDER,
                    MIN_TIME_BW_UPDATES,
                    MIN_DISTANCE_CHANGE_FOR_UPDATES,
                    (android.location.LocationListener) this);
        }

        public void stopLestiningToLocationUpdates(){
            mLocationManager.removeUpdates((android.location.LocationListener) this);
        }

        *//**
     * Function to get latitude
     * *//*
        public double getLatitude(){
            if(location != null){
                latitude = location.getLatitude();
            }

            // return latitude
            return latitude;
        }

        *//**
     * Function to get longitude
     * *//*
        public double getLongitude(){
            if(location != null){
                longitude = location.getLongitude();
            }

            // return longitude
            return longitude;
        }

        *//**
     * Function to check GPS/wifi enabled
     * @return boolean
     * *//*
        public boolean canGetLocation() {
            return this.canGetLocation;
        }

        *//**
     * Function to show settings alert dialog
     * On pressing Settings button will lauch Settings Options
     * *//*
        public void showSettingsAlert(){
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);

            // Setting Dialog Title
            alertDialog.setTitle("GPS is settings");

            // Setting Dialog Message
            alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");

            // On pressing Settings button
            alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog,int which) {
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    mContext.startActivity(intent);
                }
            });

            // on pressing cancel button
            alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

            // Showing Alert Message
            alertDialog.show();
        }

        @Override
        public IBinder onBind(Intent arg0) {
            return null;
        }
    }
*/
}
