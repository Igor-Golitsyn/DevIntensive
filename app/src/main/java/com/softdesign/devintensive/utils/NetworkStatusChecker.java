package com.softdesign.devintensive.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Игорь on 11.07.2016.
 */
public class NetworkStatusChecker {
    public static boolean isNetworkAvailable(Context context){
        ConnectivityManager connectivityManager= (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo acktiveNetwork=connectivityManager.getActiveNetworkInfo();
        return acktiveNetwork !=null && acktiveNetwork.isConnectedOrConnecting();
    }
}
