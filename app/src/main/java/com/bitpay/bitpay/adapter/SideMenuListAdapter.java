package com.bitpay.bitpay.adapter;


import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bitpay.bitpay.R;
import com.bitpay.bitpay.activity.DashboardActivity;
import com.bitpay.bitpay.constant.AppConstants;
import com.bitpay.bitpay.models.SideMenuModel;
import com.bitpay.bitpay.utils.FontUtils;

import java.util.ArrayList;


/**
 * Created by Codeslay-03 on 4/6/2017.
 */

public class SideMenuListAdapter extends RecyclerView.Adapter<SideMenuListAdapter.ViewHolder> {

    public ArrayList<SideMenuModel> sideMenuList;
    Activity currentActivty;


    public SideMenuListAdapter(Activity currentActivty) {
        this.currentActivty = currentActivty;
        sideMenuList = createSideMenuList();
    }

    @Override
    public SideMenuListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(currentActivty).inflate(R.layout.items_side_menu, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(SideMenuListAdapter.ViewHolder holder, int position) {
        final int pos = position;
        SideMenuModel menuModel = null;
        if (sideMenuList != null && sideMenuList.size() > pos) {
            menuModel = sideMenuList.get(pos);
            if (menuModel == null) return;
            holder.textSideBarItem.setText(menuModel.getMenuItemText());
            holder.imageSideBarItem.setBackgroundResource(menuModel.getMenuItemImg());
            FontUtils.changeFont(currentActivty, holder.textSideBarItem, AppConstants.FONT_CORBEL_REGULAR);
        }
        holder.containerSideMenuItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    ((DashboardActivity) currentActivty).setSelection(pos);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return sideMenuList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textSideBarItem;
        ImageView imageSideBarItem;
        LinearLayout containerSideMenuItems;

        public ViewHolder(View itemView) {
            super(itemView);
            textSideBarItem = (TextView) itemView.findViewById(R.id.textSideBarItem);
            imageSideBarItem = (ImageView) itemView.findViewById(R.id.imageSideBarItem);
            containerSideMenuItems = (LinearLayout) itemView.findViewById(R.id.containerSideMenuItems);
        }
    }

    private ArrayList<SideMenuModel> createSideMenuList() {

        ArrayList<SideMenuModel> sideMenuList = new ArrayList<>();

        SideMenuModel menuModel = null;

        menuModel = new SideMenuModel();
        menuModel.setMenuItemText(currentActivty.getString(R.string.nev_home));
        menuModel.setMenuItemImg(R.drawable.dashboard_icon);
        sideMenuList.add(menuModel);

        menuModel = new SideMenuModel();
        menuModel.setMenuItemText(currentActivty.getString(R.string.nev_bit_assress));
        menuModel.setMenuItemImg(R.drawable.my_bitcoion_address_icon);
        sideMenuList.add(menuModel);

        menuModel = new SideMenuModel();
        menuModel.setMenuItemText(currentActivty.getString(R.string.nev_documents));
        menuModel.setMenuItemImg(R.drawable.documents_icon);
        sideMenuList.add(menuModel);

        menuModel = new SideMenuModel();
        menuModel.setMenuItemText(currentActivty.getString(R.string.nev_contacts));
        menuModel.setMenuItemImg(R.drawable.contacts_book_icon);
        sideMenuList.add(menuModel);

        menuModel = new SideMenuModel();
        menuModel.setMenuItemText(currentActivty.getString(R.string.nev_transactions));
        menuModel.setMenuItemImg(R.drawable.transactions_icon);
        sideMenuList.add(menuModel);

        menuModel = new SideMenuModel();
        menuModel.setMenuItemText(currentActivty.getString(R.string.nev_support));
        menuModel.setMenuItemImg(R.drawable.support_icon);
        sideMenuList.add(menuModel);

        menuModel = new SideMenuModel();
        menuModel.setMenuItemText(currentActivty.getString(R.string.nev_about_us));
        menuModel.setMenuItemImg(R.drawable.about_us);
        sideMenuList.add(menuModel);

        return sideMenuList;

    }
}