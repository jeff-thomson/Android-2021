package com.stych.android.data.remote.response;

public class LifeFileVideosResponse {
    public int duration_seconds;
    public long size_bytes;
    public String access_url;
    public String video_file_id;
    public String recorded_at;
    public String parent_life_file_id;
    public String uploaded_at;
    public int num_video_chunks;
    public int position;
    public int average_bitrate;
    public String url_path;
    public String video_id;

    @Override
    public String toString() {
        return "LifeFileVideosResponse{" +
                "duration_seconds=" + duration_seconds +
                ", size_bytes=" + size_bytes +
                ", access_url='" + access_url + '\'' +
                ", video_file_id='" + video_file_id + '\'' +
                ", recorded_at='" + recorded_at + '\'' +
                ", parent_life_file_id='" + parent_life_file_id + '\'' +
                ", uploaded_at='" + uploaded_at + '\'' +
                ", num_video_chunks=" + num_video_chunks +
                ", position=" + position +
                ", average_bitrate=" + average_bitrate +
                ", url_path='" + url_path + '\'' +
                ", video_id='" + video_id + '\'' +
                '}';
    }
}
