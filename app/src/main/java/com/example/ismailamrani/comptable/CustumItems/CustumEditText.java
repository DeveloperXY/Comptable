package com.example.ismailamrani.comptable.CustumItems;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.EditText;
import android.widget.TextView;

import com.example.ismailamrani.comptable.LocalData.ScreenSize;
import com.example.ismailamrani.comptable.Models.ScreenSizeModel;
import com.example.ismailamrani.comptable.R;

/**
 * Created by Ismail Amrani on 25/11/2015.
 */
public class CustumEditText extends EditText {

    int height, size;
    String Font, Arabic, Frensh ;

    public CustumEditText(Context context, AttributeSet attrs) {
        super(context, attrs);


        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.CostumFont,
                0, 0);

        try {
            size = a.getInteger(R.styleable.CostumFont_Size, 0);
            Font = a.getString(R.styleable.CostumFont_Font);
            Arabic = a.getString(R.styleable.CostumFont_Arabic);
            Frensh = a.getString(R.styleable.CostumFont_Frensh);
        } finally {
            a.recycle();
        }


        ScreenSize Size = new ScreenSize(context);
        ScreenSizeModel HW = Size.GetSize(1);
        height = HW.getHEIGHT();

        Typeface typeFace=Typeface.createFromAsset(context.getAssets(), "fonts/" + Font);
        setTypeface(typeFace);

        setTextSize(TypedValue.COMPLEX_UNIT_PX, (size * height) / 492);

        setText(Frensh);

    }

    public void SetText(String Text){
        setText(Text);
    }
}
