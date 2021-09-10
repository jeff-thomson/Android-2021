package com.stych.android.data.remote.retrofit;


import com.stych.android.data.Token;
import com.stych.android.data.User;
import com.stych.android.data.remote.response.BasicResponse;
import com.stych.android.data.remote.response.CreateVideoUploadResponse;
import com.stych.android.data.remote.response.GetLifeFileVideoResponse;
import com.stych.android.data.remote.response.GetLifeFileVideosResponse;
import com.stych.android.data.remote.response.GetLifeFilesResponse;
import com.stych.android.data.remote.response.LifeFileResponse;
import com.stych.android.data.request.PasswordReset;
import com.stych.android.data.request.VideoMetadata;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface APIInterface {
    @POST("/users")
    Call<Token> register(@Header("Content-Type") String content_type, @Body User user);

    @POST("/users/{email}/log_in")
    Call<Token> login(@Path("email") String email, @Query("userPassword") String password);

    @PUT("/users/{user_id}")
    Call<BasicResponse> changePassword(@Header("Content-Type") String content_type, @Header("Authorization") String authorization,
                                       @Path("user_id") String user_id, @Query("current_user_password") String current_user_password, @Body String password);

    @PUT("/users/{user_id}/email_address/{new_email_address}")
    Call<BasicResponse> changeEmail(@Header("Content-Type") String content_type, @Header("Authorization") String authorization,
                                    @Path("user_id") String user_id, @Path("new_email_address") String new_email_address);


    @POST("/users/{user_id}/log_out")
    Call<BasicResponse> logout(@Header("Authorization") String authorization, @Path("user_id") String user_id);

    // reset Password
    @GET("/verification/phone_number_and_email_address_codes/{email}")
    Call<BasicResponse> emailVerificationCode(@Path("email") String email);

    @POST("/users/{email}/password_reset")
    Call<BasicResponse> passwordReset(
            @Header("Content-Type") String content_type,
            @Path("email") String email,
            @Query("verification_code") String verificationCode,
            @Body PasswordReset passwordReset);

    // main app apis

    @POST("/life_files/{user_id}")
    Call<BasicResponse> createLife(@Header("Content-Type") String content_type, @Header("Authorization") String authorization,
                                   @Path("user_id") String user_id, @Body LifeFileResponse title);

    @PUT("/life_files/{life_file_id}")
    Call<BasicResponse> updateLife(@Header("Content-Type") String content_type, @Header("Authorization") String authorization,
                                   @Path("life_file_id") String life_file_id, @Query("user_id") String user_id, @Body LifeFileResponse title);

    @DELETE("/life_files/{user_id}/{life_file_id}")
    Call<BasicResponse> deleteLife(@Header("Content-Type") String content_type, @Header("Authorization") String authorization,
                                   @Path("life_file_id") String life_file_id, @Path("user_id") String user_id);


    @POST("/life_files/{fromUserID}/{email}/{lifeFileID}")
    Call<BasicResponse> transferFile(@Header("Content-Type") String content_type,
                                     @Header("Authorization") String authorization,
                                     @Path("fromUserID") String fromUserID,
                                     @Path("email") String email,
                                     @Path("lifeFileID") String lifeFileID);

    @GET("/life_files/{user_id}")
    Call<GetLifeFilesResponse> getLifeFiles(@Header("Content-Type") String content_type, @Header("Authorization") String authorization, @Path("user_id") String userId);

    // life file videos

    @GET("/life_files/video_ids/{user_id}/{lifeFileID}")
    Call<GetLifeFileVideoResponse> getLifeFileVideoIDs(@Header("Content-Type") String content_type,
                                                       @Header("Authorization") String authorization,
                                                       @Path("user_id") String userId,
                                                       @Path("lifeFileID") String lifeFileID);


    @GET("/life_files/videos/{userID}/{lifeFileID}")
    Call<GetLifeFileVideosResponse> getLifeFileVideos(@Header("Content-Type") String content_type,
                                                      @Header("Authorization") String authorization,
                                                      @Path("userID") String userId,
                                                      @Path("lifeFileID") String lifeFileID,
                                                      @Query("start_at") String start_at,
                                                      @Query("end_at") String end_at);

    @POST("/life_files/videos/requests")
    Call<CreateVideoUploadResponse> createVideoUpload(@Header("Content-Type") String content_type,
                                                      @Header("Authorization") String authorization,
                                                      @Query("user_id") String user_id,
                                                      @Body VideoMetadata videoMetadata);

    @POST("/life_files/videos/requests/{video_id}")
    Call<BasicResponse> confirmVideoUpload(@Header("Content-Type") String content_type,
                                           @Header("Authorization") String authorization,
                                           @Path("video_id") String video_id,
                                           @Query("user_id") String user_id,
                                           @Query("uploaded_at") String uploaded_at);

    @DELETE("/life_files/videos/{user_id}/{video_clip_id}")
    Call<BasicResponse> deleteVideoClip(@Header("Content-Type") String content_type,
                                        @Header("Authorization") String authorization,
                                        @Path("user_id") String user_id,
                                        @Path("video_clip_id") String video_clip_id);
}
