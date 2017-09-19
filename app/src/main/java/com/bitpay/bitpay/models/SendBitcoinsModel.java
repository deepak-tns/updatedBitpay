package com.bitpay.bitpay.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Codeslay-03 on 6/3/2017.
 */

public class SendBitcoinsModel {

    private static SendBitcoinsModel sendBitcoinsModel;

    private SendBitcoinsModel() {

    }

    public static SendBitcoinsModel getInstance() {
        if (sendBitcoinsModel == null) {
            sendBitcoinsModel = new SendBitcoinsModel();
        }
        return sendBitcoinsModel;
    }

    @SerializedName("user_id")
    private int userId;
    @SerializedName("contact_id")
    private int contactId;
    @SerializedName("amounts")
    private String bitAmmount;
    private String inrAmmount;
    @SerializedName("bit_address")
    private String bitAddress;
    private String name;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getContactId() {
        return contactId;
    }

    public void setContactId(int contactId) {
        this.contactId = contactId;
    }

    public String getBitAmmount() {
        return bitAmmount;
    }

    public void setBitAmmount(String bitAmmount) {
        this.bitAmmount = bitAmmount;
    }

    public String getInrAmmount() {
        return inrAmmount;
    }

    public void setInrAmmount(String inrAmmount) {
        this.inrAmmount = inrAmmount;
    }

    public String getBitAddress() {
        return bitAddress;
    }

    public void setBitAddress(String bitAddress) {
        this.bitAddress = bitAddress;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
