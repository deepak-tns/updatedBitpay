package com.bitpay.bitpay.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.design.widget.TextInputLayout;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.TypefaceSpan;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bitpay.bitpay.R;
import com.bitpay.bitpay.application.BitPayApplication;
import com.bitpay.bitpay.interfaces.CancellingResendOtpThread;
import com.bitpay.bitpay.models.AddContactModel;
import com.bitpay.bitpay.models.LoginOrSignupModel;
import com.bitpay.bitpay.models.ProfileDetailsModel;
import com.bitpay.bitpay.receiver.IncomingSms;
import com.bitpay.bitpay.utils.DeviceUtils;
import com.bitpay.bitpay.utils.FontUtils;
import com.bitpay.bitpay.utils.Helper;
import com.bitpay.bitpay.utils.SharedPreferenceUtils;
import com.bitpay.bitpay.utils.Validator;
import com.bitpay.bitpay.widgets.CustomTypefaceSpan;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;


public class OtpValidationActivity extends BaseActivity implements View.OnClickListener, CancellingResendOtpThread {


    private EditText edtOtp;
    private Button btnSubmit;
    private Button btnResendOtp;

    public static Runnable r;
    public static Handler handler;
    public String textMobileNo;
    private SendOTP sendOTP;
    private int otp;
    private String suppliededOtp;

    private BroadcastReceiver smsReciever;
    private Boolean recieverRegistered = false;

    private int oldOtp;

    static Handler handlerButtonVisibility;
    public static Runnable rButtonVisibility;
    private CountDownTimer countDownTimer;

    private TextView textViewTimer;
    private CountDownTimer timerForUnregisteringBroadcastReciever;
    private boolean istimerForUnregisteringBroadcastRecieverrunning = false;
    private int intNumberOfTimesResendPress = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_validation);
    }

    public void onCreateWork() {
        Bundle data = getIntent().getExtras();
        if (data == null) {
            return;
        }
        textMobileNo = data.getString(KEY_MOBILE_NO);
        handler = new Handler();

        SharedPreferenceUtils.getInstance(context).putString(KEY_MOBILE_NO, textMobileNo.toString().trim());
        otp = Helper.randomNumberCreation(100000, 999999);
        SharedPreferenceUtils.getInstance(context).putString(KEY_OTP, String.valueOf(otp));
        smsReciever = new IncomingSms(OtpValidationActivity.this);

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(EVENT_SMS_RECEIVED);
        intentFilter.setPriority(PRIORITY_HIGH);
        registerReceiver(smsReciever, intentFilter);

        sendOTP = new SendOTP();

        if (Validator.isNetworkAvailable(context)) {
            btnResendOtp.setClickable(false);
            btnResendOtp.setAlpha(0.5f);
            functionEnablingButtons();
            functionInitializingCountDownTimer();
            sendOTP.execute();
            recieverRegistered = true;
            functionForUnregisteringBroadcastReceiever();
        } else {
            alert(currentActivity, getString(R.string.alert_message_no_network), getString(R.string.alert_message_no_network), getString(R.string.alert_ok_button_text_no_network), getString(R.string.alert_cancel_button_text_no_network), false, false, ALERT_TYPE_NO_NETWORK);
        }
    }

    @Override
    protected void initViews() {
        settingTitle(getString(R.string.title_otp));
        bundle = new Bundle();
        edtOtp = (EditText) findViewById(R.id.edtOtp);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        btnResendOtp = (Button) findViewById(R.id.btnResendOtp);
        textViewTimer = (TextView) findViewById(R.id.textViewTimer);
        FontUtils.changeFont(this, edtOtp, FONT_ROBOTO_REGULAR);
        FontUtils.changeFont(this, textViewTimer, FONT_ROBOTO_REGULAR);
        FontUtils.changeFont(this, btnResendOtp, FONT_CORBEL_BOLD);
        FontUtils.changeFont(this, btnSubmit, FONT_CORBEL_BOLD);

        /*Typeface fontRobotoReg = Typeface.createFromAsset(getAssets(), FONT_ROBOTO_REGULAR);
        SpannableStringBuilder sb = new SpannableStringBuilder(textViewTimer.getText());
        TypefaceSpan robotoRegularSpan = new CustomTypefaceSpan("", fontRobotoReg);
        sb.setSpan(robotoRegularSpan, 0, 6, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        textViewTimer.setText(sb);*/
        onCreateWork();
    }

    @Override
    protected void initContext() {
        currentActivity = OtpValidationActivity.this;
        context = OtpValidationActivity.this;
    }

    @Override
    protected void initListners() {
        btnSubmit.setOnClickListener(this);
        btnResendOtp.setOnClickListener(this);
    }

    @Override
    public boolean isActionBar() {
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
                if (Validator.getInstance().isNetworkAvailable(currentActivity)) {
                    suppliededOtp = edtOtp.getText().toString();
                    if (suppliededOtp.equals("")) {
                        toast(getResources().getString(R.string.emptyOtp), true);
                    } else {
                        int otp = Integer.parseInt(SharedPreferenceUtils.getInstance(context).getString(KEY_OTP));
                        if (Integer.parseInt(suppliededOtp) == otp) {
                            if (recieverRegistered == true) {
                                unregisterReceiver(smsReciever);
                                recieverRegistered = false;
                            }
                            if (!sendOTP.isCancelled()) {
                                sendOTP.cancel(true);
                            }
                            if (istimerForUnregisteringBroadcastRecieverrunning) {
                                timerForUnregisteringBroadcastReciever.cancel();
                                istimerForUnregisteringBroadcastRecieverrunning = false;
                            }
                            toast(getResources().getString(R.string.messageNoValidated), true);
                            numberVerified();
                        } else {
                            toast(getResources().getString(R.string.messageOtpInvalid), true);
                        }
                    }
                } else {
                    alert(currentActivity, getResources().getString(R.string.alert_message_no_network), getResources().getString(R.string.alert_message_no_network), getResources().getString(R.string.alert_ok_button_text_no_network), getResources().getString(R.string.alert_cancel_button_text_no_network), true, false, ALERT_TYPE_NO_NETWORK);
                }
                break;
            }
            case R.id.btnResendOtp: {
                toHideKeyboard();
                functionInitializingCountDownTimer();
                intNumberOfTimesResendPress = intNumberOfTimesResendPress + 1;

                if (intNumberOfTimesResendPress <= RESET_LIMIT) {
                    if (istimerForUnregisteringBroadcastRecieverrunning) {
                        timerForUnregisteringBroadcastReciever.cancel();
                        istimerForUnregisteringBroadcastRecieverrunning = false;
                    }
                    functionForUnregisteringBroadcastReceiever();
                    if (!sendOTP.isCancelled()) {
                        sendOTP.cancel(true);
                    }

                    sendOTP = new SendOTP();
                    if (Validator.isNetworkAvailable(context)) {
                        sendOTP.execute();
                        btnResendOtp.setClickable(false);
                        btnResendOtp.setAlpha(0.5f);
                        functionEnablingButtons();
                        toast(getResources().getString(R.string.messageOtpResent), true);
                    } else {
                        alert(currentActivity, getString(R.string.alert_message_no_network), getString(R.string.alert_message_no_network), getString(R.string.alert_ok_button_text_no_network), getString(R.string.alert_cancel_button_text_no_network), false, false, ALERT_TYPE_NO_NETWORK);
                    }
                }
                break;
            }
        }
    }


    @Override
    public void cancelHandler(boolean isCancel) {
        handlerButtonVisibility.removeCallbacks(rButtonVisibility);
        if (!sendOTP.isCancelled()) {
            sendOTP.cancel(true);
        }

        countDownTimer.cancel();
        timerForUnregisteringBroadcastReciever.cancel();
    }

    public void functionEnablingButtons() {
        rButtonVisibility = new Runnable() {
            @Override
            public void run() {
                btnResendOtp.setClickable(true);
                btnResendOtp.setAlpha(1);

            }
        };
        handlerButtonVisibility = new Handler();
        handlerButtonVisibility.postDelayed(rButtonVisibility, longTimeAfterWhichButtonEnable);
    }


    @Override
    public void onAlertClicked(int alertType) {

    }

    public class SendOTP extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            oldOtp = Integer.parseInt(SharedPreferenceUtils.getInstance(context).getString(KEY_OTP));
            Log.i("Otp is", "" + oldOtp);
        }

        @Override
        protected String doInBackground(String... params) {


            String message = "Your%20BitPay%20OTP%20verification%20code%20is%20" + oldOtp;

            new RequestTask().execute("http://sms.digimiles.in/bulksms/bulksms?username=di78-bitpay&password=digimile&type=0&dlr=1&destination=" + textMobileNo + "&source=BitPay&message=" + message);

            //String urlAddress = "http://sms.digimiles.in/bulksms/bulksms?username=di78-bitpay&password=digimile&type=0&dlr=1&destination=" + textMobileNo + "&source=BitPay&message=" + message;
            //Log.e("urlAddress", urlAddress);
            return "";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }


    public void functionInitializingCountDownTimer() {
        textViewTimer.setVisibility(View.VISIBLE);
        countDownTimer = new CountDownTimer(longTotalVerificationTime, longOneSecond) {
            @Override
            public void onTick(long millisUntilFinished) {
                textViewTimer.setVisibility(View.VISIBLE);
                int remainingIntTime = (int) millisUntilFinished / 1000;
                String remainingTime = "";
                if (remainingIntTime < 10) {
                    remainingTime = "0" + remainingIntTime;
                } else {
                    remainingTime = "" + remainingIntTime;
                }
                textViewTimer.setText("00:" + remainingTime + " " + "sec");
            }

            @Override
            public void onFinish() {
                textViewTimer.setVisibility(View.INVISIBLE);
                edtOtp.setFocusable(true);

                edtOtp.requestFocus();
                edtOtp.setFocusableInTouchMode(true);
                edtOtp.setCursorVisible(true);
                edtOtp.invalidate();
            }
        }.start();
    }

    public void functionForUnregisteringBroadcastReceiever() {
        istimerForUnregisteringBroadcastRecieverrunning = true;
        timerForUnregisteringBroadcastReciever = new CountDownTimer(timeafterwhichRecieverUnregister, 120000) {

            @Override
            public void onTick(long millisUntilFinished) {
            }

            @Override
            public void onFinish() {
                if (recieverRegistered == true) {
                    unregisterReceiver(smsReciever);
                    recieverRegistered = false;
                }
            }
        };
    }


    /*class to Request the Gupshup service to send otp Start*/
    class RequestTask extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... uri) {
            URL url = null;
            try {
                url = new URL(uri[0]);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            try {
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                conn.setRequestMethod("GET");
                conn.setDoOutput(true);
                conn.setDoInput(true);
                conn.setUseCaches(false);
                conn.connect();
                BufferedReader rd = new BufferedReader(new
                        InputStreamReader(conn.getInputStream()));
                String line;
                StringBuffer buffer = new StringBuffer();
                while ((line = rd.readLine()) != null) {
                    buffer.append(line).append("\n");
                }
                System.out.println(buffer.toString());
                rd.close();
                conn.disconnect();


            } catch (IOException e) {
                e.printStackTrace();
            }

            String responseString = "";
            return responseString;
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            //Do anything with response..
        }

    }

    public void autoFillOtp(int otp) {

        final String stringOtp = String.valueOf(otp);
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                edtOtp.setText(stringOtp);
                textViewTimer.setText("00:00" + " " + "sec");
                btnResendOtp.setClickable(true);
                btnResendOtp.setAlpha(1);
                cancelHandler(true);
                autoSubmitOtp();
            }
        });
    }

    public void autoSubmitOtp() {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                toast(getResources().getString(R.string.messageNoValidated), true);
                numberVerified();
            }
        }, 1000);
    }

    // to do the operation
    public void numberVerified() {
        initSignUpModel();
        loginOrSignUp();
    }

    private void initSignUpModel() {
        LoginOrSignupModel.getInstance().setMobileNumber(textMobileNo);
        String deviceUUID = DeviceUtils.getDeviceUUID(getApplicationContext());
        LoginOrSignupModel.getInstance().setDeviceId(deviceUUID);
        LoginOrSignupModel.getInstance().setDeviceType(DEVICE_TYPE);
        String fcmToken = SharedPreferenceUtils.getInstance(context).getString(FCM_TOKEN);
        if (!TextUtils.isEmpty(fcmToken)) {
            LoginOrSignupModel.getInstance().setFcmToken(fcmToken);
        } else {
            fcmToken = FirebaseInstanceId.getInstance().getToken();
            LoginOrSignupModel.getInstance().setFcmToken(fcmToken);
        }
    }

    private void loginOrSignUp() {

        progressDialog(context, context.getString(R.string.pdialog_message_loading), context.getString(R.string.pdialog_message_loading), false, false);

        JSONObject jsonSignUpRequest = null;
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        try {
            jsonSignUpRequest = new JSONObject(gson.toJson(LoginOrSignupModel.getInstance()));
            Log.e("jsonSignUpRequest", jsonSignUpRequest.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final String URL_SIGN_UP = SIGNUP_URL;
        JsonObjectRequest signUpRequest = new JsonObjectRequest(Request.Method.POST, URL_SIGN_UP, jsonSignUpRequest, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    cancelProgressDialog();
                    logTesting(getResources().getString(R.string.nwk_response_sign_up), response.toString(), Log.ERROR);
                    String message = response.getString(RESPONCE_MESSAGE);
                    if (response.getBoolean(RESPONCE_ERROR) || message.isEmpty()) {
                        alert(currentActivity, getString(R.string.msg_reg_failed), getString(R.string.msg_reg_failed), getResources().getString(R.string.alert_ok_button_text_no_network), getResources().getString(R.string.alert_cancel_button_text_no_network), true, true, ALERT_TYPE_NO_NETWORK);
                    } else {
                        JSONObject messageJson = new JSONObject(message);
                        SharedPreferenceUtils.getInstance(currentActivity).putInteger(USER_ID, messageJson.getInt(USER_ID));
                        SharedPreferenceUtils.getInstance(currentActivity).putString(USER_MOBILE_NO, textMobileNo);
                        SharedPreferenceUtils.getInstance(currentActivity).putString(USER_BIT_ADDRESS, messageJson.getString(USER_BIT_ADDRESS));
                        SharedPreferenceUtils.getInstance(currentActivity).putString(USER_BIT_USER_ID, messageJson.getString(USER_BIT_USER_ID));
                        if (messageJson.has("name")) {
                            SharedPreferenceUtils.getInstance(currentActivity).putString(USER_NAME, messageJson.getString("name"));
                        }
                        if (messageJson.has("email")) {
                            SharedPreferenceUtils.getInstance(currentActivity).putString(USER_EMAIL, messageJson.getString("email"));
                        }
                        if (messageJson.has("profile_image")) {
                            SharedPreferenceUtils.getInstance(context).putString(USER_PROFILE_IMAGE, messageJson.getString("profile_image"));
                        }

                        //SharedPreferenceUtils.getInstance(currentActivity).putString(USER_SECURE_PIN, messageJson.getString(USER_SECURE_PIN));
                        int userExists = messageJson.getInt(IS_USER_EXITS);
                        String securePin = messageJson.getString(USER_SECURE_PIN);
                        if (userExists == 1 && !TextUtils.isEmpty(securePin)) {
                            if (bundle == null) {
                                bundle = new Bundle();
                            }
                            bundle.putString(USER_SECURE_PIN, securePin);
                            startActivity(currentActivity, ExistingConfirmPinActivity.class, bundle, false, REQUEST_TAG_NO_RESULT, true, ANIMATION_SLIDE_UP);
                        } else {
                            startActivity(currentActivity, NewPinActivity.class, bundle, false, REQUEST_TAG_NO_RESULT, true, ANIMATION_SLIDE_UP);
                        }
                        finish();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                cancelProgressDialog();
                toast(getResources().getString(R.string.nwk_error_sign_up), true);
                logTesting(getResources().getString(R.string.nwk_error_sign_up), error.toString(), Log.ERROR);

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return super.getHeaders();
            }
        };
        BitPayApplication.getInstance().addToRequestQueue(signUpRequest);
    }


    @Override
    protected void onDestroy() {
        if (smsReciever != null) {
            if (recieverRegistered == true) {
                unregisterReceiver(smsReciever);
                recieverRegistered = false;
            }
        }
        super.onDestroy();
    }

}