package com.example.nitantsood.buyer_serverapplication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

import static com.example.nitantsood.buyer_serverapplication.FireApp.mAuth;

public class MainActivity extends AppCompatActivity {

    private String email,password;
    TextView mStatusTextView;
    EditText emailText,passText;
    Button loginUser;
//    FirebaseStorage storage;
    ImageView imageView;
    StorageReference storageRef;
    private static final String TAG = "EmailPassword";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView=(ImageView) findViewById(R.id.imageView);
        emailText=(EditText) findViewById(R.id.email);
        passText=(EditText) findViewById(R.id.pass);
        loginUser=(Button) findViewById(R.id.button2);
//        storage = FirebaseStorage.getInstance();
//        storageRef=storage.getReference();
        mStatusTextView=(TextView)findViewById(R.id.status);

    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    public void createAccount(String email, String password) {
        Log.d(TAG, "createAccount:" + email);

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Intent intent=new Intent(MainActivity.this,SellerFoodinputDetails.class);
                            intent.putExtra("lala",user.getUid());
                            updateUI(user);
                            startActivity(intent);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed while creating account",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
        // [END create_user_with_email]
    }
    public void signIn(String email, String password) {
        Log.d(TAG, "signIn:" + email);

        // [START sign_in_with_email]
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Intent intent=new Intent(MainActivity.this,SellerFoodinputDetails.class);
                            intent.putExtra("lala",user.getUid());
                            updateUI(user);
                            startActivity(intent);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failedwhile signing in !!",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
        // [END sign_in_with_email]

    }

    public void signOut() {
        mAuth.signOut();
        updateUI(null);
    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            mStatusTextView.setText(user.getEmail());
            mStatusTextView.setText(user.getUid());
//            uploadPic(user.getUid());

//            findViewById(R.id.email_password_buttons).setVisibility(View.GONE);
//            findViewById(R.id.email_password_fields).setVisibility(View.GONE);
//            findViewById(R.id.signed_in_buttons).setVisibility(View.VISIBLE);
//
//            findViewById(R.id.verify_email_button).setEnabled(!user.isEmailVerified());
        } else {
            mStatusTextView.setText("Signed Out !!");
//            mDetailTextView.setText(null);

//            findViewById(R.id.email_password_buttons).setVisibility(View.VISIBLE);
//            findViewById(R.id.email_password_fields).setVisibility(View.VISIBLE);
//            findViewById(R.id.signed_in_buttons).setVisibility(View.GONE);
        }
    }

    void uploadPic(String Uid){
        StorageReference mountainsRef = storageRef.child("mountains.jpg");
        StorageReference mountainImagesRef = storageRef.child("images/mountains.jpg");
        imageView.setDrawingCacheEnabled(true);
        imageView.buildDrawingCache();
        Bitmap bitmap = imageView.getDrawingCache();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = mountainsRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                Uri downloadUrl = taskSnapshot.getDownloadUrl();
            }
        });

    }
//public void submitClicked(View view){
//    mAuth.createUserWithEmailAndPassword(email, password)
//            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//        @Override
//        public void onComplete(@NonNull Task<AuthResult> task) {
//            if (task.isSuccessful()) {
//                // Sign in success, update UI with the signed-in user's information
//                Log.d(TAG, "createUserWithEmail:success");
//                FirebaseUser user = mAuth.getCurrentUser();
//                updateUI(user);
//            } else {
//                // If sign in fails, display a message to the user.
//                Log.w(TAG, "createUserWithEmail:failure", task.getException());
//                Toast.makeText(EmailPasswordActivity.this, "Authentication failed.",
//                        Toast.LENGTH_SHORT).show();
//                updateUI(null);
//            }
//
//            // ...
//        }
//    });
//
//}
    public void clickedBtn(View view){
        int id=view.getId();
        if(id==R.id.button2){
            signIn(emailText.getText().toString(),passText.getText().toString());
        }
        else if(id==R.id.button3){
            createAccount(emailText.getText().toString(),passText.getText().toString());
        }
        else if(id==R.id.button4){
            signOut();
        }
    }
}
