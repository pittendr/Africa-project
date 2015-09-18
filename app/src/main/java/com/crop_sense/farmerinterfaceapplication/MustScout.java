package com.crop_sense.farmerinterfaceapplication;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;


public class MustScout extends AppCompatActivity {

    Uri voice = Uri.parse("android.resource://com.crop_sense.farmerinterfaceapplication/" + R.raw.voicememo);
    MediaPlayer voiceMemo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_must_scout);

       voiceMemo = new MediaPlayer();
//        try {
//            voiceMemo.setDataSource(this,voice );
//            voiceMemo.prepare();
//            voiceMemo.start();
//        }catch (Exception e){
//            //TODO
//        }

    }

    public void toggleScoutAudio (View view){
        if(voiceMemo.isPlaying()) {
            voiceMemo.stop();
            voiceMemo.reset();
        }else {
            try{
                voiceMemo.reset();
                voiceMemo = new MediaPlayer();
                voiceMemo.setDataSource(this, voice);
                voiceMemo.prepare();
                voiceMemo.start();
            }catch (Exception e){
                //TODO
            }
        }
    }

    public void scoutClick(View view){
        Intent intent = new Intent(this, ScoutVideo.class);
        Intent killIntent = new Intent(this, MustScout.class);
        killIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        this.startActivity(killIntent);
        finish();
        startActivity(intent);
    }

    public void homeClick (View view){
        Intent intent = new Intent(this, MainScreen.class);
        Intent killIntent = new Intent(this, MustScout.class);
        killIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        this.startActivity(killIntent);
        finish();
        startActivity(intent);
    }

}
