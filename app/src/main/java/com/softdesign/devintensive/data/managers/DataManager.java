package com.softdesign.devintensive.data.managers;

import android.content.Context;

import com.softdesign.devintensive.data.network.PicassoCache;
import com.softdesign.devintensive.data.network.RestService;
import com.softdesign.devintensive.data.network.ServiceGenerator;
import com.softdesign.devintensive.data.network.req.UserLoginReq;
import com.softdesign.devintensive.data.network.res.UserListRes;
import com.softdesign.devintensive.data.network.res.UserModelRes;
import com.softdesign.devintensive.data.storage.models.DaoSession;
import com.softdesign.devintensive.data.storage.models.User;
import com.softdesign.devintensive.data.storage.models.UserDao;
import com.softdesign.devintensive.utils.DevintensiveApplication;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

/**
 * Created by Игорь on 28.06.2016.
 */
public class DataManager {
    private static DataManager ourInstance = new DataManager();
    private PreferenceManager mPreferenceManager;
    private RestService mRestService;
    private Context mContext;
    private Picasso mPicasso;
    private DaoSession mDaoSession;

    public PreferenceManager getPreferenceManager() {
        return mPreferenceManager;
    }

    public static DataManager getInstance() {
        return ourInstance;
    }

    private DataManager() {
        mPreferenceManager = new PreferenceManager();
        mRestService = ServiceGenerator.createService(RestService.class);
        mContext = DevintensiveApplication.getContext();
        mPicasso = new PicassoCache(mContext).getPicassoInstance();
        mDaoSession = DevintensiveApplication.getDaoSession();
    }

    public Context getContext() {
        return mContext;
    }

    public Picasso getPicasso() {
        return mPicasso;
    }

    public DaoSession getDaoSession() {
        return mDaoSession;
    }

    //===========region Network======================
    public Call<UserModelRes> loginUser(UserLoginReq userLoginReq) {
        return mRestService.loginUser(userLoginReq);
    }

    public Call<UserListRes> getUserListFromNetwork() {
        return mRestService.getUserList();
    }

    /*public Call<UploadPfotoRes> uploadPhoto(String userId, RequestBody photoFile) {
        return mRestService.uploadPhoto(userId, photoFile);
    }*/
    //==============================

    //-------------------db---------------------
    public List<User> getUserListFromDb() {
        List<User> userList = new ArrayList<>();
        try {
            userList = mDaoSession.queryBuilder(User.class)
                    .where(UserDao.Properties.CodeLines.gt(0))
                    .orderDesc(UserDao.Properties.CodeLines)
                    .build()
                    .list();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return userList;
    }
    public List<User>getUserListByName(String name){
        List<User> userList=new ArrayList<>();
        userList=mDaoSession.queryBuilder(User.class)
        .where(UserDao.Properties.Rating.gt(0),UserDao.Properties.SearchName.like("%"+name.toUpperCase()+"%"))
        .orderDesc(UserDao.Properties.CodeLines)
        .build()
        .list();
        return userList;
    }

    //------------------------------------------
}
