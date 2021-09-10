package com.stych.android.aws;

import android.app.Application;
import android.content.Context;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.auth.CognitoCredentialsProvider;
import com.amazonaws.auth.PropertiesFileCredentialsProvider;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.stych.android.BuildConfig;
import com.stych.android.Constant;
import com.stych.android.data.Credentials;
import com.stych.android.data.Token;

import java.io.File;

public class S3Uploader {
    private static S3Uploader s3Uploader;
    private TransferUtility transferUtility;

    private S3Uploader() {
    }

    public static S3Uploader getInstance() {
        if (null == s3Uploader) {
            s3Uploader = new S3Uploader();
        }
        return s3Uploader;
    }

    public static String getFileURL(String fileName) {
        return "https://" + BuildConfig.S3_BUCKET + ".s3.amazonaws.com" + handleSpecialCharsInUrl(fileName);
    }

    public static String handleSpecialCharsInUrl(String url) {
        return url.replace("+", "%2B").replace("$", "%24");
    }

    public void init(final Application application) {
        AWSCredentialsProvider keySecretProvider = new AWSCredentialsProvider() {
            @Override
            public AWSCredentials getCredentials() {
                Token token = Token.retrieve(application);
                Credentials credentials = token != null ? token.credentials : null;
                return new AWSCredentials() {
                    @Override
                    public String getAWSAccessKeyId() {
                        return credentials != null ? credentials.awsAccessKeyID : "";
                    }

                    @Override
                    public String getAWSSecretKey() {
                        return credentials != null ? credentials.awsClientSecretAccessKey : "";
                    }
                };
            }

            @Override
            public void refresh() {

            }
        };
        AmazonS3 s3 = new AmazonS3Client(keySecretProvider);
        s3.setRegion(Region.getRegion(Regions.US_EAST_1));
        transferUtility = new TransferUtility(s3, application);
    }

    public void upload(String key, File file, UploaderListener listener) {
        TransferObserver observer = transferUtility.upload(BuildConfig.S3_BUCKET, key, file);
        if (null != listener) {
            observer.setTransferListener(new TransferListener() {
                @Override
                public void onStateChanged(int id, TransferState state) {
                    if(state == TransferState.COMPLETED) {
                        listener.onUpload(true, "File uploaded successfully!");
                    }
                }

                @Override
                public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {
                    int percentage = (int) (bytesCurrent / bytesTotal * 100);
                    listener.onProgress(percentage);
                }

                @Override
                public void onError(int id, Exception ex) {
                    listener.onUpload(false, ex.getMessage());
                }
            });
        }
    }

    private TransferListener getTransferListener(final int uploadId, final UploaderListener listener) {
        return new TransferListener() {
            int lastPercentage = -1;

            @Override
            public void onStateChanged(int id, TransferState state) {
                if (id == uploadId && state == TransferState.COMPLETED) {
                    listener.onUpload(true, "File uploaded successfully.");
                }
            }

            @Override
            public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {
                int percentage = (int) (bytesCurrent / bytesTotal * 100);
                if (id == uploadId && percentage != lastPercentage) {
                    listener.onProgress(percentage);
                    lastPercentage = percentage;
                }
            }

            @Override
            public void onError(int id, final Exception ex) {
                if (id == uploadId) {
                    listener.onUpload(false, ex.getMessage());
                }
            }
        };
    }

    public interface UploaderListener {
        void onUpload(boolean success, String message);

        void onProgress(int percent);
    }
}
