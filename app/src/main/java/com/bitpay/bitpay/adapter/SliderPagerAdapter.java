package com.bitpay.bitpay.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.bitpay.bitpay.R;
import com.bitpay.bitpay.constant.AppConstants;
import com.bitpay.bitpay.fragment.SliderFragment;


/**
 * Created by Codeslay-03 on 1/27/2017.
 */
public class SliderPagerAdapter extends FragmentPagerAdapter {

    FragmentManager fm;

    int[] images = {R.drawable.landing_1, R.drawable.landing_2, R.drawable.landing_3, R.drawable.landing_4};

    public SliderPagerAdapter(FragmentManager fm) {
        super(fm);
        this.fm = fm;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = new SliderFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(AppConstants.KEY_POSSITION, position);
        bundle.putInt(AppConstants.KEY_IMAGE, images[position]);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getCount() {
        return images.length;
    }
}
