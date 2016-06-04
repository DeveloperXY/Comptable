package com.example.ismailamrani.comptable.customitems.OGActionBar;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.ismailamrani.comptable.R;
import com.example.ismailamrani.comptable.customitems.CustomTextView;

/**
 * Created by Ismail Amrani on 23/03/2016.
 */
public class OGActionBar extends RelativeLayout {

    RelativeLayout menu, add, search;
    CustomTextView title;
    LinearLayout mainActionLayout;

    OGActionBarInterface listener;
    SearchListener searchListener;

    public OGActionBar(Context context, AttributeSet attrs) {
        super(context, attrs);

        setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT
                , ViewGroup.LayoutParams.MATCH_PARENT));

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.action_bar, null);

        menu = (RelativeLayout) layout.findViewById(R.id.Menu);
        add = (RelativeLayout) layout.findViewById(R.id.Add);
        search = (RelativeLayout) layout.findViewById(R.id.Search);
        title = (CustomTextView) layout.findViewById(R.id.Title);
        mainActionLayout = (LinearLayout) layout.findViewById(R.id.mainActionLayout);

        menu.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null)
                    getActionBarListener().onMenuPressed();
            }
        });

        add.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null)
                    getActionBarListener().onAddPressed();
            }
        });

        search.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (searchListener != null)
                    searchListener.onSearchPressed();
            }
        });

        addView(layout);
    }

    /**
     * Specifies the background color of this custom action bar.
     *
     * @param color
     */
    public void setBackgroundColor(int color) {
        mainActionLayout.setBackgroundColor(color);
    }

    public void setBackground(int drawable) {
        mainActionLayout.setBackgroundDrawable(getResources().getDrawable(drawable));
    }

    public void setTitle(String title) {
        this.title.SetText(title);
    }

    public OGActionBar setActionBarListener(OGActionBarInterface listener) {
        this.listener = listener;
        return this;
    }

    public OGActionBarInterface getActionBarListener() {
        return listener;
    }

    public void setSearchListener(SearchListener listener) {
        searchListener = listener;
    }

    public void disableAddButton() {
        add.setVisibility(INVISIBLE);
    }

    public void disableMenu() {
        menu.setVisibility(INVISIBLE);
    }

    public void isSearchable(boolean state) {
        search.setVisibility(state ? VISIBLE : GONE);
    }
}
