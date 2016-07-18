package com.softdesign.devintensive.utils;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.facebook.stetho.Stetho;
import com.softdesign.devintensive.data.storage.models.DaoMaster;
import com.softdesign.devintensive.data.storage.models.DaoSession;

import org.greenrobot.greendao.database.Database;


public class DevintensiveApplication extends Application {
    private static final String TAG = ConstantManager.TAG_PREFIX + "DevintensiveApp";
    public static SharedPreferences sSharedPreferences;
    private static Context sContext;
    private static DaoSession sDaoSession;

    public static SharedPreferences getSharedPreferences() {
        return sSharedPreferences;
    }

    public static Context getContext() {
        return sContext;
    }

    @Override
    public void onCreate() {
        Log.d(TAG, "onCreate");
        super.onCreate();
        sSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sContext = getApplicationContext();

        DaoMaster.DevOpenHelper helper=new DaoMaster.DevOpenHelper(this,"devintensive-db");
        Database db=helper.getWritableDb();
        sDaoSession=new DaoMaster(db).newSession();
        Stetho.initializeWithDefaults(this);
    }

    public static DaoSession getDaoSession() {
        return sDaoSession;
    }
}
