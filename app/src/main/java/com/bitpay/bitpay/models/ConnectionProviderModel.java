package com.bitpay.bitpay.models;

import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by Abhishek on 6/8/2017.
 */

public class ConnectionProviderModel implements Serializable{
    private int prociderId;
    private String connectionName;

    public int getProciderId() {
        return prociderId;
    }

    public void setProciderId(int prociderId) {
        this.prociderId = prociderId;
    }

    public String getConnectionName() {
        return connectionName;
    }

    public void setConnectionName(String connectionName) {
        this.connectionName = connectionName;
    }

}
