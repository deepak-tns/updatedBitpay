package com.bitpay.bitpay.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;

import com.bitpay.bitpay.R;
import com.bitpay.bitpay.utils.SharedPreferenceUtils;


public class SplashActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
    }

    @Override
    protected void initViews() {
        createSplashThread();
    }

    @Override
    protected void initContext() {
        context = SplashActivity.this;
        currentActivity = SplashActivity.this;
    }

    @Override
    protected void initListners() {

    }

    @Override
    protected boolean isActionBar() {
        return false;
    }

    @Override
    protected boolean isHomeButton() {
        return false;
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onAlertClicked(int alertType) {

    }

    private void createSplashThread() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if (Build.VERSION.SDK_INT >= 23) {
                    // Marshmallow+
                    if (!checkReceiveSmsPermission()) {
                        requestPermission();
                    } else {
                        moveToNextActivity();
                    }
                } else {
                    moveToNextActivity();
                }

            }
        }, SPLASH_TIME);
    }

    private boolean checkReceiveSmsPermission() {
        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    private void moveToNextActivity() {
        String userEnteredPin = SharedPreferenceUtils.getInstance(context).getString(USER_SECURE_PIN);
        if (TextUtils.isEmpty(userEnteredPin)) {
            startActivity(currentActivity, LandingActivity.class, bundle, false, REQUEST_TAG_NO_RESULT, true, ANIMATION_SLIDE_UP);
        } else {
            startActivity(currentActivity, DashboardActivity.class, bundle, false, REQUEST_TAG_NO_RESULT, true, ANIMATION_SLIDE_UP);
        }
        finish();
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECEIVE_SMS, Manifest.permission.CAMERA}, RECEIVE_SMS_PERMISSION_REQUEST);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case RECEIVE_SMS_PERMISSION_REQUEST:
                moveToNextActivity();
                break;
        }
    }

}

