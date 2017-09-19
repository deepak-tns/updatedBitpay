package com.bitpay.bitpay.interfaces;

import android.view.View;

/**
 * Created by Codeslay-03 on 1/31/2017.
 */

public interface RecyclerClickListner {
    void onItemClick(int position, View v);

    void onItemLongClick(int position, View v);
}
