package com.example.kakyunglee.smokingproject.activity.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;

import com.example.kakyunglee.smokingproject.R;
import com.example.kakyunglee.smokingproject.activity.util.UtilPermissions;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ckj on 2017-10-22.
 */

public class Splash extends Activity {

    private static final int PERMISSION_ALL = 0;
    private Handler h;
    private Runnable r;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        h = new Handler();
        r = new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(Splash.this, MainActivity.class));
                finish();
            }
        };

        String[] PERMISSIONS = {
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.CAMERA,
                android.Manifest.permission.READ_EXTERNAL_STORAGE
        };

        if(!UtilPermissions.hasPermissions(this, PERMISSIONS)){
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }
        else
            h.postDelayed(r, 1500);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {

        int index = 0;
        Map<String, Integer> PermissionsMap = new HashMap<String, Integer>();
        for (String permission : permissions){
            PermissionsMap.put(permission, grantResults[index]);
            index++;
        }

        if((PermissionsMap.get( android.Manifest.permission.ACCESS_FINE_LOCATION) != 0)
                || PermissionsMap.get( Manifest.permission.CAMERA) != 0
                || PermissionsMap.get( Manifest.permission.READ_EXTERNAL_STORAGE) != 0){
            finish();
        }
        else
        {
            h.postDelayed(r, 1500);
        }
    }


}

