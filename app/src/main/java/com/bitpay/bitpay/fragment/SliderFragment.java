package com.bitpay.bitpay.fragment;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bitpay.bitpay.R;
import com.bitpay.bitpay.constant.AppConstants;


public class SliderFragment extends Fragment {

    View view;
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        int imageId = getArguments().getInt(AppConstants.KEY_IMAGE);
        int position = getArguments().getInt(AppConstants.KEY_POSSITION);
        view = inflater.inflate(R.layout.fragment_slider, container, false);
        (view.findViewById(R.id.pagerContainer)).setBackground(getResources().getDrawable(imageId));
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }
}
