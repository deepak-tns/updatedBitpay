package com.bitpay.bitpay.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bitpay.bitpay.R;
import com.bitpay.bitpay.adapter.ActivityAdapter;
import com.bitpay.bitpay.application.BitPayApplication;
import com.bitpay.bitpay.models.ActivityModel;
import com.bitpay.bitpay.models.ActivityModel;
import com.bitpay.bitpay.utils.SharedPreferenceUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created by Abhishek on 6/3/2017.
 */

public class ActivityFragment extends BaseFragment {
    private ListView activityListView;
    private ArrayList<ActivityModel> arrayList;
    private ActivityAdapter activityAdapter;

    public ActivityFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void alertOkClicked(int alertType) {

    }

    @Override
    protected void initViews() {
        arrayList = new ArrayList<>();
        activityListView = (ListView) view.findViewById(R.id.activityListView);
        setAdapter();
        getActivityList();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activityfragment, container, false);
        return view;
    }


    private void setAdapter() {
        activityAdapter = new ActivityAdapter(getContext(), arrayList);
        activityListView.setAdapter(activityAdapter);
    }


    @Override
    public void onClick(View v) {

    }

    private void getActivityList() {

        int userId = SharedPreferenceUtils.getInstance(context).getInteger(USER_ID);

        progressDialog(context, context.getString(R.string.pdialog_message_loading), context.getString(R.string.pdialog_message_loading), false, false);

        JSONObject jsonGetActivity = null;
        String URL_GET_ACTIVITY = GET_ACTIVITY_URL;
        try {
            jsonGetActivity = new JSONObject();
            jsonGetActivity.put(KEY_USER_ID, userId);
            Log.e("jsonGetActivity", jsonGetActivity.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest getGetActivityRequest = new JsonObjectRequest(Request.Method.POST, URL_GET_ACTIVITY, jsonGetActivity, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                cancelProgressDialog();
                try {
                    String message = response.getString(RESPONCE_MESSAGE);
                    if (response.getBoolean(RESPONCE_ERROR)) {
                        arrayList.clear();
                        activityAdapter.notifyDataSetChanged();
                    } else {
                        GsonBuilder builder = new GsonBuilder();
                        Gson gson = builder.create();
                        List<ActivityModel> contactList = Arrays.asList(gson.fromJson(message.toString(), ActivityModel[].class));
                        arrayList.clear();
                        arrayList.addAll(contactList);
                        activityAdapter.notifyDataSetChanged();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                cancelProgressDialog();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return super.getHeaders();
            }
        };

        BitPayApplication.getInstance().addToRequestQueue(getGetActivityRequest);
    }

}


