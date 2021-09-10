package com.stych.android.data.remote.response;

public class BasicResponse {
    public String message;
    public String status;
    public Error error;
    public LifeFileResponse life_file;

    @Override
    public String toString() {
        return "BasicResponse{" +
                "message='" + message + '\'' +
                ", status='" + status + '\'' +
                ", error=" + error +
                ", life_file=" + life_file +
                '}';
    }
}

