package com.example.safetyfirst;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Arrays;
import java.util.Vector;

public class Track extends AppCompatActivity {

    public static EditText key;
    Button fetch;
    Button next;
    Button previous;
    ListView GPSCoordinates;
    ImageView imageView;

    FirebaseStorage storage;
    public DatabaseReference ref;

    private String TAG = "Track";

    int index = 0;
    int count = 0;

    Vector<Bitmap> downloadedImages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track);

        key = (EditText) findViewById(R.id.key);
        fetch = (Button) findViewById(R.id.fetch);
        next = (Button) findViewById(R.id.next);
        previous = (Button) findViewById(R.id.previous);
        GPSCoordinates = (ListView) findViewById(R.id.GPSCoordinates);
        imageView = (ImageView) findViewById(R.id.imageView1);


        FirebaseApp.initializeApp(Track.this);

        storage = FirebaseStorage.getInstance();

        //next.setVisibility(View.INVISIBLE);
        //previous.setVisibility(View.INVISIBLE);


        fetch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String keyValue = key.getText().toString();

                ref = FirebaseDatabase.getInstance().getReference().child("Keys");

                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Toast.makeText(Track.this, keyValue+" "+(dataSnapshot.hasChild(keyValue) && !keyValue.equals((""))), Toast.LENGTH_LONG).show();

                        if(dataSnapshot.hasChild(keyValue) && !keyValue.equals((""))) {

                            //next.setVisibility(View.VISIBLE);
                            //previous.setVisibility(View.VISIBLE);

                            Toast.makeText(Track.this, "Valid Key", Toast.LENGTH_LONG).show();

                            try {

                                ref = FirebaseDatabase.getInstance().getReference().child("Keys/" + Track.key.getText().toString());


                                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                        StringBuilder sb = new StringBuilder();
                                        int count = 0;
                                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                            try {
                                                String s = (String) snapshot.getValue();
                                                if (!s.equals("0")) {
                                                    sb.append(s + "\n");
                                                    count++;
                                                }

                                            } catch (Exception e) {
                                                Toast.makeText(Track.this, e.toString() + "11111111", Toast.LENGTH_LONG).show();
                                            }

                                        }



                                        Toast.makeText(Track.this, "Step 1 Done", Toast.LENGTH_LONG).show();

                                        String data[];

                                        try {
                                            /*
                                            String s = sb.toString();
                                            data = s.split("\n");

                                            String arr[] = new String[data.length + 1];

                                            arr[0] = "Time-Stamp\t\tCoodinates";

                                            for (int i = 0; i < data.length; i++) {

                                                String temp[] = data[i].split(" ");

                                                arr[i + 1] = temp[0] + "\t\t\t" + temp[1] + "\t\t\t" + temp[2];

                                            }
                                            */


                                            String s = sb.toString();
                                            data = s.split("\n");

                                            String arr[] = new String[data.length + 1];

                                            arr[0] = "Time-Stamp\t\t\t\tCoodinates";

                                            for (int i = 0; i < data.length; i++) {

                                                arr[i + 1] = data[i];

                                            }




                                            ArrayAdapter arrayAdapter = new ArrayAdapter(Track.this,android.R.layout.simple_expandable_list_item_1, Arrays.asList(arr));
                                            GPSCoordinates.setAdapter(arrayAdapter);
                                            Toast.makeText(Track.this, "Valid Keyaaa", Toast.LENGTH_LONG).show();

                                            downloadImage();




                                        } catch (Exception e) {
                                            Toast.makeText(Track.this, e.toString() + "22222222222222", Toast.LENGTH_LONG).show();
                                        }


                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });

                            }

                            catch (Exception e) {

                                Toast.makeText(Track.this, e.toString() + "333333333333333", Toast.LENGTH_LONG).show();

                            }



                        }
                        else {

                            Toast.makeText(Track.this, "Invalid Key", Toast.LENGTH_LONG).show();

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
        });

        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //index = (index-1)%Status.totalTimesSendingData;
                displayImage();
                if(index == 0) {
                    index = Status.totalTimesSendingData-1;
                }
                else {
                    index--;
                }

            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                displayImage();
                index = (index+1)%Status.totalTimesSendingData;

            }
        });



    }



    public void downloadImage() {

        try {

            ref = FirebaseDatabase.getInstance().getReference().child("Images/"+key.getText().toString());

            downloadedImages = new Vector<>();

            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    count = 0;

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                        String filename = snapshot.getValue().toString()+".jpg";
                        Toast.makeText(Track.this, filename+" "+key.getText().toString()+"/"+filename, Toast.LENGTH_LONG).show();
                        StorageReference islandRef = storage.getReference(key.getText().toString()+"/"+filename);

                        final long ONE_MEGABYTE = 1024 * 1024;
                        islandRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                            @Override
                            public void onSuccess(byte[] bytes) {

                                // Data for "images/island.jpg" is returns, use this as needed
                                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                                downloadedImages.add(bitmap);
                                Toast.makeText(Track.this, "Image "+count+" downloaded", Toast.LENGTH_LONG).show();
                                Log.d(TAG, "Image "+count+" downloaded");
                                count++;
                                //imageView.setImageBitmap(bitmap);
                                if(count == Status.totalTimesSendingData) {
                                    displayImage();
                                }

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                // Handle any errors
                            }
                        });




                    }



                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            /*
            StorageReference islandRef = storage.getReference("mountains.jpg");

            //StorageReference islandRef = storageRef.child(key.getText().toString());
            */


            /*
            Toast.makeText(Track.this, "Success", Toast.LENGTH_LONG).show();
            if(true) {
                return;
            }
            */

            /*
            final long ONE_MEGABYTE = 1024 * 1024;
            islandRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    // Data for "images/island.jpg" is returns, use this as needed
                    Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    imageView.setImageBitmap(bitmap);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle any errors
                }
            });

            */

            /*
            islandRef.Lis
            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                try {
                    String s = (String) snapshot.getValue();
                    if (!s.equals("0")) {
                        sb.append(s + "\n");
                        count++;

                    }
        */
        }
        catch (Exception e) {
            Toast.makeText(Track.this, e.toString()+" eeeeeeeeeeeee", Toast.LENGTH_LONG).show();
        }

    }



    public void displayImage() {

        imageView.setImageBitmap(downloadedImages.get(index));

    }


}
