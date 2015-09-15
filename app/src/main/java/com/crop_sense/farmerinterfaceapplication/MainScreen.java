package com.crop_sense.farmerinterfaceapplication;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.MediaController;
import android.widget.VideoView;


public class MainScreen extends AppCompatActivity {

    VideoView introVideo;
    Uri uri = Uri.parse("android.resource://com.crop_sense.farmerinterfaceapplication;/"+R.raw.test);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

//        introVideo = (VideoView) findViewById(R.id.introVideo);
//
//        introVideo.setVideoURI(uri);
//        introVideo.setMediaController(new MediaController(this));
//        introVideo.requestFocus();
//        introVideo.start();


    }

    public void nextClick (View view){
        Intent intent = new Intent(this, AskIfPlanting.class);
        startActivity(intent);
    }

    public void homeClick (View view){
        Intent intent = new Intent(this, MainScreen.class);
        startActivity(intent);
    }

}
