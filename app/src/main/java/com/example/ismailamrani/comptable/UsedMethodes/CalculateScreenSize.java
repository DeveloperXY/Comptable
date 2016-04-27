package com.example.ismailamrani.comptable.UsedMethodes;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.view.Display;

import com.example.ismailamrani.comptable.LocalData.ScreenSize;


/**
 * Created by ismai on 15/10/2015.
 */
public class CalculateScreenSize {

    public void CalculateScreenSize(Context context){

        Display display = ((Activity) context).getWindowManager().getDefaultDisplay();
        Point S = new Point();
        display.getSize(S);

        ScreenSize size = new ScreenSize(context);
        if( size.getCount() == 0 ){
            size.AddSize(S.x, S.y-getStatutBarSize(S.y, S.x));
        }

    }

    public int getStatutBarSize(int height, int widht){

        if(height <= 320){
            return 20;
        }
        else if (320 < height && height <= 480){
            return 25;
        }
        else if (320 < height && height <= 480){
            return 25;
        }
        else if (480 < height && height <= 800){
            return 25;
        }
        else if (800 < height && height <= 1280){
            if(widht > 720){
                return 56;
            }else{
                return 50;
            }
        }
        else if (1280 < height && height <= 1920){
            return 75;
        }
        else{
            return 50;
        }
    }

}
