package com.bitpay.bitpay.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Codeslay-03 on 6/1/2017.
 */

public class BuyBitcoinModel {

    private static BuyBitcoinModel buyBitcoinModel;


    @SerializedName("user_id")
    private int userId;
    @SerializedName("amounts")
    private String bitAmounts;
    @SerializedName("amount_inr")
    private String inrAmounts;
    @SerializedName("transection_id")
    private String transectionId;

    public static BuyBitcoinModel getInstance() {
        if (buyBitcoinModel == null) {
            buyBitcoinModel = new BuyBitcoinModel();
        }
        return buyBitcoinModel;
    }

    private BuyBitcoinModel() {

    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getBitAmounts() {
        return bitAmounts;
    }

    public void setBitAmounts(String bitAmounts) {
        this.bitAmounts = bitAmounts;
    }

    public String getInrAmounts() {
        return inrAmounts;
    }

    public void setInrAmounts(String inrAmounts) {
        this.inrAmounts = inrAmounts;
    }

    public String getTransectionId() {
        return transectionId;
    }

    public void setTransectionId(String transectionId) {
        this.transectionId = transectionId;
    }
}
