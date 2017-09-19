package com.bitpay.bitpay.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bitpay.bitpay.R;
import com.bitpay.bitpay.adapter.SideMenuListAdapter;
import com.bitpay.bitpay.application.BitPayApplication;
import com.bitpay.bitpay.fragment.AboutUsFragment;
import com.bitpay.bitpay.fragment.BitAddressFragment;
import com.bitpay.bitpay.fragment.ContactBookFragment;
import com.bitpay.bitpay.fragment.DocumentFragment;
import com.bitpay.bitpay.fragment.HomeFragment;
import com.bitpay.bitpay.fragment.SupportFragment;
import com.bitpay.bitpay.fragment.TransactionFragment;
import com.bitpay.bitpay.models.PancardModel;
import com.bitpay.bitpay.utils.FontUtils;
import com.bitpay.bitpay.utils.SharedPreferenceUtils;
import com.bitpay.bitpay.widgets.CircularImageView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;


public class DashboardActivity extends BaseActivity {
    private DrawerLayout drawerLayout;
    private RecyclerView recyclerNavigationDrawer;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private TextView textUserName;
    private TextView textUserMobile;
    private ImageView editUserProfileIV;
    private boolean isExitable;

    public Fragment frag;
    private SideMenuListAdapter sideMenuListAdapter;
    private LinearLayoutManager managerSideMenu;
    private CircularImageView circleImageDrawerProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
    }

    @Override
    protected void initViews() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        recyclerNavigationDrawer = (RecyclerView) findViewById(R.id.recyclerNavigationDrawer);
        textUserName = (TextView) findViewById(R.id.textUserName);
        textUserMobile = (TextView) findViewById(R.id.textUserMobile);
        editUserProfileIV = (ImageView) findViewById(R.id.editUserProfileIV);
        circleImageDrawerProfile = (CircularImageView) findViewById(R.id.circleImageDrawerProfile);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.app_name, R.string.app_name) {

            @Override
            public boolean onOptionsItemSelected(MenuItem item) {
                drawerLayout.openDrawer(Gravity.LEFT);

                return true;
            }

            @Override
            public void onDrawerOpened(View drawerView) {

                super.onDrawerOpened(drawerView);
                toHideKeyboard();

            }

            @Override
            public void onDrawerStateChanged(int newState) {

                super.onDrawerStateChanged(newState);
                if (newState == DrawerLayout.STATE_DRAGGING) {
                    toHideKeyboard();
                }
            }

        };
        actionBarDrawerToggle.syncState();
        drawerLayout.setDrawerListener(actionBarDrawerToggle);

        sideMenuListAdapter = new SideMenuListAdapter(currentActivity);
        managerSideMenu = new LinearLayoutManager(currentActivity);

        recyclerNavigationDrawer.setLayoutManager(managerSideMenu);
        recyclerNavigationDrawer.setAdapter(sideMenuListAdapter);
        displayProfile();
        setSelection(HOME);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        if (menu instanceof MenuBuilder) {
            ((MenuBuilder) menu).setOptionalIconsVisible(true);
        }
        inflater.inflate(R.menu.menu_dashbord, menu);
        for (int i = 0; i < menu.size(); i++) {
            MenuItem item = menu.getItem(i);
            SpannableString spanString = new SpannableString(menu.getItem(i).getTitle().toString());
            spanString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorWhite)), 0, spanString.length(), 0);
            item.setTitle(spanString);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (item.getItemId() == android.R.id.home) {
            return true;
        } else if (id == R.id.menuSendBitcoin) {
            startActivity(currentActivity, SendBitcoinsActivity.class, bundle, false, REQUEST_TAG_NO_RESULT, true, ANIMATION_SLIDE_UP);
            return true;
        } else if (id == R.id.menuReceiveBitcoin) {
            startActivity(currentActivity, ReceiveBitcoinActivity.class, bundle, false, REQUEST_TAG_NO_RESULT, true, ANIMATION_SLIDE_UP);
            return true;
        } else if (id == R.id.menuBuyShell) {
            startActivity(currentActivity, BuyActivity.class, bundle, false, REQUEST_TAG_NO_RESULT, true, ANIMATION_SLIDE_UP);
            return true;
        } else if (id == R.id.menuShell) {
            startActivity(currentActivity, BySellBitcoinActivity.class, bundle, false, REQUEST_TAG_NO_RESULT, true, ANIMATION_SLIDE_UP);
            return true;
        }
        else if (id == R.id.menuMobileTopup) {
            startActivity(currentActivity, MobileTopUpActivity.class, bundle, false, REQUEST_TAG_NO_RESULT, true, ANIMATION_SLIDE_UP);
            return true;
        } else if (id == R.id.menuDthRecharge) {
            startActivity(currentActivity, DthRechargeActivity.class, bundle, false, REQUEST_TAG_NO_RESULT, true, ANIMATION_SLIDE_UP);
            return true;
        } else if (id == R.id.menuGasBillPayment) {
            startActivity(currentActivity, GasProviderActivity.class, bundle, false, REQUEST_TAG_NO_RESULT, true, ANIMATION_SLIDE_UP);
            return true;
        } else if (id == R.id.menuElectricityBillPayment) {
            startActivity(currentActivity, ElectricityProviderActivity.class, bundle, false, REQUEST_TAG_NO_RESULT, true, ANIMATION_SLIDE_UP);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void displayProfile() {
        String userName = SharedPreferenceUtils.getInstance(context).getString(USER_NAME);
        String userImage = SharedPreferenceUtils.getInstance(context).getString(USER_PROFILE_IMAGE);
        if (!TextUtils.isEmpty(userName)) {
            textUserName.setText(userName);
        }
        textUserMobile.setText(SharedPreferenceUtils.getInstance(context).getString(USER_MOBILE_NO));
        if (!TextUtils.isEmpty(userImage)) {
            String imageUrl = BASE_URL_IMAGES + userImage;
            Picasso.with(currentActivity).load(imageUrl).error(R.drawable.logo_drawer).into(circleImageDrawerProfile);
        }
        FontUtils.changeFont(context, textUserName, FONT_CORBEL_REGULAR);
        FontUtils.changeFont(context, textUserMobile, FONT_ROBOTO_REGULAR);
    }

    public void setSelection(int position) {
        drawerLayout.closeDrawer(Gravity.LEFT);
        frag = null;
        boolean isReplace = false;
        switch (position) {

            case HOME: {
                frag = new HomeFragment();
                settingTitle(getResources().getString(R.string.title_home));
                removingHomeButton();
                isReplace = true;
                break;
            }
            case BIT_ADDRESS: {
                frag = new BitAddressFragment();
                settingTitle(getResources().getString(R.string.title_bit_assress));
                removingHomeButton();
                isReplace = true;
                break;
            }
            case DOCUMENTS: {
                frag = new DocumentFragment();
                settingTitle(getResources().getString(R.string.title_documents));
                removingHomeButton();
                isReplace = true;
                break;
            }
            case CONTACT_BOOK: {
                frag = new ContactBookFragment();
                settingTitle(getResources().getString(R.string.title_contacts));
                removingHomeButton();
                isReplace = true;
                break;
            }
            case TRANSACTIONS: {
                frag = new TransactionFragment();
                settingTitle(getResources().getString(R.string.title_transactions));
                removingHomeButton();
                isReplace = true;
                break;
            }
            case SUPPORT: {
                frag = new SupportFragment();
                settingTitle(getResources().getString(R.string.title_support));
                removingHomeButton();
                isReplace = true;
                break;
            }
            case ABOUT_US: {
                frag = new AboutUsFragment();
                settingTitle(getResources().getString(R.string.title_about_us));
                removingHomeButton();
                isReplace = true;
                break;
            }
        }
        if (frag != null) {
            switchContent(frag, false, isReplace, frag.getClass().getName());
        }
    }


    @Override
    protected void initContext() {
        currentActivity = DashboardActivity.this;
        context = DashboardActivity.this;
    }

    @Override
    protected void initListners() {
        editUserProfileIV.setOnClickListener(this);
    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        actionBarDrawerToggle.syncState();
        super.onPostCreate(savedInstanceState);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        actionBarDrawerToggle.onConfigurationChanged(newConfig);
        super.onConfigurationChanged(newConfig);
    }


    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(Gravity.LEFT)) {
            drawerLayout.closeDrawer(Gravity.LEFT);
        } else {
            if (isExitable) {
                super.onBackPressed();
            } else {
                toast(getResources().getString(R.string.message_app_exit), true);
                isExitable = true;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        isExitable = false;
                    }
                }, APP_EXIT_TIME);
            }
        }

    }


    @Override
    protected boolean isActionBar() {
        return true;
    }

    @Override
    protected boolean isHomeButton() {
        return false;
    }


    @Override
    public void onAlertClicked(int alertType) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.editUserProfileIV: {
                //drawerLayout.closeDrawer(Gravity.LEFT);
                startActivity(currentActivity, ProfileActivity.class, bundle, true, REQUEST_TAG_EDIT_PROFILE_ACTIVITY, true, ANIMATION_SLIDE_UP);
                break;
            }
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case WRITE_EXTERNAL_STORAGE_REQUEST:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (frag != null) {
                        frag.onRequestPermissionsResult(requestCode, permissions, grantResults);
                    }
                }
                break;
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        switch (requestCode) {
            case REQUEST_TAG_EDIT_PROFILE_ACTIVITY: {
                displayProfile();
                break;
            }
        }
    }
}
