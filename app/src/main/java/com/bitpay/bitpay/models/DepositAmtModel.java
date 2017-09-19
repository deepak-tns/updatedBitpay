package com.bitpay.bitpay.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Codeslay-03 on 6/6/2017.
 */

public class DepositAmtModel {

    private static DepositAmtModel depositAmtModel;

    private DepositAmtModel() {

    }

    public static DepositAmtModel getInstance() {
        if (depositAmtModel == null) {
            depositAmtModel = new DepositAmtModel();
        }
        return depositAmtModel;
    }

    @SerializedName("user_id")
    private int userId;
    @SerializedName("reference_no")
    private String referenceNo;
    @SerializedName("account_number")
    private String accountNumber;
    @SerializedName("amount_deposite")
    private String amountDeposite;
    @SerializedName("bit_amount")
    private String bitAmount;
    @SerializedName("image")
    private String imageName;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getReferenceNo() {
        return referenceNo;
    }

    public void setReferenceNo(String referenceNo) {
        this.referenceNo = referenceNo;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getAmountDeposite() {
        return amountDeposite;
    }

    public void setAmountDeposite(String amountDeposite) {
        this.amountDeposite = amountDeposite;
    }

    public String getBitAmount() {
        return bitAmount;
    }

    public void setBitAmount(String bitAmount) {
        this.bitAmount = bitAmount;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }
}
