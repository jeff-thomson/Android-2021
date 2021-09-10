package com.stych.android.start.login;

import android.app.Application;
import android.util.Patterns;

import androidx.annotation.NonNull;

import com.stych.android.Constant;
import com.stych.android.R;


public class LoginModel {
    public String email;
    public String password;
    public String deviceToken;
    public String deviceId;
    public String deviceType = "2";
    public String userType = "2";

    public String isValid(@NonNull Application application) {
        if (email == null || (email = email.trim()).length() == 0) {
            return String.format(application.getString(R.string.field_s_required), application.getString(R.string.email_address));
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return String.format(application.getString(R.string.field_s_invalid), application.getString(R.string.email_address));
        }

        if (password == null || (password = password.trim()).length() == 0) {
            return String.format(application.getString(R.string.field_s_required), application.getString(R.string.password));
        }

        if (password.length() < Constant.PASSWORD_MIN_LEN) {
            return String.format(application.getString(R.string.field_s_short_d), application.getString(R.string.password), Constant.PASSWORD_MIN_LEN);
        }
        return null;
    }

}
