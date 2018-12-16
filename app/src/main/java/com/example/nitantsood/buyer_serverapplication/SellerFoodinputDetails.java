package com.example.nitantsood.buyer_serverapplication;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.animation.Interpolator;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TimePicker;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.security.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static com.example.nitantsood.buyer_serverapplication.FireApp.mAuth;

public class SellerFoodinputDetails extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener, OnMapReadyCallback {

    LocationManager locationManager;
    LocationListener locationListener;
    LatLng currentLatLang;
    private GoogleMap mMap;
    ArrayList<OnePoolItem> poolList;
    OneSellerDetail currentSellerLoggedIn;

    private DatabaseReference databaseReference;
    private Uri imageUri = null;
    private static final int GALLERY_REQUEST = 1;
    private StorageReference imageStorage;
    private ProgressDialog progressDialog;

    ImageButton imageButton;
    boolean firstTimeLocationPick = true;
    String seller_uid;
    Switch priceSwitch;
    java.sql.Timestamp expiryTimestamp;
    Calendar expiryCalendar = Calendar.getInstance();
    private String food_uid;
    EditText Title, Description, Quantity, Price, Expiry;
    private Firebase mRef, mRef1;
    ImageButton expiryPicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_foodinput_details);


        poolList=new ArrayList<>();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        imageStorage = FirebaseStorage.getInstance().getReference();

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                if (firstTimeLocationPick) {
                    firstTimeLocationPick = false;
                    currentLatLang = new LatLng(location.getLatitude(), location.getLongitude());
                    storeItem();
                }
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        Title = (EditText) findViewById(R.id.SellerFoodItemTitle);
        Description = (EditText) findViewById(R.id.SellerFoodItemDescription);
        Quantity = (EditText) findViewById(R.id.SellerFoodItemQuantity);
        Price = (EditText) findViewById(R.id.SellerFoodItemTitle);
        Expiry = (EditText) findViewById(R.id.SellerFoodItemExpiry);
        expiryPicker = (ImageButton) findViewById(R.id.SellerFoodItemExpiryPicker);
        priceSwitch = (Switch) findViewById(R.id.switch1);
        imageButton = (ImageButton) findViewById(R.id.imageButton);
        progressDialog = new ProgressDialog(this);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, GALLERY_REQUEST);
            }
        });
        priceSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked) {
                    Price.setText("");
//                    priceSwitch.setTextColor(Color.GREEN);
//                    Price.setHint("Price");
                    Price.setEnabled(true);
                } else {
//                    Price.setText("0");
//                    priceSwitch.setTextColor(Color.RED);
                    Price.setEnabled(false);
                }
            }
        });
        expiryPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int dom = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(SellerFoodinputDetails.this, SellerFoodinputDetails.this, year, month, dom);
                datePickerDialog.show();
            }
        });


        mRef1 = new Firebase("https://buyer-serverapplication.firebaseio.com/Pool");
        mRef1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                poolList.clear();
                for (DataSnapshot singlePoolItem : dataSnapshot.getChildren()) {
                    OnePoolItem onePoolItem;
                    onePoolItem = (OnePoolItem) singlePoolItem.getValue(OnePoolItem.class);
                    onePoolItem.setPool_UID(singlePoolItem.getKey());
                    poolList.add(onePoolItem);
                }

                Toast.makeText(SellerFoodinputDetails.this, "checkpoint 1 Cleared", Toast.LENGTH_SHORT).show();
                onMapReady(mMap);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
        mRef = new Firebase("https://buyer-serverapplication.firebaseio.com/Food_Item");
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        currentSellerLoggedIn = (OneSellerDetail) bundle.get("oneSeller");
        seller_uid = currentSellerLoggedIn.getSeller_UID();
        Toast.makeText(this, seller_uid, Toast.LENGTH_SHORT).show();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Date date = new Date();
                java.sql.Timestamp currentTimestamp = new java.sql.Timestamp(date.getTime());
                food_uid = currentTimestamp.getTime() + "";
                if (checkExpiryDelay(expiryTimestamp, currentTimestamp) && !priceSwitch.isChecked()) {
                    Toast.makeText(SellerFoodinputDetails.this, "Expiry time is less than 2 hours, Please make your Food Free for All !!", Toast.LENGTH_SHORT).show();
                } else if (Title.getText().toString().equals("")) {
                    Toast.makeText(SellerFoodinputDetails.this, "Please enter a valid Title for Food", Toast.LENGTH_SHORT).show();
                } else if (Quantity.getText().toString().equals("")) {
                    Toast.makeText(SellerFoodinputDetails.this, "Please enter a valid Quantity for Food", Toast.LENGTH_SHORT).show();
                } else if (!currentTimestamp.before(expiryTimestamp)) {
                    Toast.makeText(SellerFoodinputDetails.this, "Please choose a valid Expiry Time", Toast.LENGTH_SHORT).show();
                } else {
                    if (Build.VERSION.SDK_INT < 23) {
                        if (ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            //    ActivityCompat#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for ActivityCompat#requestPermissions for more details.
                            return;
                        }
                        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);

                    } else {
                        if (ContextCompat.checkSelfPermission(SellerFoodinputDetails.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(SellerFoodinputDetails.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                        } else {
                            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
                        }
                    }
                }

            }
        });
    }

    private boolean checkExpiryDelay(java.sql.Timestamp expiryTimestamp, java.sql.Timestamp currentTimestamp) {
        long difference = expiryTimestamp.getTime() - currentTimestamp.getTime();
//        long hoursDifference=(difference/(60*1000));
//        Log.v("time",hoursDifference+"");
        if (difference < (2 * 60 * 60 * 1000)) {
            return true;
        } else {
            return false;
        }
    }

    private void storeItem() {

        progressDialog.setMessage("kaam chal raha hai....");
        progressDialog.show();
        StorageReference filepath = imageStorage.child("Food Image").child(imageUri.getLastPathSegment());
        filepath.putFile(imageUri).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                taskSnapshot.getBytesTransferred();
                taskSnapshot.getTotalByteCount();
            }
        })
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        Uri downloadUri = taskSnapshot.getMetadata().getDownloadUrl();
                        OneFoodItem currentFoodItem = new OneFoodItem();
                        currentFoodItem.setTitle(Title.getText().toString());
                        currentFoodItem.setExpiry_date_time(expiryTimestamp.getTime() + "");
                        currentFoodItem.setDescription(Description.getText().toString());
                        if (priceSwitch.isChecked()) {
                            currentFoodItem.setPrice("0");
                        } else {
                            currentFoodItem.setPrice(Price.getText().toString());
                        }
                        currentFoodItem.setQuantity(Quantity.getText().toString());
                        currentFoodItem.setSeller_UID(seller_uid);
                        currentFoodItem.setSeller_lat(currentLatLang.latitude);
                        currentFoodItem.setPicture_URL(downloadUri.toString());
                        Log.v("hehe", downloadUri.toString());
                        currentFoodItem.setSeller_lng(currentLatLang.longitude);
                        currentFoodItem.setFood_UID(food_uid + "" + seller_uid);
                        Firebase oneFoodItemRef = mRef.child(food_uid + "" + seller_uid);
                        oneFoodItemRef.setValue(currentFoodItem);
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_LONG).show();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });


    }


    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        expiryCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        expiryCalendar.set(Calendar.MONTH, month);
        expiryCalendar.set(Calendar.YEAR, year);

        Calendar c = Calendar.getInstance();
        int hours = c.get(Calendar.HOUR_OF_DAY);
        int mins = c.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(SellerFoodinputDetails.this, SellerFoodinputDetails.this, hours, mins, true);
        timePickerDialog.show();
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        expiryCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        expiryCalendar.set(Calendar.MINUTE, minute);

        expiryTimestamp = new java.sql.Timestamp(expiryCalendar.getTimeInMillis());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        Expiry.setText(simpleDateFormat.format(expiryTimestamp));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_REQUEST && resultCode == RESULT_OK) {
            imageUri = data.getData();
            imageButton.setImageURI(imageUri);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        Toast.makeText(SellerFoodinputDetails.this, "checkpoint 2 Cleared", Toast.LENGTH_SHORT).show();

        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);
//        Toast.makeText(this,poolList.size(), Toast.LENGTH_SHORT).show();
        for(int i=0;i<poolList.size();i++){
            OnePoolItem onePoolItem=poolList.get(i);
            LatLng item = new LatLng(onePoolItem.getPoolLat(),onePoolItem.getPoolLng());
            Marker marker=mMap.addMarker(new MarkerOptions().position(item).title(onePoolItem.getName()).snippet(onePoolItem.getPlace()));
            marker.setTag(onePoolItem);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(item,10));
        }

    }
}
