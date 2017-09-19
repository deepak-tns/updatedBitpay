package com.bitpay.bitpay.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bitpay.bitpay.R;
import com.bitpay.bitpay.constant.AppConstants;
import com.bitpay.bitpay.interfaces.RecyclerClickListner;
import com.bitpay.bitpay.models.UserTransactionModel;
import com.bitpay.bitpay.utils.DateTimeUtils;
import com.bitpay.bitpay.utils.FontUtils;

import java.util.List;

/**
 * Created by Codeslay-03 on 2/15/2017.
 */
public class TransactionListAdapter extends RecyclerView.Adapter<TransactionListAdapter.ContactsViewHolder> {

    private List<UserTransactionModel> transactionModelList;
    private Context context;
    private LayoutInflater layoutInflater;
    private static RecyclerClickListner clickListener;

    public class ContactsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView textDateTV;
        private TextView textDescTV;
        private TextView textAmtTV;
        private TextView textTnxIdTV;

        public ContactsViewHolder(View view) {
            super(view);
            textDateTV = (TextView) view.findViewById(R.id.textDateTV);
            textDescTV = (TextView) view.findViewById(R.id.textDescTV);
            textAmtTV = (TextView) view.findViewById(R.id.textAmtTV);
            textTnxIdTV = (TextView) view.findViewById(R.id.textTnxIdTV);
            /******set font*********/
            FontUtils.changeFont(context, textDateTV, AppConstants.FONT_ROBOTO_REGULAR);
            FontUtils.changeFont(context, textDescTV, AppConstants.FONT_CORBEL_REGULAR);
            FontUtils.changeFont(context, textAmtTV, AppConstants.FONT_ROBOTO_REGULAR);
            FontUtils.changeFont(context, textTnxIdTV, AppConstants.FONT_ROBOTO_REGULAR);

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            clickListener.onItemClick(getAdapterPosition(), v);
        }
    }


    public TransactionListAdapter(Context context, List<UserTransactionModel> transactionModelList) {
        this.transactionModelList = transactionModelList;
        this.context = context;
    }

    @Override
    public ContactsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(context);
        }
        View itemView = layoutInflater.inflate(R.layout.transaction_rowitem, parent, false);
        ContactsViewHolder contactsViewHolder = new ContactsViewHolder(itemView);
        return contactsViewHolder;
    }

    @Override
    public void onBindViewHolder(ContactsViewHolder holder, int position) {
        if (transactionModelList != null && transactionModelList.size() > position) {
            UserTransactionModel userTransactionModel = transactionModelList.get(position);
            if (userTransactionModel != null) {
                holder.textDateTV.setText(DateTimeUtils.getDateByTimestamp(userTransactionModel.getDate()));
                holder.textDescTV.setText(userTransactionModel.getTransectionType().toUpperCase());
                holder.textTnxIdTV.setText(userTransactionModel.getTransectionId());
                holder.textAmtTV.setText(userTransactionModel.getBitAmmount());
            }
        }
    }

    @Override
    public int getItemCount() {
        if (transactionModelList == null) {
            return 0;
        }
        return transactionModelList.size();
    }

    public void setOnItemClickListener(RecyclerClickListner clickListener) {
        TransactionListAdapter.clickListener = clickListener;
    }
}