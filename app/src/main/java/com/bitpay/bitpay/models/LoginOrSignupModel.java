package com.bitpay.bitpay.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Codeslay-03 on 6/22/2017.
 */

public class LoginOrSignupModel {

    private static LoginOrSignupModel loginOrSignupModel;

    private LoginOrSignupModel() {

    }

    public static LoginOrSignupModel getInstance() {
        if (loginOrSignupModel == null) {
            loginOrSignupModel = new LoginOrSignupModel();
        }
        return loginOrSignupModel;
    }

    @SerializedName("mobile_no")
    private String mobileNumber;
    @SerializedName("device_id")
    private String deviceId;
    @SerializedName("device_type")
    private String deviceType;
    @SerializedName("device_token")
    private String fcmToken;

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getFcmToken() {
        return fcmToken;
    }

    public void setFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }
}
