package com.example.nitantsood.buyer_serverapplication;

import android.*;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TimePicker;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class CreatePoolActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener,TimePickerDialog.OnTimeSetListener {
    ImageButton locationButton,calendarButton;
    Button createPool;
    EditText name,location,timestampText;
    Calendar poolCalendar;
    Double lat,lng;
    String Address;
    OnePoolItem onePoolItem;
    Timestamp poolTimestamp;
    OneNgoDetail myNgo;
    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS=0;
    Route route;
    String mPhoneNo,mMessage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_pool);

        final Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
        myNgo=(OneNgoDetail) bundle.get("oneNgo");

        locationButton=(ImageButton) findViewById(R.id.newPoolLocationButton);
        calendarButton=(ImageButton) findViewById(R.id.newPoolTimestampButton);
        name=(EditText) findViewById(R.id.newPoolName);
        location=(EditText) findViewById(R.id.newPoolLocation);
        timestampText=(EditText) findViewById(R.id.newPoolTimestamp);
        createPool=(Button) findViewById(R.id.creatPoolButton);
        poolCalendar=Calendar.getInstance();

        timestampText.setEnabled(false);
        location.setEnabled(false);

        createPool.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onePoolItem=new OnePoolItem();
                onePoolItem.setName(name.getText().toString());
                onePoolItem.setPool_UID(poolTimestamp.getTime()+myNgo.getNgo_UID());
                onePoolItem.setDateTime(poolTimestamp.getTime()+"");
                onePoolItem.setPlace(location.getText().toString());
                onePoolItem.setPoolLat(lat);
                onePoolItem.setPoolLng(lng);

                Firebase mRef=new Firebase("https://buyer-serverapplication.firebaseio.com/Pool");
                Firebase onePoolItemRef=mRef.child(onePoolItem.getPool_UID());
                onePoolItemRef.setValue(onePoolItem);

                Toast.makeText(CreatePoolActivity.this, "Pool "+onePoolItem.getName()+" Successfully Created !!", Toast.LENGTH_SHORT).show();
                poolSuccessfullyCreated();
//                Intent intent1=new Intent(CreatePoolActivity.this,NgoListActivity.class);
//                Bundle bundle1=new Bundle();
//                bundle1.putSerializable("oneNgo",myNgo);
//                intent1.putExtras(bundle1);
//                startActivity(intent1);
            }
        });
        locationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(CreatePoolActivity.this,PoolLocationSetFinder.class);
                startActivityForResult(intent,1);
            }
        });

        calendarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c=Calendar.getInstance();
                int year=c.get(Calendar.YEAR);
                int month=c.get(Calendar.MONTH);
                int dom=c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog=new DatePickerDialog(CreatePoolActivity.this,CreatePoolActivity.this,year,month,dom);
                datePickerDialog.show();
            }
        });
    }

    private void poolSuccessfullyCreated() {
        final String message="As you are Registered with Our App, We would like to inform you that A new Pool "+onePoolItem.getName()+" is going to held at "+onePoolItem.getPlace();
        Firebase mRef=new Firebase("https://buyer-serverapplication.firebaseio.com/");
        sendSMS("7291892016",message);
//        mRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                DataSnapshot userSnapshot=dataSnapshot.child("user");
//                DataSnapshot sellerSnapshot=dataSnapshot.child("Seller");
//
//                for(DataSnapshot singleUser:userSnapshot.getChildren()){
//                    OneUserDetails oneUserDetails=singleUser.getValue(OneUserDetails.class);
//                    sendSMS(oneUserDetails.getContact_no(),message);
//                }
//                for(DataSnapshot singleSeller:sellerSnapshot.getChildren()) {
//                    OneSellerDetail oneSellerDetail = singleSeller.getValue(OneSellerDetail.class);
//                    sendSMS(oneSellerDetail.getContact_no(), message);
//                }
//            }
//
//            @Override
//            public void onCancelled(FirebaseError firebaseError) {
//
//            }
//        });
    }

    private void sendSMS(String phoneNo,String message) {
        mPhoneNo=phoneNo;
        mMessage=message;
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.SEND_SMS)) {
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.SEND_SMS},
                        MY_PERMISSIONS_REQUEST_SEND_SMS);
            }
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_SEND_SMS: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(mPhoneNo, null, mMessage, null, null);
                    Toast.makeText(getApplicationContext(), "SMS sent.",
                            Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(),
                            "SMS faild, please try again.", Toast.LENGTH_LONG).show();
                    return;
                }
            }
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==1 &&resultCode==RESULT_OK){
            Address=data.getStringExtra("address");
            lat= data.getDoubleExtra("lat",0);
            lng=data.getDoubleExtra("lng",0);
            location.setText(Address);
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        poolCalendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
        poolCalendar.set(Calendar.MONTH,month);
        poolCalendar.set(Calendar.YEAR,year);

        Calendar c=Calendar.getInstance();
        int hours=c.get(Calendar.HOUR_OF_DAY);
        int mins=c.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog=new TimePickerDialog(CreatePoolActivity.this,CreatePoolActivity.this,hours,mins,true);
        timePickerDialog.show();
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        poolCalendar.set(Calendar.HOUR_OF_DAY,hourOfDay);
        poolCalendar.set(Calendar.MINUTE,minute);

        poolTimestamp=new java.sql.Timestamp(poolCalendar.getTimeInMillis());
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd/MM/yyyy 'at' HH:mm");
        timestampText.setText(simpleDateFormat.format(poolTimestamp));
    }
}
