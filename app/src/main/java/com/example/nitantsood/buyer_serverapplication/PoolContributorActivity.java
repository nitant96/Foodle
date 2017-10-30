package com.example.nitantsood.buyer_serverapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.firebase.client.Firebase;

public class PoolContributorActivity extends AppCompatActivity {
    TextView poolName,location;
    EditText name,title,quantity;
    int call;
    Button submitButton;
    OnePoolItem onePoolItem;
    OneUserDetails oneUserDetails;
    OneSellerDetail oneSellerDetail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pool_contributor);

        poolName=(TextView) findViewById(R.id.pool_name);
        submitButton=(Button) findViewById(R.id.contri_button);
        location=(TextView) findViewById(R.id.pool_Location);
        name=(EditText) findViewById(R.id.contri_name);
        title=(EditText) findViewById(R.id.contri_title);
        quantity=(EditText) findViewById(R.id.contri_qty);

        Intent intent=getIntent();
        call=intent.getIntExtra("call",0);
        Bundle bundle=intent.getExtras();
        onePoolItem=(OnePoolItem)bundle.get("onePool");
        if(call==2){
            oneUserDetails=(OneUserDetails)bundle.get("oneUser");

        }else{
            oneSellerDetail=(OneSellerDetail)bundle.get("oneSeller");
        }

        poolName.setText(onePoolItem.getName());
        location.setText(onePoolItem.getPlace());

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OneContributorDetail oneContributorDetail=new OneContributorDetail();
                oneContributorDetail.setName(name.getText().toString());
                oneContributorDetail.setFood(title.getText().toString());
                oneContributorDetail.setQuantity(quantity.getText().toString());
                Firebase mRef=new Firebase("https://buyer-serverapplication.firebaseio.com/Pool/"+onePoolItem.getPool_UID());
                Firebase oneContributor;
                if(call==2) {
                    oneContributorDetail.setContributor_UID(oneUserDetails.getUser_UID());
                    oneContributor=mRef.child(oneUserDetails.getUser_UID());
                }else{
                    oneContributorDetail.setContributor_UID(oneSellerDetail.getSeller_UID());
                    oneContributor=mRef.child(oneSellerDetail.getSeller_UID());
                }
                oneContributor.setValue(oneContributorDetail);
            }
        });

    }
}
