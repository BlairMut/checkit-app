package com.example.checkit_app;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BaseCampActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String CORSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final String BACK_LOCATION = Manifest.permission.ACCESS_BACKGROUND_LOCATION;

    private boolean mLocationPermissionGranted = false;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private MarkerOptions options;
    private EditText searchText;
    private ImageView mGps;
    private Button baseCampLocationButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        searchText = (EditText)findViewById(R.id.input_search);
        baseCampLocationButton = (Button)findViewById(R.id.buttonBaseCamp);
        baseCampLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                    LatLng currerntMarked = options.getPosition();
                    Log.d("TAG","Based Camped Location: "+ currerntMarked.latitude+" , "+currerntMarked.longitude);

            }
        });

        searchText.setFocusable(false);
        mGps = (ImageView)findViewById(R.id.ic_gps);

        Places.initialize(getApplicationContext(),"AIzaSyBdcKjWCFvW4gUYxRoXSSYJjVrW0ypi-Wc");

        searchText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Place.Field> fieldList = Arrays.asList(Place.Field.ADDRESS, Place.Field.LAT_LNG,Place.Field.NAME);
                Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY,fieldList).build(BaseCampActivity.this);
                Log.d("TAG","onClick - SearchText  Starting AutoComplete.....");
                startActivityForResult(intent,100);
            }
        });

        getLocationPermission();


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d("TAG","onClick - SearchText  Starting AutoComplete.....");
        if(requestCode == 100 && resultCode == RESULT_OK)
        {
            Place place = Autocomplete.getPlaceFromIntent(data);
            searchText.setText(place.getAddress());
            geoLocate();
        }
        else
        {
            Status status = Autocomplete.getStatusFromIntent(data);
            Log.e("TAG","AutoComplete Error: "+status.getStatusMessage());
        }
    }

    private void init()
    {
        Log.d("TAG","Initializing");

        searchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_SEARCH
                || actionId == EditorInfo.IME_ACTION_DONE
                || event.getAction() == KeyEvent.ACTION_DOWN
                || event.getAction() == KeyEvent.KEYCODE_ENTER)
                {
                    geoLocate();
                }


                return false;
            }
        });


        mGps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("TAG","onClick: clicked gps icon");
                getCurrentDeviceLocation();
            }
        });
        hideSoftKeyBoard();
    }

    private void geoLocate()
    {
        Log.d("TAG","GeoLocating...");

        String searchString = searchText.getText().toString();

        Geocoder geocoder = new Geocoder(BaseCampActivity.this);
        List<Address> list = new ArrayList<>();

        try {
            list = geocoder.getFromLocationName(searchString,1);
        }catch(IOException e)
        {
            Log.e("TAG","geoLocate: IOException: "+e.getMessage());
        }

        if(list.size()>0)
        {
            Address address = list.get(0);
            Log.d("TAG","geolocate: found a location "+ address.toString());
            moveCamera(new LatLng(address.getLatitude(),address.getLongitude()),15f,address.getAddressLine(0));
        }
    }

    private void initMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;

        Log.d("TAG", "Invoking onRequestPermissionsResult() ");

        switch (requestCode) {
            case 101: {
                if (grantResults.length > 0) {
                    for (int i = 0; i < grantResults.length; i++) {
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                            Log.d("TAG", "Permission Not Granted");
                            mLocationPermissionGranted = false;
                            return;
                        }
                    }
                    mLocationPermissionGranted = true;
                    initMap();
                    Log.d("TAG", "Initializing Map ");
                }
            }
        }
    }

    private void getLocationPermission() {
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_BACKGROUND_LOCATION};

        if (ContextCompat.checkSelfPermission(this.getApplicationContext(), FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this.getApplicationContext(), CORSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                if(ContextCompat.checkSelfPermission(this.getApplicationContext(), BACK_LOCATION) == PackageManager.PERMISSION_GRANTED)
                {
                    mLocationPermissionGranted = true;
                    initMap();
                    Log.d("TAG", "All the permission are Granted ");
                }
                else
                {
                    ActivityCompat.requestPermissions(this, permissions, 101);
                    Log.d("TAG", "Asking for Background Location Permission ");
                }
            } else {
                ActivityCompat.requestPermissions(this, permissions, 101);
                Log.d("TAG", "Asking for Corse Location Permission ");
            }
        } else {
            ActivityCompat.requestPermissions(this, permissions, 101);
            Log.d("TAG", "Asking for fine Location Permission ");
        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        Log.d("TAG", "Map is Ready ");
        Toast.makeText(this, "Map is Ready", Toast.LENGTH_SHORT).show();

        if (mLocationPermissionGranted) {
            getCurrentDeviceLocation();
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(false);
        init();
    }

     private void getCurrentDeviceLocation()
     {

         Log.d("TAG","Getting Device's current location");
         final LatLng[] latLng = new LatLng[1];
         fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

         try {
             if(mLocationPermissionGranted)
             {
                 Task location = fusedLocationProviderClient.getLastLocation();
                 location.addOnCompleteListener(new OnCompleteListener() {
                     @Override
                     public void onComplete(@NonNull Task task) {
                         if(task.isSuccessful() && task.getResult()!= null)
                         {
                             Log.d("TAG","onComplete: found Location !");
                             Location currentLocation = (Location) task.getResult();

                              latLng[0] = new LatLng(currentLocation.getLatitude(),currentLocation.getLongitude());
                             moveCamera(latLng[0],15f,"Your Location");



                         }
                         else
                         {
                             Log.d("TAG","onComplete: current location is null");
                             Toast.makeText(BaseCampActivity.this,"unable to get current location",Toast.LENGTH_SHORT).show();
                         }
                     }
                 });
             }
         }
         catch(SecurityException ex)
         {
             Log.e("TAG","getCurrentDeviceLocation: SecurityException: "+ ex.getMessage());
         }

     }

     private void moveCamera(LatLng latLng,float zoom,String title)
     {
         Log.d("TAG","moveCamera: moving camera to : lat"+latLng.latitude+" ,lng: "+latLng.longitude);
         mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,zoom));

             mMap.clear();
             options = new MarkerOptions().position(latLng).title(title);
             mMap.addMarker(options);


         hideSoftKeyBoard();
     }

     private void hideSoftKeyBoard()
     {
         this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN );
     }


}