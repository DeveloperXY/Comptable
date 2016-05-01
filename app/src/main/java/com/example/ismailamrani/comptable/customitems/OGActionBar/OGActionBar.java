package com.example.ismailamrani.comptable.customitems.OGActionBar;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.example.ismailamrani.comptable.R;
import com.example.ismailamrani.comptable.customitems.CustumTextView;

/**
 * Created by Ismail Amrani on 23/03/2016.
 */
public class OGActionBar extends RelativeLayout {

    RelativeLayout Menu, Add;
    CustumTextView Title;

    OGActionBarInterface Listener;

    public OGActionBar(Context context, AttributeSet attrs) {
        super(context, attrs);

        setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT
                , ViewGroup.LayoutParams.MATCH_PARENT));

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View Layout = inflater.inflate(R.layout.action_bar, null);

        Menu = (RelativeLayout) Layout.findViewById(R.id.Menu);
        Add = (RelativeLayout) Layout.findViewById(R.id.Add);
        Title = (CustumTextView) Layout.findViewById(R.id.Title);

        Menu.setOnClickListener(v -> {
            if (Listener != null) {
                getActionBarListener().onMenuPressed();
            }
        });

        Add.setOnClickListener(v -> {
            if (Listener != null) {
                getActionBarListener().onAddPressed();
            }
        });

        addView(Layout);
    }

    public void setTitle(String title) {
        Title.SetText(title);
    }

    public OGActionBar setActionBarListener(OGActionBarInterface Listener) {
        this.Listener = Listener;
        return this;
    }

    public OGActionBarInterface getActionBarListener() {
        return this.Listener;
    }

    public void AddDisable() {
        Add.setVisibility(INVISIBLE);
    }

}
