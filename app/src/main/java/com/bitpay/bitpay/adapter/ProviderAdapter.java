package com.bitpay.bitpay.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bitpay.bitpay.R;
import com.bitpay.bitpay.constant.AppConstants;
import com.bitpay.bitpay.interfaces.RecyclerClickListner;
import com.bitpay.bitpay.models.ConnectionProviderModel;
import com.bitpay.bitpay.utils.FontUtils;

import java.util.List;

/**
 * Created by Codeslay-03 on 2/15/2017.
 */
public class ProviderAdapter extends RecyclerView.Adapter<ProviderAdapter.ContactsViewHolder> {

    private List<ConnectionProviderModel> connectionProviderModelList;
    private Context context;
    private LayoutInflater layoutInflater;
    private static RecyclerClickListner clickListener;

    public class ContactsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView connectionProviderTextView;

        public ContactsViewHolder(View view) {
            super(view);
            connectionProviderTextView = (TextView) view.findViewById(R.id.connectionProviderTextView);
            /******set font*********/
            FontUtils.changeFont(context, connectionProviderTextView, AppConstants.FONT_CORBEL_REGULAR);
            view.setOnClickListener(this);
        }
        @Override
        public void onClick(View v) {
            clickListener.onItemClick(getAdapterPosition(), v);
        }
    }


    public ProviderAdapter(Context context, List<ConnectionProviderModel> connectionProviderModelList) {
        this.connectionProviderModelList = connectionProviderModelList;
        this.context = context;

    }

    @Override
    public ContactsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(context);
        }
        View itemView = layoutInflater.inflate(R.layout.provider_row_item, parent, false);
        ContactsViewHolder contactsViewHolder = new ContactsViewHolder(itemView);
        return contactsViewHolder;
    }

    @Override
    public void onBindViewHolder(ContactsViewHolder holder, int position) {
        if (connectionProviderModelList != null && connectionProviderModelList.size() > position) {
            ConnectionProviderModel connectionProviderModel = connectionProviderModelList.get(position);
            if (connectionProviderModel != null) {
                holder.connectionProviderTextView.setText(connectionProviderModel.getConnectionName());
            }
        }
    }

    @Override
    public int getItemCount() {
        if (connectionProviderModelList == null) {
            return 0;
        }
        return connectionProviderModelList.size();
    }

    public void setOnItemClickListener(RecyclerClickListner clickListener) {
        ProviderAdapter.clickListener = clickListener;
    }
}