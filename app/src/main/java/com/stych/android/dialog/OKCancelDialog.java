package com.stych.android.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;

import androidx.appcompat.app.AlertDialog;

import com.stych.android.R;


public class OKCancelDialog {
    public static AlertDialog show(Context context, String message, String okText, final OnOKClickListener listener) {
        if (TextUtils.isEmpty(okText)) {
            okText = context.getString(R.string.ok);
        }
        return new AlertDialog.Builder(context)
                .setTitle(R.string.stych)
                .setMessage(message)

                .setPositiveButton(okText, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        if (listener != null) {
                            listener.onOKClick();
                        }
                    }
                })

                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })

                .show();
    }

    public static AlertDialog show(Context context, String message, String okText, String removeText, final OnOKCancelClickListener listener) {
        if (TextUtils.isEmpty(okText)) {
            okText = context.getString(R.string.ok);
        }
        return new AlertDialog.Builder(context)
                .setTitle(R.string.app_name)
                .setMessage(message)

                .setPositiveButton(okText, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        if (listener != null) {
                            listener.onOKClick();
                        }
                    }
                })

                .setNegativeButton(removeText, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        if (listener != null) {
                            listener.onCancelClick();
                        }
                    }
                })

                .show();
    }

    public interface OnOKClickListener {
        void onOKClick();
    }

    public interface OnOKCancelClickListener {
        void onOKClick();

        void onCancelClick();
    }
}
