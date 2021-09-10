package com.stych.android.profile;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.stych.android.BaseAndroidViewModel;
import com.stych.android.Constant;
import com.stych.android.LifeFileCache;
import com.stych.android.data.Token;
import com.stych.android.data.remote.APIResponse;
import com.stych.android.data.remote.Repository;
import com.stych.android.data.remote.response.BasicResponse;
import com.stych.android.data.request.PurchaseData;

import javax.inject.Inject;

public class ProfileViewModel extends BaseAndroidViewModel {

    private final Repository repository;
    private final ProfileModel model;
    protected MutableLiveData<String> _logoutData = new MutableLiveData<>();
    public final LiveData<String> logoutData = _logoutData;
    protected MutableLiveData<String> _changePasswordData = new MutableLiveData<>();
    public final LiveData<String> changePasswordData = _changePasswordData;
    protected MutableLiveData<String> _changeEmailData = new MutableLiveData<>();
    public final LiveData<String> changeEmailData = _changeEmailData;
    protected MutableLiveData<String> _updatePurchase = new MutableLiveData<>();
    public final LiveData<String> updatePurchase = _updatePurchase;

    @Inject
    public ProfileViewModel(@NonNull Application application, @NonNull Repository repository) {
        super(application);
        this.repository = repository;
        this.model = new ProfileModel();
    }

    public void setOldPassword(String value) {
        model.oldPassword = value;
    }

    public void setPassword(String value) {
        model.password = value;
    }

    public void setConfirmPassword(String value) {
        model.confirmPassword = value;
    }

    public void setEmail(String value) {
        model.email = value;
    }

    public void logout() {
        _loading.setValue(true);
        repository.logout(Token.retrieve(getApplication()), new Repository.ResponseListener() {
            @Override
            public void onResponse(APIResponse apiResponse) {
                _loading.setValue(false);
                if (apiResponse.isSuccessful()) {
                    if (((BasicResponse) apiResponse.getData()).error == null) {
                        BasicResponse logoutResponse = (BasicResponse) apiResponse.getData();
                        //_logoutData.setValue(logoutResponse.message);
                    } else {
                        _error.setValue(((BasicResponse) apiResponse.getData()).error.message);
                    }

                } else {
                    _error.setValue(apiResponse.getMessage());
                }
                LifeFileCache.getInstance().clearLifeFileData();
                _logoutData.setValue("Logged out successfully!");
            }
        });

    }

    public void changePassword() {
        _loading.setValue(true);
        String error = model.isValidChangePasswordForm(getApplication());
        if (error == null) {
            repository.changePassword(Token.retrieve(getApplication()), model.oldPassword, model.password, new Repository.ResponseListener() {
                @Override
                public void onResponse(APIResponse apiResponse) {
                    _loading.setValue(false);
                    if (apiResponse.isSuccessful()) {
                        if (((BasicResponse) apiResponse.getData()).error == null) {
                            BasicResponse logoutResponse = (BasicResponse) apiResponse.getData();
                            _changePasswordData.setValue(logoutResponse.message);
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

    public void changeEmail() {
        _loading.setValue(true);
        String error = model.isValidEmailForm(getApplication());
        if (error == null) {
            repository.changeEmail(Token.retrieve(getApplication()), model.email, new Repository.ResponseListener() {
                @Override
                public void onResponse(APIResponse apiResponse) {
                    _loading.setValue(false);
                    if (apiResponse.isSuccessful()) {
                        if (((BasicResponse) apiResponse.getData()).error == null) {
                            BasicResponse logoutResponse = (BasicResponse) apiResponse.getData();
                            Token token = (Token) Token.retrieve(getApplication());
                            token.user.email_address = model.email;
                            token.save(getApplication());
                            _changeEmailData.setValue(logoutResponse.message);
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

    public void updatePurchaseData(String productId) {
        PurchaseData data;
        if(Constant.UNLIMITED_TIRE_PRODUCT_ID.equalsIgnoreCase(productId)) {
            data = PurchaseData.newUnlimitedPurchase("1.99");
        } else if(Constant.PAID_TIRE_PRODUCT_ID.equalsIgnoreCase(productId)) {
            data = PurchaseData.newPaidPurchase("0.99");
        } else {
            data = PurchaseData.newFreePurchase();
        }

        _loading.setValue(true);
        repository.setPurchaseData(Token.retrieve(getApplication()), data, new Repository.ResponseListener() {
            @Override
            public void onResponse(APIResponse apiResponse) {
                _loading.setValue(false);
                if (apiResponse.isSuccessful()) {
                    if (((BasicResponse) apiResponse.getData()).error == null) {
                        _updatePurchase.setValue("Purchase record updated successfully.");
                    } else {
                        _error.setValue(((BasicResponse) apiResponse.getData()).error.message);
                    }
                } else {
                    _error.setValue(apiResponse.getMessage());
                }
            }
        });
    }
}
