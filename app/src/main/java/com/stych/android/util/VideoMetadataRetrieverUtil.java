package com.stych.android.util;

import android.media.MediaMetadataRetriever;
import android.text.TextUtils;
import android.util.Log;

import com.stych.android.data.request.VideoMetadata;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class VideoMetadataRetrieverUtil {
    private static final String TAG = VideoMetadataRetrieverUtil.class.getSimpleName();

    public static void retrieveMetadata(String filePath, VideoMetadata metadata) {
        if (TextUtils.isEmpty(filePath) || null == metadata) return;

        MediaMetadataRetriever metaRetriever = new MediaMetadataRetriever();
        metaRetriever.setDataSource(filePath);

        //metadata.title = getTitle(filePath, metaRetriever);
        metadata.duration_seconds = getDuration(metaRetriever);
        metadata.segments_average_bitrate = VideoMetadataRetrieverUtil.parseInt(metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_BITRATE));
        metadata.size_bytes = getFileSize(filePath);
        metadata.recorded_at = getRecordedAt(filePath, metaRetriever);

        metaRetriever.release();
    }

    private static long getFileSize(String filePath) {
        File file = new File(filePath);
        long length = file.length();
        return length;
    }

    private static String getTitle(String filePath, MediaMetadataRetriever metaRetriever) {
        String[] paths = filePath.split("/");
        String title = metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_BITRATE);
        if (TextUtils.isEmpty(title)) {
            title = new File(filePath).getName();
        }
        if (paths != null && paths.length > 0) {
//            return paths[paths.length - 1];
            title = paths[paths.length - 1];
            if (title.contains(".")) {
                return title.substring(0, title.indexOf(".") + 1);
            }
        }
        return title;
    }

    private static int getDuration(MediaMetadataRetriever metaRetriever) {
        String data = metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
        long durationMs = parseLong(data);
        int durationS = (int) (durationMs / 1000);
        return durationS;
    }

    private static String getRecordedAt(String filePath, MediaMetadataRetriever metaRetriever) {
        String metaDate = metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DATE);
        Log.d(TAG, "getRecordedAt metaDate=" + metaDate);
        Date date = null;
        if(metaDate != null && metaDate.length() == 20 && !metaDate.startsWith("1904")) {
            try {
                //20210427T054233.000Z
                date = new SimpleDateFormat("yyyyMMdd'T'HHmmss.SSS").parse(metaDate);
                Log.d(TAG, "getRecordedAt parsed date=" + date.toString());
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        if(date == null) {
            File file = new File(filePath);
            date = new Date(file.lastModified());
            Log.d(TAG, "getRecordedAt last modified date=" + date.toString());
        }

        return toUTCDate(date);
    }

    public static String toUTCDate(Date date) {
        if (date == null) {
            date = new Date();
            Log.d(TAG, "toUTCDate default date=" + date.toString());
        } else {
            Log.d(TAG, "toUTCDate date=" + date.toString());
        }
        return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ").format(date);
    }

    public static long parseLong(String s) {
        if (s != null && (s = s.trim()).length() > 0) {
            try {
                return Long.parseLong(s);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return 0l;
    }

    public static int parseInt(String s) {
        if (s != null && (s = s.trim()).length() > 0) {
            try {
                return Integer.parseInt(s);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return 0;
    }
}
