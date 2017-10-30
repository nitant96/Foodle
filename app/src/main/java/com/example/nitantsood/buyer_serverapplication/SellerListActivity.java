package com.example.nitantsood.buyer_serverapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class SellerListActivity extends AppCompatActivity {
OneSellerDetail oneSellerDetail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_list);

        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
        oneSellerDetail=(OneSellerDetail) bundle.get("oneSeller");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.seller_list_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        if(id==R.id.poolFromSellerButton){
            Intent intent=new Intent(this,AllPoolShowActivity.class);
            intent.putExtra("call",3);
            Bundle bundle=new Bundle();
            bundle.putSerializable("caller",oneSellerDetail);
            intent.putExtras(bundle);
            startActivity(intent);
        }
        else if(id==R.id.addNewSellerItem){
            Intent intent=new Intent(this,SellerFoodinputDetails.class);
            Bundle bundle=new Bundle();
            bundle.putSerializable("oneSeller",oneSellerDetail);
            intent.putExtras(bundle);
            startActivity(intent);
        }
        return true;
    }
}
