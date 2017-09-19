package com.bitpay.bitpay.service;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.bitpay.bitpay.constant.AppConstants;
import com.bitpay.bitpay.utils.SharedPreferenceUtils;
import com.google.firebase.iid.FirebaseInstanceId;


public class RegistrationIntentService extends IntentService implements AppConstants {

    private static final String TAG = "RegIntentService";

    public RegistrationIntentService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.e("fcm reg", "service");
        try {
            unsetTokenSended();
            String refreshedToken = FirebaseInstanceId.getInstance().getToken();
            Log.e("refreshedToken = ", refreshedToken);
            SharedPreferenceUtils.getInstance(getApplicationContext()).putString(FCM_TOKEN, refreshedToken);
        } catch (Exception e) {
            Log.e(TAG, "Failed to complete token refresh", e);
        }
    }

    private void unsetTokenSended() {
        SharedPreferenceUtils.getInstance(getApplicationContext()).putBoolean(SENT_TOKEN_TO_SERVER, false);
    }
}