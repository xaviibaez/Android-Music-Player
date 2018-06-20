package com.example.pc.mymusic;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    Button button, btnCrearLlista;
    ListView lv;
    String[] items;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lv = (ListView) findViewById(R.id.lvPlaylist);
        button = (Button) findViewById(R.id.btnRandom);
        btnCrearLlista = (Button) findViewById(R.id.btnCrearLlista);

        int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 0;

        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            }
            else {

                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
        else {
            // Permission has already been granted
        }
        final ArrayList<File> mySongs = findSongs(Environment.getExternalStorageDirectory());
        items = new String[ mySongs.size() ];

        for(int i = 0; i<mySongs.size(); i++){
            items[i] = mySongs.get(i).getName().toString().replace(".mp3", "").replace(".wav", "");
        }

        //ArrayAdapter<String> adp = new ArrayAdapter<String>(getApplicationContext(), R.layout.song_layout,R.id.textView, items);
        ArrayAdapter<String> adp = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, items);

        lv.setAdapter(adp);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                startActivity(new Intent(getApplicationContext(), Player.class).putExtra("pos",position).putExtra("songlist",mySongs));//24:01
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            Random rand = new Random();
            @Override
            public void onClick(View v) {
                int positionR = rand.nextInt(mySongs.size());
                startActivity(new Intent(getApplicationContext(), Player.class).putExtra("pos", positionR).putExtra("songlist",mySongs));//24:01
            }
        });

        btnCrearLlista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), CrearLlista.class));
            }
        });
    }

    public ArrayList<File> findSongs(File root){
        ArrayList<File> al = new ArrayList<File>();
        File[] files =  root.listFiles();
        for(File singleFile : files){
            if(singleFile.isDirectory() && !singleFile.isHidden()){
                al.addAll(findSongs(singleFile));
            }
            else{
                if(singleFile.getName().endsWith(".mp3") || singleFile.getName().endsWith(".wav")){
                    al.add(singleFile);
                }
            }
        }
        return al;
    }

    public void toast(String text){
        Toast.makeText(getApplicationContext(),text,Toast.LENGTH_SHORT).show();
    }

}
