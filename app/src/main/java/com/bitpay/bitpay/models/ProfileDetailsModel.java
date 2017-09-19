package com.bitpay.bitpay.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Codeslay-03 on 2/4/2017.
 */

public class ProfileDetailsModel {

    private static ProfileDetailsModel profileDetailsModel;

    @SerializedName("user_id")
    private int userId;
    private String name;
    @SerializedName("email")
    private String emailId;
    @SerializedName("profile_image")
    private String userImageName;

    private ProfileDetailsModel() {

    }

    public static ProfileDetailsModel getInstance() {
        if (profileDetailsModel == null) {
            profileDetailsModel = new ProfileDetailsModel();
        }
        return profileDetailsModel;
    }

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

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getUserImageName() {
        return userImageName;
    }

    public void setUserImageName(String userImageName) {
        this.userImageName = userImageName;
    }
}
