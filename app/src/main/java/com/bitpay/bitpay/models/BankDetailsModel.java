package com.bitpay.bitpay.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Codeslay-03 on 6/6/2017.
 */

public class BankDetailsModel {

    private static BankDetailsModel bankDetailsModel;

    private BankDetailsModel() {

    }

    public static BankDetailsModel getInstance() {
        if (bankDetailsModel == null) {
            bankDetailsModel = new BankDetailsModel();
        }
        return bankDetailsModel;
    }

    private int id;
    @SerializedName("user_id")
    private int userId;
    @SerializedName("ifsc_code")
    private String ifscCode;
    @SerializedName("bank_name")
    private String bankName;
    @SerializedName("branch_name")
    private String branchName;
    @SerializedName("account_holder_name")
    private String accountHolderName;
    @SerializedName("account_number")
    private String accountNumber;
    private String status;
    @SerializedName("image")
    private String imageName;

    public String getIfscCode() {
        return ifscCode;
    }

    public void setIfscCode(String ifscCode) {
        this.ifscCode = ifscCode;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public String getAccountHolderName() {
        return accountHolderName;
    }

    public void setAccountHolderName(String accountHolderName) {
        this.accountHolderName = accountHolderName;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }
}
