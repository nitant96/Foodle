package com.example.nitantsood.buyer_serverapplication;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;

import org.w3c.dom.Text;

import java.util.ArrayList;

import static com.example.nitantsood.buyer_serverapplication.FireApp.mAuth;

public class RegisterActivity extends AppCompatActivity {
    EditText email, pass1, pass2, contact, company;
    RadioGroup radioGroup;
    TextView loginText;
    CheckBox sellerCheck,userCheck,NgoCheck;
    private Firebase mRef;
    Button registerButton;

    String Aadhaar, namexyz, uidxyz, genderxyz;
    boolean found,foundAt=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        email = (EditText) findViewById(R.id.registerEmail);
        pass1 = (EditText) findViewById(R.id.registerPass1);
        pass2 = (EditText) findViewById(R.id.registerPass2);
        contact = (EditText) findViewById(R.id.registerNumber);
        company = (EditText) findViewById(R.id.registerCompanyName);
        loginText = (TextView) findViewById(R.id.loginText);
        radioGroup=(RadioGroup) findViewById(R.id.radioGroup);
        registerButton=(Button) findViewById(R.id.registerButton);
        radioGroup.check(R.id.userradioButton);
//        try {
//
//            Intent intent = new Intent("com.google.zxing.client.android.SCAN");
//            intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
//            startActivityForResult(intent, 0);
//
//        } catch (Exception e) {
//
//            Uri marketUri = Uri.parse("market://details?id=com.google.zxing.client.android");
//            Intent marketIntent = new Intent(Intent.ACTION_VIEW,marketUri);
//            startActivity(marketIntent);
//
//        }

        company.setVisibility(View.INVISIBLE);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                if(checkedId==R.id.userradioButton){
                    company.setVisibility(View.INVISIBLE);
                }
                else{
                    company.setVisibility(View.VISIBLE);
                }
            }
        });
        loginText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
            }
        });
    }

    public void registerButtonClicked(View view) {
        if (email.getText().toString().equals("")) {
            Toast.makeText(this, "Enter a Valid Email-ID", Toast.LENGTH_SHORT).show();
        } else if (pass1.getText().toString().equals("") || pass2.getText().toString().equals("")) {
            Toast.makeText(this, "Please check the Password", Toast.LENGTH_SHORT).show();
        } else if (!pass1.getText().toString().equals(pass2.getText().toString())) {
            Toast.makeText(this, "Password Does not match, Please try again", Toast.LENGTH_SHORT).show();
            pass1.setText("");
            pass2.setText("");
        } else if (contact.getText().toString().equals("") || contact.getText().toString().length() != 10) {
            Toast.makeText(this, "Please check your contact  Detail, Make sure you enter 10 digit Number only", Toast.LENGTH_SHORT).show();
        } else {
            createAccount(email.getText().toString(), pass1.getText().toString());
        }
    }

    private void goForDatabase() {
        if (radioGroup.getCheckedRadioButtonId()==R.id.sellerradioButton) {
            OneSellerDetail oneSeller = new OneSellerDetail();
            oneSeller.setAadhaar_UID("Edit This");
            oneSeller.setCompany_name(company.getText().toString());
            oneSeller.setContact_no(contact.getText().toString());
            oneSeller.setSeller_UID(mAuth.getCurrentUser().getUid());
            oneSeller.setEmail(email.getText().toString());
            oneSeller.setName("Edit this");
            mRef = new Firebase("https://buyer-serverapplication.firebaseio.com/Seller");
            Firebase OneSellerRef = mRef.child(mAuth.getCurrentUser().getUid());
            OneSellerRef.setValue(oneSeller);

            Intent intent=new Intent(RegisterActivity.this,SellerListActivity.class);
            Bundle bundle=new Bundle();
            bundle.putSerializable("oneSeller",oneSeller);
            intent.putExtras(bundle);
            startActivity(intent);
        } else if(radioGroup.getCheckedRadioButtonId()==R.id.userradioButton) {
            OneUserDetails oneUser = new OneUserDetails();
            oneUser.setEmail(email.getText().toString());
            oneUser.setName("none");
            oneUser.setUser_UID(mAuth.getCurrentUser().getUid());
            oneUser.setContact_no(contact.getText().toString());
            oneUser.setAadhaar_UID("none");
            mRef = new Firebase("https://buyer-serverapplication.firebaseio.com/user");
            Firebase OneUserRef = mRef.child(mAuth.getCurrentUser().getUid());
            OneUserRef.setValue(oneUser);

            Intent intent=new Intent(RegisterActivity.this,foodListActivity.class);
            Bundle bundle=new Bundle();
            bundle.putSerializable("oneUser",oneUser);
            intent.putExtras(bundle);
            startActivity(intent);
        }
        else if(radioGroup.getCheckedRadioButtonId()==R.id.NgoradioButton){
            OneNgoDetail oneNgoDetail=new OneNgoDetail();
            oneNgoDetail.setEmail(email.getText().toString());
            oneNgoDetail.setOrganization_name(company.getText().toString());
            oneNgoDetail.setAadhaar_UID("");
            oneNgoDetail.setNgo_UID(mAuth.getCurrentUser().getUid());
            oneNgoDetail.setContact_no(contact.getText().toString());
            mRef = new Firebase("https://buyer-serverapplication.firebaseio.com/Ngo");
            Firebase OneNgoRef = mRef.child(mAuth.getCurrentUser().getUid());
            OneNgoRef.setValue(oneNgoDetail);

            Intent intent=new Intent(RegisterActivity.this,NgoListActivity.class);
            Bundle bundle=new Bundle();
            bundle.putSerializable("oneNgo",oneNgoDetail);
            intent.putExtras(bundle);
            startActivity(intent);
        }

    }
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == 0) {
//
//            if (resultCode == RESULT_OK) {
//                Aadhaar = data.getStringExtra("SCAN_RESULT");
//
//                ArrayList<String> Key = new ArrayList<String>();
//                ArrayList<String> Value = new ArrayList<String>();
//                int keyIndex = 0, valueIndex = 0;
//                String json = Aadhaar;
//                for (int i = 0; i < json.length(); i++) {
//                    String ch1 = String.valueOf(json.charAt(i));
//                    if (ch1.equals(" ")) {
//                        for (int j = i + 1; j < json.length(); j++) {
//                            String ch2 = String.valueOf(json.charAt(j));
//                            if (ch2.equals("=")) {
//                                Key.add(keyIndex++, json.substring(i + 1, j));
//                                break;
//                            }
//                            if (ch2.equals(" ")) {
//                                break;
//                            }
//                        }
//                    }
//                }
//                for (int i = 0; i < json.length(); i++) {
//                    String ch1 = String.valueOf(json.charAt(i));
//                    if (ch1.equals("=")) {
//                        for (int j = i + 2; j < json.length() - 1; j++) {
//                            String ch2 = String.valueOf(json.charAt(j)) + String.valueOf(json.charAt(j + 1));
//                            if (ch2.equals("\" ") || ch2.equals("\"/") || ch2.equals("\"?")) {
//                                Value.add(valueIndex++, json.substring(i + 2, j));
//                                break;
//                            }
//                        }
//                    }
//                }
//                String Address = "";
//                int addressExistsCount = 0;
//                for (int i = 0; i < Key.size(); i++) {
//                    if (Key.get(i).equals("name")) {
//                        namexyz=(Value.get(i));
//                    } else if (Key.get(i).equals("gender")) {
//                        genderxyz=Value.get(i);
//                    } else if (Key.get(i).equals("uid")){
//                        uidxyz=(Value.get(i));
//                    }
//                }
//                checkUIDUnique(uidxyz);
//            }
//        }
//    }

    private void checkUIDUnique(final String uidxyz) {

        mRef=new Firebase("https://buyer-serverapplication.firebaseio.com/");
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (foundAt) {
                    foundAt=false;
                    DataSnapshot userSnapshot = dataSnapshot.child("user");
                    DataSnapshot sellerSnapshot = dataSnapshot.child("Seller");

                    for (DataSnapshot singleUser : userSnapshot.getChildren()) {
                        OneUserDetails oneUserDetails = singleUser.getValue(OneUserDetails.class);
                        oneUserDetails.setUser_UID(singleUser.getKey());
                        if (oneUserDetails.getAadhaar_UID().equals(uidxyz)) {
                            found = true;
                            break;
                        }
                    }

                    if (!found) {
                        for (DataSnapshot singleSeller : sellerSnapshot.getChildren()) {
                            OneSellerDetail oneSellerDetail = singleSeller.getValue(OneSellerDetail.class);
                            oneSellerDetail.seller_UID = mAuth.getCurrentUser().getUid();
                            if (oneSellerDetail.getAadhaar_UID().equals(uidxyz)) {
                                found = true;
                                break;
                            }
                        }
                    }
                    if (found) {
                        Toast.makeText(RegisterActivity.this, "Already registered with this Aadhaar Card!! Try Again!!", Toast.LENGTH_SHORT).show();
                        registerButton.setEnabled(false);
                    }
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    public void createAccount(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            goForDatabase();

                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(RegisterActivity.this, "Authentication failed while creating account",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}