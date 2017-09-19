package com.bitpay.bitpay.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Codeslay-03 on 6/1/2017.
 */

public class ContactModel {

    private int id;
    @SerializedName("name")
    private String contactName;
    @SerializedName("bit_address")
    private String bitPayAddress;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getBitPayAddress() {
        return bitPayAddress;
    }

    public void setBitPayAddress(String bitPayAddress) {
        this.bitPayAddress = bitPayAddress;
    }
}
