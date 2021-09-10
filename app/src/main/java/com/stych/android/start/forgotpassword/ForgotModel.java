package com.stych.android.start.forgotpassword;

import android.app.Application;
import android.util.Patterns;

import androidx.annotation.NonNull;

import com.stych.android.Constant;
import com.stych.android.R;


public class ForgotModel {
    public String email;
    public String password;
    public String confirmPassword;
    public String passwordResetToken;

    public String isValid(@NonNull Application application) {
        if (email == null || (email = email.trim()).length() == 0) {
            return String.format(application.getString(R.string.field_s_required), application.getString(R.string.email_address));
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return String.format(application.getString(R.string.field_s_invalid), application.getString(R.string.email_address));
        }

        return null;
    }

    public String isValidChangePasswordForm(@NonNull Application application) {

        if (passwordResetToken == null || (passwordResetToken.trim()).length() == 0) {
            return String.format(application.getString(R.string.field_s_required), application.getString(R.string.code));
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

}
