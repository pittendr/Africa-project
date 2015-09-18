package com.crop_sense.farmerinterfaceapplication;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;


public class GoBackNextDay extends AppCompatActivity {

    Uri voice = Uri.parse("android.resource://com.crop_sense.farmerinterfaceapplication/" + R.raw.voicememo);
    MediaPlayer voiceMemo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_go_back_next_day);
        
        voiceMemo = new MediaPlayer();
//        try {
//            voiceMemo.setDataSource(this,voice );
//            voiceMemo.prepare();
//            voiceMemo.start();
//        }catch (Exception e){
//            //TODO
//        }
    }

    public void toggleGoBackAudio (View view){
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

    public void homeClick (View view){
        Intent intent = new Intent(this, MainScreen.class);
        startActivity(intent);
    }
}
