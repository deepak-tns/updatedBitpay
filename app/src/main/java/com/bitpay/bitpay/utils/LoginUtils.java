package com.bitpay.bitpay.utils;

import android.content.Context;

import com.bitpay.bitpay.constant.AppConstants;


/**
 * Created by Codeslay-03 on 1/23/2017.
 */
public class LoginUtils {


    public static boolean isLogin(Context context) {

        return false;

        //return SharedPreferenceUtils.getInstance(context).getSharedPreferences().getBoolean(AppConstants.IS_LOGIN, false);
    }


}
