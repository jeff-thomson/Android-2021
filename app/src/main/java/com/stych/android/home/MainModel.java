package com.stych.android.home;

import android.app.Application;
import android.util.Patterns;

import androidx.annotation.NonNull;

import com.stych.android.R;

public class MainModel {
    public String email;

    public String isValidEmailForm(@NonNull Application application) {
        if (email == null || (email = email.trim()).length() == 0) {
            return String.format(application.getString(R.string.field_s_required), application.getString(R.string.email_address));
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return String.format(application.getString(R.string.field_s_invalid), application.getString(R.string.email_address));
        }
        return null;
    }
}
