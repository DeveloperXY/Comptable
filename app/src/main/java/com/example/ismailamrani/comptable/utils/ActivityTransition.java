package com.example.ismailamrani.comptable.utils;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.View;

/**
 * Created by Mohammed Aouf ZOUAG on 23/05/2016.
 */
public class ActivityTransition {
    /**
     * @param context the source activity
     * @param targetActivity the activity to be transitioned to
     * @param sharedView between the two activities
     * @param transitionName name of the shared element transition
     */
    public static void startActivityWithSharedElement(Context context,
                                                      Class<?> targetActivity,
                                                      View sharedView,
                                                      String transitionName) {
        Intent intent = new Intent(context, targetActivity);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP)
            context.startActivity(intent);
        else {
            ActivityOptions transitionActivityOptions =
                    ActivityOptions.makeSceneTransitionAnimation(
                            (Activity) context, sharedView, transitionName);
            context.startActivity(intent, transitionActivityOptions.toBundle());
        }
    }

    public static void startActivityWithSharedElement(Context context,
                                                      Intent intent,
                                                      View sharedView,
                                                      String transitionName) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP)
            context.startActivity(intent);
        else {
            ActivityOptions transitionActivityOptions =
                    ActivityOptions.makeSceneTransitionAnimation(
                            (Activity) context, sharedView, transitionName);
            context.startActivity(intent, transitionActivityOptions.toBundle());
        }
    }
}
