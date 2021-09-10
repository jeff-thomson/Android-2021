package com.stych.android.data.remote.retrofit;

import com.stych.android.Constant;
import com.stych.android.data.Token;
import com.stych.android.data.remote.APIResponse;
import com.stych.android.data.remote.Repository;
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
import com.stych.android.start.forgotpassword.ForgotModel;
import com.stych.android.start.login.LoginModel;
import com.stych.android.start.signup.RegisterModel;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RepositoryV2Impl implements Repository {
    private final APIV2Interface apiInterface;

    @Inject
    public RepositoryV2Impl(APIV2Interface apiInterface) {
        this.apiInterface = apiInterface;
    }

    @Override
    public void register(RegisterModel registerModel, final ResponseListener listener) {
        Call<Token> call = apiInterface.register(Constant.CONTENT_TYPE_APPLICATION_JSON, registerModel.toUser());
        call.enqueue(new Callback<Token>() {
            @Override
            public void onResponse(Call<Token> call, Response<Token> response) {
                if (listener != null) {
                    if (!notifyErrorIfAny(response, listener)) {
                        APIResponse<Token> tokenAPIResponse = new APIResponse<Token>();
                        tokenAPIResponse.setData(response.body());
                        listener.onResponse(tokenAPIResponse);
                    }
                }
            }

            @Override
            public void onFailure(Call<Token> call, Throwable t) {
                notifyErrorResponse(t, listener);
            }
        });
    }

    @Override
    public void login(LoginModel loginModel, final ResponseListener listener) {
        Call<Token> call = apiInterface.login(loginModel.email, loginModel.password);
        call.enqueue(new Callback<Token>() {
            @Override
            public void onResponse(Call<Token> call, Response<Token> response) {
                if (listener != null) {
                    if (!notifyErrorIfAny(response, listener)) {
                        APIResponse<Token> tokenAPIResponse = new APIResponse<Token>();
                        tokenAPIResponse.setData(response.body());
                        listener.onResponse(tokenAPIResponse);
                    }
                }
            }

            @Override
            public void onFailure(Call<Token> call, Throwable t) {
                notifyErrorResponse(t, listener);
            }
        });
    }

    @Override
    public void changePassword(Token token, String currentPassword, String password, ResponseListener<BasicResponse> listener) {
        Call<BasicResponse> call = apiInterface.changePassword(Constant.CONTENT_TYPE_APPLICATION_JSON,
                token.token, token.user.id + "", currentPassword, password);
        call.enqueue(new Callback<BasicResponse>() {
            @Override
            public void onResponse(Call<BasicResponse> call, Response<BasicResponse> response) {
                if (!notifyErrorIfAny(response, listener)) {
                    APIResponse<BasicResponse> tokenAPIResponse = new APIResponse<BasicResponse>();
                    tokenAPIResponse.setData(response.body());
                    listener.onResponse(tokenAPIResponse);
                }
            }

            @Override
            public void onFailure(Call<BasicResponse> call, Throwable t) {
                notifyErrorResponse(t, listener);
            }
        });
    }

    @Override
    public void changeEmail(Token token, String new_email_address, ResponseListener<BasicResponse> listener) {
        Call<BasicResponse> call = apiInterface.changeEmail(Constant.CONTENT_TYPE_APPLICATION_JSON,
                token.token, token.user.id + "", new_email_address);
        call.enqueue(new Callback<BasicResponse>() {
            @Override
            public void onResponse(Call<BasicResponse> call, Response<BasicResponse> response) {
                if (!notifyErrorIfAny(response, listener)) {
                    APIResponse<BasicResponse> tokenAPIResponse = new APIResponse<BasicResponse>();
                    tokenAPIResponse.setData(response.body());
                    listener.onResponse(tokenAPIResponse);
                }
            }

            @Override
            public void onFailure(Call<BasicResponse> call, Throwable t) {
                notifyErrorResponse(t, listener);
            }
        });
    }

    @Override
    public void createLifeFile(Token token, String name, ResponseListener<BasicResponse> listener) {
        Call<BasicResponse> call = apiInterface.createLifeFile(Constant.CONTENT_TYPE_APPLICATION_JSON,
                token.token, token.user.id, new CreateUpdateLifeFile(name));
        call.enqueue(new Callback<BasicResponse>() {
            @Override
            public void onResponse(Call<BasicResponse> call, Response<BasicResponse> response) {
                if (!notifyErrorIfAny(response, listener)) {
                    APIResponse<BasicResponse> tokenAPIResponse = new APIResponse<BasicResponse>();
                    tokenAPIResponse.setData(response.body());
                    listener.onResponse(tokenAPIResponse);
                }
            }

            @Override
            public void onFailure(Call<BasicResponse> call, Throwable t) {
                notifyErrorResponse(t, listener);
            }
        });
    }

    @Override
    public void updateLifeFile(Token token, String life_file_id, String name, ResponseListener<BasicResponse> listener) {
        Call<BasicResponse> call = apiInterface.updateLifeFile(Constant.CONTENT_TYPE_APPLICATION_JSON,
                token.token, life_file_id, token.user.id, new CreateUpdateLifeFile(name));
        call.enqueue(new Callback<BasicResponse>() {
            @Override
            public void onResponse(Call<BasicResponse> call, Response<BasicResponse> response) {
                if (!notifyErrorIfAny(response, listener)) {
                    APIResponse<BasicResponse> tokenAPIResponse = new APIResponse<BasicResponse>();
                    tokenAPIResponse.setData(response.body());
                    listener.onResponse(tokenAPIResponse);
                }
            }

            @Override
            public void onFailure(Call<BasicResponse> call, Throwable t) {
                notifyErrorResponse(t, listener);
            }
        });
    }

    @Override
    public void deleteLifeFile(Token token, String life_file_id, ResponseListener<BasicResponse> listener) {
        Call<BasicResponse> call = apiInterface.deleteLifeFile(Constant.CONTENT_TYPE_APPLICATION_JSON,
                token.token, life_file_id, token.user.id);
        call.enqueue(new Callback<BasicResponse>() {
            @Override
            public void onResponse(Call<BasicResponse> call, Response<BasicResponse> response) {
                if (!notifyErrorIfAny(response, listener)) {
                    APIResponse<BasicResponse> tokenAPIResponse = new APIResponse<BasicResponse>();
                    tokenAPIResponse.setData(response.body());
                    listener.onResponse(tokenAPIResponse);
                }
            }

            @Override
            public void onFailure(Call<BasicResponse> call, Throwable t) {
                notifyErrorResponse(t, listener);
            }
        });
    }

    @Override
    public void transferLifeFile(Token token, String email, String life_id, ResponseListener<BasicResponse> listener) {
        Call<BasicResponse> call = apiInterface.transferLifeFile(Constant.CONTENT_TYPE_APPLICATION_JSON, token.token, token.user.id, email, life_id);
        call.enqueue(new Callback<BasicResponse>() {
            @Override
            public void onResponse(Call<BasicResponse> call, Response<BasicResponse> response) {
                if (!notifyErrorIfAny(response, listener)) {
                    APIResponse<BasicResponse> tokenAPIResponse = new APIResponse<BasicResponse>();
                    tokenAPIResponse.setData(response.body());
                    listener.onResponse(tokenAPIResponse);
                }
            }

            @Override
            public void onFailure(Call<BasicResponse> call, Throwable t) {
                notifyErrorResponse(t, listener);
            }
        });
    }

    @Override
    public void getLifeFileList(Token token, ResponseListener<GetLifeFilesResponse> listener) {
        Call<GetLifeFilesResponse> call = apiInterface.getLifeFiles(Constant.CONTENT_TYPE_APPLICATION_JSON, token.token, token.user.id);
        call.enqueue(new Callback<GetLifeFilesResponse>() {
            @Override
            public void onResponse(Call<GetLifeFilesResponse> call, Response<GetLifeFilesResponse> response) {
                if (!notifyErrorIfAny(response, listener)) {
                    APIResponse<GetLifeFilesResponse> tokenAPIResponse = new APIResponse<GetLifeFilesResponse>();
                    tokenAPIResponse.setData(response.body());
                    listener.onResponse(tokenAPIResponse);
                }
            }

            @Override
            public void onFailure(Call<GetLifeFilesResponse> call, Throwable t) {
                notifyErrorResponse(t, listener);
            }
        });
    }

    @Override
    public void getLifeFileVideoIds(Token token, String lifeFileId, ResponseListener<GetLifeFileVideoResponse> listener) {
        Call<GetLifeFileVideoResponse> call = apiInterface.getLifeFileVideoIDs(Constant.CONTENT_TYPE_APPLICATION_JSON, token.token, token.user.id, lifeFileId);
        call.enqueue(new Callback<GetLifeFileVideoResponse>() {
            @Override
            public void onResponse(Call<GetLifeFileVideoResponse> call, Response<GetLifeFileVideoResponse> response) {
                if (!notifyErrorIfAny(response, listener)) {
                    APIResponse<GetLifeFileVideoResponse> tokenAPIResponse = new APIResponse<GetLifeFileVideoResponse>();
                    tokenAPIResponse.setData(response.body());
                    listener.onResponse(tokenAPIResponse);
                }
            }

            @Override
            public void onFailure(Call<GetLifeFileVideoResponse> call, Throwable t) {
                notifyErrorResponse(t, listener);
            }
        });
    }

    @Override
    public void getLifeFileVideos(Token token, String lifeFileId, String startAt, String endAt, ResponseListener<GetLifeFileVideosResponse> listener) {
        Call<GetLifeFileVideosResponse> call = apiInterface.getLifeFileVideos(Constant.CONTENT_TYPE_APPLICATION_JSON, token.token, token.user.id, lifeFileId, startAt, endAt);
        call.enqueue(new Callback<GetLifeFileVideosResponse>() {
            @Override
            public void onResponse(Call<GetLifeFileVideosResponse> call, Response<GetLifeFileVideosResponse> response) {
                if (!notifyErrorIfAny(response, listener)) {
                    APIResponse<GetLifeFileVideosResponse> tokenAPIResponse = new APIResponse<GetLifeFileVideosResponse>();
                    tokenAPIResponse.setData(response.body());
                    listener.onResponse(tokenAPIResponse);
                }
            }

            @Override
            public void onFailure(Call<GetLifeFileVideosResponse> call, Throwable t) {
                notifyErrorResponse(t, listener);
            }
        });
    }

    @Override
    public void createVideoUploadRequest(Token token, String lifeFileId, VideoUploadRequest request, ResponseListener<VideoUploadResponse> listener) {
        Call<VideoUploadResponse> call = apiInterface.createVideoUploadRequest(Constant.CONTENT_TYPE_APPLICATION_JSON, token.token, token.user.id, request);
        call.enqueue(new Callback<VideoUploadResponse>() {
            @Override
            public void onResponse(Call<VideoUploadResponse> call, Response<VideoUploadResponse> response) {
                if (!notifyErrorIfAny(response, listener)) {
                    APIResponse<VideoUploadResponse> tokenAPIResponse = new APIResponse<VideoUploadResponse>();
                    tokenAPIResponse.setData(response.body());
                    listener.onResponse(tokenAPIResponse);
                }
            }

            @Override
            public void onFailure(Call<VideoUploadResponse> call, Throwable t) {
                notifyErrorResponse(t, listener);
            }
        });
    }

    @Override
    public void confirmVideoUploaded(Token token, String videoId, String uploadAt, VideoUploadRequest request, ResponseListener<BasicResponse> listener) {
        Call<BasicResponse> call = apiInterface.confirmVideoUpload(Constant.CONTENT_TYPE_APPLICATION_JSON, token.token, videoId, token.user.id, uploadAt, request);
        call.enqueue(new Callback<BasicResponse>() {
            @Override
            public void onResponse(Call<BasicResponse> call, Response<BasicResponse> response) {
                if (!notifyErrorIfAny(response, listener)) {
                    APIResponse<BasicResponse> tokenAPIResponse = new APIResponse<BasicResponse>();
                    tokenAPIResponse.setData(response.body());
                    listener.onResponse(tokenAPIResponse);
                }
            }

            @Override
            public void onFailure(Call<BasicResponse> call, Throwable t) {
                notifyErrorResponse(t, listener);
            }
        });
    }

    @Override
    public void deleteLifeFileVideo(Token token, String videoClipId, ResponseListener<BasicResponse> listener) {
        Call<BasicResponse> call = apiInterface.deleteLifeFileVideo(Constant.CONTENT_TYPE_APPLICATION_JSON, token.token, videoClipId, token.user.id);
        call.enqueue(new Callback<BasicResponse>() {
            @Override
            public void onResponse(Call<BasicResponse> call, Response<BasicResponse> response) {
                if (!notifyErrorIfAny(response, listener)) {
                    APIResponse<BasicResponse> tokenAPIResponse = new APIResponse<BasicResponse>();
                    tokenAPIResponse.setData(response.body());
                    listener.onResponse(tokenAPIResponse);
                }
            }

            @Override
            public void onFailure(Call<BasicResponse> call, Throwable t) {
                notifyErrorResponse(t, listener);
            }
        });
    }

    @Override
    public void logout(Token token, final ResponseListener listener) {
        Call<BasicResponse> call = apiInterface.logout(token.token, token.user.id + "");
        call.enqueue(new Callback<BasicResponse>() {
            @Override
            public void onResponse(Call<BasicResponse> call, Response<BasicResponse> response) {
                if (!notifyErrorIfAny(response, listener)) {
                    APIResponse<BasicResponse> tokenAPIResponse = new APIResponse<BasicResponse>();
                    tokenAPIResponse.setData(response.body());
                    listener.onResponse(tokenAPIResponse);
                }
            }

            @Override
            public void onFailure(Call<BasicResponse> call, Throwable t) {
                notifyErrorResponse(t, listener);
            }
        });
    }

    @Override
    public void emailVerificationCode(String email, ResponseListener listener) {
        Call<BasicResponse> call = apiInterface.emailVerificationCode(email);
        call.enqueue(new Callback<BasicResponse>() {
            @Override
            public void onResponse(Call<BasicResponse> call, Response<BasicResponse> response) {
                if (!notifyErrorIfAny(response, listener)) {
                    APIResponse<BasicResponse> tokenAPIResponse = new APIResponse<BasicResponse>();
                    tokenAPIResponse.setData(response.body());
                    listener.onResponse(tokenAPIResponse);
                }
            }

            @Override
            public void onFailure(Call<BasicResponse> call, Throwable t) {
                notifyErrorResponse(t, listener);
            }
        });
    }

    @Override
    public void sendVerificationEmail(String email, ResponseListener listener) {
        Call<BasicResponse> call = apiInterface.sendVerificationEmail(email);
        call.enqueue(new Callback<BasicResponse>() {
            @Override
            public void onResponse(Call<BasicResponse> call, Response<BasicResponse> response) {
                if (!notifyErrorIfAny(response, listener)) {
                    APIResponse<BasicResponse> apiResponse = new APIResponse<BasicResponse>();
                    apiResponse.setData(response.body());
                    listener.onResponse(apiResponse);
                }
            }

            @Override
            public void onFailure(Call<BasicResponse> call, Throwable t) {
                notifyErrorResponse(t, listener);
            }
        });
    }

    @Override
    public void passwordReset(ForgotModel forgotModel, ResponseListener listener) {
        Call<BasicResponse> call = apiInterface.passwordReset(Constant.CONTENT_TYPE_APPLICATION_JSON, forgotModel.email, forgotModel.passwordResetToken, new PasswordReset(forgotModel.password));
        call.enqueue(new Callback<BasicResponse>() {
            @Override
            public void onResponse(Call<BasicResponse> call, Response<BasicResponse> response) {
                if (!notifyErrorIfAny(response, listener)) {
                    APIResponse<BasicResponse> tokenAPIResponse = new APIResponse<BasicResponse>();
                    tokenAPIResponse.setData(response.body());
                    listener.onResponse(tokenAPIResponse);
                }
            }

            @Override
            public void onFailure(Call<BasicResponse> call, Throwable t) {
                notifyErrorResponse(t, listener);
            }
        });
    }

    @Override
    public void copyLifeFile(Token token, String lifeFileId, String toUserEmailAddr, ResponseListener<BasicResponse> listener) {
        Call<BasicResponse> call = apiInterface.copyLifeFile(Constant.CONTENT_TYPE_APPLICATION_JSON, token.token, lifeFileId, token.user.id, toUserEmailAddr);
        call.enqueue(new Callback<BasicResponse>() {
            @Override
            public void onResponse(Call<BasicResponse> call, Response<BasicResponse> response) {
                if (!notifyErrorIfAny(response, listener)) {
                    APIResponse<BasicResponse> tokenAPIResponse = new APIResponse<BasicResponse>();
                    tokenAPIResponse.setData(response.body());
                    listener.onResponse(tokenAPIResponse);
                }
            }

            @Override
            public void onFailure(Call<BasicResponse> call, Throwable t) {
                notifyErrorResponse(t, listener);
            }
        });
    }

    @Override
    public void getLifeFileHls(Token token, String lifeFileId, ResponseListener<BasicResponse> listener) {
        Call<BasicResponse> call = apiInterface.getLifeFileHls(Constant.CONTENT_TYPE_APPLICATION_JSON, token.token, token.user.id, lifeFileId);
        call.enqueue(new Callback<BasicResponse>() {
            @Override
            public void onResponse(Call<BasicResponse> call, Response<BasicResponse> response) {
                if (!notifyErrorIfAny(response, listener)) {
                    APIResponse<BasicResponse> tokenAPIResponse = new APIResponse<BasicResponse>();
                    tokenAPIResponse.setData(response.body());
                    listener.onResponse(tokenAPIResponse);
                }
            }

            @Override
            public void onFailure(Call<BasicResponse> call, Throwable t) {
                notifyErrorResponse(t, listener);
            }
        });
    }

    @Override
    public void getUsageData(Token token, ResponseListener<UserVideoData> listener) {
        Call<UserVideoData> call = apiInterface.getUsageData(Constant.CONTENT_TYPE_APPLICATION_JSON, token.token, token.user.id);
        call.enqueue(new Callback<UserVideoData>() {
            @Override
            public void onResponse(Call<UserVideoData> call, Response<UserVideoData> response) {
                if (!notifyErrorIfAny(response, listener)) {
                    APIResponse<UserVideoData> tokenAPIResponse = new APIResponse<UserVideoData>();
                    tokenAPIResponse.setData(response.body());
                    listener.onResponse(tokenAPIResponse);
                }
            }

            @Override
            public void onFailure(Call<UserVideoData> call, Throwable t) {
                notifyErrorResponse(t, listener);
            }
        });
    }

    @Override
    public void setPurchaseData(Token token, PurchaseData data, ResponseListener<BasicResponse> listener) {
        Call<BasicResponse> call = apiInterface.setPurchaseData(Constant.CONTENT_TYPE_APPLICATION_JSON, token.token, token.user.id, data);
        call.enqueue(new Callback<BasicResponse>() {
            @Override
            public void onResponse(Call<BasicResponse> call, Response<BasicResponse> response) {
                if (!notifyErrorIfAny(response, listener)) {
                    APIResponse<BasicResponse> tokenAPIResponse = new APIResponse<BasicResponse>();
                    tokenAPIResponse.setData(response.body());
                    listener.onResponse(tokenAPIResponse);
                }
            }

            @Override
            public void onFailure(Call<BasicResponse> call, Throwable t) {
                notifyErrorResponse(t, listener);
            }
        });
    }

    private boolean notifyErrorIfAny(Response response, ResponseListener listener) {
        if (response == null || !response.isSuccessful()) {
            if (listener != null) {
                listener.onResponse(APIResponse.createDefaultErrorResponse());
            }
            return true;
        }
        return false;
    }

    private void notifyErrorResponse(Throwable throwable, ResponseListener listener) {
        if (listener != null) {
            listener.onResponse(APIResponse.createErrorResponse(throwable));
        }
    }
}
