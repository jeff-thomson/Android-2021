package com.stych.android.start.thankyouforsignup;

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

public class ThankYouViewModel extends BaseAndroidViewModel {
    private final Repository repository;
    protected MutableLiveData<String> _createFileData = new MutableLiveData<>();
    public final LiveData<String> createFileData = _createFileData;

    private Token token;

    @Inject
    public ThankYouViewModel(@NonNull Application application, @NonNull Repository repository) {
        super(application);
        this.repository = repository;
        this.token = Token.retrieve(application);
    }

    public void createLifeFile(String name) {
        _loading.setValue(true);
        repository.createLifeFile(token, name, new Repository.ResponseListener<BasicResponse>() {
            @Override
            public void onResponse(APIResponse<BasicResponse> apiResponse) {
                _loading.setValue(false);
                if (apiResponse.isSuccessful()) {
                    if (((BasicResponse) apiResponse.getData()).error == null) {
                        BasicResponse logoutResponse = (BasicResponse) apiResponse.getData();
                        LifeFileCache.getInstance().setCurrent(logoutResponse.life_file);
                        logoutResponse.life_file.save(getApplication());
                        _createFileData.setValue(null);
                        _createFileData.setValue("Life file added successfully.");
                        LifeFileCache.getInstance().getLifeFilesFromServer();
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
