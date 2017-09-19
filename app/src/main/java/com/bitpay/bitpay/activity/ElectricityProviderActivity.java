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


public class ElectricityProviderActivity extends BaseActivity {

    private RecyclerView electricProviderRecyclerView;
    private ArrayList<ConnectionProviderModel> connectionProviderModelArrayList;
    private ProviderAdapter connectionProviderAdapter;
    private ConnectionProviderModel connectionProviderModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_electrc_provider);
    }

    @Override
    protected void initViews() {
        settingTitle(getString(R.string.title_electricity_provider));
        electricProviderRecyclerView = (RecyclerView) findViewById(R.id.electricProviderRecyclerView);
        getGasProviderList();
        displayGasProviderList();
    }

    @Override
    protected void initContext() {
        context = ElectricityProviderActivity.this;
        currentActivity = ElectricityProviderActivity.this;
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
                    startActivity(currentActivity, ElectricityBillpaymentActivity.class, bundle, true, REQUEST_TAG_CONFIRM_PIN, true, ANIMATION_SLIDE_UP);
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
        electricProviderRecyclerView.setLayoutManager(mLayoutManager);
        electricProviderRecyclerView.setItemAnimator(new DefaultItemAnimator());
        electricProviderRecyclerView.setAdapter(connectionProviderAdapter);
    }

    private void getGasProviderList() {

        ConnectionProviderModel connectionProviderModel;
        connectionProviderModelArrayList = new ArrayList<>();

        connectionProviderModel = new ConnectionProviderModel();
        connectionProviderModel.setProciderId(34);
        connectionProviderModel.setConnectionName("BSES Yamuna - DELHI");
        connectionProviderModelArrayList.add(connectionProviderModel);

        connectionProviderModel = new ConnectionProviderModel();
        connectionProviderModel.setProciderId(35);
        connectionProviderModel.setConnectionName("BSES Rajdhani - DELHI");
        connectionProviderModelArrayList.add(connectionProviderModel);

        connectionProviderModel = new ConnectionProviderModel();
        connectionProviderModel.setProciderId(36);
        connectionProviderModel.setConnectionName("NORTH DELHI POWER LIMITED");
        connectionProviderModelArrayList.add(connectionProviderModel);

        connectionProviderModel = new ConnectionProviderModel();
        connectionProviderModel.setProciderId(105);
        connectionProviderModel.setConnectionName("Uttar Pradesh Power Corporation Limited");
        connectionProviderModelArrayList.add(connectionProviderModel);

        connectionProviderModel = new ConnectionProviderModel();
        connectionProviderModel.setProciderId(101);
        connectionProviderModel.setConnectionName("Noida Power - NOIDA");
        connectionProviderModelArrayList.add(connectionProviderModel);

        connectionProviderModel = new ConnectionProviderModel();
        connectionProviderModel.setProciderId(86);
        connectionProviderModel.setConnectionName("Jaipur Vidyut Vitran Nigam - RAJASTHAN");
        connectionProviderModelArrayList.add(connectionProviderModel);

        connectionProviderModel = new ConnectionProviderModel();
        connectionProviderModel.setProciderId(87);
        connectionProviderModel.setConnectionName("BESCOM - BENGALURU");
        connectionProviderModelArrayList.add(connectionProviderModel);

        connectionProviderModel = new ConnectionProviderModel();
        connectionProviderModel.setProciderId(88);
        connectionProviderModel.setConnectionName("JODHPUR VIDYUT VITRAN NIGAM");
        connectionProviderModelArrayList.add(connectionProviderModel);

        connectionProviderModel = new ConnectionProviderModel();
        connectionProviderModel.setProciderId(89);
        connectionProviderModel.setConnectionName("South Bihar Power Distribution Company");
        connectionProviderModelArrayList.add(connectionProviderModel);

        connectionProviderModel = new ConnectionProviderModel();
        connectionProviderModel.setProciderId(90);
        connectionProviderModel.setConnectionName("North Bihar Power Distribution Company");
        connectionProviderModelArrayList.add(connectionProviderModel);

        connectionProviderModel = new ConnectionProviderModel();
        connectionProviderModel.setProciderId(93);
        connectionProviderModel.setConnectionName("Southern Power - ANDHRA PRADESH");
        connectionProviderModelArrayList.add(connectionProviderModel);

        connectionProviderModel = new ConnectionProviderModel();
        connectionProviderModel.setProciderId(94);
        connectionProviderModel.setConnectionName("Southern Power - TELANGANA");
        connectionProviderModelArrayList.add(connectionProviderModel);

        connectionProviderModel = new ConnectionProviderModel();
        connectionProviderModel.setProciderId(96);
        connectionProviderModel.setConnectionName("Madhya Kshetra Vitaran - MADHYA PRADESH");
        connectionProviderModelArrayList.add(connectionProviderModel);

        connectionProviderModel = new ConnectionProviderModel();
        connectionProviderModel.setProciderId(97);
        connectionProviderModel.setConnectionName("Rajasthan Vidyut Vitran Nigam Limited");
        connectionProviderModelArrayList.add(connectionProviderModel);

        connectionProviderModel = new ConnectionProviderModel();
        connectionProviderModel.setProciderId(98);
        connectionProviderModel.setConnectionName("MSEDC - MAHARASHTRA");
        connectionProviderModelArrayList.add(connectionProviderModel);

        connectionProviderModel = new ConnectionProviderModel();
        connectionProviderModel.setProciderId(99);
        connectionProviderModel.setConnectionName("INDIA POWER LTD");
        connectionProviderModelArrayList.add(connectionProviderModel);

        connectionProviderModel = new ConnectionProviderModel();
        connectionProviderModel.setProciderId(100);
        connectionProviderModel.setConnectionName("Jamshedpur Utilities & Services (JUSCO)");
        connectionProviderModelArrayList.add(connectionProviderModel);

        connectionProviderModel = new ConnectionProviderModel();
        connectionProviderModel.setProciderId(102);
        connectionProviderModel.setConnectionName("Brihan Mumbai Electric Supply and Transport Undertaking");
        connectionProviderModelArrayList.add(connectionProviderModel);

        connectionProviderModel = new ConnectionProviderModel();
        connectionProviderModel.setProciderId(103);
        connectionProviderModel.setConnectionName("Paschim Kshetra Vitaran - MADHYA PRADESH");
        connectionProviderModelArrayList.add(connectionProviderModel);

        connectionProviderModel = new ConnectionProviderModel();
        connectionProviderModel.setProciderId(105);
        connectionProviderModel.setConnectionName("Uttar Pradesh Power Corporation Limited");
        connectionProviderModelArrayList.add(connectionProviderModel);


        connectionProviderModel = new ConnectionProviderModel();
        connectionProviderModel.setProciderId(48);
        connectionProviderModel.setConnectionName("Ajmer Vidyut Vitran Nigam - RAJASTHAN");
        connectionProviderModelArrayList.add(connectionProviderModel);

        connectionProviderModel = new ConnectionProviderModel();
        connectionProviderModel.setProciderId(50);
        connectionProviderModel.setConnectionName("CSPDCL - CHHATTISGARH");
        connectionProviderModelArrayList.add(connectionProviderModel);

        connectionProviderModel = new ConnectionProviderModel();
        connectionProviderModel.setProciderId(52);
        connectionProviderModel.setConnectionName("TORRENT POWER");
        connectionProviderModelArrayList.add(connectionProviderModel);

        connectionProviderModel = new ConnectionProviderModel();
        connectionProviderModel.setProciderId(54);
        connectionProviderModel.setConnectionName("UTTAR GUJARAT VIJ COMPANY");
        connectionProviderModelArrayList.add(connectionProviderModel);

        connectionProviderModel = new ConnectionProviderModel();
        connectionProviderModel.setProciderId(55);
        connectionProviderModel.setConnectionName("RELIANCE ENERGY MUMBAI");
        connectionProviderModelArrayList.add(connectionProviderModel);

        connectionProviderModel = new ConnectionProviderModel();
        connectionProviderModel.setProciderId(56);
        connectionProviderModel.setConnectionName("UTTAR HARYANA BIJLI VITRANA NI");
        connectionProviderModelArrayList.add(connectionProviderModel);

        connectionProviderModel = new ConnectionProviderModel();
        connectionProviderModel.setProciderId(57);
        connectionProviderModel.setConnectionName("WEST BENGAL ELECTRICITY DISTRI");
        connectionProviderModelArrayList.add(connectionProviderModel);

        connectionProviderModel = new ConnectionProviderModel();
        connectionProviderModel.setProciderId(59);
        connectionProviderModel.setConnectionName("BEST Undertaking - MUMBAI");
        connectionProviderModelArrayList.add(connectionProviderModel);

        connectionProviderModel = new ConnectionProviderModel();
        connectionProviderModel.setProciderId(63);
        connectionProviderModel.setConnectionName("TATA POWER DELHI LIMITED");
        connectionProviderModelArrayList.add(connectionProviderModel);

        connectionProviderModel = new ConnectionProviderModel();
        connectionProviderModel.setProciderId(69);
        connectionProviderModel.setConnectionName("PANJAB STATE POWER CORPORATION");
        connectionProviderModelArrayList.add(connectionProviderModel);

        connectionProviderModel = new ConnectionProviderModel();
        connectionProviderModel.setProciderId(71);
        connectionProviderModel.setConnectionName("TAMIL NADU ELECTICITY BOARD");
        connectionProviderModelArrayList.add(connectionProviderModel);

        connectionProviderModel = new ConnectionProviderModel();
        connectionProviderModel.setProciderId(74);
        connectionProviderModel.setConnectionName("DAKSHIN GUJARAT VIJ COMPANY");
        connectionProviderModelArrayList.add(connectionProviderModel);

        connectionProviderModel = new ConnectionProviderModel();
        connectionProviderModel.setProciderId(75);
        connectionProviderModel.setConnectionName("MADHYA GUJARAT VIJ COMPANY");
        connectionProviderModelArrayList.add(connectionProviderModel);

        connectionProviderModel = new ConnectionProviderModel();
        connectionProviderModel.setProciderId(76);
        connectionProviderModel.setConnectionName("PASCHIM GUJARAT VIJ COMPANY");
        connectionProviderModelArrayList.add(connectionProviderModel);

        connectionProviderModel = new ConnectionProviderModel();
        connectionProviderModel.setProciderId(83);
        connectionProviderModel.setConnectionName("CESC - WEST BENGAL");
        connectionProviderModelArrayList.add(connectionProviderModel);


        connectionProviderModel = new ConnectionProviderModel();
        connectionProviderModel.setProciderId(113);
        connectionProviderModel.setConnectionName("APDCL - ASSAM");
        connectionProviderModelArrayList.add(connectionProviderModel);

        connectionProviderModel = new ConnectionProviderModel();
        connectionProviderModel.setProciderId(114);
        connectionProviderModel.setConnectionName("TSECL - TRIPURA");
        connectionProviderModelArrayList.add(connectionProviderModel);

    }
}
