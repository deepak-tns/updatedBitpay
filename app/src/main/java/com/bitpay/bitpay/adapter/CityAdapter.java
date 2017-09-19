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

import java.util.ArrayList;

/**
 * Created by Abhishek on 6/8/2017.
 */

public class CityAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<CityModel> arrayList;
    private LayoutInflater inflater = null;

    public CityAdapter(Context context, ArrayList<CityModel> arrayList) {
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

    public int getPossition(String cityName) {
        int possition = 0;
        if (cityName == null) return possition;
        for (int i = 0; i < arrayList.size(); i++) {
            CityModel cityModel = arrayList.get(i);
            if (cityModel == null) continue;
            String cName = cityModel.getCityName();
            if (cName.equalsIgnoreCase(cityName)) {
                possition = i;
                break;
            }
        }
        return possition;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CityModel cityModel = null;
        inflater = ((Activity) context).getLayoutInflater();
        final CityAdapter.MyViewHolder mViewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.row_item_state, parent, false);
            mViewHolder = new CityAdapter.MyViewHolder();
            mViewHolder.cityTextView = (TextView) convertView.findViewById(R.id.stateTextView);

            mViewHolder.cityTextView.setTextColor(context.getResources().getColor(R.color.colorBlack));

            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (CityAdapter.MyViewHolder) convertView.getTag();
        }

        if (arrayList != null && arrayList.size() > position) {
            cityModel = arrayList.get(position);
        }
        if (cityModel != null) {
            mViewHolder.cityTextView.setText(cityModel.getCityName());
        }

        return convertView;
    }

    private class MyViewHolder {

        TextView cityTextView;

    }
}
