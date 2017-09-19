package com.bitpay.bitpay.utils;

import android.content.Context;
import android.provider.Settings;


/**
 * Created by admin on 30-11-2016.
 */

public class DeviceUtils {
    public static String getDeviceUUID(Context context) {

        String deviceUUID = "";

        /*TelephonyManager tManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (tManager != null) {
            deviceUUID = tManager.getDeviceId();
        }*/
        try {
            deviceUUID = Settings.Secure.getString(context.getContentResolver(),
                    Settings.Secure.ANDROID_ID);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return deviceUUID;
    }


}
