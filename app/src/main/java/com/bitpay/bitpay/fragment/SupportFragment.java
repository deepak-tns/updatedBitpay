package com.bitpay.bitpay.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bitpay.bitpay.R;
import com.bitpay.bitpay.activity.BaseActivity;
import com.bitpay.bitpay.application.BitPayApplication;
import com.bitpay.bitpay.models.SupportModel;
import com.bitpay.bitpay.utils.FontUtils;
import com.bitpay.bitpay.utils.SharedPreferenceUtils;
import com.bitpay.bitpay.utils.Validator;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class SupportFragment extends BaseFragment {

    private EditText editName;
    private EditText editEmail;
    private EditText editquery;
    private Button btnSubmit;

    public SupportFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return view = inflater.inflate(R.layout.fragment_support, container, false);
    }

    @Override
    public void alertOkClicked(int alertType) {

    }

    @Override
    protected void initViews() {
        editName = (EditText) view.findViewById(R.id.editName);
        editEmail = (EditText) view.findViewById(R.id.editEmail);
        editquery = (EditText) view.findViewById(R.id.editquery);
        btnSubmit = (Button) view.findViewById(R.id.btnSubmit);
        FontUtils.changeFont(context, editName, FONT_ROBOTO_REGULAR);
        FontUtils.changeFont(context, editEmail, FONT_ROBOTO_REGULAR);
        FontUtils.changeFont(context, editquery, FONT_ROBOTO_REGULAR);
        FontUtils.changeFont(context, btnSubmit, FONT_CORBEL_REGULAR);

        editName.setText(SharedPreferenceUtils.getInstance(context).getString(USER_NAME));
        editEmail.setText(SharedPreferenceUtils.getInstance(context).getString(USER_EMAIL));
    }

    @Override
    protected void initContext() {
        context = getActivity();
        currentActivity = getActivity();
    }

    @Override
    protected void initListners() {
        btnSubmit.setOnClickListener(this);
    }


    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSubmit: {
                toHideKeyboard();
                if (Validator.isNetworkAvailable(currentActivity)) {
                    if (isMandatoryFields()) {
                        addSupport();
                    }
                } else {
                    alert(currentActivity, getResources().getString(R.string.alert_message_no_network), getResources().getString(R.string.alert_message_no_network), getResources().getString(R.string.alert_ok_button_text_no_network), getResources().getString(R.string.alert_cancel_button_text_no_network), false, false, ALERT_TYPE_NO_NETWORK);
                }
                break;
            }
        }
    }

    private boolean isMandatoryFields() {

        editName.setError(null);
        editEmail.setError(null);
        editquery.setError(null);

        if (editName.getText().toString().isEmpty()) {
            editName.setError(getResources().getString(R.string.error_empty_name));
            editName.requestFocus();
            return false;
        } else if (!Validator.getInstance().isValidEmail(context, editEmail.getText().toString()).equals("")) {
            String emailError = Validator.getInstance().isValidEmail(context, editEmail.getText().toString());
            editEmail.setError(emailError);
            editEmail.requestFocus();
            return false;
        } else if (editquery.getText().toString().isEmpty()) {
            editquery.setError(getResources().getString(R.string.error_empty_query));
            editquery.requestFocus();
            return false;
        }
        initSupportModel();
        return true;

    }

    private void initSupportModel() {
        SupportModel.getInstance().setUserId(SharedPreferenceUtils.getInstance(context).getInteger(USER_ID));
        SupportModel.getInstance().setName(editName.getText().toString());
        SupportModel.getInstance().setEmail(editEmail.getText().toString());
        SupportModel.getInstance().setMessage(editquery.getText().toString());
        SupportModel.getInstance().setTicketNumber(System.currentTimeMillis() + "");

    }

    private void addSupport() {

        progressDialog(context, context.getString(R.string.pdialog_message_loading), context.getString(R.string.pdialog_message_loading), false, false);

        JSONObject jsonAddSupportRequest = null;
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        try {
            jsonAddSupportRequest = new JSONObject(gson.toJson(SupportModel.getInstance()));
            Log.e("jsonAddSupportRequest", jsonAddSupportRequest.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final String URL_ADD_SUPPORT_URL = ADD_SUPPORT_URL;
        JsonObjectRequest sellBitcoinRequest = new JsonObjectRequest(Request.Method.POST, URL_ADD_SUPPORT_URL, jsonAddSupportRequest, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    cancelProgressDialog();
                    ((BaseActivity) currentActivity).logTesting(getResources().getString(R.string.nwk_response_submit_query), response.toString(), Log.ERROR);
                    String message = response.getString(RESPONCE_MESSAGE);
                    if (response.getBoolean(RESPONCE_ERROR) || message.isEmpty()) {
                        alert(currentActivity, getString(R.string.msg_submit_query_failed), getString(R.string.msg_submit_query_failed), getResources().getString(R.string.alert_ok_button_text_no_network), getResources().getString(R.string.alert_cancel_button_text_no_network), false, false, ALERT_TYPE_NO_NETWORK);
                    } else {
                        toast(context, getString(R.string.msg_submit_query_suc));
                        editName.setText("");
                        editEmail.setText("");
                        editquery.setText("");
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                cancelProgressDialog();
                toast(context, getResources().getString(R.string.nwk_error_submit_query));
                ((BaseActivity) currentActivity).logTesting(getResources().getString(R.string.nwk_error_submit_query), error.toString(), Log.ERROR);
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return super.getHeaders();
            }
        };
        BitPayApplication.getInstance().addToRequestQueue(sellBitcoinRequest);
    }
}
