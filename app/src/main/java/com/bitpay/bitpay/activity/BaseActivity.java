package com.bitpay.bitpay.activity;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bitpay.bitpay.R;
import com.bitpay.bitpay.constant.AppConstants;
import com.bitpay.bitpay.interfaces.AlertClicked;
import com.bitpay.bitpay.utils.FontUtils;

import java.util.HashMap;
import java.util.Map;

public abstract class BaseActivity extends AppCompatActivity implements View.OnClickListener, AlertClicked, AppConstants {

    Activity currentActivity;
    public Bundle bundle;
    Context context;
    ProgressDialog pdialog;

    protected abstract void initViews();

    protected abstract void initContext();

    protected abstract void initListners();

    protected abstract boolean isActionBar();

    protected abstract boolean isHomeButton();

    public Map<String, String> linkParamsMap;

    Dialog customDialog;
    Toolbar toolbar;

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        CoordinatorLayout layout = (CoordinatorLayout) getLayoutInflater().inflate(R.layout.activity_base, null);
        LinearLayout activityLayout = (LinearLayout) layout.findViewById(R.id.activity_layout);
        getLayoutInflater().inflate(layoutResID, activityLayout, true);

        super.setContentView(layout);


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (isActionBar()) {
            setSupportActionBar(toolbar);
            applyFontForToolbarTitle();
        } else {
            toolbar.setVisibility(View.GONE);
        }

        if (isHomeButton()) {
            settingHomeButton();
        }

        initContext();
        initViews();
        initListners();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        linkParamsMap = new HashMap<>();
    }


    public void progressDialog(Context context, String title, String message, boolean cancelable, boolean isTitle) {
        if (pdialog == null) {
            pdialog = new ProgressDialog(context);
        }


        if (isTitle) {
            pdialog.setTitle(title);
        }

        pdialog.setMessage(message);

        if (!cancelable) {
            pdialog.setCancelable(false);
        }

        if (!pdialog.isShowing()) {
            pdialog.show();

        }

    }

    public void cancelProgressDialog() {
        pdialog.cancel();
    }


    public void toast(String text, boolean isLengthLong) {
        int time;
        if (isLengthLong) {
            time = Toast.LENGTH_LONG;
        } else {
            time = Toast.LENGTH_SHORT;
        }
        Toast.makeText(currentActivity, text, time).show();
    }

    public void toastTesting(String text, boolean isLengthLong) {
        int time;
        if (isLengthLong) {
            time = Toast.LENGTH_LONG;
        } else {
            time = Toast.LENGTH_SHORT;
        }
        Toast.makeText(currentActivity, text, time).show();
    }


    public static void log(String key, String value, int LogType) {
        if (LogType == Log.ASSERT) {
            Log.wtf(key, value);
        } else if (LogType == Log.DEBUG) {
            Log.d(key, value);
        } else if (LogType == Log.ERROR) {
            Log.e(key, value);
        } else if (LogType == Log.INFO) {
            Log.i(key, value);
        } else if (LogType == Log.VERBOSE) {
            Log.v(key, value);
        } else if (LogType == Log.WARN) {
            Log.w(key, value);
        }

    }


    public void logTesting(String key, String value, int LogType) {
        if (LogType == Log.ASSERT) {
            Log.wtf(key, value);
        } else if (LogType == Log.DEBUG) {
            Log.d(key, value);
        } else if (LogType == Log.ERROR) {
            Log.e(key, value);
        } else if (LogType == Log.INFO) {
            Log.i(key, value);
        } else if (LogType == Log.VERBOSE) {
            Log.v(key, value);
        } else if (LogType == Log.WARN) {
            Log.w(key, value);
        }
    }


    protected void toHideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public void settingHomeButton() {

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_icon);
    }

    public void settingTitle(String title) {
        getSupportActionBar().setTitle(title);
    }

    public void removingHomeButton() {

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (item.getItemId() == android.R.id.home) {

            finish();
            overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_right);
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    public void switchContent(Fragment fragment, boolean addToBackStack, boolean isReplace, String tag) {

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        if (isReplace) {
            ft.replace(R.id.fragment_container, fragment, tag);
        } else {
            ft.add(R.id.fragment_container, fragment, tag);
        }
        if (addToBackStack) {
            ft.addToBackStack(tag);
        }
        ft.commit();
    }

    public void popBackStack(String tag) {
        if (getSupportFragmentManager().getBackStackEntryCount() >= 0) {
            getSupportFragmentManager().popBackStack(tag, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }

    }


    protected Dialog creatingDialog(Context context, boolean isCancelableBack, boolean isCancelableoutside, View view, int height, int width, boolean heightMatchParent) {

        return customDialog;

    }

    protected void cancelCustomDialog() {
        if (customDialog != null) {
            customDialog.cancel();
        }

    }


    protected Dialog creatingDialog(Context context, boolean isCancelableBack, boolean isCancelableoutside, View view, int height, int width) {
        return customDialog;
    }


    public void alert(Context context, String title, String message, String positiveButton, String negativeButton, boolean isNegativeButton, boolean isTitle, final int alertType) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        if (isTitle) {
            builder.setTitle(title);
        }


        builder.setMessage(message);
        builder.setPositiveButton(positiveButton, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                onAlertClicked(alertType);
            }
        });
        if (isNegativeButton) {
            builder.setNegativeButton(negativeButton, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
        }
        builder.show();
    }

    public void startActivity(Activity activity, Class newclass, Bundle bundle, boolean isResult, int requestCode, boolean animationRequired, int animationType) {
        Intent intent = new Intent(activity, newclass);
        if (newclass.getName().equalsIgnoreCase(DashboardActivity.class.getName())) {
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        }
        if (bundle != null)

            intent.putExtras(bundle);
        if (!isResult && !animationRequired)
            startActivity(intent);
        else if (!isResult && animationRequired) {
            startActivity(intent);
            if (animationType == AppConstants.ANIMATION_SLIDE_LEFT) {
                overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);
            } else if (animationType == AppConstants.ANIMATION_SLIDE_UP) {
                overridePendingTransition(R.anim.anim_slide_up, R.anim.anim_stopping_exiting_activity);
            }

        } else if (isResult && animationRequired) {
            startActivityForResult(intent, requestCode);
            if (animationType == AppConstants.ANIMATION_SLIDE_LEFT) {
                overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);
            } else if (animationType == AppConstants.ANIMATION_SLIDE_UP) {
                overridePendingTransition(R.anim.anim_slide_up, R.anim.anim_stopping_exiting_activity);
            }
        } else
            startActivityForResult(intent, requestCode);
    }

    public void applyFontForToolbarTitle() {
        try {
            for (int i = 0; i < toolbar.getChildCount(); i++) {
                View view = toolbar.getChildAt(i);
                if (view instanceof TextView) {
                    TextView titleTV = (TextView) view;
                    if (titleTV.getText().equals(getTitle())) {
                        titleTV.setTextSize(getResources().getDimension(R.dimen.font_toolbar_title));
                        FontUtils.changeFont(this, titleTV, FONT_CORBEL_REGULAR);
                        break;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
