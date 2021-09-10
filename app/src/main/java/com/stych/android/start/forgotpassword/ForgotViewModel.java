package com.stych.android.start.forgotpassword;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.stych.android.BaseAndroidViewModel;
import com.stych.android.data.remote.APIResponse;
import com.stych.android.data.remote.Repository;
import com.stych.android.data.remote.response.BasicResponse;

import javax.inject.Inject;

public class ForgotViewModel extends BaseAndroidViewModel {

    private final ForgotModel model;
    private final Repository repository;
    protected MutableLiveData<String> _emailCode = new MutableLiveData<>();
    public final LiveData<String> emailCode = _emailCode;
    protected MutableLiveData<String> _verifyCode = new MutableLiveData<>();
    public final LiveData<String> verifyCode = _verifyCode;

    @Inject
    public ForgotViewModel(@NonNull Application application, @NonNull Repository repository) {
        super(application);
        model = new ForgotModel();
        this.repository = repository;
    }

    public void setEmail(String email) {
        model.email = email;
    }

    public void setToken(String token) {
        model.passwordResetToken = token;
    }

    public void setPassword(String password) {
        model.password = password;
    }

    public void setPasswordConfirm(String passwordConfirm) {
        model.confirmPassword = passwordConfirm;
    }


    public void getEmailVerificationCodePassword() {
        _loading.setValue(true);
        String error = model.isValid(getApplication());
        if (error == null) {
            repository.emailVerificationCode(model.email, new Repository.ResponseListener() {
                @Override
                public void onResponse(APIResponse apiResponse) {
                    _loading.setValue(false);
                    if (apiResponse.isSuccessful()) {
                        if (((BasicResponse) apiResponse.getData()).error == null) {
                            BasicResponse logoutResponse = (BasicResponse) apiResponse.getData();
                            _emailCode.setValue(logoutResponse.message);
                        } else {
                            _error.setValue(((BasicResponse) apiResponse.getData()).error.message);
                        }

                    } else {
                        _error.setValue(apiResponse.getMessage());
                    }
                }
            });
        } else {
            _loading.setValue(false);
            _error.setValue(error);
        }
    }

    public void sendTokenPassword() {
        _loading.setValue(true);
        String error = model.isValidChangePasswordForm(getApplication());
        if (error == null) {
            repository.passwordReset(model, new Repository.ResponseListener() {
                @Override
                public void onResponse(APIResponse apiResponse) {
                    _loading.setValue(false);
                    if (apiResponse.isSuccessful()) {
                        if (((BasicResponse) apiResponse.getData()).error == null) {
                            BasicResponse basicResponse = (BasicResponse) apiResponse.getData();
                            _verifyCode.setValue("Password reset successfully completed.");
                        } else {
                            _error.setValue(((BasicResponse) apiResponse.getData()).error.message);
                        }

                    } else {
                        _error.setValue(apiResponse.getMessage());
                    }
                }
            });
        } else {
            _loading.setValue(false);
            _error.setValue(error);
        }
    }
}
