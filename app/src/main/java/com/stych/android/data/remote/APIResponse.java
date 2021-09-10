package com.stych.android.data.remote;

import com.google.gson.annotations.SerializedName;
import com.stych.android.Constant;

public class APIResponse<T> {
    @SerializedName("data")
    private T data;
    @SerializedName("message")
    private String message;
    @SerializedName("status")
    private String status = "OK";

    public static APIResponse createErrorResponse(Throwable throwable) {
        APIResponse response = new APIResponse();
        response.status = "ERROR";
        response.message = throwable.getLocalizedMessage();
        return response;
    }

    public static APIResponse createDefaultErrorResponse() {
        APIResponse response = new APIResponse();
        response.status = "ERROR";
        response.message = Constant.DEFAULT_ERROR;
        return response;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isSuccessful() {
        return "OK".equals(status);
    }

    public boolean isUnauthorized() {
        return "STATUS_UNAUTHORIZED".equals(status);
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "APIResponse{" +
                "data=" + data +
                ", message='" + message + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
