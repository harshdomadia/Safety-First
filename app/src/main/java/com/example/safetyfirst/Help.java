package com.example.safetyfirst;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Handler;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;

public class Help extends AppCompatActivity {

    Button safe;
    Button clear;
    Button alert;

    public String filename = Authentication.filename;

    public LocationManager locationManager;
    public LocationListener locationListener;

    public DatabaseReference ref;

    public int status = 1;

    private String TAG = "Help";

    public static String key;


    //public String alertMessage;
    public  String trademarkMessage = "@SafetyFirst ";
    public String alertMessage = "User is in danger, act immediately. ";
    public String SelectedContactsFile = ViewContacts.SelectedContactsfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        safe = (Button) findViewById(R.id.safe);
        clear = (Button) findViewById(R.id.clear);
        alert = (Button) findViewById(R.id.alert);

        FirebaseApp.initializeApp(Help.this);

        final HashMap<String, String> map = loadcontacts();
        final String selectedContacts[] = getSelectedContactsfilelist(SelectedContactsFile);

        safe.setVisibility(View.INVISIBLE);
        clear.setVisibility(View.INVISIBLE);
        alert.setVisibility(View.VISIBLE);


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




        safe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                safe.setVisibility(View.INVISIBLE);
                clear.setVisibility(View.INVISIBLE);
                alert.setVisibility(View.VISIBLE);

                String message = trademarkMessage+"The user is now safe so no need to worry";
                sendmessage(message, map, selectedContacts);
            }
        });


        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                safe.setVisibility(View.INVISIBLE);
                clear.setVisibility(View.INVISIBLE);
                alert.setVisibility(View.VISIBLE);

                String message = trademarkMessage+"The user by mistakely activated the app so ignore the message";
                sendmessage(message, map, selectedContacts);

            }
        });


        alert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startSafetyApp(map, selectedContacts);

                /*
                safe.setVisibility(View.VISIBLE);
                clear.setVisibility(View.VISIBLE);
                alert.setVisibility(View.INVISIBLE);

                key = getKey();

                String message = trademarkMessage+alertMessage+"Location:- "+getGPSLocation()+" Use this key to track the user:- "+key;
                sendmessage(message, map, selectedContacts);

                sendGPSLocations();
                Intent intent1 = new Intent(Help.this, CustomCamera.class);
                intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent1);
                */



            }
        });

        if(ScreenOnOffReceiver.status == 2) {
            ScreenOnOffReceiver.status = 1;
            startSafetyApp(map, selectedContacts);
        }



    }

    public void startSafetyApp(HashMap<String, String> map, String selectedContacts[]) {

        safe.setVisibility(View.VISIBLE);
        clear.setVisibility(View.VISIBLE);
        alert.setVisibility(View.INVISIBLE);

        key = getKey();

        String message = trademarkMessage+alertMessage+"Location:- "+getGPSLocation()+" Use this key to track the user:- "+key;
        sendmessage(message, map, selectedContacts);

        sendGPSLocations();
        Intent intent1 = new Intent(Help.this, CustomCamera.class);
        intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent1);


    }


    public void startSafetyApp1(HashMap<String, String> map, String selectedContacts[]) {

        key = getKey();

        /*

        String message = trademarkMessage+alertMessage+"Location:- "+getGPSLocation()+" Use this key to track the user:- "+key;
        sendmessage(message, map, selectedContacts);

        sendGPSLocations();
        Intent intent1 = new Intent(Help.this, CustomCamera.class);
        intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent1);

        */

    }






    public void sendmessage(String message, HashMap<String,String> map, String selectedContacts[]) {

        if(Status.sendingMessages) {
        /*
        Intent intent1=new Intent(Help.this,Handler.class);
        PendingIntent pi=PendingIntent.getActivity(Help.this, 0, intent1,0);
        */


            SmsManager sms = SmsManager.getDefault();

            /*
            SmsManager sms = SmsManager.getDefault();
            sms.sendTextMessage("8691083100", null, "abcd", null, null);
            Toast.makeText(Help.this,"Message is sent to ", Toast.LENGTH_LONG).show();
            */

            for (int i = 0; i < selectedContacts.length; i++) {

                String name = selectedContacts[i];
                String phoneNumber = map.get(name);
                //Toast.makeText(Help.this, phoneNumber, Toast.LENGTH_SHORT).show();

                Log.d(TAG, "Message is sent to "+name+" whose phone number is "+phoneNumber);
                sms.sendTextMessage(phoneNumber, null, message, null, null);
                Toast.makeText(Help.this,"Message is sent to "+message+" "+name+" whose phone number is "+phoneNumber, Toast.LENGTH_LONG).show();
                Log.d(TAG, "Message is sent to "+name+" whose phone number is "+phoneNumber);

            }


        }
        else {
            Log.d(TAG, "Sending Message is not allowed");
        }


    }


    public HashMap<String, String> loadcontacts()  {
        //StringBuilder builder = new StringBuilder();
        ContentResolver contentResolver = getContentResolver();
        Cursor cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);

        //String data[][] = new String[cursor.getCount()][2];
        HashMap<String ,String> map = new HashMap<>();

        int i=0;
        if(cursor.getCount() > 0) {
            while(cursor.moveToNext()) {
                String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                int hasPhoneNumber = Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)));

                if(hasPhoneNumber > 0) {
                    Cursor cursor2 = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID+" = ?", new  String[] {id}, null);

                    while(cursor2.moveToNext()) {
                        String phoneNumber = cursor2.getString(cursor2.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        //builder.append("Contact : ").append(name).append(", Phone Number : ").append(phoneNumber).append("");
                        //data[i][0]=name;
                        //data[i][1]=phoneNumber;
                        map.put(name,phoneNumber);
                        i++;
                        break;
                    }

                    cursor2.close();;
                }
            }
        }

        cursor.close();;

        //show.setText(builder.toString());
        //show.setText(Arrays.toString(data));


        //return data;
        return map;

    }


    public String[] getSelectedContactsfilelist(String SelectedContactsfile) {
        int c;
        StringBuilder temp = new StringBuilder();

        /*
        File file = new File(SelectedContactsfile);
        show.setText(Boolean.toString(file.exists()));
        if(!file.exists()) {
            try {
                FileOutputStream fOut = openFileOutput(SelectedContactsfile, MODE_PRIVATE);
                //String s = SelectedContactsfile+""+"\nas\n4\nt";
                String s="Devansh";
                fOut.write(s.getBytes());
                fOut.close();
            } catch (Exception e) {
                //e.printStackTrace();
                show.setText(e.toString());
            }
        }
        try {
            FileInputStream fIn = openFileInput(SelectedContactsfile);
            while((c = fIn.read()) != -1) {
                //temp = temp + Character.toString((char) c);
                temp.append(Character.toString((char) c));
            }
            //show.setText(temp);
        }
        catch(Exception e) {
            //e.printStackTrace();
            show.setText(e.toString());
        }

        */

        try {
            FileInputStream fIn = openFileInput(SelectedContactsfile);
            while((c = fIn.read()) != -1) {
                //temp = temp + Character.toString((char) c);
                temp.append(Character.toString((char) c));
            }
            //show.setText(temp);
        }
        catch(Exception e) {
            //e.printStackTrace();
            //show.setText(e.toString());
            try {
                FileOutputStream fOut = openFileOutput(SelectedContactsfile, MODE_PRIVATE);
                fOut.close();
                FileInputStream fIn = openFileInput(SelectedContactsfile);
                while((c = fIn.read()) != -1) {
                    //temp = temp + Character.toString((char) c);
                    temp.append(Character.toString((char) c));
                }
                //show.setText(temp);
            }
            catch (Exception e1) {
                //e.printStackTrace();
                //show.setText(e.toString());
            }

        }

        if(temp.equals("")) {
            return new String[0];
        }

        String names[] = temp.toString().split("\n");

        return names;

    }


    public String getGPSLocation() {

        try {
            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);


            if (ActivityCompat.checkSelfPermission(Help.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(Help.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return "";
            }

            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            //textView.setText(location.getLatitude()+" "+location.getLongitude());

            //Intent intent = new Intent(Home.this, Help.class);
            //startActivity(intent);


            String lc = location.getLatitude() + " " + location.getLongitude();

            return lc;

        }
        catch (Exception e) {
            Log.d(TAG, "GPS could not be located because:- "+e.toString());
            //return e.toString()+" "+getKey();
            return getKey();
        }

    }


    public String getKey() {

        /*
        StringBuilder temp = new StringBuilder();
        int c;

        try {
            FileInputStream fIn = openFileInput(filename);
            while((c = fIn.read()) != -1) {
                //temp = temp + Character.toString((char) c);
                temp.append(Character.toString((char) c));
            }
            //show.setText(temp);
        }
        catch(Exception e) {
            //e.printStackTrace();
            //show.setText(e.toString());
            try {

                FileOutputStream fOut = openFileOutput(filename, MODE_PRIVATE);
                fOut.close();
                FileInputStream fIn = openFileInput(filename);
                while((c = fIn.read()) != -1) {
                    //temp = temp + Character.toString((char) c);
                    temp.append(Character.toString((char) c));
                }
                //show.setText(temp);
            }
            catch (Exception e1) {
                //e.printStackTrace();
                //show.setText(e.toString());
            }

        }
        */

        String s = Authentication.username1;

        String arr[] = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};
        Toast.makeText(Help.this, s, Toast.LENGTH_LONG).show();
        StringBuilder sb = new StringBuilder();
        sb.append(s+"---");

        for(int i=0; i<Status.lengthOfKey; i++) {
            sb.append(arr[((int)(Math.random()*10000))%arr.length]);
        }

        return sb.toString();




    }


    public void sendGPSLocations() {

        if(Status.sendinGPSCoordinatesToFirebase) {

            final int intervalsForSendingDataToFirebase = Status.intervalsForSendingDataToFirebase;
            final int totalTimesSendingData = Status.totalTimesSendingData;


            ref = FirebaseDatabase.getInstance().getReference().child("Keys").child(key);

            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    for(int i=0;i<totalTimesSendingData;i++) {

                        android.os.Handler handler1 = new Handler();
                        final int j = i;

                        handler1.postDelayed
                                (new Runnable() {
                                    @Override
                                    public void run() {

                                        Date date = new Date();
                                        String gpsCoordinatesWithTimestamp = date.toString()+" "+getGPSLocation();
                                        Toast.makeText(Help.this, gpsCoordinatesWithTimestamp, Toast.LENGTH_LONG).show();
                                        ref.child(Integer.toString(j)).setValue(gpsCoordinatesWithTimestamp);

                                    }
                                }, j*intervalsForSendingDataToFirebase*1000);

                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }
        else {
            Log.d(TAG,"Sending GPS coordinates to firebase is not allowed");
        }


    }



}
