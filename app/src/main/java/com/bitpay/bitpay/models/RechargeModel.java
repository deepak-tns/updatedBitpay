package com.bitpay.bitpay.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Codeslay-03 on 6/9/2017.
 */

public class RechargeModel {

    private static RechargeModel rechargeModel;

    private RechargeModel() {

    }

    public static RechargeModel getInstance() {
        if (rechargeModel == null) {
            rechargeModel = new RechargeModel();
        }
        return rechargeModel;
    }

    @SerializedName("user_id")
    private int userId;
    @SerializedName("provider_id")
    private int providerId;
    @SerializedName("amount_inr")
    private String inrAmount;
    @SerializedName("amount_bit")
    private String bitAmount;
    private String number;
    @SerializedName("recharge_type")
    private int rechargeType;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getProviderId() {
        return providerId;
    }

    public void setProviderId(int providerId) {
        this.providerId = providerId;
    }

    public String getInrAmount() {
        return inrAmount;
    }

    public void setInrAmount(String inrAmount) {
        this.inrAmount = inrAmount;
    }

    public String getBitAmount() {
        return bitAmount;
    }

    public void setBitAmount(String bitAmount) {
        this.bitAmount = bitAmount;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public int getRechargeType() {
        return rechargeType;
    }

    public void setRechargeType(int rechargeType) {
        this.rechargeType = rechargeType;
    }
}
