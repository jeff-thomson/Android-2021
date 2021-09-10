package com.stych.android.home.transferlifefile;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.stych.android.BaseAndroidViewModel;
import com.stych.android.BuildConfig;
import com.stych.android.LifeFileCache;
import com.stych.android.data.Token;
import com.stych.android.data.UserData;
import com.stych.android.data.remote.APIResponse;
import com.stych.android.data.remote.Repository;
import com.stych.android.data.remote.response.BasicResponse;
import com.stych.android.data.remote.response.CreateVideoUploadResponse;
import com.stych.android.data.remote.response.GeneralData;
import com.stych.android.data.remote.response.GetLifeFileVideoResponse;
import com.stych.android.data.remote.response.GetLifeFileVideosResponse;
import com.stych.android.data.remote.response.LifeFileResponse;
import com.stych.android.data.remote.response.LifeFileVideo;
import com.stych.android.data.remote.response.UserVideoData;
import com.stych.android.home.MainModel;
import com.stych.android.util.VideoMetadataRetrieverUtil;

import java.util.Calendar;
import java.util.List;

import javax.inject.Inject;

public class TransferLifeFileViewModel extends BaseAndroidViewModel {
    private final Repository repository;
    private final MainModel model;
    protected MutableLiveData<String> _transferFileData = new MutableLiveData<>();
    public final LiveData<String> transferFileData = _transferFileData;

    private Token token;

    @Inject
    public TransferLifeFileViewModel(@NonNull Application application, @NonNull Repository repository) {
        super(application);
        this.repository = repository;
        this.token = Token.retrieve(application);
        this.model = new MainModel();
    }

    public void setEmail(String email) {
        this.model.email = email;
    }

    public void transferLifeFile(String lifeId) {
        _loading.setValue(true);
        String error = model.isValidEmailForm(getApplication());
        if (error == null) {
            repository.transferLifeFile(token, model.email, lifeId, new Repository.ResponseListener<BasicResponse>() {
                @Override
                public void onResponse(APIResponse<BasicResponse> apiResponse) {
                    _loading.setValue(false);
                    if (apiResponse.isSuccessful()) {
                        if (((BasicResponse) apiResponse.getData()).error == null) {
                            BasicResponse logoutResponse = (BasicResponse) apiResponse.getData();
                            _transferFileData.setValue("Life file transferred successfully.");
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
