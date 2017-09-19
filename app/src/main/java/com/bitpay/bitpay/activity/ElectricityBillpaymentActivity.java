package com.bitpay.bitpay.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bitpay.bitpay.R;
import com.bitpay.bitpay.application.BitPayApplication;
import com.bitpay.bitpay.models.ConnectionProviderModel;
import com.bitpay.bitpay.models.RechargeModel;
import com.bitpay.bitpay.utils.DocumentUtils;
import com.bitpay.bitpay.utils.FontUtils;
import com.bitpay.bitpay.utils.SharedPreferenceUtils;
import com.bitpay.bitpay.utils.Validator;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.Map;

public class ElectricityBillpaymentActivity extends BaseActivity {

    private LinearLayout currentBalanceProgressBar;
    private TextView lblCurrentBalanceTV;
    private TextView currentBalanceTV;
    private TextView currentInrBalanceTV;
    private TextView symbollBitcoin1TV;
    private TextView amtBitcoinTV;
    private EditText elecricityProviderETV;
    private EditText editElecricityNumber;
    private EditText amountEditText;
    private TextView rupeesTextView;

    private Button btnSubmit;
    private String mobileNumber;
    private String amount;
    private ConnectionProviderModel selectedConnectionProvider;

    private String buyingRate;
    private String sellingRate;
    private String currentBalance;
    private String bitAmount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_electrc_bill_payment);
    }

    @Override
    protected void initViews() {
        settingTitle(getString(R.string.menu_electricity_bill_pay));
        if (getIntent().getExtras() != null) {
            selectedConnectionProvider = (ConnectionProviderModel) getIntent().getExtras().getSerializable(KEY_PROVIDER_MODEL);
        }
        currentBalanceProgressBar = (LinearLayout) findViewById(R.id.currentBalanceProgressBar);
        lblCurrentBalanceTV = (TextView) findViewById(R.id.lblCurrentBalanceTV);
        currentBalanceTV = (TextView) findViewById(R.id.currentBalanceTV);
        currentInrBalanceTV = (TextView) findViewById(R.id.currentInrBalanceTV);
        elecricityProviderETV = (EditText) findViewById(R.id.elecricityProviderETV);
        editElecricityNumber = (EditText) findViewById(R.id.editElecricityNumber);
        amountEditText = (EditText) findViewById(R.id.amountEditText);
        rupeesTextView = (TextView) findViewById(R.id.rupeesTextView);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        symbollBitcoin1TV = (TextView) findViewById(R.id.symbollBitcoin1TV);
        amtBitcoinTV = (TextView) findViewById(R.id.amtBitcoinTV);

        FontUtils.changeFont(context, rupeesTextView, FONT_BITCOIN_BOLD);
        FontUtils.changeFont(context, editElecricityNumber, FONT_ROBOTO_REGULAR);
        FontUtils.changeFont(context, elecricityProviderETV, FONT_ROBOTO_REGULAR);
        FontUtils.changeFont(context, amountEditText, FONT_ROBOTO_REGULAR);
        FontUtils.changeFont(context, btnSubmit, FONT_ROBOTO_REGULAR);
        FontUtils.changeFont(context, lblCurrentBalanceTV, FONT_CORBEL_REGULAR);
        FontUtils.changeFont(context, currentBalanceTV, FONT_ROBOTO_REGULAR);
        FontUtils.changeFont(context, currentInrBalanceTV, FONT_ROBOTO_REGULAR);
        FontUtils.changeFont(context, symbollBitcoin1TV, FONT_BITCOIN_BOLD);
        FontUtils.changeFont(context, amtBitcoinTV, FONT_ROBOTO_REGULAR);
        if (selectedConnectionProvider != null)
            elecricityProviderETV.setText(selectedConnectionProvider.getConnectionName());
        getCurrentBalance();
        getBitcoinRate();
    }

    @Override
    protected void initContext() {
        context = ElectricityBillpaymentActivity.this;
        currentActivity = ElectricityBillpaymentActivity.this;
    }

    @Override
    protected void initListners() {
        btnSubmit.setOnClickListener(this);
        amountEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                amount = s.toString();
                if (TextUtils.isEmpty(sellingRate)) return;
                if (TextUtils.isEmpty(amount)) return;
                double sellRate = Double.parseDouble(sellingRate);
                double inputValue = Double.parseDouble(amount);
                double bitcoinValue = inputValue / sellRate;
                amtBitcoinTV.setText(new DecimalFormat("##.######").format(bitcoinValue));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        editElecricityNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                mobileNumber = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


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
                        if (bundle == null) {
                            bundle = new Bundle();
                        }
                        bundle.putInt(KEY_TYPE, TYPE_SEND_BITCOIN);
                        startActivity(currentActivity, ExistingConfirmPinActivity.class, bundle, true, REQUEST_TAG_CONFIRM_PIN, true, ANIMATION_SLIDE_UP);
                    }
                } else {
                    alert(currentActivity, getResources().getString(R.string.alert_message_no_network), getResources().getString(R.string.alert_message_no_network), getResources().getString(R.string.alert_ok_button_text_no_network), getResources().getString(R.string.alert_cancel_button_text_no_network), true, false, ALERT_TYPE_NO_NETWORK);
                }
                break;
            }
        }

    }


    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        if (requestCode == REQUEST_TAG_CONFIRM_PIN) {
            if (resultCode == RESULT_OK) {
                showAlertDialog();
            }
        }
    }

    private boolean isMandatoryFields() {
        editElecricityNumber.setError(null);
        amountEditText.setError(null);

        if (TextUtils.isEmpty(mobileNumber)) {
            editElecricityNumber.setError(getString(R.string.error_enter_customer_number));
            editElecricityNumber.requestFocus();
            return false;
        } /*else if (!Validator.getInstance().validateGasNumber(context, editElecricityNumber.getText().toString()).equals("")) {
            String numberError = Validator.getInstance().validateNumber(context, editElecricityNumber.getText().toString());
            editElecricityNumber.setError(numberError);
            editElecricityNumber.requestFocus();
            return false;
        }*/

        if (TextUtils.isEmpty(amount)) {
            amountEditText.setError(getString(R.string.error_empty_amt));
            amountEditText.requestFocus();
            return false;
        } else {
            if (TextUtils.isEmpty(sellingRate) || TextUtils.isEmpty(currentBalance)) {
                toast("Please try again later", true);
                return false;
            }
            double ammount = Double.parseDouble(amount);
            double currentBitAmt = Double.parseDouble(currentBalance);
            double inputBitAmt = Double.parseDouble(convertRsToBit(amount));
            if (currentBitAmt < inputBitAmt) {
                amountEditText.setError(getString(R.string.error_invalid_amt));
                amountEditText.requestFocus();
                return false;
            } else if ((ammount < MIN_RECHARGE_AMT)) {
                amountEditText.setError(getString(R.string.enter_min_amount));
                amountEditText.requestFocus();
                return false;
            }
        }
        initRechargeModel();
        return true;
    }

    private void initRechargeModel() {
        RechargeModel.getInstance().setUserId(SharedPreferenceUtils.getInstance(context).getInteger(USER_ID));
        RechargeModel.getInstance().setBitAmount(bitAmount);
        RechargeModel.getInstance().setInrAmount(amount);
        RechargeModel.getInstance().setProviderId(selectedConnectionProvider.getProciderId());
        RechargeModel.getInstance().setNumber(editElecricityNumber.getText().toString());
        RechargeModel.getInstance().setRechargeType(RECHARGE_ELECTRICITY);
    }

    @Override
    public void onAlertClicked(int alertType) {
        if (alertType == ALERT_TYPE_SEND_BIT) {
            startActivity(currentActivity, DashboardActivity.class, bundle, false, REQUEST_TAG_NO_RESULT, true, ANIMATION_SLIDE_UP);
        }
    }

    private String convertRsToBit(String inputRs) {
        String bitAmt = "0.0";
        try {
            double sellRate = Double.parseDouble(sellingRate);
            double inputValue = Double.parseDouble(inputRs);
            double bitcoinValue = inputValue / sellRate;
            bitAmt = new DecimalFormat("##.######").format(bitcoinValue);
            bitAmount = bitAmt;
        } catch (NumberFormatException nfe) {
            nfe.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitAmt;
    }

    private void showAlertDialog() {
        View customView = currentActivity.getLayoutInflater().inflate(R.layout.mobile_recharge_alert_dialog, null);
        final Dialog dialog = new Dialog(currentActivity, R.style.CustomDialog);
        dialog.setContentView(customView);
        int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.9);
        int height = (int) (getResources().getDisplayMetrics().heightPixels * 0.4);
        dialog.getWindow().setLayout(width, height);

        TextView mobileNumberTextView = (TextView) dialog.findViewById(R.id.mobileNumberTextView);
        TextView mobileNumberValueTextView = (TextView) dialog.findViewById(R.id.mobileNumberValueTextView);
        TextView amountTextView = (TextView) dialog.findViewById(R.id.amountTextView);
        TextView amountValueTextView = (TextView) dialog.findViewById(R.id.amountValueTextView);
        Button btnProceed = (Button) dialog.findViewById(R.id.btnProceed);
        Button btnCancel = (Button) dialog.findViewById(R.id.btnCancel);

        mobileNumberTextView.setText(getString(R.string.elecricityNumber));

        FontUtils.changeFont(context, mobileNumberTextView, FONT_CORBEL_REGULAR);
        FontUtils.changeFont(context, mobileNumberValueTextView, FONT_ROBOTO_REGULAR);
        FontUtils.changeFont(context, amountTextView, FONT_CORBEL_REGULAR);
        FontUtils.changeFont(context, amountValueTextView, FONT_ROBOTO_REGULAR);
        FontUtils.changeFont(context, btnProceed, FONT_CORBEL_REGULAR);
        FontUtils.changeFont(context, btnCancel, FONT_CORBEL_REGULAR);

        mobileNumberValueTextView.setText(mobileNumber);
        amountValueTextView.setText(amount);

        btnProceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (Validator.getInstance().isNetworkAvailable(currentActivity)) {
                    rechargeGas();
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

    private void getCurrentBalance() {
        currentBalanceProgressBar.setVisibility(View.VISIBLE);
        int userId = SharedPreferenceUtils.getInstance(context).getInteger(USER_ID);

        JSONObject jsonGetCurrentBalance = null;
        String URL_GET_CURRENT_BALANCE = GET_CURRENT_BALANCE_URL;
        try {
            jsonGetCurrentBalance = new JSONObject();
            jsonGetCurrentBalance.put(KEY_USER_ID, userId + "");
            Log.e("jsonGetContact", jsonGetCurrentBalance.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest getGetCurentBalanceRequest = new JsonObjectRequest(Request.Method.POST, URL_GET_CURRENT_BALANCE, jsonGetCurrentBalance, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    currentBalanceProgressBar.setVisibility(View.GONE);
                    ((BaseActivity) currentActivity).logTesting(currentActivity.getResources().getString(R.string.nwk_response_get_balance), response.toString(), Log.ERROR);
                    String message = response.getString(RESPONCE_MESSAGE);
                    if (!response.getBoolean(RESPONCE_ERROR)) {
                        JSONObject messageJson = new JSONObject(message);
                        currentBalance = messageJson.getString(KEY_CURRENT_BALANCE);
                        if (TextUtils.isEmpty(currentBalance)) return;
                        double balance = Double.parseDouble(currentBalance);
                        currentBalanceTV.setText(new DecimalFormat("##.######").format(balance) + " " + getString(R.string.text_bitcoin));
                        if (!TextUtils.isEmpty(sellingRate)) {
                            double sellRate = Double.parseDouble(sellingRate);
                            double inrValue = balance * sellRate;
                            currentInrBalanceTV.setText(new DecimalFormat("##.######").format(inrValue) + " " + getString(R.string.text_rs_currency));
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                currentBalanceProgressBar.setVisibility(View.GONE);
                //((BaseActivity) currentActivity).toast(currentActivity.getResources().getString(R.string.nwk_error_get_balance), true);
                ((BaseActivity) currentActivity).logTesting(currentActivity.getResources().getString(R.string.nwk_error_get_balance), error.toString(), Log.ERROR);

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return super.getHeaders();
            }
        };

        BitPayApplication.getInstance().addToRequestQueue(getGetCurentBalanceRequest, "getGetCurentBalanceRequest4");
    }

    @Override
    public void onStop() {
        super.onStop();
        BitPayApplication.getInstance().cancelPendingRequest("getGetCurentBalanceRequest4");
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
                    ((BaseActivity) currentActivity).logTesting(currentActivity.getResources().getString(R.string.nwk_response_get_rate), response.toString(), Log.ERROR);
                    String message = response.getString(RESPONCE_MESSAGE);
                    if (!response.getBoolean(RESPONCE_ERROR)) {
                        JSONObject messageJson = new JSONObject(message);
                        buyingRate = messageJson.getString(KEY_BUYING_RATE);
                        sellingRate = messageJson.getString(KEY_SELLING_RATE);
                        double sellRate = Double.parseDouble(sellingRate);
                        if (!TextUtils.isEmpty(currentBalance)) {
                            double bitAmt = Double.parseDouble(currentBalance);
                            double inrValue = bitAmt * sellRate;
                            currentInrBalanceTV.setText(new DecimalFormat("##.######").format(inrValue) + " " + getString(R.string.text_rs_currency));
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                cancelProgressDialog();
                //((BaseActivity) currentActivity).toast(currentActivity.getResources().getString(R.string.nwk_error_get_rate), true);
                ((BaseActivity) currentActivity).logTesting(currentActivity.getResources().getString(R.string.nwk_error_get_rate), error.toString(), Log.ERROR);

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return super.getHeaders();
            }
        };

        BitPayApplication.getInstance().addToRequestQueue(getGetBitcoinRateRequest);
    }

    private void rechargeGas() {

        progressDialog(context, context.getString(R.string.pdialog_message_loading), context.getString(R.string.pdialog_message_loading), false, false);

        JSONObject jsonRechargeRequest = null;
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        try {
            jsonRechargeRequest = new JSONObject(gson.toJson(RechargeModel.getInstance()));
            Log.e("jsonRechargeRequest", jsonRechargeRequest.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final String URL_RECHARGE_MOBILE = RECHARGE_MOBILE_URL;
        JsonObjectRequest rechargeRequest = new JsonObjectRequest(Request.Method.POST, URL_RECHARGE_MOBILE, jsonRechargeRequest, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    cancelProgressDialog();
                    logTesting(getResources().getString(R.string.nwk_response_recharge), response.toString(), Log.ERROR);
                    String message = response.getString(RESPONCE_MESSAGE);
                    if (response.getBoolean(RESPONCE_ERROR) || message.isEmpty()) {
                        alert(currentActivity, getString(R.string.msg_recharge_elecricity_failed), getString(R.string.msg_recharge_elecricity_failed), getResources().getString(R.string.alert_ok_button_text_no_network), getResources().getString(R.string.alert_cancel_button_text_no_network), true, true, ALERT_TYPE_NO_NETWORK);
                    } else {
                        JSONObject messageJson = new JSONObject(message);
                        String status = messageJson.getString(RESPONCE_STATUS);
                        if (status.equalsIgnoreCase("failure") || status.equalsIgnoreCase(RESPONCE_FAIL)) {
                            if(!messageJson.has("message")){
                                JSONObject data = messageJson.getJSONObject(RESPONCE_DATA);
                                String errorMsg = data.getString(RESPONCE_ERRPR_MESSAGE);
                                alert(currentActivity, "", errorMsg, getResources().getString(R.string.alert_ok_button_text_no_network), getResources().getString(R.string.alert_cancel_button_text_no_network), false, false, ALERT_TYPE_NO_NETWORK);
                                return;
                            }
                            String errorMsg = messageJson.getString("message");
                            alert(currentActivity, "", errorMsg, getResources().getString(R.string.alert_ok_button_text_no_network), getResources().getString(R.string.alert_cancel_button_text_no_network), false, false, ALERT_TYPE_NO_NETWORK);
                            return;
                        }
                        String transactionId = messageJson.getString(KEY_TRANSACTION_ID);
                        if (bundle == null) {
                            bundle = new Bundle();
                        }
                        bundle.putString(KEY_TRANSACTION_ID, transactionId);
                        bundle.putInt(KEY_TRANSACTION_TYPE, TYPE_ELECTRICITY);
                        bundle.putString(KEY_BIT_AMMOUNT, amount);
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
                toast(getResources().getString(R.string.nwk_error_recharge), true);
                logTesting(getResources().getString(R.string.nwk_error_recharge), error.toString(), Log.ERROR);

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return super.getHeaders();
            }
        };
        BitPayApplication.getInstance().addToRequestQueue(rechargeRequest);
    }
}
