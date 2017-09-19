package com.bitpay.bitpay.service;

import android.content.Intent;
import android.util.Log;

import com.bitpay.bitpay.constant.AppConstants;
import com.bitpay.bitpay.utils.SharedPreferenceUtils;
import com.google.firebase.iid.FirebaseInstanceIdService;


/**
 * Created by admin on 15-11-2016.
 */
public class BitpayFirebaseInstanceIDService extends FirebaseInstanceIdService implements AppConstants {
    private static final String TAG = BitpayFirebaseInstanceIDService.class.getSimpleName();


    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        unsetTokenSended();
        Intent intent = new Intent(this, RegistrationIntentService.class);
        startService(intent);
        Log.e("tokenrefersh", "yes");
    }

    private void unsetTokenSended() {
        SharedPreferenceUtils.getInstance(getApplicationContext()).putBoolean(SENT_TOKEN_TO_SERVER, false);
    }
}
