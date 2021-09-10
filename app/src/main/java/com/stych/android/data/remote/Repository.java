package com.stych.android.data.remote;


import com.stych.android.data.Token;
import com.stych.android.data.UserData;
import com.stych.android.data.remote.response.BasicResponse;
import com.stych.android.data.remote.response.GetLifeFileVideoResponse;
import com.stych.android.data.remote.response.GetLifeFileVideosResponse;
import com.stych.android.data.remote.response.GetLifeFilesResponse;
import com.stych.android.data.remote.response.UserVideoData;
import com.stych.android.data.remote.response.VideoUploadResponse;
import com.stych.android.data.request.PurchaseData;
import com.stych.android.data.request.VideoUploadRequest;
import com.stych.android.start.forgotpassword.ForgotModel;
import com.stych.android.start.login.LoginModel;
import com.stych.android.start.signup.RegisterModel;

import javax.inject.Singleton;

@Singleton
public interface Repository {
    void register(RegisterModel registerModel, ResponseListener<Token> listener);

    void login(LoginModel loginModel, ResponseListener<Token> listener);

    void changePassword(Token token, String currentPassword, String password, ResponseListener<BasicResponse> listener);

    void changeEmail(Token token, String new_email_address, ResponseListener<BasicResponse> listener);

    void createLifeFile(Token token, String name, ResponseListener<BasicResponse> listener);

    void updateLifeFile(Token token, String life_file_id, String name, ResponseListener<BasicResponse> listener);

    void deleteLifeFile(Token token, String life_file_id, ResponseListener<BasicResponse> listener);

    void transferLifeFile(Token token, String email, String life_id, ResponseListener<BasicResponse> listener);

    void getLifeFileList(Token token, ResponseListener<GetLifeFilesResponse> listener);

    void getLifeFileVideoIds(Token token, String lifeFileId, ResponseListener<GetLifeFileVideoResponse> listener);

    void getLifeFileVideos(Token token, String lifeFileId, String startAt, String endAt, ResponseListener<GetLifeFileVideosResponse> listener);

    void createVideoUploadRequest(Token token, String lifeFileId, VideoUploadRequest request, ResponseListener<VideoUploadResponse> listener);

    void confirmVideoUploaded(Token token, String videoId, String uploadAt, VideoUploadRequest request, ResponseListener<BasicResponse> listener);

    void deleteLifeFileVideo(Token token, String lifeFileVideoId, ResponseListener<BasicResponse> listener);

    void logout(Token token, ResponseListener listener);

    void emailVerificationCode(String email, ResponseListener listener);

    void sendVerificationEmail(String email, ResponseListener listener);

    void passwordReset(ForgotModel forgotModel, ResponseListener listener);

    void copyLifeFile(Token token, String lifeFileId, String toUserEmailAddr, ResponseListener<BasicResponse> listener);

    void getLifeFileHls(Token token, String lifeFileId, ResponseListener<BasicResponse> listener);

    void getUsageData(Token token, ResponseListener<UserVideoData> listener);

    void setPurchaseData(Token token, PurchaseData data, ResponseListener<BasicResponse> listener);

    interface ResponseListener<T> {
        void onResponse(APIResponse<T> apiResponse);
    }
}
