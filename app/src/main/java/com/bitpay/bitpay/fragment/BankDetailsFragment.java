package com.bitpay.bitpay.fragment;


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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bitpay.bitpay.R;
import com.bitpay.bitpay.activity.BaseActivity;
import com.bitpay.bitpay.application.BitPayApplication;
import com.bitpay.bitpay.models.BankDetailsModel;
import com.bitpay.bitpay.models.PancardModel;
import com.bitpay.bitpay.network.BaseVolleyRequest;
import com.bitpay.bitpay.utils.FontUtils;
import com.bitpay.bitpay.utils.Helper;
import com.bitpay.bitpay.utils.SharedPreferenceUtils;
import com.bitpay.bitpay.utils.Validator;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import static android.app.Activity.RESULT_OK;

public class BankDetailsFragment extends BaseFragment {

    private TextView lblBankAccountTV;
    private TextView statusTV;
    private EditText editIfscCode;
    private EditText editBankName;
    private EditText editBranchName;
    private EditText editAccountHolderName;
    private EditText editAccountNumber;
    private EditText editConfAccountNumber;
    private Button btnSubmit;
    private TextView note1TV;
    private TextView note2TV;
    private TextView note3TV;
    private ImageView uploadPanIV;
    private ImageView pancardImageIV;
    private BankDetailsModel bankDetailsModel;
    private int bytesAvailable;
    private int bytesRead;
    private String mimeType;

    public BankDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return view = inflater.inflate(R.layout.fragment_bank_details, container, false);
    }

    @Override
    public void alertOkClicked(int alertType) {

    }

    @Override
    protected void initViews() {
        pancardImageIV = (ImageView) view.findViewById(R.id.pancardImageIV);
        uploadPanIV = (ImageView) view.findViewById(R.id.uploadPanIV);
        lblBankAccountTV = (TextView) view.findViewById(R.id.lblBankAccountTV);
        statusTV = (TextView) view.findViewById(R.id.statusTV);
        editIfscCode = (EditText) view.findViewById(R.id.editIfscCode);
        editBankName = (EditText) view.findViewById(R.id.editBankName);
        editBranchName = (EditText) view.findViewById(R.id.editBranchName);
        editAccountHolderName = (EditText) view.findViewById(R.id.editAccountHolderName);
        editAccountNumber = (EditText) view.findViewById(R.id.editAccountNumber);
        editConfAccountNumber = (EditText) view.findViewById(R.id.editConfAccountNumber);
        btnSubmit = (Button) view.findViewById(R.id.btnSubmit);
        note1TV = (TextView) view.findViewById(R.id.note1TV);
        note2TV = (TextView) view.findViewById(R.id.note2TV);
        note3TV = (TextView) view.findViewById(R.id.note3TV);

        FontUtils.changeFont(context, lblBankAccountTV, FONT_CORBEL_REGULAR);
        FontUtils.changeFont(context, editIfscCode, FONT_ROBOTO_REGULAR);
        FontUtils.changeFont(context, editBankName, FONT_ROBOTO_REGULAR);
        FontUtils.changeFont(context, editBranchName, FONT_ROBOTO_REGULAR);
        FontUtils.changeFont(context, editAccountHolderName, FONT_ROBOTO_REGULAR);
        FontUtils.changeFont(context, editAccountNumber, FONT_ROBOTO_REGULAR);
        FontUtils.changeFont(context, editConfAccountNumber, FONT_ROBOTO_REGULAR);
        FontUtils.changeFont(context, btnSubmit, FONT_CORBEL_REGULAR);
        FontUtils.changeFont(context, note1TV, FONT_ROBOTO_REGULAR);
        FontUtils.changeFont(context, note2TV, FONT_ROBOTO_REGULAR);
        FontUtils.changeFont(context, note3TV, FONT_ROBOTO_REGULAR);
        FontUtils.changeFont(context, statusTV, FONT_CORBEL_REGULAR);
        getBankDetail();
    }

    @Override
    protected void initContext() {
        context = getActivity();
        currentActivity = getActivity();
    }

    @Override
    protected void initListners() {
        btnSubmit.setOnClickListener(this);
        uploadPanIV.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSubmit: {
                toHideKeyboard();
                if (Validator.getInstance().isNetworkAvailable(currentActivity)) {
                    if (isMandatoryFields()) {
                        addBankDetails();
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
                        Bitmap imageBitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filePath);
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

    public void uploadImageByVolley() {

        progressDialog(context, context.getString(R.string.pdialog_message_loading), context.getString(R.string.pdialog_message_loading), false, false);


        final String lineEnd = "\r\n";
        final String twoHyphens = "--";
        final String boundary = "*****";

        final int maxBufferSize = 1 * 1024 * 1024;
        final byte[] imageBytesArray = Helper.getImageBytes(((BitmapDrawable) pancardImageIV.getDrawable()).getBitmap());
        Long timeMillis = System.currentTimeMillis();
        final String fileName = "BankAccountImage_" + timeMillis.toString() + ".jpg";
        BankDetailsModel.getInstance().setImageName(fileName);

        String imageUploadUrl = UPLOAD_NEW_IMAGE;
        BaseVolleyRequest uploadProfileImageRequest = new BaseVolleyRequest(1, imageUploadUrl, new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {

                try {
                    String jsonString = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
                    Log.e("image response  is", response.toString() + jsonString);
                    JSONObject jsonObject = new JSONObject(jsonString);
                    if (jsonObject.getBoolean(RESPONCE_ERROR)) {
                        cancelProgressDialog();
                        pancardImageIV.setVisibility(View.GONE);
                        alert(currentActivity, getString(R.string.message_image_upload_fail), getString(R.string.message_image_upload_fail), getResources().getString(R.string.alert_ok_button_text_no_network), getResources().getString(R.string.alert_cancel_button_text_no_network), false, false, ALERT_TYPE_NO_NETWORK);
                    } else {
                        pancardImageIV.setVisibility(View.VISIBLE);
                        BankDetailsModel.getInstance().setImageName(fileName);
                        updateBankImage(fileName);
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
                toast(context, getString(R.string.error_uploading_image));
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

    private boolean isMandatoryFields() {

        editIfscCode.setError(null);
        editBankName.setError(null);
        editBranchName.setError(null);
        editAccountHolderName.setError(null);
        editAccountNumber.setError(null);
        editConfAccountNumber.setError(null);

        if (editIfscCode.getText().toString().isEmpty()) {
            editIfscCode.setError(getResources().getString(R.string.error_empty_ifsc_code));
            editIfscCode.requestFocus();
            return false;
        } else if (editBankName.getText().toString().isEmpty()) {
            editBankName.setError(getResources().getString(R.string.error_empty_bank_name));
            editBankName.requestFocus();
            return false;
        } else if (editBranchName.getText().toString().isEmpty()) {
            editBranchName.setError(getResources().getString(R.string.error_empty_branch_name));
            editBranchName.requestFocus();
            return false;
        } else if (editAccountHolderName.getText().toString().isEmpty()) {
            editAccountHolderName.setError(getResources().getString(R.string.error_empty_account_holder));
            editAccountHolderName.requestFocus();
            return false;
        } else if (editAccountNumber.getText().toString().isEmpty()) {
            editAccountNumber.setError(getResources().getString(R.string.error_empty_account_number));
            editAccountNumber.requestFocus();
            return false;
        } else if (editAccountNumber.getText().toString().length() < MIN_ACCOUNT_NUMBER_LENGTH) {
            editAccountNumber.setError(getResources().getString(R.string.error_minlength_account));
            editAccountNumber.requestFocus();
            return false;
        } else if (editConfAccountNumber.getText().toString().isEmpty() || !editAccountNumber.getText().toString().equals(editConfAccountNumber.getText().toString())) {
            editConfAccountNumber.setError(getResources().getString(R.string.error_conf_ac_notmatch));
            editConfAccountNumber.requestFocus();
            return false;
        }
        initBankDetailsModel();
        return true;

    }

    private void initBankDetailsModel() {
        BankDetailsModel.getInstance().setUserId(SharedPreferenceUtils.getInstance(context).getInteger(USER_ID));
        BankDetailsModel.getInstance().setIfscCode(editIfscCode.getText().toString());
        BankDetailsModel.getInstance().setBankName(editBankName.getText().toString());
        BankDetailsModel.getInstance().setBranchName(editBranchName.getText().toString());
        BankDetailsModel.getInstance().setAccountHolderName(editAccountHolderName.getText().toString());
        BankDetailsModel.getInstance().setAccountNumber(editAccountNumber.getText().toString());
    }

    private void addBankDetails() {

        progressDialog(context, context.getString(R.string.pdialog_message_loading), context.getString(R.string.pdialog_message_loading), false, false);

        JSONObject jsonAddBankDetailsRequest = null;
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        try {
            jsonAddBankDetailsRequest = new JSONObject(gson.toJson(BankDetailsModel.getInstance()));
            Log.e("jsonAddBankRequest", jsonAddBankDetailsRequest.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final String URL_ADD_BANK_DETAILS = ADD_BANK_DETAILS_URL;
        JsonObjectRequest addBankDetailRequest = new JsonObjectRequest(Request.Method.POST, URL_ADD_BANK_DETAILS, jsonAddBankDetailsRequest, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    cancelProgressDialog();
                    ((BaseActivity) currentActivity).logTesting(getResources().getString(R.string.nwk_response_add_bank_detail), response.toString(), Log.ERROR);
                    String message = response.getString(RESPONCE_MESSAGE);
                    if (response.getBoolean(RESPONCE_ERROR) || message.isEmpty()) {
                        alert(currentActivity, getString(R.string.msg_add_bank_details_failed), getString(R.string.msg_add_bank_details_failed), getResources().getString(R.string.alert_ok_button_text_no_network), getResources().getString(R.string.alert_cancel_button_text_no_network), false, false, ALERT_TYPE_NO_NETWORK);
                    } else {
                        toast(context, getResources().getString(R.string.msg_add_bank_details_suc));
                        GsonBuilder builder = new GsonBuilder();
                        Gson gson = builder.create();
                        bankDetailsModel = gson.fromJson(message.toString(), BankDetailsModel.class);
                        displayBankDetails();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                cancelProgressDialog();
                toast(context, getResources().getString(R.string.nwk_error_add_bank_detail));
                ((BaseActivity) currentActivity).logTesting(getResources().getString(R.string.nwk_error_add_bank_detail), error.toString(), Log.ERROR);
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return super.getHeaders();
            }
        };
        BitPayApplication.getInstance().addToRequestQueue(addBankDetailRequest);
    }

    private void updateBankImage(String imageName) {
        int userId = SharedPreferenceUtils.getInstance(context).getInteger(USER_ID);
        JSONObject jsonAddBankDetailsRequest = null;
        try {
            jsonAddBankDetailsRequest = new JSONObject();
            jsonAddBankDetailsRequest.put("user_id", userId);
            jsonAddBankDetailsRequest.put("image", imageName);
            Log.e("jsonUpdateBankRequest", jsonAddBankDetailsRequest.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final String URL_UPDATE_BANK_IMAGE = UPDATE_BANK_IMAGE_URL;
        JsonObjectRequest addBankDetailRequest = new JsonObjectRequest(Request.Method.POST, URL_UPDATE_BANK_IMAGE, jsonAddBankDetailsRequest, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    cancelProgressDialog();
                    String message = response.getString(RESPONCE_MESSAGE);
                    if (response.getBoolean(RESPONCE_ERROR) || message.isEmpty()) {
                        alert(currentActivity, getString(R.string.message_image_upload_fail), getString(R.string.message_image_upload_fail), getResources().getString(R.string.alert_ok_button_text_no_network), getResources().getString(R.string.alert_cancel_button_text_no_network), false, false, ALERT_TYPE_NO_NETWORK);
                    } else {
                        toast(context, getString(R.string.message_image_upload_suc));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                cancelProgressDialog();
                ((BaseActivity) currentActivity).logTesting(getResources().getString(R.string.nwk_error_add_bank_detail), error.toString(), Log.ERROR);
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return super.getHeaders();
            }
        };
        BitPayApplication.getInstance().addToRequestQueue(addBankDetailRequest);
    }

    private void getBankDetail() {

        int userId = SharedPreferenceUtils.getInstance(context).getInteger(USER_ID);

        progressDialog(context, context.getString(R.string.pdialog_message_loading), context.getString(R.string.pdialog_message_loading), false, false);

        JSONObject jsonGetBankDetails = null;
        String URL_GET_BANK_DETAIL = GET_BANK_DETAIL_URL;
        try {
            jsonGetBankDetails = new JSONObject();
            jsonGetBankDetails.put(KEY_USER_ID, userId);
            Log.e("jsonGetBankDetails", jsonGetBankDetails.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest getGetBankDetailRequest = new JsonObjectRequest(Request.Method.POST, URL_GET_BANK_DETAIL, jsonGetBankDetails, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                cancelProgressDialog();
                try {
                    ((BaseActivity) currentActivity).logTesting(currentActivity.getResources().getString(R.string.nwk_response_get_bank_details), response.toString(), Log.ERROR);
                    String message = response.getString(RESPONCE_MESSAGE);
                    if (!response.getBoolean(RESPONCE_ERROR)) {
                        GsonBuilder builder = new GsonBuilder();
                        Gson gson = builder.create();
                        bankDetailsModel = gson.fromJson(message.toString(), BankDetailsModel.class);
                        displayBankDetails();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                cancelProgressDialog();
                //toast(context, currentActivity.getResources().getString(R.string.nwk_error_get_contact));
                ((BaseActivity) currentActivity).logTesting(currentActivity.getResources().getString(R.string.nwk_error_get_bank_details), error.toString(), Log.ERROR);
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return super.getHeaders();
            }
        };

        BitPayApplication.getInstance().addToRequestQueue(getGetBankDetailRequest);
    }

    private void displayBankDetails() {
        if (bankDetailsModel == null) return;
        if (!TextUtils.isEmpty(bankDetailsModel.getImageName())) {
            pancardImageIV.setVisibility(View.VISIBLE);
            String imageUrl = BASE_URL_IMAGES + bankDetailsModel.getImageName();
            Picasso.with(currentActivity).load(imageUrl).into(pancardImageIV);
            BankDetailsModel.getInstance().setImageName(bankDetailsModel.getImageName());
        }
        editIfscCode.setText(bankDetailsModel.getIfscCode());
        editBankName.setText(bankDetailsModel.getBankName());
        editBranchName.setText(bankDetailsModel.getBranchName());
        editAccountHolderName.setText(bankDetailsModel.getAccountHolderName());
        editAccountNumber.setText(bankDetailsModel.getAccountNumber());
        editConfAccountNumber.setText(bankDetailsModel.getAccountNumber());
        statusTV.setText(bankDetailsModel.getStatus());
        if (bankDetailsModel.getStatus().equalsIgnoreCase(STATUS_REJECTED)) {
            btnSubmit.setText(getString(R.string.btn_update));
        } else if (bankDetailsModel.getStatus().equalsIgnoreCase(STATUS_PENDING) || bankDetailsModel.getStatus().equalsIgnoreCase(STATUS_APPROVED)) {
            btnSubmit.setEnabled(false);
            btnSubmit.setAlpha(0.5f);
            uploadPanIV.setEnabled(false);
            editIfscCode.setFocusable(false);
            editBankName.setFocusable(false);
            editBranchName.setFocusable(false);
            editAccountHolderName.setFocusable(false);
            editAccountNumber.setFocusable(false);
            editConfAccountNumber.setFocusable(false);
        }
        if (bankDetailsModel.getStatus().equalsIgnoreCase(STATUS_REJECTED)) {
            statusTV.setTextColor(ContextCompat.getColor(context, R.color.colorReject));
        } else if (bankDetailsModel.getStatus().equalsIgnoreCase(STATUS_PENDING)) {
            statusTV.setTextColor(ContextCompat.getColor(context, R.color.colorPending));
        } else if (bankDetailsModel.getStatus().equalsIgnoreCase(STATUS_APPROVED)) {
            statusTV.setTextColor(ContextCompat.getColor(context, R.color.colorApproved));
        }
        SharedPreferenceUtils.getInstance(context).putString(KEY_BANK_STATUS, bankDetailsModel.getStatus());
    }
}
