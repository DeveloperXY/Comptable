package com.example.ismailamrani.comptable.ui.base;

import android.animation.Animator;
import android.annotation.TargetApi;
import android.os.Build;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AccelerateInterpolator;

import com.example.ismailamrani.comptable.R;

/**
 * Created by Mohammed Aouf ZOUAG on 23/05/2016.
 *
 * This activity sets the ground for a reveal animation that targets
 * the action bar, on startup.
 */
public abstract class AnimatedActivity extends WithDrawerActivity {

    /**
     * This flag indicates whether the activity is concerned with performing the
     * header shared element transition or not.
     */
    public static final String TURN_OFF_HEADER_ANIMATION = "turnOffHeaderAnimation";
    public static final String HEADER_IMAGE_RES = "imageRes";

    /**
     * Setup the action bar for the reveal animation.
     */
    @Override
    protected void setupActionBar() {
        super.setupActionBar();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // Check if the activity wants to turn off the header animation
            if (!getIntent().getBooleanExtra(TURN_OFF_HEADER_ANIMATION, false)) {
                mActionBar.setVisibility(View.INVISIBLE);
                actionbarImage.setImageResource(getIntent().getIntExtra(HEADER_IMAGE_RES, -1));
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    protected void setupRevealTransition() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Transition transition = TransitionInflater.from(this)
                    .inflateTransition(R.transition.changebounds_with_arcmotion);
            getWindow().setSharedElementEnterTransition(transition);
            transition.addListener(new Transition.TransitionListener() {
                @Override
                public void onTransitionStart(Transition transition) {
                }

                @Override
                public void onTransitionEnd(Transition transition) {
                    // Removing listener here is very important because shared element transition
                    // is executed again backwards on exit.
                    // If we don't remove the listener this code will be triggered again.
                    transition.removeListener(this);
                    animateRevealShow(mActionBar);
                }

                @Override
                public void onTransitionCancel(Transition transition) {
                }

                @Override
                public void onTransitionPause(Transition transition) {
                }

                @Override
                public void onTransitionResume(Transition transition) {
                }
            });
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void animateRevealShow(View viewRoot) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            int cx = (viewRoot.getLeft() + viewRoot.getRight()) / 2;
            int cy = (viewRoot.getTop() + viewRoot.getBottom()) / 2;
            int finalRadius = Math.max(viewRoot.getWidth(), viewRoot.getHeight());

            Animator anim = ViewAnimationUtils.createCircularReveal(viewRoot, cx, cy, 0, finalRadius);
            viewRoot.setVisibility(View.VISIBLE);
            anim.setDuration(1000);
            anim.setInterpolator(new AccelerateInterpolator());
            anim.start();
        }
    }
}
