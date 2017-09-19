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
import com.bitpay.bitpay.models.ContactModel;
import com.bitpay.bitpay.utils.FontUtils;

import java.util.List;

/**
 * Created by Codeslay-03 on 2/15/2017.
 */
public class ContactListAdapter extends RecyclerView.Adapter<ContactListAdapter.ContactsViewHolder> {

    private List<ContactModel> contactModelList;
    private Context context;
    private LayoutInflater layoutInflater;
    private static RecyclerClickListner clickListener;

    public class ContactsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView contactNameTV;
        private TextView bitAddressTV;
        private ImageView contactIV;

        public ContactsViewHolder(View view) {
            super(view);
            contactNameTV = (TextView) view.findViewById(R.id.contactNameTV);
            bitAddressTV = (TextView) view.findViewById(R.id.bitAddressTV);
            contactIV = (ImageView) view.findViewById(R.id.contactIV);
            /******set font*********/
            FontUtils.changeFont(context, contactNameTV, AppConstants.FONT_CORBEL_REGULAR);
            FontUtils.changeFont(context, bitAddressTV, AppConstants.FONT_ROBOTO_REGULAR);
            view.setOnClickListener(this);
        }
        @Override
        public void onClick(View v) {
            clickListener.onItemClick(getAdapterPosition(), v);
        }
    }


    public ContactListAdapter(Context context, List<ContactModel> contactModelList) {
        this.contactModelList = contactModelList;
        this.context = context;

    }

    @Override
    public ContactsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(context);
        }
        View itemView = layoutInflater.inflate(R.layout.contacts_rowitem, parent, false);
        ContactsViewHolder contactsViewHolder = new ContactsViewHolder(itemView);
        return contactsViewHolder;
    }

    @Override
    public void onBindViewHolder(ContactsViewHolder holder, int position) {
        if (contactModelList != null && contactModelList.size() > position) {
            ContactModel contactModel = contactModelList.get(position);
            if (contactModel != null) {
                holder.contactNameTV.setText(contactModel.getContactName());
                holder.bitAddressTV.setText(contactModel.getBitPayAddress().trim());
            }
        }
    }

    @Override
    public int getItemCount() {
        if (contactModelList == null) {
            return 0;
        }
        return contactModelList.size();
    }

    public void setOnItemClickListener(RecyclerClickListner clickListener) {
        ContactListAdapter.clickListener = clickListener;
    }
}