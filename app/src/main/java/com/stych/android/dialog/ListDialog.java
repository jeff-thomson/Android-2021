package com.stych.android.dialog;

import android.content.Context;
import android.content.DialogInterface;

import androidx.appcompat.app.AlertDialog;

import com.stych.android.R;


public class ListDialog {
    public static AlertDialog show(Context context, int titleResId, final CharSequence[] options, final OnItemClickListener listener) {
        return new AlertDialog.Builder(context)
                .setTitle(titleResId)
                .setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (listener != null) {
                            listener.onItemClick(which, options[which]);
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

    public interface OnItemClickListener {
        void onItemClick(int position, CharSequence item);
    }
}
