package com.bitpay.bitpay.fragment;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.bitpay.bitpay.R;
import com.bitpay.bitpay.adapter.DocumentPagerAdapter;

public class DocumentFragment extends BaseFragment implements TabLayout.OnTabSelectedListener {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private DocumentPagerAdapter adapter;

    public DocumentFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return view = inflater.inflate(R.layout.fragment_document, container, false);
    }

    @Override
    public void alertOkClicked(int alertType) {

    }

    @Override
    protected void initViews() {
        tabLayout = (TabLayout) view.findViewById(R.id.tabLayout);
        viewPager = (ViewPager) view.findViewById(R.id.viewPager);
        setTabLayout();
    }

    @Override
    protected void initContext() {
        context = getActivity();
        currentActivity = getActivity();
    }

    @Override
    protected void initListners() {

    }


    @Override
    public void onClick(View v) {

    }

    private void setTabLayout() {
        //Adding the tabs using addTab() method
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.tab_pancard)));
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.tab_account)));
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.tab_kyc_details)));


        LinearLayout linearLayout = (LinearLayout) tabLayout.getChildAt(0);
        linearLayout.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
        GradientDrawable drawable = new GradientDrawable();
        drawable.setColor(ContextCompat.getColor(context, R.color.colorPrimary));
        drawable.setSize(1, 0);
        //linearLayout.setDividerPadding(10);
        linearLayout.setDividerDrawable(drawable);

        //Creating our pager adapter
        adapter = new DocumentPagerAdapter(getChildFragmentManager(), tabLayout.getTabCount());
        //Adding adapter to pager
        viewPager.setAdapter(adapter);
        //Adding onTabSelectedListener to swipe views
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        viewPager.setCurrentItem(tab.getPosition());
        int selectedTabPosition = tab.getPosition();

        View firstTab = ((ViewGroup) tabLayout.getChildAt(0)).getChildAt(0);
        View secondTab = ((ViewGroup) tabLayout.getChildAt(0)).getChildAt(1);
        View thirdTab = ((ViewGroup) tabLayout.getChildAt(0)).getChildAt(2);

        if (selectedTabPosition == 0) { // that means first tab
            firstTab.setBackground(getResources().getDrawable(R.drawable.first_tab_selected));
            secondTab.setBackground(getResources().getDrawable(R.drawable.second_tab_unselected));
            thirdTab.setBackground(getResources().getDrawable(R.drawable.third_tab_unselected));
        } else if (selectedTabPosition == 1) { // that means it's a last tab
            firstTab.setBackground(getResources().getDrawable(R.drawable.first_tab_unselected));
            secondTab.setBackground(getResources().getDrawable(R.drawable.mid_tab_selceted));
            thirdTab.setBackground(getResources().getDrawable(R.drawable.third_tab_unselected));
        } else if (selectedTabPosition == 2) { // that means it's a last tab
            firstTab.setBackground(getResources().getDrawable(R.drawable.first_tab_unselected));
            secondTab.setBackground(getResources().getDrawable(R.drawable.second_tab_unselected));
            thirdTab.setBackground(getResources().getDrawable(R.drawable.third_tab_selceted));
        }
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }
}
