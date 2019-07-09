package com.example.safetyfirst;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class Home extends AppCompatActivity {

    Button helpMe;
    Button track;
    Button contacts;
    Button faqs;
    Button settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        helpMe = (Button) findViewById(R.id.helpMe);
        track = (Button) findViewById(R.id.track);
        //faqs = (Button) findViewById(R.id.faqs);
        settings = (Button) findViewById(R.id.settings);
        contacts = (Button) findViewById(R.id.contacts1);



        /*
        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Home.this,"Selected",Toast.LENGTH_LONG).show();

                //Intent intent = new Intent(Home.this, Help.class);
                //startActivity(intent);

            }
        });
        */

        track.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Home.this, Track.class);
                startActivity(intent);

            }
        });

        helpMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(Home.this,"Selected",Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Home.this, Help.class);
                startActivity(intent);

            }
        });


        contacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Home.this, ViewContacts.class);
                startActivity(intent);

            }
        });

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Home.this, Settings.class);
                startActivity(intent);


            }
        });

    }
}
