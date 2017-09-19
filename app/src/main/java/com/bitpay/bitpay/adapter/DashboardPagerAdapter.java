package com.bitpay.bitpay.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.bitpay.bitpay.fragment.ActivityFragment;
import com.bitpay.bitpay.fragment.YourActivitesFragment;

/**
 * Created by Abhishek on 6/3/2017.
 */

public class DashboardPagerAdapter extends FragmentStatePagerAdapter {
    int tabCount;

    //Constructor to the class
    public DashboardPagerAdapter(FragmentManager fm, int tabCount) {
        super(fm);
        //Initializing tab count
        this.tabCount= tabCount;
    }
    @Override
    public Fragment getItem(int position) {
        //Returning the current tabs
        switch (position) {
            case 0:
                ActivityFragment tab1 = new ActivityFragment();
                return tab1;
            case 1:
                YourActivitesFragment tab2 = new YourActivitesFragment();
                return tab2;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return tabCount;
    }
}
