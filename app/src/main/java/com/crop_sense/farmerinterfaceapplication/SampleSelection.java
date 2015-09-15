package com.crop_sense.farmerinterfaceapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;


public class SampleSelection extends AppCompatActivity {

    private int countRed = 0;
    private int countGreen = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample_selection);

        //TODO flash boxes

    }

    public void pestSighted (View view){

        //TODO change box to red
        countRed++;
        if(countRed + countGreen == 10){
            tallyBoxes();
        }

    }

    public void pestNotSighted (View view){

        //TODO change box to green
        countGreen++;
        if(countRed + countGreen == 10){
            tallyBoxes();
        }

    }

    public void tallyBoxes(){
        if(countRed >= 4){
            Intent intent = new Intent(this, NeedToSpray.class);
            startActivity(intent);
        } else if(countRed < 4){
            Intent intent = new Intent(this, GoBackNextDay.class);
            startActivity(intent);
        }
    }

    public void homeClick (View view){
        Intent intent = new Intent(this, MainScreen.class);
        startActivity(intent);
    }


}
