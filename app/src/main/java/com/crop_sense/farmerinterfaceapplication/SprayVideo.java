package com.crop_sense.farmerinterfaceapplication;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.PopupWindow;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class SprayVideo extends AppCompatActivity {

    private final static int REQUEST_ENABLE_BT = 1;
    Intent enableBtIntent;


    Boolean flag=false;

    EditText searchInput;

    ListView listView;
    ListViewAdapter adapter;
    ListViewAdapter pairedAdapter;
    List<String> arrayList = new ArrayList<>();
    List<String> pairedList = new ArrayList<>();

    String[] explanationVideo;
    String[] scoutVideo;
    String[] sprayVideo;

    SoundPool soundPool;
    int soundId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spray_video);


        final Intent videointent = new Intent(this, fullscreenvideo.class);

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
        adapter = (new ListViewAdapter(getApplicationContext(), arrayList));

        pairedAdapter = (new ListViewAdapter(getApplicationContext(), pairedList));

        listView = (ListView) findViewById(R.id.listView);

        listView.setAdapter(adapter);

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

    public void homeClick (View view){

        InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(searchInput.getWindowToken(), 0);
        soundPool.play(soundId,1,1,0,0,1);
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
        soundPool.play(soundId,1,1,0,0,1);
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

    public void option1Click (View view){
        InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(searchInput.getWindowToken(), 0);
        Toast.makeText(getApplicationContext(), "Feature not yet active", Toast.LENGTH_SHORT).show();
    }
    public void option2Click (View view){
        InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(searchInput.getWindowToken(), 0);
        Toast.makeText(getApplicationContext(), "Feature not yet active", Toast.LENGTH_SHORT).show();
    }
    public void option3Click (View view){
        InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(searchInput.getWindowToken(), 0);
        Toast.makeText(getApplicationContext(), "Feature not yet active", Toast.LENGTH_SHORT).show();
    }
    public void option4Click (View view){
        InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(searchInput.getWindowToken(), 0);
        Toast.makeText(getApplicationContext(), "Feature not yet active", Toast.LENGTH_SHORT).show();
    }
    public void option5Click (View view){
        InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(searchInput.getWindowToken(), 0);
        Toast.makeText(getApplicationContext(), "Feature not yet active", Toast.LENGTH_SHORT).show();
    }
    public void option6Click (View view){
        InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(searchInput.getWindowToken(), 0);
        enableBluetooth();
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            createNewSoundPool();
        }else{
            createOldSoundPool();
        }

        soundId = soundPool.load(getApplicationContext(), R.raw.buttonclick, 1);
        super.onResume();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if (resultCode == RESULT_CANCELED){
            Toast.makeText(getApplicationContext(), "Enabling Bluetooth failed", Toast.LENGTH_SHORT).show();
        }else if (resultCode == RESULT_OK){
            Toast.makeText(getApplicationContext(), "Succesfully enabled Bluetooth", Toast.LENGTH_SHORT).show();
            ApplicationInfo app = getApplicationContext().getApplicationInfo();
            String filePath = app.sourceDir;

            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_SEND);
            intent.setType("*/*");
            intent.setPackage("com.android.bluetooth");
            intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(filePath)));
            startActivity(Intent.createChooser(intent, "Share app"));
        }


    }

    @Override
    protected void onDestroy(){
        soundPool.release();
        soundPool=null;
        super.onDestroy();
    }
    BluetoothAdapter btAdapter;

    public void enableBluetooth(){
        btAdapter = BluetoothAdapter.getDefaultAdapter();
        if (btAdapter == null){
            Toast.makeText(getApplicationContext(), "Device does not have Bluetooth capabilities", Toast.LENGTH_SHORT).show();
        }else if (!btAdapter.isEnabled()){
            enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);

        }else{

            ApplicationInfo app = getApplicationContext().getApplicationInfo();
            String filePath = app.sourceDir;

            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_SEND);
            intent.setType("*/*");
            intent.setPackage("com.android.bluetooth");
            intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(filePath)));
            startActivity(Intent.createChooser(intent, "Share app"));
        }

    }

}
