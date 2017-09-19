package com.bitpay.bitpay.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import com.bitpay.bitpay.R;
import com.bitpay.bitpay.adapter.BuySellPagerAdapter;
import com.bitpay.bitpay.fragment.BuyBitcoinFragment;
import com.bitpay.bitpay.fragment.SellBitcoinFragment;

public class BySellBitcoinActivity extends BaseActivity implements TabLayout.OnTabSelectedListener {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private BuySellPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_by_shell_bitcoin);
    }

    @Override
    protected void initViews() {
        settingTitle(getString(R.string.title_buysell));
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        setTabLayout();
        if (getIntent().getExtras() != null) {
            int type = getIntent().getExtras().getInt(KEY_TYPE);
            if (type == TYPE_SELL_BITCOIN) {
                viewPager.setCurrentItem(1);
            }
        }

    }

    @Override
    protected void initContext() {
        context = BySellBitcoinActivity.this;
        currentActivity = BySellBitcoinActivity.this;
    }

    @Override
    protected void initListners() {

    }

    @Override
    protected boolean isActionBar() {
        return true;
    }

    @Override
    protected boolean isHomeButton() {
        return true;
    }


    @Override
    public void onClick(View v) {

    }

    @Override
    public void onAlertClicked(int alertType) {

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        viewPager.setCurrentItem(tab.getPosition());
        int selectedTabPosition = tab.getPosition();

        View firstTab = ((ViewGroup) tabLayout.getChildAt(0)).getChildAt(0);
        View secondTab = ((ViewGroup) tabLayout.getChildAt(0)).getChildAt(1);

        if (selectedTabPosition == 0) { // that means first tab
            firstTab.setBackground(ContextCompat.getDrawable(context, R.drawable.first_tab_selected));
            secondTab.setBackground(ContextCompat.getDrawable(context, R.drawable.second_tab_unselected));
        } else if (selectedTabPosition == 1) { // that means it's a last tab
            firstTab.setBackground(ContextCompat.getDrawable(context, R.drawable.first_tab_unselected));
            secondTab.setBackground(ContextCompat.getDrawable(context, R.drawable.second_tab_selceted));
        }
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    private void setTabLayout() {
        //Adding the tabs using addTab() method
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.tab_buy)));
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.tab_sell)));
        //Creating our pager adapter
      //  adapter = new BuySellPagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        adapter = new BuySellPagerAdapter(getSupportFragmentManager(), 1);
        //Adding adapter to pager
        viewPager.setAdapter(adapter);
        //Adding onTabSelectedListener to swipe views
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(this);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        switch (requestCode) {
            case REQUEST_TAG_CONFIRM_PIN: {
                if (resultCode == RESULT_OK) {
                    if (intent != null) {
                        int type = intent.getIntExtra(KEY_TYPE, 0);
                        if (type == TYPE_BUY_BITCOIN) {
                            BuyBitcoinFragment buyBitcoinFragment = (BuyBitcoinFragment) adapter.getCurrentFragment(1);
                            if (buyBitcoinFragment != null) {
                                buyBitcoinFragment.onActivityResult(requestCode, resultCode, intent);
                            }
                        } else if (type == TYPE_SELL_BITCOIN) {
                            SellBitcoinFragment sellBitcoinFragment = (SellBitcoinFragment) adapter.getCurrentFragment(0);
                            if (sellBitcoinFragment != null) {
                                sellBitcoinFragment.onActivityResult(requestCode, resultCode, intent);
                            }
                        }
                    }
                }
                break;
            }
        }
    }

}
