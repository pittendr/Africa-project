package com.crop_sense.farmerinterfaceapplication;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaMetadataRetriever;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class DisplayVideos extends AppCompatActivity {

    ListView listView;
    VideoListViewAdapter adapter;
    List<String> arrayList = new ArrayList<>();
    List<Bitmap> thumbList = new ArrayList<>();

    String[] explanationVideo;
    String[] scoutVideo;
    String[] sprayVideo;
    EditText searchInput;

    Boolean flag = false;
    SoundPool soundPool;
    int soundId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_videos);

        final Intent videointent = new Intent(this, searchvideo.class);

        searchInput = (EditText) findViewById(R.id.searchInput);

        arrayList = new ArrayList<>();

        explanationVideo = (getResources().getResourceName(R.raw.explanationvideo)).split("raw/");
        scoutVideo = (getResources().getResourceName(R.raw.scoutvideo)).split("raw/");
        sprayVideo = (getResources().getResourceName(R.raw.sprayvideo)).split("raw/");

        Uri eURI = Uri.parse("android.resource://" + getPackageName() + "/"
                + R.raw.explanationvideo);
        MediaMetadataRetriever eretriever = new MediaMetadataRetriever();
        eretriever.setDataSource(this, eURI);
        Bitmap ethumb = eretriever
                .getFrameAtTime(3486816, MediaMetadataRetriever.OPTION_CLOSEST_SYNC);
        Uri scURI = Uri.parse("android.resource://" + getPackageName() + "/"
                + R.raw.scoutvideo);
        MediaMetadataRetriever scretriever = new MediaMetadataRetriever();
        scretriever.setDataSource(this, scURI);
        Bitmap scthumb = scretriever
                .getFrameAtTime(20203516, MediaMetadataRetriever.OPTION_CLOSEST_SYNC);
        Uri spURI = Uri.parse("android.resource://" + getPackageName() + "/"
                + R.raw.sprayvideo);
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(this, spURI);
        Bitmap spthumb = retriever
                .getFrameAtTime(54000000, MediaMetadataRetriever.OPTION_CLOSEST_SYNC);

        arrayList.add(explanationVideo[1]);
        arrayList.add(scoutVideo[1]);
        arrayList.add(sprayVideo[1]);
        thumbList.add(ethumb);
        thumbList.add(scthumb);
        thumbList.add(spthumb);

        adapter = (new VideoListViewAdapter(getApplicationContext(),arrayList, thumbList ));

        listView = (ListView) findViewById(R.id.videolistView);

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

    @Override
    protected void onResume(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            createNewSoundPool();
        }else{
            createOldSoundPool();
        }

        soundId = soundPool.load(getApplicationContext(), R.raw.buttonclick, 1);
        super.onResume();
    }

    public void homeClick (View view){
        soundPool.play(soundId,1,1,0,0,1);

        InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(
                Context                                                                                                                                                                                                                                             .INPUT_METHOD_SERVICE);
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
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
        }else{
            searchInput.setVisibility(View.INVISIBLE);
            searchInput.setText("");
            flag = false;
            InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(
                    Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(searchInput.getWindowToken(), 0);
        }
    }

    @Override
    public void onBackPressed() {
        soundPool.play(soundId,1,1,0,0,1);
        InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(searchInput.getWindowToken(), 0);
        super.onBackPressed();
        this.finish();
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
    protected void onDestroy(){
        soundPool.release();
        soundPool=null;
        super.onDestroy();
    }

    @Override
    protected void onPause(){
        searchInput.setVisibility(View.INVISIBLE);
        searchInput.setText("");
        flag = false;
        InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(searchInput.getWindowToken(), 0);
        super.onPause();
    }


}
