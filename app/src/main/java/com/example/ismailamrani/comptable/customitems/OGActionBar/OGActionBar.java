package com.example.ismailamrani.comptable.customitems.OGActionBar;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.example.ismailamrani.comptable.R;
import com.example.ismailamrani.comptable.customitems.CustomTextView;

/**
 * Created by Ismail Amrani on 23/03/2016.
 */
public class OGActionBar extends RelativeLayout {

    RelativeLayout menu, add;
    CustomTextView title;

    OGActionBarInterface listener;

    public OGActionBar(Context context, AttributeSet attrs) {
        super(context, attrs);

        setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT
                , ViewGroup.LayoutParams.MATCH_PARENT));

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.action_bar, null);

        menu = (RelativeLayout) layout.findViewById(R.id.Menu);
        add = (RelativeLayout) layout.findViewById(R.id.Add);
        title = (CustomTextView) layout.findViewById(R.id.Title);

        menu.setOnClickListener(v -> {
            if (listener != null)
                getActionBarListener().onMenuPressed();
        });

        add.setOnClickListener(v -> {
            if (listener != null)
                getActionBarListener().onAddPressed();
        });

        addView(layout);
    }

    public void setTitle(String title) {
        this.title.SetText(title);
    }

    public OGActionBar setActionBarListener(OGActionBarInterface listener) {
        this.listener = listener;
        return this;
    }

    public OGActionBarInterface getActionBarListener() {
        return this.listener;
    }

    public void disableAddButton() {
        add.setVisibility(INVISIBLE);
    }

    public void disableMenu() {
        menu.setVisibility(INVISIBLE);
    }
}
