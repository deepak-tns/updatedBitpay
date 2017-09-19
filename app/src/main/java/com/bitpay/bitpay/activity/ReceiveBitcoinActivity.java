package com.bitpay.bitpay.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bitpay.bitpay.R;
import com.bitpay.bitpay.utils.FontUtils;
import com.bitpay.bitpay.utils.SharedPreferenceUtils;
import com.google.zxing.WriterException;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class ReceiveBitcoinActivity extends BaseActivity {

    private TextView lblBitcoinTV;
    private TextView textBitcoinTV;
    private TextView copyTV;
    private TextView shareTV;
    private ImageView buttonCopyIV;
    private ImageView buttonShareIV;
    private ImageView buttonScanIV;
    private TextView lbl1TV;
    private TextView lbl2TV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receive_bitcoin);
    }

    @Override
    protected void initViews() {
        settingTitle(getString(R.string.menu_receive_bitcoin));

        lblBitcoinTV = (TextView) findViewById(R.id.lblBitcoinTV);
        textBitcoinTV = (TextView) findViewById(R.id.textBitcoinTV);
        copyTV = (TextView) findViewById(R.id.copyTV);
        shareTV = (TextView) findViewById(R.id.shareTV);
        buttonCopyIV = (ImageView) findViewById(R.id.buttonCopyIV);
        buttonShareIV = (ImageView) findViewById(R.id.buttonShareIV);
        buttonScanIV = (ImageView) findViewById(R.id.buttonScanIV);
        lbl1TV = (TextView) findViewById(R.id.lbl1TV);
        lbl2TV = (TextView) findViewById(R.id.lbl2TV);


        textBitcoinTV.setText(SharedPreferenceUtils.getInstance(context).getString(USER_BIT_ADDRESS));

        FontUtils.changeFont(context, lblBitcoinTV, FONT_CORBEL_REGULAR);
        FontUtils.changeFont(context, textBitcoinTV, FONT_ROBOTO_REGULAR);
        FontUtils.changeFont(context, copyTV, FONT_CORBEL_REGULAR);
        FontUtils.changeFont(context, shareTV, FONT_CORBEL_REGULAR);
        FontUtils.changeFont(context, lbl1TV, FONT_CORBEL_REGULAR);
        FontUtils.changeFont(context, lbl2TV, FONT_CORBEL_REGULAR);

        int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.6);
        QRGEncoder qrgEncoder = new QRGEncoder(textBitcoinTV.getText().toString(), null, QRGContents.Type.TEXT, width);

        try {
            // Getting QR-Code as Bitmap
            Bitmap bitmap = qrgEncoder.encodeAsBitmap();
            // Setting Bitmap to ImageView
            buttonScanIV.setImageBitmap(bitmap);
        } catch (WriterException e) {
            Log.v("", e.toString());
        }


    }

    @Override
    protected void initContext() {
        currentActivity = ReceiveBitcoinActivity.this;
        context = ReceiveBitcoinActivity.this;
    }

    @Override
    protected void initListners() {
        buttonCopyIV.setOnClickListener(this);
        buttonShareIV.setOnClickListener(this);
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
            case R.id.buttonCopyIV: {
                copyBitAddress();
                break;
            }
            case R.id.buttonShareIV: {
                shareBitAddress();
                break;
            }
        }
    }

    @Override
    public void onAlertClicked(int alertType) {

    }

    private void copyBitAddress() {
        android.content.ClipboardManager clipboard = (android.content.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        android.content.ClipData clip = android.content.ClipData.newPlainText("Text Label", textBitcoinTV.getText().toString());
        clipboard.setPrimaryClip(clip);
        toast("Copied to Clipboard !", true);
    }

    public void shareBitAddress() {
        //drawerLayout.closeDrawer(Gravity.LEFT);
        try {
            String message = textBitcoinTV.getText().toString();
            Intent share = new Intent(Intent.ACTION_SEND);
            share.setType("text/plain");
            share.putExtra(Intent.EXTRA_TEXT, message);
            startActivity(Intent.createChooser(share, "Share Via"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
