package com.bitpay.bitpay.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Codeslay-03 on 6/1/2017.
 */

public class AddContactModel {

    private static AddContactModel addContactModel;

    @SerializedName("name")
    private String contactName;
    @SerializedName("bit_address")
    private String bitAddress;
    @SerializedName("user_id")
    private int userId;

    public static AddContactModel getInstance() {
        if (addContactModel == null) {
            addContactModel = new AddContactModel();
        }
        return addContactModel;
    }

    private AddContactModel() {

    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getBitAddress() {
        return bitAddress;
    }

    public void setBitAddress(String bitAddress) {
        this.bitAddress = bitAddress;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
