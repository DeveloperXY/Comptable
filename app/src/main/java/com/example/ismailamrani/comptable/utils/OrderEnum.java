package com.example.ismailamrani.comptable.utils;

/**
 * Created by Mohammed Aouf ZOUAG on 11/05/2016.
 */
public enum OrderEnum {
    SALE("SALE"), PURCHASE("PURCHASE");

    private final String desc;

    OrderEnum(String desc) {
        this.desc = desc;
    }

    public String getValue() {
        return desc;
    }
}
