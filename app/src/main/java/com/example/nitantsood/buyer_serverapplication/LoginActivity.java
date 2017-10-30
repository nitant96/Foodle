package com.example.nitantsood.buyer_serverapplication;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

import static com.example.nitantsood.buyer_serverapplication.FireApp.mAuth;

public class LoginActivity extends AppCompatActivity {
    EditText loginEmail,loginPass;
    Button loginButton;
    TextView registerText;
    private Firebase mRef;
    boolean  found=false;
    int foundAt=0;
    OneUserDetails oneUserDetails;
    OneSellerDetail oneSellerDetail;
    OneNgoDetail oneNgoDetail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginEmail=(EditText) findViewById(R.id.loginEmail);
        loginPass=(EditText) findViewById(R.id.loginPass);
        loginButton=(Button) findViewById(R.id.registerButton);
        registerText=(TextView) findViewById(R.id.RegisterText);

        registerText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //call  intent for registering !!
                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
            }
        });
    }

    public void loginButtonClicked(View view){
        signIn(loginEmail.getText().toString(),loginPass.getText().toString());
    }

    public void signIn(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            successfulLogin();
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(LoginActivity.this, "Email or Password is incorrect",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        // [END sign_in_with_email]

    }

    private void successfulLogin() {

        mRef=new Firebase("https://buyer-serverapplication.firebaseio.com/");
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DataSnapshot userSnapshot=dataSnapshot.child("user");
                DataSnapshot sellerSnapshot=dataSnapshot.child("Seller");
                DataSnapshot ngoSnapshot=dataSnapshot.child("Ngo");

                for(DataSnapshot singleUser:userSnapshot.getChildren()){
                    oneUserDetails=singleUser.getValue(OneUserDetails.class);
                    oneUserDetails.setUser_UID(singleUser.getKey());
                    if(oneUserDetails.getUser_UID().equals(mAuth.getCurrentUser().getUid())){
                        found=true;
                        foundAt=0;
                        break;
                    }
                }

                if(!found){
                    for(DataSnapshot singleSeller:sellerSnapshot.getChildren()){
                        oneSellerDetail=singleSeller.getValue(OneSellerDetail.class);
                        oneSellerDetail.setSeller_UID(singleSeller.getKey());
                        if(oneSellerDetail.getSeller_UID().equals(mAuth.getCurrentUser().getUid())){
                            found=true;
                            foundAt=2;
                            break;
                        }
                    }
                }
                if(!found){
                    for(DataSnapshot singleNgo:ngoSnapshot.getChildren()){
                        oneNgoDetail=singleNgo.getValue(OneNgoDetail.class);
                        oneNgoDetail.setNgo_UID(singleNgo.getKey());
                        if(oneNgoDetail.getNgo_UID().equals(mAuth.getCurrentUser().getUid())){
                            foundAt=3;
                            break;
                        }
                    }
                }
                afterLoginActivityDecider();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    private void afterLoginActivityDecider() {
        if(foundAt==0){
            //user login
            Intent intent=new Intent(LoginActivity.this,foodListActivity.class);
            Bundle bundle=new Bundle();
            bundle.putSerializable("oneUser",oneUserDetails);
            intent.putExtras(bundle);
            startActivity(intent);
        }
        else if(foundAt==2){
            //seller login
            Intent intent=new Intent(LoginActivity.this,SellerListActivity.class);
            Bundle bundle=new Bundle();
            bundle.putSerializable("oneSeller",oneSellerDetail);
            intent.putExtras(bundle);
            startActivity(intent);
        }
        else if(foundAt==3){
            Intent intent=new Intent(LoginActivity.this,NgoListActivity.class);
            Bundle bundle=new Bundle();
            bundle.putSerializable("oneNgo",oneNgoDetail);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }
}
