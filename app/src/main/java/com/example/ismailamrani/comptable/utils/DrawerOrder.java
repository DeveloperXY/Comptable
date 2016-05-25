package com.example.ismailamrani.comptable.utils;

/**
 * Created by Mohammed Aouf ZOUAG on 25/05/2016.
 */
public interface DrawerOrder {
    ActivityOrder getActivity();

    enum ActivityOrder {
        PRODUCTS, STOCK, SALES, PURCHASES, CHARGES, CLIENTS, SUPPLIERS, NONE
    }
}
