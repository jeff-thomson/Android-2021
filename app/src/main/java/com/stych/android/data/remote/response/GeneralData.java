package com.stych.android.data.remote.response;

public class GeneralData {
    public int num_life_files;
    public long total_data_used_mb;

    @Override
    public String toString() {
        return "GeneralData{" +
                "num_life_files=" + num_life_files +
                ", total_data_used_mb=" + total_data_used_mb +
                '}';
    }
}
