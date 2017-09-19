package com.bitpay.bitpay.fragment;

import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.TypefaceSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bitpay.bitpay.R;
import com.bitpay.bitpay.activity.BaseActivity;
import com.bitpay.bitpay.activity.BuyActivity;
import com.bitpay.bitpay.activity.BySellBitcoinActivity;
import com.bitpay.bitpay.adapter.DashboardPagerAdapter;
import com.bitpay.bitpay.application.BitPayApplication;
import com.bitpay.bitpay.utils.FontUtils;
import com.bitpay.bitpay.utils.SharedPreferenceUtils;
import com.bitpay.bitpay.widgets.CustomTypefaceSpan;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Map;


public class HomeFragment extends BaseFragment implements TabLayout.OnTabSelectedListener {
    private TabLayout tabLayout;
    private View view;
    DashboardPagerAdapter adapter;
    private LinearLayout bitcoinRateProgressBar;
    private LinearLayout currentBalanceProgressBar;
    private TextView lblCurrentBalanceTV;
    private TextView currentBalanceTV;
    private TextView currentInrBalanceTV;
    private TextView lblBuyingRateTV;
    private TextView amtBuyingRateTV;
    private TextView lblSellingRateTV;
    private TextView amtSellingRateTV;
    private Button sellButton;
    private Button buyButton;
    private ViewPager viewPager;

    private String buyingRate;
    private String sellingRate;
    private String currentBalance;
    private NumberFormat format;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);
        return view;
    }

    private void setTabLayout() {
        //Adding the tabs using addTab() method
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.tab_activity)));
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.tab_your_activity)));
        //Creating our pager adapter
        adapter = new DashboardPagerAdapter(getChildFragmentManager(), tabLayout.getTabCount());
        //Adding adapter to pager
        viewPager.setAdapter(adapter);
        //Adding onTabSelectedListener to swipe views
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(this);
    }


    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void alertOkClicked(int alertType) {

    }

    @Override
    protected void initViews() {
        tabLayout = (TabLayout) view.findViewById(R.id.tabLayout);
        viewPager = (ViewPager) view.findViewById(R.id.viewPager);
        bitcoinRateProgressBar = (LinearLayout) view.findViewById(R.id.bitcoinRateProgressBar);
        currentBalanceProgressBar = (LinearLayout) view.findViewById(R.id.currentBalanceProgressBar);
        lblCurrentBalanceTV = (TextView) view.findViewById(R.id.lblCurrentBalanceTV);
        currentBalanceTV = (TextView) view.findViewById(R.id.currentBalanceTV);
        currentInrBalanceTV = (TextView) view.findViewById(R.id.currentInrBalanceTV);
        lblBuyingRateTV = (TextView) view.findViewById(R.id.lblBuyingRateTV);
        amtBuyingRateTV = (TextView) view.findViewById(R.id.amtBuyingRateTV);
        lblSellingRateTV = (TextView) view.findViewById(R.id.lblSellingRateTV);
        amtSellingRateTV = (TextView) view.findViewById(R.id.amtSellingRateTV);
        sellButton = (Button) view.findViewById(R.id.sellButton);
        buyButton = (Button) view.findViewById(R.id.buyButton);

        FontUtils.changeFont(context, lblCurrentBalanceTV, FONT_CORBEL_REGULAR);
        FontUtils.changeFont(context, currentBalanceTV, FONT_ROBOTO_REGULAR);
        FontUtils.changeFont(context, lblBuyingRateTV, FONT_CORBEL_REGULAR);
        FontUtils.changeFont(context, amtBuyingRateTV, FONT_ROBOTO_REGULAR);
        FontUtils.changeFont(context, lblSellingRateTV, FONT_CORBEL_REGULAR);
        FontUtils.changeFont(context, amtSellingRateTV, FONT_ROBOTO_REGULAR);
        FontUtils.changeFont(context, sellButton, FONT_CORBEL_REGULAR);
        FontUtils.changeFont(context, buyButton, FONT_CORBEL_REGULAR);
        FontUtils.changeFont(context, currentInrBalanceTV, FONT_ROBOTO_REGULAR);

        Typeface fontRobotoReg = Typeface.createFromAsset(context.getAssets(), FONT_ROBOTO_REGULAR);
        Typeface fontBitReg = Typeface.createFromAsset(context.getAssets(), FONT_BITCOIN_REGULAR);
        SpannableStringBuilder sb = new SpannableStringBuilder(lblBuyingRateTV.getText());
        TypefaceSpan robotoRegularSpan = new CustomTypefaceSpan("", fontRobotoReg);
        TypefaceSpan bitRegularSpan = new CustomTypefaceSpan("", fontBitReg);
        sb.setSpan(robotoRegularSpan, 0, 1, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        sb.setSpan(bitRegularSpan, 1, 2, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        lblBuyingRateTV.setText(sb);

        sb = new SpannableStringBuilder(lblSellingRateTV.getText());
        sb.setSpan(robotoRegularSpan, 0, 1, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        sb.setSpan(bitRegularSpan, 1, 2, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        lblSellingRateTV.setText(sb);

        format = NumberFormat.getCurrencyInstance();
        setTabLayout();
        getCurrentBalance();
        getBitcoinRate();
        getDocumentStatus();
    }

    @Override
    protected void initContext() {
        context = getActivity();
        currentActivity = getActivity();
    }

    @Override
    protected void initListners() {
        buyButton.setOnClickListener(this);
        sellButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buyButton: {
                ((BaseActivity) currentActivity).startActivity(currentActivity, BuyActivity.class, bundle, false, REQUEST_TAG_NO_RESULT, true, ANIMATION_SLIDE_UP);

                // toOpenBuySellActivity(TYPE_BUY_BITCOIN);
                break;
            }
            case R.id.sellButton: {
                toOpenBuySellActivity(TYPE_SELL_BITCOIN);
                break;
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        viewPager.setCurrentItem(tab.getPosition());
        int selectedTabPosition = tab.getPosition();


        View firstTab = ((ViewGroup) tabLayout.getChildAt(0)).getChildAt(0);
        View secondTab = ((ViewGroup) tabLayout.getChildAt(0)).getChildAt(1);


        if (selectedTabPosition == 0) { // that means first tab

            firstTab.setBackground(getResources().getDrawable(R.drawable.first_tab_selected));
            secondTab.setBackground(getResources().getDrawable(R.drawable.second_tab_unselected));


        } else if (selectedTabPosition == 1) { // that means it's a last tab

            firstTab.setBackground(getResources().getDrawable(R.drawable.first_tab_unselected));
            secondTab.setBackground(getResources().getDrawable(R.drawable.second_tab_selceted));


        }


    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    private void getCurrentBalance() {
        currentBalanceProgressBar.setVisibility(View.VISIBLE);
        int userId = SharedPreferenceUtils.getInstance(context).getInteger(USER_ID);

        JSONObject jsonGetCurrentBalance = null;
        String URL_GET_CURRENT_BALANCE = GET_CURRENT_BALANCE_URL;
        try {
            jsonGetCurrentBalance = new JSONObject();
            jsonGetCurrentBalance.put(KEY_USER_ID, userId + "");
            Log.e("jsonGetContact", jsonGetCurrentBalance.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest getGetCurentBalanceRequest = new JsonObjectRequest(Request.Method.POST, URL_GET_CURRENT_BALANCE, jsonGetCurrentBalance, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    currentBalanceProgressBar.setVisibility(View.GONE);
                    ((BaseActivity) currentActivity).logTesting(currentActivity.getResources().getString(R.string.nwk_response_get_balance), response.toString(), Log.ERROR);
                    String message = response.getString(RESPONCE_MESSAGE);
                    if (!response.getBoolean(RESPONCE_ERROR)) {
                        JSONObject messageJson = new JSONObject(message);
                        currentBalance = messageJson.getString(KEY_CURRENT_BALANCE);
                        SharedPreferenceUtils.getInstance(context).putString(KEY_MIN_AMOUNT, messageJson.getString(KEY_MIN_AMOUNT));
                        SharedPreferenceUtils.getInstance(context).putString(KEY_MAX_AMOUNT, messageJson.getString(KEY_MAX_AMOUNT));
                        if (TextUtils.isEmpty(currentBalance)) return;
                        double balance = Double.parseDouble(currentBalance);
                        currentBalanceTV.setText(new DecimalFormat("##.######").format(balance) + " " + getString(R.string.text_bitcoin));
                        if (!TextUtils.isEmpty(sellingRate)) {
                            double sellRate = Double.parseDouble(sellingRate);
                            double inrValue = balance * sellRate;
                            currentInrBalanceTV.setText(new DecimalFormat("##.######").format(inrValue) + " " + getString(R.string.text_rs_currency));
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                currentBalanceProgressBar.setVisibility(View.GONE);
                //((BaseActivity) currentActivity).toast(currentActivity.getResources().getString(R.string.nwk_error_get_balance), true);
                ((BaseActivity) currentActivity).logTesting(currentActivity.getResources().getString(R.string.nwk_error_get_balance), error.toString(), Log.ERROR);

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return super.getHeaders();
            }
        };

        BitPayApplication.getInstance().addToRequestQueue(getGetCurentBalanceRequest, "getGetCurentBalanceRequest0");
    }

    private void getBitcoinRate() {
        bitcoinRateProgressBar.setVisibility(View.VISIBLE);
        int userId = SharedPreferenceUtils.getInstance(context).getInteger(USER_ID);

        JSONObject jsonGetBitCoinRate = null;
        String URL_GET_BITCOIN_RATE = GET_BITCOIN_RATE_URL;
        try {
            jsonGetBitCoinRate = new JSONObject();
            jsonGetBitCoinRate.put(KEY_USER_ID, userId);
            Log.e("jsonGetContact", jsonGetBitCoinRate.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest getGetBitcoinRateRequest = new JsonObjectRequest(Request.Method.POST, URL_GET_BITCOIN_RATE, jsonGetBitCoinRate, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    bitcoinRateProgressBar.setVisibility(View.GONE);
                    ((BaseActivity) currentActivity).logTesting(currentActivity.getResources().getString(R.string.nwk_response_get_rate), response.toString(), Log.ERROR);
                    String message = response.getString(RESPONCE_MESSAGE);
                    if (!response.getBoolean(RESPONCE_ERROR)) {
                        JSONObject messageJson = new JSONObject(message);
                        buyingRate = messageJson.getString(KEY_BUYING_RATE);
                        sellingRate = messageJson.getString(KEY_SELLING_RATE);
                        if (TextUtils.isEmpty(buyingRate) || TextUtils.isEmpty(sellingRate)) {
                            return;
                        }
                        double buyRate = Double.parseDouble(buyingRate);
                        double sellRate = Double.parseDouble(sellingRate);
                        amtBuyingRateTV.setText(format.format(buyRate));
                        amtSellingRateTV.setText(format.format(sellRate));
                        if (!TextUtils.isEmpty(currentBalance)) {
                            double bitAmt = Double.parseDouble(currentBalance);
                            double inrValue = bitAmt * sellRate;
                            currentInrBalanceTV.setText(new DecimalFormat("##.######").format(inrValue) + " " + getString(R.string.text_rs_currency));
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                bitcoinRateProgressBar.setVisibility(View.GONE);
                //((BaseActivity) currentActivity).toast(currentActivity.getResources().getString(R.string.nwk_error_get_rate), true);
                ((BaseActivity) currentActivity).logTesting(currentActivity.getResources().getString(R.string.nwk_error_get_rate), error.toString(), Log.ERROR);

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return super.getHeaders();
            }
        };

        BitPayApplication.getInstance().addToRequestQueue(getGetBitcoinRateRequest, "getGetBitcoinRateRequest0");
    }

    @Override
    public void onStop() {
        super.onStop();
        BitPayApplication.getInstance().cancelPendingRequest("getGetBitcoinRateRequest0");
        BitPayApplication.getInstance().cancelPendingRequest("getGetCurentBalanceRequest0");
    }

    private void toOpenBuySellActivity(int type) {
        if (bundle == null) {
            bundle = new Bundle();
        }
        bundle.putInt(KEY_TYPE, type);
        ((BaseActivity) currentActivity).startActivity(currentActivity, BySellBitcoinActivity.class, bundle, false, REQUEST_TAG_NO_RESULT, true, ANIMATION_SLIDE_UP);
    }

    private void getDocumentStatus() {

        int userId = SharedPreferenceUtils.getInstance(context).getInteger(USER_ID);

        JSONObject jsonGetPancardDetails = null;
        String URL_GET_PAN_DETAIL = GET_DOC_DETAIL_URL;
        try {
            jsonGetPancardDetails = new JSONObject();
            jsonGetPancardDetails.put(KEY_USER_ID, userId);
            Log.e("jsonGetPancardDetails", jsonGetPancardDetails.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest getGetPancardDetailRequest = new JsonObjectRequest(Request.Method.POST, URL_GET_PAN_DETAIL, jsonGetPancardDetails, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String message = response.getString(RESPONCE_MESSAGE);
                    if (!response.getBoolean(RESPONCE_ERROR)) {
                        JSONObject jsonObject = new JSONObject(message);
                        String panStatus = jsonObject.getString(STATUS_PAN);
                        String bankStatus = jsonObject.getString(STATUS_BANK);
                        String kycStatus = jsonObject.getString(STATUS_KYC);
                        int emailStatus = jsonObject.getInt(STATUS_EMAIL);
                        SharedPreferenceUtils.getInstance(context).putString(KEY_PAN_STATUS, panStatus);
                        SharedPreferenceUtils.getInstance(context).putString(KEY_BANK_STATUS, bankStatus);
                        SharedPreferenceUtils.getInstance(context).putString(KEY_KYC_STATUS, kycStatus);
                        SharedPreferenceUtils.getInstance(context).putInteger(KEY_EMAIL_STATUS, emailStatus);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return super.getHeaders();
            }
        };

        BitPayApplication.getInstance().addToRequestQueue(getGetPancardDetailRequest);
    }

}
