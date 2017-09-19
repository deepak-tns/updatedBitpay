package com.bitpay.bitpay.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.bitpay.bitpay.fragment.BuyBitcoinFragment;
import com.bitpay.bitpay.fragment.SellBitcoinFragment;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Abhishek on 6/3/2017.
 */

public class BuySellPagerAdapter extends FragmentStatePagerAdapter {
    private int tabCount;
    private Map<Integer, Fragment> fragmentMap;

    //Constructor to the class
    public BuySellPagerAdapter(FragmentManager fm, int tabCount) {
        super(fm);
        //Initializing tab count
        this.tabCount = tabCount;
        this.fragmentMap = new HashMap<>();
    }

    @Override
    public Fragment getItem(int position) {
        //Returning the current tabs
        switch (position) {
            case 1:
                BuyBitcoinFragment tab1 = new BuyBitcoinFragment();
                fragmentMap.put(position, tab1);
                return tab1;
            case 0:
                SellBitcoinFragment tab2 = new SellBitcoinFragment();
                fragmentMap.put(position, tab2);
                return tab2;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return tabCount;
    }

    public Fragment getCurrentFragment(int position) {
        Fragment fragment = null;
        if (fragmentMap == null) return fragment;
        if (fragmentMap.size() > position) {
            fragment = fragmentMap.get(position);
        }
        return fragment;
    }
}
