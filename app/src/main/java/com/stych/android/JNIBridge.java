package com.stych.android;

import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.util.Log;
import android.util.Size;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class JNIBridge {
    public static final String CACHE_PATH = "/data/user/0/com.stych.android/cache/";
    public static final String SEGMENT_EXT = ".ts";
    public static final String THUMB_EXT = ".jpg";
    private static final String TAG = JNIBridge.class.getSimpleName();

    static {
        System.loadLibrary("ap4");
    }

    public static int generateVideoSegments(VideoFile videoFile) {
        createCacheFolder(videoFile.lifeFileId);

        String inputFilePath = videoFile.localPath;
        String outputFilePath = getSegmentPath(videoFile.lifeFileId, Integer.MIN_VALUE);

        deleteAllSegments(videoFile.lifeFileId);
        boolean result = new JNIBridge().generateSegments(inputFilePath, outputFilePath);
        Log.d(TAG, "generateVideoSegments result=" + result);

        return result ? countSegments(videoFile.lifeFileId) : 0;
    }

    public static String getVideoThumbPath(String lifeFileId, String videoId) {
        return CACHE_PATH + lifeFileId + "/" + videoId + THUMB_EXT;
    }

    public static File getVideoThumbPath(String lifeFileId) {
        File dir = new File(CACHE_PATH + lifeFileId);
        if(dir.exists()) {
            for (File file : dir.listFiles()) {
                if (file.getName().endsWith(THUMB_EXT)) {
                    return file;
                }
            }
        }
        return null;
    }

    public static String saveVideoThumbnail(VideoFile videoFile) {
        try {
            String thumbFilePath = getVideoThumbPath(videoFile.lifeFileId, videoFile.videoUploadResponse.video_id);
            Bitmap bitmap = ThumbnailUtils.createVideoThumbnail(new File(videoFile.localPath), new Size(200, 120), null);
            FileOutputStream fileOutputStream = new FileOutputStream(thumbFilePath);
            boolean compressed = bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fileOutputStream);
            Log.d(TAG, "saveVideoThumbnail compressed=" + compressed + ", thumbFilePath=" + thumbFilePath);
            return thumbFilePath;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static void deleteAllSegments(String lifeFileId) {
        String path = CACHE_PATH + lifeFileId;
        File dir = new File(path);
        if(dir.exists()) {
            for (File file : dir.listFiles()) {
                if(file.getName().endsWith(SEGMENT_EXT)) {
                    file.delete();
                }
            }
        }
    }

    public static String getSegmentPath(String lifeFileId, int index) {
        String path = CACHE_PATH + lifeFileId + "/" + "%d" + SEGMENT_EXT;
        if (index >= 0) {
            return String.format(path, index);
        }
        return path;
    }

    public static int countSegments(String lifeFileId) {
        int segmentCount = 0, i = 0;
        while (i >= 0) {
            File file = new File(getSegmentPath(lifeFileId, i));
            if (file.exists()) {
                i++;
                segmentCount = i;
            } else {
                i = -1;
            }
        }
        return segmentCount;
    }

    private static String createCacheFolder(String lifeFileId) {
        File parentDir = new File(CACHE_PATH, lifeFileId);
        if (!parentDir.exists()) {
            parentDir.mkdirs();
        }

        String path = parentDir.getAbsolutePath();
        Log.d(TAG, "Cache directory created at " + path);
        return path;
    }

    public native boolean generateSegments(String inputFileUri, String outputFileUri);
}
