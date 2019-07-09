package com.example.safetyfirst;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.content.pm.PackageManager;
import android.telephony.SmsManager;

import android.util.Log;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Date;
import java.util.HashMap;

import static android.content.Context.MODE_PRIVATE;


public class ScreenOnOffReceiver extends BroadcastReceiver {

    String timestampFile = "";
    int clicks = 3;
    int maxTime = 1500;
    public final static String SCREEN_TOGGLE_TAG = "SCREEN_TOGGLE_TAG";
    public static int status = 1;

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if(Intent.ACTION_SCREEN_OFF.equals(action))
        {
            Log.d(SCREEN_TOGGLE_TAG, "Screen is turn off.");
            addAndCheck(context, intent);
        }else if(Intent.ACTION_SCREEN_ON.equals(action))
        {
            Log.d(SCREEN_TOGGLE_TAG, "Screen is turn on.");
            addAndCheck(context, intent);
        }
    }

    public void addAndCheck(Context context, Intent intent) {

        StringBuilder temp = new StringBuilder();
        int c;
        int startIndex;

        /*
        try {
            FileInputStream fIn = new FileInputStream(timestampFile);
            while((c = fIn.read()) != -1) {
                //temp = temp + Character.toString((char) c);
                temp.append(Character.toString((char) c));
            }
            Log.d(SCREEN_TOGGLE_TAG, "File present");
            //show.setText(temp);
        }
        catch(Exception e) {
            //e.printStackTrace();
            //show.setText(e.toString());
            Log.d(SCREEN_TOGGLE_TAG, e.toString());
            try {
                FileOutputStream fOut = new FileOutputStream(timestampFile);
                fOut.close();
                FileInputStream fIn = new FileInputStream(timestampFile);
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

        for(int i=0;i<timestampFile.length();i++) {
            temp.append(Character.toString(timestampFile.charAt(i)));
        }

        Log.d(SCREEN_TOGGLE_TAG, temp.toString()+"111111111111111111111");

        temp.append(new Date().getTime()+"\n");
        String data[] = temp.toString().split("\n");
        Log.d(SCREEN_TOGGLE_TAG, temp.toString());
        if(data.length == clicks) {
            if ((Long.parseLong(data[2]))-(Long.parseLong(data[0])) <= maxTime) {

                Log.d(SCREEN_TOGGLE_TAG, Integer.toString(clicks)+" clicks in less than "+Integer.toString(maxTime)+" second");


                status = 2;

                Intent intent1 = new Intent(context, Help.class);
                intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent1);


                /*

                Help help = new Help();

                String SelectedContactsFile = help.SelectedContactsFile;
                try {
                    HashMap<String, String> map = help.loadcontacts();
                }
                catch (Exception e) {
                    Log.d("Error", e.toString());
                }
                */

                //String selectedContacts[] = help.getSelectedContactsfilelist(SelectedContactsFile);

                //help.startSafetyApp1(map,selectedContacts);


                /*
                Intent intent1 = new Intent(context, CustomCamera.class);
                intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent1);
                */

                /*
                Intent intent1=new Intent(context,Handler.class);
                PendingIntent pi=PendingIntent.getActivity(context, 0, intent1,0);

                SmsManager sms=SmsManager.getDefault();
                sms.sendTextMessage("9423219840", null, "---------------------", pi,null);
                //Toast.makeText(getApplicationContext(), "Message Sent successfully!", Toast.LENGTH_LONG).show();
                //Deleting the first and storing them again in the file
                Log.d(SCREEN_TOGGLE_TAG,"SMS sent");
                */
            }

            startIndex = 1;
        }
        else {
            //Keeping the first and storing them again
            startIndex=0;
        }

        StringBuilder temp1 = new StringBuilder();

        for(int i=startIndex;i<data.length;i++) {
            temp1.append(data[i]+"\n");
        }

        timestampFile = temp1.toString();

        /*
        try {
            FileOutputStream fOut = new FileOutputStream(timestampFile);
            fOut.write(temp.toString().getBytes());
            fOut.close();
            //show.setText(temp);
        }
        catch (Exception e1) {
            //e.printStackTrace();
            //show.setText(e.toString());
        }
        */


    }

}