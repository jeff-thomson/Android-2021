package com.stych.android.data.remote.response;

public class VideoUploadResponse {
    public String video_id;
    public VideoUploadFile video_file;

    public String getKey() {
        String key = video_file != null ? video_file.directory_path : null;
        return key != null && key.startsWith("/") ? key.substring(1) : key;
    }

    @Override
    public String toString() {
        return "VideoUpload{" +
                "video_id='" + video_id + '\'' +
                ", video_file=" + video_file +
                '}';
    }
}
