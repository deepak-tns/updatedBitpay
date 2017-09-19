package com.bitpay.bitpay.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by admin on 05-12-2016.
 */

public class NotificationModel implements Parcelable {

    private String id;
    private String title;
    private String message;
    private String type;
    private long timestamp;

    protected NotificationModel(Parcel in) {
        id = in.readString();
        title = in.readString();
        message = in.readString();
        type = in.readString();
        timestamp = in.readLong();
    }

    public static final Creator<NotificationModel> CREATOR = new Creator<NotificationModel>() {
        @Override
        public NotificationModel createFromParcel(Parcel in) {
            return new NotificationModel(in);
        }

        @Override
        public NotificationModel[] newArray(int size) {
            return new NotificationModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(title);
        parcel.writeString(message);
        parcel.writeString(type);
        parcel.writeLong(timestamp);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
