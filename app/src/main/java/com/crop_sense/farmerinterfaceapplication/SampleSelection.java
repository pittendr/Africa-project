package com.crop_sense.farmerinterfaceapplication;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class SampleSelection extends AppCompatActivity {

    ListView listView;
    ListViewAdapter adapter;
    List<String> arrayList = new ArrayList<>();

    String[] explanationVideo;
    String[] scoutVideo;
    String[] sprayVideo;

    RelativeLayout screenBackground;

    RelativeLayout menuBar;

    EditText searchInput;

    boolean flag = false;

    AnimationDrawable frameAnimation;

    ImageView undo;

    private int countRed = 0;
    private int countGreen = 0;

    int [] rectangleViews = {R.id.rectangle1, R.id.rectangle2, R.id.rectangle3,
            R.id.rectangle4, R.id.rectangle5, R.id.rectangle6, R.id.rectangle7,
            R.id.rectangle8, R.id.rectangle9, R.id.rectangle10 };

    boolean [] rectangleSelected = {false, false, false, false, false, false, false, false, false, false,};
    ImageView tmp;

    SoundPool soundPool;
    int soundId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample_selection);

        tmp = (ImageView) findViewById(rectangleViews[countGreen+countRed]);
        tmp.setImageResource(R.drawable.flash);
        AnimationDrawable frameAnimation = (AnimationDrawable) tmp.getDrawable();
        frameAnimation.start();

        final Intent videointent = new Intent(this, searchvideo.class);

        arrayList = new ArrayList<>();

        explanationVideo = (getResources().getResourceName(R.raw.explanationvideo)).split("raw/");
        scoutVideo = (getResources().getResourceName(R.raw.scoutvideo)).split("raw/");
        sprayVideo = (getResources().getResourceName(R.raw.sprayvideo)).split("raw/");

        arrayList.add(explanationVideo[1]);
        arrayList.add(scoutVideo[1]);
        arrayList.add(sprayVideo[1]);
        arrayList.add("Test");
        arrayList.add("Test");
        arrayList.add("Test");
        arrayList.add("Test");
        arrayList.add("Test");
        arrayList.add("Test");

        screenBackground = (RelativeLayout) findViewById(R.id.smallmessageboxbackground);
        menuBar = (RelativeLayout) findViewById(R.id.menuBar);

        searchInput = (EditText) findViewById(R.id.searchInput);
        adapter = (new ListViewAdapter(getApplicationContext(),arrayList ));
        undo = (ImageView) findViewById(R.id.undo);

        listView = (ListView) findViewById(R.id.listView);

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                soundPool.play(soundId,1,1,0,0,1);
                searchInput.setText("");
                String videoname = listView.getItemAtPosition(position).toString();
                InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(
                        Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(searchInput.getWindowToken(), 0);
                videointent.putExtra("videoname", videoname);
                if (!videoname.equals("Test")) {
                    videointent.putExtra("videoname", videoname);
                    startActivity(videointent);
                } else {
                    Toast.makeText(getApplicationContext(), "Test video", Toast.LENGTH_SHORT).show();
                }

            }
        });

        searchInput.addTextChangedListener(
                new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        String text = searchInput.getText().toString().toLowerCase(Locale.getDefault());
                        adapter.filter(text);
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                }
        );

    }

    public void yesPestClick (View view){
        rectangleSelected[countGreen+countRed]=true;

        soundPool.play(soundId, 1, 1, 0, 0, 1);

        tmp.setImageResource(R.drawable.flashrectangle2);
        tmp.setSelected(true);
        countRed++;
        if(countRed + countGreen == 10){
            tallyBoxes();
        }else {

            tmp = (ImageView) findViewById(rectangleViews[countGreen + countRed]);
            tmp.setImageResource(R.drawable.flash);
            frameAnimation = (AnimationDrawable) tmp.getDrawable();
            frameAnimation.start();
        }



    }

    public void noPestClick (View view){
        rectangleSelected[countGreen+countRed]=false;

        soundPool.play(soundId, 1, 1, 0, 0, 1);

        tmp.setImageResource(R.drawable.flashrectangle1);
        countGreen++;

        if(countRed + countGreen == 10){
            tallyBoxes();
        }else {

            tmp = (ImageView) findViewById(rectangleViews[countGreen + countRed]);
            tmp.setImageResource(R.drawable.flash);
            frameAnimation = (AnimationDrawable) tmp.getDrawable();
            frameAnimation.start();
        }
    }

    public void tallyBoxes(){
        if(countRed >= 4){
            InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(
                    Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(searchInput.getWindowToken(), 0);
            Intent intent = new Intent(this, SmallMessageBox.class);
            intent.putExtra("screenNumber", 4);
            Intent killIntent = new Intent(this, SampleSelection.class);
            killIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            this.startActivity(killIntent);
            finish();
            startActivity(intent);
        } else if(countRed < 4){
            InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(
                    Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(searchInput.getWindowToken(), 0);
            Intent intent = new Intent(this, LargeMessageBox.class);
            Intent killIntent = new Intent(this, SampleSelection.class);
            killIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            this.startActivity(killIntent);
            finish();
            startActivity(intent);
        }
    }

    public void undoClick(View view){
        soundPool.play(soundId,1,1,0,0,1);

        if(countGreen+countRed>0) {
            if (rectangleSelected[countGreen + countRed] == true) {
                countRed--;
            } else {
                countGreen--;
            }


            frameAnimation.stop();
            tmp.setImageResource(R.drawable.flashrectangle1);
            tmp = (ImageView) findViewById(rectangleViews[countGreen + countRed]);
            tmp.setImageResource(R.drawable.flash);
            frameAnimation = (AnimationDrawable) tmp.getDrawable();
            frameAnimation.start();
        }
    }

    public void homeClick (View view){
        soundPool.play(soundId,1,1,0,0,1);

        InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(searchInput.getWindowToken(), 0);
        Intent intent = new Intent(this, MainScreen.class);
        Intent killIntent = new Intent(this, SampleSelection.class);
        killIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        this.startActivity(killIntent);
        finish();
        startActivity(intent);
    }

    public void searchClick (View view){
        soundPool.play(soundId, 1, 1, 0, 0, 1);
        if(!flag) {
            undo.setVisibility(View.INVISIBLE);
            searchInput.setVisibility(View.VISIBLE);
            listView.setVisibility(View.VISIBLE);
            flag = true;
            searchInput.setEnabled(true);
            searchInput.setFocusable(true);
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
        }else{
            searchInput.setVisibility(View.INVISIBLE);
            listView.setVisibility(View.INVISIBLE);
            undo.setVisibility(View.VISIBLE);
            searchInput.setText("");
            flag = false;
            InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(
                    Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(searchInput.getWindowToken(), 0);
        }
    }

    @Override
    public void onBackPressed() {
        soundPool.play(soundId, 1, 1, 0, 0, 1);
        InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(searchInput.getWindowToken(), 0);
        if(!flag){
            super.onBackPressed();
            this.finish();

        }else{
            undo.setVisibility(View.VISIBLE);
            searchInput.setVisibility(View.INVISIBLE);
            listView.setVisibility(View.INVISIBLE);
            flag=false;
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    protected void createNewSoundPool(){
        AudioAttributes attributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();
        soundPool = new SoundPool.Builder()
                .setAudioAttributes(attributes)
                .build();
    }
    @SuppressWarnings("deprecation")
    protected void createOldSoundPool(){
        soundPool = new SoundPool(soundId , AudioManager.STREAM_MUSIC,0);
    }

    @Override
    protected void onPause(){
        searchInput.setVisibility(View.INVISIBLE);
        listView.setVisibility(View.INVISIBLE);
        searchInput.setText("");
        flag = false;
        InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(searchInput.getWindowToken(), 0);
        super.onPause();
    }
    @Override
    protected void onResume(){
        undo.setVisibility(View.VISIBLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            createNewSoundPool();
        }else{
            createOldSoundPool();
        }

        soundId = soundPool.load(getApplicationContext(), R.raw.buttonclick, 1);
        super.onResume();
    }
    @Override
    protected void onDestroy(){
        soundPool.release();
        soundPool=null;
        super.onDestroy();
    }

}
