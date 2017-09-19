package com.bitpay.bitpay.fragment;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bitpay.bitpay.R;
import com.bitpay.bitpay.activity.BaseActivity;
import com.bitpay.bitpay.adapter.ContactListAdapter;
import com.bitpay.bitpay.application.BitPayApplication;
import com.bitpay.bitpay.interfaces.RecyclerClickListner;
import com.bitpay.bitpay.models.AddContactModel;
import com.bitpay.bitpay.models.ContactModel;
import com.bitpay.bitpay.utils.FontUtils;
import com.bitpay.bitpay.utils.SharedPreferenceUtils;
import com.bitpay.bitpay.utils.Validator;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;


public class ContactBookFragment extends BaseFragment {

    private TextView addContactTV;
    private EditText editName;
    private EditText editBitAddress;
    private Button buttonAdd;
    private TextView noContactTV;
    private RecyclerView contactRecyclerView;
    private List<ContactModel> contactModelList;
    private ContactListAdapter contactListAdapter;

    public ContactBookFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_contact_book, container, false);
        return view;
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
        contactModelList = new ArrayList<>();
        addContactTV = (TextView) view.findViewById(R.id.addContactTV);
        editName = (EditText) view.findViewById(R.id.editName);
        editBitAddress = (EditText) view.findViewById(R.id.editBitAddress);
        buttonAdd = (Button) view.findViewById(R.id.buttonAdd);
        noContactTV = (TextView) view.findViewById(R.id.noContactTV);
        contactRecyclerView = (RecyclerView) view.findViewById(R.id.contactRecyclerView);
        FontUtils.changeFont(context, addContactTV, FONT_CORBEL_BOLD);
        FontUtils.changeFont(context, editName, FONT_ROBOTO_REGULAR);
        FontUtils.changeFont(context, editBitAddress, FONT_ROBOTO_REGULAR);
        FontUtils.changeFont(context, buttonAdd, FONT_CORBEL_REGULAR);
        FontUtils.changeFont(context, noContactTV, FONT_CORBEL_REGULAR);
        displayContactList();
        getContactList();
    }

    @Override
    protected void initContext() {
        context = getActivity();
        currentActivity = getActivity();
    }

    @Override
    protected void initListners() {
        buttonAdd.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonAdd: {
                toHideKeyboard();
                if (Validator.getInstance().isNetworkAvailable(currentActivity)) {
                    if (isMandatoryFields()) {
                        addContact();
                    }
                } else {
                    alert(currentActivity, getResources().getString(R.string.alert_message_no_network), getResources().getString(R.string.alert_message_no_network), getResources().getString(R.string.alert_ok_button_text_no_network), getResources().getString(R.string.alert_cancel_button_text_no_network), true, false, ALERT_TYPE_NO_NETWORK);
                }
                break;
            }
        }
    }

    private void displayContactList() {
        contactListAdapter = new ContactListAdapter(context, contactModelList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);
        contactRecyclerView.setLayoutManager(mLayoutManager);
        contactRecyclerView.setItemAnimator(new DefaultItemAnimator());
        contactRecyclerView.setAdapter(contactListAdapter);

        contactListAdapter.setOnItemClickListener(new RecyclerClickListner() {
            @Override
            public void onItemClick(int pos, View v) {

                if (contactModelList != null && contactModelList.size() > pos) {
                    ContactModel contactModel = contactModelList.get(pos);
                    if (contactModel == null) return;

                }
            }

            @Override
            public void onItemLongClick(int position, View v) {

            }
        });
    }


    private boolean isMandatoryFields() {

        editName.setError(null);
        editBitAddress.setError(null);

        if (editName.getText().toString().isEmpty()) {
            editName.setError(getString(R.string.error_empty_name));
            editName.requestFocus();
            return false;
        } else if (editBitAddress.getText().toString().isEmpty()) {
            editBitAddress.setError(getString(R.string.error_empty_bit_address));
            editBitAddress.requestFocus();
            return false;
        }
        initAddContactModel();
        return true;
    }

    private void initAddContactModel() {
        AddContactModel.getInstance().setContactName(editName.getText().toString());
        AddContactModel.getInstance().setBitAddress(editBitAddress.getText().toString().trim());
        AddContactModel.getInstance().setUserId(SharedPreferenceUtils.getInstance(context).getInteger(USER_ID));
    }

    private void addContact() {

        progressDialog(context, context.getString(R.string.pdialog_message_loading), context.getString(R.string.pdialog_message_loading), false, false);

        JSONObject jsonAddContactRequest = null;
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        try {
            jsonAddContactRequest = new JSONObject(gson.toJson(AddContactModel.getInstance()));
            Log.e("jsonAddContactRequest", jsonAddContactRequest.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final String URL_ADD_CONTACT = ADD_CONTACT_URL;
        JsonObjectRequest signUpRequest = new JsonObjectRequest(Request.Method.POST, URL_ADD_CONTACT, jsonAddContactRequest, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    ((BaseActivity) currentActivity).logTesting(getResources().getString(R.string.nwk_response_add_contact), response.toString(), Log.ERROR);
                    String message = response.getString(RESPONCE_MESSAGE);
                    if (response.getBoolean(RESPONCE_ERROR) || message.isEmpty()) {
                        cancelProgressDialog();
                        alert(currentActivity, getString(R.string.msg_add_contact_failed), getString(R.string.msg_add_contact_failed), getResources().getString(R.string.alert_ok_button_text_no_network), getResources().getString(R.string.alert_cancel_button_text_no_network), true, true, ALERT_TYPE_NO_NETWORK);
                    } else {
                        toast(context, getResources().getString(R.string.msg_add_contact_suc));
                        editName.setText("");
                        editBitAddress.setText("");
                        getContactList();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                cancelProgressDialog();
                toast(context, getResources().getString(R.string.nwk_error_add_contact));
                ((BaseActivity) currentActivity).logTesting(getResources().getString(R.string.nwk_error_add_contact), error.toString(), Log.ERROR);

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return super.getHeaders();
            }
        };
        BitPayApplication.getInstance().addToRequestQueue(signUpRequest);
    }

    private void getContactList() {

        int userId = SharedPreferenceUtils.getInstance(context).getInteger(USER_ID);

        progressDialog(context, context.getString(R.string.pdialog_message_loading), context.getString(R.string.pdialog_message_loading), false, false);

        JSONObject jsonGetContact = null;
        String URL_GET_CONTACT = GET_CONTACT_URL;
        try {
            jsonGetContact = new JSONObject();
            jsonGetContact.put("user_id", userId);
            Log.e("jsonGetContact", jsonGetContact.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest getGetContactRequest = new JsonObjectRequest(Request.Method.POST, URL_GET_CONTACT, jsonGetContact, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                cancelProgressDialog();
                try {
                    ((BaseActivity) currentActivity).logTesting(currentActivity.getResources().getString(R.string.nwk_response_get_contact), response.toString(), Log.ERROR);
                    String message = response.getString(RESPONCE_MESSAGE);

                    if (response.getBoolean(RESPONCE_ERROR)) {
                        contactModelList.clear();
                        contactListAdapter.notifyDataSetChanged();
                        contactRecyclerView.setVisibility(View.GONE);
                        noContactTV.setVisibility(View.VISIBLE);
                    } else {
                        contactRecyclerView.setVisibility(View.VISIBLE);
                        noContactTV.setVisibility(View.GONE);
                        GsonBuilder builder = new GsonBuilder();
                        Gson gson = builder.create();
                        List<ContactModel> contactList = Arrays.asList(gson.fromJson(message.toString(), ContactModel[].class));
                        contactModelList.clear();
                        contactModelList.addAll(contactList);
                        contactListAdapter.notifyDataSetChanged();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                cancelProgressDialog();
                contactRecyclerView.setVisibility(View.GONE);
                noContactTV.setVisibility(View.VISIBLE);
                ((BaseActivity) currentActivity).toast(currentActivity.getResources().getString(R.string.nwk_error_get_contact), true);
                ((BaseActivity) currentActivity).logTesting(currentActivity.getResources().getString(R.string.nwk_error_get_contact), error.toString(), Log.ERROR);

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return super.getHeaders();
            }
        };

        BitPayApplication.getInstance().addToRequestQueue(getGetContactRequest);
    }

}
