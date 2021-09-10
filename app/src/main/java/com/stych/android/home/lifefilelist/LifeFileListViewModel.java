package com.stych.android.home.lifefilelist;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.stych.android.BaseAndroidViewModel;
import com.stych.android.LifeFileCache;
import com.stych.android.data.Token;
import com.stych.android.data.remote.APIResponse;
import com.stych.android.data.remote.Repository;
import com.stych.android.data.remote.response.BasicResponse;
import com.stych.android.data.remote.response.LifeFileResponse;

import java.util.List;

import javax.inject.Inject;

public class LifeFileListViewModel extends BaseAndroidViewModel {
    private final Repository repository;
    protected MutableLiveData<List<LifeFileResponse>> _listFileData = new MutableLiveData<>();
    public final LiveData<List<LifeFileResponse>> listFileData = _listFileData;
    protected MutableLiveData<String> _deleteFile = new MutableLiveData<>();
    public final LiveData<String> deleteFile = _deleteFile;

    private Token token;

    @Inject
    public LifeFileListViewModel(@NonNull Application application, @NonNull Repository repository) {
        super(application);
        this.repository = repository;
        this.token = Token.retrieve(application);
        _listFileData.setValue(LifeFileCache.getInstance().getLifeFiles());

        LifeFileCache.getInstance().setLifeFileListener(new LifeFileCache.LifeFileListener() {
            @Override
            public void onLifeFileRefresh(List<LifeFileResponse> lifeFiles) {
                Log.d("LifeFileListViewModel", "Current life file=" + LifeFileCache.getInstance().getCurrent());
                _listFileData.setValue(lifeFiles);
            }
        });
    }

    public void deleteLifeFile(String fileId) {
        _loading.setValue(true);
        repository.deleteLifeFile(token, fileId, new Repository.ResponseListener<BasicResponse>() {
            @Override
            public void onResponse(APIResponse<BasicResponse> apiResponse) {
                _loading.setValue(false);
                if (apiResponse.isSuccessful()) {
                    if (((BasicResponse) apiResponse.getData()).error == null) {
                        BasicResponse logoutResponse = (BasicResponse) apiResponse.getData();
                        _deleteFile.setValue(null);
                        _deleteFile.setValue("Life file deleted successfully.");
                        LifeFileResponse lifeFile = LifeFileCache.getInstance().getCurrent();
                        //If same life file as current then remove it
                        if(lifeFile != null && fileId.equalsIgnoreCase(lifeFile.life_file_id)) {
                            LifeFileResponse.clear(getApplication());
                            LifeFileCache.getInstance().setCurrent(null);
                        }
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
