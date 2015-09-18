package com.crop_sense.farmerinterfaceapplication;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;


public class SampleSelection extends AppCompatActivity {

    private int countRed = 0;
    private int countGreen = 0;

    int [] rectangleViews = {R.id.rectangle1, R.id.rectangle2, R.id.rectangle3,
            R.id.rectangle4, R.id.rectangle5, R.id.rectangle6, R.id.rectangle7,
            R.id.rectangle8, R.id.rectangle9, R.id.rectangle10 };
    ImageView tmp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample_selection);

        tmp = (ImageView) findViewById(rectangleViews[countGreen+countRed]);
        tmp.setImageResource(R.drawable.flash);
        AnimationDrawable frameAnimation = (AnimationDrawable) tmp.getDrawable();
        frameAnimation.start();

    }

    public void yesPestClick (View view){

        tmp.setImageResource(R.drawable.flashrectangle2);

        countRed++;
        if(countRed + countGreen == 10){
            tallyBoxes();
        }else {

            tmp = (ImageView) findViewById(rectangleViews[countGreen + countRed]);
            tmp.setImageResource(R.drawable.flash);
            AnimationDrawable frameAnimation = (AnimationDrawable) tmp.getDrawable();
            frameAnimation.start();
        }



    }

    public void noPestClick (View view){

        tmp.setImageResource(R.drawable.flashrectangle1);
        countGreen++;
        if(countRed + countGreen == 10){
            tallyBoxes();
        }else {

            tmp = (ImageView) findViewById(rectangleViews[countGreen + countRed]);
            tmp.setImageResource(R.drawable.flash);
            AnimationDrawable frameAnimation = (AnimationDrawable) tmp.getDrawable();
            frameAnimation.start();
        }
    }

    public void tallyBoxes(){
        if(countRed >= 4){
            Intent intent = new Intent(this, NeedToSpray.class);
            Intent killIntent = new Intent(this, SampleSelection.class);
            killIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            this.startActivity(killIntent);
            finish();
            startActivity(intent);
        } else if(countRed < 4){
            Intent intent = new Intent(this, GoBackNextDay.class);
            Intent killIntent = new Intent(this, SampleSelection.class);
            killIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            this.startActivity(killIntent);
            finish();
            startActivity(intent);
        }
    }

    public void homeClick (View view){
        Intent intent = new Intent(this, MainScreen.class);
        Intent killIntent = new Intent(this, SampleSelection.class);
        killIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        this.startActivity(killIntent);
        finish();
        startActivity(intent);
    }


}
