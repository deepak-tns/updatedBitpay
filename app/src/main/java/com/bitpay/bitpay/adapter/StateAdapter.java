package com.bitpay.bitpay.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bitpay.bitpay.R;
import com.bitpay.bitpay.models.CityModel;
import com.bitpay.bitpay.models.StateModel;
import com.bitpay.bitpay.models.StateModel;

import java.util.ArrayList;

/**
 * Created by Abhishek on 6/8/2017.
 */

public class StateAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<StateModel> arrayList;
    private LayoutInflater inflater = null;

    public StateAdapter(Context context, ArrayList<StateModel> arrayList) {
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
        StateModel stateModel = null;
        inflater = ((Activity) context).getLayoutInflater();
        final StateAdapter.MyViewHolder mViewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.row_item_state, parent, false);
            mViewHolder = new StateAdapter.MyViewHolder();
            mViewHolder.stateTextView = (TextView) convertView.findViewById(R.id.stateTextView);

            mViewHolder.stateTextView.setTextColor(context.getResources().getColor(R.color.colorBlack));
            mViewHolder.stateTextView.setEnabled(true);

            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (StateAdapter.MyViewHolder) convertView.getTag();
        }

        if (arrayList != null && arrayList.size() > position) {
            stateModel = arrayList.get(position);
        }
        if (stateModel != null) {
            mViewHolder.stateTextView.setText(stateModel.getStateName());

        }

        return convertView;
    }

    private class MyViewHolder {

        TextView stateTextView;

    }

    public int getPossition(String stateName) {
        int possition = 0;
        if (stateName == null) return possition;
        for (int i = 0; i < arrayList.size(); i++) {
            StateModel stateModel = arrayList.get(i);
            if (stateModel == null) continue;
            String sName = stateModel.getStateName();
            if (sName.equalsIgnoreCase(stateName)) {
                possition = i;
                break;
            }
        }
        return possition;
    }

}
