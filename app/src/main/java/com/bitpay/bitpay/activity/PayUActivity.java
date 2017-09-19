package com.bitpay.bitpay.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;


import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bitpay.bitpay.R;
import com.bitpay.bitpay.application.BitPayApplication;
import com.bitpay.bitpay.constant.AppConstants;
import com.bitpay.bitpay.models.BuyBitcoinModel;
import com.bitpay.bitpay.models.DepositAmtModel;
import com.bitpay.bitpay.utils.SharedPreferenceUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.apache.http.util.EncodingUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;


public class PayUActivity extends BaseActivity {

    private static final String TAG = "PayUActivity";
    private WebView webviewPayment;
    private LinearLayout progressBarLL;
    private String emailId = "";
    private String userName = "";
    private String phoneNumber = "";
    private String ammountToBePaid;
    private String bitAmmount;
    private String txnid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payu);
    }

    @Override
    protected void initViews() {

        settingTitle(getResources().getString(R.string.title_pay_u_activity));
        progressBarLL = (LinearLayout) findViewById(R.id.progressBarLL);
        emailId = SharedPreferenceUtils.getInstance(context).getString(USER_EMAIL);
        userName = SharedPreferenceUtils.getInstance(context).getString(USER_NAME);
        phoneNumber = SharedPreferenceUtils.getInstance(context).getString(USER_MOBILE_NO);
        if (getIntent().getExtras() != null) {
            ammountToBePaid = getIntent().getExtras().getString(KEY_RATE_AMMOUNT);
            bitAmmount = getIntent().getExtras().getString(KEY_BIT_AMMOUNT);
        }
        webviewPayment = (WebView) findViewById(R.id.webviewPayment);
        webviewPayment.getSettings().setJavaScriptEnabled(true);
        webviewPayment.getSettings().setDomStorageEnabled(true);
        webviewPayment.getSettings().setLoadWithOverviewMode(true);
        webviewPayment.getSettings().setUseWideViewPort(true);
        StringBuilder url_s = new StringBuilder();
        url_s.append("https://secure.payu.in/_payment");
        //url_s.append("https://test.payu.in/_payment"); //test
        Log.e(TAG, "callurl " + url_s);

        webviewPayment.postUrl(url_s.toString(), EncodingUtils.getBytes(getPostString(), "utf-8"));
        webviewPayment.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                progressBarLL.setVisibility(View.GONE);
                Log.e("page load for order", "finish!" + url);
            }

            @SuppressWarnings("unused")
            public void onReceivedSslError(WebView view) {
                Log.e("Error for orde", "Exception caught!");
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {

                if (url.toString().equals(AppConstants.URL_PAYMENT_SUCCESS)) {
                    //toast(getResources().getString(R.string.msg_transaction_success), true);
                    initDepositAmtModel();
                } else if (url.toString().equals(AppConstants.URL_PAYMENT_FAIL)) {
                    alert(currentActivity, getString(R.string.msg_transaction_failed), getString(R.string.msg_transaction_failed), getResources().getString(R.string.alert_ok_button_text_no_network), getResources().getString(R.string.alert_cancel_button_text_no_network), false, false, ALERT_TYPE_NO_NETWORK);
                } else {
                    super.onPageStarted(view, url, favicon);
                }

            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.e("override page for orde", url.toString());
                return super.shouldOverrideUrlLoading(view, url);
            }

        });
    }

    @Override
    protected void initContext() {
        currentActivity = PayUActivity.this;
        context = PayUActivity.this;
    }

    @Override
    protected void initListners() {

    }

    @Override
    protected boolean isActionBar() {
        return true;
    }

    @Override
    protected boolean isHomeButton() {
        return false;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }

    private String getPostString() {

        /*  test credenials*/
       /* String key = "gtKFFx";
        String salt = "eCwWELxi";*/

         /*  production credenials*/
        String key = "Mi6cCI";
        String salt = "BMKIG4MW";

        txnid = String.valueOf(System.currentTimeMillis());
        String amount = ammountToBePaid;
        String firstname = userName;
        String email = emailId;
        String phone = phoneNumber;
        String productInfo = "Bitcoin";
        Log.e("first name email", firstname + email);

        StringBuilder post = new StringBuilder();
        post.append("key=");
        post.append(key);
        post.append("&");
        post.append("txnid=");
        post.append(txnid);
        post.append("&");
        post.append("amount=");
        post.append(amount);
        post.append("&");
        post.append("productinfo=");
        post.append(productInfo);
        post.append("&");
        post.append("firstname=");
        post.append(firstname);
        post.append("&");
        post.append("email=");
        post.append(email);
        post.append("&");
        post.append("phone=");
        post.append(phone);
        post.append("&");
        post.append("surl=");
        post.append("https://payu.herokuapp.com/success");
        post.append("&");
        post.append("furl=");
        post.append("https://payu.herokuapp.com/failure");
        post.append("&");
        post.append("service_provider=");
        post.append("payu_paisa");
        post.append("&");

        StringBuilder checkSumStr = new StringBuilder();
        MessageDigest digest = null;
        String hash;
        try {
            digest = MessageDigest.getInstance("SHA-512");

            checkSumStr.append(key);
            checkSumStr.append("|");
            checkSumStr.append(txnid);
            checkSumStr.append("|");
            checkSumStr.append(amount);
            checkSumStr.append("|");
            checkSumStr.append(productInfo);
            checkSumStr.append("|");
            checkSumStr.append(firstname);
            checkSumStr.append("|");
            checkSumStr.append(email);
            checkSumStr.append("|||||||||||");
            checkSumStr.append(salt);

            digest.update(checkSumStr.toString().getBytes());

            hash = bytesToHexString(digest.digest());
            post.append("hash=");
            post.append(hash);
            post.append("&");
            Log.i(TAG, "SHA result is " + hash);
        } catch (NoSuchAlgorithmException e1) {
            e1.printStackTrace();
        }
        return post.toString();
    }

    private static String bytesToHexString(byte[] bytes) {

        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(0xFF & bytes[i]);
            if (hex.length() == 1) {
                sb.append('0');
            }
            sb.append(hex);
        }
        return sb.toString();
    }

    @Override
    public void onClick(View view) {

    }


    @Override
    public void onBackPressed() {
        alert(currentActivity, getString(R.string.title_cancel_transaction), getString(R.string.message_cancel_transaction), getResources().getString(R.string.alert_ok_button_text_no_network), getResources().getString(R.string.alert_cancel_button_text_no_network), false, true, ALERT_TYPE_CANCEL_TRANSCATION);
    }

    @Override
    public void onAlertClicked(int alertType) {
        if (alertType == ALERT_TYPE_CANCEL_TRANSCATION) {
            finish();
        } else if (alertType == ALERT_TYPE_BUY_SELL_BITCOIN) {
            startActivity(currentActivity, DashboardActivity.class, bundle, false, REQUEST_TAG_NO_RESULT, true, ANIMATION_SLIDE_UP);
        }
    }

    private void initDepositAmtModel() {
        DepositAmtModel.getInstance().setUserId(SharedPreferenceUtils.getInstance(context).getInteger(USER_ID));
        DepositAmtModel.getInstance().setReferenceNo(txnid);
        DepositAmtModel.getInstance().setAmountDeposite(ammountToBePaid);
        DepositAmtModel.getInstance().setBitAmount(bitAmmount);
        DepositAmtModel.getInstance().setAccountNumber("PayUMoney");
        DepositAmtModel.getInstance().setImageName("");
        depositAmtRequest();
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
                        //alert(currentActivity, getString(R.string.msg_add_deposit_amt_suc), getString(R.string.msg_add_deposit_amt_suc), getResources().getString(R.string.alert_ok_button_text_no_network), getResources().getString(R.string.alert_cancel_button_text_no_network), false, false, ALERT_TYPE_BUY_SELL_BITCOIN);
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





    /*private void initBuyBitcoinModel() {
        if (TextUtils.isEmpty(bitAmmount) || TextUtils.isEmpty(ammountToBePaid)) return;
        BuyBitcoinModel.getInstance().setUserId(SharedPreferenceUtils.getInstance(context).getInteger(USER_ID));
        BuyBitcoinModel.getInstance().setBitAmounts(bitAmmount);
        BuyBitcoinModel.getInstance().setInrAmounts(ammountToBePaid);
        BuyBitcoinModel.getInstance().setTransectionId(txnid);
        buyBitcoins();
    }

    private void buyBitcoins() {

        progressDialog(context, context.getString(R.string.pdialog_message_loading), context.getString(R.string.pdialog_message_loading), false, false);

        JSONObject jsonBuyBitcoinsRequest = null;
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        try {
            jsonBuyBitcoinsRequest = new JSONObject(gson.toJson(BuyBitcoinModel.getInstance()));
            Log.e("jsonBuyBitcoinsRequest", jsonBuyBitcoinsRequest.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final String URL_BUY_BITCOINS = BUY_BITCOINS_URL;
        JsonObjectRequest buyBitcoinRequest = new JsonObjectRequest(Request.Method.POST, URL_BUY_BITCOINS, jsonBuyBitcoinsRequest, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    cancelProgressDialog();
                    ((BaseActivity) currentActivity).logTesting(getResources().getString(R.string.nwk_response_buy_bitcoin), response.toString(), Log.ERROR);
                    String message = response.getString(RESPONCE_MESSAGE);
                    if (response.getBoolean(RESPONCE_ERROR) || message.isEmpty()) {
                        alert(currentActivity, getString(R.string.msg_buy_bitcoins_failed), getString(R.string.msg_buy_bitcoins_failed), getResources().getString(R.string.alert_ok_button_text_no_network), getResources().getString(R.string.alert_cancel_button_text_no_network), false, false, ALERT_TYPE_NO_NETWORK);
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
                        bundle.putInt(KEY_TRANSACTION_TYPE, TYPE_BUY_BITCOIN);
                        bundle.putString(KEY_BIT_AMMOUNT, bitAmmount);
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
                toast(getResources().getString(R.string.nwk_error_buy_bitcoin), true);
                ((BaseActivity) currentActivity).logTesting(getResources().getString(R.string.nwk_error_buy_bitcoin), error.toString(), Log.ERROR);
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return super.getHeaders();
            }
        };
        BitPayApplication.getInstance().addToRequestQueue(buyBitcoinRequest);
    }
*/

}
