package com.example.ismailamrani.comptable.customitems;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.WindowManager;

/**
 * Created by Ismail Amrani on 10/02/2016.
 */
public class ColorStatutBar {
    public void ColorStatutBar(Context context) {
        ((Activity) context).getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        if (android.os.Build.VERSION.SDK_INT >= 21) {
            ((Activity) context).getWindow().setStatusBarColor(Color.parseColor("#3d5469"));
        }
    }
}
