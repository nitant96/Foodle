package com.example.nitantsood.buyer_serverapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;

public class AllPoolShowActivity extends AppCompatActivity implements PoolVieAdapter.setOnPoolClickListener {
    RecyclerView recyclerView;
    ArrayList<OnePoolItem> poolList;
    PoolVieAdapter adapter;
    private Firebase mRef;

    OneSellerDetail oneSellerDetail;
    OneUserDetails oneUserDetails;
    int call;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_pool_show);
        poolList=new ArrayList<>();
        recyclerView=(RecyclerView) findViewById(R.id.allPoolRecyclerView);
        adapter=new PoolVieAdapter(getApplicationContext(),poolList,this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);

        Intent intent=getIntent();
        call=intent.getIntExtra("call",0);
        Bundle bundle=intent.getExtras();
        if(call==2){
            oneUserDetails=(OneUserDetails) bundle.get("caller");
        }else if(call==3){
            oneSellerDetail=(OneSellerDetail) bundle.get("caller");
        }

        mRef=new Firebase("https://buyer-serverapplication.firebaseio.com/Pool");
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                poolList.clear();
                for(DataSnapshot singlePoolItem : dataSnapshot.getChildren()){
                    OnePoolItem onePoolItem;
                    onePoolItem=(OnePoolItem) singlePoolItem.getValue(OnePoolItem.class);
                    onePoolItem.setPool_UID(singlePoolItem.getKey());
                    poolList.add(onePoolItem);
                }
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

    }

    @Override
    public void OnPoolClicked(View v, int position) {
        if(call==2){
            Toast.makeText(this, "user", Toast.LENGTH_SHORT).show();
            Intent intent=new Intent(this,PoolContributorActivity.class);
            intent.putExtra("call",call);

            Bundle bundle=new Bundle();
            bundle.putSerializable("onePool",poolList.get(position));
            bundle.putSerializable("oneUser",oneUserDetails);





            intent.putExtras(bundle);
            startActivity(intent);
        }else if (call==3){
            Toast.makeText(this, "seller", Toast.LENGTH_SHORT).show();
            Intent intent=new Intent(this,PoolContributorActivity.class);
            intent.putExtra("call",call);

            Bundle bundle=new Bundle();
            bundle.putSerializable("onePool",poolList.get(position));
            bundle.putSerializable("oneSeller",oneSellerDetail);
            intent.putExtras(bundle);
            startActivity(intent);
        }else{
            Toast.makeText(this, "Please Sign In First to continue !!", Toast.LENGTH_SHORT).show();
        }
    }
}
