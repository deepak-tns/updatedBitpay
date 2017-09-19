package com.bitpay.bitpay.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.bitpay.bitpay.constant.AppConstants;


/**
 * A simple {@link Fragment} subclass.
 */
public abstract class BaseFragment extends Fragment implements AppConstants, View.OnClickListener {

    ProgressDialog pdialog;

    public abstract void alertOkClicked(int alertType);

    protected abstract void initViews();

    protected abstract void initContext();

    protected abstract void initListners();

    View view;

    Activity currentActivity;

    Context context;

    Bundle bundle;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        currentActivity = getActivity();
        context = getActivity();
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initContext();
        initViews();
        initListners();
    }


    public void alert(Context context, String title, String message, String positivebutton, String negativeButton, boolean isNegativeButton,boolean isTitle, final int alertType) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        //  builder.setLocationTitle(title);

        builder.setMessage(message);
        builder.setPositiveButton(positivebutton, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                alertOkClicked(alertType);
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

    public void cancelProgressDialog() {
        pdialog.cancel();
    }


    protected void progressDialog(Context context, String title, String message, boolean cancelable) {
        pdialog = new ProgressDialog(context);
        //  pdialog.setLocationTitle(title);
        pdialog.setMessage(message);

        if (!cancelable) {
            pdialog.setCancelable(false);
        }


        pdialog.show();

    }

    public void toast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();

    }

    protected void toHideKeyboard() {
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }


    public void logTesting(String key, String value) {
        Log.e(key, value);
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
}
