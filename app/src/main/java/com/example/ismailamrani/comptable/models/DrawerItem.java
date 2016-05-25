package com.example.ismailamrani.comptable.models;

import android.content.Intent;

/**
 * Created by Mohammed Aouf ZOUAG on 25/05/2016.
 */
public class DrawerItem {
    /**
     * The title of that specific drawer item.
     */
    private String title;
    /**
     * The intent corresponding to the target activity, that will be launched on click.
     */
    private Intent intent;
    /**
     * The icon associated with this drawer item.
     */
    private int icon;

    public DrawerItem(String title, Intent intent, int icon) {
        this.title = title;
        this.intent = intent;
        this.icon = icon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Intent getIntent() {
        return intent;
    }

    public void setIntent(Intent intent) {
        this.intent = intent;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }
}
