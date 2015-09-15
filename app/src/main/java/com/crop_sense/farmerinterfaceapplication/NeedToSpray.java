package com.crop_sense.farmerinterfaceapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;


public class NeedToSpray extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options_screen);

        //TODO autoplay audio

    }

    public void toggleAudio (View view){
        //TODO toggle audio button
    }

    public void goClick (View view){
        Intent intent = new Intent(this, SprayVideo.class);
        startActivity(intent);
    }

    public void homeClick (View view){
        Intent intent = new Intent(this, MainScreen.class);
        startActivity(intent);
    }

}
