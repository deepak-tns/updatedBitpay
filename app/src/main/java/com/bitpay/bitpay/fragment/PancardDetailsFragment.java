package com.bitpay.bitpay.fragment;


import android.Manifest;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatSpinner;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static android.app.Activity.RESULT_OK;

public class PancardDetailsFragment extends BaseFragment {

    private LinearLayout pancardImageLL;
    private TextView statusTV;
    private ImageView pancardImageIV;
    private ImageView uploadPanIV;
    private TextView lblPancardTV;
    private EditText editFirstName;
    private EditText editLastName;
    private EditText editPanNumber;
    private EditText editBirthDate;
    private Button btnSubmit;
    private TextView note1TV;
    private TextView note2TV;
    private TextView note3TV;
    private TextView note4TV;
    private TextView note5TV;
    private AppCompatSpinner genderSpinner;

    private Calendar mCalendar;
    private int bytesAvailable;
    private int bytesRead;
    private String mimeType;
    private PancardModel pancardModel;

    public PancardDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return view = inflater.inflate(R.layout.fragment_pancard_details, container, false);
    }


    @Override
    public void alertOkClicked(int alertType) {

    }

    @Override
    protected void initViews() {
        pancardImageLL = (LinearLayout) view.findViewById(R.id.pancardImageLL);
        statusTV = (TextView) view.findViewById(R.id.statusTV);
        pancardImageIV = (ImageView) view.findViewById(R.id.pancardImageIV);
        uploadPanIV = (ImageView) view.findViewById(R.id.uploadPanIV);
        lblPancardTV = (TextView) view.findViewById(R.id.lblPancardTV);
        editFirstName = (EditText) view.findViewById(R.id.editFirstName);
        editLastName = (EditText) view.findViewById(R.id.editLastName);
        editPanNumber = (EditText) view.findViewById(R.id.editPanNumber);
        editBirthDate = (EditText) view.findViewById(R.id.editBirthDate);
        btnSubmit = (Button) view.findViewById(R.id.btnSubmit);
        note1TV = (TextView) view.findViewById(R.id.note1TV);
        note2TV = (TextView) view.findViewById(R.id.note2TV);
        note3TV = (TextView) view.findViewById(R.id.note3TV);
        note4TV = (TextView) view.findViewById(R.id.note4TV);
        note5TV = (TextView) view.findViewById(R.id.note5TV);

        genderSpinner = (AppCompatSpinner) view.findViewById(R.id.genderSpinner);
        String[] items = getResources().getStringArray(R.array.gender);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(currentActivity, android.R.layout.simple_spinner_dropdown_item, items);
        genderSpinner.setAdapter(adapter);
        mCalendar = Calendar.getInstance();

        FontUtils.changeFont(context, lblPancardTV, FONT_CORBEL_REGULAR);
        FontUtils.changeFont(context, editFirstName, FONT_ROBOTO_REGULAR);
        FontUtils.changeFont(context, editLastName, FONT_ROBOTO_REGULAR);
        FontUtils.changeFont(context, editPanNumber, FONT_ROBOTO_REGULAR);
        FontUtils.changeFont(context, editBirthDate, FONT_ROBOTO_REGULAR);
        FontUtils.changeFont(context, btnSubmit, FONT_CORBEL_REGULAR);
        FontUtils.changeFont(context, note1TV, FONT_CORBEL_REGULAR);
        FontUtils.changeFont(context, note2TV, FONT_CORBEL_REGULAR);
        FontUtils.changeFont(context, note3TV, FONT_CORBEL_REGULAR);
        FontUtils.changeFont(context, note4TV, FONT_CORBEL_REGULAR);
        FontUtils.changeFont(context, note5TV, FONT_CORBEL_REGULAR);
        FontUtils.changeFont(context, statusTV, FONT_CORBEL_REGULAR);
        getPancardDetail();
    }

    @Override
    protected void initContext() {
        context = getActivity();
        currentActivity = getActivity();
    }

    @Override
    protected void initListners() {
        editBirthDate.setOnClickListener(this);
        uploadPanIV.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.editBirthDate: {
                DatePickerDialog dpDialog = new DatePickerDialog(currentActivity, date, mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH), mCalendar.get(Calendar.DAY_OF_MONTH));
                dpDialog.getDatePicker().setMaxDate(mCalendar.getTimeInMillis());
                dpDialog.show();
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
            case R.id.btnSubmit: {
                toHideKeyboard();
                if (Validator.getInstance().isNetworkAvailable(currentActivity)) {
                    if (isMandatoryFields()) {
                        addPancardDetails();
                    }
                } else {
                    alert(currentActivity, getResources().getString(R.string.alert_message_no_network), getResources().getString(R.string.alert_message_no_network), getResources().getString(R.string.alert_ok_button_text_no_network), getResources().getString(R.string.alert_cancel_button_text_no_network), false, false, ALERT_TYPE_NO_NETWORK);
                }
                break;
            }
        }
    }

    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            mCalendar.set(Calendar.YEAR, year);
            mCalendar.set(Calendar.MONTH, monthOfYear);
            mCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            String myFormat = "dd/MM/yyyy";
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
            editBirthDate.setText(sdf.format(mCalendar.getTime()));
        }

    };

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

    private boolean isMandatoryFields() {

        editFirstName.setError(null);
        editLastName.setError(null);
        editPanNumber.setError(null);
        editBirthDate.setError(null);

        if (TextUtils.isEmpty(PancardModel.getInstance().getImageName())) {
            alert(currentActivity, "", getString(R.string.error_empty_image), getResources().getString(R.string.alert_ok_button_text_no_network), getResources().getString(R.string.alert_cancel_button_text_no_network), false, false, ALERT_TYPE_NO_NETWORK);
            return false;
        } else if (editFirstName.getText().toString().isEmpty()) {
            editFirstName.setError(getResources().getString(R.string.error_empty_first_name));
            editFirstName.requestFocus();
            return false;
        } else if (editLastName.getText().toString().isEmpty()) {
            editLastName.setError(getResources().getString(R.string.error_empty_last_name));
            editLastName.requestFocus();
            return false;
        } else if (editPanNumber.getText().toString().isEmpty()) {
            editPanNumber.setError(getResources().getString(R.string.error_empty_pan_number));
            editPanNumber.requestFocus();
            return false;
        } else if (editPanNumber.getText().toString().length() < MIN_PAN_NUMBER_LENGTH) {
            editPanNumber.setError(getResources().getString(R.string.error_invalid_pan_number));
            editPanNumber.requestFocus();
            return false;
        } else if (genderSpinner.getSelectedItem().toString().equalsIgnoreCase(getString(R.string.hint_gender))) {
            alert(currentActivity, "", getString(R.string.error_empty_gender), getResources().getString(R.string.alert_ok_button_text_no_network), getResources().getString(R.string.alert_cancel_button_text_no_network), false, false, ALERT_TYPE_NO_NETWORK);
            return false;
        }
        initPancardDetailsModel();
        return true;

    }

    private void initPancardDetailsModel() {
        PancardModel.getInstance().setUserId(SharedPreferenceUtils.getInstance(context).getInteger(USER_ID));
        PancardModel.getInstance().setFirstName(editFirstName.getText().toString());
        PancardModel.getInstance().setLastName(editLastName.getText().toString());
        PancardModel.getInstance().setPanCardNumber(editPanNumber.getText().toString());
        PancardModel.getInstance().setDob(editBirthDate.getText().toString());
        PancardModel.getInstance().setGender(genderSpinner.getSelectedItem().toString());
        //  PancardModel.getInstance().setImageName("pancard");
    }

    public void uploadImageByVolley() {

        progressDialog(context, context.getString(R.string.pdialog_message_loading), context.getString(R.string.pdialog_message_loading), false, false);


        final String lineEnd = "\r\n";
        final String twoHyphens = "--";
        final String boundary = "*****";

        final int maxBufferSize = 1 * 1024 * 1024;
        final byte[] imageBytesArray = Helper.getImageBytes(((BitmapDrawable) pancardImageIV.getDrawable()).getBitmap());
        Long timeMillis = System.currentTimeMillis();
        final String fileName = "PanCardImage_" + timeMillis.toString() + ".jpg";
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
                        PancardModel.getInstance().setImageName(fileName);
                        toast(context, getString(R.string.message_image_upload_suc));
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

    private void addPancardDetails() {

        progressDialog(context, context.getString(R.string.pdialog_message_loading), context.getString(R.string.pdialog_message_loading), false, false);

        JSONObject jsonAddPancardDetailsRequest = null;
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        try {
            jsonAddPancardDetailsRequest = new JSONObject(gson.toJson(PancardModel.getInstance()));
            Log.e("jsonAddPancardRequest", jsonAddPancardDetailsRequest.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final String URL_ADD_PAN_DETAILS = ADD_PAN_DETAILS_URL;
        JsonObjectRequest addPancardDetailRequest = new JsonObjectRequest(Request.Method.POST, URL_ADD_PAN_DETAILS, jsonAddPancardDetailsRequest, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    cancelProgressDialog();
                    ((BaseActivity) currentActivity).logTesting(getResources().getString(R.string.nwk_response_add_pan_detail), response.toString(), Log.ERROR);
                    String message = response.getString(RESPONCE_MESSAGE);
                    if (response.getBoolean(RESPONCE_ERROR) || message.isEmpty()) {
                        alert(currentActivity, getString(R.string.msg_add_pan_details_failed), getString(R.string.msg_add_pan_details_failed), getResources().getString(R.string.alert_ok_button_text_no_network), getResources().getString(R.string.alert_cancel_button_text_no_network), false, false, ALERT_TYPE_NO_NETWORK);
                    } else {
                        toast(context, getResources().getString(R.string.msg_add_pan_details_suc));
                        GsonBuilder builder = new GsonBuilder();
                        Gson gson = builder.create();
                        pancardModel = gson.fromJson(message.toString(), PancardModel.class);
                        displayPancardDetails();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                cancelProgressDialog();
                toast(context, getResources().getString(R.string.nwk_error_add_pan_detail));
                ((BaseActivity) currentActivity).logTesting(getResources().getString(R.string.nwk_error_add_pan_detail), error.toString(), Log.ERROR);
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return super.getHeaders();
            }
        };
        BitPayApplication.getInstance().addToRequestQueue(addPancardDetailRequest);
    }

    private void getPancardDetail() {

        int userId = SharedPreferenceUtils.getInstance(context).getInteger(USER_ID);

        progressDialog(context, context.getString(R.string.pdialog_message_loading), context.getString(R.string.pdialog_message_loading), false, false);

        JSONObject jsonGetPancardDetails = null;
        String URL_GET_PAN_DETAIL = GET_PAN_DETAIL_URL;
        try {
            jsonGetPancardDetails = new JSONObject();
            jsonGetPancardDetails.put(KEY_USER_ID, userId);
            Log.e("jsonGetPancardDetails", jsonGetPancardDetails.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest getGetPancardDetailRequest = new JsonObjectRequest(Request.Method.POST, URL_GET_PAN_DETAIL, jsonGetPancardDetails, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                cancelProgressDialog();
                try {
                    ((BaseActivity) currentActivity).logTesting(currentActivity.getResources().getString(R.string.nwk_response_get_pan_details), response.toString(), Log.ERROR);
                    String message = response.getString(RESPONCE_MESSAGE);
                    if (!response.getBoolean(RESPONCE_ERROR)) {
                        GsonBuilder builder = new GsonBuilder();
                        Gson gson = builder.create();
                        pancardModel = gson.fromJson(message.toString(), PancardModel.class);
                        displayPancardDetails();
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
                ((BaseActivity) currentActivity).logTesting(currentActivity.getResources().getString(R.string.nwk_error_get_pan_details), error.toString(), Log.ERROR);

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return super.getHeaders();
            }
        };

        BitPayApplication.getInstance().addToRequestQueue(getGetPancardDetailRequest);
    }

    private void displayPancardDetails() {
        if (pancardModel == null) return;
        if (!TextUtils.isEmpty(pancardModel.getImageName())) {
            pancardImageIV.setVisibility(View.VISIBLE);
            String imageUrl = BASE_URL_IMAGES + pancardModel.getImageName();
            Picasso.with(currentActivity).load(imageUrl).into(pancardImageIV);
            PancardModel.getInstance().setImageName(pancardModel.getImageName());
        }
        editFirstName.setText(pancardModel.getFirstName());
        editLastName.setText(pancardModel.getLastName());
        editPanNumber.setText(pancardModel.getPanCardNumber());
        editBirthDate.setText(pancardModel.getDob());
        if (pancardModel.getGender().equalsIgnoreCase("Male")) {
            genderSpinner.setSelection(1);
        } else if (pancardModel.getGender().equalsIgnoreCase("Female")) {
            genderSpinner.setSelection(2);
        } else {
            genderSpinner.setSelection(0);
        }
        statusTV.setText(pancardModel.getStatus());
        if (pancardModel.getStatus().equalsIgnoreCase(STATUS_REJECTED)) {
            btnSubmit.setText(getString(R.string.btn_update));
        } else if (pancardModel.getStatus().equalsIgnoreCase(STATUS_PENDING) || pancardModel.getStatus().equalsIgnoreCase(STATUS_APPROVED)) {
            btnSubmit.setEnabled(false);
            btnSubmit.setAlpha(0.5f);
            uploadPanIV.setEnabled(false);
            editFirstName.setFocusable(false);
            editLastName.setFocusable(false);
            editPanNumber.setFocusable(false);
            editBirthDate.setOnClickListener(null);
            genderSpinner.setEnabled(false);
        }
        if (pancardModel.getStatus().equalsIgnoreCase(STATUS_REJECTED)) {
            statusTV.setTextColor(ContextCompat.getColor(context, R.color.colorReject));
        } else if (pancardModel.getStatus().equalsIgnoreCase(STATUS_PENDING)) {
            statusTV.setTextColor(ContextCompat.getColor(context, R.color.colorPending));
        } else if (pancardModel.getStatus().equalsIgnoreCase(STATUS_APPROVED)) {
            statusTV.setTextColor(ContextCompat.getColor(context, R.color.colorApproved));
        }

        SharedPreferenceUtils.getInstance(context).putString(KEY_PAN_STATUS, pancardModel.getStatus());

    }

}
