package com.stych.android.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.stych.android.LifeFileCache;
import com.stych.android.R;

public class DialogCustomAddEditLifeFile {
    public static void show(Context context, String message, String okS, String cancelS, OnOkButton onOkButton) {
        final AlertDialog.Builder alert = new AlertDialog.Builder(context);
        View mView = LayoutInflater.from(context).inflate(R.layout.custom_dialog_add_edit, null);
        final EditText txt_inputText = (EditText) mView.findViewById(R.id.txt_input);
        if (okS != null && okS.equalsIgnoreCase("Rename") && LifeFileCache.getInstance().getCurrent() != null) {
            txt_inputText.setText(LifeFileCache.getInstance().getCurrent().name);
        }
        TextView messageTv = mView.findViewById(R.id.messageTv);
        messageTv.setText(message);
        Button btn_cancel = (Button) mView.findViewById(R.id.btn_cancel);
        Button btn_okay = (Button) mView.findViewById(R.id.btn_okay);
        btn_cancel.setText(cancelS);
        btn_okay.setText(okS);
        alert.setView(mView);
        final AlertDialog alertDialog = alert.create();
        alertDialog.setCanceledOnTouchOutside(false);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        btn_okay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txt_inputText.getText().toString().isEmpty()) {
                    Toast.makeText(context, context.getResources().getString(R.string.provide_name), Toast.LENGTH_LONG).show();
                } else {
                    onOkButton.onCreateRename(txt_inputText.getText().toString());
                    alertDialog.dismiss();
                }
            }
        });
        alertDialog.show();
    }

    public interface OnOkButton {
        void onCreateRename(String name);
    }
}
