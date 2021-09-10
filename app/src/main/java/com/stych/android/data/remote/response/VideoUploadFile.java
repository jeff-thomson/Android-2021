package com.stych.android.data.remote.response;

public class VideoUploadFile {
    public String id;
    public String directory_path;

    @Override
    public String toString() {
        return "VideoUploadFile{" +
                "id='" + id + '\'' +
                ", directory_path='" + directory_path + '\'' +
                '}';
    }
}
