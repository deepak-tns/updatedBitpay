package com.bitpay.bitpay.activity;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.TypefaceSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bitpay.bitpay.R;
import com.bitpay.bitpay.adapter.ContactListAdapter;
import com.bitpay.bitpay.application.BitPayApplication;
import com.bitpay.bitpay.interfaces.RecyclerClickListner;
import com.bitpay.bitpay.models.AddContactModel;
import com.bitpay.bitpay.models.ContactModel;
import com.bitpay.bitpay.models.SendBitcoinsModel;
import com.bitpay.bitpay.utils.FontUtils;
import com.bitpay.bitpay.utils.SharedPreferenceUtils;
import com.bitpay.bitpay.utils.Validator;
import com.bitpay.bitpay.widgets.CustomTypefaceSpan;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class ContactBookActivity extends BaseActivity {

    private TextView addContactTV;
    private EditText editName;
    private EditText editBitAddress;
    private Button buttonAdd;
    private ImageView buttonScanIV;
    private TextView noContactTV;
    private RecyclerView contactRecyclerView;
    private List<ContactModel> contactModelList;
    private ContactListAdapter contactListAdapter;
    private String rateAmtString;
    private String bitCoinAmtString;
    private String estimatedNetworkFee;
    private ContactModel contactModel;

    @Override
    protected void initViews() {
        settingTitle(getString(R.string.title_contacts));
        contactModelList = new ArrayList<>();
        addContactTV = (TextView) findViewById(R.id.addContactTV);
        editName = (EditText) findViewById(R.id.editName);
        editBitAddress = (EditText) findViewById(R.id.editBitAddress);
        buttonAdd = (Button) findViewById(R.id.buttonAdd);
        buttonScanIV = (ImageView) findViewById(R.id.buttonScanIV);
        noContactTV = (TextView) findViewById(R.id.noContactTV);
        contactRecyclerView = (RecyclerView) findViewById(R.id.contactRecyclerView);
        FontUtils.changeFont(context, addContactTV, FONT_CORBEL_BOLD);
        FontUtils.changeFont(context, editName, FONT_ROBOTO_REGULAR);
        FontUtils.changeFont(context, editBitAddress, FONT_ROBOTO_REGULAR);
        FontUtils.changeFont(context, buttonAdd, FONT_CORBEL_REGULAR);
        FontUtils.changeFont(context, noContactTV, FONT_CORBEL_REGULAR);

        if (getIntent().getExtras() != null) {
            rateAmtString = getIntent().getExtras().getString(KEY_RATE_AMMOUNT);
            bitCoinAmtString = getIntent().getExtras().getString(KEY_BIT_AMMOUNT);
        }
        displayContactList();
        getContactList();
    }

    @Override
    protected void initContext() {
        context = ContactBookActivity.this;
        currentActivity = ContactBookActivity.this;
    }

    @Override
    protected void initListners() {
        buttonAdd.setOnClickListener(this);
        buttonScanIV.setOnClickListener(this);
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_book);
    }


    @Override
    public void onAlertClicked(int alertType) {
        if (alertType == ALERT_TYPE_SEND_BIT) {
            startActivity(currentActivity, DashboardActivity.class, bundle, false, REQUEST_TAG_NO_RESULT, true, ANIMATION_SLIDE_UP);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonAdd: {
                toHideKeyboard();
                if (Validator.getInstance().isNetworkAvailable(currentActivity)) {
                    if (isMandatoryFields()) {
                        addContact();
                    }
                } else {
                    alert(currentActivity, getResources().getString(R.string.alert_message_no_network), getResources().getString(R.string.alert_message_no_network), getResources().getString(R.string.alert_ok_button_text_no_network), getResources().getString(R.string.alert_cancel_button_text_no_network), true, false, ALERT_TYPE_NO_NETWORK);
                }
                break;
            }
            case R.id.buttonScanIV: {
                try {
                    if (checkPermission()) {
                        openScannerActivity();
                    } else {
                        requestPermission();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            }
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
                    openScannerActivity();
                }
                break;
            }
        }
    }

    private void openScannerActivity() {

        startActivity(currentActivity, QRcodeScannerActivity.class, bundle, true, REQUEST_TAG_SCANNER_ACTIVITY, true, ANIMATION_SLIDE_UP);
    }

    private void displayContactList() {
        contactListAdapter = new ContactListAdapter(context, contactModelList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);
        contactRecyclerView.setLayoutManager(mLayoutManager);
        contactRecyclerView.setItemAnimator(new DefaultItemAnimator());
        contactRecyclerView.setAdapter(contactListAdapter);

        contactListAdapter.setOnItemClickListener(new RecyclerClickListner() {
            @Override
            public void onItemClick(int pos, View v) {

                if (contactModelList != null && contactModelList.size() > pos) {
                    contactModel = contactModelList.get(pos);
                    if (contactModel == null) return;
                    if (bundle == null) {
                        bundle = new Bundle();
                    }
                    bundle.putInt(KEY_TYPE, TYPE_SEND_BITCOIN);
                    startActivity(currentActivity, ExistingConfirmPinActivity.class, bundle, true, REQUEST_TAG_CONFIRM_PIN, true, ANIMATION_SLIDE_UP);
                }
            }

            @Override
            public void onItemLongClick(int position, View v) {

            }
        });
    }


    private boolean isMandatoryFields() {

        editName.setError(null);
        editBitAddress.setError(null);

        if (editName.getText().toString().isEmpty()) {
            editName.setError(getString(R.string.error_empty_name));
            editName.requestFocus();
            return false;
        } else if (editBitAddress.getText().toString().isEmpty()) {
            editBitAddress.setError(getString(R.string.error_empty_bit_address));
            editBitAddress.requestFocus();
            return false;
        }
        initAddContactModel();
        return true;
    }

    private void initAddContactModel() {
        AddContactModel.getInstance().setContactName(editName.getText().toString());
        AddContactModel.getInstance().setBitAddress(editBitAddress.getText().toString().trim());
        AddContactModel.getInstance().setUserId(SharedPreferenceUtils.getInstance(context).getInteger(USER_ID));
    }

    private void addContact() {

        progressDialog(context, context.getString(R.string.pdialog_message_loading), context.getString(R.string.pdialog_message_loading), false, false);

        JSONObject jsonAddContactRequest = null;
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        try {
            jsonAddContactRequest = new JSONObject(gson.toJson(AddContactModel.getInstance()));
            Log.e("jsonAddContactRequest", jsonAddContactRequest.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final String URL_ADD_CONTACT = ADD_CONTACT_URL;
        JsonObjectRequest signUpRequest = new JsonObjectRequest(Request.Method.POST, URL_ADD_CONTACT, jsonAddContactRequest, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    logTesting(getResources().getString(R.string.nwk_response_add_contact), response.toString(), Log.ERROR);
                    String message = response.getString(RESPONCE_MESSAGE);
                    if (response.getBoolean(RESPONCE_ERROR) || message.isEmpty()) {
                        cancelProgressDialog();
                        alert(currentActivity, getString(R.string.msg_add_contact_failed), getString(R.string.msg_add_contact_failed), getResources().getString(R.string.alert_ok_button_text_no_network), getResources().getString(R.string.alert_cancel_button_text_no_network), true, true, ALERT_TYPE_NO_NETWORK);
                    } else {
                        toast(getResources().getString(R.string.msg_add_contact_suc), true);
                        editName.setText("");
                        editBitAddress.setText("");
                        getContactList();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                cancelProgressDialog();
                toast(getResources().getString(R.string.nwk_error_add_contact), true);
                logTesting(getResources().getString(R.string.nwk_error_add_contact), error.toString(), Log.ERROR);

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return super.getHeaders();
            }
        };
        BitPayApplication.getInstance().addToRequestQueue(signUpRequest);
    }

    private void getContactList() {

        int userId = SharedPreferenceUtils.getInstance(context).getInteger(USER_ID);

        progressDialog(context, context.getString(R.string.pdialog_message_loading), context.getString(R.string.pdialog_message_loading), false, false);

        JSONObject jsonGetContact = null;
        String URL_GET_CONTACT = GET_CONTACT_URL;
        try {
            jsonGetContact = new JSONObject();
            jsonGetContact.put(KEY_USER_ID, userId);
            Log.e("jsonGetContact", jsonGetContact.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest getGetContactRequest = new JsonObjectRequest(Request.Method.POST, URL_GET_CONTACT, jsonGetContact, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                cancelProgressDialog();
                try {
                    logTesting(currentActivity.getResources().getString(R.string.nwk_response_get_contact), response.toString(), Log.ERROR);
                    String message = response.getString(RESPONCE_MESSAGE);

                    if (response.getBoolean(RESPONCE_ERROR)) {
                        contactModelList.clear();
                        contactListAdapter.notifyDataSetChanged();
                        contactRecyclerView.setVisibility(View.GONE);
                        noContactTV.setVisibility(View.VISIBLE);
                    } else {
                        contactRecyclerView.setVisibility(View.VISIBLE);
                        noContactTV.setVisibility(View.GONE);
                        GsonBuilder builder = new GsonBuilder();
                        Gson gson = builder.create();
                        List<ContactModel> contactList = Arrays.asList(gson.fromJson(message.toString(), ContactModel[].class));
                        contactModelList.clear();
                        contactModelList.addAll(contactList);
                        contactListAdapter.notifyDataSetChanged();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                cancelProgressDialog();
                contactRecyclerView.setVisibility(View.GONE);
                noContactTV.setVisibility(View.VISIBLE);
                toast(currentActivity.getResources().getString(R.string.nwk_error_get_contact), true);
                logTesting(currentActivity.getResources().getString(R.string.nwk_error_get_contact), error.toString(), Log.ERROR);

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return super.getHeaders();
            }
        };

        BitPayApplication.getInstance().addToRequestQueue(getGetContactRequest);
    }


    private void showDialog() {

        if (contactModel == null) return;

        View customView = currentActivity.getLayoutInflater().inflate(R.layout.custom_transfer_bitcoin_dialog, null);
        final Dialog dialog = new Dialog(currentActivity, R.style.CustomDialog);
        dialog.setContentView(customView);
        int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.9);
        int height = (int) (getResources().getDisplayMetrics().heightPixels * 0.48);
        dialog.getWindow().setLayout(width, height);

        TextView lblReceiverNameTV = (TextView) dialog.findViewById(R.id.lblReceiverNameTV);
        TextView textReceiverNameTV = (TextView) dialog.findViewById(R.id.textReceiverNameTV);
        TextView lblBitAmtTV = (TextView) dialog.findViewById(R.id.lblBitAmtTV);
        TextView textBitAmtTV = (TextView) dialog.findViewById(R.id.textBitAmtTV);
        TextView lblRateAmtTV = (TextView) dialog.findViewById(R.id.lblRateAmtTV);
        TextView textRateAmtTV = (TextView) dialog.findViewById(R.id.textRateAmtTV);
        TextView lblNetworkFeeTV = (TextView) dialog.findViewById(R.id.lblNetworkFeeTV);
        TextView textNetworkFeeTV = (TextView) dialog.findViewById(R.id.textNetworkFeeTV);
        Button btnProceed = (Button) dialog.findViewById(R.id.btnProceed);
        Button btnCancel = (Button) dialog.findViewById(R.id.btnCancel);
        textReceiverNameTV.setText(contactModel.getContactName());
        textBitAmtTV.setText(getString(R.string.text_bitcoin) + " " + bitCoinAmtString);
        textRateAmtTV.setText(getString(R.string.text_rs_currency) + " " + rateAmtString);
        textNetworkFeeTV.setText(getString(R.string.text_bitcoin) + " " + estimatedNetworkFee);

        FontUtils.changeFont(context, lblReceiverNameTV, FONT_CORBEL_REGULAR);
        FontUtils.changeFont(context, textReceiverNameTV, FONT_CORBEL_REGULAR);
        FontUtils.changeFont(context, lblBitAmtTV, FONT_CORBEL_REGULAR);
        FontUtils.changeFont(context, textBitAmtTV, FONT_ROBOTO_REGULAR);
        FontUtils.changeFont(context, lblRateAmtTV, FONT_CORBEL_REGULAR);
        FontUtils.changeFont(context, textRateAmtTV, FONT_ROBOTO_REGULAR);
        FontUtils.changeFont(context, textNetworkFeeTV, FONT_ROBOTO_REGULAR);
        FontUtils.changeFont(context, btnProceed, FONT_CORBEL_REGULAR);
        FontUtils.changeFont(context, btnCancel, FONT_CORBEL_REGULAR);

        Typeface fontBitReg = Typeface.createFromAsset(context.getAssets(), FONT_BITCOIN_REGULAR);
        SpannableStringBuilder sb = new SpannableStringBuilder(textBitAmtTV.getText());
        TypefaceSpan bitRegularSpan = new CustomTypefaceSpan("", fontBitReg);
        sb.setSpan(bitRegularSpan, 0, 1, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        textBitAmtTV.setText(sb);

        sb = new SpannableStringBuilder(textRateAmtTV.getText());
        sb.setSpan(bitRegularSpan, 0, 1, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        textRateAmtTV.setText(sb);

        sb = new SpannableStringBuilder(textNetworkFeeTV.getText());
        sb.setSpan(bitRegularSpan, 0, 1, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        textNetworkFeeTV.setText(sb);

        btnProceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (Validator.getInstance().isNetworkAvailable(currentActivity)) {
                    initSendBitcoinModel();
                } else {
                    alert(currentActivity, getResources().getString(R.string.alert_message_no_network), getResources().getString(R.string.alert_message_no_network), getResources().getString(R.string.alert_ok_button_text_no_network), getResources().getString(R.string.alert_cancel_button_text_no_network), true, false, ALERT_TYPE_NO_NETWORK);
                }
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void initSendBitcoinModel() {
        if (contactModel == null) return;
        SendBitcoinsModel.getInstance().setContactId(contactModel.getId());
        SendBitcoinsModel.getInstance().setName(contactModel.getContactName());
        SendBitcoinsModel.getInstance().setBitAddress(contactModel.getBitPayAddress());
        SendBitcoinsModel.getInstance().setBitAmmount(bitCoinAmtString);
        SendBitcoinsModel.getInstance().setInrAmmount(rateAmtString);
        SendBitcoinsModel.getInstance().setUserId(SharedPreferenceUtils.getInstance(context).getInteger(USER_ID));
        sendBitcoins();
    }

    private void sendBitcoins() {

        progressDialog(context, context.getString(R.string.pdialog_message_loading), context.getString(R.string.pdialog_message_loading), false, false);

        JSONObject jsonSendBitcoinsRequest = null;
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        try {
            jsonSendBitcoinsRequest = new JSONObject(gson.toJson(SendBitcoinsModel.getInstance()));
            Log.e("jsonSendBitcoinsRequest", jsonSendBitcoinsRequest.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final String URL_SEND_BITCOIN = SEND_BITCOIN_URL;
        JsonObjectRequest sendBitcoinsRequest = new JsonObjectRequest(Request.Method.POST, URL_SEND_BITCOIN, jsonSendBitcoinsRequest, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    cancelProgressDialog();
                    logTesting(getResources().getString(R.string.nwk_response_send_bitcoin), response.toString(), Log.ERROR);
                    String message = response.getString(RESPONCE_MESSAGE);
                    if (response.getBoolean(RESPONCE_ERROR) || message.isEmpty()) {
                        alert(currentActivity, getString(R.string.msg_send_bitcoin_failed), getString(R.string.msg_send_bitcoin_failed), getResources().getString(R.string.alert_ok_button_text_no_network), getResources().getString(R.string.alert_cancel_button_text_no_network), true, true, ALERT_TYPE_NO_NETWORK);
                    } else {
                        JSONObject messageJson = new JSONObject(message);
                        String status = messageJson.getString(RESPONCE_STATUS);
                        if (status.equalsIgnoreCase(RESPONCE_FAIL)) {
                            JSONObject data = messageJson.getJSONObject(RESPONCE_DATA);
                            String errorMsg = data.getString(RESPONCE_ERRPR_MESSAGE);
                            alert(currentActivity, "", errorMsg, getResources().getString(R.string.alert_ok_button_text_no_network), getResources().getString(R.string.alert_cancel_button_text_no_network), false, false, ALERT_TYPE_NO_NETWORK);
                            return;
                        }
                        String transactionId = messageJson.getString(KEY_TRANSACTION_ID);
                        if (bundle == null) {
                            bundle = new Bundle();
                        }
                        bundle.putString(KEY_TRANSACTION_ID, transactionId);
                        bundle.putInt(KEY_TRANSACTION_TYPE, TYPE_SEND_BITCOIN);
                        bundle.putString(KEY_BIT_AMMOUNT, bitCoinAmtString);
                        ((BaseActivity) currentActivity).startActivity(currentActivity, TransactionInfoActivity.class, bundle, false, REQUEST_TAG_NO_RESULT, true, ANIMATION_SLIDE_UP);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                cancelProgressDialog();
                toast(getResources().getString(R.string.nwk_error_send_bitcoin), true);
                logTesting(getResources().getString(R.string.nwk_error_send_bitcoin), error.toString(), Log.ERROR);

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return super.getHeaders();
            }
        };
        BitPayApplication.getInstance().addToRequestQueue(sendBitcoinsRequest);
    }

    private void getNetworkFee() {
        progressDialog(context, context.getString(R.string.pdialog_message_loading), context.getString(R.string.pdialog_message_loading), false, false);
        JSONObject jsonGetNetworkFee = null;
        String URL_GET_NETWORK_FEE = GET_NETWORK_FEE_URL;
        try {
            jsonGetNetworkFee = new JSONObject();
            jsonGetNetworkFee.put("amounts", bitCoinAmtString);
            jsonGetNetworkFee.put("addresses", SharedPreferenceUtils.getInstance(context).getString(USER_BIT_ADDRESS));
            Log.e("jsonGetNetworkFee", jsonGetNetworkFee.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonGetNetworkFeeRequest = new JsonObjectRequest(Request.Method.POST, URL_GET_NETWORK_FEE, jsonGetNetworkFee, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    cancelProgressDialog();
                    logTesting(currentActivity.getResources().getString(R.string.nwk_response_get_balance), response.toString(), Log.ERROR);
                    String message = response.getString(RESPONCE_MESSAGE);
                    if (!response.getBoolean(RESPONCE_ERROR)) {
                        JSONObject messageJson = new JSONObject(message);
                        String status = messageJson.getString(RESPONCE_STATUS);
                        if (TextUtils.isEmpty(status)) return;
                        if (status.equalsIgnoreCase("success")) {
                            JSONObject dataJson = messageJson.getJSONObject(RESPONCE_DATA);
                            if (dataJson != null) {
                                estimatedNetworkFee = dataJson.getString("estimated_network_fee");
                            }
                            showDialog();
                        }else {
                            JSONObject dataJson = messageJson.getJSONObject(RESPONCE_DATA);
                            if (dataJson != null) {
                                toast(dataJson.getString(RESPONCE_ERRPR_MESSAGE),true);
                            }
                        }
                        //double networkFee = Double.parseDouble(estimatedNetworkFee);
                        //currentBalanceTV.setText(new DecimalFormat("##.######").format(networkFee) + " " + getString(R.string.text_bitcoin));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                cancelProgressDialog();
                logTesting(currentActivity.getResources().getString(R.string.nwk_error_get_balance), error.toString(), Log.ERROR);

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return super.getHeaders();
            }
        };

        BitPayApplication.getInstance().addToRequestQueue(jsonGetNetworkFeeRequest, "jsonGetNetworkFeeRequest2");
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        switch (requestCode) {
            case REQUEST_TAG_CONFIRM_PIN: {
                if (resultCode == RESULT_OK) {
                    getNetworkFee();
                }
                break;
            }
            case REQUEST_TAG_SCANNER_ACTIVITY: {
                if (resultCode == RESULT_OK) {
                    if (bundle == null) {
                        bundle = new Bundle();
                    }
                    bundle.putInt(KEY_TYPE, TYPE_SEND_BITCOIN);
                    startActivity(currentActivity, ExistingConfirmPinActivity.class, bundle, true, REQUEST_TAG_CONFIRM_PIN, true, ANIMATION_SLIDE_UP);

                    String bitAddress = intent.getStringExtra(USER_BIT_ADDRESS);
                    contactModel = new ContactModel();
                    contactModel.setContactName("Bitpay User");
                    contactModel.setBitPayAddress(bitAddress);
                }
                break;
            }
        }
    }
}
