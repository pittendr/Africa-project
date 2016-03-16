package com.crop_sense.farmerinterfaceapplication;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class SmallMessageBox extends AppCompatActivity {

    public final static String DECISION = "Decision";

    Uri screenVoice;

    Uri  voice1 = Uri.parse("android.resource://com.crop_sense.farmerinterfaceapplication/" + R.raw.voicememo1);
    Uri  voice2 = Uri.parse("android.resource://com.crop_sense.farmerinterfaceapplication/" + R.raw.voicememo2);
    Uri  voice3 = Uri.parse("android.resource://com.crop_sense.farmerinterfaceapplication/" + R.raw.voicememo3);
    Uri  voice6 = Uri.parse("android.resource://com.crop_sense.farmerinterfaceapplication/" + R.raw.voicememo6);
    Uri  alarm = Uri.parse("android.resource://com.crop_sense.farmerinterfaceapplication/" + R.raw.alarm);
    MediaPlayer voiceMemo;
    MediaPlayer alarmActivate;

    SoundPool soundPool;
    int soundId;

    int loop =1;

    TextView smallText;

    ListView listView;
    ListViewAdapter adapter;
    List<String> arrayList = new ArrayList<>();
    List<Bitmap> thumbList = new ArrayList<>();

    String[] explanationVideo;
    String[] scoutVideo;
    String[] sprayVideo;

    RelativeLayout screenBackground;

    LinearLayout background;
    LinearLayout background2;
    RelativeLayout menuBar;

    EditText searchInput;

    ImageView goButton;
    ImageView scoutButton;

    ImageButton flowerAudio;


    int screenNumber;

    boolean flag = false;

    ImageView yesButton;
    ImageView noButton;

    final AnimationDrawable drawable = new AnimationDrawable();
    final AnimationDrawable drawable2 = new AnimationDrawable();

    final Handler handler = new Handler();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        screenNumber = intent.getIntExtra("screenNumber", 1);
        setContentView(R.layout.activity_message_box_small);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            createNewSoundPool();
        }else{
            createOldSoundPool();
        }

        soundId = soundPool.load(getApplicationContext(), R.raw.buttonclick, 1);

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


        smallText = (TextView) findViewById(R.id.smallText);


        background = (LinearLayout) findViewById(R.id.buttonBackground);
        background2 = (LinearLayout) findViewById(R.id.buttonBackground2);
        screenBackground = (RelativeLayout) findViewById(R.id.smallmessageboxbackground);
        menuBar = (RelativeLayout) findViewById(R.id.menuBar);
        yesButton = (ImageView) findViewById(R.id.yesButton);
        noButton = (ImageView) findViewById(R.id.noButton);
        flowerAudio = (ImageButton) findViewById(R.id.flowerAudio);

        searchInput = (EditText) findViewById(R.id.searchInput);
        adapter = (new ListViewAdapter(getApplicationContext(),arrayList, thumbList ));

        goButton = (ImageView) findViewById(R.id.goButton);
        scoutButton = (ImageView) findViewById(R.id.scoutButton);
        listView = (ListView) findViewById(R.id.listView);

        listView.setAdapter(adapter);

        setMessageText(screenNumber);
        setButtons(screenNumber);
        Typeface type = Typeface.createFromAsset(getAssets(), "myriadproregular.otf");
        smallText.setTypeface(type);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                soundPool.play(soundId, 1, 1, 0, 0, 1);
                searchInput.setText("");
                String videoname = listView.getItemAtPosition(position).toString();

                InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(
                        Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(searchInput.getWindowToken(), 0);
                if (!videoname.equals("Test")) {
                    videointent.putExtra("videoname", videoname);
                    if (alarmActivate != null) {
                        alarmActivate.setLooping(false);
                        alarmActivate.reset();
                        alarmActivate.release();
                        alarmActivate = null;
                    }
                    startActivity(videointent);
                } else {
                    Toast.makeText(getApplicationContext(), "Test video", Toast.LENGTH_SHORT).show();
                }

            }
        });

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

    public void togglePlantingAudio (View view){
        if(voiceMemo!=null && voiceMemo.isPlaying()) {
            voiceMemo.reset();
            voiceMemo.release();
            voiceMemo=null;
        }else {
            try{
                voiceMemo = new MediaPlayer();
                voiceMemo.setDataSource(this, screenVoice);
                voiceMemo.prepare();
                voiceMemo.start();
            }catch (Exception e){
                //TODO
            }
        }
    }

    @Override
    protected void onPause(){
        if (voiceMemo != null) {
            voiceMemo.reset();
            voiceMemo.release();
            voiceMemo = null;
        }
        if (alarmActivate != null) {
            alarmActivate.reset();
            alarmActivate.release();
            alarmActivate = null;
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
        setScreenVoice(screenNumber);
        loop=1;
        super.onResume();
    }

    public void yesClick (View view){

        soundPool.play(soundId, 1, 1, 0, 0, 0);
        InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(searchInput.getWindowToken(), 0);
        nextScreen(screenNumber, "yes");
    }

    public void noClick (View view){

        InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(searchInput.getWindowToken(), 0);
        soundPool.play(soundId, 1, 1, 0, 0, 0);
        nextScreen(screenNumber, "no");
    }

    public void scoutClick(View view){

        InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(searchInput.getWindowToken(), 0);
        soundPool.play(soundId, 1, 1, 0, 0, 0);
        Intent intent = new Intent(this, ScoutVideo.class);
        startActivity(intent);

    }
    public void goClick(View view){

        InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(searchInput.getWindowToken(), 0);
        soundPool.play(soundId,1,1,0,0,0);
        if(alarmActivate!=null){
            alarmActivate.setLooping(false);
            alarmActivate.reset();
            alarmActivate.release();
            alarmActivate=null;
        }
        Intent intent = new Intent(this, SprayVideo.class);
        startActivity(intent);

    }



    public void homeClick (View view){

        InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(searchInput.getWindowToken(), 0);
        soundPool.play(soundId,1,1,0,0,0);
        Intent intent = new Intent(this, MainScreen.class);
        startActivity(intent);
    }

    public void searchClick (View view){
        soundPool.play(soundId, 1, 1, 0, 0, 0);
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
            listView.setVisibility(View.INVISIBLE);
            searchInput.setText("");
            flag = false;
            InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(
                    Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(searchInput.getWindowToken(), 0);
        }
    }

    private void setMessageText(int previousScreen){
        if (previousScreen==1){
            smallText.setText(getApplicationContext().getResources().getString(R.string.askIfPlanting));
        }
        else if (previousScreen==2){
            smallText.setText(getApplicationContext().getResources().getString(R.string.askIfFlowers));
        }
        else if (previousScreen==3){
            smallText.setText(getApplicationContext().getResources().getString(R.string.mustScout));
        }
        else if (previousScreen==4){
            smallText.setText(getApplicationContext().getResources().getString(R.string.needToSpray));
        }

    }
    private void nextScreen(int previousScreen, String decision){

        InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(searchInput.getWindowToken(), 0);
        if (previousScreen==1){
            if (decision.equals("yes")) {
                Intent intent = new Intent(this, OptionsScreen.class);
                intent.putExtra(DECISION,true);
                startActivity(intent);
            }else if (decision.equals("no")) {
                Intent intent = new Intent(this, OptionsScreen.class);
                intent.putExtra(DECISION, false);
                startActivity(intent);
            }
        }
        else if (previousScreen==2){
            if (decision.equals("yes")) {
                Intent intent = new Intent(this, SmallMessageBox.class);
                intent.putExtra("screenNumber", 3);
                startActivity(intent);
            }else if (decision.equals("no")) {
                Intent intent = new Intent(this, LargeMessageBox.class);
                startActivity(intent);
            }
        }
    }

    private void setButtons(int previousScreen){

        if (previousScreen==1){
            yesButton.setVisibility(View.VISIBLE);
            noButton.setVisibility(View.VISIBLE);
            background.setVisibility(View.INVISIBLE);
            background2.setVisibility(View.INVISIBLE);
            flowerAudio.setImageResource(R.drawable.audio);
        }
        else if (previousScreen==2){
            yesButton.setVisibility(View.VISIBLE);
            noButton.setVisibility(View.VISIBLE);
            background.setVisibility(View.INVISIBLE);
            background2.setVisibility(View.INVISIBLE);
            flowerAudio.setImageResource(R.drawable.audio);
        }
        else if (previousScreen==3){
            yesButton.setVisibility(View.INVISIBLE);
            noButton.setVisibility(View.INVISIBLE);
            background.setBackgroundColor(Color.parseColor("#6D9681"));
            flowerAudio.setImageResource(R.drawable.audio);
            background.setVisibility(View.VISIBLE);
            background2.setVisibility(View.INVISIBLE);
        }
        else if (previousScreen==4){
            yesButton.setVisibility(View.INVISIBLE);
            noButton.setVisibility(View.INVISIBLE);
            background2.setBackgroundColor(Color.parseColor("#B80F10"));
            flowerAudio.setImageResource(R.drawable.redaudio);
            background2.setVisibility(View.VISIBLE);
            background.setVisibility(View.INVISIBLE);
            screenBackground.setBackgroundColor(Color.parseColor("#d35657"));
        }

    }

    private void setScreenVoice(int previousScreen){
        if (previousScreen==1){
            screenVoice = voice1;
            try {
                voiceMemo.setDataSource(this, screenVoice);
                voiceMemo.prepare();
            }catch (Exception e){
                //TODO
            }
            voiceMemo.start();
        }
        else if (previousScreen==2){
            screenVoice = voice2;
            try {
                voiceMemo.setDataSource(this, screenVoice);
                voiceMemo.prepare();
            }catch (Exception e){
                //TODO
            }
            voiceMemo.start();
        }
        else if (previousScreen==3){
            screenVoice = voice3;
            try {
                voiceMemo.setDataSource(this, screenVoice);
                voiceMemo.prepare();
            }catch (Exception e){
                //TODO
            }
            voiceMemo.start();

        }
        else if (previousScreen==4){
            screenVoice = voice6;
            alarmActivate = new MediaPlayer();
            try {
                voiceMemo.setDataSource(this, screenVoice);
                voiceMemo.prepare();
                alarmActivate.setDataSource(this, alarm);
                alarmActivate.prepare();
            } catch (Exception e) {
                //TODO
            }
            alarmActivate.setVolume(0.07f, 0.07f);
            alarmActivate.start();
            alarmActivate.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    if (loop != 0) {
                        alarmActivate.start();
                        loop--;
                    }
                }
            });
            voiceMemo.start();

            drawable.addFrame(new ColorDrawable(Color.parseColor("#d35657")), 300);
            drawable.addFrame(new ColorDrawable(Color.parseColor("#BFD584")), 300);
            drawable.setOneShot(false);
            drawable2.addFrame(new ColorDrawable(Color.parseColor("#BFD584")), 300);
            drawable2.addFrame(new ColorDrawable(Color.parseColor("#B80F10")), 300);
            drawable2.setOneShot(false);

            screenBackground.setBackgroundDrawable(drawable);
            background2.setBackgroundDrawable(drawable2);
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    drawable.start();
                    drawable2.start();
                }
            }, 100);

        }

    }
    @Override
    protected void onDestroy(){
        soundPool.release();
        soundPool=null;
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {

        soundPool.play(soundId,1,1,0,0,0);
        InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(searchInput.getWindowToken(), 0);
        if(!flag){
            super.onBackPressed();

            if(alarmActivate!=null){
                alarmActivate.reset();
                alarmActivate.release();
                alarmActivate=null;
            }

            this.finish();


        }else{
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
        soundPool = new SoundPool(1 , AudioManager.STREAM_MUSIC,0);
    }

}

