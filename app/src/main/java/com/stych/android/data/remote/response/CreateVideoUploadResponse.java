package com.stych.android.data.remote.response;

public class CreateVideoUploadResponse {
    public Error error;
    public String url_path;
    public String video_id;

    @Override
    public String toString() {
        return "CreateVideoUploadResponse{" +
                "error=" + error +
                ", url_path='" + url_path + '\'' +
                ", video_id='" + video_id + '\'' +
                '}';
    }
}
