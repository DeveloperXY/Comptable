package com.example.ismailamrani.comptable.utils.ui;

/**
 * Created by Mohammed Aouf ZOUAG on 25/05/2016.
 *
 * Each activity with a DrawerLayout needs to implement this interface,
 * since it's the way used to determine the order of the drawer items within it.
 */
public interface DrawerOrder {
    ActivityOrder getActivity();

    enum ActivityOrder {
        PRODUCTS, STOCK, SALES, PURCHASES, CHARGES, CLIENTS, SUPPLIERS, COMPTABILITE, NONE
    }
}
