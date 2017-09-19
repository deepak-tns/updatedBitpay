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
import com.bitpay.bitpay.models.BankDetailsModel;
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

public class SubmitRequestActivity extends BaseActivity {

    private Spinner spinnerAcctNo;
    private EditText editRefNo;
    private EditText editAmount;
    private Button btnSubmit;
    private ImageView pancardImageIV;
    private ImageView uploadPanIV;

    private String buyingRate;
    private String sellingRate;
    private int bytesAvailable;
    private int bytesRead;
    private String mimeType;
    private double minTransferAmt;
    private double maxTransferAmt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_request);
    }

    @Override
    protected void initViews() {
        settingTitle(getString(R.string.btn_submit_request));
        spinnerAcctNo = (Spinner) findViewById(R.id.spinnerAcctNo);
        editRefNo = (EditText) findViewById(R.id.editRefNo);
        editAmount = (EditText) findViewById(R.id.editAmount);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        pancardImageIV = (ImageView) findViewById(R.id.pancardImageIV);
        uploadPanIV = (ImageView) findViewById(R.id.uploadPanIV);

        String minAmount = SharedPreferenceUtils.getInstance(context).getString(KEY_MIN_AMOUNT);
        String maxAmount = SharedPreferenceUtils.getInstance(context).getString(KEY_MAX_AMOUNT);
        if (!TextUtils.isEmpty(minAmount) && !TextUtils.isEmpty(maxAmount)) {
            minTransferAmt = Double.parseDouble(minAmount);
            maxTransferAmt = Double.parseDouble(maxAmount);
        }

        getBitcoinRate();
    }

    @Override
    protected void initContext() {
        context = SubmitRequestActivity.this;
        currentActivity = SubmitRequestActivity.this;
    }

    @Override
    protected void initListners() {
        btnSubmit.setOnClickListener(this);
        uploadPanIV.setOnClickListener(this);
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
            case R.id.btnSubmit: {
                toHideKeyboard();
                if (!DocumentUtils.isDocumentApproved(context)) {
                    alert(currentActivity, "", getString(R.string.error_document_status), getResources().getString(R.string.alert_ok_button_text_no_network), getResources().getString(R.string.alert_cancel_button_text_no_network), false, false, ALERT_TYPE_NO_NETWORK);
                    return;
                }
                if (Validator.getInstance().isNetworkAvailable(currentActivity)) {
                    if (isMandatoryFields()) {
                        depositAmtRequest();
                    }
                } else {
                    alert(currentActivity, getResources().getString(R.string.alert_message_no_network), getResources().getString(R.string.alert_message_no_network), getResources().getString(R.string.alert_ok_button_text_no_network), getResources().getString(R.string.alert_cancel_button_text_no_network), false, false, ALERT_TYPE_NO_NETWORK);
                }
                break;
            }
            case R.id.uploadPanIV: {
                toHideKeyboard();
                if (Validator.isNetworkAvailable(currentActivity)) {
                    selectImage();
                } else {
                    alert(currentActivity, getResources().getString(R.string.alert_message_no_network), getResources().getString(R.string.alert_message_no_network), getResources().getString(R.string.alert_ok_button_text_no_network), getResources().getString(R.string.alert_cancel_button_text_no_network), false, false, ALERT_TYPE_NO_NETWORK);
                }
                break;
            }
        }
    }

    @Override
    public void onAlertClicked(int alertType) {

    }


    private void selectImage() {
        try {
            if (checkPermission()) {
                openChooseImageDialog();
            } else {
                Toast.makeText(context, "Camera Permission error", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(currentActivity, Manifest.permission.CAMERA);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    public void openChooseImageDialog() {
        final CharSequence[] options = {getString(R.string.option_take_photo), getString(R.string.option_gallery), getString(R.string.option_cancel)};
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(currentActivity);
        builder.setTitle(getString(R.string.select_option));
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals(getString(R.string.option_take_photo))) {
                    dialog.dismiss();
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, PICK_IMAGE_CAMERA);
                } else if (options[item].equals(getString(R.string.option_gallery))) {
                    dialog.dismiss();
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pickPhoto, PICK_IMAGE_GALLERY);
                } else if (options[item].equals(getString(R.string.option_cancel))) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        switch (requestCode) {
            case PICK_IMAGE_CAMERA: {
                if (resultCode == RESULT_OK) {
                    Bitmap imageBitmap = (Bitmap) imageReturnedIntent.getExtras().get(KEY_DATA);
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    imageBitmap.compress(Bitmap.CompressFormat.JPEG, 70, bos);
                    pancardImageIV.setVisibility(View.VISIBLE);
                    pancardImageIV.setImageBitmap(imageBitmap);
                    uploadImageByVolley();
                }
                break;
            }
            case PICK_IMAGE_GALLERY: {
                if (resultCode == RESULT_OK) {
                    Uri filePath = imageReturnedIntent.getData();
                    try {
                        //Getting the Bitmap from Gallery
                        Bitmap imageBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                        ByteArrayOutputStream bos = new ByteArrayOutputStream();
                        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 70, bos);
                        pancardImageIV.setVisibility(View.VISIBLE);
                        pancardImageIV.setImageBitmap(imageBitmap);
                        uploadImageByVolley();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
                break;
            }

        }
    }

    private void getBitcoinRate() {
        progressDialog(context, context.getString(R.string.pdialog_message_loading), context.getString(R.string.pdialog_message_loading), false, false);
        int userId = SharedPreferenceUtils.getInstance(context).getInteger(USER_ID);

        JSONObject jsonGetBitCoinRate = null;
        String URL_GET_BITCOIN_RATE = GET_BITCOIN_RATE_URL;
        try {
            jsonGetBitCoinRate = new JSONObject();
            jsonGetBitCoinRate.put(KEY_USER_ID, userId);
            Log.e("jsonGetContact", jsonGetBitCoinRate.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest getGetBitcoinRateRequest = new JsonObjectRequest(Request.Method.POST, URL_GET_BITCOIN_RATE, jsonGetBitCoinRate, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    cancelProgressDialog();
                    logTesting(currentActivity.getResources().getString(R.string.nwk_response_get_rate), response.toString(), Log.ERROR);
                    String message = response.getString(RESPONCE_MESSAGE);
                    if (!response.getBoolean(RESPONCE_ERROR)) {
                        JSONObject messageJson = new JSONObject(message);
                        buyingRate = messageJson.getString(KEY_BUYING_RATE);
                        sellingRate = messageJson.getString(KEY_SELLING_RATE);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                cancelProgressDialog();
                logTesting(currentActivity.getResources().getString(R.string.nwk_error_get_rate), error.toString(), Log.ERROR);
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return super.getHeaders();
            }
        };

        BitPayApplication.getInstance().addToRequestQueue(getGetBitcoinRateRequest);
    }


    public void uploadImageByVolley() {

        progressDialog(context, context.getString(R.string.pdialog_message_loading), context.getString(R.string.pdialog_message_loading), false, false);


        final String lineEnd = "\r\n";
        final String twoHyphens = "--";
        final String boundary = "*****";

        final int maxBufferSize = 1 * 1024 * 1024;
        final byte[] imageBytesArray = Helper.getImageBytes(((BitmapDrawable) pancardImageIV.getDrawable()).getBitmap());
        Long timeMillis = System.currentTimeMillis();
        final String fileName = "AmountDepositImage_" + timeMillis.toString() + ".jpg";
        PancardModel.getInstance().setImageName(fileName);

        String imageUploadUrl = UPLOAD_NEW_IMAGE;
        BaseVolleyRequest uploadProfileImageRequest = new BaseVolleyRequest(1, imageUploadUrl, new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {

                try {
                    cancelProgressDialog();
                    String jsonString = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
                    Log.e("image response  is", response.toString() + jsonString);
                    JSONObject jsonObject = new JSONObject(jsonString);
                    if (jsonObject.getBoolean(RESPONCE_ERROR)) {
                        pancardImageIV.setVisibility(View.GONE);
                        alert(currentActivity, getString(R.string.message_image_upload_fail), getString(R.string.message_image_upload_fail), getResources().getString(R.string.alert_ok_button_text_no_network), getResources().getString(R.string.alert_cancel_button_text_no_network), false, false, ALERT_TYPE_NO_NETWORK);
                    } else {
                        pancardImageIV.setVisibility(View.VISIBLE);
                        DepositAmtModel.getInstance().setImageName(fileName);
                        toast(getString(R.string.message_image_upload_suc), true);
                    }
                } catch (UnsupportedEncodingException e) {
                    cancelProgressDialog();
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.e("image response  is", response.toString());
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                cancelProgressDialog();
                pancardImageIV.setVisibility(View.GONE);
                toast(getString(R.string.error_uploading_image), true);
                Log.e(getString(R.string.error_uploading_image), error.toString());
            }
        }) {
            @Override
            public String getBodyContentType() {
                mimeType = "multipart/form-data;boundary=" + boundary;
                return mimeType;
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                DataOutputStream dos = new DataOutputStream(bos);


                try {
                    dos.writeBytes(twoHyphens + boundary + lineEnd);
                    dos.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\"" + fileName + "\"" + lineEnd);

                    dos.writeBytes(lineEnd);

                    ByteArrayInputStream fileInputStream = new ByteArrayInputStream(imageBytesArray);
                    bytesAvailable = fileInputStream.available();

                    int bufferSize = Math.min(imageBytesArray.length, maxBufferSize);
                    byte[] buffer = new byte[bufferSize];

                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                    while (bytesRead > 0) {
                        dos.write(buffer, 0, bufferSize);
                        bytesAvailable = fileInputStream.available();
                        bufferSize = Math.min(bytesAvailable, maxBufferSize);
                        bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                    }


                    //    dos.write(imageBytesArray, 0, bufferSize);
                    // send multipart form data necesssary after file data...
                    dos.writeBytes(lineEnd);
                    dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                    return bos.toByteArray();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return imageBytesArray;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                Long timeMillis = System.currentTimeMillis();
                Map<String, String> params = new HashMap<String, String>();
                params.put("Connection", "Keep-Alive");
                params.put("ENCTYPE", "multipart/form-data");
                params.put("accept", "application/json");
                params.put("uploaded_file", fileName);
                params.put("Content-Type", "multipart/form-data;boundary=" + boundary);
                return params;

            }

        };

        BitPayApplication.getInstance().addToRequestQueue(uploadProfileImageRequest);
    }

    private String convertRsToBit(String inputRs) {
        String bitAmt = "";
        try {
            if (TextUtils.isEmpty(buyingRate)) return bitAmt;
            double buyRate = Double.parseDouble(buyingRate);
            if (TextUtils.isEmpty(inputRs)) {
                return bitAmt;
            }
            double inputValue = Double.parseDouble(inputRs);
            double bitcoinValue = inputValue / buyRate;
            bitAmt = new DecimalFormat("##.######").format(bitcoinValue);
        } catch (NumberFormatException nfe) {
            nfe.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitAmt;
    }

    private boolean isMandatoryFields() {

        editRefNo.setError(null);
        editAmount.setError(null);

        if (spinnerAcctNo.getSelectedItem().toString().equalsIgnoreCase("Select Account Number")) {
            toast("Please Select Account Number", true);
            return false;
        } else if (editRefNo.getText().toString().isEmpty()) {
            editRefNo.setError(getResources().getString(R.string.error_empty_ref_no));
            editRefNo.requestFocus();
            return false;
        } else if (editAmount.getText().toString().isEmpty()) {
            editAmount.setError(getResources().getString(R.string.error_empty_amt));
            editAmount.requestFocus();
            return false;
        } else {
            double ammount = Double.parseDouble(editAmount.getText().toString());
            if ((ammount < minTransferAmt)) {
                String errorString = getString(R.string.enter_min_deposit_amount) + " " + minTransferAmt;
                editAmount.setError(errorString);
                editAmount.requestFocus();
                return false;
            }
        }
        String bitAmt = convertRsToBit(editAmount.getText().toString());
        if (TextUtils.isEmpty(bitAmt)) {
            toast("Please try after some time", true);
            return false;
        }
        initDepositAmtModel();
        return true;
    }

    private void initDepositAmtModel() {
        DepositAmtModel.getInstance().setUserId(SharedPreferenceUtils.getInstance(context).getInteger(USER_ID));
        DepositAmtModel.getInstance().setReferenceNo(editRefNo.getText().toString());
        DepositAmtModel.getInstance().setAmountDeposite(editAmount.getText().toString());
        String bitAmt = convertRsToBit(editAmount.getText().toString());
        DepositAmtModel.getInstance().setBitAmount(bitAmt);
        DepositAmtModel.getInstance().setAccountNumber(spinnerAcctNo.getSelectedItem().toString());
        if (DepositAmtModel.getInstance().getImageName() == null)
            DepositAmtModel.getInstance().setImageName("");
    }

    private void depositAmtRequest() {

        progressDialog(context, context.getString(R.string.pdialog_message_loading), context.getString(R.string.pdialog_message_loading), false, false);

        JSONObject jsonDepositAmtRequest = null;
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        try {
            jsonDepositAmtRequest = new JSONObject(gson.toJson(DepositAmtModel.getInstance()));
            Log.e("jsonDepositAmtRequest", jsonDepositAmtRequest.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final String URL_DEPOSIT_AMOUNT = DEPOSIT_AMOUNT_URL;
        JsonObjectRequest addBankDetailRequest = new JsonObjectRequest(Request.Method.POST, URL_DEPOSIT_AMOUNT, jsonDepositAmtRequest, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    cancelProgressDialog();
                    String message = response.getString(RESPONCE_MESSAGE);
                    if (response.getBoolean(RESPONCE_ERROR) || message.isEmpty()) {
                        alert(currentActivity, getString(R.string.msg_add_deposit_amt_failed), getString(R.string.msg_add_deposit_amt_failed), getResources().getString(R.string.alert_ok_button_text_no_network), getResources().getString(R.string.alert_cancel_button_text_no_network), false, false, ALERT_TYPE_NO_NETWORK);
                    } else {
                        toast(getResources().getString(R.string.msg_add_deposit_amt_suc), true);
                        startActivity(currentActivity, DashboardActivity.class, bundle, false, REQUEST_TAG_NO_RESULT, true, ANIMATION_SLIDE_UP);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                cancelProgressDialog();
                logTesting(getResources().getString(R.string.nwk_error_add_deposit_amt), error.toString(), Log.ERROR);
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return super.getHeaders();
            }
        };
        BitPayApplication.getInstance().addToRequestQueue(addBankDetailRequest);
    }

}


