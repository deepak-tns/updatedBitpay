package com.bitpay.bitpay.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Codeslay-03 on 8/17/2017.
 */

public class KycDetailsModel {

    private static KycDetailsModel kycDetailsModel;

    private KycDetailsModel() {

    }

    public static KycDetailsModel getInstance() {
        if (kycDetailsModel == null) {
            kycDetailsModel = new KycDetailsModel();
        }
        return kycDetailsModel;
    }

    @SerializedName("user_id")
    private int userId;
    @SerializedName("address_line1")
    private String addressLine1;
    @SerializedName("address_line2")
    private String addressLine2;
    private String landmark;
    private String city;
    private String state;
    @SerializedName("pincode")
    private String pinCode;
    @SerializedName("image")
    private String imageName;
    @SerializedName("back_image")
    private String backImage;
    private String status;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getAddressLine1() {
        return addressLine1;
    }

    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }

    public String getAddressLine2() {
        return addressLine2;
    }

    public void setAddressLine2(String addressLine2) {
        this.addressLine2 = addressLine2;
    }

    public String getLandmark() {
        return landmark;
    }

    public void setLandmark(String landmark) {
        this.landmark = landmark;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPinCode() {
        return pinCode;
    }

    public void setPinCode(String pinCode) {
        this.pinCode = pinCode;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getBackImage() {
        return backImage;
    }

    public void setBackImage(String backImage) {
        this.backImage = backImage;
    }
}
