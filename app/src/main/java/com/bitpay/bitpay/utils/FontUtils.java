package com.bitpay.bitpay.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.support.design.widget.TextInputLayout;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by Codeslay-03 on 2/8/2017.
 */
public class FontUtils {

    static Typeface customFont;

    public static void changeFont(Context context, TextView textView, String font) {

        customFont = Typeface.createFromAsset(context.getAssets(), font);

        textView.setTypeface(customFont);
    }

    public static void changeFont(Context context, Button button, String font) {

        customFont = Typeface.createFromAsset(context.getAssets(), font);

        button.setTypeface(customFont);
    }

    public static void changeFont(Context context, EditText edittext, String font) {

        customFont = Typeface.createFromAsset(context.getAssets(), font);

        edittext.setTypeface(customFont);
    }

    public static void changeFont(Context context, TextInputLayout textInputLayout, String font) {

        customFont = Typeface.createFromAsset(context.getAssets(), font);

        textInputLayout.setTypeface(customFont);
    }

}
