package com.bitpay.bitpay.activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.TypefaceSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bitpay.bitpay.R;
import com.bitpay.bitpay.application.BitPayApplication;
import com.bitpay.bitpay.utils.DocumentUtils;
import com.bitpay.bitpay.utils.FontUtils;
import com.bitpay.bitpay.utils.SharedPreferenceUtils;
import com.bitpay.bitpay.utils.Validator;
import com.bitpay.bitpay.widgets.CustomTypefaceSpan;


import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Map;

public class SendBitcoinsActivity extends BaseActivity {

    private TextView lblCurrentBalanceTV;
    private TextView currentBalanceTV;
    private TextView lblBuyingRateTV;
    private TextView amtBuyingRateTV;
    private TextView lblSellingRateTV;
    private TextView amtSellingRateTV;
    private Button buttonSubmit;
    private LinearLayout bitcoinRateProgressBar;
    private LinearLayout currentBalanceProgressBar;
    private CardView bitcoinRateCV;
    private LinearLayout rateToBitcoinLL;
    private LinearLayout bitcoinToRateLL;
    private TextView inrCurrency1TV;
    private EditText editAmmountRate;
    private TextView symbollBitcoin1TV;
    private TextView amtBitcoinTV;
    private TextView inrCurrency2TV;
    private EditText editAmmountBit;
    private TextView symbollBitcoin2TV;
    private TextView amtRateTV;
    private TextView validAmtTV;
    private ImageView bitRateIV;
    private LinearLayout successBitcoinTransferLL;
    private TextView currentInrBalanceTV;
    private TextView lblMaxAmtTV;
    private String buyingRate;
    private String sellingRate;
    private String currentBalance;
    private double minTransferAmt;
    private double maxTransferAmt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_bitcoins);
    }

    @Override
    protected void initViews() {
        settingTitle(getString(R.string.menu_send_bitcoin));
        lblCurrentBalanceTV = (TextView) findViewById(R.id.lblCurrentBalanceTV);
        currentBalanceTV = (TextView) findViewById(R.id.currentBalanceTV);
        lblBuyingRateTV = (TextView) findViewById(R.id.lblBuyingRateTV);
        amtBuyingRateTV = (TextView) findViewById(R.id.amtBuyingRateTV);
        lblSellingRateTV = (TextView) findViewById(R.id.lblSellingRateTV);
        amtSellingRateTV = (TextView) findViewById(R.id.amtSellingRateTV);
        buttonSubmit = (Button) findViewById(R.id.buttonSubmit);
        bitcoinRateCV = (CardView) findViewById(R.id.bitcoinRateCV);
        bitcoinRateProgressBar = (LinearLayout) findViewById(R.id.bitcoinRateProgressBar);
        currentBalanceProgressBar = (LinearLayout) findViewById(R.id.currentBalanceProgressBar);
        currentInrBalanceTV = (TextView) findViewById(R.id.currentInrBalanceTV);
        lblMaxAmtTV = (TextView) findViewById(R.id.lblMaxAmtTV);
        rateToBitcoinLL = (LinearLayout) findViewById(R.id.rateToBitcoinLL);
        successBitcoinTransferLL = (LinearLayout) findViewById(R.id.successBitcoinTransferLL);
        bitcoinToRateLL = (LinearLayout) findViewById(R.id.bitcoinToRateLL);
        validAmtTV = (TextView) findViewById(R.id.validAmtTV);
        inrCurrency1TV = (TextView) findViewById(R.id.inrCurrency1TV);
        editAmmountRate = (EditText) findViewById(R.id.editAmmountRate);
        symbollBitcoin1TV = (TextView) findViewById(R.id.symbollBitcoin1TV);
        amtBitcoinTV = (TextView) findViewById(R.id.amtBitcoinTV);
        inrCurrency2TV = (TextView) findViewById(R.id.inrCurrency2TV);
        editAmmountBit = (EditText) findViewById(R.id.editAmmountBit);
        symbollBitcoin2TV = (TextView) findViewById(R.id.symbollBitcoin2TV);
        amtRateTV = (TextView) findViewById(R.id.amtRateTV);
        bitRateIV = (ImageView) findViewById(R.id.bitRateIV);

        FontUtils.changeFont(context, lblCurrentBalanceTV, FONT_CORBEL_REGULAR);
        FontUtils.changeFont(context, currentBalanceTV, FONT_ROBOTO_REGULAR);
        FontUtils.changeFont(context, lblBuyingRateTV, FONT_CORBEL_REGULAR);
        FontUtils.changeFont(context, amtBuyingRateTV, FONT_ROBOTO_REGULAR);
        FontUtils.changeFont(context, lblSellingRateTV, FONT_CORBEL_REGULAR);
        FontUtils.changeFont(context, amtSellingRateTV, FONT_ROBOTO_REGULAR);
        FontUtils.changeFont(context, buttonSubmit, FONT_CORBEL_REGULAR);
        FontUtils.changeFont(context, inrCurrency1TV, FONT_BITCOIN_BOLD);
        FontUtils.changeFont(context, editAmmountRate, FONT_ROBOTO_REGULAR);
        FontUtils.changeFont(context, symbollBitcoin1TV, FONT_BITCOIN_BOLD);
        FontUtils.changeFont(context, amtBitcoinTV, FONT_ROBOTO_REGULAR);
        FontUtils.changeFont(context, inrCurrency2TV, FONT_BITCOIN_BOLD);
        FontUtils.changeFont(context, editAmmountBit, FONT_ROBOTO_REGULAR);
        FontUtils.changeFont(context, symbollBitcoin2TV, FONT_BITCOIN_BOLD);
        FontUtils.changeFont(context, amtRateTV, FONT_ROBOTO_REGULAR);
        FontUtils.changeFont(context, currentInrBalanceTV, FONT_ROBOTO_REGULAR);
        FontUtils.changeFont(context, lblMaxAmtTV, FONT_ROBOTO_REGULAR);
        FontUtils.changeFont(context, validAmtTV, FONT_ROBOTO_REGULAR);

        Typeface fontRobotoReg = Typeface.createFromAsset(getAssets(), FONT_ROBOTO_REGULAR);
        Typeface fontBitReg = Typeface.createFromAsset(getAssets(), FONT_BITCOIN_REGULAR);
        SpannableStringBuilder sb = new SpannableStringBuilder(lblBuyingRateTV.getText());
        TypefaceSpan robotoRegularSpan = new CustomTypefaceSpan("", fontRobotoReg);
        TypefaceSpan bitRegularSpan = new CustomTypefaceSpan("", fontBitReg);
        sb.setSpan(robotoRegularSpan, 0, 1, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        sb.setSpan(bitRegularSpan, 1, 2, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        lblBuyingRateTV.setText(sb);

        sb = new SpannableStringBuilder(lblSellingRateTV.getText());
        sb.setSpan(robotoRegularSpan, 0, 1, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        sb.setSpan(bitRegularSpan, 1, 2, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        lblSellingRateTV.setText(sb);

        String minAmount = SharedPreferenceUtils.getInstance(context).getString(KEY_MIN_AMOUNT);
        String maxAmount = SharedPreferenceUtils.getInstance(context).getString(KEY_MAX_AMOUNT);
        if (!TextUtils.isEmpty(minAmount) && !TextUtils.isEmpty(maxAmount)) {
            minTransferAmt = Double.parseDouble(minAmount);
            maxTransferAmt = Double.parseDouble(maxAmount);
        }
        String textString = getString(R.string.text_min_amt) + " " + minAmount + " " + getString(R.string.text_max_amt) + " " + maxAmount;
        validAmtTV.setText(textString);

        getCurrentBalance();
        getBitcoinRate();
    }

    @Override
    protected void initContext() {
        context = SendBitcoinsActivity.this;
        currentActivity = SendBitcoinsActivity.this;
    }

    @Override
    protected void initListners() {
        buttonSubmit.setOnClickListener(this);
        bitRateIV.setOnClickListener(this);
        editAmmountRate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                convertRsToBit(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        editAmmountBit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                convertBitToRs(s.toString());
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
            case R.id.buttonSubmit: {
                toHideKeyboard();
                if (!DocumentUtils.isDocumentApproved(context)) {
                    alert(currentActivity, "", getString(R.string.error_document_status), getResources().getString(R.string.alert_ok_button_text_no_network), getResources().getString(R.string.alert_cancel_button_text_no_network), false, false, ALERT_TYPE_NO_NETWORK);
                    return;
                }
                if (Validator.getInstance().isNetworkAvailable(currentActivity)) {
                    if (isMandatoryFields()) {
                        toOpenContactActivity();
                    }
                } else {
                    alert(currentActivity, getResources().getString(R.string.alert_message_no_network), getResources().getString(R.string.alert_message_no_network), getResources().getString(R.string.alert_ok_button_text_no_network), getResources().getString(R.string.alert_cancel_button_text_no_network), true, false, ALERT_TYPE_NO_NETWORK);
                }
                break;
            }
            case R.id.bitRateIV: {
                if (bitcoinToRateLL.getVisibility() == View.VISIBLE) {
                    bitcoinToRateLL.setVisibility(View.GONE);
                    rateToBitcoinLL.setVisibility(View.VISIBLE);
                    bitRateIV.setImageDrawable(ContextCompat.getDrawable(currentActivity, R.drawable.reverse_r_b));
                } else if (rateToBitcoinLL.getVisibility() == View.VISIBLE) {
                    rateToBitcoinLL.setVisibility(View.GONE);
                    bitcoinToRateLL.setVisibility(View.VISIBLE);
                    bitRateIV.setImageDrawable(ContextCompat.getDrawable(currentActivity, R.drawable.reverse_b_r));
                }
                break;
            }
        }
    }

    @Override
    public void onAlertClicked(int alertType) {

    }

    private void toOpenContactActivity() {
        String rateAmtString = "";
        String bitCoinAmtString = "";
        if (bitcoinToRateLL.getVisibility() == View.VISIBLE) {
            rateAmtString = amtRateTV.getText().toString();
            bitCoinAmtString = editAmmountBit.getText().toString();
        } else if (rateToBitcoinLL.getVisibility() == View.VISIBLE) {
            bitCoinAmtString = amtBitcoinTV.getText().toString();
            rateAmtString = editAmmountRate.getText().toString();
        }
        if (bundle == null) {
            bundle = new Bundle();
        }
        bundle.putString(KEY_RATE_AMMOUNT, rateAmtString);
        bundle.putString(KEY_BIT_AMMOUNT, bitCoinAmtString);
        startActivity(currentActivity, ContactBookActivity.class, bundle, true, REQUEST_TAG_CONTACT_ACTIVITY, true, ANIMATION_SLIDE_UP);
    }


    private void convertRsToBit(String inputRs) {
        try {
            if (TextUtils.isEmpty(sellingRate)) return;
            double sellRate = Double.parseDouble(sellingRate);
            if (TextUtils.isEmpty(inputRs)) {
                amtBitcoinTV.setText("");
                return;
            }
            double inputValue = Double.parseDouble(inputRs);
            double bitcoinValue = inputValue / sellRate;
            amtBitcoinTV.setText(new DecimalFormat("##.######").format(bitcoinValue));
        } catch (NumberFormatException nfe) {
            nfe.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void convertBitToRs(String inputBit) {
        try {
            if (TextUtils.isEmpty(sellingRate)) return;
            double sellRate = Double.parseDouble(sellingRate);
            if (TextUtils.isEmpty(inputBit)) {
                amtRateTV.setText("");
                return;
            }
            double inputValue = Double.parseDouble(inputBit);
            double bitcoinValue = inputValue * sellRate;
            amtRateTV.setText(new DecimalFormat("##.######").format(bitcoinValue));
        } catch (NumberFormatException nfe) {
            nfe.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean isMandatoryFields() {
        if (TextUtils.isEmpty(currentBalance)) {
            return false;
        }
        editAmmountBit.setError(null);
        editAmmountRate.setError(null);
        if (bitcoinToRateLL.getVisibility() == View.VISIBLE) {
            if (editAmmountBit.getText().toString().isEmpty()) {
                editAmmountBit.setError(getString(R.string.error_empty_amt));
                editAmmountBit.requestFocus();
                return false;
            } else {
                double ammount = Double.parseDouble(amtRateTV.getText().toString());
                double currentBitAmt = Double.parseDouble(currentBalance);
                double inputBitAmt = Double.parseDouble(editAmmountBit.getText().toString());
                if (currentBitAmt < inputBitAmt) {
                    editAmmountBit.setError(getString(R.string.error_invalid_amt));
                    editAmmountBit.requestFocus();
                    return false;
                } else if ((ammount < minTransferAmt || ammount > maxTransferAmt)) {
                    editAmmountBit.setError(getString(R.string.error_invalid_amt));
                    editAmmountBit.requestFocus();
                    return false;
                }

            }

        } else if (rateToBitcoinLL.getVisibility() == View.VISIBLE) {
            if (editAmmountRate.getText().toString().isEmpty()) {
                editAmmountRate.setError(getString(R.string.error_empty_amt));
                editAmmountRate.requestFocus();
                return false;
            } else {
                double ammount = Double.parseDouble(editAmmountRate.getText().toString());
                double currentBitAmt = Double.parseDouble(currentBalance);
                double inputBitAmt = Double.parseDouble(amtBitcoinTV.getText().toString());
                if (currentBitAmt < inputBitAmt) {
                    editAmmountRate.setError(getString(R.string.error_invalid_amt));
                    editAmmountRate.requestFocus();
                    return false;
                } else if ((ammount < minTransferAmt || ammount > maxTransferAmt)) {
                    editAmmountRate.setError(getString(R.string.error_invalid_amt));
                    editAmmountRate.requestFocus();
                    return false;
                }
            }
        }

        return true;
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

        BitPayApplication.getInstance().addToRequestQueue(getGetCurentBalanceRequest);
    }

    private void getBitcoinRate() {
        bitcoinRateProgressBar.setVisibility(View.VISIBLE);
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
                    bitcoinRateProgressBar.setVisibility(View.GONE);
                    ((BaseActivity) currentActivity).logTesting(currentActivity.getResources().getString(R.string.nwk_response_get_rate), response.toString(), Log.ERROR);
                    String message = response.getString(RESPONCE_MESSAGE);
                    if (!response.getBoolean(RESPONCE_ERROR)) {
                        JSONObject messageJson = new JSONObject(message);
                        buyingRate = messageJson.getString(KEY_BUYING_RATE);
                        sellingRate = messageJson.getString(KEY_SELLING_RATE);
                        if (TextUtils.isEmpty(buyingRate) || TextUtils.isEmpty(sellingRate)) {
                            return;
                        }
                        double buyRate = Double.parseDouble(buyingRate);
                        double sellRate = Double.parseDouble(sellingRate);
                        amtBuyingRateTV.setText(NumberFormat.getCurrencyInstance().format(buyRate));
                        amtSellingRateTV.setText(NumberFormat.getCurrencyInstance().format(sellRate));
                        if (!TextUtils.isEmpty(currentBalance)) {
                            double bitAmt = Double.parseDouble(currentBalance);
                            double inrValue = bitAmt * sellRate;
                            currentInrBalanceTV.setText(new DecimalFormat("##.######").format(inrValue) + " " + getString(R.string.text_rs_currency));
                        }
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
                bitcoinRateProgressBar.setVisibility(View.GONE);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_TAG_CONTACT_ACTIVITY) {
            if (resultCode == RESULT_OK) {
                bitcoinRateCV.setVisibility(View.GONE);
                rateToBitcoinLL.setVisibility(View.GONE);
                bitcoinToRateLL.setVisibility(View.GONE);
                bitRateIV.setVisibility(View.GONE);
                buttonSubmit.setVisibility(View.GONE);
                successBitcoinTransferLL.setVisibility(View.VISIBLE);
            }
        }
    }

}
