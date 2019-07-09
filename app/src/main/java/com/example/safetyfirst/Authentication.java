package com.example.safetyfirst;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.io.FileOutputStream;

public class Authentication extends AppCompatActivity {

    public Button signIn;
    public Button signUp;
    public EditText username;
    public EditText password;
    public EditText confirmPassword;
    public Button ok;

    public String TAG = "Authentication";

    public static String filename = "userCredentials.txt";

    public DatabaseReference ref;

    public int status = 1;

    public static String username1 = "";
    public static String password1 = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);

        signIn = (Button) findViewById(R.id.signIn);
        signUp = (Button) findViewById(R.id.signUp);
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        confirmPassword = (EditText) findViewById(R.id.confirmPassword);
        ok = (Button) findViewById(R.id.ok1);

        confirmPassword.setVisibility(View.INVISIBLE);

        FirebaseApp.initializeApp(Authentication.this);

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                status = 1;
                confirmPassword.setVisibility(View.INVISIBLE);

            }
        });


        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                status=2;
                confirmPassword.setVisibility(View.VISIBLE);

            }
        });


        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String user = username.getText().toString();
                final String pass = password.getText().toString();
                final String confPass = confirmPassword.getText().toString();

                ref = FirebaseDatabase.getInstance().getReference().child("Users");


                if(status == 1) {

                    ref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(dataSnapshot.hasChild(user+"---"+pass)) {
                                Toast.makeText(Authentication.this, "Sign In successfull!!!", Toast.LENGTH_LONG).show();
                                startApp();
                            }
                            else {
                                Toast.makeText(Authentication.this, "Invalid Username or Password", Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }
                else {

                    if(pass.equals(confPass)) {
                        Member member = new Member(user, pass);
                        ref.child(user+"---"+pass).setValue(member);
                        Toast.makeText(Authentication.this, "You are regeistered!!!", Toast.LENGTH_LONG).show();
                        startApp();
                    }
                    else {
                        Toast.makeText(Authentication.this, "Password did not match", Toast.LENGTH_LONG).show();
                    }

                }

            }
        });



    }


    public void startApp() {

        username1 = username.getText().toString();
        password1 = password.getText().toString();

        /*
        try {

            FileOutputStream fOut = new FileOutputStream(filename);
            fOut.write((username.getText().toString()+"---"+password.getText().toString()).getBytes());
            Toast.makeText(Authentication.this, username.getText().toString()+"---"+password.getText().toString(), Toast.LENGTH_LONG).show();
            fOut.close();;

            Log.d(TAG, "User Credentials Saved");

        }
        catch (Exception e) {

            Log.d(TAG, "User Credentials Not Saved");

        }
        */

        Intent intent = new Intent(Authentication.this, Home.class);
        startActivity(intent);

    }
}
