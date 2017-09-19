package com.bitpay.bitpay.activity;

import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.TypefaceSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bitpay.bitpay.R;
import com.bitpay.bitpay.application.BitPayApplication;
import com.bitpay.bitpay.utils.FontUtils;
import com.bitpay.bitpay.utils.SharedPreferenceUtils;
import com.bitpay.bitpay.widgets.CustomTypefaceSpan;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.Map;

/**
 * Created by Codeslay-03 on 7/21/2017.
 */

public class BuyActivity extends BaseActivity {

    private LinearLayout currentBalanceProgressBar;
    private TextView lblCurrentBalanceTV;
    private TextView currentBalanceTV;
    private TextView currentInrBalanceTV;
    private Button btnBankDetails;
    private Button btnSubmitRequest;
    private Button btnPayOnline;
    private TextView textDeposit1;
    private TextView textDeposit2;
    private TextView textDeposit3;
    private TextView textDeposit4;
    private TextView textDeposit5;
    private TextView textDeposit6;

    private String buyingRate;
    private String sellingRate;
    private String currentBalance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy);
    }

    @Override
    protected void initViews() {
        settingTitle(getString(R.string.title_buy_bitcoins));

        currentBalanceProgressBar = (LinearLayout) findViewById(R.id.currentBalanceProgressBar);
        lblCurrentBalanceTV = (TextView) findViewById(R.id.lblCurrentBalanceTV);
        currentBalanceTV = (TextView) findViewById(R.id.currentBalanceTV);
        currentInrBalanceTV = (TextView) findViewById(R.id.currentInrBalanceTV);
        btnBankDetails = (Button) findViewById(R.id.btnBankDetails);
        btnSubmitRequest = (Button) findViewById(R.id.btnSubmitRequest);
        btnPayOnline = (Button) findViewById(R.id.btnPayOnline);
        textDeposit1 = (TextView) findViewById(R.id.textDeposit1);
        textDeposit2 = (TextView) findViewById(R.id.textDeposit2);
        textDeposit3 = (TextView) findViewById(R.id.textDeposit3);
        textDeposit4 = (TextView) findViewById(R.id.textDeposit4);
        textDeposit5 = (TextView) findViewById(R.id.textDeposit5);
        textDeposit6 = (TextView) findViewById(R.id.textDeposit6);

        FontUtils.changeFont(context, lblCurrentBalanceTV, FONT_CORBEL_REGULAR);
        FontUtils.changeFont(context, currentBalanceTV, FONT_ROBOTO_REGULAR);
        FontUtils.changeFont(context, currentInrBalanceTV, FONT_ROBOTO_REGULAR);
        FontUtils.changeFont(context, btnBankDetails, FONT_ROBOTO_REGULAR);
        FontUtils.changeFont(context, btnPayOnline, FONT_ROBOTO_REGULAR);
        FontUtils.changeFont(context, btnSubmitRequest, FONT_ROBOTO_REGULAR);
        FontUtils.changeFont(context, textDeposit1, FONT_ROBOTO_REGULAR);
        FontUtils.changeFont(context, textDeposit2, FONT_CORBEL_REGULAR);
        FontUtils.changeFont(context, textDeposit3, FONT_CORBEL_REGULAR);
        FontUtils.changeFont(context, textDeposit4, FONT_CORBEL_REGULAR);
        FontUtils.changeFont(context, textDeposit5, FONT_CORBEL_REGULAR);
        FontUtils.changeFont(context, textDeposit6, FONT_CORBEL_REGULAR);

        Typeface fontRobotoReg = Typeface.createFromAsset(getAssets(), FONT_ROBOTO_REGULAR);
        SpannableStringBuilder sb = new SpannableStringBuilder(textDeposit6.getText());
        TypefaceSpan robotoRegularSpan = new CustomTypefaceSpan("", fontRobotoReg);
        int length = textDeposit6.getText().length();
        sb.setSpan(robotoRegularSpan, 90, length - 1, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        textDeposit6.setText(sb);
        getBitcoinRate();
        getCurrentBalance();


    }

    @Override
    protected void initContext() {
        context = BuyActivity.this;
        currentActivity = BuyActivity.this;
    }

    @Override
    protected void initListners() {
        btnBankDetails.setOnClickListener(this);
        btnSubmitRequest.setOnClickListener(this);
        btnPayOnline.setOnClickListener(this);
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
            case R.id.btnBankDetails: {
                alert(currentActivity, "Alert", "This functionality is comming soon.", getResources().getString(R.string.alert_ok_button_text_no_network), getResources().getString(R.string.alert_cancel_button_text_no_network), false, true, ALERT_TYPE_NO_NETWORK);
                //startActivity(currentActivity, BankDetailsActivity.class, bundle, false, REQUEST_TAG_NO_RESULT, true, ANIMATION_SLIDE_UP);
                break;
            }

            case R.id.btnSubmitRequest: {
                alert(currentActivity, "Alert", "This functionality is comming soon.", getResources().getString(R.string.alert_ok_button_text_no_network), getResources().getString(R.string.alert_cancel_button_text_no_network), false, true, ALERT_TYPE_NO_NETWORK);
                //startActivity(currentActivity, SubmitRequestActivity.class, bundle, false, REQUEST_TAG_NO_RESULT, true, ANIMATION_SLIDE_UP);
                break;
            }
            case R.id.btnPayOnline: {
                startActivity(currentActivity, PayOnlineActivity.class, bundle, false, REQUEST_TAG_NO_RESULT, true, ANIMATION_SLIDE_UP);
                break;
            }
        }

    }

    @Override
    public void onAlertClicked(int alertType) {

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
                    logTesting(currentActivity.getResources().getString(R.string.nwk_response_get_balance), response.toString(), Log.ERROR);
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
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                currentBalanceProgressBar.setVisibility(View.GONE);
                logTesting(currentActivity.getResources().getString(R.string.nwk_error_get_balance), error.toString(), Log.ERROR);

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return super.getHeaders();
            }
        };

        BitPayApplication.getInstance().addToRequestQueue(getGetCurentBalanceRequest, "getGetCurentBalanceRequest5");
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
}
