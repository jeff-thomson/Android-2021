package com.stych.android;

import android.util.Log;

import androidx.annotation.NonNull;

import com.amplifyframework.core.Amplify;
import com.amplifyframework.core.Consumer;
import com.amplifyframework.storage.StorageException;
import com.amplifyframework.storage.result.StorageGetUrlResult;
import com.amplifyframework.storage.result.StorageUploadFileResult;
import com.stych.android.aws.S3Uploader;
import com.stych.android.data.Token;
import com.stych.android.data.remote.APIResponse;
import com.stych.android.data.remote.Repository;
import com.stych.android.data.remote.response.BasicResponse;
import com.stych.android.data.remote.response.VideoUploadResponse;
import com.stych.android.data.request.VideoUploadRequest;
import com.stych.android.util.VideoMetadataRetrieverUtil;

import java.io.File;

public class VideoFile {
    private static final String TAG = VideoFile.class.getSimpleName();

    public String lifeFileId;
    public String localPath;
    public VideoUploadRequest videoUploadRequest;

    private Status status;
    public VideoUploadResponse videoUploadResponse;

    public VideoFile() {
        status = Status.CREATED;
    }

    public static VideoFile createFromLocalPath(String lifeFileId, String localPath) {
        VideoFile videoFile = new VideoFile();
        videoFile.lifeFileId = lifeFileId;
        videoFile.localPath = localPath;
        videoFile.videoUploadRequest = new VideoUploadRequest(lifeFileId);
        return videoFile;
    }

    public static void preProcessVideoFile(VideoFile videoFile) {
        Log.d(TAG, "processVideoFile => processing started " + videoFile);
        videoFile.extractMetaData();
        Log.d(TAG, "processVideoFile => metadata extracted " + videoFile);
        int segmentCount = JNIBridge.generateVideoSegments(videoFile);
        videoFile.setSegmentCount(segmentCount);
        Log.d(TAG, "processVideoFile => segments generated " + videoFile);
    }

    public void extractMetaData() {
        VideoMetadataRetrieverUtil.retrieveMetadata(localPath, videoUploadRequest.video_file);
        status = Status.METADATA_RETRIEVED;
    }

    public void setSegmentCount(int count) {
        videoUploadRequest.setSegmentsCount(count);
        status = Status.SEGMENTED;
    }

    public void requestUploadData(Token token, Repository repository, final VideoFileListener listener) {
        status = Status.UPLOAD_DATA_REQUESTED;
        Log.d(TAG, "requestUploadData => requesting video upload " + this);
        repository.createVideoUploadRequest(token, lifeFileId, videoUploadRequest, new Repository.ResponseListener<VideoUploadResponse>() {
            @Override
            public void onResponse(APIResponse<VideoUploadResponse> apiResponse) {
                if (apiResponse != null && apiResponse.isSuccessful()) {
                    videoUploadResponse = apiResponse.getData();
                    status = Status.UPLOAD_DATA_RECEIVED;
                    Log.d(TAG, "requestUploadData => requested video upload " + videoUploadResponse.toString());
                    listener.onRequestVideoUploadGranted(videoUploadResponse);
                }
                Log.d(TAG, "requestUploadData => requested video upload " + this.toString());
            }
        });
    }

    public void uploadVideoThumb(final VideoFileListener listener) {
        String thumbPath = JNIBridge.saveVideoThumbnail(this);
        File file = new File(thumbPath);
        if(!file.exists()) {
            listener.onUploadError("Failed to process video thumbnail!");
            return;
        }

        final String key = videoUploadResponse.getKey() + ".jpg";
        S3Uploader.getInstance().upload(key, file, new S3Uploader.UploaderListener() {
            @Override
            public void onUpload(boolean success, String message) {
                if(success) {
                    Log.d(TAG, "Segment upload key=" + key);
                    listener.onVideoThumbUploaded();
                } else {
                    Log.e(TAG, "Failed to upload segment thumbPath=" + thumbPath);
                    listener.onUploadError(message);
                }
            }

            @Override
            public void onProgress(int percent) {

            }
        });
    }

    public void uploadVideosToS3UsingTransferUtil(final int index, final VideoFileListener listener) {
        if (index >= videoUploadRequest.getSegmentCount()) {
            status = Status.UPLOADED;
            Log.d(TAG, "All segments upload successfully!");
            listener.onVideoUploaded(index);
            return;
        }
        status = Status.UPLOAD_IN_PROGRESS;

        File file = new File(JNIBridge.getSegmentPath(lifeFileId, index));
        final String key = videoUploadResponse.getKey() + "/" + file.getName();
        S3Uploader.getInstance().upload(key, file, new S3Uploader.UploaderListener() {
            @Override
            public void onUpload(boolean success, String message) {
                if(success) {
                    Log.d(TAG, "Segment upload at=" + index + ", key=" + key);
                    listener.onVideoSegmentUploaded(index, videoUploadRequest.getSegmentCount());
                    uploadVideosToS3UsingTransferUtil(index + 1, listener);
                } else {
                    Log.e(TAG, "Failed to upload segment at=" + index);
                    listener.onUploadError(message);
                }
            }

            @Override
            public void onProgress(int percent) {

            }
        });
    }

    public void uploadVideosToS3(final int index, final VideoFileListener listener) {
        if (index >= videoUploadRequest.getSegmentCount()) {
            status = Status.UPLOADED;
            Log.d(TAG, "All segments upload successfully!");
            listener.onVideoUploaded(index);
            return;
        }
        status = Status.UPLOAD_IN_PROGRESS;

        File file = new File(JNIBridge.getSegmentPath(lifeFileId, index));
        String key = videoUploadResponse.getKey() + "/" + file.getName();
        Amplify.Storage.uploadFile(key, file, new Consumer<StorageUploadFileResult>() {
            @Override
            public void accept(@NonNull StorageUploadFileResult value) {
                Log.d(TAG, "Segment upload at=" + index + ", key=" + value.getKey());
                Amplify.Storage.getUrl(value.getKey(), new Consumer<StorageGetUrlResult>() {
                    @Override
                    public void accept(@NonNull StorageGetUrlResult value) {
                        Log.d(TAG, "Segment upload at=" + index + ", url=" + value.getUrl().toString());
                    }
                }, new Consumer<StorageException>() {
                    @Override
                    public void accept(@NonNull StorageException value) {
                        Log.e(TAG, "Failed to upload segment because =" + value.getMessage());
                    }
                });
                listener.onVideoSegmentUploaded(index, videoUploadRequest.getSegmentCount());
                uploadVideosToS3(index + 1, listener);
            }
        }, new Consumer<StorageException>() {
            @Override
            public void accept(@NonNull StorageException value) {
                Log.e(TAG, "Failed to upload segment at=" + index);
                listener.onUploadError(value.getMessage());
            }
        });
    }

    public void confirmVideoUpload(Token token, Repository repository, final VideoFileListener listener) {
        status = Status.UPLOAD_CONFIRM_REQUESTED;
        Log.d(TAG, "confirmVideoUpload => requesting video upload confirmation " + this.toString());
        repository.confirmVideoUploaded(token, videoUploadResponse.video_id, VideoMetadataRetrieverUtil.toUTCDate(null), videoUploadRequest, new Repository.ResponseListener<BasicResponse>() {
            @Override
            public void onResponse(APIResponse<BasicResponse> apiResponse) {
                if (apiResponse != null && apiResponse.isSuccessful()) {
                    BasicResponse response = apiResponse.getData();
                    Log.d(TAG, "confirmVideoUpload => requested video upload confirmed " + response != null ? response.toString() : null);
                } else {
                    Log.e(TAG, "confirmVideoUpload => requested video upload confirmation failed!" + this.toString());
                }
                status = Status.UPLOAD_CONFIRMED;
                listener.onVideoConfirmed(apiResponse != null ? apiResponse.getData() : null);
            }
        });
    }

    @Override
    public String toString() {
        return "VideoFile{" +
                "lifeFileId='" + lifeFileId + '\'' +
                ", localPath='" + localPath + '\'' +
                ", videoMetadata=" + videoUploadRequest +
                ", status=" + status +
                ", videoUploadResponse=" + videoUploadResponse +
                '}';
    }

    public enum Status {
        CREATED, METADATA_RETRIEVED, SEGMENTED, UPLOAD_DATA_REQUESTED, UPLOAD_DATA_RECEIVED, UPLOAD_IN_PROGRESS, UPLOADED, UPLOAD_CONFIRM_REQUESTED, UPLOAD_CONFIRMED, DOWNLOAD_IN_PROGRESS, DOWNLOADED
    }

    public interface VideoFileListener {
        void onRequestVideoUploadGranted(VideoUploadResponse response);

        void onVideoThumbUploaded();

        void onVideoUploaded(int segment);

        void onVideoConfirmed(BasicResponse response);

        void onVideoSegmentUploaded(int index, int totalCount);

        void onUploadError(String error);
    }
}
