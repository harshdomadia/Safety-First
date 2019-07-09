package com.example.safetyfirst;

import android.content.ContentResolver;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import java.util.HashMap;

public class Settings extends AppCompatActivity {

    public CheckBox allowMessages;
    public CheckBox allowGPS;
    public CheckBox allowImages;

    public EditText alertInterval;
    public EditText numbersOfAlerts;

    public Button apply;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        allowMessages = (CheckBox) findViewById(R.id.allowMessages);
        allowGPS = (CheckBox) findViewById(R.id.allowGPS);
        allowImages = (CheckBox) findViewById(R.id.allowImages);

        alertInterval = (EditText) findViewById(R.id.alertInterval);
        numbersOfAlerts = (EditText) findViewById(R.id.numbersOfAlerts);


        apply = (Button) findViewById(R.id.apply);


        allowMessages.setChecked(Status.sendingMessages);
        allowGPS.setChecked(Status.sendinGPSCoordinatesToFirebase);
        allowImages.setChecked(Status.sendingImagesToFirebase);

        alertInterval.setText(Integer.toString(Status.intervalsForSendingDataToFirebase));
        numbersOfAlerts.setText(Integer.toString(Status.totalTimesSendingData));


        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (allowMessages.isChecked()) {

                    Status.sendingMessages = true;

                } else {

                    Status.sendingMessages = false;

                }

                if (allowGPS.isChecked()) {

                    Status.sendinGPSCoordinatesToFirebase = true;

                } else {

                    Status.sendinGPSCoordinatesToFirebase = false;

                }

                if (allowImages.isChecked()) {

                    Status.sendingImagesToFirebase = true;

                } else {

                    Status.sendingImagesToFirebase = false;

                }


                try {

                    Status.intervalsForSendingDataToFirebase = Integer.parseInt(alertInterval.getText().toString());

                    Status.totalTimesSendingData = Integer.parseInt(numbersOfAlerts.getText().toString());

                    Toast.makeText(Settings.this, "The status is set to " + Status.sendingMessages + " " + Status.sendinGPSCoordinatesToFirebase + " " + Status.sendingImagesToFirebase + " " + Status.intervalsForSendingDataToFirebase + " " + Status.totalTimesSendingData, Toast.LENGTH_LONG).show();

                } catch (Exception e) {

                    Toast.makeText(Settings.this, "Enter valid numbers", Toast.LENGTH_LONG).show();

                }


            }
        });


    }

}
