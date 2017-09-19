package com.bitpay.bitpay.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bitpay.bitpay.R;
import com.bitpay.bitpay.utils.FontUtils;


public class AboutUsFragment extends BaseFragment {

    private TextView companyNameTV;
    private TextView companyWebsiteTV;
    private TextView versionCodeTV;
    private TextView allRightTV;
    private ImageView fbIV;
    private ImageView twitterIV;
    private ImageView gPlusIV;

    public AboutUsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return view = inflater.inflate(R.layout.fragment_about_us, container, false);
    }


    @Override
    public void alertOkClicked(int alertType) {

    }

    @Override
    protected void initViews() {
        companyNameTV = (TextView) view.findViewById(R.id.companyNameTV);
        companyWebsiteTV = (TextView) view.findViewById(R.id.companyWebsiteTV);
        versionCodeTV = (TextView) view.findViewById(R.id.versionCodeTV);
        allRightTV = (TextView) view.findViewById(R.id.allRightTV);
        fbIV = (ImageView) view.findViewById(R.id.fbIV);
        twitterIV = (ImageView) view.findViewById(R.id.twitterIV);
        gPlusIV = (ImageView) view.findViewById(R.id.gPlusIV);
        versionCodeTV.setText(getString(R.string.text_version_code) + VERSION_CODE);

        FontUtils.changeFont(context, companyNameTV, FONT_CORBEL_REGULAR);
        FontUtils.changeFont(context, companyWebsiteTV, FONT_CORBEL_REGULAR);
        FontUtils.changeFont(context, versionCodeTV, FONT_ROBOTO_REGULAR);
        FontUtils.changeFont(context, allRightTV, FONT_CORBEL_REGULAR);
    }

    @Override
    protected void initContext() {
        currentActivity = getActivity();
        context = getActivity();
    }

    @Override
    protected void initListners() {

    }

    @Override
    public void onClick(View v) {

    }
}
