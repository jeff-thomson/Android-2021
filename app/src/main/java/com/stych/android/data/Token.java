package com.stych.android.data;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.stych.android.data.remote.response.Error;
import com.stych.android.util.EncryptionUtl;

public class Token {
    private static final String TAG = "Token";

    public String token;
    public boolean isLogin;
    public User user;
    public Error error;
    public String token_created_at;
    public Credentials credentials;

    public Token(String token) {
        this.token = token;
        this.user = new User();
    }

    public static Token retrieve(Application application) {
        if (application == null) return new Token("");
        SharedPreferences preferences = application.getSharedPreferences(TAG, Context.MODE_PRIVATE);
        String token = preferences.getString("token", "{}");
//        token = EncryptionUtl.decrypt(token);
        return new Gson().fromJson(token, Token.class);
    }

    public static void clear(Application application) {
        if (application == null) return;
        SharedPreferences preferences = application.getSharedPreferences(TAG, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove("token");
        editor.commit();
    }

    public boolean isValid() {
        return token != null && token.length() > 0;
    }

    public void save(Application application) {
        if (application == null) return;
        SharedPreferences preferences = application.getSharedPreferences(TAG, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        String token = new Gson().toJson(this);
//        token = EncryptionUtl.encrypt(token);
        editor.putString("token", token);
        editor.commit();
    }

    @Override
    public String toString() {
        return "Token{" +
                "token='" + token + '\'' +
                ", isLogin=" + isLogin +
                ", user=" + user +
                ", error=" + error +
                ", token_created_at='" + token_created_at + '\'' +
                ", credentials=" + credentials +
                '}';
    }
}
