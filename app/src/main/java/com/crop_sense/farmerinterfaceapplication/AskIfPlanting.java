package com.crop_sense.farmerinterfaceapplication;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;


public class AskIfPlanting extends AppCompatActivity {

    public final static String DECISION = "Decision";

    Uri  voice = Uri.parse("android.resource://com.crop_sense.farmerinterfaceapplication/" + R.raw.voicememo);
    MediaPlayer voiceMemo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ask_if_planting);


        voiceMemo = new MediaPlayer();
//        try {
//            voiceMemo.setDataSource(this,voice );
//            voiceMemo.prepare();
//            voiceMemo.start();
//        }catch (Exception e){
//            //TODO
//        }
    }

    public void togglePlantingAudio (View view){
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

    public void yesClick (View view){
        Intent intent = new Intent(this, OptionsScreen.class);
        intent.putExtra(DECISION, true);
        Intent killIntent = new Intent(this, AskIfPlanting.class);
        killIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        this.startActivity(killIntent);
        finish();
        startActivity(intent);
    }

    public void noClick (View view){
        Intent intent = new Intent(this, OptionsScreen.class);
        intent.putExtra(DECISION, false);
        Intent killIntent = new Intent(this, AskIfPlanting.class);
        killIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        this.startActivity(killIntent);
        finish();
        startActivity(intent);
    }

    public void homeClick (View view){
        Intent intent = new Intent(this, MainScreen.class);
        Intent killIntent = new Intent(this, AskIfPlanting.class);
        killIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        this.startActivity(killIntent);
        finish();
        startActivity(intent);
    }


}
