package com.example.ismailamrani.comptable.adapters.spinners;

/**
 * Created by Mohammed Aouf ZOUAG on 03/05/2016.
 */
public class Item {

    private int mDrawableRes;

    private String mTitle;

    public Item(int drawable, String title) {
        mDrawableRes = drawable;
        mTitle = title;
    }

    public int getDrawableResource() {
        return mDrawableRes;
    }

    public String getTitle() {
        return mTitle;
    }

}