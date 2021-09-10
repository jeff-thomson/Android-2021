package com.stych.android.data.remote.retrofit;


import com.stych.android.data.Token;
import com.stych.android.data.User;
import com.stych.android.data.remote.response.BasicResponse;
import com.stych.android.data.remote.response.GetLifeFileVideoResponse;
import com.stych.android.data.remote.response.GetLifeFileVideosResponse;
import com.stych.android.data.remote.response.GetLifeFilesResponse;
import com.stych.android.data.remote.response.UserVideoData;
import com.stych.android.data.remote.response.VideoUploadResponse;
import com.stych.android.data.request.CreateUpdateLifeFile;
import com.stych.android.data.request.PasswordReset;
import com.stych.android.data.request.PurchaseData;
import com.stych.android.data.request.VideoUploadRequest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface APIV2Interface {

    @POST("/users")
    Call<Token> register(@Header("Content-Type") String contentType, @Body User user);

    @POST("/users/{email}/log_in")
    Call<Token> login(@Path("email") String email,
                      @Query("userPassword") String password);

    @POST("users/{userId}/is_verified")
    Call<Token> checkIfVerified(@Path("userId") String userId);

    @PUT("/users/{userId}")
    Call<BasicResponse> changePassword(@Header("Content-Type") String contentType,
                                       @Header("Authorization") String authorization,
                                       @Path("userId") String userId,
                                       @Query("current_user_password") String currentUserPassword,
                                       @Body String password);

    @PUT("/users/{userId}/email_address/{new_email_address}")
    Call<BasicResponse> changeEmail(@Header("Content-Type") String contentType, @Header("Authorization") String authorization,
                                    @Path("userId") String userId, @Path("new_email_address") String newEmailAddress);


    @POST("/users/{userId}/log_out")
    Call<BasicResponse> logout(@Header("Authorization") String authorization, @Path("userId") String userId);

    @GET("/verification/phone_number_and_email_address_codes/{email}")
    Call<BasicResponse> emailVerificationCode(@Path("email") String email);

    @GET("/verification/phone_number_and_email_address_tokens/{email}")
    Call<BasicResponse> sendVerificationEmail(@Path("email") String email);

    @POST("/users/{email}/password_reset")
    Call<BasicResponse> passwordReset(
            @Header("Content-Type") String contentType,
            @Path("email") String email,
            @Query("verification_code") String verificationCode,
            @Body PasswordReset passwordReset);

    @POST("/life_files/{userId}")
    Call<BasicResponse> createLifeFile(@Header("Content-Type") String contentType, @Header("Authorization") String authorization,
                                       @Path("userId") String userId, @Body CreateUpdateLifeFile lifeFile);

    @PUT("/life_files/{lifeFileId}/{userId}")
    Call<BasicResponse> updateLifeFile(@Header("Content-Type") String contentType, @Header("Authorization") String authorization,
                                       @Path("lifeFileId") String lifeFileId, @Path("userId") String userId, @Body CreateUpdateLifeFile lifeFile);

    @DELETE("/life_files/{lifeFileId}/{userId}")
    Call<BasicResponse> deleteLifeFile(@Header("Content-Type") String contentType, @Header("Authorization") String authorization,
                                       @Path("lifeFileId") String lifeFileId, @Path("userId") String userId);

    @POST("/life_files/{fromUserID}/{email}/{lifeFileID}")
    Call<BasicResponse> transferLifeFile(@Header("Content-Type") String contentType,
                                         @Header("Authorization") String authorization,
                                         @Path("fromUserID") String fromUserID,
                                         @Path("email") String email,
                                         @Path("lifeFileID") String lifeFileID);

    @GET("/life_files/{lifeFileId}/{userId}")
    Call<GetLifeFilesResponse> getLifeFile(@Header("Content-Type") String contentType, @Header("Authorization") String authorization, @Path("lifeFileId") String lifeFileId, @Path("userId") String userId);


    @GET("/life_files/{userId}")
    Call<GetLifeFilesResponse> getLifeFiles(@Header("Content-Type") String contentType, @Header("Authorization") String authorization, @Path("userId") String userId);

    @GET("/life_files/{lifeFileId}/video_ids/{userId}")
    Call<GetLifeFileVideoResponse> getLifeFileVideoIDs(@Header("Content-Type") String contentType,
                                                       @Header("Authorization") String authorization,
                                                       @Path("userId") String userId,
                                                       @Path("lifeFileId") String lifeFileId);

    @GET("/life_files/{lifeFileId}/videos/{userId}")
    Call<GetLifeFileVideosResponse> getLifeFileVideos(@Header("Content-Type") String contentType,
                                                      @Header("Authorization") String authorization,
                                                      @Path("userId") String userId,
                                                      @Path("lifeFileId") String lifeFileId,
                                                      @Query("start_at") String startAt,
                                                      @Query("end_at") String endAt);

    @GET("/hls/life_files/{lifeFileId}/{userId}/master.m3u8")
    Call<BasicResponse> getLifeFileHls(@Header("Content-Type") String contentType,
                                                   @Header("Authorization") String authorization,
                                                   @Path("userId") String userId,
                                                   @Path("lifeFileId") String lifeFileId);

    @GET("/hls/life_files/videos/{videoId}/{userId}/master.m3u8")
    Call<GetLifeFileVideosResponse> getLifeFileVideoHls(@Header("Content-Type") String contentType,
                                                        @Header("Authorization") String authorization,
                                                        @Path("userId") String userId,
                                                        @Path("videoId") String videoId);

    @GET("/life_files/{lifeFileId}/{fromUserId}/{toUserEmailAddr}")
    Call<BasicResponse> copyLifeFile(@Header("Content-Type") String contentType,
                                                 @Header("Authorization") String authorization,
                                                 @Path("lifeFileId") String lifeFileId,
                                                 @Path("fromUserId") String fromUserId,
                                                 @Path("toUserEmailAddr") String toUserEmailAddr);

    @GET("/life_files/{lifeFileId}/mp4/{userId}")
    Call<GetLifeFileVideosResponse> downloadLifeFileMp4(@Header("Content-Type") String contentType,
                                                        @Header("Authorization") String authorization,
                                                        @Path("lifeFileId") String lifeFileId,
                                                        @Path("userId") String userId);

    @GET("/life_files/videos/{lifeFileVideoId}/mp4/{userId}")
    Call<GetLifeFileVideosResponse> downloadLifeFileVideoMp4(@Header("Content-Type") String contentType,
                                                             @Header("Authorization") String authorization,
                                                             @Path("lifeFileVideoId") String lifeFileVideoId,
                                                             @Path("userId") String userId);

    @POST("/life_files/videos/requests/{userId}")
    Call<VideoUploadResponse> createVideoUploadRequest(@Header("Content-Type") String contentType,
                                                       @Header("Authorization") String authorization,
                                                       @Path("userId") String userId, @Body VideoUploadRequest request);

    @POST("/life_files/videos/requests/{videoId}")
    Call<BasicResponse> confirmVideoUpload(@Header("Content-Type") String contentType,
                                           @Header("Authorization") String authorization,
                                           @Path("videoId") String videoId,
                                           @Query("user_id") String userId,
                                           @Query("uploaded_at") String uploadedAt, @Body VideoUploadRequest request);

    @DELETE("/life_files/videos/{lifeFileVideoId}/{userId}")
    Call<BasicResponse> deleteLifeFileVideo(@Header("Content-Type") String contentType,
                                            @Header("Authorization") String authorization,
                                            @Path("lifeFileVideoId") String lifeFileVideoId,
                                            @Path("userId") String userId);

    @GET("/users/{userId}/usage_data")
    Call<UserVideoData> getUsageData(@Header("Content-Type") String contentType,
                                     @Header("Authorization") String authorization,
                                     @Path("userId") String userId);

    @POST("/users/{userId}/user_purchases/data")
    Call<BasicResponse> setPurchaseData(@Header("Content-Type") String contentType,
                                     @Header("Authorization") String authorization,
                                     @Path("userId") String userId, @Body PurchaseData data);
}
