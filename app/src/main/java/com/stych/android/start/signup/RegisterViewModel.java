package com.stych.android.start.signup;

import android.app.Application;
import android.provider.Settings;
import android.text.TextUtils;
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

import java.io.File;

import javax.inject.Inject;

public class RegisterViewModel extends BaseAndroidViewModel {
    private final Repository repository;
    public boolean isUploadAble;
    protected MutableLiveData<String> _registerData = new MutableLiveData<>();
    public final LiveData<String> registerData = _registerData;
    protected MutableLiveData<Boolean> _validateData = new MutableLiveData<>();
    public final LiveData<Boolean> validateData = _validateData;
    private RegisterModel model;

    @Inject
    public RegisterViewModel(@NonNull Application application, @NonNull Repository repository) {
        super(application);
        model = new RegisterModel();
        this.repository = repository;
    }

    public void setFirstName(String firstName) {
        model.firstName = firstName;
    }

    public void setLastName(String lastName) {
        model.lastName = lastName;
    }

    public void setEmail(String email) {
        model.email = email;
    }

    public void setPassword(String password) {
        model.password = password;
    }

    public void setConfirmPassword(String password) {
        model.confirmPassword = password;
    }

    public void setFile(File file) {
        model.file = file;
    }

    public RegisterModel getModel() {
        return model;
    }

    public void setModel(RegisterModel registerModel) {
        model = registerModel;
    }

    public void setDeviceToken(String token) {
        model.deviceToken = token;
    }

    public void setDeviceId() {
        model.deviceId = Settings.Secure.getString(getApplication().getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    public void newRegister() {
        _loading.setValue(true);
        String error = model.isValid(getApplication());
        if (error == null) {
            if (TextUtils.isEmpty(model.firstName)) {
                model.firstName = model.email.split("@")[0];
            }
            repository.register(model, new Repository.ResponseListener() {
                @Override
                public void onResponse(APIResponse apiResponse) {
                    _loading.setValue(false);
                    if (apiResponse.isSuccessful()) {
                        if (((Token) apiResponse.getData()).error == null) {
                            sendVerificationEmail(model.email);
                            Token token = (Token) apiResponse.getData();
                            token.isLogin = true;
                            token.save(getApplication());
                            LifeFileCache.getInstance().setToken(token);
                            LifeFileCache.getInstance().getLifeFilesFromServer();
                            _registerData.setValue(getApplication().getResources().getString(R.string.register_success));
                        } else {
                            _error.setValue(((Token) apiResponse.getData()).error.message);
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

    private void sendVerificationEmail(String email) {
        repository.sendVerificationEmail(model.email, new Repository.ResponseListener() {
            @Override
            public void onResponse(APIResponse apiResponse) {
                Log.d("RegisterViewModel", "sendVerificationEmail " + apiResponse != null ? apiResponse.toString() : "Failed!");
            }
        });
    }

    public void validate() {
        String error = model.isValid(getApplication());
        if (error == null) {
            _validateData.setValue(true);
        } else {
            _error.setValue(error);
        }
    }
}
