package com.crop_sense.farmerinterfaceapplication;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;

public class LocationService extends Service
{
    private LocationManager mLocationManager = null;
    private static final int LOCATION_INTERVAL = 1000;
    private static final float LOCATION_DISTANCE = 1f;
    Location mLastLocation;
    SharedPreferences savedLocation;
    SharedPreferences.Editor edit;
    SharedPreferences savedTime;
    SharedPreferences.Editor editTime;
    String usableLocation = "No Location Detected";

    private class LocationListener implements android.location.LocationListener{

        public LocationListener(String provider)
        {
            mLastLocation = new Location(provider);
        }
        @Override
        public void onLocationChanged(Location location)
        {
            mLastLocation.set(location);

            savedLocation = getSharedPreferences("location", MODE_PRIVATE);
            edit = savedLocation.edit();
            usableLocation = "Latitude: "+ String.valueOf(mLastLocation.getLatitude())+", Longitude: "+ String.valueOf(mLastLocation.getLongitude())+", Accuracy: "+ String.valueOf(mLastLocation.getAccuracy()) + ", Bearing: " + String.valueOf(mLastLocation.getBearing());
            edit.putString("locationAddress", usableLocation);
            edit.apply();

            savedTime = getSharedPreferences("time", MODE_PRIVATE);
            editTime = savedTime.edit();
            editTime.putLong("timeMS", System.currentTimeMillis());
            editTime.apply();

        }
        @Override
        public void onProviderDisabled(String provider)
        {
            //TODO
        }
        @Override
        public void onProviderEnabled(String provider)
        {
            //TODO
        }
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras)
        {
            //TODO
        }
    }

    LocationListener[] mLocationListeners = new LocationListener[] {
            new LocationListener(LocationManager.GPS_PROVIDER),
            new LocationListener(LocationManager.NETWORK_PROVIDER)
    };

    @Override
    public IBinder onBind(Intent arg0)
    {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        super.onStartCommand(intent, flags, startId);
        return START_NOT_STICKY;
    }
    @Override
    public void onCreate()
    {
        initializeLocationManager();
        try {
            mLocationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                    mLocationListeners[1]);
        } catch (Exception e){
            //TODO
        }
        try {
            mLocationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                    mLocationListeners[0]);
        } catch (Exception e){
            //TODO
        }
    }
    @Override
    public void onDestroy()
    {
        super.onDestroy();
        if (mLocationManager != null) {
            for (int i = 0; i < mLocationListeners.length; i++) {
                try {
                    mLocationManager.removeUpdates(mLocationListeners[i]);
                } catch (Exception ex) {
                    //TODO
                }
            }
        }
    }
    private void initializeLocationManager() {
        if (mLocationManager == null) {
            mLocationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        }
    }

}