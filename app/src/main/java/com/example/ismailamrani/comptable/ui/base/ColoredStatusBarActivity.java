package com.example.ismailamrani.comptable.ui.base;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.WindowManager;
import android.widget.ImageView;

import com.example.ismailamrani.comptable.R;
import com.example.ismailamrani.comptable.customitems.OGActionBar.OGActionBar;
import com.example.ismailamrani.comptable.customitems.OGActionBar.OGActionBarInterface;
import com.example.ismailamrani.comptable.ui.startup.HomeActivity;

import butterknife.Bind;

/**
 * Extending this activity takes care of coloring the status bar & initializing
 * the custom action bar.
 */
public abstract class ColoredStatusBarActivity extends HTTPActivity
        implements OGActionBarInterface {

    /**
     * This flag, if set to 'true', will cause the activity to be finished
     * when the onStop() method is called.
     */
    protected boolean shouldFinish = false;

    @Nullable
    @Bind(R.id.MyActionBar)
    protected OGActionBar mActionBar;

    /**
     * The target view of the shared-element-transition animation that
     * comes before the reveal animation.
     */
    @Nullable
    @Bind(R.id.actionbarImage)
    protected ImageView actionbarImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN |
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        if (android.os.Build.VERSION.SDK_INT >= 21)
            getWindow().setStatusBarColor(Color.parseColor("#3d5469"));
    }

    protected void setupActionBar() {
        mActionBar.setActionBarListener(this);
    }

    @Override
    public void onMenuPressed() {
        Intent intent = new Intent(this, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    public void onAddPressed() {

    }

    /**
     * Signals that the current activity should be finished onStop().
     */
    protected void activityShouldFinish() {
        shouldFinish = true;
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (shouldFinish)
            finish();
    }
}
