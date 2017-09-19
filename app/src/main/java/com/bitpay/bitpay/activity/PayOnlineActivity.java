package com.bitpay.bitpay.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bitpay.bitpay.R;
import com.bitpay.bitpay.application.BitPayApplication;
import com.bitpay.bitpay.fragment.BuyBitcoinFragment;
import com.bitpay.bitpay.models.DepositAmtModel;
import com.bitpay.bitpay.models.PancardModel;
import com.bitpay.bitpay.network.BaseVolleyRequest;
import com.bitpay.bitpay.utils.DocumentUtils;
import com.bitpay.bitpay.utils.Helper;
import com.bitpay.bitpay.utils.SharedPreferenceUtils;
import com.bitpay.bitpay.utils.Validator;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Codeslay-03 on 7/25/2017.
 */

public class PayOnlineActivity extends BaseActivity {

    private Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_online);
    }

    @Override
    protected void initViews() {
        settingTitle(getString(R.string.text_pay_online));
        fragment = new BuyBitcoinFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.fragment_container, fragment);
        ft.commit();
    }

    @Override
    protected void initContext() {
        context = PayOnlineActivity.this;
        currentActivity = PayOnlineActivity.this;
    }

    @Override
    protected void initListners() {

    }

    @Override
    protected boolean isActionBar() {
        return true;
    }

    @Override
    protected boolean isHomeButton() {
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

        }
    }

    @Override
    public void onAlertClicked(int alertType) {

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        switch (requestCode) {
            case REQUEST_TAG_CONFIRM_PIN: {
                if (fragment != null) {
                    fragment.onActivityResult(requestCode, resultCode, imageReturnedIntent);
                }
                break;
            }
        }
    }
}


