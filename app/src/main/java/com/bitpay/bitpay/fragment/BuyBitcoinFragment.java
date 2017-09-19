package com.bitpay.bitpay.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.TypefaceSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.bitpay.bitpay.activity.BaseActivity;
import com.bitpay.bitpay.activity.ExistingConfirmPinActivity;
import com.bitpay.bitpay.activity.PayUActivity;
import com.bitpay.bitpay.application.BitPayApplication;
import com.bitpay.bitpay.constant.AppConstants;
import com.bitpay.bitpay.utils.DeviceUtils;
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

import static android.app.Activity.RESULT_OK;

public class BuyBitcoinFragment extends BaseFragment {

    private LinearLayout bitcoinRateProgressBar;
    private LinearLayout currentBalanceProgressBar;
    private TextView lblCurrentBalanceTV;
    private TextView currentBalanceTV;
    private TextView lblBuyingRateTV;
    private TextView amtBuyingRateTV;
    private TextView lblMaxAmtTV;
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
    private Button buttonBuy;
    private TextView currentInrBalanceTV;
    private String currentBalance;
    private String buyingRate;
    private String rateAmtString;
    private String bitCoinAmtString;
    private double minTransferAmt;
    private double maxTransferAmt;

    public BuyBitcoinFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return view = inflater.inflate(R.layout.fragment_buy, container, false);
    }

    @Override
    public void alertOkClicked(int alertType) {

    }

    @Override
    protected void initViews() {
        bitcoinRateProgressBar = (LinearLayout) view.findViewById(R.id.bitcoinRateProgressBar);
        currentBalanceProgressBar = (LinearLayout) view.findViewById(R.id.currentBalanceProgressBar);
        lblCurrentBalanceTV = (TextView) view.findViewById(R.id.lblCurrentBalanceTV);
        currentBalanceTV = (TextView) view.findViewById(R.id.currentBalanceTV);
        lblBuyingRateTV = (TextView) view.findViewById(R.id.lblBuyingRateTV);
        amtBuyingRateTV = (TextView) view.findViewById(R.id.amtBuyingRateTV);
        currentInrBalanceTV = (TextView) view.findViewById(R.id.currentInrBalanceTV);
        rateToBitcoinLL = (LinearLayout) view.findViewById(R.id.rateToBitcoinLL);
        bitcoinToRateLL = (LinearLayout) view.findViewById(R.id.bitcoinToRateLL);
        validAmtTV = (TextView) view.findViewById(R.id.validAmtTV);
        inrCurrency1TV = (TextView) view.findViewById(R.id.inrCurrency1TV);
        editAmmountRate = (EditText) view.findViewById(R.id.editAmmountRate);
        symbollBitcoin1TV = (TextView) view.findViewById(R.id.symbollBitcoin1TV);
        amtBitcoinTV = (TextView) view.findViewById(R.id.amtBitcoinTV);
        inrCurrency2TV = (TextView) view.findViewById(R.id.inrCurrency2TV);
        editAmmountBit = (EditText) view.findViewById(R.id.editAmmountBit);
        symbollBitcoin2TV = (TextView) view.findViewById(R.id.symbollBitcoin2TV);
        amtRateTV = (TextView) view.findViewById(R.id.amtRateTV);
        bitRateIV = (ImageView) view.findViewById(R.id.bitRateIV);
        buttonBuy = (Button) view.findViewById(R.id.buttonBuy);
        lblMaxAmtTV = (TextView) view.findViewById(R.id.lblMaxAmtTV);

        FontUtils.changeFont(context, lblCurrentBalanceTV, FONT_CORBEL_REGULAR);
        FontUtils.changeFont(context, currentBalanceTV, FONT_ROBOTO_REGULAR);
        FontUtils.changeFont(context, lblBuyingRateTV, FONT_CORBEL_REGULAR);
        FontUtils.changeFont(context, amtBuyingRateTV, FONT_ROBOTO_REGULAR);
        FontUtils.changeFont(context, currentInrBalanceTV, FONT_ROBOTO_REGULAR);
        FontUtils.changeFont(context, buttonBuy, FONT_CORBEL_REGULAR);
        FontUtils.changeFont(context, inrCurrency1TV, FONT_BITCOIN_BOLD);
        FontUtils.changeFont(context, editAmmountRate, FONT_ROBOTO_REGULAR);
        FontUtils.changeFont(context, symbollBitcoin1TV, FONT_BITCOIN_BOLD);
        FontUtils.changeFont(context, amtBitcoinTV, FONT_ROBOTO_REGULAR);
        FontUtils.changeFont(context, inrCurrency2TV, FONT_BITCOIN_BOLD);
        FontUtils.changeFont(context, editAmmountBit, FONT_ROBOTO_REGULAR);
        FontUtils.changeFont(context, symbollBitcoin2TV, FONT_BITCOIN_BOLD);
        FontUtils.changeFont(context, amtRateTV, FONT_ROBOTO_REGULAR);
        FontUtils.changeFont(context, lblMaxAmtTV, FONT_ROBOTO_REGULAR);
        FontUtils.changeFont(context, validAmtTV, FONT_ROBOTO_REGULAR);
        
        Typeface fontRobotoReg = Typeface.createFromAsset(context.getAssets(), FONT_ROBOTO_REGULAR);
        Typeface fontBitReg = Typeface.createFromAsset(context.getAssets(), FONT_BITCOIN_REGULAR);
        SpannableStringBuilder sb = new SpannableStringBuilder(lblBuyingRateTV.getText());
        TypefaceSpan robotoRegularSpan = new CustomTypefaceSpan("", fontRobotoReg);
        TypefaceSpan bitRegularSpan = new CustomTypefaceSpan("", fontBitReg);
        sb.setSpan(robotoRegularSpan, 0, 1, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        sb.setSpan(bitRegularSpan, 1, 2, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        lblBuyingRateTV.setText(sb);

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
        context = getActivity();
        currentActivity = getActivity();
    }

    @Override
    protected void initListners() {
        buttonBuy.setOnClickListener(this);
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonBuy: {
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
                        bundle.putInt(KEY_TYPE, TYPE_BUY_BITCOIN);
                        ((BaseActivity) currentActivity).startActivity(currentActivity, ExistingConfirmPinActivity.class, bundle, true, REQUEST_TAG_CONFIRM_PIN, true, ANIMATION_SLIDE_UP);
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


    private void convertRsToBit(String inputRs) {
        try {
            if (TextUtils.isEmpty(buyingRate)) return;
            double sellRate = Double.parseDouble(buyingRate);
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
            if (TextUtils.isEmpty(buyingRate)) return;
            double sellRate = Double.parseDouble(buyingRate);
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
                        if (!TextUtils.isEmpty(buyingRate)) {
                            double buyRate = Double.parseDouble(buyingRate);
                            double inrValue = balance * buyRate;
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
                //((BaseActivity) currentActivity).toast(currentActivity.getResources().getString(R.string.nwk_error_get_balance), true);
                ((BaseActivity) currentActivity).logTesting(currentActivity.getResources().getString(R.string.nwk_error_get_balance), error.toString(), Log.ERROR);

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return super.getHeaders();
            }
        };

        BitPayApplication.getInstance().addToRequestQueue(getGetCurentBalanceRequest, "getGetCurentBalanceRequest1");
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
                        if (TextUtils.isEmpty(buyingRate)) {
                            return;
                        }
                        double buyRate = Double.parseDouble(buyingRate);
                        amtBuyingRateTV.setText(NumberFormat.getCurrencyInstance().format(buyRate));
                        if (!TextUtils.isEmpty(currentBalance)) {
                            double bitAmt = Double.parseDouble(currentBalance);
                            double inrValue = bitAmt * buyRate;
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

        BitPayApplication.getInstance().addToRequestQueue(getGetBitcoinRateRequest, "getGetBitcoinRateRequest1");
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
                if ((ammount < minTransferAmt || ammount > maxTransferAmt)) {
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
                if ((ammount < minTransferAmt || ammount > maxTransferAmt)) {
                    editAmmountRate.setError(getString(R.string.error_invalid_amt));
                    editAmmountRate.requestFocus();
                    return false;
                }
            }
        }
        initAmmount();
        return true;
    }

    private void initAmmount() {
        if (bitcoinToRateLL.getVisibility() == View.VISIBLE) {
            rateAmtString = amtRateTV.getText().toString();
            bitCoinAmtString = editAmmountBit.getText().toString();
        } else if (rateToBitcoinLL.getVisibility() == View.VISIBLE) {
            bitCoinAmtString = amtBitcoinTV.getText().toString();
            rateAmtString = editAmmountRate.getText().toString();
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        switch (requestCode) {
            case REQUEST_TAG_CONFIRM_PIN: {
                if (resultCode == RESULT_OK) {
                    showDialog();
                }
                break;
            }
        }
    }

    private void showDialog() {

        View customView = currentActivity.getLayoutInflater().inflate(R.layout.custom_buy_bitcoin_dialog, null);
        final Dialog dialog = new Dialog(currentActivity, R.style.CustomDialog);
        dialog.setContentView(customView);
        int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.9);
        int height = (int) (getResources().getDisplayMetrics().heightPixels * 0.37);
        dialog.getWindow().setLayout(width, height);

        LinearLayout networkFeeLL = (LinearLayout) dialog.findViewById(R.id.networkFeeLL);
        networkFeeLL.setVisibility(View.GONE);
        TextView lblBitAmtTV = (TextView) dialog.findViewById(R.id.lblBitAmtTV);
        TextView textBitAmtTV = (TextView) dialog.findViewById(R.id.textBitAmtTV);
        TextView lblRateAmtTV = (TextView) dialog.findViewById(R.id.lblRateAmtTV);
        TextView textRateAmtTV = (TextView) dialog.findViewById(R.id.textRateAmtTV);
        Button btnProceed = (Button) dialog.findViewById(R.id.btnProceed);
        Button btnCancel = (Button) dialog.findViewById(R.id.btnCancel);
        textBitAmtTV.setText(getString(R.string.text_bitcoin) + " " + bitCoinAmtString);
        textRateAmtTV.setText(getString(R.string.text_rs_currency) + " " + rateAmtString);

        FontUtils.changeFont(context, lblBitAmtTV, FONT_CORBEL_REGULAR);
        FontUtils.changeFont(context, textBitAmtTV, FONT_ROBOTO_REGULAR);
        FontUtils.changeFont(context, lblRateAmtTV, FONT_CORBEL_REGULAR);
        FontUtils.changeFont(context, textRateAmtTV, FONT_ROBOTO_REGULAR);
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

        btnProceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (Validator.getInstance().isNetworkAvailable(currentActivity)) {
                    if (bundle == null) {
                        bundle = new Bundle();
                    }
                    bundle.putString(KEY_RATE_AMMOUNT, rateAmtString);
                    bundle.putString(KEY_BIT_AMMOUNT, bitCoinAmtString);
                    ((BaseActivity) currentActivity).startActivity(currentActivity, PayUActivity.class, bundle, false, REQUEST_TAG_NO_RESULT, true, ANIMATION_SLIDE_UP);
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

    @Override
    public void onStop() {
        super.onStop();
        BitPayApplication.getInstance().cancelPendingRequest("getGetBitcoinRateRequest1");
        BitPayApplication.getInstance().cancelPendingRequest("getGetCurentBalanceRequest1");
    }

}
