package com.example.ismailamrani.comptable.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Mohammed Aouf ZOUAG on 04/06/2016.
 */
public class TypeFaces {
    private static final Map<String, Typeface> cache = new HashMap<>();

    public static Typeface getTypeFace(Context context, String assetPath) {
        synchronized (cache) {
            if (!cache.containsKey(assetPath)) {
                try {
                    Typeface typeFace = Typeface.createFromAsset(
                            context.getAssets(), assetPath);
                    cache.put(assetPath, typeFace);
                } catch (Exception e) {
                    Log.e("TypeFaces", "Typeface not loaded./" + e.toString());
                    return null;
                }
            }
            return cache.get(assetPath);
        }
    }
}
