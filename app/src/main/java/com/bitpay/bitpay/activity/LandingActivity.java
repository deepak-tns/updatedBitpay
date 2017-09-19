package com.bitpay.bitpay.activity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.bitpay.bitpay.R;
import com.bitpay.bitpay.adapter.SliderPagerAdapter;
import com.bitpay.bitpay.utils.FontUtils;
import com.viewpagerindicator.CirclePageIndicator;


public class LandingActivity extends BaseActivity {

    ViewPager viewPager;
    CirclePageIndicator circleIndicator;
    SliderPagerAdapter viewPagerAdpater;
    TextView textContinue;
    CountDownTimer timer;
    boolean ifNotStarted = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);
    }


    @Override
    protected void initViews() {
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        circleIndicator = (CirclePageIndicator) findViewById(R.id.circleIndicator);
        textContinue = (TextView) findViewById(R.id.textContinue);
        FontUtils.changeFont(context, textContinue, FONT_CORBEL_REGULAR);
        viewPagerAdpater = new SliderPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(viewPagerAdpater);
        viewPager.setCurrentItem(0);
        circleIndicator.setFillColor(getResources().getColor(R.color.colorWhite));
        circleIndicator.setRadius(10);
        circleIndicator.setStrokeWidth(0);
        circleIndicator.setViewPager(viewPager);
        settingAutoChanger();
    }

    @Override
    protected void initContext() {
        currentActivity = LandingActivity.this;
        context = LandingActivity.this;
    }

    @Override
    protected void initListners() {
        textContinue.setOnClickListener(this);
    }

    @Override
    protected boolean isActionBar() {
        return false;
    }

    @Override
    protected boolean isHomeButton() {
        return false;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.textContinue: {
                startActivity(currentActivity, VerificationActivity.class, bundle, false, REQUEST_TAG_NO_RESULT, true, ANIMATION_SLIDE_UP);
                finish();
                break;
            }
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (timer != null) {
            timer.cancel();
        }
        ifNotStarted = true;
    }


    public void settingAutoChanger() {
        final Handler mUpdateTimeHandler = new Handler();
        Runnable mUpdateTimeTask = new Runnable() {
            public void run() {
                // do your updates here
                mUpdateTimeHandler.postDelayed(this, 5000);
                toChangeViewPager();
            }
        };
        mUpdateTimeHandler.post(mUpdateTimeTask);
    }

    private void toChangeViewPager() {
        if (ifNotStarted) {
            ifNotStarted = false;
            return;
        }
        int pos = viewPager.getCurrentItem();
        if (pos == 3) {
            viewPager.setCurrentItem(0);
        } else {
            viewPager.setCurrentItem(pos + 1);
        }
    }

    @Override
    public void onAlertClicked(int alertType) {

    }
}
