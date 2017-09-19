package com.bitpay.bitpay.receiver;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import com.bitpay.bitpay.R;
import com.bitpay.bitpay.activity.OtpValidationActivity;
import com.bitpay.bitpay.constant.AppConstants;
import com.bitpay.bitpay.interfaces.CancellingResendOtpThread;
import com.bitpay.bitpay.utils.SharedPreferenceUtils;

public class IncomingSms extends BroadcastReceiver implements AppConstants, CancellingResendOtpThread {


    final SmsManager sms = SmsManager.getDefault();
    private static String message;
    Context myContext;
    String phone;
    Activity callingActivityObj;
    int callingObjectType;

    public IncomingSms() {

    }

    public IncomingSms(Activity callingActivityObj) {

        this.callingActivityObj = callingActivityObj;
        Log.e("reciever registered", "by me");
    }

    public void onReceive(Context context, Intent intent) {
        Log.e("message recieved", " sms recieved");
        // Retrieves a map of extended data from the intent.
        final Bundle bundle = intent.getExtras();
        myContext = context;

        try {

            if (bundle != null) {

                final Object[] pdusObj = (Object[]) bundle.get("pdus");

                for (int i = 0; i < pdusObj.length; i++) {

                    SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
                    String phoneNumber = currentMessage.getDisplayOriginatingAddress();

                    String senderNum = phoneNumber;
                    message = currentMessage.getDisplayMessageBody();

                    Log.e("SmsReceiver", "senderNum: " + senderNum + "; message: " + message);

                    int otp = Integer.parseInt(SharedPreferenceUtils.getInstance(myContext).getString(KEY_OTP));
                    phone = SharedPreferenceUtils.getInstance(myContext).getString(KEY_MOBILE_NO);
                    Log.e("Recived otp", "" + Integer.parseInt(message.substring(37, 43).trim()));
                    Log.e("my otp", "" + "" + otp);
                    int incomingOtp = Integer.parseInt(message.substring(37, 43).trim());
                    if (otp == incomingOtp) {
                        LocalBroadcastManager.getInstance(context).unregisterReceiver(this);
                        cancelHandler(true);
                        ((OtpValidationActivity) myContext).autoFillOtp(otp);

                    } else {
                        Toast.makeText(context, context.getResources().getString(R.string.messageMobileNotVerified), Toast.LENGTH_LONG).show();
                    }
                }
            }

        } catch (Exception e) {
            Log.e("SmsReceiver", "Exception smsReceiver" + e);
        }


    }


    @Override
    public void cancelHandler(boolean iscancel) {

    }
}



