package com.stych.android.dialog;

import android.content.Context;
import android.content.DialogInterface;

import androidx.appcompat.app.AlertDialog;

import com.stych.android.R;


public class OKDialog {
    public static AlertDialog show(Context context, String message, final OnOKClickListener listener) {
        return new AlertDialog.Builder(context)
                .setTitle(R.string.stych)
                .setMessage(message)

                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        if (listener != null) {
                            listener.onOKClick();
                        }
                    }
                })
                .show();
    }

    public interface OnOKClickListener {
        void onOKClick();
    }
}
