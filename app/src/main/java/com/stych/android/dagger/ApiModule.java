package com.stych.android.dagger;

import android.app.Application;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.stych.android.BuildConfig;
import com.stych.android.data.remote.Repository;
import com.stych.android.data.remote.retrofit.APIInterface;
import com.stych.android.data.remote.retrofit.APIV2Interface;
import com.stych.android.data.remote.retrofit.RepositoryV2Impl;

import java.io.File;
import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class ApiModule {
    @Provides
    @Singleton
    Gson provideGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat("yyyy-MM-dd HH:mm:ss");
        return gsonBuilder.create();
    }

    @Provides
    @Singleton
    Cache provideCache(Application application) {
        long cacheSize = 10 * 1024 * 1024; // 10 MB
        File httpCacheDirectory = new File(application.getCacheDir(), "http-cache");
        return new Cache(httpCacheDirectory, cacheSize);
    }

    @Provides
    @Singleton
    OkHttpClient provideOkHttpClient(Cache cache) {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.cache(cache);
        httpClient.addInterceptor(logging);
        //httpClient.addNetworkInterceptor(new RequestInterceptor());
        httpClient.connectTimeout(30, TimeUnit.SECONDS);
        httpClient.readTimeout(30, TimeUnit.SECONDS);
        return httpClient.build();
    }

    @Provides
    @Singleton
    Retrofit provideRetrofit(Gson gson, OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(BuildConfig.BASE_URL)
                .client(okHttpClient)
                .build();
    }

    @Provides
    @Singleton
    APIInterface provideAPIInterface(Retrofit retrofit) {
        return retrofit.create(APIInterface.class);
    }

    @Provides
    @Singleton
    APIV2Interface provideAPIV2Interface(Retrofit retrofit) {
        return retrofit.create(APIV2Interface.class);
    }

    @Provides
    @Singleton
    Repository provideRepositoryV2(APIV2Interface apiInterface) {
        return new RepositoryV2Impl(apiInterface);
    }
}
