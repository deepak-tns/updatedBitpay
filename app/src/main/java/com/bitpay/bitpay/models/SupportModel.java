package com.bitpay.bitpay.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Codeslay-03 on 6/7/2017.
 */

public class SupportModel {

    private static SupportModel supportModel;

    private SupportModel() {

    }

    public static SupportModel getInstance() {
        if (supportModel == null) {
            supportModel = new SupportModel();
        }
        return supportModel;
    }

    @SerializedName("user_id")
    private int userId;
    private String name;
    private String email;
    private String message;
    @SerializedName("ticket_number")
    private String ticketNumber;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTicketNumber() {
        return ticketNumber;
    }

    public void setTicketNumber(String ticketNumber) {
        this.ticketNumber = ticketNumber;
    }
}
