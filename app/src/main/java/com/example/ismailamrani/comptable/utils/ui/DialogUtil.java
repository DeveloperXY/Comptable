package com.example.ismailamrani.comptable.utils.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.Window;

import com.example.ismailamrani.comptable.R;

/**
 * Created by Mohammed Aouf ZOUAG on 27/04/2016.
 */
public class DialogUtil {

    private static AlertDialog buildDialog(Context context, String title, String message, final String actionText,
                                           final DialogInterface.OnClickListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(
                context, R.style.AppCompatAlertDialogStyle);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton(actionText, listener);

        AlertDialog alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(true);

        return alertDialog;
    }

    private static AlertDialog buildDoubleChoiceDialog(
            Context context, String title, String message,
            final String actionText1,
            final DialogInterface.OnClickListener listener1,
            final String actionText2,
            final DialogInterface.OnClickListener listener2) {
        AlertDialog.Builder builder = new AlertDialog.Builder(
                context, R.style.AppCompatAlertDialogStyle);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setNegativeButton(actionText1, listener1);
        builder.setPositiveButton(actionText2, listener2);

        AlertDialog alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(true);

        return alertDialog;
    }

    /**
     * Shows a custom dialog.
     *
     * @param title      of the dialog
     * @param message    of the dialog
     * @param actionText text of the dialog's action button
     * @param listener   on the action button
     */
    public static void showDialog(Context context, final String title,
                                  final String message, final String actionText,
                                  final DialogInterface.OnClickListener listener) {
        showDialog(context, title, message, actionText, listener, null);
    }

    public static void showDialog(Context context, final String title,
                                  final String message, final String actionText,
                                  final DialogInterface.OnClickListener listener,
                                  final DialogInterface.OnDismissListener dismissListener) {
        AlertDialog alertDialog = buildDialog(context, title, message, actionText, listener);
        alertDialog.setOnDismissListener(dismissListener);
        alertDialog.show();
    }

    public static void showMutliDialog(Context context, final String title,
                                       final String message,
                                       final String actionText1,
                                       final DialogInterface.OnClickListener listener1,
                                       final String actionText2,
                                       final DialogInterface.OnClickListener listener2) {
        showMutliDialog(context, title, message, actionText1,
                listener1, actionText2, listener2, false);
    }

    public static void showMutliDialog(Context context,
                                       final String message,
                                       final String actionText1,
                                       final DialogInterface.OnClickListener listener1,
                                       final String actionText2,
                                       final DialogInterface.OnClickListener listener2) {
        showMutliDialog(context, "", message, actionText1,
                listener1, actionText2, listener2, true);
    }

    public static void showMutliDialog(Context context, final String title,
                                       final String message,
                                       final String actionText1,
                                       final DialogInterface.OnClickListener listener1,
                                       final String actionText2,
                                       final DialogInterface.OnClickListener listener2,
                                       boolean noTitle) {
        AlertDialog alertDialog = buildDoubleChoiceDialog(
                context, title, message, actionText1, listener1, actionText2, listener2);

        if (noTitle)
            alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        alertDialog.show();
    }
}
