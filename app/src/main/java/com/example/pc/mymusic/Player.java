package com.example.pc.mymusic;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

public class Player extends AppCompatActivity implements View.OnClickListener{
    static MediaPlayer mp;
    ArrayList<File> mySongs ;
    int position;
    Uri u;
    Thread updateSeekBar;

    SeekBar sb, sbV;
    Button btPlay, btFF, btFB, btNxt, btPv;
    TextView songName, txtEnd, txtBegin;

    Boolean esFinal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        btPlay = (Button) findViewById(R.id.btPlay);
        btFF = (Button) findViewById(R.id.btFF);
        btFB = (Button) findViewById(R.id.btFB);
        btNxt = (Button) findViewById(R.id.btNxt);
        btPv = (Button) findViewById(R.id.btPv);

        btPlay.setOnClickListener(this);
        btFF.setOnClickListener(this);
        btFB.setOnClickListener(this);
        btNxt.setOnClickListener(this);
        btPv.setOnClickListener(this);

        songName = (TextView) findViewById(R.id.songName);
        txtEnd = (TextView) findViewById(R.id.txtEnd);
        txtBegin = (TextView) findViewById(R.id.txtBegin);

        sb = (SeekBar) findViewById(R.id.seekBar);
        sbV = (SeekBar) findViewById(R.id.sbVolumen);

        sbV.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        float volumeNum = progress / 100f;
                        mp.setVolume(volumeNum, volumeNum);
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                }
        );

        updateSeekBar = new Thread(){
            @Override
            public void run(){
                int totalDuration = mp.getDuration();
                int currentPosisiton = 0;
                while (currentPosisiton < totalDuration){
                    try {
                        sleep(500);
                        currentPosisiton = mp.getCurrentPosition();
                        sb.setProgress(currentPosisiton);
                        Message msg = new Message();
                        msg.what = mp.getCurrentPosition();
                        handler.sendMessage(msg);
                    }
                    catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };

        /*mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener(){

            @Override
            public void onCompletion(MediaPlayer mp) {
                Log.i("hola", "FINAL 2");
                //performOnEnd();
            }
        });*/
        if(mp!=null){
            mp.stop();
            mp.release();
        }

        Intent i = getIntent();
        Bundle b = i.getExtras();
        mySongs = (ArrayList) b.getParcelableArrayList("songlist");
        position = b.getInt("pos", 0);

        u = Uri.parse(mySongs.get(position).toString());
        mp = MediaPlayer.create(getApplicationContext(), u);
        mp.start();
        sb.setMax(mp.getDuration());
        updateSeekBar.start();

        songName.setText(mySongs.get(position).getName().toString().replace(".mp3", "").replace(".wav", ""));//Texto de la canción

        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mp.seekTo(seekBar.getProgress());
            }
        });
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    public String createTimeLabel(int time) {
        String timeLabel = "";
        int min = time / 1000 / 60;
        int sec = time / 1000 % 60;

        timeLabel = min + ":";
        if (sec < 10) timeLabel += "0";
        timeLabel += sec;

        return timeLabel;
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            int currentPosition = msg.what;

            // Update Labels.
            String elapsedTime = createTimeLabel(currentPosition);
            txtBegin.setText(elapsedTime);

            String remainingTime = createTimeLabel(mp.getDuration()-currentPosition);
            txtEnd.setText("- " + remainingTime);

            if(remainingTime.equals("0:00")){
                nextSong();
            }
        }
    };

    public void nextSong(){
        btPlay.setBackgroundResource(android.R.drawable.ic_media_pause);
        mp.stop();
        mp.release();
        position = (position + 1)%mySongs.size();
        u = Uri.parse(mySongs.get(position).toString());
        mp = MediaPlayer.create(getApplicationContext(), u);
        mp.start();
        sb.setMax(mp.getDuration());
        songName.setText(mySongs.get(position).getName().toString().replace(".mp3", "").replace(".wav", ""));//Texto de la canción
    }

    public void previousSong(){
        btPlay.setBackgroundResource(android.R.drawable.ic_media_pause);
        mp.stop();
        mp.release();
        if(position == 0){
            position = (position)%mySongs.size();
        }
        else{
            position = (position - 1)%mySongs.size();
        }
        /*position = (position-1<0)? mySongs.size(): position-1;
        if(position-1 < 0){
            position = mySongs.size()-1;
        }
        else{
            position = position - 1;
        }*/
        u = Uri.parse(mySongs.get(position).toString());//Aqui pone to String
        mp = MediaPlayer.create(getApplicationContext(), u);
        mp.start();//39.21
        sb.setMax(mp.getDuration());
        songName.setText(mySongs.get(position).getName().toString().replace(".mp3", "").replace(".wav", ""));//Texto de la canción*/
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.btPlay:
                if(mp.isPlaying()){
                    btPlay.setBackgroundResource(android.R.drawable.ic_media_play);
                    mp.pause();//48.00
                }
                else {
                    btPlay.setBackgroundResource(android.R.drawable.ic_media_pause);
                    mp.start();
                }
                break;

            case R.id.btFF:
                mp.seekTo(mp.getCurrentPosition()+5000);
                break;

            case R.id.btFB:
                mp.seekTo(mp.getCurrentPosition()-5000);
                break;

            case R.id.btNxt:
                nextSong();
                break;

            case R.id.btPv:
                previousSong();

                break;
        }
    }
}
