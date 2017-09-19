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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
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
import com.bitpay.bitpay.adapter.CityAdapter;
import com.bitpay.bitpay.adapter.ConnectionProviderAdapter;
import com.bitpay.bitpay.adapter.StateAdapter;
import com.bitpay.bitpay.application.BitPayApplication;
import com.bitpay.bitpay.models.BankDetailsModel;
import com.bitpay.bitpay.models.CityModel;
import com.bitpay.bitpay.models.KycDetailsModel;
import com.bitpay.bitpay.models.StateModel;
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
import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static android.app.Activity.RESULT_OK;

public class KycDetailsFragment extends BaseFragment {

    private TextView statusTV;
    private ImageView pancardImageIV;
    private ImageView uploadPanIV;
    private ImageView backImageIV;
    private ImageView uploadBackIV;
    private TextView lblPancardTV;
    private EditText editAddressLine1;
    private EditText editAddressLine2;
    private EditText editLandmark;
    private EditText editPincode;
    private Spinner stateSpinner;
    private Spinner citySpinner;
    private Button btnSubmit;
    private TextView note1TV;
    private TextView note2TV;
    private TextView note3TV;
    private TextView note4TV;
    private TextView note5TV;

    private ArrayList<StateModel> stateModelArrayList;
    private ArrayList<CityModel> cityModelArrayList;

    private int bytesAvailable;
    private int bytesRead;
    private String mimeType;
    private KycDetailsModel kycDetailsModel;
    private StateAdapter stateAdapter;
    private StateModel stateModel;
    private CityModel cityModel;
    private CityAdapter cityAdapter;

    private int imageType;

    public KycDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return view = inflater.inflate(R.layout.fragment_kyc_details, container, false);
    }


    @Override
    public void alertOkClicked(int alertType) {

    }

    @Override
    protected void initViews() {
        statusTV = (TextView) view.findViewById(R.id.statusTV);
        pancardImageIV = (ImageView) view.findViewById(R.id.pancardImageIV);
        uploadPanIV = (ImageView) view.findViewById(R.id.uploadPanIV);
        backImageIV = (ImageView) view.findViewById(R.id.backImageIV);
        uploadBackIV = (ImageView) view.findViewById(R.id.uploadBackIV);
        lblPancardTV = (TextView) view.findViewById(R.id.lblPancardTV);
        editAddressLine1 = (EditText) view.findViewById(R.id.editAddressLine1);
        editAddressLine2 = (EditText) view.findViewById(R.id.editAddressLine2);
        editLandmark = (EditText) view.findViewById(R.id.editLandmark);
        editPincode = (EditText) view.findViewById(R.id.editPincode);
        stateSpinner = (Spinner) view.findViewById(R.id.stateSpinner);
        citySpinner = (Spinner) view.findViewById(R.id.citySpinner);
        btnSubmit = (Button) view.findViewById(R.id.btnSubmit);
        note1TV = (TextView) view.findViewById(R.id.note1TV);
        note2TV = (TextView) view.findViewById(R.id.note2TV);
        note3TV = (TextView) view.findViewById(R.id.note3TV);
        note4TV = (TextView) view.findViewById(R.id.note4TV);
        note5TV = (TextView) view.findViewById(R.id.note5TV);

        stateModelArrayList = new ArrayList<>();
        stateAdapter = new StateAdapter(context, stateModelArrayList);
        stateSpinner.setAdapter(stateAdapter);

        cityModelArrayList = new ArrayList<>();
        cityAdapter = new CityAdapter(context, cityModelArrayList);
        citySpinner.setAdapter(cityAdapter);

        FontUtils.changeFont(context, lblPancardTV, FONT_CORBEL_REGULAR);
        FontUtils.changeFont(context, editAddressLine1, FONT_ROBOTO_REGULAR);
        FontUtils.changeFont(context, editAddressLine2, FONT_ROBOTO_REGULAR);
        FontUtils.changeFont(context, editLandmark, FONT_ROBOTO_REGULAR);
        FontUtils.changeFont(context, editPincode, FONT_ROBOTO_REGULAR);
        FontUtils.changeFont(context, btnSubmit, FONT_CORBEL_REGULAR);
        FontUtils.changeFont(context, note1TV, FONT_CORBEL_REGULAR);
        FontUtils.changeFont(context, note2TV, FONT_CORBEL_REGULAR);
        FontUtils.changeFont(context, note3TV, FONT_CORBEL_REGULAR);
        FontUtils.changeFont(context, note4TV, FONT_CORBEL_REGULAR);
        FontUtils.changeFont(context, note5TV, FONT_CORBEL_REGULAR);
        FontUtils.changeFont(context, statusTV, FONT_CORBEL_REGULAR);
        getKycDetail();
    }

    @Override
    protected void initContext() {
        context = getActivity();
        currentActivity = getActivity();
    }

    @Override
    protected void initListners() {
        uploadPanIV.setOnClickListener(this);
        uploadBackIV.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);

        stateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (stateModelArrayList != null && stateModelArrayList.size() > position) {
                    StateModel sModel = stateModelArrayList.get(position);
                    if (sModel == null) return;
                    stateModel = sModel;
                    getCityList();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        citySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (cityModelArrayList != null && cityModelArrayList.size() > position) {
                    CityModel cModel = cityModelArrayList.get(position);
                    if (cModel == null) return;
                    cityModel = cModel;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.uploadPanIV: {
                toHideKeyboard();
                if (Validator.isNetworkAvailable(currentActivity)) {
                    selectImage();
                    imageType = 1;
                } else {
                    alert(currentActivity, getResources().getString(R.string.alert_message_no_network), getResources().getString(R.string.alert_message_no_network), getResources().getString(R.string.alert_ok_button_text_no_network), getResources().getString(R.string.alert_cancel_button_text_no_network), false, false, ALERT_TYPE_NO_NETWORK);
                }
                break;
            }
            case R.id.uploadBackIV: {
                toHideKeyboard();
                if (Validator.isNetworkAvailable(currentActivity)) {
                    selectImage();
                    imageType = 2;
                } else {
                    alert(currentActivity, getResources().getString(R.string.alert_message_no_network), getResources().getString(R.string.alert_message_no_network), getResources().getString(R.string.alert_ok_button_text_no_network), getResources().getString(R.string.alert_cancel_button_text_no_network), false, false, ALERT_TYPE_NO_NETWORK);
                }
                break;
            }
            case R.id.btnSubmit: {
                toHideKeyboard();
                if (Validator.getInstance().isNetworkAvailable(currentActivity)) {
                    if (isMandatoryFields()) {
                        addKycDetails();
                    }
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
                    if (imageType == 1) {
                        pancardImageIV.setVisibility(View.VISIBLE);
                        pancardImageIV.setImageBitmap(imageBitmap);
                    } else if (imageType == 2) {
                        backImageIV.setVisibility(View.VISIBLE);
                        backImageIV.setImageBitmap(imageBitmap);
                    }
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
                        if (imageType == 1) {
                            pancardImageIV.setVisibility(View.VISIBLE);
                            pancardImageIV.setImageBitmap(imageBitmap);
                        } else if (imageType == 2) {
                            backImageIV.setVisibility(View.VISIBLE);
                            backImageIV.setImageBitmap(imageBitmap);
                        }
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

        editAddressLine1.setError(null);
        editAddressLine2.setError(null);
        editLandmark.setError(null);

        if (TextUtils.isEmpty(KycDetailsModel.getInstance().getImageName())) {
            alert(currentActivity, "", getString(R.string.error_empty_image), getResources().getString(R.string.alert_ok_button_text_no_network), getResources().getString(R.string.alert_cancel_button_text_no_network), false, false, ALERT_TYPE_NO_NETWORK);
            return false;
        } else if (TextUtils.isEmpty(KycDetailsModel.getInstance().getBackImage())) {
            alert(currentActivity, "", getString(R.string.error_empty_image), getResources().getString(R.string.alert_ok_button_text_no_network), getResources().getString(R.string.alert_cancel_button_text_no_network), false, false, ALERT_TYPE_NO_NETWORK);
            return false;
        } else if (editAddressLine1.getText().toString().isEmpty()) {
            editAddressLine1.setError(getResources().getString(R.string.error_empty_address));
            editAddressLine1.requestFocus();
            return false;
        } else if (editLandmark.getText().toString().isEmpty()) {
            editLandmark.setError(getResources().getString(R.string.error_empty_landmark));
            editLandmark.requestFocus();
            return false;
        } else if (editPincode.getText().toString().isEmpty()) {
            editPincode.setError(getResources().getString(R.string.error_empty_pan_number));
            editPincode.requestFocus();
            return false;
        }
        initPancardDetailsModel();
        return true;

    }

    private void initPancardDetailsModel() {
        KycDetailsModel.getInstance().setUserId(SharedPreferenceUtils.getInstance(context).getInteger(USER_ID));
        KycDetailsModel.getInstance().setAddressLine1(editAddressLine1.getText().toString());
        KycDetailsModel.getInstance().setAddressLine2(editAddressLine2.getText().toString());
        KycDetailsModel.getInstance().setLandmark(editLandmark.getText().toString());
        KycDetailsModel.getInstance().setCity(cityModel.getCityName());
        KycDetailsModel.getInstance().setState(stateModel.getStateName());
        KycDetailsModel.getInstance().setPinCode(editPincode.getText().toString());
    }

    public void uploadImageByVolley() {

        progressDialog(context, context.getString(R.string.pdialog_message_loading), context.getString(R.string.pdialog_message_loading), false, false);


        final String lineEnd = "\r\n";
        final String twoHyphens = "--";
        final String boundary = "*****";

        final int maxBufferSize = 1 * 1024 * 1024;
        Bitmap bitmap = null;//((BitmapDrawable) pancardImageIV.getDrawable()).getBitmap();
        if (imageType == 1) {
            bitmap = ((BitmapDrawable) pancardImageIV.getDrawable()).getBitmap();
        } else if (imageType == 2) {
            bitmap = ((BitmapDrawable) backImageIV.getDrawable()).getBitmap();
        }
        final byte[] imageBytesArray = Helper.getImageBytes(bitmap);
        Long timeMillis = System.currentTimeMillis();
        final String fileName = "KycImage_" + timeMillis.toString() + ".jpg";

        if (imageType == 1) {
            KycDetailsModel.getInstance().setImageName(fileName);
        } else if (imageType == 2) {
            KycDetailsModel.getInstance().setBackImage(fileName);
        }
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
                        if (imageType == 1) {
                            KycDetailsModel.getInstance().setImageName(fileName);
                        } else if (imageType == 2) {
                            KycDetailsModel.getInstance().setBackImage(fileName);
                        }
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

    private void addKycDetails() {

        progressDialog(context, context.getString(R.string.pdialog_message_loading), context.getString(R.string.pdialog_message_loading), false, false);

        JSONObject jsonAddPancardDetailsRequest = null;
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        try {
            jsonAddPancardDetailsRequest = new JSONObject(gson.toJson(KycDetailsModel.getInstance()));
            Log.e("jsonAddPancardRequest", jsonAddPancardDetailsRequest.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final String URL_ADD_PAN_DETAILS = ADD_KYC_DETAILS_URL;
        JsonObjectRequest addPancardDetailRequest = new JsonObjectRequest(Request.Method.POST, URL_ADD_PAN_DETAILS, jsonAddPancardDetailsRequest, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    cancelProgressDialog();
                    String message = response.getString(RESPONCE_MESSAGE);
                    if (response.getBoolean(RESPONCE_ERROR) || message.isEmpty()) {
                        alert(currentActivity, getString(R.string.msg_add_kyc_details_failed), getString(R.string.msg_add_pan_details_failed), getResources().getString(R.string.alert_ok_button_text_no_network), getResources().getString(R.string.alert_cancel_button_text_no_network), false, false, ALERT_TYPE_NO_NETWORK);
                    } else {
                        toast(context, getResources().getString(R.string.msg_add_kyc_details_suc));
                        GsonBuilder builder = new GsonBuilder();
                        Gson gson = builder.create();
                        kycDetailsModel = gson.fromJson(message.toString(), KycDetailsModel.class);
                        kycDetailsModel.setAddressLine1(KycDetailsModel.getInstance().getAddressLine1());
                        kycDetailsModel.setAddressLine2(KycDetailsModel.getInstance().getAddressLine2());
                        kycDetailsModel.setLandmark(KycDetailsModel.getInstance().getLandmark());
                        kycDetailsModel.setCity(KycDetailsModel.getInstance().getCity());
                        kycDetailsModel.setState(KycDetailsModel.getInstance().getState());
                        kycDetailsModel.setPinCode(KycDetailsModel.getInstance().getPinCode());
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
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return super.getHeaders();
            }
        };
        BitPayApplication.getInstance().addToRequestQueue(addPancardDetailRequest);
    }

    private void getKycDetail() {

        int userId = SharedPreferenceUtils.getInstance(context).getInteger(USER_ID);

        progressDialog(context, context.getString(R.string.pdialog_message_loading), context.getString(R.string.pdialog_message_loading), false, false);

        JSONObject jsonGetPancardDetails = null;
        String URL_GET_PAN_DETAIL = GET_KYC_DETAIL_URL;
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
                try {
                    String message = response.getString(RESPONCE_MESSAGE);
                    if (!response.getBoolean(RESPONCE_ERROR)) {
                        GsonBuilder builder = new GsonBuilder();
                        Gson gson = builder.create();
                        kycDetailsModel = gson.fromJson(message.toString(), KycDetailsModel.class);
                        displayPancardDetails();
                        getStateList();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                cancelProgressDialog();
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
        if (kycDetailsModel == null) return;
        if (!TextUtils.isEmpty(kycDetailsModel.getImageName())) {
            pancardImageIV.setVisibility(View.VISIBLE);
            String imageUrl1 = BASE_URL_IMAGES + kycDetailsModel.getImageName();
            Picasso.with(currentActivity).load(imageUrl1).into(pancardImageIV);
            KycDetailsModel.getInstance().setImageName(kycDetailsModel.getImageName());
        }
        if (!TextUtils.isEmpty(kycDetailsModel.getBackImage())) {
            backImageIV.setVisibility(View.VISIBLE);
            String imageUrl2 = BASE_URL_IMAGES + kycDetailsModel.getBackImage();
            Picasso.with(currentActivity).load(imageUrl2).into(backImageIV);
            KycDetailsModel.getInstance().setImageName(kycDetailsModel.getBackImage());
        }
        editAddressLine1.setText(kycDetailsModel.getAddressLine1());
        editAddressLine2.setText(kycDetailsModel.getAddressLine2());
        editLandmark.setText(kycDetailsModel.getLandmark());
        editPincode.setText(kycDetailsModel.getPinCode());
        statusTV.setText(kycDetailsModel.getStatus());
        if (kycDetailsModel.getStatus().equalsIgnoreCase(STATUS_REJECTED)) {
            btnSubmit.setText(getString(R.string.btn_update));
        } else if (kycDetailsModel.getStatus().equalsIgnoreCase(STATUS_PENDING) || kycDetailsModel.getStatus().equalsIgnoreCase(STATUS_APPROVED)) {
            btnSubmit.setEnabled(false);
            btnSubmit.setAlpha(0.5f);
            uploadPanIV.setEnabled(false);
            uploadBackIV.setEnabled(false);
            editAddressLine1.setFocusable(false);
            editAddressLine2.setFocusable(false);
            editLandmark.setFocusable(false);
            editPincode.setFocusable(false);
            stateSpinner.setEnabled(false);
            citySpinner.setEnabled(false);
        }
        if (kycDetailsModel.getStatus().equalsIgnoreCase(STATUS_REJECTED)) {
            statusTV.setTextColor(ContextCompat.getColor(context, R.color.colorReject));
        } else if (kycDetailsModel.getStatus().equalsIgnoreCase(STATUS_PENDING)) {
            statusTV.setTextColor(ContextCompat.getColor(context, R.color.colorPending));
        } else if (kycDetailsModel.getStatus().equalsIgnoreCase(STATUS_APPROVED)) {
            statusTV.setTextColor(ContextCompat.getColor(context, R.color.colorApproved));
        }

        SharedPreferenceUtils.getInstance(context).putString(KEY_KYC_STATUS, kycDetailsModel.getStatus());
    }

    private void getStateList() {

        progressDialog(context, context.getString(R.string.pdialog_message_loading), context.getString(R.string.pdialog_message_loading), false, false);

        JSONObject jsonGetPancardDetails = null;
        String URL_GET_STATE_LIST = GET_STATE_LIST_URL;
        try {
            jsonGetPancardDetails = new JSONObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        JsonObjectRequest getGetPancardDetailRequest = new JsonObjectRequest(Request.Method.POST, URL_GET_STATE_LIST, jsonGetPancardDetails, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String message = response.getString(RESPONCE_MESSAGE);
                    if (!response.getBoolean(RESPONCE_ERROR)) {
                        GsonBuilder builder = new GsonBuilder();
                        Gson gson = builder.create();
                        List<StateModel> stateModelList = Arrays.asList(gson.fromJson(message.toString(), StateModel[].class));
                        stateModelArrayList.clear();
                        stateModelArrayList.addAll(stateModelList);
                        stateAdapter.notifyDataSetChanged();

                        String state = kycDetailsModel.getState();
                        int selectedState = stateAdapter.getPossition(state);
                        stateSpinner.setSelection(selectedState);
                        stateModel = stateModelArrayList.get(selectedState);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                cancelProgressDialog();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return super.getHeaders();
            }
        };

        BitPayApplication.getInstance().addToRequestQueue(getGetPancardDetailRequest);
    }

    private void getCityList() {

        if (stateModel == null) return;
        progressDialog(context, context.getString(R.string.pdialog_message_loading), context.getString(R.string.pdialog_message_loading), false, false);

        JSONObject jsonGetPancardDetails = null;
        String URL_GET_CITY_LIST = GET_CITY_LIST_URL;
        try {
            jsonGetPancardDetails = new JSONObject();
            jsonGetPancardDetails.put("state", stateModel.getStateName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        JsonObjectRequest getGetPancardDetailRequest = new JsonObjectRequest(Request.Method.POST, URL_GET_CITY_LIST, jsonGetPancardDetails, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                cancelProgressDialog();
                try {
                    String message = response.getString(RESPONCE_MESSAGE);
                    if (!response.getBoolean(RESPONCE_ERROR)) {
                        GsonBuilder builder = new GsonBuilder();
                        Gson gson = builder.create();
                        List<CityModel> cityModelList = Arrays.asList(gson.fromJson(message.toString(), CityModel[].class));
                        cityModelArrayList.clear();
                        cityModelArrayList.addAll(cityModelList);
                        cityAdapter.notifyDataSetChanged();

                        String city = kycDetailsModel.getCity();
                        int selectedCity = cityAdapter.getPossition(city);
                        citySpinner.setSelection(selectedCity);
                        cityModel = cityModelArrayList.get(selectedCity);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                cancelProgressDialog();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return super.getHeaders();
            }
        };

        BitPayApplication.getInstance().addToRequestQueue(getGetPancardDetailRequest);
    }

}
