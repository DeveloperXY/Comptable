package com.example.ismailamrani.comptable.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import com.example.ismailamrani.comptable.R;

/**
 * Created by Mohammed Aouf ZOUAG on 27/04/2016.
 */
public class DialogUtil {
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
        AlertDialog alertDialog = buildDialog(context, title, message, actionText, listener);
        alertDialog.show();
    }

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

    public static void showDialog(Context context, final String title,
                                  final String message, final String actionText,
                                  final DialogInterface.OnClickListener listener,
                                  final DialogInterface.OnDismissListener dismissListener) {
        AlertDialog alertDialog = buildDialog(context, title, message, actionText, listener);
        alertDialog.setOnDismissListener(dismissListener);
        alertDialog.show();
    }
}
