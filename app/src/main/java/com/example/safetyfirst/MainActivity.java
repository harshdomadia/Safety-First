package com.example.safetyfirst;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    public LocationManager locationManager;
    public LocationListener locationListener;
    public static final String TAG = ViewContacts.class.getSimpleName();

    public ScreenOnOffReceiver screenOnOffReceiver = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*
        final ScreenActionReceiver screenactionreceiver = new ScreenActionReceiver();

        registerReceiver(screenactionreceiver, screenactionreceiver.getFilter());
        */






        Intent backgroundService = new Intent(getApplicationContext(), ScreenOnOffBackgroundService.class);
        startService(backgroundService);

        Log.d(ScreenOnOffReceiver.SCREEN_TOGGLE_TAG, "Activity onCreate");








        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {

            @Override
            public void onLocationChanged(Location location) {

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        Button send = (Button) findViewById(R.id.send);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this,
                        "Button clicked", Toast.LENGTH_LONG).show();
            }
        });

        Button contacts = (Button) findViewById(R.id.contacts);
        contacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                Toast.makeText(MainActivity.this,
                        "Button clicked", Toast.LENGTH_LONG).show();
                */
                Intent intent = new Intent(MainActivity.this, ViewContacts.class);
                startActivity(intent);
            }
        });


        EditText editText = (EditText) findViewById(R.id.key);

        Button ok = (Button) findViewById(R.id.ok);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        Button go = (Button) findViewById(R.id.go);
        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Handler.class);
                startActivity(intent);
            }
        });

        Button start = (Button) findViewById(R.id.start);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Authentication.class);
                startActivity(intent);
            }
        });


        final TextView textView = (TextView) findViewById(R.id.textView);

        Button location = (Button) findViewById(R.id.location);
        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                textView.setText(location.getLatitude()+" "+location.getLongitude());
            }
        });

        Button openCamera = (Button) findViewById(R.id.openCamera);
        openCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CustomCamera.class);
                startActivity(intent);
            }
        });


        Intent intent = new Intent(MainActivity.this, Authentication.class);
        startActivity(intent);



    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        Log.d(ScreenOnOffReceiver.SCREEN_TOGGLE_TAG, "Activity onDestroy");
    }


}
