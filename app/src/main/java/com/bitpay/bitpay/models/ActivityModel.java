package com.bitpay.bitpay.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Abhishek on 6/4/2017.
 */

public class ActivityModel {
    @SerializedName("description")
    private String activityName;
    @SerializedName("timestamp")
    private String activityTime;
    private int activityImageView;


    public int getActivityImageView() {
        return activityImageView;
    }

    public void setActivityImageView(int activityImageView) {
        this.activityImageView = activityImageView;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public String getActivityTime() {
        return activityTime;
    }

    public void setActivityTime(String activityTime) {
        this.activityTime = activityTime;
    }


}
