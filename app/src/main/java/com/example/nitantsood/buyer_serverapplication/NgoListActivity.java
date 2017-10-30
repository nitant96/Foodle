package com.example.nitantsood.buyer_serverapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class NgoListActivity extends AppCompatActivity {
    OneNgoDetail myNgo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ngo_list);
        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
        myNgo=(OneNgoDetail) bundle.get("oneNgo");

        Intent intent1=new Intent(this,CreatePoolActivity.class);
        Bundle bundle1=new Bundle();
        bundle1.putSerializable("oneNgo",myNgo);
        intent1.putExtras(bundle);
        startActivity(intent1);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.ngo_list_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        if(id==R.id.addPoolButton){
            Intent intent=new Intent(this,CreatePoolActivity.class);
            Bundle bundle=new Bundle();
            bundle.putSerializable("oneNgo",myNgo);
            intent.putExtras(bundle);
            startActivity(intent);
        }
        return true;
    }
}
