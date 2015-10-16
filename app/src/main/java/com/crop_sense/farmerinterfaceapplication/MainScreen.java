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
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;


public class MainScreen extends AppCompatActivity {

    VideoView introVideo;
    Uri uri = Uri.parse("android.resource://com.crop_sense.farmerinterfaceapplication/"+R.raw.explanationvideo);

    FrameLayout frameVideo;

    SoundPool soundPool;
    int soundId;

    ImageView skip;
    ImageView fullscreen;
    int position =0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        skip = new ImageView(getApplicationContext());
        fullscreen = new ImageView(getApplicationContext());
        final Intent videointent = new Intent(this, searchvideo.class);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            createNewSoundPool();
        }else{
            createOldSoundPool();
        }

        soundId = soundPool.load(getApplicationContext(), R.raw.buttonclick, 1);
        final CustomMediaController mediaController = new CustomMediaController(this, true);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            frameVideo = (FrameLayout) findViewById(R.id.mediacontroller);
            introVideo = (VideoView) findViewById(R.id.introVideo);
            introVideo.setVideoURI(uri);
            introVideo.setMediaController(mediaController);
            introVideo.requestFocus();
            introVideo.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mediaController.setAnchorView(frameVideo);
                    mediaController.show(0);
                }
            });
        }else{
            introVideo = (VideoView) findViewById(R.id.introVideo);
            introVideo.setVideoURI(uri);
            introVideo.setMediaController(mediaController);
            introVideo.requestFocus();
            introVideo.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mediaController.show(0);
                }
            });
        }

        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!introVideo.isPlaying()) {
                    introVideo.start();
                }
                introVideo.seekTo(introVideo.getDuration());
            }
        });

        fullscreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                videointent.putExtra("videoname", "explanationvideo");
                videointent.putExtra("videoposition", introVideo.getCurrentPosition());
                startActivityForResult(videointent, 8008);
            }
        });

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        position=data.getIntExtra("videoposition",0);
        introVideo.seekTo(position);
        introVideo.start();
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
        soundPool = new SoundPool(soundId ,AudioManager.STREAM_MUSIC,0);
    }

    public void nextClick (View view){
        introVideo.stopPlayback();

//        buttonClick = new MediaPlayer();
//        try {
//            buttonClick.setDataSource(this,click );
//            buttonClick.prepare();
//            buttonClick.start();
//        }catch (Exception e){
//            //TODO
//        }
        soundPool.play(soundId,1,1,0,0,1);

        Intent intent = new Intent(this, SmallMessageBox.class);
        intent.putExtra("screenNumber", 1);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {

        soundPool.play(soundId,1,1,0,0,1);

        super.onBackPressed();
        this.finish();
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
