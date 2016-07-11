package com.softdesign.devintensive.utils;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;


public class DevintensiveApplication extends Application {
    private static final String TAG = ConstantManager.TAG_PREFIX + "DevintensiveApp";
    public static SharedPreferences sSharedPreferences;

    public static SharedPreferences getSharedPreferences() {
        return sSharedPreferences;
    }

    @Override
    public void onCreate() {
        Log.d(TAG, "onCreate");
        super.onCreate();
        sSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
    }
}
