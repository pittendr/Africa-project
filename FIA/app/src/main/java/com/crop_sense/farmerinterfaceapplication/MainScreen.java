package com.crop_sense.farmerinterfaceapplication;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;


public class MainScreen extends AppCompatActivity {

    VideoView introVideo;
    Uri uri = Uri.parse("android.resource://com.crop_sense.farmerinterfaceapplication/"+R.raw.explanationvideo);


    FrameLayout frameVideo;

    SoundPool soundPool;
    int soundId;

    ImageView skip;
    ImageView fullscreen;
    int position =0;

    private String ipString = "";

    ImageButton ip;

    SharedPreferences settings;
    SharedPreferences.Editor edit;

    LocationManager locationManager = null;

    File file;
    String [] arrData = null;
    ArrayList<String[]> arrList = new ArrayList<String[]>();
    int count =0;

    JSONObject postMessage = new JSONObject();

    boolean done;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        done = false;

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        ip = (ImageButton) findViewById(R.id.ipButton);
        skip = new ImageView(getApplicationContext());
        fullscreen = new ImageView(getApplicationContext());
        final Intent videointent = new Intent(this, searchvideo.class);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            createNewSoundPool();
        }else{
            createOldSoundPool();
        }

        settings = getSharedPreferences("ip", MODE_PRIVATE);
        ipString = settings.getString("ipAddress", ipString);


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

        ip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainScreen.this);
                builder.setTitle("Please Enter the API address:");
                final EditText input = new EditText(MainScreen.this);
                input.setText(ipString);
                input.setInputType(InputType.TYPE_CLASS_PHONE);
                builder.setView(input);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ipString = input.getText().toString();
                        edit = settings.edit();
                        edit.putString("ipAddress", ipString);
                        edit.apply();
                        InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(
                                Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(input.getWindowToken(), 0);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(
                                Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(input.getWindowToken(), 0);
                    }
                });

                builder.show();
            }
        });


        if ( !locationManager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {

            final android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
            builder.setMessage("GPS must be enabled. Click OK to enable it.")
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                            startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        }
                    }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
            final android.support.v7.app.AlertDialog alert = builder.create();
            alert.show();

        }else{
            //TODO
        }

        if (!ipString.equals("")){
            sendData();
        }



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
        soundPool.play(soundId,1,1,0,0,1);

        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            startService(new Intent(this, LocationService.class));
        }
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

    private void getSQLdata(){

        dbHelper mDbHelper = new dbHelper(getApplicationContext());

        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        Cursor cursor = db.query("test2", new String[] { "*" },
                null,
                null, null, null, null, null);

        if(cursor != null)
        {
            if (cursor.moveToFirst()) {
                do {
                    arrData = new String[cursor.getColumnCount()];
                    for (int i = 0; i < cursor.getColumnCount(); i++) {
                        arrData[i] = cursor.getString(i);
                    }
                    arrList.add(arrData);
                }while(cursor.moveToNext());
            }
        }

        cursor.close();
        db.close();
    }

    private class postRequest extends AsyncTask<String, Void, String> {

        String url ="http://"+ipString+":8080/data";

        HttpClient httpclient = new DefaultHttpClient();

        HttpPost httppost = new HttpPost(url);
        HttpResponse response;
        int code;

        @Override
        protected String doInBackground(String... params){


            try {
                httppost.setEntity((new ByteArrayEntity(postMessage.toString().getBytes("UTF8"))));
                httppost.setHeader("Content-type", "application/json");
                response = httpclient.execute(httppost);
                //code = EntityUtils.toString(response.getEntity());
                code=response.getStatusLine().getStatusCode();

            } catch (Exception e) {
                //TODO
            }
            return "nothing";

        }

        @Override
        protected void onPostExecute(String Result){

            if(code==201) {
                dbHelper mDbHelper = new dbHelper(getApplicationContext());
                SQLiteDatabase db = mDbHelper.getWritableDatabase();
                db.delete("test2", "_id = ?", new String[]{arrList.get(count)[0]});
            }
            count++;
            writeJSON(count);

        }
        @Override
        protected void onProgressUpdate(Void... values){

        }

    }

    private void writeJSON(int cnt){
        /*try {
            if(cnt>=arrList.size()){
                done = true;
                return;
            }
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(("<?xml version='1.0' ?><data id=\"fiadata\"><gps>" + arrList.get(cnt)[5] + "</gps><aid>" + arrList.get(cnt)[3] + "</aid><mac>" + arrList.get(cnt)[1] + "</mac><serial>" + arrList.get(cnt)[2] + "</serial><time>" + arrList.get(cnt)[4] + "</time><pests>" + arrList.get(cnt)[6] + "</pests></data>").getBytes());
            fos.close();
            new postRequest().execute();
        } catch (Exception e) {
            //TODO
        }*/
        try{
            if(cnt>=arrList.size()){
                done = true;
                return;
            }
            postMessage.put("phone", arrList.get(cnt)[1]);
            postMessage.put("gps", arrList.get(cnt)[6]);
            postMessage.put("aid", arrList.get(cnt)[4]);
            postMessage.put("mac", arrList.get(cnt)[2]);
            postMessage.put("serial", arrList.get(cnt)[3]);
            postMessage.put("time", arrList.get(cnt)[5]);
            postMessage.put("pests", arrList.get(cnt)[7]);
            new postRequest().execute();
        }catch(Exception e){
            //TODO
        }
    }

    private void sendData(){

        getSQLdata();

        /*file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "fiadata.xml");
        if (!file.exists()){
            try {
                file.createNewFile();
            }catch (Exception e){
                //TODO
            }
        }*/

        writeJSON(count);

    }

    @Override
    protected void onStart(){
        if(android.os.Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
        }
        super.onStart();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }else if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }else if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }else if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_PHONE_STATE}, 1);
        }else if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_SMS}, 1);
        }
    }



}
