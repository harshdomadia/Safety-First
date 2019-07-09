package com.example.safetyfirst;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class Handler extends AppCompatActivity {

    public ImageView mimageView;
    public static final int REQUEST_IMAGE_CAPTURE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_handler);

        Button send = (Button) findViewById(R.id.send);
        Button capture = (Button) findViewById(R.id.capture);
        mimageView = (ImageView) findViewById(R.id.imageView);


        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                //Getting intent and PendingIntent instance
                Intent intent=new Intent(getApplicationContext(),Handler.class);
                PendingIntent pi=PendingIntent.getActivity(getApplicationContext(), 0, intent,0);

                //Get the SmsManager instance and call the sendTextMessage method to send message
                SmsManager sms=SmsManager.getDefault();
                sms.sendTextMessage("9423219840", null, "---------------------", pi,null);
                Toast.makeText(getApplicationContext(), "Message Sent successfully!", Toast.LENGTH_LONG).show();
                */
            }
        });


        capture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePicture();
            }
        });

        //Toast.makeText(Handler.this,generatekey("atharva456"),Toast.LENGTH_LONG).show();



    }

    public String generatekey(String username) {
        StringBuilder sb = new StringBuilder();
        int n=5;
        char arr[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'} ;
        sb.append(username+"-");
        for(int i=0;i<n;i++) {
            sb.append(arr[((int)(Math.random()*10000))%(arr.length)]);
        }
        return sb.toString();
    }


    public void takePicture() {
        Intent imageTakeIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if(imageTakeIntent.resolveActivity(getPackageManager())!=null) {
            startActivityForResult(imageTakeIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode==REQUEST_IMAGE_CAPTURE && resultCode==RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            mimageView.setImageBitmap(imageBitmap);
        }
    }

}

