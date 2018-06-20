package com.example.pc.mymusic;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Pc on 18/03/2018.
 */

public class CrearLlista extends AppCompatActivity {
    ListView crearLlista;
    String[] items;
    List<String> listaR = new ArrayList<String>();
    int pos;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crearllista);
        FloatingActionButton recordFab = (FloatingActionButton) findViewById(R.id.fab);
        crearLlista = (ListView) findViewById(R.id.crear_lista);
        final ArrayList<File> mySongs = findSongs(Environment.getExternalStorageDirectory());
        items = new String[mySongs.size()];
        for (int i = 0; i < mySongs.size(); i++) {
            items[i] = mySongs.get(i).getName().toString().replace(".mp3", "").replace(".wav", "");
        }
        ArrayAdapter<String> adp = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_checked, items);
        crearLlista.setAdapter(adp);

        crearLlista.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                pos=position;
                CheckedTextView textview = (CheckedTextView)view;
                textview.setChecked(!textview.isChecked());

                if(textview.isChecked()){
                    listaR.add(mySongs.get(position).getName().toString());

                }

                else {
                    listaR.remove(mySongs.get(position).getName().toString());
                }
            }
        });
        recordFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fEmergent(v);
            }
        });
    }
    public void fEmergent(View v){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.alert);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.custom_dialog, null);
        builder.setView(dialogView);
        final EditText edt = (EditText) dialogView.findViewById(R.id.edt1);

        builder.setPositiveButton(R.string.positivo, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(CrearLlista.this, R.string.hecho, Toast.LENGTH_SHORT).show();
                String x = edt.getText().toString();
                Intent i = new Intent(CrearLlista.this, PlayList.class);
                i.putExtra("nom", x);
                i.putStringArrayListExtra("llistaR", (ArrayList<String>) listaR);
                i.putExtra("pos", pos);
                startActivity(i);
            }
        });
        builder.setNegativeButton(android.R.string.cancel, null);
        Dialog dialog = builder.create();
        dialog.show();
    }
    public ArrayList<File> findSongs(File root){
        ArrayList<File> al = new ArrayList<File>();
        File[] files = root.listFiles();
        for(File singleFile : files){
            if (singleFile.isDirectory() && !singleFile.isHidden()){
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
    public void toast(String text) {
        Toast.makeText(getApplicationContext(),text, Toast.LENGTH_SHORT).show();
    }
}

