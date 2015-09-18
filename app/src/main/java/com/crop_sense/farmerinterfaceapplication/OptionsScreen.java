package com.crop_sense.farmerinterfaceapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;


public class OptionsScreen extends AppCompatActivity {

    ImageView optionsScout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();

        Boolean decision = intent.getBooleanExtra(AskIfPlanting.DECISION, true);

        setContentView(R.layout.activity_options_screen);

        optionsScout = (ImageView) findViewById(R.id.optionsscout);

        if (!decision){
            optionsScout.setAlpha(0.4f);
        }



    }

    public void loadDisplayVideos(View view){
        Intent intent = new Intent(this, DisplayVideos.class);
        Intent killIntent = new Intent(this, OptionsScreen.class);
        killIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        this.startActivity(killIntent);
        finish();
        startActivity(intent);
    }

    public void loadAskIfFlower(View view){
        Intent intent = new Intent(this, AskIfFlowers.class);
        Intent killIntent = new Intent(this, OptionsScreen.class);
        killIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        this.startActivity(killIntent);
        finish();
        startActivity(intent);
    }

    public void homeClick (View view){
        Intent intent = new Intent(this, MainScreen.class);
        startActivity(intent);
    }

}
