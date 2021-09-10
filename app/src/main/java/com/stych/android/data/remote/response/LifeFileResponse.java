package com.stych.android.data.remote.response;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;

import java.util.Objects;

public class LifeFileResponse {
    private static final String TAG = "LifeFileResponse";
    public String life_file_id;
    public String name;
    public String created_at;
    public String deleted_at;

    public LifeFileResponse(String s) {
        life_file_id = s;
        name = s;
    }

    public LifeFileResponse() {
    }

    public static LifeFileResponse retrieve(Context application) {
        if (application == null) return new LifeFileResponse("");
        SharedPreferences preferences = application.getSharedPreferences(TAG, Context.MODE_PRIVATE);
        String token = preferences.getString("life_stored", "");
        return new Gson().fromJson(token, LifeFileResponse.class);
    }

    public static void clear(Context application) {
        if (application == null) return;
        Log.d(TAG, "clear");
        SharedPreferences preferences = application.getSharedPreferences(TAG, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove("life_stored");
        editor.commit();
    }

    public void save(Context application) {
        if (application == null) return;
        SharedPreferences preferences = application.getSharedPreferences(TAG, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("life_stored", new Gson().toJson(this));
        editor.commit();
        Log.d(TAG, "save");
    }

    public int getIDHash() {
        return life_file_id.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (life_file_id == null) return false;
        if (o == null || getClass() != o.getClass()) return false;
        LifeFileResponse that = (LifeFileResponse) o;
        return life_file_id.equals(that.life_file_id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(life_file_id);
    }

    @Override
    public String toString() {
        return "LifeFileResponse{" +
                "life_file_id='" + life_file_id + '\'' +
                ", name='" + name + '\'' +
                ", created_at='" + created_at + '\'' +
                ", deleted_at='" + deleted_at + '\'' +
                '}';
    }
}
