package com.softdesign.devintensive.data.managers;

import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Log;

import com.softdesign.devintensive.utils.ConstantManager;
import com.softdesign.devintensive.utils.DevintensiveApplication;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Игорь on 28.06.2016.
 */
public class PreferenceManager {
    private static final String TAG = ConstantManager.TAG_PREFIX + "PreferenceManager";
    private SharedPreferences mSharedPreferences;
    private static final String[] USER_FIELDS = {
            ConstantManager.USER_PHONE_KEY,
            ConstantManager.USER_EMAIL_KEY,
            ConstantManager.USER_VK_KEY,
            ConstantManager.USER_GIT_KEY,
            ConstantManager.USER_BIO_KEY,
    };
    private static final String[] USER_VALUES = {
            ConstantManager.USER_REITING_VALUE,
            ConstantManager.USER_CODE_LINES_VALUE,
            ConstantManager.USER_PROJECT_VALUE,
    };

    public PreferenceManager() {
        Log.d(TAG, "PreferenceManager");
        this.mSharedPreferences = DevintensiveApplication.getSharedPreferences();
    }

    public void saveUserProfileData(List<String> userFields) {
        Log.d(TAG, "saveUserProfileData");
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        for (int i = 0; i < USER_FIELDS.length; i++) {
            editor.putString(USER_FIELDS[i], userFields.get(i));
        }
        editor.apply();
    }

    public List<String> loadUserProfileData() {
        Log.d(TAG, "loadUserProfileData");
        List<String> userData = new ArrayList<>();
        for (int i = 0; i < USER_FIELDS.length; i++) {
            userData.add(mSharedPreferences.getString(USER_FIELDS[i], "null"));
        }
        return userData;
    }

    public void saveUserPhoto(Uri uri) {
        Log.d(TAG, "saveUserPhoto");
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(ConstantManager.USER_PHOTO_KEY, uri.toString());
        editor.apply();
    }
    public void saveUserAvatar(Uri uri) {
        Log.d(TAG, "saveUserAvatar");
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(ConstantManager.USER_AVATAR_KEY, uri.toString());
        editor.apply();
    }

    public Uri loadUserPhoto() {
        Log.d(TAG, "loadUserPhoto");
        Uri uri=Uri.parse(mSharedPreferences.getString(ConstantManager.USER_PHOTO_KEY, "android.resource://com.softdesign.devintensive/drawable/userphoto"));
        return uri;
    }
    public Uri loadUserAvatar() {
        Log.d(TAG, "loadUserAvatar");
        Uri uri=Uri.parse(mSharedPreferences.getString(ConstantManager.USER_AVATAR_KEY, "android.resource://com.softdesign.devintensive/drawable/camaro_yellow"));
        return uri;
    }

    public void saveAuthToken(String token) {
        Log.d(TAG, "saveAuthToken");
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(ConstantManager.AUTH_TOKEN, token);
        editor.apply();
    }

    public String getAuthToken() {
        Log.d(TAG, "getAuthToken");
        return mSharedPreferences.getString(ConstantManager.AUTH_TOKEN, "null");
    }

    public void saveUserId(String userId) {
        Log.d(TAG, "saveUserId");
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(ConstantManager.USER_ID_KEY, userId);
        editor.apply();
    }

    public String getUserId() {
        Log.d(TAG, "getUserId");
        return mSharedPreferences.getString(ConstantManager.USER_ID_KEY, "null");
    }

    public void saveUserProfileValues(int[] userValues) {
        Log.d(TAG, "saveUserProfileValues");
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        for (int i = 0; i < USER_VALUES.length; i++) {
            editor.putString(USER_VALUES[i],String.valueOf(userValues[i]));
        }
        editor.apply();
    }
    public List<String> loadUserProfileValues(){
        List<String> valuse=new ArrayList<>();
        valuse.add(mSharedPreferences.getString(ConstantManager.USER_REITING_VALUE,"0"));
        valuse.add(mSharedPreferences.getString(ConstantManager.USER_CODE_LINES_VALUE,"0"));
        valuse.add(mSharedPreferences.getString(ConstantManager.USER_PROJECT_VALUE,"0"));
        return valuse;
    }
}
