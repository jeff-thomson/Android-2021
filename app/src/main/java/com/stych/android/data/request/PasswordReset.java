package com.stych.android.data.request;

public class PasswordReset {
    public String password;

    public PasswordReset(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "PasswordReset{" +
                "password='" + password + '\'' +
                '}';
    }
}
