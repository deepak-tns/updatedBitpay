package com.bitpay.bitpay.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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
import com.bitpay.bitpay.utils.Validator;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class ConfirmPinActivity extends BaseActivity {
    private EditText pinBox0;
    private EditText pinBox1;
    private EditText pinBox2;
    private EditText pinBox3;
    private EditText[] pinBoxArray;
    private TextView statusView;
    private Button button0;
    private Button button1;
    private Button button2;
    private Button button3;
    private Button button4;
    private Button button5;
    private Button button6;
    private Button button7;
    private Button button8;
    private Button button9;
    private ImageButton buttonDeleteBack;
    private TextView pinDescTV;
    private TextView enterPinTV;
    private String userEntered;
    private String userEnteredPin;
    private String userMobileNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_pin);
    }

    @Override
    protected void initViews() {
        settingTitle(getString(R.string.title_confirm_pin));
        userEntered = "";
        pinBox0 = (EditText) findViewById(R.id.pinBox0);
        pinBox1 = (EditText) findViewById(R.id.pinBox1);
        pinBox2 = (EditText) findViewById(R.id.pinBox2);
        pinBox3 = (EditText) findViewById(R.id.pinBox3);
        pinBoxArray = new EditText[PIN_LENGTH];
        pinBoxArray[0] = pinBox0;
        pinBoxArray[1] = pinBox1;
        pinBoxArray[2] = pinBox2;
        pinBoxArray[3] = pinBox3;
        button0 = (Button) findViewById(R.id.button0);
        button1 = (Button) findViewById(R.id.button1);
        button2 = (Button) findViewById(R.id.button2);
        button3 = (Button) findViewById(R.id.button3);
        button4 = (Button) findViewById(R.id.button4);
        button5 = (Button) findViewById(R.id.button5);
        button6 = (Button) findViewById(R.id.button6);
        button7 = (Button) findViewById(R.id.button7);
        button8 = (Button) findViewById(R.id.button8);
        button9 = (Button) findViewById(R.id.button9);
        buttonDeleteBack = (ImageButton) findViewById(R.id.buttonDeleteBack);
        statusView = (TextView) findViewById(R.id.statusMessage);
        enterPinTV = (TextView) findViewById(R.id.enterPinTV);
        pinDescTV = (TextView) findViewById(R.id.pinDescTV);

        FontUtils.changeFont(this, statusView, FONT_CORBEL_REGULAR);
        FontUtils.changeFont(this, enterPinTV, FONT_CORBEL_REGULAR);
        FontUtils.changeFont(this, pinDescTV, FONT_CORBEL_ITALIC);
        FontUtils.changeFont(this, button0, FONT_ROBOTO_REGULAR);
        FontUtils.changeFont(this, button1, FONT_ROBOTO_REGULAR);
        FontUtils.changeFont(this, button2, FONT_ROBOTO_REGULAR);
        FontUtils.changeFont(this, button3, FONT_ROBOTO_REGULAR);
        FontUtils.changeFont(this, button4, FONT_ROBOTO_REGULAR);
        FontUtils.changeFont(this, button5, FONT_ROBOTO_REGULAR);
        FontUtils.changeFont(this, button6, FONT_ROBOTO_REGULAR);
        FontUtils.changeFont(this, button7, FONT_ROBOTO_REGULAR);
        FontUtils.changeFont(this, button8, FONT_ROBOTO_REGULAR);
        FontUtils.changeFont(this, button9, FONT_ROBOTO_REGULAR);

        if (getIntent().getExtras() != null) {
            userEnteredPin = getIntent().getExtras().getString(KEY_USER_PIN);
        }
        userMobileNumber = SharedPreferenceUtils.getInstance(context).getString(USER_MOBILE_NO);
    }

    @Override
    protected void initContext() {
        context = ConfirmPinActivity.this;
        currentActivity = ConfirmPinActivity.this;
    }

    @Override
    protected void initListners() {

        View.OnClickListener pinButtonHandler = new View.OnClickListener() {
            public void onClick(View v) {

                Button pressedButton = (Button) v;
                if (userEntered.length() < PIN_LENGTH) {
                    userEntered = userEntered + pressedButton.getText();
                    //Update pin boxes
                    pinBoxArray[userEntered.length() - 1].setText(pressedButton.getText());
                    if (userEntered.length() == PIN_LENGTH) {
                        if (TextUtils.isEmpty(userEnteredPin)) return;
                        //Check if entered PIN is correct
                        if (userEntered.equals(userEnteredPin)) {
                            if (Validator.getInstance().isNetworkAvailable(currentActivity)) {
                                resetSecurePin();
                            } else {
                                alert(currentActivity, getResources().getString(R.string.alert_message_no_network), getResources().getString(R.string.alert_message_no_network), getResources().getString(R.string.alert_ok_button_text_no_network), getResources().getString(R.string.alert_cancel_button_text_no_network), true, false, ALERT_TYPE_NO_NETWORK);
                            }
                        } else {
                            statusView.setText(getString(R.string.msgErrorWrongPin));
                            userEntered = "";
                            pinBoxArray[0].setText("");
                            pinBoxArray[1].setText("");
                            pinBoxArray[2].setText("");
                            pinBoxArray[3].setText("");
                        }
                    }
                }
            }
        };

        button0.setOnClickListener(pinButtonHandler);
        button1.setOnClickListener(pinButtonHandler);
        button2.setOnClickListener(pinButtonHandler);
        button3.setOnClickListener(pinButtonHandler);
        button4.setOnClickListener(pinButtonHandler);
        button5.setOnClickListener(pinButtonHandler);
        button6.setOnClickListener(pinButtonHandler);
        button7.setOnClickListener(pinButtonHandler);
        button8.setOnClickListener(pinButtonHandler);
        button9.setOnClickListener(pinButtonHandler);
        buttonDeleteBack.setOnClickListener(this);
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
            case R.id.buttonDeleteBack: {
                if (userEntered.length() > 0) {
                    userEntered = userEntered.substring(0, userEntered.length() - 1);
                    pinBoxArray[userEntered.length()].setText("");
                }
                break;
            }
        }
    }

    @Override
    public void onAlertClicked(int alertType) {

    }

    private void resetSecurePin() {

        progressDialog(context, context.getString(R.string.pdialog_message_loading), context.getString(R.string.pdialog_message_loading), false, false);

        JSONObject jsonresetPinRequest = null;
        try {
            jsonresetPinRequest = new JSONObject();
            jsonresetPinRequest.put(USER_MOBILE_NO, userMobileNumber);
            jsonresetPinRequest.put(USER_SECURE_PIN, userEnteredPin);
            Log.e("jsonresetPinRequest", jsonresetPinRequest.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final String URL_RESET_PIN = RESET_PIN_URL;
        JsonObjectRequest resetPinRequest = new JsonObjectRequest(Request.Method.POST, URL_RESET_PIN, jsonresetPinRequest, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    cancelProgressDialog();
                    logTesting(getResources().getString(R.string.nwk_response_reset_pin), response.toString(), Log.ERROR);
                    String message = response.getString(RESPONCE_MESSAGE);
                    if (response.getBoolean(RESPONCE_ERROR) || message.isEmpty()) {
                        alert(currentActivity, getString(R.string.msg_reset_pin_failed), getString(R.string.msg_reset_pin_failed), getResources().getString(R.string.alert_ok_button_text_no_network), getResources().getString(R.string.alert_cancel_button_text_no_network), true, true, ALERT_TYPE_NO_NETWORK);
                    } else {
                        SharedPreferenceUtils.getInstance(context).putString(USER_SECURE_PIN, userEnteredPin);
                        startActivity(currentActivity, DashboardActivity.class, bundle, false, REQUEST_TAG_NO_RESULT, true, ANIMATION_SLIDE_UP);
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
                toast(getResources().getString(R.string.nwk_error_reset_pin), true);
                logTesting(getResources().getString(R.string.nwk_error_reset_pin), error.toString(), Log.ERROR);

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return super.getHeaders();
            }
        };
        BitPayApplication.getInstance().addToRequestQueue(resetPinRequest);
    }
}
