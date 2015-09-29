package com.crop_sense.farmerinterfaceapplication;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.SoundEffectConstants;
import android.view.View;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


public class MainScreen extends AppCompatActivity {

    VideoView introVideo;
    Uri uri = Uri.parse("android.resource://com.crop_sense.farmerinterfaceapplication/"+R.raw.explanationvideo);


    SoundPool soundPool;
    int soundId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            createNewSoundPool();
        }else{
            createOldSoundPool();
        }

        soundId = soundPool.load(getApplicationContext(), R.raw.buttonclick, 1);


        introVideo = (VideoView) findViewById(R.id.introVideo);
        introVideo.setVideoURI(uri);
        introVideo.setMediaController(new MediaController(this));
        introVideo.requestFocus();
        introVideo.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                introVideo.start();
            }
        });


    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    protected void createNewSoundPool(){
        AudioAttributes attributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();
        soundPool = new SoundPool.Builder()
                .setAudioAttributes(attributes)
                .build();
    }
    @SuppressWarnings("deprecation")
    protected void createOldSoundPool(){
        soundPool = new SoundPool(soundId ,AudioManager.STREAM_MUSIC,0);
    }

    public void nextClick (View view){
        introVideo.stopPlayback();

//        buttonClick = new MediaPlayer();
//        try {
//            buttonClick.setDataSource(this,click );
//            buttonClick.prepare();
//            buttonClick.start();
//        }catch (Exception e){
//            //TODO
//        }
        soundPool.play(soundId,1,1,0,0,1);

        Intent intent = new Intent(this, SmallMessageBox.class);
        intent.putExtra("screenNumber", 1);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {

        soundPool.play(soundId,1,1,0,0,1);

        super.onBackPressed();
        this.finish();
    }



}
