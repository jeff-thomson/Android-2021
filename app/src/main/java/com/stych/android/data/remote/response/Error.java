package com.stych.android.data.remote.response;

import com.google.gson.annotations.SerializedName;

public class Error {
    @SerializedName("code")
    public int code;
    @SerializedName("type")
    public String type;
    @SerializedName("message")
    public String message;

    @Override
    public String toString() {
        return "Error{" +
                "code=" + code +
                ", type='" + type + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
