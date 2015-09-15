package com.crop_sense.farmerinterfaceapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;


public class OptionsScreen extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();

        Boolean decision = intent.getBooleanExtra(AskIfPlanting.DECISION, true);

        setContentView(R.layout.activity_options_screen);


    }

    public void loadDisplayVideos(View view){
        Intent intent = new Intent(this, DisplayVideos.class);
        startActivity(intent);
    }

    public void loadAskIfFlowers(View view){
        Intent intent = new Intent(this, AskIfFlowers.class);
        startActivity(intent);
    }

    public void homeClick (View view){
        Intent intent = new Intent(this, MainScreen.class);
        startActivity(intent);
    }

}
