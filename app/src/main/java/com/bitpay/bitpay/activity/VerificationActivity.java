package com.bitpay.bitpay.activity;

import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.text.style.TypefaceSpan;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.bitpay.bitpay.R;
import com.bitpay.bitpay.utils.FontUtils;
import com.bitpay.bitpay.utils.Validator;
import com.bitpay.bitpay.widgets.CustomTypefaceSpan;


public class VerificationActivity extends BaseActivity {

    private EditText editMobileNumber;
    private EditText editCountryCode;
    private Button buttonSendOtp;
    private CheckBox termConditionCheck;
    private TextView pinDescTV;
    private TextView enterPinTV;
    private TextView readTermTV;
    private TextView termConditionTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);
    }

    @Override
    protected void initViews() {
        settingTitle(getString(R.string.title_verification));
        editMobileNumber = (EditText) findViewById(R.id.editMobileNumber);
        editCountryCode = (EditText) findViewById(R.id.editCountryCode);
        buttonSendOtp = (Button) findViewById(R.id.buttonSendOtp);
        termConditionCheck = (CheckBox) findViewById(R.id.termConditionCheck);
        pinDescTV = (TextView) findViewById(R.id.pinDescTV);
        enterPinTV = (TextView) findViewById(R.id.enterPinTV);
        readTermTV = (TextView) findViewById(R.id.readTermTV);
        termConditionTV = (TextView) findViewById(R.id.termConditionTV);
        FontUtils.changeFont(this, pinDescTV, FONT_CORBEL_REGULAR);
        FontUtils.changeFont(this, enterPinTV, FONT_CORBEL_REGULAR);
        FontUtils.changeFont(this, editMobileNumber, FONT_ROBOTO_REGULAR);
        FontUtils.changeFont(this, editCountryCode, FONT_ROBOTO_REGULAR);
        FontUtils.changeFont(this, readTermTV, FONT_CORBEL_REGULAR);
        FontUtils.changeFont(this, termConditionTV, FONT_CORBEL_REGULAR);
        FontUtils.changeFont(this, buttonSendOtp, FONT_CORBEL_BOLD);

        Typeface fontRobotoReg = Typeface.createFromAsset(getAssets(), FONT_ROBOTO_REGULAR);
        SpannableStringBuilder sb = new SpannableStringBuilder(enterPinTV.getText());
        TypefaceSpan robotoRegularSpan = new CustomTypefaceSpan("", fontRobotoReg);
        sb.setSpan(robotoRegularSpan, 18, 20, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        enterPinTV.setText(sb);
    }

    @Override
    protected void initContext() {
        context = VerificationActivity.this;
        currentActivity = VerificationActivity.this;
    }

    @Override
    protected void initListners() {
        buttonSendOtp.setOnClickListener(this);
        termConditionCheck.setOnClickListener(this);
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
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.termConditionCheck: {
                if (!termConditionCheck.isChecked()) {
                    buttonSendOtp.setEnabled(false);
                    buttonSendOtp.setAlpha(0.5f);
                } else {
                    buttonSendOtp.setEnabled(true);
                    buttonSendOtp.setAlpha(1.0f);
                }
                break;
            }
            case R.id.buttonSendOtp: {
                toHideKeyboard();
                if (Validator.getInstance().isNetworkAvailable(currentActivity)) {
                    if (isMandatoryFields()) {
                        if (bundle == null) {
                            bundle = new Bundle();
                        }
                        bundle.putString(KEY_MOBILE_NO, editMobileNumber.getText().toString());
                        startActivity(currentActivity, OtpValidationActivity.class, bundle, false, REQUEST_TAG_NO_RESULT, true, ANIMATION_SLIDE_UP);
                    }
                } else {
                    alert(currentActivity, getResources().getString(R.string.alert_message_no_network), getResources().getString(R.string.alert_message_no_network), getResources().getString(R.string.alert_ok_button_text_no_network), getResources().getString(R.string.alert_cancel_button_text_no_network), true, false, ALERT_TYPE_NO_NETWORK);
                }
                break;
            }
        }
    }

    @Override
    public void onAlertClicked(int alertType) {

    }

    private boolean isMandatoryFields() {
        editMobileNumber.setError(null);

        if (!Validator.getInstance().validateNumber(context, editMobileNumber.getText().toString()).equals("")) {
            String numberError = Validator.getInstance().validateNumber(context, editMobileNumber.getText().toString());
            editMobileNumber.setError(numberError);
            editMobileNumber.requestFocus();
            return false;

        }
        return true;
    }
}
