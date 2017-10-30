
package com.example.nitantsood.buyer_serverapplication;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.firebase.client.realtime.util.StringListReader;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class foodListActivity extends AppCompatActivity implements FoodListAdapter.onFoodItemClickListener, DirectionFinderListener {
    RecyclerView foodListView;
    public static  ArrayList<OneFoodItem> list;
    FoodListAdapter adapter;
    private Firebase mRef;
    static int k=0;
    private ProgressDialog progressDialog;

    LocationManager locationManager;
    LocationListener locationListener;
    OneUserDetails oneUserDetails;
    public static LatLng currentLatLang;

    boolean firstTimeLocationPick=true;
//    DatabaseReference database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_list);
        list=new ArrayList<>();
        foodListView=(RecyclerView) findViewById(R.id.FoodList);

        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
        oneUserDetails=(OneUserDetails) bundle.get("oneUser");
        foodListView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));

        foodListView.addItemDecoration(new SimpledividerItemDecoration(this));
        adapter=new FoodListAdapter(this,list,this);
        foodListView.setAdapter(adapter);

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                if(firstTimeLocationPick) {
                    firstTimeLocationPick=false;
                    currentLatLang = new LatLng(location.getLatitude(), location.getLongitude());
                    callFirebase();
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
        if(Build.VERSION.SDK_INT<23){
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

        }else {
            if (ContextCompat.checkSelfPermission(foodListActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(foodListActivity.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},1);
            }else {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.food_list_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        if(id==R.id.showMapView){
            Intent intent=new Intent(this,AllMapActivity.class);
            Bundle bundle=new Bundle();
            bundle.putSerializable("FoodList",list);
            bundle.putSerializable("oneUSer",oneUserDetails);
            intent.putExtras(bundle);
            startActivity(intent);
        }
        else if(id==R.id.showPoolFromUSerButton){
            Intent intent=new Intent(this,AllPoolShowActivity.class);
            intent.putExtra("call",2);
            Bundle bundle=new Bundle();
            bundle.putSerializable("caller",oneUserDetails);
            intent.putExtras(bundle);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    private void callFirebase() {

        mRef=new Firebase("https://buyer-serverapplication.firebaseio.com/Food_Item");
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                list.clear();
                for(DataSnapshot singleFoodItem : dataSnapshot.getChildren()){
                    OneFoodItem currentFoodItem;
                    Log.v("lala",singleFoodItem.getValue()+"");
                    currentFoodItem=(OneFoodItem) singleFoodItem.getValue(OneFoodItem.class);
                    currentFoodItem.setFood_UID(singleFoodItem.getKey());
                    currentFoodItem.setDistance("");
                    calculateDistance(currentFoodItem.getSeller_lat(),currentFoodItem.getSeller_lng(),list.size());
                    list.add(currentFoodItem);
                }
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

    }

    private void calculateDistance(Double seller_lat, Double seller_lng,int position) {
        String origin=currentLatLang.latitude+","+currentLatLang.longitude;
        String destination=seller_lat+","+seller_lng;
        try {
            new DirectionFinder(this, origin, destination,position).execute();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFoodItemClicked(View view, OneFoodItem selectedFood) {
        int id=view.getId();
        if(id==R.id.foodDistance){
            Intent intent=new Intent(this,OneMapActivity.class);
            Bundle bundle=new Bundle();
            bundle.putSerializable("selectedFood",selectedFood);
            intent.putExtras(bundle);
            startActivity(intent);
        }
        else{
            Intent intent=new Intent(this,foodDetailActivity.class);
            Bundle bundle=new Bundle();
            bundle.putSerializable("selectedFood",selectedFood);
            intent.putExtras(bundle);
            startActivity(intent);
        }
//        Toast.makeText(this,selectedFood.getTitle()+" Clicked", Toast.LENGTH_SHORT).show();
//        int k=selectedFood.getOrdered();
//        if(k==0) {
//           mRef.child(selectedFood.getFood_UID()).child("ordered").setValue(++k);
//        }
//        else{
//           Toast.makeText(this," Sorry Item Booked", Toast.LENGTH_SHORT).show();
//        }
    }

    @Override
    public void onDirectionFinderStart() {

    }

    @Override
    public void onDirectionFinderSuccess(List<Route> route,int position) {
        adapter.notifyDataSetChanged();
       list.get(position).setDistance(route.get(0).distance.text);
    }
}
