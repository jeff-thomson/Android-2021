package com.stych.android.home;

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
import com.stych.android.util.VideoMetadataRetrieverUtil;

import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

public class MainViewModel extends BaseAndroidViewModel {
    private final Repository repository;
    private final MainModel model;
    protected MutableLiveData<Boolean> _enableSelect = new MutableLiveData<>();
    public final LiveData<Boolean> enableSelect = _enableSelect;
    protected MutableLiveData<String> _createFileData = new MutableLiveData<>();
    public final LiveData<String> createFileData = _createFileData;
    protected MutableLiveData<String> _renameFileData = new MutableLiveData<>();
    public final LiveData<String> renameFileData = _renameFileData;
    protected MutableLiveData<List<LifeFileResponse>> _listFileData = new MutableLiveData<>();
    public final LiveData<List<LifeFileResponse>> listFileData = _listFileData;
    protected MutableLiveData<GetLifeFileVideoResponse> _videoIdFile = new MutableLiveData<>();
    public final LiveData<GetLifeFileVideoResponse> videoIDFile = _videoIdFile;
    protected MutableLiveData<List<LifeFileVideo>> _listVideo = new MutableLiveData<>();
    public final LiveData<List<LifeFileVideo>> listVideo = _listVideo;
    protected MutableLiveData<LifeFileVideo> _videoSegment = new MutableLiveData<>();
    public final LiveData<LifeFileVideo> videoSegment = _videoSegment;
    protected MutableLiveData<LifeFileResponse> _currentLifeFile = new MutableLiveData<>();
    public final LiveData<LifeFileResponse> currentLifeFile = _currentLifeFile;

    private Token token;

    @Inject
    public MainViewModel(@NonNull Application application, @NonNull Repository repository) {
        super(application);
        this.repository = repository;
        this.token = Token.retrieve(application);
        _enableSelect.setValue(false);
        this.model = new MainModel();

        registerLifeFileListListener();
    }

    public void registerLifeFileListListener() {
        LifeFileCache.getInstance().setLifeFileListener(new LifeFileCache.LifeFileListener() {
            @Override
            public void onLifeFileRefresh(List<LifeFileResponse> lifeFiles) {
                Log.d("MainViewModel", "Current life file=" + LifeFileCache.getInstance().getCurrent());
                _listFileData.setValue(lifeFiles);
                if (LifeFileCache.getInstance().getCurrent() != null) {
                    //TODO: display in menu as selected. add life files in menu as well
                    _currentLifeFile.setValue(LifeFileCache.getInstance().getCurrent());
                }
            }
        });
    }

    public Token getToken() {
        return token;
    }

    public void setEnableSelect(boolean value) {
        _enableSelect.setValue(value);
    }

    public void setEmail(String email) {
        this.model.email = email;
    }

    public void setVideoSegment(LifeFileVideo lifeFileVideo) {
        _videoSegment.setValue(lifeFileVideo);
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

    public void renameLifeFile(String name) {
        if (LifeFileCache.getInstance().getCurrent() != null) {
            _loading.setValue(true);
            repository.updateLifeFile(token, LifeFileCache.getInstance().getCurrent().life_file_id, name, new Repository.ResponseListener<BasicResponse>() {
                @Override
                public void onResponse(APIResponse<BasicResponse> apiResponse) {
                    _loading.setValue(false);
                    if (apiResponse.isSuccessful()) {
                        if (((BasicResponse) apiResponse.getData()).error == null) {
                            BasicResponse logoutResponse = (BasicResponse) apiResponse.getData();
                            _renameFileData.setValue(null);
                            _renameFileData.setValue("Life file renamed successfully.");
                            LifeFileCache.getInstance().getCurrent().name = name;
                            LifeFileCache.getInstance().getLifeFilesFromServer();
                        } else {
                            _error.setValue(((BasicResponse) apiResponse.getData()).error.message);
                        }
                    } else {
                        _error.setValue(apiResponse.getMessage());
                    }
                }
            });
        } else {
            _error.setValue("No Life Selected or Created yet.");
        }
    }

    public void getLifeFileVideoIds(String lifeId) {
        _loading.setValue(true);
        repository.getLifeFileVideoIds(token, lifeId, new Repository.ResponseListener<GetLifeFileVideoResponse>() {
            @Override
            public void onResponse(APIResponse<GetLifeFileVideoResponse> apiResponse) {
                _loading.setValue(false);
                if (apiResponse.isSuccessful()) {
                    if (((GetLifeFileVideoResponse) apiResponse.getData()).error == null) {
                        GetLifeFileVideoResponse getLifeFilesResponse = (GetLifeFileVideoResponse) apiResponse.getData();
                        _videoIdFile.setValue(getLifeFilesResponse);
                    } else {
                        _error.setValue(((GetLifeFileVideoResponse) apiResponse.getData()).error.message);
                    }
                } else {
                    _error.setValue(apiResponse.getMessage());
                }
            }
        });
    }

    public void refreshCurrentLifeFileVideos() {
        LifeFileResponse lifeFile = _currentLifeFile.getValue();
        if(lifeFile != null) {
            getLifeFileVideoList(lifeFile.life_file_id);
        }
    }

    public void getLifeFileVideoList(String lifeId) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, 2000);
        String startAt = VideoMetadataRetrieverUtil.toUTCDate(calendar.getTime());
        calendar.add(Calendar.YEAR, 100);
        String endAt = VideoMetadataRetrieverUtil.toUTCDate(calendar.getTime());

        _loading.setValue(true);
        repository.getLifeFileVideos(token, lifeId, startAt, endAt, new Repository.ResponseListener<GetLifeFileVideosResponse>() {
            @Override
            public void onResponse(APIResponse<GetLifeFileVideosResponse> apiResponse) {
                _loading.setValue(false);
                if (apiResponse.isSuccessful()) {
                    if (((GetLifeFileVideosResponse) apiResponse.getData()).error == null) {
                        GetLifeFileVideosResponse getLifeFilesResponse = (GetLifeFileVideosResponse) apiResponse.getData();
                        if(getLifeFilesResponse != null) {
                            Collections.reverse(getLifeFilesResponse.life_file_videos);
                            getLifeFilesResponse.buildVideoStartSeconds();
                        }
                        _listVideo.setValue(getLifeFilesResponse != null ? getLifeFilesResponse.life_file_videos : null);
                    } else {
                        _error.setValue(((GetLifeFileVideosResponse) apiResponse.getData()).error.message);
                    }
                } else {
                    _error.setValue(apiResponse.getMessage());
                }
            }
        });
    }

    public void deleteLifeFileVideo(String lifeFileVideoId) {
        _loading.setValue(true);
        repository.deleteLifeFileVideo(token, lifeFileVideoId, new Repository.ResponseListener<BasicResponse>() {
            @Override
            public void onResponse(APIResponse<BasicResponse> apiResponse) {
                _loading.setValue(false);
                if (apiResponse.isSuccessful()) {
                    if (((BasicResponse) apiResponse.getData()).error == null) {
                        BasicResponse getLifeFilesResponse = (BasicResponse) apiResponse.getData();
                        refreshCurrentLifeFileVideos();
                    } else {
                        _error.setValue(((BasicResponse) apiResponse.getData()).error.message);
                    }
                } else {
                    _error.setValue(apiResponse.getMessage());
                }
            }
        });
    }

    public void copyLifeFile(String lifeFileVideoId, String toUserEmailAddr) {
        _loading.setValue(true);
        repository.copyLifeFile(token, lifeFileVideoId, toUserEmailAddr, new Repository.ResponseListener<BasicResponse>() {
            @Override
            public void onResponse(APIResponse<BasicResponse> apiResponse) {
                _loading.setValue(false);
                if (apiResponse.isSuccessful()) {
                    if (((BasicResponse) apiResponse.getData()).error == null) {
                        _error.setValue("Life file video shared successfully.");
                    } else {
                        _error.setValue(((BasicResponse) apiResponse.getData()).error.message);
                    }
                } else {
                    _error.setValue(apiResponse.getMessage());
                }
            }
        });
    }

    public void getGeneralUserData() {
        _loading.setValue(true);
        repository.getUsageData(token, new Repository.ResponseListener<UserVideoData>() {
            @Override
            public void onResponse(APIResponse<UserVideoData> apiResponse) {
                _loading.setValue(false);
                if (apiResponse.isSuccessful()) {
                    UserVideoData userVideoData = apiResponse.getData();
                    Log.d("MainViewModel", "getUsageData " + (userVideoData != null ? userVideoData.toString() : null));
                    UserVideoData.UserData userData = userVideoData != null ? userVideoData.user : null;
                    UserVideoData.UsageData usageData = userData != null ? userData.usage_data : null;
                    GeneralData generalData = usageData != null ? usageData.general : null;
                    if(generalData != null) {
                        UserData user = UserData.retrieve(getApplication());
                        user.setTotalDataUsedMb(generalData.total_data_used_mb);
                        user.setPurchasePlan(usageData.getBytesPurchased());
                        user.save(getApplication());
                    }
                } else {
                    _error.setValue(apiResponse.getMessage());
                }
            }
        });
    }

    public String getCurrentVideoURL() {
        LifeFileResponse lifeFile = _currentLifeFile.getValue();
        List<LifeFileVideo> lifeFileVideos = _listVideo.getValue();
        if (lifeFile != null && lifeFileVideos != null && !lifeFileVideos.isEmpty()) {
            return BuildConfig.BASE_URL + "/hls/life_files/" + lifeFile.life_file_id + "/" + token.user.id + "/master.m3u8";
        }
        return null;
    }
}
