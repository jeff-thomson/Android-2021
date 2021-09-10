package com.stych.android.data.remote.response;

import java.util.List;

public class LifeFileVideoIDsResponse {
    public String life_file_created_at;
    public String life_file_id;
    public String life_file_num_videos;
    public String life_file_title;
    public List<String> life_file_video_ids;

    @Override
    public String toString() {
        return "LifeFileVideoIDsResponse{" +
                "life_file_created_at='" + life_file_created_at + '\'' +
                ", life_file_id='" + life_file_id + '\'' +
                ", life_file_num_videos='" + life_file_num_videos + '\'' +
                ", life_file_title='" + life_file_title + '\'' +
                ", life_file_video_ids=" + life_file_video_ids +
                '}';
    }
}
