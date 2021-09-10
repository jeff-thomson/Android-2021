package com.stych.android.data.request;

public class PurchaseData {
    public long megabytes;
    public float amount_paid;

    private PurchaseData(long megabytes, float amount_paid) {
        this.megabytes = megabytes;
        this.amount_paid = amount_paid;
    }

    public static PurchaseData newFreePurchase() {
        long megabytes = 260;
        return new PurchaseData(megabytes, 0.1f);
    }

    public static PurchaseData newPaidPurchase(String amountPaid) {
        long megabytes = 35 * 1024;
        float price = PurchaseData.parseFloat(amountPaid);
        return new PurchaseData(megabytes, price);
    }

    public static PurchaseData newUnlimitedPurchase(String amountPaid) {
        long megabytes = 35 * 1024 * 100;
        float price = PurchaseData.parseFloat(amountPaid);
        return new PurchaseData(megabytes, price);
    }

    public static float parseFloat(String s) {
        if (s != null && (s = s.trim()).length() > 0) {
            try {
                return Float.parseFloat(s);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return 0;
    }

    @Override
    public String toString() {
        return "PurchaseData{" +
                "megabytes=" + megabytes +
                ", amount_paid=" + amount_paid +
                '}';
    }
}
