package com.stych.android.data;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.stych.android.Constant;

public class UserData {
    private static final String TAG = "UserData";

    public String purchasePlan;
    public boolean tutorialShown;
    public long totalVideoSize;

    public UserData() {
        this.purchasePlan = Constant.FREE_TIRE_PRODUCT_ID;
        this.tutorialShown = false;
        this.totalVideoSize = 0;
    }

    public static UserData retrieve(Application application) {
        if (application == null) return new UserData();
        SharedPreferences preferences = application.getSharedPreferences(TAG, Context.MODE_PRIVATE);
        String token = preferences.getString("user_data", "{}");
        return new Gson().fromJson(token, UserData.class);
    }

    public static void clear(Application application) {
        if (application == null) return;
        SharedPreferences preferences = application.getSharedPreferences(TAG, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove("user_data");
        editor.commit();
    }

    public long getMaxVideoUploadLimit() {
        if(Constant.UNLIMITED_TIRE_PRODUCT_ID.equalsIgnoreCase(purchasePlan)) {
            return Constant.UNLIMITED_TIRE_MAX_BYTES_ALLOWED;
        } else if(Constant.PAID_TIRE_PRODUCT_ID.equalsIgnoreCase(purchasePlan)) {
            return Constant.PAID_TIRE_MAX_BYTES_ALLOWED;
        }
        return Constant.FREE_TIRE_MAX_BYTES_ALLOWED;
    }

    public boolean isSizeExceedThanAllowed(long size) {
        return (totalVideoSize + size ) > getMaxVideoUploadLimit();
    }

    public void addVideoSize(long size, Application application) {
        totalVideoSize += size;
        save(application);
    }

    public void save(Application application) {
        if (application == null) return;
        SharedPreferences preferences = application.getSharedPreferences(TAG, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("user_data", new Gson().toJson(this));
        editor.commit();
    }

    public String getSubscriptionPlanStorageText() {
        if(Constant.UNLIMITED_TIRE_PRODUCT_ID.equalsIgnoreCase(purchasePlan)) {
            return "Unlimited Storage";
        } else if(Constant.PAID_TIRE_PRODUCT_ID.equalsIgnoreCase(purchasePlan)) {
            return "35 GBs of Storage";
        }
        return "Free";
    }

    public String getSubscriptionPlanStorage() {
        if(Constant.UNLIMITED_TIRE_PRODUCT_ID.equalsIgnoreCase(purchasePlan)) {
            return "Unlimited Storage";
        } else if(Constant.PAID_TIRE_PRODUCT_ID.equalsIgnoreCase(purchasePlan)) {
            return "35 GB";
        }
        return "250 MB";
    }

    public String getLifeFileSize() {
        float kbs = totalVideoSize / 1024f;
        float mbs = kbs / 1024f;
        float gbs = mbs / 1024f;
        if (gbs >= 1) {
            mbs %= 1024;
            return (int) gbs + "." + ((int) (mbs / 100)) + "GB";
        } else if (mbs >= 1) {
            kbs %= 1024;
            return (int) mbs + "." + ((int) (kbs / 100)) + "MB";
        }
        return ((int) kbs) + "KB";
    }

    public void setTotalDataUsedMb(long megabytes) {
        this.totalVideoSize = megabytes * 1024 * 1024;
    }

    public void setPurchasePlan(long megabytesPurchased) {
        if(megabytesPurchased > (35 * 1024)) {
            this.purchasePlan = Constant.UNLIMITED_TIRE_PRODUCT_ID;
        } else if(megabytesPurchased > 250) {
            this.purchasePlan = Constant.PAID_TIRE_PRODUCT_ID;
        } else {
            this.purchasePlan = Constant.FREE_TIRE_PRODUCT_ID;
        }
    }

    @Override
    public String toString() {
        return "UserData{" +
                "purchasePlan='" + purchasePlan + '\'' +
                ", tutorialShown=" + tutorialShown +
                '}';
    }
}
