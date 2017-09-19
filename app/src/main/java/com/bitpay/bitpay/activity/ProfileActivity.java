package com.bitpay.bitpay.activity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
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
import com.bitpay.bitpay.application.BitPayApplication;
import com.bitpay.bitpay.models.PancardModel;
import com.bitpay.bitpay.models.ProfileDetailsModel;
import com.bitpay.bitpay.network.BaseVolleyRequest;
import com.bitpay.bitpay.utils.FontUtils;
import com.bitpay.bitpay.utils.Helper;
import com.bitpay.bitpay.utils.SharedPreferenceUtils;
import com.bitpay.bitpay.utils.Validator;
import com.bitpay.bitpay.widgets.CircularImageView;
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

public class ProfileActivity extends BaseActivity {

    private TextView lblBitcoinTV;
    private TextView textBitcoinTV;
    private Button buttonCopy;
    private TextView resetPinTV;
    private EditText editName;
    private EditText editEmail;
    private Button btnSubmit;
    private ImageView uploadProfileImage;
    private CircularImageView circularProfileImage;

    private int bytesAvailable;
    private int bytesRead;
    private String mimeType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
    }

    @Override
    protected void initViews() {
        settingTitle(getString(R.string.title_profile));
        resetPinTV = (TextView) findViewById(R.id.resetPinTV);
        lblBitcoinTV = (TextView) findViewById(R.id.lblBitcoinTV);
        textBitcoinTV = (TextView) findViewById(R.id.textBitcoinTV);
        buttonCopy = (Button) findViewById(R.id.buttonCopy);
        editName = (EditText) findViewById(R.id.editName);
        editEmail = (EditText) findViewById(R.id.editEmail);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        uploadProfileImage = (ImageView) findViewById(R.id.uploadProfileImage);
        circularProfileImage = (CircularImageView) findViewById(R.id.circularProfileImage);

        FontUtils.changeFont(context, editName, FONT_ROBOTO_REGULAR);
        FontUtils.changeFont(context, editEmail, FONT_ROBOTO_REGULAR);
        FontUtils.changeFont(context, btnSubmit, FONT_CORBEL_REGULAR);
        FontUtils.changeFont(context, buttonCopy, FONT_CORBEL_REGULAR);
        FontUtils.changeFont(context, lblBitcoinTV, FONT_CORBEL_REGULAR);
        FontUtils.changeFont(context, textBitcoinTV, FONT_ROBOTO_REGULAR);
        FontUtils.changeFont(context, resetPinTV, FONT_CORBEL_REGULAR);
        textBitcoinTV.setText(SharedPreferenceUtils.getInstance(context).getString(USER_BIT_ADDRESS));
        toSetProfileDetails();
    }

    @Override
    protected void initContext() {
        context = ProfileActivity.this;
        currentActivity = ProfileActivity.this;
    }

    @Override
    protected void initListners() {
        buttonCopy.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);
        uploadProfileImage.setOnClickListener(this);
        resetPinTV.setOnClickListener(this);
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
            case R.id.buttonCopy: {
                android.content.ClipboardManager clipboard = (android.content.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                android.content.ClipData clip = android.content.ClipData.newPlainText("Text Label", textBitcoinTV.getText().toString());
                clipboard.setPrimaryClip(clip);
                toast("Copied to Clipboard !", true);
                break;
            }
            case R.id.btnSubmit: {
                toHideKeyboard();
                int emailStatus = SharedPreferenceUtils.getInstance(context).getInteger(KEY_EMAIL_STATUS);
                if (emailStatus == STATUS_VERYFIED) {
                    return;
                }
                if (Validator.isNetworkAvailable(currentActivity)) {
                    if (isMandatoryFields()) {
                        updateProfile();
                    }
                } else {
                    alert(currentActivity, getResources().getString(R.string.alert_message_no_network), getResources().getString(R.string.alert_message_no_network), getResources().getString(R.string.alert_ok_button_text_no_network), getResources().getString(R.string.alert_cancel_button_text_no_network), false, false, ALERT_TYPE_NO_NETWORK);
                }
                break;
            }
            case R.id.uploadProfileImage: {
                toHideKeyboard();
                if (Validator.isNetworkAvailable(currentActivity)) {
                    selectImage();
                } else {
                    alert(currentActivity, getResources().getString(R.string.alert_message_no_network), getResources().getString(R.string.alert_message_no_network), getResources().getString(R.string.alert_ok_button_text_no_network), getResources().getString(R.string.alert_cancel_button_text_no_network), false, false, ALERT_TYPE_NO_NETWORK);
                }
                break;
            }
            case R.id.resetPinTV: {
                if (bundle == null) {
                    bundle = new Bundle();
                }
                bundle.putInt(KEY_TYPE, TYPE_RESET_PIN);
                startActivity(currentActivity, ExistingConfirmPinActivity.class, bundle, false, REQUEST_TAG_NO_RESULT, true, ANIMATION_SLIDE_UP);
                break;
            }
        }
    }

    @Override
    public void onAlertClicked(int alertType) {

    }

    private void toSetProfileDetails() {

        editName.setText(SharedPreferenceUtils.getInstance(context).getString(USER_NAME));
        editEmail.setText(SharedPreferenceUtils.getInstance(context).getString(USER_EMAIL));
        String userImage = SharedPreferenceUtils.getInstance(context).getString(USER_PROFILE_IMAGE);
        if (!TextUtils.isEmpty(userImage)) {
            String imageUrl = BASE_URL_IMAGES + userImage;
            Picasso.with(currentActivity).load(imageUrl).error(R.drawable.logo_drawer).into(circularProfileImage);
            ProfileDetailsModel.getInstance().setUserImageName(userImage);
        }
        int emailStatus = SharedPreferenceUtils.getInstance(context).getInteger(KEY_EMAIL_STATUS);
        if (emailStatus == STATUS_VERYFIED) {
            editName.setFocusable(false);
            editName.setClickable(false);
            editEmail.setFocusable(false);
            editName.setClickable(false);
            btnSubmit.setOnClickListener(null);
            btnSubmit.setClickable(false);
            btnSubmit.setAlpha(0.5f);
        }

    }


    private boolean isMandatoryFields() {

        editName.setError(null);
        editEmail.setError(null);

        if (editName.getText().toString().isEmpty()) {
            editName.setError(getResources().getString(R.string.error_empty_name));
            editName.requestFocus();
            return false;
        } else if (!Validator.getInstance().isValidEmail(context, editEmail.getText().toString()).equals("")) {
            String emailError = Validator.getInstance().isValidEmail(context, editEmail.getText().toString());
            editEmail.setError(emailError);
            editEmail.requestFocus();
            return false;
        }
        initProfileDetailsModel();
        return true;
    }

    private void initProfileDetailsModel() {
        ProfileDetailsModel.getInstance().setUserId(SharedPreferenceUtils.getInstance(context).getInteger(USER_ID));
        ProfileDetailsModel.getInstance().setName(editName.getText().toString());
        ProfileDetailsModel.getInstance().setEmailId(editEmail.getText().toString());
        if (ProfileDetailsModel.getInstance().getUserImageName() == null) {
            ProfileDetailsModel.getInstance().setUserImageName("");
        }
    }

    private void updateProfile() {

        progressDialog(context, context.getString(R.string.pdialog_message_loading), context.getString(R.string.pdialog_message_loading), false, false);

        JSONObject jsonUpdateUser = null;
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        try {
            jsonUpdateUser = new JSONObject(gson.toJson(ProfileDetailsModel.getInstance()));
            Log.e("jsonUpdateUser", jsonUpdateUser.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final String URL_UPDATE_USER = UPDATE_USER_DETAILS_URL;
        JsonObjectRequest profileUpdateRequest = new JsonObjectRequest(Request.Method.POST, URL_UPDATE_USER, jsonUpdateUser, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                cancelProgressDialog();
                try {
                    logTesting(getResources().getString(R.string.nwk_response_edit_profile), response.toString(), Log.ERROR);
                    String message = response.getString(RESPONCE_MESSAGE);
                    if (response.getBoolean(RESPONCE_ERROR)) {
                        alert(currentActivity, getString(R.string.message_profile_update_fail), getString(R.string.message_profile_update_fail), getResources().getString(R.string.alert_ok_button_text_no_network), getResources().getString(R.string.alert_cancel_button_text_no_network), false, false, ALERT_TYPE_NO_NETWORK);
                    } else {
                        SharedPreferenceUtils.getInstance(currentActivity).putString(USER_NAME, editName.getText().toString());
                        SharedPreferenceUtils.getInstance(currentActivity).putString(USER_EMAIL, editEmail.getText().toString());
                        toast(getResources().getString(R.string.message_profile_updated), true);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                cancelProgressDialog();
                toast(getResources().getString(R.string.nwk_error_edit_profile), true);
                logTesting(getResources().getString(R.string.nwk_error_edit_profile), error.toString(), Log.ERROR);

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return super.getHeaders();
            }
        };

        BitPayApplication.getInstance().addToRequestQueue(profileUpdateRequest);
    }

    private void selectImage() {
        try {
            if (checkPermission()) {
                openChooseImageDialog();
            } else {
                requestPermission();
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

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case CAMERA_PERMISSION_REQUEST: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openChooseImageDialog();
                }
                break;
            }
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
                    circularProfileImage.setImageBitmap(imageBitmap);
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
                        circularProfileImage.setImageBitmap(imageBitmap);
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
        final byte[] imageBytesArray = Helper.getImageBytes(((BitmapDrawable) circularProfileImage.getDrawable()).getBitmap());
        Long timeMillis = System.currentTimeMillis();
        final String fileName = "Image_" + timeMillis.toString() + ".jpg";


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
                        alert(currentActivity, getString(R.string.message_image_upload_fail), getString(R.string.message_image_upload_fail), getResources().getString(R.string.alert_ok_button_text_no_network), getResources().getString(R.string.alert_cancel_button_text_no_network), false, false, ALERT_TYPE_NO_NETWORK);
                    } else {
                        SharedPreferenceUtils.getInstance(context).putString(USER_PROFILE_IMAGE, fileName);
                        ProfileDetailsModel.getInstance().setUserId(SharedPreferenceUtils.getInstance(context).getInteger(USER_ID));
                        ProfileDetailsModel.getInstance().setUserImageName(fileName);
                        ProfileDetailsModel.getInstance().setName(editName.getText().toString());
                        ProfileDetailsModel.getInstance().setEmailId(editEmail.getText().toString());
                        toast(getString(R.string.message_image_upload_suc), true);
                        updateProfile();
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

}
