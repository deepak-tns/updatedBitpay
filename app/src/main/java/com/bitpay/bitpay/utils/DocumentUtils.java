package com.bitpay.bitpay.utils;

import android.content.Context;
import android.text.TextUtils;

import static com.bitpay.bitpay.constant.AppConstants.KEY_BANK_STATUS;
import static com.bitpay.bitpay.constant.AppConstants.KEY_EMAIL_STATUS;
import static com.bitpay.bitpay.constant.AppConstants.KEY_KYC_STATUS;
import static com.bitpay.bitpay.constant.AppConstants.KEY_PAN_STATUS;
import static com.bitpay.bitpay.constant.AppConstants.STATUS_APPROVED;
import static com.bitpay.bitpay.constant.AppConstants.STATUS_VERYFIED;

/**
 * Created by Codeslay-03 on 6/13/2017.
 */

public class DocumentUtils {

    public static boolean isDocumentApproved(Context context) {
        String panStatus = SharedPreferenceUtils.getInstance(context).getString(KEY_PAN_STATUS);
        String bankStatus = SharedPreferenceUtils.getInstance(context).getString(KEY_BANK_STATUS);
        int emailStatus = SharedPreferenceUtils.getInstance(context).getInteger(KEY_EMAIL_STATUS);
        String kycStatus = SharedPreferenceUtils.getInstance(context).getString(KEY_KYC_STATUS);
        if (TextUtils.isEmpty(panStatus) || TextUtils.isEmpty(bankStatus)) {
            return false;
        }
        if (panStatus.equalsIgnoreCase(STATUS_APPROVED) && bankStatus.equalsIgnoreCase(STATUS_APPROVED) && kycStatus.equalsIgnoreCase(STATUS_APPROVED) && emailStatus == STATUS_VERYFIED) {
            return true;
        } else {
            return false;
        }
    }
}
