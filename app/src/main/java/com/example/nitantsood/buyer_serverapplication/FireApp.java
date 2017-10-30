package com.example.nitantsood.buyer_serverapplication;

import android.app.Application;

import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by NITANT SOOD on 23-10-2017.
 */

public class FireApp extends Application {
    static public FirebaseAuth mAuth;

    @Override
    public void onCreate() {
        super.onCreate();
        Firebase.setAndroidContext(this);
        mAuth = FirebaseAuth.getInstance();
    }
}
