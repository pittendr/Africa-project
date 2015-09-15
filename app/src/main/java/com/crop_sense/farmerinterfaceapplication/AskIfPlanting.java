package com.crop_sense.farmerinterfaceapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;


public class AskIfPlanting extends AppCompatActivity {

    public final static String DECISION = "Decision";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ask_if_planting);

        //TODO autoplay audio
    }

    public void toggleAudio (View view){
        //TODO toggle audio button
    }

    public void yesClick (View view){
        Intent intent = new Intent(this, OptionsScreen.class);
        intent.putExtra(DECISION, true);
        startActivity(intent);
    }

    public void noClick (View view){
        Intent intent = new Intent(this, OptionsScreen.class);
        intent.putExtra(DECISION, false);
        startActivity(intent);
    }

    public void homeClick (View view){
        Intent intent = new Intent(this, MainScreen.class);
        startActivity(intent);
    }


}
