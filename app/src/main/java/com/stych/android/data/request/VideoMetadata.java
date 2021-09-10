package com.stych.android.data.request;

public class VideoMetadata {
    public int duration_seconds;
    public long size_bytes;
    public int segments_average_bitrate;
    public int num_segments;
    public String recorded_at;

    @Override
    public String toString() {
        return "VideoMetadata{" +
                "duration_seconds=" + duration_seconds +
                ", size_bytes=" + size_bytes +
                ", segments_average_bitrate=" + segments_average_bitrate +
                ", num_segments=" + num_segments +
                ", recorded_at='" + recorded_at + '\'' +
                '}';
    }
}
