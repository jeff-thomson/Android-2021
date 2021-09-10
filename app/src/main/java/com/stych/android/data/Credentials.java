package com.stych.android.data;

public class Credentials {
    public String awsAccessKeyID;
    public String awsClientSecretAccessKey;

    @Override
    public String toString() {
        return "Credentials{" +
                "awsAccessKeyID='" + awsAccessKeyID + '\'' +
                ", awsClientSecretAccessKey='" + awsClientSecretAccessKey + '\'' +
                '}';
    }
}
