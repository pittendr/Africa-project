package com.crop_sense.farmerinterfaceapplication;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class LargeMessageBox extends AppCompatActivity {

    Uri voice = Uri.parse("android.resource://com.crop_sense.farmerinterfaceapplication/" + R.raw.voicememo4);
    MediaPlayer voiceMemo;

    ListView listView;
    ListViewAdapter adapter;
    List<String> arrayList = new ArrayList<>();
    List<Bitmap> thumbList = new ArrayList<>();

    String[] explanationVideo;
    String[] scoutVideo;
    String[] sprayVideo;

    RelativeLayout screenBackground;

    RelativeLayout menuBar;

    EditText searchInput;

    boolean flag = false;

    SoundPool soundPool;
    int soundId;

    TextView largeText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_box_large);

        final Intent videointent = new Intent(this, searchvideo.class);

        voiceMemo=new MediaPlayer();

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

        screenBackground = (RelativeLayout) findViewById(R.id.smallmessageboxbackground);
        menuBar = (RelativeLayout) findViewById(R.id.menuBar);

        searchInput = (EditText) findViewById(R.id.searchInput);
        adapter = (new ListViewAdapter(getApplicationContext(),arrayList, thumbList ));

        listView = (ListView) findViewById(R.id.listView);

        listView.setAdapter(adapter);

        largeText = (TextView) findViewById(R.id.largeText);

        Typeface type = Typeface.createFromAsset(getAssets(), "myriadproregular.otf");
        largeText.setTypeface(type);

        voiceMemo.setOnCompletionListener(
                new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        voiceMemo.reset();
                        voiceMemo.release();
                        voiceMemo = null;
                    }
                }
        );

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                soundPool.play(soundId,1,1,0,0,1);
                if (voiceMemo != null) {
                    voiceMemo.reset();
                    voiceMemo.release();
                    voiceMemo = null;
                }
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

    public void searchClick (View view){
        soundPool.play(soundId,1,1,0,0,1);
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

    public void toggleGoBackAudio (View view){
        if(voiceMemo != null && voiceMemo.isPlaying()) {
            voiceMemo.reset();
            voiceMemo.release();
            voiceMemo=null;
        }else {
            try{
                voiceMemo = new MediaPlayer();
                voiceMemo.setDataSource(this, voice);
                voiceMemo.prepare();
                voiceMemo.start();
            }catch (Exception e){
                //TODO
            }
        }
    }

    public void homeClick (View view){
        if(voiceMemo!=null) {
            voiceMemo.reset();
            voiceMemo.release();
            voiceMemo = null;
        }
        soundPool.play(soundId,1,1,0,0,1);

        InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(searchInput.getWindowToken(), 0);

        Intent intent = new Intent(this, MainScreen.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        if(voiceMemo!=null) {
            voiceMemo.reset();
            voiceMemo.release();
            voiceMemo = null;
        }
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

    @Override
    protected void onPause(){
        if (voiceMemo != null) {
            voiceMemo.reset();
            voiceMemo.release();
            voiceMemo = null;
        }
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
        if(voiceMemo==null) {
            voiceMemo = new MediaPlayer();
        }
        try {
            voiceMemo.setDataSource(this,voice );
            voiceMemo.prepare();
            voiceMemo.start();
        }catch (Exception e){
            //TODO
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            createNewSoundPool();
        }else{
            createOldSoundPool();
        }

        soundId = soundPool.load(getApplicationContext(), R.raw.buttonclick, 1);
        super.onResume();
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


}
