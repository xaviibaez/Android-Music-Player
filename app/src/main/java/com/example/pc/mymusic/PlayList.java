package com.example.pc.mymusic;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Pc on 19/03/2018.
 */

public class PlayList extends AppCompatActivity implements Serializable {
    ListView veure_llista;
    String[] items;
    ArrayList<String> lista;
    TextView nomPlayList;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_veurellista);
        veure_llista = (ListView) findViewById(R.id.lista);
        nomPlayList = (TextView) findViewById(R.id.nomPlayList);
        lista = (ArrayList<String>) getIntent().getSerializableExtra("llistaR");

        final ArrayList<File> mySongs = findSongs(Environment.getExternalStorageDirectory());

        items = new String[mySongs.size()];

        ArrayAdapter<String> adp = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, lista);
        veure_llista.setAdapter(adp);

        Bundle extras = getIntent().getExtras();
        String nombre = extras.getString("nom");


        veure_llista.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(getApplicationContext(), Player.class).putExtra("pos", position).putExtra("songlist", mySongs));
            }
        });

        nomPlayList.setText(String.valueOf(nombre));
    }


    public ArrayList<File> findSongs(File root) {

        ArrayList<File> al = new ArrayList<File>();
        File[] files = root.listFiles();
        for (File singleFile : files) {
            if (singleFile.isDirectory() && !singleFile.isHidden()) {
                al.addAll(findSongs(singleFile));
            }
            else {
                if (singleFile.getName().endsWith(".mp3") || singleFile.getName().endsWith(".wav")) {
                    if (lista.contains(singleFile.getName())) {
                        al.add(singleFile);
                    }
                }
            }

        }
        return al;
    }
}
