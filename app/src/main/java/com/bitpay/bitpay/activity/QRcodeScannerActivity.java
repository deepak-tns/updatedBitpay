package com.bitpay.bitpay.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import me.dm7.barcodescanner.zbar.Result;
import me.dm7.barcodescanner.zbar.ZBarScannerView;

public class QRcodeScannerActivity extends BaseActivity implements ZBarScannerView.ResultHandler {

    private ZBarScannerView mScannerView;


    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        mScannerView = new ZBarScannerView(this);
        setContentView(mScannerView);
    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this);
        mScannerView.startCamera();
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();
    }

    @Override
    protected void initViews() {

    }

    @Override
    protected void initContext() {
        context = QRcodeScannerActivity.this;
        currentActivity = QRcodeScannerActivity.this;
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
    public void handleResult(Result rawResult) {
        Log.v("handleResult", rawResult.getContents());
        Intent intent = new Intent();
        intent.putExtra(USER_BIT_ADDRESS, rawResult.getContents());
        setResult(RESULT_OK, intent);
        finish();
        //Toast.makeText(this, rawResult.getContents(), Toast.LENGTH_LONG).show();
        // If you would like to resume scanning, call this method below:
        //mScannerView.resumeCameraPreview(this);
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onAlertClicked(int alertType) {

    }
}
