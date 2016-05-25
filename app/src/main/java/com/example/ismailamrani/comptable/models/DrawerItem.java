package com.example.ismailamrani.comptable.models;

/**
 * Created by Mohammed Aouf ZOUAG on 25/05/2016.
 */
public class DrawerItem {
    /**
     * The title of that specific drawer item.
     */
    private String title;
    /**
     * The class of the target activity, that will be launched on click.
     */
    private Class<?> activityClass;
    /**
     * The icon associated with this drawer item.
     */
    private int icon;

    public DrawerItem(String title, Class<?> activityClass, int icon) {
        this.title = title;
        this.activityClass = activityClass;
        this.icon = icon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Class<?> getActivityClass() {
        return activityClass;
    }

    public void setActivityClass(Class<?> activityClass) {
        this.activityClass = activityClass;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }
}
