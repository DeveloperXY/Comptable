package com.example.ismailamrani.comptable.ui.base;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;

import com.example.ismailamrani.comptable.R;

import butterknife.Bind;

/**
 * Created by Mohammed Aouf ZOUAG on 03/06/2016.
 */
public abstract class AnimatedWithBackArrowActivity extends WithBackArrowActivity {

    @Bind(R.id.mainRootView)
    protected ViewGroup mainRootView;

    private Interpolator interpolator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            interpolator = AnimationUtils.loadInterpolator(
                    this, android.R.interpolator.linear_out_slow_in);
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    protected void setupRevealTransition() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mainRootView.post(() -> {
                int cx = (mainRootView.getLeft() + mainRootView.getRight()) / 2;
                int cy = (mainRootView.getTop() + mainRootView.getBottom()) / 2;
                int finalRadius = Math.max(mainRootView.getWidth(), mainRootView.getHeight());

                Animator anim = ViewAnimationUtils.createCircularReveal(
                        mainRootView, cx, cy, 0, finalRadius);
                mainRootView.setBackground(getResources().getDrawable(R.mipmap.ic_bg_add));
                anim.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        animateViewsIn();
                    }
                });
                anim.start();
            });
        }
    }

    private void animateViewsIn() {
        for (int i = 0; i < mainRootView.getChildCount(); i++) {
            View child = mainRootView.getChildAt(i);
            child.animate()
                    .setStartDelay(50 + i * 50)
                    .setInterpolator(interpolator)
                    .alpha(1)
                    .scaleX(1)
                    .scaleY(1);
        }
    }
}
