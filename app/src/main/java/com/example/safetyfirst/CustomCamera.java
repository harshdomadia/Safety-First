package com.example.safetyfirst;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
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
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.util.Date;

public class CustomCamera extends AppCompatActivity {

    Camera camera;
    FrameLayout frameLayout;
    ShowCamera showCamera;
    Button capture;
    Button sendData;
    Button download;
    ImageView imageView;
    DatabaseReference ref;
    FirebaseStorage storage;
    StorageReference storageReference;
    public int globalCount;

    private String TAG = "ScreenActionReceiver";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_camera);
        frameLayout = (FrameLayout) findViewById(R.id.frameLayout);
        capture = (Button) findViewById(R.id.capture);
        sendData = (Button) findViewById(R.id.sendData);
        download = (Button) findViewById(R.id.download);
        imageView = (ImageView) findViewById(R.id.imageView);

        FirebaseApp.initializeApp(CustomCamera.this);

        storage = FirebaseStorage.getInstance();
        //storageReference = storage.getReference();

        Log.d(TAG,"abc "+storage.toString());

        //Toast.makeText(CustomCamera.this, "Firebase Connected",Toast.LENGTH_LONG).show();
        final Member member = new Member("Atharva", "Gole");
        ref = FirebaseDatabase.getInstance().getReference().child("Users");

        capture.setVisibility(View.INVISIBLE);
        sendData.setVisibility(View.INVISIBLE);
        download.setVisibility(View.INVISIBLE);



        capture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                captureImage1();
            }
        });

        camera = android.hardware.Camera.open();

        showCamera = new ShowCamera(this, camera);
        frameLayout.addView(showCamera);


        sendData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ref.child(member.usernname+"---"+member.password).child("default").setValue(member);
                Toast.makeText(CustomCamera.this, "Data Sent", Toast.LENGTH_LONG).show();
            }
        });

        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downloadImage();
            }
        });


        captureImage1();


    }

    public File getOutputMediaFile() {
        String state = Environment.getExternalStorageState();
        if(!state.equals(Environment.MEDIA_MOUNTED)) {
            return null;
        }
        else {
            File folder_gui = new File(Environment.getExternalStorageDirectory()+File.separator+"GUI");

            if(!folder_gui.exists()) {
                folder_gui.mkdirs();
            }

            File outputFile = new File(folder_gui, "temp.jpg");
            return outputFile;
        }
    }

    Camera.PictureCallback nPictureCallback = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            /*
            File picture_file = getOutputMediaFile();

            if(picture_file == null) {
                return;
            }
            else {
                try {
                    FileOutputStream fOut = new FileOutputStream(picture_file);
                    fOut.write(data);
                    fOut.close();

                    camera.startPreview();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
            */
            //Log.d(TAG, data.toString());

            final Bitmap picture = BitmapFactory.decodeByteArray(data, 0, data.length);
            Log.d(TAG, Integer.toString(picture.getPixel(50,50)));
            int p = picture.getPixel(50,50);
            int R = (p & 0xff0000) >> 16;
            int G = (p & 0xff00) >> 8;
            int B = p & 0xff;
            Log.d(TAG ,"RGB values:- "+R+" "+G+" "+B+" "+picture.getHeight()+" "+picture.getByteCount()+" "+(picture.getRowBytes()*picture.getRowBytes()));
            imageView.setImageBitmap(picture);
            camera.startPreview();
            Log.d(TAG, globalCount+" we are here");
            globalCount++;
            if(true) {

                Log.d(TAG,"Upload is started");

                ref = FirebaseDatabase.getInstance().getReference().child("Images").child(Help.key);

                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {

                        Date date = new Date();
                        ref.child(Integer.toString(globalCount)).setValue(date.toString());

                        // Create a storage reference from our app
                        StorageReference storageRef = storage.getReference(Help.key);

                        // Create a reference to "mountains.jpg"
                        StorageReference mountainsRef = storageRef.child(date.toString()+".jpg");

                        // Create a reference to 'images/mountains.jpg'
                        StorageReference mountainImagesRef = storageRef.child("images/"+date.toString()+".jpg");

                        // While the file names are the same, the references point to different files
                        mountainsRef.getName().equals(mountainImagesRef.getName());    // true
                        mountainsRef.getPath().equals(mountainImagesRef.getPath());    // false

                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        picture.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                        byte[] data1 = baos.toByteArray();

                        UploadTask uploadTask = mountainsRef.putBytes(data1);
                        uploadTask.addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                // Handle unsuccessful uploads
                            }
                        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                                // ...
                                Toast.makeText(CustomCamera.this,"Success in upload image",Toast.LENGTH_LONG).show();
                            }
                        });

                        Log.d(TAG,"Image uploaded");


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


            }
            /*
            try {
                FileOutputStream out = new FileOutputStream("temp.jpeg");
                picture.compress(Bitmap.CompressFormat.JPEG, 90, out);
                picture.recycle();
                camera.startPreview();
            } catch (Exception e) {
                e.printStackTrace();
            }

            if(globalCount==1) {
                StorageReference ref = storageReference.child("Images/1");
                ref.putFile(URI.("temp.jpeg"));
            }
            */

        }
    };


    public void captureImage() {
        if(camera!=null) {
            camera.takePicture(null, null, nPictureCallback);
        }

    }

    public void captureImage1() {

        if(Status.sendingImagesToFirebase) {

            for (int i = 1; i <= Status.totalTimesSendingData; i++) {
                android.os.Handler handler1 = new Handler();
                final int j = i;

                handler1.postDelayed
                        (new Runnable() {
                            @Override
                            public void run() {
                                if (camera != null) {
                                    Date date = new Date();
                                    ref.child(Integer.toString(j)).setValue(date.toString());
                                    camera.takePicture(null, null, nPictureCallback);
                                    Log.d(TAG, "Image:- " + j);
                                }
                            }
                        }, i*Status.intervalsForSendingDataToFirebase*1000);

            }


        }
        else {
            Log.d(TAG,"Sending Images to Firebase is not allowed");
        }


    }


    public void downloadImage() {

        StorageReference storageRef = storage.getReference();

        StorageReference islandRef = storageRef.child("mountains.jpg");

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

    }

}
