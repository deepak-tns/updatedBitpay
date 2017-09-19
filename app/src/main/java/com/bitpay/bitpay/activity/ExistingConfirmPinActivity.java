package com.bitpay.bitpay.activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.TypefaceSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bitpay.bitpay.R;
import com.bitpay.bitpay.utils.FontUtils;
import com.bitpay.bitpay.utils.SharedPreferenceUtils;
import com.bitpay.bitpay.widgets.CustomTypefaceSpan;

public class ExistingConfirmPinActivity extends BaseActivity {
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
    private TextView forgotPinTV;
    private TextView pinDescTV;
    private TextView enterPinTV;

    private String userEntered;
    private String userEnteredPin;
    private int errorPinCount = 4;
    private int verificationType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_existing_confirm_pin);
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
        forgotPinTV = (TextView) findViewById(R.id.forgotPinTV);
        statusView = (TextView) findViewById(R.id.statusMessage);
        enterPinTV = (TextView) findViewById(R.id.enterPinTV);
        pinDescTV = (TextView) findViewById(R.id.pinDescTV);

        FontUtils.changeFont(this, enterPinTV, FONT_CORBEL_REGULAR);
        FontUtils.changeFont(this, pinDescTV, FONT_CORBEL_ITALIC);
        FontUtils.changeFont(this, statusView, FONT_CORBEL_REGULAR);
        FontUtils.changeFont(this, forgotPinTV, FONT_CORBEL_REGULAR);
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
            userEnteredPin = getIntent().getExtras().getString(USER_SECURE_PIN);
            verificationType = getIntent().getExtras().getInt(KEY_TYPE);
            if (verificationType == TYPE_RESET_PIN || verificationType == TYPE_SELL_BITCOIN || verificationType == TYPE_BUY_BITCOIN || verificationType == TYPE_SEND_BITCOIN) {
                userEnteredPin = SharedPreferenceUtils.getInstance(context).getString(USER_SECURE_PIN);
            }
        }
        if (verificationType == TYPE_RESET_PIN) {
            settingTitle(getString(R.string.title_old_pin));
        }
    }

    @Override
    protected void initContext() {
        context = ExistingConfirmPinActivity.this;
        currentActivity = ExistingConfirmPinActivity.this;
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
                        //Check if entered PIN is correct
                        if (TextUtils.isEmpty(userEnteredPin)) return;
                        if (userEntered.equals(userEnteredPin)) {
                            if (verificationType == TYPE_SELL_BITCOIN || verificationType == TYPE_BUY_BITCOIN || verificationType == TYPE_SEND_BITCOIN) {
                                Intent intent = new Intent();
                                intent.putExtra(KEY_TYPE, verificationType);
                                setResult(RESULT_OK, intent);
                                finish();
                            } else if (verificationType == TYPE_RESET_PIN) {
                                startActivity(currentActivity, NewPinActivity.class, bundle, false, REQUEST_TAG_NO_RESULT, true, ANIMATION_SLIDE_UP);
                                finish();
                            } else {
                                SharedPreferenceUtils.getInstance(context).putString(USER_SECURE_PIN, userEnteredPin);
                                startActivity(currentActivity, DashboardActivity.class, bundle, false, REQUEST_TAG_NO_RESULT, true, ANIMATION_SLIDE_UP);
                                finish();
                            }
                        } else {
                            errorPinCount--;
                            if (errorPinCount == 0) {
                                finish();
                                return;
                            }
                            statusView.setVisibility(View.VISIBLE);
                            String errorString = getString(R.string.msgErrorPin);
                            errorString = errorString.replace("0", "" + errorPinCount);
                            statusView.setText(errorString);

                            Typeface fontRobotoReg = Typeface.createFromAsset(getAssets(), FONT_ROBOTO_REGULAR);
                            SpannableStringBuilder sb = new SpannableStringBuilder(statusView.getText());
                            TypefaceSpan robotoRegularSpan = new CustomTypefaceSpan("", fontRobotoReg);
                            sb.setSpan(robotoRegularSpan, 18, 19, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                            statusView.setText(sb);

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
        forgotPinTV.setOnClickListener(this);
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
            case R.id.forgotPinTV: {
                startActivity(currentActivity, NewPinActivity.class, bundle, false, REQUEST_TAG_NO_RESULT, true, ANIMATION_SLIDE_UP);
                break;
            }
        }
    }

    @Override
    public void onAlertClicked(int alertType) {

    }
}
