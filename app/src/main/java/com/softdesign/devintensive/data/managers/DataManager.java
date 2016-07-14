package com.softdesign.devintensive.data.managers;

import com.softdesign.devintensive.data.network.RestService;
import com.softdesign.devintensive.data.network.ServiceGenerator;
import com.softdesign.devintensive.data.network.req.UserLoginReq;
import com.softdesign.devintensive.data.network.res.UserListRes;
import com.softdesign.devintensive.data.network.res.UserModelRes;

import retrofit2.Call;

/**
 * Created by Игорь on 28.06.2016.
 */
public class DataManager {
    private static DataManager ourInstance = new DataManager();
    private PreferenceManager mPreferenceManager;
    private RestService mRestService;

    public PreferenceManager getPreferenceManager() {
        return mPreferenceManager;
    }

    public static DataManager getInstance() {
        return ourInstance;
    }

    private DataManager() {
        mPreferenceManager = new PreferenceManager();
        mRestService = ServiceGenerator.createService(RestService.class);
    }

    //===========region Network======================
    public Call<UserModelRes> loginUser( UserLoginReq userLoginReq) {
        return mRestService.loginUser( userLoginReq);
    }
    public Call<UserListRes> getUserList(){
        return mRestService.getUserList();
    }
    //==============================
}
