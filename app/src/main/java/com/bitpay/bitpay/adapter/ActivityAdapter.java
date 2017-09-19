package com.bitpay.bitpay.adapter;

import android.app.Activity;
import android.content.Context;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bitpay.bitpay.R;
import com.bitpay.bitpay.constant.AppConstants;
import com.bitpay.bitpay.models.ActivityModel;
import com.bitpay.bitpay.utils.DateTimeUtils;
import com.bitpay.bitpay.utils.FontUtils;

import java.util.ArrayList;

/**
 * Created by Abhishek on 6/4/2017.
 */

public class ActivityAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<ActivityModel> arrayList;
    private LayoutInflater inflater = null;

    public ActivityAdapter(Context context, ArrayList<ActivityModel> arrayList) {
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
        ActivityModel activityModel = null;
        inflater = ((Activity) context).getLayoutInflater();
        final ActivityAdapter.MyViewHolder mViewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.activity_listview_row_item, parent, false);
            mViewHolder = new ActivityAdapter.MyViewHolder();
            mViewHolder.activityTextView = (TextView) convertView.findViewById(R.id.activityTextView);
            mViewHolder.activityImageView = (ImageView) convertView.findViewById(R.id.activityImageView);
            mViewHolder.timeTextView = (TextView) convertView.findViewById(R.id.timeTextVIew);
            FontUtils.changeFont(context, mViewHolder.activityTextView, AppConstants.FONT_CORBEL_REGULAR);
            FontUtils.changeFont(context, mViewHolder.timeTextView, AppConstants.FONT_ROBOTO_REGULAR);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (ActivityAdapter.MyViewHolder) convertView.getTag();
        }

        if (arrayList != null && arrayList.size() > position) {
            activityModel = (ActivityModel) arrayList.get(position);
        }
        if (activityModel != null) {
            mViewHolder.activityTextView.setText(activityModel.getActivityName());
            mViewHolder.timeTextView.setText(DateTimeUtils.getDateByTimestamp(activityModel.getActivityTime()) + " " + DateTimeUtils.getTimeByTimestamp(activityModel.getActivityTime()));
        }
        return convertView;

    }

    private class MyViewHolder {

        TextView activityTextView;
        ImageView activityImageView;
        TextView timeTextView;
    }
}
