package com.crop_sense.farmerinterfaceapplication;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class SprayVideo extends AppCompatActivity {

    VideoView spray;
    Uri sprayUri = Uri.parse("android.resource://com.crop_sense.farmerinterfaceapplication/"+R.raw.sprayvideo);
    Uri  voice = Uri.parse("android.resource://com.crop_sense.farmerinterfaceapplication/" + R.raw.voicememo5);
    MediaPlayer voiceMemo;

    ImageButton yesButton;
    ImageButton noButton;
    TextView textView;
    ImageView homeButton;

    ListView listView;
    ListViewAdapter adapter;
    List<String> arrayList = new ArrayList<>();

    String[] explanationVideo;
    String[] scoutVideo;
    String[] sprayVideo;

    boolean flag = false;

    RelativeLayout screenBackground;

    RelativeLayout menuBar;

    EditText searchInput;

    SoundPool soundPool;
    int soundId;
    FrameLayout frameVideo;

    ImageView skip;
    ImageView fullscreen;

    int position =0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        spray = (VideoView) findViewById(R.id.scoutVideo);
        yesButton = (ImageButton) findViewById(R.id.yesButton2);
        noButton = (ImageButton) findViewById(R.id.noButton2);
        textView = (TextView) findViewById(R.id.textView);
        homeButton = (ImageView) findViewById(R.id.home);

        skip = new ImageView(getApplicationContext());
        fullscreen = new ImageView(getApplicationContext());

        final CustomMediaController mediaController = new CustomMediaController(this, true);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {

            frameVideo = (FrameLayout) findViewById(R.id.mediacontroller);
            spray.setVideoURI(sprayUri);
            spray.setMediaController(mediaController);
            spray.requestFocus();
            spray.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mediaController.setAnchorView(frameVideo);
                    mediaController.show();
                    spray.start();
                }
            });
        }else{
            spray.setVideoURI(sprayUri);
            spray.setMediaController(mediaController);
            spray.requestFocus();
            spray.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mediaController.show();
                    spray.start();
                }
            });
        }

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

        listView = (ListView) findViewById(R.id.listView);

        listView.setAdapter(adapter);

        spray.setOnCompletionListener(
                new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        voiceMemo = new MediaPlayer();
                        try {
                            voiceMemo.setDataSource(getApplicationContext(), voice);
                            voiceMemo.prepare();
                        } catch (Exception e) {
                            //TODO
                        }
                        voiceMemo.start();
                        yesButton.setVisibility(View.VISIBLE);
                        noButton.setVisibility(View.VISIBLE);
                        textView.setVisibility(View.VISIBLE);
                        spray.setVisibility(View.INVISIBLE);
                        spray.stopPlayback();

                    }
                }
        );

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                soundPool.play(soundId, 1, 1, 0, 0, 1);
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
                    videointent.putExtra("videoposition", 0);
                    Intent killIntent = new Intent(getApplicationContext(), SprayVideo.class);
                    killIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(killIntent);
                    finish();
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

        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!spray.isPlaying()) {
                    spray.start();
                }
                spray.seekTo(spray.getDuration());
            }
        });

        fullscreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                videointent.putExtra("videoname", "sprayvideo");
                videointent.putExtra("videoposition", spray.getCurrentPosition());
                startActivityForResult(videointent, 8008);
            }
        });

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        position=data.getIntExtra("videoposition",0);
        spray.seekTo(position);
        spray.start();
    }

    public void yesVideoClick (View view){
        if(voiceMemo!=null) {
            voiceMemo.reset();
            voiceMemo.release();
            voiceMemo = null;
        }
        InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(searchInput.getWindowToken(), 0);

        soundPool.play(soundId,1,1,0,0,1);
        yesButton.setVisibility(View.INVISIBLE);
        noButton.setVisibility(View.INVISIBLE);
        textView.setVisibility(View.INVISIBLE);
        spray.setVisibility(View.VISIBLE);
        spray.requestFocus();
        spray.seekTo(0);
        spray.start();
    }

    public void noVideoClick (View view){
        if(voiceMemo!=null) {
            voiceMemo.reset();
            voiceMemo.release();
            voiceMemo = null;
        }
        InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(searchInput.getWindowToken(), 0);

        soundPool.play(soundId, 1, 1, 0, 0, 1);
        Intent intent= new Intent(this, FinalScreen.class);
        Intent killIntent = new Intent(this, SprayVideo.class);
        killIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        this.startActivity(killIntent);
        finish();
        startActivity(intent);
    }

    public void homeClick (View view){
        if(voiceMemo!=null) {
            voiceMemo.reset();
            voiceMemo.release();
            voiceMemo = null;
        }

        InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(searchInput.getWindowToken(), 0);


        soundPool.play(soundId, 1, 1, 0, 0, 1);
        spray.stopPlayback();
        Intent intent = new Intent(this, MainScreen.class);
        Intent killIntent = new Intent(this, SprayVideo.class);
        killIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        this.startActivity(killIntent);
        finish();
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

    public class CustomMediaController extends MediaController {


        public CustomMediaController(Context context, Boolean buttons) {
            super(context, buttons);
        }

        @Override
        public void setAnchorView(View view) {
            super.setAnchorView(view);

            skip.setImageResource(R.drawable.skip);
            DisplayMetrics displaymetrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
            int height = displaymetrics.heightPixels/18;

            int orgWidth = skip.getDrawable().getIntrinsicWidth();
            int orgHeight = skip.getDrawable().getIntrinsicHeight();

            double newWidth = Math.floor((orgWidth * height) / orgHeight);



            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams((int)newWidth, height);
            params.setMargins((int) (newWidth/3.9),(int) (newWidth/2.17),(int) (newWidth/3.9),(int) (newWidth/3.9));
            params.gravity = Gravity.END;
            skip.setScaleType(ImageView.ScaleType.FIT_XY);
            addView(skip, params);

            fullscreen.setImageResource(R.drawable.fullscreen);

            int orgWidth2 = fullscreen.getDrawable().getIntrinsicWidth();
            int orgHeight2 = fullscreen.getDrawable().getIntrinsicHeight();

            double newWidth2 = Math.floor((orgWidth2 * height) / orgHeight2);


            FrameLayout.LayoutParams params2 = new FrameLayout.LayoutParams((int)newWidth2, height);
            params2.setMargins((int) (newWidth/3.9),(int) (newWidth/2.17),(int) (newWidth/3.9),(int) (newWidth/3.9));
            params2.gravity = Gravity.START;
            fullscreen.setScaleType(ImageView.ScaleType.FIT_XY);
            addView(fullscreen, params2);
        }
    }

}
