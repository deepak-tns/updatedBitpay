package com.bitpay.bitpay.activity;

import android.os.Bundle;
import android.view.View;

import com.bitpay.bitpay.R;

/**
 * Created by Codeslay-03 on 7/21/2017.
 */

public class BankDetailsActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_details);
    }

    @Override
    protected void initViews() {
        settingTitle(getString(R.string.title_bank_details));
    }

    @Override
    protected void initContext() {


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

    }

    @Override
    public void onAlertClicked(int alertType) {

    }
}
