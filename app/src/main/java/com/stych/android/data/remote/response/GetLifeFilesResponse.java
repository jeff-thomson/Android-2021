package com.stych.android.data.remote.response;

import java.util.List;

public class GetLifeFilesResponse {
    public String user_id;
    public List<LifeFileResponse> life_files;
    public Error error;

    @Override
    public String toString() {
        return "GetLifeFilesResponse{" +
                "user_id='" + user_id + '\'' +
                ", life_files=" + life_files +
                ", error=" + error +
                '}';
    }
}
