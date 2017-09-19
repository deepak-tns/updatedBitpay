package com.bitpay.bitpay.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.bitpay.bitpay.constant.AppConstants;

/**
 * Created by Codeslay-03 on 1/23/2017.
 */

public class SharedPreferenceUtils {
    static SharedPreferenceUtils sharedPreferenceUtils;
    static SharedPreferences sharedPreferences;

    static Context context;

    private SharedPreferenceUtils() {

    }

    public static SharedPreferenceUtils getInstance(Context context) {
        if (sharedPreferenceUtils == null) {
            sharedPreferenceUtils = new SharedPreferenceUtils();
        }

        if (sharedPreferenceUtils.context == null) {
            sharedPreferenceUtils.context = context;
        }
        return sharedPreferenceUtils;
    }

    protected SharedPreferences getSharedPreferences() {

        if (sharedPreferences == null) {
            sharedPreferences = context.getSharedPreferences(AppConstants.APP_NAME, Context.MODE_PRIVATE);
        }
        return sharedPreferences;
    }


    public String getString(String key) {
        return getSharedPreferences().getString(key, "");
    }

    public boolean getBoolean(String key) {
        return getSharedPreferences().getBoolean(key, false);
    }

    public void putBoolean(String key, boolean value) {
        getSharedPreferences().edit().putBoolean(key, value).commit();

    }

    public void putString(String key, String value) {
        getSharedPreferences().edit().putString(key, value).commit();
    }

    public void putInteger(String key, int value) {
        getSharedPreferences().edit().putInt(key, value).commit();
    }

    public int getInteger(String key) {
        return getSharedPreferences().getInt(key, 0);
    }

    public void clearALl() {
        getSharedPreferences().edit().clear().commit();
    }

}
