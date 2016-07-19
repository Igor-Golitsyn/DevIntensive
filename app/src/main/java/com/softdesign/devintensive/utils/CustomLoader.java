package com.softdesign.devintensive.utils;


import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.softdesign.devintensive.data.storage.models.User;
import com.softdesign.devintensive.data.storage.models.UserDao;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Игорь on 19.07.2016.
 */
public class CustomLoader extends AsyncTaskLoader<List<User>> {
    private static final String TAG = ConstantManager.TAG_PREFIX + "CustomLoader";

    public CustomLoader(Context context) {
        super(context);
        Log.d(TAG,"CustomLoader");
    }

    @Override
    public List<User> loadInBackground() {
        Log.d(TAG,"loadInBackground");
        List<User> userList = new ArrayList<>();
        try {
            userList = DevintensiveApplication.getDaoSession().queryBuilder(User.class)
                    .where(UserDao.Properties.CodeLines.gt(0))
                    .orderDesc(UserDao.Properties.CodeLines)
                    .build()
                    .list();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return userList;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        Log.d(TAG,"onStartLoading");
        forceLoad();
    }
}
