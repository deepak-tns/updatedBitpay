package com.bitpay.bitpay.activity;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.bitpay.bitpay.R;
import com.bitpay.bitpay.adapter.ProviderAdapter;
import com.bitpay.bitpay.interfaces.RecyclerClickListner;
import com.bitpay.bitpay.models.ConnectionProviderModel;

import java.util.ArrayList;


public class GasProviderActivity extends BaseActivity {

    private RecyclerView gasProviderRecyclerView;
    private ArrayList<ConnectionProviderModel> connectionProviderModelArrayList;
    private ProviderAdapter connectionProviderAdapter;
    private ConnectionProviderModel connectionProviderModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gas_provider);
    }

    @Override
    protected void initViews() {
        settingTitle(getString(R.string.title_gas_provider));
        gasProviderRecyclerView = (RecyclerView) findViewById(R.id.gasProviderRecyclerView);
        getGasProviderList();
        displayGasProviderList();
    }

    @Override
    protected void initContext() {
        context = GasProviderActivity.this;
        currentActivity = GasProviderActivity.this;
    }

    @Override
    protected void initListners() {
        connectionProviderAdapter.setOnItemClickListener(new RecyclerClickListner() {
            @Override
            public void onItemClick(int pos, View v) {

                if (connectionProviderModelArrayList != null && connectionProviderModelArrayList.size() > pos) {
                    connectionProviderModel = connectionProviderModelArrayList.get(pos);
                    if (connectionProviderModel == null) return;
                    if (bundle == null) {
                        bundle = new Bundle();
                    }
                    bundle.putSerializable(KEY_PROVIDER_MODEL, connectionProviderModel);
                    startActivity(currentActivity, GasBillpaymentActivity.class, bundle, true, REQUEST_TAG_CONFIRM_PIN, true, ANIMATION_SLIDE_UP);
                }
            }

            @Override
            public void onItemLongClick(int position, View v) {

            }
        });
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
    public void onAlertClicked(int alertType) {

    }

    @Override
    public void onClick(View v) {

    }

    private void displayGasProviderList() {
        connectionProviderAdapter = new ProviderAdapter(context, connectionProviderModelArrayList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);
        gasProviderRecyclerView.setLayoutManager(mLayoutManager);
        gasProviderRecyclerView.setItemAnimator(new DefaultItemAnimator());
        gasProviderRecyclerView.setAdapter(connectionProviderAdapter);
    }

    private void getGasProviderList() {

        ConnectionProviderModel connectionProviderModel;
        connectionProviderModelArrayList = new ArrayList<>();

        connectionProviderModel = new ConnectionProviderModel();
        connectionProviderModel.setProciderId(80);
        connectionProviderModel.setConnectionName("GUJARAT GAS COMPANY LTD");
        connectionProviderModelArrayList.add(connectionProviderModel);

        connectionProviderModel = new ConnectionProviderModel();
        connectionProviderModel.setProciderId(81);
        connectionProviderModel.setConnectionName("INDRAPRASHTA GAS LTD");
        connectionProviderModelArrayList.add(connectionProviderModel);


        connectionProviderModel = new ConnectionProviderModel();
        connectionProviderModel.setProciderId(85);
        connectionProviderModel.setConnectionName("MAHANAGAR GAS LTD");
        connectionProviderModelArrayList.add(connectionProviderModel);

    }
}
