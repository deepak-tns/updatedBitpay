package com.bitpay.bitpay.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Codeslay-03 on 6/1/2017.
 */

public class SellBitcoinModel {

    private static SellBitcoinModel sellBitcoinModel;


    @SerializedName("user_id")
    private int userId;
    private String amounts;


    public static SellBitcoinModel getInstance() {
        if (sellBitcoinModel == null) {
            sellBitcoinModel = new SellBitcoinModel();
        }
        return sellBitcoinModel;
    }

    private SellBitcoinModel() {

    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getAmounts() {
        return amounts;
    }

    public void setAmounts(String amounts) {
        this.amounts = amounts;
    }
}
