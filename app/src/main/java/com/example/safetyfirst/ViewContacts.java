package com.example.safetyfirst;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class ViewContacts extends AppCompatActivity {

    public EditText search;
    public Button ok;
    public TextView show;
    public ListView lv;

    public static final String SelectedContactsfile = "SelectedContactsfile.txt";

    public int c1 = Color.parseColor("#FFB6B546");
    public int c2 = Color.parseColor("#FFCCCB4C");

    String g="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_contacts);
        final HashMap<String, String> map = loadcontacts();
        search = (EditText) findViewById(R.id.search);
        ok = (Button) findViewById(R.id.ok);
        show = (TextView) findViewById(R.id.show);
        lv = (ListView) findViewById(R.id.lv);

        show.setVisibility(View.INVISIBLE);

        //getSelectedContactsfilelist();
        showSelectedContacts();

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadcontacts();
            }
        });

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //show.setText(Integer.toString(position));
                //ListAdapter listAdapter = lv.getAdapter();
                //show.setText(Integer.toString(lv[position]));
                final HashSet <String> selectedNames = getSelectedContactsfileSet();
                String s = (String) lv.getItemAtPosition(position);
                if(selectedNames.contains(s)) {
                    selectedNames.remove(s);
                    view.setBackgroundColor(c2);
                    Toast.makeText(ViewContacts.this, s+" removed", Toast.LENGTH_LONG).show();
                }
                else {
                    selectedNames.add(s);
                    view.setBackgroundColor(c1);
                    Toast.makeText(ViewContacts.this, s+" added", Toast.LENGTH_LONG).show();
                }


                saveContactsfileset(selectedNames);
                show.setText("1111111111111111111111111111111");



                //show.setText(s);
                /*
                for(int i=0;i<listAdapter.getCount();i++) {

                }
                */


            }
        });

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                int n = map.size();
                String names[] = new String[n];
                int i=0;
                Set<String> keys = map.keySet();
                String s1 = s.toString().toLowerCase();
                //StringBuilder b1 = new StringBuilder();
                for(String key: keys){
                    names[i] = key;
                    //show.setText(key);
                    i++;
                }

                Arrays.sort(names);

                int countMatches=0;

                for(i=0;i<n;i++) {
                    if(names[i].toLowerCase().startsWith(s1)) {
                        //matches[countMatches] = names[i];
                        //b1.append(names[i]);
                        countMatches++;
                    }
                }

                String matches[] = new String[countMatches];

                countMatches=0;

                for(i=0;i<n;i++) {
                    if(names[i].toLowerCase().startsWith(s1)) {
                        matches[countMatches] = names[i];
                        //b1.append(names[i]);
                        countMatches++;
                    }
                }

                final String matches1[] = matches.clone();

                final HashSet<String> hs = getSelectedContactsfileSet();
                show.setText(hs.toString());

                List<String> matches_list = new ArrayList<>(Arrays.asList(matches));

                //show.setText(b1.toString());
                //show.setText(Integer.toString(countMatches));

                final ArrayAdapter<String> arrayAdapter= new ArrayAdapter<String>
                        (ViewContacts.this, android.R.layout.simple_list_item_1, matches_list){
                    @Override
                    public View getView(int position, View convertView, ViewGroup parent){
                        // Get the current item from ListView
                        View view = super.getView(position,convertView,parent);
                        /*
                        if(position %2 == 1)
                        {
                            // Set a background color for ListView regular row/item
                            view.setBackgroundColor(Color.parseColor("#FFB6B546"));
                        }
                        else
                        {
                            // Set the background color for alternate row/item
                            view.setBackgroundColor(Color.parseColor("#FFCCCB4C"));
                        }
                        */

                        if(hs.contains(matches1[position])) {
                            view.setBackgroundColor(c1);
                        }
                        else {
                            view.setBackgroundColor(c2);
                        }
                        return view;
                    }
                };

                // DataBind ListView with items from ArrayAdapter
                lv.setAdapter(arrayAdapter);

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


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

    public HashSet<String> getSelectedContactsfileSet() {
        HashSet <String> hs = new HashSet<>();

        int c;
        StringBuilder temp = new StringBuilder();

        File file = new File(SelectedContactsfile);
        show.setText(Boolean.toString(file.exists()));

        /*
        if(!file.exists()) {
            try {
                FileOutputStream fOut = openFileOutput(SelectedContactsfile, MODE_PRIVATE);
                //String s = SelectedContactsfile+""+"\nas\n4\nt";
                //String s="Devansh";
                //fOut.write(s.getBytes());
                fOut.close();
            } catch (Exception e) {
                //e.printStackTrace();
                show.setText(e.toString());
            }
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

        String names[] = temp.toString().split("\n");

        for(int i=0;i<names.length;i++) {
            hs.add(names[i]);
        }

        return hs;
    }


    public String[] getSelectedContactsfilelist() {
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


    public void saveContactsfileset(HashSet<String> hs) {

        StringBuilder sb = new StringBuilder();

        Iterator<String> it = hs.iterator();

        while(it.hasNext()){
            sb.append(it.next()+"\n");
        }

        try {
            FileOutputStream fOut = openFileOutput(SelectedContactsfile, MODE_PRIVATE);
            fOut.write(sb.toString().getBytes());
            fOut.close();
        }
        catch (Exception e) {
            //e.printStackTrace();
            show.setText(e.toString());
        }


    }


    public void showSelectedContacts() {
        String selectedNames[] = getSelectedContactsfilelist();
        //show.setText(Arrays.toString(selectedNames));
        if(selectedNames.length==1 && selectedNames[0].equals("")) {
            return;
        }
        //show.setText(selectedNames[0]);

        Arrays.sort(selectedNames);

        List<String> matches_list = new ArrayList<>(Arrays.asList(selectedNames));

        //show.setText(b1.toString());
        //show.setText(Integer.toString(countMatches));

        final ArrayAdapter<String> arrayAdapter= new ArrayAdapter<String>
                (ViewContacts.this, android.R.layout.simple_list_item_1, matches_list){
            @Override
            public View getView(int position, View convertView, ViewGroup parent){
                // Get the current item from ListView
                View view = super.getView(position,convertView,parent);
                view.setBackgroundColor(c1);
                return view;
            }
        };

        // DataBind ListView with items from ArrayAdapter
        lv.setAdapter(arrayAdapter);

    }

}
