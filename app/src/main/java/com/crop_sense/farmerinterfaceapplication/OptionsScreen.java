package com.crop_sense.farmerinterfaceapplication;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class OptionsScreen extends AppCompatActivity {

    ImageView optionsScout;

    Boolean decision;
     Boolean flag=false;

    EditText searchInput;

    ListView listView;
    ListViewAdapter adapter;
    List<String> arrayList = new ArrayList<>();

    String[] explanationVideo;
    String[] scoutVideo;
    String[] sprayVideo;

    SoundPool soundPool;
    int soundId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();

         decision = intent.getBooleanExtra(SmallMessageBox.DECISION, true);

        setContentView(R.layout.activity_options_screen);

        final Intent videointent = new Intent(this, searchvideo.class);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            createNewSoundPool();
        }else{
            createOldSoundPool();
        }

        soundId = soundPool.load(getApplicationContext(), R.raw.buttonclick, 1);

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

        searchInput = (EditText) findViewById(R.id.searchInput);
        adapter = (new ListViewAdapter(getApplicationContext(),arrayList ));

        listView = (ListView) findViewById(R.id.listView);

        listView.setAdapter(adapter);
        optionsScout = (ImageView) findViewById(R.id.optionsscout);

        if (!decision){
            optionsScout.setAlpha(0.4f);
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                soundPool.play(soundId,1,1,0,0,1);
                searchInput.setText("");
                String videoname = listView.getItemAtPosition(position).toString();
                InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(
                        Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(searchInput.getWindowToken(), 0);
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

    public void loadDisplayVideos(View view){
        soundPool.play(soundId,1,1,0,0,1);
        Intent intent = new Intent(this, DisplayVideos.class);
        startActivity(intent);
    }

    public void loadAskIfFlower(View view){
        soundPool.play(soundId, 1, 1, 0, 0, 1);
        if(decision) {
            Intent intent = new Intent(this, SmallMessageBox.class);
            intent.putExtra("screenNumber", 2);
            startActivity(intent);
        }else{
                Toast.makeText(getApplicationContext(), "You have not started planting yet.", Toast.LENGTH_SHORT).show();
        }
    }

    public void homeClick (View view){
        soundPool.play(soundId,1,1,0,0,1);

        InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(searchInput.getWindowToken(), 0);

        Intent intent = new Intent(this, MainScreen.class);
        startActivity(intent);
    }

    public void searchClick (View view){

        soundPool.play(soundId, 1, 1, 0, 0, 1);
        if(!flag) {
            searchInput.setVisibility(View.VISIBLE);
            listView.setVisibility(View.VISIBLE);
            flag = true;
            searchInput.setEnabled(true);
            searchInput.setFocusable(true);
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
        }else{
            searchInput.setVisibility(View.INVISIBLE);
            listView.setVisibility(View.INVISIBLE);
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
            searchInput.setVisibility(View.INVISIBLE);
            listView.setVisibility(View.INVISIBLE);
            flag=false;
        }
    }


    public void messageClick (View view){
        Toast.makeText(getApplicationContext(), "Feature not yet active", Toast.LENGTH_SHORT).show();
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
    protected void onDestroy(){
        soundPool.release();
        soundPool=null;
        super.onDestroy();
    }


}
