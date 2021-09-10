package com.stych.android.data.remote.response;

import android.util.Log;

import java.util.List;

public class GetLifeFileVideosResponse {
    public Error error;
    public List<LifeFileVideo> life_file_videos;

    public void buildVideoStartSeconds() {
        if (life_file_videos != null) {
            int startMillis = 0;
            for (LifeFileVideo video : life_file_videos) {
                video.videoStartMillis = startMillis;
                VideoFile videoFile = video.video_file;
                int durationMillis = 0;
                if (videoFile != null) {
                    if (videoFile.segments_average_bitrate > 0) {
                        durationMillis = (int) ((videoFile.size_bytes * 8l * 1000l) / videoFile.segments_average_bitrate);
                    } else {
                        durationMillis = videoFile.duration_seconds * 1000;
                    }
                    startMillis += durationMillis;
                }
                Log.d("LifeFile", "buildVideoStartSeconds videoStartMillis=" + video.videoStartMillis + ", duration=" + videoFile.duration_seconds + ", durationMillis=" + durationMillis);
            }
        }
    }

    @Override
    public String toString() {
        return "GetLifeFileVideosResponse{" +
                "error=" + error +
                ", life_file_videos=" + life_file_videos +
                '}';
    }
}
