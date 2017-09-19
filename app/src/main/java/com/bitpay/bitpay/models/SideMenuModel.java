package com.bitpay.bitpay.models;

/**
 * Created by Codeslay-03 on 4/6/2017.
 */

public class SideMenuModel {

    private String menuItemText;
    private int menuItemImg;
    private boolean isSelected;

    public String getMenuItemText() {
        return menuItemText;
    }

    public void setMenuItemText(String menuItemText) {
        this.menuItemText = menuItemText;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public int getMenuItemImg() {
        return menuItemImg;
    }

    public void setMenuItemImg(int menuItemImg) {
        this.menuItemImg = menuItemImg;
    }


}
