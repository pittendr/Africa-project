package com.crop_sense.farmerinterfaceapplication;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.VideoView;


public class SprayVideo extends AppCompatActivity {

    VideoView sprayVideo;
    Uri uri = Uri.parse("android.resource://com.crop_sense.farmerinterfaceapplication/"+R.raw.introvideo);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spray_video);

        sprayVideo = (VideoView) findViewById(R.id.sprayVideo);

        sprayVideo.setVideoURI(uri);
        sprayVideo.setMediaController(new MediaController(this));
        sprayVideo.requestFocus();
        sprayVideo.start();

    }

    public void homeClick (View view){
        Intent intent = new Intent(this, MainScreen.class);
        startActivity(intent);
    }
}
