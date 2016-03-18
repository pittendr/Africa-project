package com.crop_sense.farmerinterfaceapplication;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaMetadataRetriever;
import android.media.SoundPool;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
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


import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class SampleSelection extends AppCompatActivity {


    SharedPreferences settings;
    String server = "";

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

    File file;

    boolean flag = false;

    AnimationDrawable frameAnimation;

    ImageView undo;

    private int countRed = 0;
    private int countGreen = 0;
    private int total = 0;

    JSONObject postMessage = new JSONObject();

    int [] rectangleViews = {R.id.rectangle1, R.id.rectangle2, R.id.rectangle3,
            R.id.rectangle4, R.id.rectangle5, R.id.rectangle6, R.id.rectangle7,
            R.id.rectangle8, R.id.rectangle9, R.id.rectangle10 };

    boolean [] rectangleSelected = {false, false, false, false, false, false, false, false, false, false,};
    ImageView tmp;

    SoundPool soundPool;
    int soundId;

    private String macAddress = null;
    private String androidid = null;
    private String serialNumber = null;
    long newRowId;

    String date;
    String pests;
    String phone;
    SharedPreferences savedLocation;
    JSONObject usableLocation = new JSONObject();

    SharedPreferences savedTime;
    long usableTime = 0;

    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample_selection);


         dialog = new ProgressDialog(SampleSelection.this);

        savedLocation = getSharedPreferences("location", MODE_PRIVATE);
        try {
            usableLocation = new JSONObject(savedLocation.getString("locationAddress", usableLocation.toString()));
        }catch(JSONException e){
            //TODO
        }
        savedTime = getSharedPreferences("time", MODE_PRIVATE);
        usableTime = savedTime.getLong("timeMS", usableTime);
        if ((System.currentTimeMillis() - usableTime)>1800000){
            usableLocation=null;
        }

        if (!checkService()){
            startService(new Intent(this, LocationService.class));
        }

        tmp = (ImageView) findViewById(rectangleViews[countGreen+countRed]);
        tmp.setImageResource(R.drawable.flash);
        AnimationDrawable frameAnimation = (AnimationDrawable) tmp.getDrawable();
        frameAnimation.start();

        final Intent videointent = new Intent(this, searchvideo.class);

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


        settings = getSharedPreferences("ip", MODE_PRIVATE);
        server = settings.getString("ipAddress", server);

        screenBackground = (RelativeLayout) findViewById(R.id.smallmessageboxbackground);
        menuBar = (RelativeLayout) findViewById(R.id.menuBar);

        searchInput = (EditText) findViewById(R.id.searchInput);
        adapter = (new ListViewAdapter(getApplicationContext(),arrayList, thumbList ));
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

        soundPool.play(soundId, 1, 1, 0, 0, 1);

        tmp.setImageResource(R.drawable.flashrectangle2);
        tmp.setSelected(true);
        countRed++;
        total=countGreen+countRed;
        if (total>=10){
            total=9;
        }

        rectangleSelected[total] = true;
        if(countRed + countGreen == 10){
            GetLocation();
        }else {

            tmp = (ImageView) findViewById(rectangleViews[total]);
            tmp.setImageResource(R.drawable.flash);
            frameAnimation = (AnimationDrawable) tmp.getDrawable();
            frameAnimation.start();
        }



    }

    public void noPestClick (View view){
        soundPool.play(soundId, 1, 1, 0, 0, 1);

        tmp.setImageResource(R.drawable.flashrectangle1);
        countGreen++;
        total=countGreen+countRed;
        if (total>=10){
            total=9;
        }

        rectangleSelected[total] = false;

        if(countRed + countGreen == 10){
            GetLocation();
        }else {

            tmp = (ImageView) findViewById(rectangleViews[total]);
            tmp.setImageResource(R.drawable.flash);
            frameAnimation = (AnimationDrawable) tmp.getDrawable();
            frameAnimation.start();
        }
    }

    public void tallyBoxes(){
        dialog.dismiss();
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
        soundPool.play(soundId, 1, 1, 0, 0, 1);

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
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
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

    protected void saveData(){

        date = DateFormat.getDateTimeInstance().format(new Date());
        pests = String.valueOf(countRed);
        ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mWifi = connManager.getActiveNetworkInfo();

        WifiManager wifi = (WifiManager)getSystemService(Context.WIFI_SERVICE);
        if (wifi.isWifiEnabled()){
            if (mWifi.isConnected()) {
                WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
                WifiInfo wInfo = wifiManager.getConnectionInfo();
                macAddress = wInfo.getMacAddress();
            }
        }

        TelephonyManager tMgr = (TelephonyManager)getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
        phone = tMgr.getLine1Number();

        androidid = Settings.Secure.getString(this.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        serialNumber = Build.SERIAL;


        /*file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "fiadata.xml");
        if (!file.exists()){
            try {
                file.createNewFile();
            }catch (Exception e){
                //TODO
            }
        }
        try {
            FileOutputStream fos = new  FileOutputStream(file);
            fos.write(("<?xml version='1.0' ?><data id=\"fiadata\"><gps>"+usableLocation+"</gps><aid>"+androidid+"</aid><mac>"+macAddress+"</mac><serial>"+serialNumber+"</serial><time>"+date+"</time><pests>"+pests+"</pests></data>").getBytes());
            fos.close();}catch (Exception e){
            //TODO
        }*/
        try{
            postMessage.put("phone", phone);
            postMessage.put("gps", usableLocation);
            postMessage.put("aid", androidid);
            postMessage.put("mac", macAddress);
            postMessage.put("serial", serialNumber);
            postMessage.put("time", date);
            postMessage.put("pests", pests);
            new postRequest().execute();
        }catch(Exception e){
            //TODO
        }


    }


    protected void GetLocation(){
        dialog.setMessage("Saving Data");
        dialog.setCancelable(false);
        dialog.show();

        stopService(new Intent(this, LocationService.class));
        saveData();
    }

    private class saveToDb extends AsyncTask<String, Void, String>{
        @Override
        protected String doInBackground(String... params){

            dbHelper mDbHelper = new dbHelper(getApplicationContext());

            SQLiteDatabase db = mDbHelper.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(dbContract.FeedEntry.COLUMN_NAME_PHONE, phone);
            values.put(dbContract.FeedEntry.COLUMN_NAME_MAC, macAddress);
            values.put(dbContract.FeedEntry.COLUMN_NAME_SERIAL, serialNumber);
            values.put(dbContract.FeedEntry.COLUMN_NAME_AID, androidid);
            values.put(dbContract.FeedEntry.COLUMN_NAME_TIME, date);
            values.put(dbContract.FeedEntry.COLUMN_NAME_PESTS, pests);
            values.put(dbContract.FeedEntry.COLUMN_NAME_GPS, usableLocation.toString());

            newRowId = db.insert(
                    dbContract.FeedEntry.TABLE_NAME,
                    null,
                    values);

            return "done";

        }

        @Override
        protected void onPostExecute(String Result){

        }
        @Override
        protected void onPreExecute(){
        }
        @Override
        protected void onProgressUpdate(Void... values){

        }

    }

    private class postRequest extends AsyncTask<String, Void, String>{

        String url ="http://"+server+":8080/data";

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
                code=response.getStatusLine().getStatusCode();

            } catch (Exception e) {
                //TODO
            }
            return "nothing";

        }

        @Override
        protected void onPostExecute(String Result){
            if(code!=201){
                new saveToDb().execute();
            }
            tallyBoxes();
        }
        @Override
        protected void onProgressUpdate(Void... values){

        }

    }

    public boolean checkService(){
        ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE))
        {
            if ("com.crop_sense.farmerinterfaceapplication.LocationService"
                    .equals(service.service.getClassName()))
            {
                return true;
            }
        }
        return false;
    }

}
