package com.stych.android;

import android.app.Application;
import android.text.TextUtils;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.stych.android.data.Token;
import com.stych.android.data.UserData;
import com.stych.android.data.remote.APIResponse;
import com.stych.android.data.remote.Repository;
import com.stych.android.data.remote.response.BasicResponse;
import com.stych.android.data.remote.response.GetLifeFilesResponse;
import com.stych.android.data.remote.response.LifeFileResponse;
import com.stych.android.data.remote.response.VideoUploadResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LifeFileCache {
    private static LifeFileCache INSTANCE;
    private final String TAG = LifeFileCache.class.getSimpleName();
    private final Map<LifeFileResponse, VideoFile> videoFileMap = new HashMap<>();
    private Token token;
    private Repository repository;
    private LifeFileListener listener;
    private List<LifeFileResponse> lifeFiles;
    private LifeFileResponse currentLifeFile;

    protected MutableLiveData<String> _error = new MutableLiveData<>();
    public final LiveData<String> error = _error;
    protected MutableLiveData<String> _progress = new MutableLiveData<>();
    public final LiveData<String> progress = _progress;

    private LifeFileCache() {
    }

    public static LifeFileCache getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new LifeFileCache();
        }
        return INSTANCE;
    }

    public void init(LifeFileResponse lifeFileResponse, Token token, Repository repository) {
        this.currentLifeFile = lifeFileResponse;
        this.token = token;
        this.repository = repository;
    }

    public void clearLifeFileData() {
        lifeFiles = null;
        currentLifeFile = null;
    }

    public void setToken(Token token) {
        this.token = token;
    }

    public void setLifeFileListener(LifeFileListener listener) {
        this.listener = listener;
        notifyLifeFilesChanged();
    }

    public List<LifeFileResponse> getLifeFiles() {
        if (lifeFiles == null) {
            lifeFiles = new ArrayList<>();
        }
        return lifeFiles;
    }

    public LifeFileResponse getCurrent() {
        if (lifeFiles != null && !lifeFiles.isEmpty()) {
            if(currentLifeFile == null) {
                currentLifeFile = lifeFiles.get(0);
            }
            Log.d(TAG, "getCurrent currentLifeFile=" + (currentLifeFile != null ? currentLifeFile.toString() : null));
            return currentLifeFile;
        }
        Log.d(TAG, "getCurrent currentLifeFile=" + null);
        return null;
    }

    public void setCurrent(LifeFileResponse lifeFile) {
        this.currentLifeFile = lifeFile;
        if(lifeFile != null) {
            if(lifeFiles == null) {
                lifeFiles = new ArrayList<>();
            }
            if(lifeFiles.isEmpty()) {
                lifeFiles.add(lifeFile);
            }
        }
        Log.d(TAG, "setCurrent currentLifeFile=" + (lifeFile != null ? lifeFile.toString() : null));
        notifyLifeFilesChanged();
    }

    public void getLifeFilesFromServer() {
        Log.d(TAG, "getLifeFiles repository=" + repository + ", hashCode=" + hashCode());

        repository.getLifeFileList(token, new Repository.ResponseListener<GetLifeFilesResponse>() {
            @Override
            public void onResponse(APIResponse<GetLifeFilesResponse> apiResponse) {
                if (apiResponse != null && apiResponse.getData() != null) {
                    lifeFiles = apiResponse.getData().life_files;
                }
                setCurrent(getCurrent());
                Log.d(TAG, "getLifeFiles lifeFiles=" + lifeFiles);
                notifyLifeFilesChanged();
            }
        });
    }

    private void notifyLifeFilesChanged() {
        if (listener != null) {
            listener.onLifeFileRefresh(getLifeFiles());
        }
    }

    public void addVideoFileToProcessQueue(Application application, String localPath) {
        if(!TextUtils.isEmpty(_progress.getValue())) {
            _error.setValue("Only one video can be uploaded at a time!");
            _progress.setValue(null);
            return;
        }
        if(TextUtils.isEmpty(localPath)) {
            _error.setValue("Cloud Files not enabled, Upload from local gallery.");
            _progress.setValue(null);
            return;
        }
        _progress.setValue("Validating video file ...");
        final VideoFile videoFile = VideoFile.createFromLocalPath(getCurrent().life_file_id, localPath);
        VideoFile.preProcessVideoFile(videoFile);

        if(videoFile.videoUploadRequest.video_file.num_segments == 0) {
            _error.setValue("Video is invalid or segments are not generated!");
            _progress.setValue(null);
            return;
        } /*else if(videoFile.videoUploadRequest.video_file.num_segments < videoFile.videoUploadRequest.video_file.duration_seconds) {
           Log.d(TAG, "addVideoFileToProcessQueue " + videoFile.toString());
            _error.setValue("Video not supported!");
            _progress.setValue(null);
            return;
        }*/
        UserData userData = UserData.retrieve(application);
        if(userData.isSizeExceedThanAllowed(videoFile.videoUploadRequest.video_file.size_bytes)) {
            _error.setValue("Allowed limit exceeded! Please subscribe higher plan.");
            _progress.setValue(null);
            return;
        }
        userData.totalVideoSize = userData.totalVideoSize + videoFile.videoUploadRequest.video_file.size_bytes;
        userData.save(application);
        _progress.setValue("Segments generated, creating video upload request ...");

        final VideoFile.VideoFileListener listener = new VideoFile.VideoFileListener() {
            @Override
            public void onRequestVideoUploadGranted(VideoUploadResponse response) {
                _progress.setValue("Processing video upload request ...");
                videoFile.uploadVideoThumb(this);
            }

            @Override
            public void onVideoThumbUploaded() {
                _progress.setValue("Starting video upload ...");
                videoFile.uploadVideosToS3UsingTransferUtil(0, this);
            }

            @Override
            public void onVideoUploaded(int segment) {
                _progress.setValue("Video uploaded, requesting confirmation...");
                videoFile.confirmVideoUpload(token, repository, this);
            }

            @Override
            public void onVideoConfirmed(BasicResponse response) {
                notifyLifeFilesChanged();
                _progress.setValue(null);
            }

            @Override
            public void onVideoSegmentUploaded(int index, int totalCount) {
                _progress.setValue("Uploading video part " + (index + 1) + "/" + totalCount + " ...");
            }

            @Override
            public void onUploadError(String error) {
                _error.setValue(error);
                _progress.setValue(null);
            }
        };

        videoFile.requestUploadData(token, repository, listener);
    }

    public interface LifeFileListener {
        void onLifeFileRefresh(List<LifeFileResponse> lifeFiles);
    }
}
