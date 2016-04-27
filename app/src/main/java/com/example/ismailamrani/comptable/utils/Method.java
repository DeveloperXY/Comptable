package com.example.ismailamrani.comptable.utils;

/**
 * Created by Mohammed Aouf ZOUAG on 27/04/2016.
 */
public enum Method {
    POST("POST"), GET("GET");

    private String method = "";

    Method(String s) {
        method = s;
    }

    @Override
    public String toString() {
        return method;
    }
}