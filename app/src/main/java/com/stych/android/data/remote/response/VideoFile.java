package com.stych.android.data.remote.response;

import java.util.List;

public class VideoFile {
    public int duration_seconds;
    public String recorded_at;
    public long size_bytes;
    public String uploaded_at;
    public String directory_path;
    public String id;
    public String recorded_at_utc;
    public int num_segments;
    public int segments_average_bitrate;
    public List<String> segment_access_urls;

    @Override
    public String toString() {
        return "VideoFile{" +
                "duration_seconds=" + duration_seconds +
                ", recorded_at='" + recorded_at + '\'' +
                ", size_bytes=" + size_bytes +
                ", uploaded_at='" + uploaded_at + '\'' +
                ", directory_path='" + directory_path + '\'' +
                ", id='" + id + '\'' +
                ", recorded_at_utc='" + recorded_at_utc + '\'' +
                ", num_segments=" + num_segments +
                ", segments_average_bitrate=" + segments_average_bitrate +
                ", segment_access_urls=" + segment_access_urls +
                '}';
    }
}
