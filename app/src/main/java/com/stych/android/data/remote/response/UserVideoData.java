package com.stych.android.data.remote.response;

import java.util.List;

public class UserVideoData {
    public UserData user;

    public static class UserData {
        public String id;
        public UsageData usage_data;

        @Override
        public String toString() {
            return "UserData{" +
                    "id='" + id + '\'' +
                    ", usage_data=" + usage_data +
                    '}';
        }
    }

    public static class UsageData {
        public GeneralData general;
        public List<ProductDataPurchase> product_data_purchases;
        public long data_limit_mb;

        public long getBytesPurchased() {
            if(data_limit_mb > 0) {
                return data_limit_mb;
            } else if(product_data_purchases != null) {
                long megabytes = 0;
                for (ProductDataPurchase purchase : product_data_purchases) {
                    megabytes += purchase.megabytes;
                }
                return megabytes;
            }
            return 250;
        }

        @Override
        public String toString() {
            return "UsageData{" +
                    "general=" + general +
                    ", product_data_purchases=" + product_data_purchases +
                    ", data_limit_mb=" + data_limit_mb +
                    '}';
        }
    }

    public static class ProductDataPurchase {
        public Float amount_paid;
        public String purchased_at;
        public long megabytes;

        @Override
        public String toString() {
            return "ProductDataPurchase{" +
                    "amount_paid=" + amount_paid +
                    ", purchased_at='" + purchased_at + '\'' +
                    ", megabytes=" + megabytes +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "UserVideoData{" +
                "user=" + user +
                '}';
    }
}
