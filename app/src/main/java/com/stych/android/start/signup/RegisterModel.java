package com.stych.android.start.signup;

import android.app.Application;
import android.util.Patterns;

import androidx.annotation.NonNull;

import com.stych.android.Constant;
import com.stych.android.R;
import com.stych.android.data.User;

import java.io.File;
import java.io.Serializable;

public class RegisterModel implements Serializable {
    public String firstName;
    public String lastName;
    public String email;
    public String password;
    public String confirmPassword;
    public File file;

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

        String upperCaseChars = "(.*[A-Z].*)";
        if (!password.matches(upperCaseChars)) {
            return String.format("Password must have at least one uppercase character");
        }

        if (confirmPassword == null || (confirmPassword = confirmPassword.trim()).length() == 0) {
            return String.format(application.getString(R.string.field_s_required), application.getString(R.string.confirm_password));
        }

        if (!password.equals(confirmPassword)) {
            return String.format(application.getString(R.string.confirm_password_not_matched));
        }

        return null;
    }

    public User toUser() {
        User user = new User();
        user.firstName = this.firstName;
        user.lastName = this.lastName;
        user.email_address = this.email;
        user.password = this.password;
        return user;
    }
}
