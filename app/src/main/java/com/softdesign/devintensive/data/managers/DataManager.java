package com.softdesign.devintensive.data.managers;

/**
 * Created by Игорь on 28.06.2016.
 */
public class DataManager {
    private static DataManager ourInstance = new DataManager();
    private PreferenceManager mPreferenceManager;

    public PreferenceManager getPreferenceManager() {
        return mPreferenceManager;
    }

    public static DataManager getInstance() {
        return ourInstance;
    }

    private DataManager() {
        mPreferenceManager=new PreferenceManager();
    }
}
