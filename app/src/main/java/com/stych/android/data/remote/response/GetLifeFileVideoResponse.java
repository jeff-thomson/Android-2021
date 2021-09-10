package com.stych.android.data.remote.response;

public class GetLifeFileVideoResponse {
    public Error error;
    public LifeFileVideoIDsResponse life_file_video_ids;

    @Override
    public String toString() {
        return "GetLifeFileVideoResponse{" +
                "error=" + error +
                ", life_file_video_ids=" + life_file_video_ids +
                '}';
    }
}
