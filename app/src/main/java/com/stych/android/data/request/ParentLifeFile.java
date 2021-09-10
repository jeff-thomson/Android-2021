package com.stych.android.data.request;

import com.stych.android.util.VideoMetadataRetrieverUtil;

public class ParentLifeFile {
    public long parent_life_file_id;

    public ParentLifeFile(String parent_life_file_id) {
        this.parent_life_file_id = VideoMetadataRetrieverUtil.parseLong(parent_life_file_id);
    }

    @Override
    public String toString() {
        return "ParentLifeFile{" +
                "parent_life_file_id='" + parent_life_file_id + '\'' +
                '}';
    }
}
