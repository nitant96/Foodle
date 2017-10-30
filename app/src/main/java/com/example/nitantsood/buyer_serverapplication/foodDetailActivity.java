package com.example.nitantsood.buyer_serverapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class foodDetailActivity extends AppCompatActivity {
    OneFoodItem oneFoodItem;
    OneSellerDetail oneSellerDetail;
    TextView title,name,price,quantity,address,expiry;
    ImageView imageView,routeButton;
    Firebase mRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        title=(TextView) findViewById(R.id.foodDetailTitle);
        name=(TextView)findViewById(R.id.foodDetailOrganisation);
        price=(TextView) findViewById(R.id.foodDetailCost);
        quantity=(TextView) findViewById(R.id.foodDetailQuantity);
        address=(TextView) findViewById(R.id.foodDetailAddress);
        expiry=(TextView) findViewById(R.id.foodDetailExpiry);

        imageView=(ImageView) findViewById(R.id.foodDetailImage);
        routeButton=(ImageView) findViewById(R.id.foodDetailRouteButton);

        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
        oneFoodItem=(OneFoodItem) bundle.get("selectedFood");

        Picasso.with(this).load(oneFoodItem.getPicture_URL()).into(imageView);

        mRef=new Firebase("https://buyer-serverapplication.firebaseio.com/Seller");
        mRef.addValueEventListener(new ValueEventListener() {
                                       @Override
                                       public void onDataChange(DataSnapshot dataSnapshot) {
                                           for(DataSnapshot singleSeller:dataSnapshot.getChildren()) {
                                               if(singleSeller.getKey().equals(oneFoodItem.getSeller_UID())){
                                                   oneSellerDetail=singleSeller.getValue(OneSellerDetail.class);
                                                   name.setText(oneSellerDetail.getName());
                                               }
                                           }
                                       }

                                       @Override
                                       public void onCancelled(FirebaseError firebaseError) {

                                       }
                                   });

        title.setText(oneFoodItem.getTitle());
        price.setText(oneFoodItem.getQuantity());
        quantity.setText(oneFoodItem.getQuantity());

        Timestamp expiryTimestamp=new java.sql.Timestamp(Long.parseLong(oneFoodItem.getExpiry_date_time()));
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd/MM/yyyy 'at' HH:mm");
        expiry.setText("EXPIRY:"+simpleDateFormat.format(expiryTimestamp));




        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_food_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
