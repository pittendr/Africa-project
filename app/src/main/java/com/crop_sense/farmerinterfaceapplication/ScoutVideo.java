package com.crop_sense.farmerinterfaceapplication;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;


public class ScoutVideo extends AppCompatActivity {

    VideoView scoutVideo;
    Uri uri = Uri.parse("android.resource://com.crop_sense.farmerinterfaceapplication/"+R.raw.introvideo);

    ImageButton yesButton;
    ImageButton noButton;
    TextView textView;
    ImageView fade;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scout_video);

        scoutVideo = (VideoView) findViewById(R.id.scoutVideo);
        yesButton = (ImageButton) findViewById(R.id.yesButton2);
        noButton = (ImageButton) findViewById(R.id.noButton2);
        textView = (TextView) findViewById(R.id.textView);
        fade = (ImageView) findViewById(R.id.fade);

        scoutVideo.setVideoURI(uri);
        scoutVideo.setMediaController(new MediaController(this));
        scoutVideo.requestFocus();
        scoutVideo.start();

        scoutVideo.setOnCompletionListener(
                new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        yesButton.setVisibility(View.VISIBLE);
                        noButton.setVisibility(View.VISIBLE);
                        textView.setVisibility(View.VISIBLE);
                        scoutVideo.setVisibility(View.INVISIBLE);
                        scoutVideo.stopPlayback();

                    }
                }
        );

    }

    public void yesScoutClick (View view){
        yesButton.setVisibility(View.INVISIBLE);
        noButton.setVisibility(View.INVISIBLE);
        textView.setVisibility(View.INVISIBLE);
        scoutVideo.setVisibility(View.VISIBLE);
        scoutVideo.requestFocus();
        scoutVideo.seekTo(0);
        scoutVideo.start();
    }

    public void noScoutClick (View view){
        Intent intent = new Intent(this, SampleSelection.class);
        Intent killIntent = new Intent(this, ScoutVideo.class);
        killIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        this.startActivity(killIntent);
        finish();
        startActivity(intent);
    }

    public void homeClick (View view){
        scoutVideo.stopPlayback();
        scoutVideo.suspend();
        Intent intent = new Intent(this, MainScreen.class);
        Intent killIntent = new Intent(this, ScoutVideo.class);
        killIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        this.startActivity(killIntent);
        finish();
        startActivity(intent);
    }

}
