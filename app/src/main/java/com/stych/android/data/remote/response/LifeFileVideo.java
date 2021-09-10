package com.stych.android.data.remote.response;

import com.stych.android.BuildConfig;
import com.stych.android.data.Token;

public class LifeFileVideo {
    public String id;
    public VideoFile video_file;
    public String parent_life_file_id;
    public int position;
    public int videoStartMillis;

    public String getVideoURL(Token token) {
        String videoUrl = BuildConfig.BASE_URL + "/hls/life_files/" + parent_life_file_id + "/videos/" + id + "/" + token.user.id + "/master.m3u8";
        return videoUrl;
    }

    @Override
    public String toString() {
        return "LifeFileVideo{" +
                "id='" + id + '\'' +
                ", video_file=" + video_file +
                ", parent_life_file_id='" + parent_life_file_id + '\'' +
                ", position=" + position +
                ", videoStartMillis=" + videoStartMillis +
                '}';
    }
}
