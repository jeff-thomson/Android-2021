package com.stych.android.data.request;

public class VideoUploadRequest {
    public VideoMetadata video_file;
    public ParentLifeFile video;

    public VideoUploadRequest(String lifeFileId) {
        this.video_file = new VideoMetadata();
        this.video = new ParentLifeFile(lifeFileId);
    }

    public void setSegmentsCount(int numSegments) {
        this.video_file.num_segments = numSegments;
    }

    public int getSegmentCount() {
        return video_file.num_segments;
    }

    @Override
    public String toString() {
        return "VideoUploadRequest{" +
                "video_file=" + video_file +
                ", video=" + video +
                '}';
    }
}