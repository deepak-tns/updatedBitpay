package com.bitpay.bitpay.fragment;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bitpay.bitpay.R;
import com.bitpay.bitpay.utils.FontUtils;
import com.bitpay.bitpay.utils.SharedPreferenceUtils;
import com.google.zxing.WriterException;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;


public class BitAddressFragment extends BaseFragment {

    private TextView lblBitcoinTV;
    private TextView textBitcoinTV;
    private TextView copyTV;
    private TextView shareTV;
    private ImageView buttonCopyIV;
    private ImageView buttonShareIV;
    private ImageView buttonScanIV;
    private TextView lbl1TV;
    private TextView lbl2TV;

    public BitAddressFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return view = inflater.inflate(R.layout.fragment_bitaddtess, container, false);
    }


    @Override
    public void alertOkClicked(int alertType) {

    }

    @Override
    protected void initViews() {

        lblBitcoinTV = (TextView) view.findViewById(R.id.lblBitcoinTV);
        textBitcoinTV = (TextView) view.findViewById(R.id.textBitcoinTV);
        copyTV = (TextView) view.findViewById(R.id.copyTV);
        shareTV = (TextView) view.findViewById(R.id.shareTV);
        buttonCopyIV = (ImageView) view.findViewById(R.id.buttonCopyIV);
        buttonShareIV = (ImageView) view.findViewById(R.id.buttonShareIV);
        buttonScanIV = (ImageView) view.findViewById(R.id.buttonScanIV);
        lbl1TV = (TextView) view.findViewById(R.id.lbl1TV);
        lbl2TV = (TextView) view.findViewById(R.id.lbl2TV);


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
        currentActivity = getActivity();
        context = getActivity();
    }

    @Override
    protected void initListners() {
        buttonCopyIV.setOnClickListener(this);
        buttonShareIV.setOnClickListener(this);
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

    private void copyBitAddress() {
        android.content.ClipboardManager clipboard = (android.content.ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
        android.content.ClipData clip = android.content.ClipData.newPlainText("Text Label", textBitcoinTV.getText().toString());
        clipboard.setPrimaryClip(clip);
        toast(context, "Copied to Clipboard !");
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
