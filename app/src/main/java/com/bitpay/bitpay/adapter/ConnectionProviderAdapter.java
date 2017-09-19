package com.bitpay.bitpay.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bitpay.bitpay.R;
import com.bitpay.bitpay.models.ConnectionProviderModel;

import java.util.ArrayList;

/**
 * Created by Abhishek on 6/8/2017.
 */

public class ConnectionProviderAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<ConnectionProviderModel> arrayList;
    private LayoutInflater inflater = null;

    public ConnectionProviderAdapter(Context context, ArrayList<ConnectionProviderModel> arrayList) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @Override
    public int getCount() {
        int size = 0;
        if (arrayList != null && arrayList.size() > 0) {
            size = arrayList.size();
        }
        return size;
    }

    @Override
    public Object getItem(int position) {
        if (arrayList != null)
            return arrayList.get(position);
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ConnectionProviderModel connectionProviderModel = null;
        inflater = ((Activity) context).getLayoutInflater();
        final ConnectionProviderAdapter.MyViewHolder mViewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.connection_provider_row_item, parent, false);
            mViewHolder = new ConnectionProviderAdapter.MyViewHolder();
            mViewHolder.activityTextView = (TextView) convertView.findViewById(R.id.connectionProviderTextView);

            mViewHolder.activityTextView.setTextColor(context.getResources().getColor(R.color.colorBlack));
            mViewHolder.activityTextView.setEnabled(true);

            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (ConnectionProviderAdapter.MyViewHolder) convertView.getTag();
        }

        if (arrayList != null && arrayList.size() > position) {
            connectionProviderModel = (ConnectionProviderModel) arrayList.get(position);
        }
        if (connectionProviderModel != null) {
            mViewHolder.activityTextView.setText(connectionProviderModel.getConnectionName());

        }

        return convertView;
    }

    private class MyViewHolder {

        TextView activityTextView;

    }
}
