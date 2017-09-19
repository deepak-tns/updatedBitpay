package com.bitpay.bitpay.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Codeslay-03 on 6/8/2017.
 */

public class UserTransactionModel implements Serializable {

    @SerializedName("timestamp")
    private String date;
    @SerializedName("transection_id")
    private String transectionId;
    @SerializedName("transection_type")
    private String transectionType;
    private String description;
    @SerializedName("amount")
    private String bitAmmount;
    @SerializedName("to_name")
    private String toName;
    @SerializedName("to_phone")
    private String toPhone;
    @SerializedName("to_bit_address")
    private String toBitAddress;
    private String name;
    private String phone;
    private String status;
    @SerializedName("network_fee")
    private String networkFee;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTransectionId() {
        return transectionId;
    }

    public void setTransectionId(String transectionId) {
        this.transectionId = transectionId;
    }

    public String getTransectionType() {
        return transectionType;
    }

    public void setTransectionType(String transectionType) {
        this.transectionType = transectionType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBitAmmount() {
        return bitAmmount;
    }

    public void setBitAmmount(String bitAmmount) {
        this.bitAmmount = bitAmmount;
    }

    public String getToName() {
        return toName;
    }

    public void setToName(String toName) {
        this.toName = toName;
    }

    public String getToPhone() {
        return toPhone;
    }

    public void setToPhone(String toPhone) {
        this.toPhone = toPhone;
    }

    public String getToBitAddress() {
        return toBitAddress;
    }

    public void setToBitAddress(String toBitAddress) {
        this.toBitAddress = toBitAddress;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNetworkFee() {
        return networkFee;
    }

    public void setNetworkFee(String networkFee) {
        this.networkFee = networkFee;
    }
}
