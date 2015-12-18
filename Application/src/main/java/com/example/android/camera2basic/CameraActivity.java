/*
 * Copyright 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.camera2basic;

import android.app.Activity;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PowerManager;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.WindowManager;

import java.util.HashSet;
import java.util.Set;

public class CameraActivity extends Activity {

    public SharedPreferences prefs;

    static public String myClientCodePref; //you can make it an int or whatever you need
    static public Set<String> myDaysToShootPref;
    static public int myWaitDurationPref;
    static public String myPhotoStartTimePref;
    static public String myPhotoFinishTimePref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        setContentView(R.layout.activity_camera);
        initializeStyle();
        createWakeLocks();
        if (null == savedInstanceState) {
            getFragmentManager().beginTransaction()
                    .replace(R.id.container, Camera2BasicFragment.newInstance())
                    .commit();
        }
    }

    private void initializeStyle() {
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        myClientCodePref = prefs.getString("client_code", "");
        myDaysToShootPref = prefs.getStringSet("photo_days_list", new HashSet<String>());
        myWaitDurationPref = Integer.parseInt(prefs.getString("photo_frequency", "0"));
        myPhotoStartTimePref = prefs.getString("photo_start_time", "");
        myPhotoFinishTimePref = prefs.getString("photo_finish_time", "");
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        if(fullWakeLock.isHeld()){
            fullWakeLock.release();
        }
        if(partialWakeLock.isHeld()){
            partialWakeLock.release();
        }
        SharedPreferences myPreference=PreferenceManager.getDefaultSharedPreferences(this);
        myClientCodePref = prefs.getString("client_code", "");
        myDaysToShootPref = prefs.getStringSet("photo_days_list", new HashSet<String>());
        myWaitDurationPref = Integer.parseInt(prefs.getString("photo_frequency", "0"));
        myPhotoStartTimePref = prefs.getString("photo_start_time", "");
        myPhotoFinishTimePref = prefs.getString("photo_finish_time", "");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    // This method is called once the menu is selected
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                // Launch settings activity
                Intent i = new Intent(this, ActivitySettings.class);
                startActivity(i);
                break;
            // more code...
        }
        return true;
    }

    public static PowerManager.WakeLock fullWakeLock;
    public static PowerManager.WakeLock partialWakeLock;

    // Called from onCreate
    protected void createWakeLocks(){
        PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
        fullWakeLock = powerManager.newWakeLock((PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP), "Loneworker - FULL WAKE LOCK");
        partialWakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "Loneworker - PARTIAL WAKE LOCK");
    }

    // Called implicitly when device is about to sleep or application is backgrounded
    @Override
    protected void onPause(){
        super.onPause();
        partialWakeLock.acquire();
    }

    // Called whenever we need to wake up the device
    public static void wakeDevice() {
        fullWakeLock.acquire();

        //KeyguardManager keyguardManager = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
        //KeyguardManager.KeyguardLock keyguardLock = keyguardManager.newKeyguardLock("TAG");
        //keyguardLock.disableKeyguard();
    }

    public static void releaseLock() {
        if(fullWakeLock.isHeld()){
            fullWakeLock.release();
        }
    }

}


