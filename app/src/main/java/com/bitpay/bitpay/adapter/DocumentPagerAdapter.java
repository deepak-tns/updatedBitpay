package com.bitpay.bitpay.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.bitpay.bitpay.fragment.ActivityFragment;
import com.bitpay.bitpay.fragment.BankDetailsFragment;
import com.bitpay.bitpay.fragment.DocumentFragment;
import com.bitpay.bitpay.fragment.KycDetailsFragment;
import com.bitpay.bitpay.fragment.PancardDetailsFragment;
import com.bitpay.bitpay.fragment.YourActivitesFragment;

/**
 * Created by Abhishek on 6/3/2017.
 */

public class DocumentPagerAdapter extends FragmentStatePagerAdapter {
    int tabCount;

    //Constructor to the class
    public DocumentPagerAdapter(FragmentManager fm, int tabCount) {
        super(fm);
        //Initializing tab count
        this.tabCount = tabCount;
    }

    @Override
    public Fragment getItem(int position) {
        //Returning the current tabs
        switch (position) {
            case 0:
                PancardDetailsFragment tab1 = new PancardDetailsFragment();
                return tab1;
            case 1:
                BankDetailsFragment tab2 = new BankDetailsFragment();
                return tab2;
            case 2:
                KycDetailsFragment tab3 = new KycDetailsFragment();
                return tab3;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return tabCount;
    }
}
