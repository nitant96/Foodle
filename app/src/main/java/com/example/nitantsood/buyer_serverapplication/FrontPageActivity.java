package com.example.nitantsood.buyer_serverapplication;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class FrontPageActivity extends AppCompatActivity {
    private SensorManager ss;
    private float x;
    private float y;
    private float z;

    Firebase firebaseRef;
    ImageView imageView;
    TextView RegisterText,viewAllPool;
    TextView title1,location1,time1,title2,location2,time2;
    ArrayList<OnePoolItem> poolList=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_front_page);
        imageView=(ImageView) findViewById(R.id.poolBack);
        viewAllPool=(TextView) findViewById(R.id.viewMorePools);
        RegisterText=(TextView) findViewById(R.id.frontRegisterText);
        title1=(TextView)  findViewById(R.id.frontPagePool1).findViewById(R.id.PoolName);
        title2=(TextView) findViewById(R.id.frontPagePool2).findViewById(R.id.PoolName);
        location1=(TextView) findViewById(R.id.frontPagePool1).findViewById(R.id.poolLocation);
        location2=(TextView) findViewById(R.id.frontPagePool2).findViewById(R.id.poolLocation);
        time1=(TextView) findViewById(R.id.frontPagePool1).findViewById(R.id.poolTimeAndDate);
        time2=(TextView) findViewById(R.id.frontPagePool2).findViewById(R.id.poolTimeAndDate);

        ss = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        ss.registerListener(sensorListener, ss.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
        x=SensorManager.GRAVITY_EARTH;
        y=SensorManager.GRAVITY_EARTH;
        z=0.00f;


        viewAllPool.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(FrontPageActivity.this,AllPoolShowActivity.class);
                intent.putExtra("call",1);
                startActivity(intent);
            }
        });

        RegisterText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FrontPageActivity.this,RegisterActivity.class));

            }
        });

        firebaseRef=new Firebase("https://buyer-serverapplication.firebaseio.com/Pool");
        firebaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot onePoolItemSnapshot:dataSnapshot.getChildren()){
                    OnePoolItem onePoolItem=onePoolItemSnapshot.getValue(OnePoolItem.class);
                    onePoolItem.setPool_UID(onePoolItemSnapshot.getKey());
                    poolList.add(onePoolItem);
                }
                title1.setText(poolList.get(0).getName());
                location1.setText(poolList.get(0).getPlace());

                title2.setText(poolList.get(1).getName());
                location2.setText(poolList.get(1).getPlace());

                Timestamp timestamp=new Timestamp(Long.parseLong(poolList.get(0).getDateTime()));
                SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd/MM/yyyy 'at' HH:mm");
                time1.setText(simpleDateFormat.format(timestamp));

                timestamp=new Timestamp(Long.parseLong(poolList.get(1).getDateTime()));
                time2.setText(simpleDateFormat.format(timestamp));

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }
    private final SensorEventListener sensorListener=new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {

            float a = event.values[0];
            float b = event.values[1];
            float c = event.values[2];
            y = x;
            x = (float) Math.sqrt((double) (a * a + b * b + c * c));
            float delta = x - y;
            z = z * 0.9f + delta;
            if (z > 12) {
                Intent intent = new Intent(FrontPageActivity.this, AllPoolShowActivity.class);
                intent.putExtra("call", 1);
                startActivity(intent);
            }

        }
        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.front_page_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        if(id==R.id.loginFromFrontPage){
            startActivity(new Intent(this,LoginActivity.class));
        }
        return true;
    }
}
