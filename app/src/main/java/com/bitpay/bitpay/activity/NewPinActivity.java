package com.bitpay.bitpay.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bitpay.bitpay.R;
import com.bitpay.bitpay.utils.FontUtils;

public class NewPinActivity extends BaseActivity {
    private EditText pinBox0;
    private EditText pinBox1;
    private EditText pinBox2;
    private EditText pinBox3;
    private EditText[] pinBoxArray;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_pin);
    }

    @Override
    protected void initViews() {
        settingTitle(getString(R.string.title_new_pin));
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
        enterPinTV = (TextView) findViewById(R.id.enterPinTV);
        pinDescTV = (TextView) findViewById(R.id.pinDescTV);
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
    }

    @Override
    protected void initContext() {
        context = NewPinActivity.this;
        currentActivity = NewPinActivity.this;
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
                        if (bundle == null) {
                            bundle = new Bundle();
                        }
                        bundle.putString(KEY_USER_PIN, userEntered);
                        startActivity(currentActivity, ConfirmPinActivity.class, bundle, false, REQUEST_TAG_NO_RESULT, true, ANIMATION_SLIDE_UP);
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
}
