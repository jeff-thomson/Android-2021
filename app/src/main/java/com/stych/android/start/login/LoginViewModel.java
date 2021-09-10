package com.stych.android.start.login;

import android.app.Application;
import android.provider.Settings;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.stych.android.BaseAndroidViewModel;
import com.stych.android.LifeFileCache;
import com.stych.android.R;
import com.stych.android.data.Token;
import com.stych.android.data.remote.APIResponse;
import com.stych.android.data.remote.Repository;

import javax.inject.Inject;

public class LoginViewModel extends BaseAndroidViewModel {

    private final LoginModel model;
    private final Repository repository;
    protected MutableLiveData<String> _loginData = new MutableLiveData<>();
    public final LiveData<String> loginData = _loginData;

    @Inject
    public LoginViewModel(@NonNull Application application, @NonNull Repository repository) {
        super(application);
        model = new LoginModel();
        this.repository = repository;
    }

    public void setEmail(String email) {
        model.email = email;
    }

    public void setPassword(String password) {
        model.password = password;
    }

    public void setDeviceToken(String token) {
        model.deviceToken = token;
    }

    public void setDeviceId() {
        model.deviceId = Settings.Secure.getString(getApplication().getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    public void login() {
        _loading.setValue(true);
        String error = model.isValid(getApplication());
        if (error == null) {
            repository.login(model, new Repository.ResponseListener() {
                @Override
                public void onResponse(APIResponse apiResponse) {
                    _loading.setValue(false);
                    if (apiResponse.isSuccessful()) {
                        if (((Token) apiResponse.getData()).error == null) {
                            Token token = (Token) apiResponse.getData();
                            token.isLogin = true;
                            token.save(getApplication());
                            LifeFileCache.getInstance().setToken(token);
                            LifeFileCache.getInstance().getLifeFilesFromServer();
                            _loginData.setValue(getApplication().getResources().getString(R.string.login_success));
                        } else {
                            String error = ((Token) apiResponse.getData()).error.message;
                            _error.setValue(error);
                            if(error != null && error.endsWith("not been verified.")) {
                                sendVerificationEmail();
                            }
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

    private void sendVerificationEmail() {
        repository.sendVerificationEmail(model.email, new Repository.ResponseListener() {
            @Override
            public void onResponse(APIResponse apiResponse) {
                Log.d("LoginViewModel", "sendVerificationEmail " + apiResponse != null ? apiResponse.toString() : "Failed!");
            }
        });
    }

}
