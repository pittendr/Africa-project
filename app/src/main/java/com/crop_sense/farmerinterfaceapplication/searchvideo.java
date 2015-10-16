package com.crop_sense.farmerinterfaceapplication;

import android.content.Intent;
import android.graphics.Color;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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


public class searchvideo extends AppCompatActivity {

    VideoView introVideo;
    MediaPlayer video;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        String videoname = intent.getStringExtra("videoname");
        final int videoposition = intent.getIntExtra("videoposition", 0);

        setContentView(R.layout.activity_fullscreenvideo);

        Integer id = getResources().getIdentifier(videoname, "raw", getPackageName());
        String uriid = id.toString();


        Uri uri = Uri.parse("android.resource://com.crop_sense.farmerinterfaceapplication/"+uriid);


        introVideo = (VideoView) findViewById(R.id.fullscreenvideo);
        introVideo.setVideoURI(uri);
        introVideo.setMediaController(new MediaController(this));
        introVideo.requestFocus();
        introVideo.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                video = mp;
                introVideo.seekTo(videoposition);
                introVideo.start();
            }
        });



    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        setResult(8008, intent.putExtra("videoposition", introVideo.getCurrentPosition()));
        introVideo.stopPlayback();
        video.release();
        video=null;
        super.onBackPressed();
        this.finish();
    }



}
