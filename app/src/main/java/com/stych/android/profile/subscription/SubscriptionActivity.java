package com.stych.android.profile.subscription;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.billingclient.api.AcknowledgePurchaseParams;
import com.android.billingclient.api.AcknowledgePurchaseResponseListener;
import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.SkuDetails;
import com.android.billingclient.api.SkuDetailsParams;
import com.android.billingclient.api.SkuDetailsResponseListener;
import com.stych.android.BaseActivity;
import com.stych.android.Constant;
import com.stych.android.R;
import com.stych.android.data.UserData;
import com.stych.android.dialog.OKDialog;
import com.stych.android.profile.ProfileViewModel;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import dagger.android.AndroidInjection;

public class SubscriptionActivity extends BaseActivity implements PurchasesUpdatedListener {
    private final String TAG = SubscriptionActivity.class.getSimpleName();

    @Inject
    ProfileViewModel viewModel;

    private RelativeLayout freeTireLl, paidTireLl, unlimitedTireLl;
    private TextView freeTv, paidTv, unlimitedTv;
    private CheckBox freeCb, paidCb, unlimitedCb;

    private BillingClient billingClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscription);
        AndroidInjection.inject(this);

        initViews();

        setupBillingClient();
    }

    private void initViews() {
        freeTireLl = findViewById(R.id.freeTireLl);
        freeTv = findViewById(R.id.freeTv);
        freeCb = findViewById(R.id.freeCb);
        paidTireLl = findViewById(R.id.paidTireLl);
        paidTv = findViewById(R.id.paidTv);
        paidCb = findViewById(R.id.paidCb);
        unlimitedTireLl = findViewById(R.id.unlimitedTireLl);
        unlimitedTv = findViewById(R.id.unlimitedTv);
        unlimitedCb = findViewById(R.id.unlimitedCb);

        findViewById(R.id.btnRestorePurchase).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_OK);
                finish();
            }
        });

        freeCb.setClickable(false);
        paidCb.setClickable(false);
        unlimitedCb.setClickable(false);
    }

    private void resetProductVisibility() {
        freeCb.setChecked(true);

        paidTireLl.setVisibility(View.GONE);
        paidCb.setChecked(false);
        unlimitedTireLl.setVisibility(View.GONE);
        unlimitedCb.setChecked(false);
    }

    private void showPaidTireProduct(SkuDetails details) {
        Log.i(TAG, "showPaidTireProduct found " + (details != null ? details.toString() : details));
        paidTireLl.setVisibility(View.VISIBLE);
        paidTv.setText(details.getPrice() + "/month");
        paidTireLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BillingFlowParams billingFlowParams = BillingFlowParams.newBuilder().setSkuDetails(details).build();
                billingClient.launchBillingFlow(SubscriptionActivity.this, billingFlowParams);
            }
        });
    }

    private void showUnlimitedTireProduct(SkuDetails details) {
        Log.i(TAG, "showUnlimitedTireProduct found " + (details != null ? details.toString() : details));
        unlimitedTireLl.setVisibility(View.VISIBLE);
        unlimitedTv.setText(details.getPrice() + "/month");
        unlimitedTireLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BillingFlowParams billingFlowParams = BillingFlowParams.newBuilder().setSkuDetails(details).build();
                billingClient.launchBillingFlow(SubscriptionActivity.this, billingFlowParams);
            }
        });
    }

    private void setSetPurchasedProduct(String productId) {
        freeCb.setChecked(false);
        paidCb.setChecked(false);
        unlimitedCb.setChecked(false);

        UserData userData = UserData.retrieve(getApplication());
        if (Constant.PAID_TIRE_PRODUCT_ID.equalsIgnoreCase(productId)) {
            paidCb.setChecked(true);
            paidTireLl.setOnClickListener(null);
            userData.purchasePlan = Constant.PAID_TIRE_PRODUCT_ID;
        } else if (Constant.UNLIMITED_TIRE_PRODUCT_ID.equalsIgnoreCase(productId)) {
            unlimitedCb.setChecked(true);
            unlimitedTireLl.setOnClickListener(null);
            userData.purchasePlan = Constant.UNLIMITED_TIRE_PRODUCT_ID;
        } else {
            freeCb.setChecked(true);
            userData.purchasePlan = Constant.FREE_TIRE_PRODUCT_ID;
        }
        userData.save(getApplication());
    }

    private void setupBillingClient() {
        billingClient = BillingClient.newBuilder(this)
                .enablePendingPurchases()
                .setListener(this)
                .build();

        billingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingSetupFinished(@NonNull BillingResult billingResult) {
                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                    loadAllSKUs();
                }
            }

            @Override
            public void onBillingServiceDisconnected() {
            }
        });
    }

    private void loadAllSKUs() {
        if (billingClient.isReady()) {
            loadPurchaseHistory();

            SkuDetailsParams params = SkuDetailsParams.newBuilder()
                    .setSkusList((Arrays.asList(Constant.PAID_TIRE_PRODUCT_ID, Constant.UNLIMITED_TIRE_PRODUCT_ID)))
                    .setType(BillingClient.SkuType.SUBS)
                    .build();
            billingClient.querySkuDetailsAsync(params, new SkuDetailsResponseListener() {
                @Override
                public void onSkuDetailsResponse(@NonNull BillingResult billingResult, @Nullable List<SkuDetails> skuDetailsList) {
                    Log.i(TAG, "onSkuDetailsResponse found " + (skuDetailsList != null ? skuDetailsList.size() : 0));
                    if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK && !skuDetailsList.isEmpty()) {
                        resetProductVisibility();
                        for (int i = 0; i < skuDetailsList.size(); i++) {
                            if (Constant.PAID_TIRE_PRODUCT_ID.equalsIgnoreCase(skuDetailsList.get(i).getSku())) {
                                showPaidTireProduct(skuDetailsList.get(i));
                            } else if (Constant.UNLIMITED_TIRE_PRODUCT_ID.equalsIgnoreCase(skuDetailsList.get(i).getSku())) {
                                showUnlimitedTireProduct(skuDetailsList.get(i));
                            }
                        }
                    }
                }
            });
        } else {
            toast("Billing engine not ready!");
        }
    }

    private void loadPurchaseHistory() {
        Purchase.PurchasesResult result = billingClient.queryPurchases(BillingClient.SkuType.SUBS);
        List<Purchase> purchases = result.getPurchasesList();
        if(purchases != null && !purchases.isEmpty()) {
            for (Purchase purchase : purchases) {
                setSetPurchasedProduct(purchase.getSku());
            }
        }
    }

    @Override
    public void onPurchasesUpdated(@NonNull BillingResult billingResult, @Nullable List<Purchase> list) {
        loadPurchaseHistory();
        if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK && list != null) {
            for (int i = 0; i < list.size(); i++) {
                Purchase purchase = list.get(i);
                acknowledgePurchase(purchase.getPurchaseToken());
                viewModel.updatePurchaseData(purchase.getSku());
            }
        } else if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.USER_CANCELED) {
            // Handle an error caused by a user cancelling the purchase flow.
            OKDialog.show(this, getString(R.string.subscribe_cancel_msg), null);
        } else {
            // Handle any other error codes.
            OKDialog.show(this, getString(R.string.subscribe_error_msg), null);
        }
    }

    private void acknowledgePurchase(String purchaseToken) {
        AcknowledgePurchaseParams params = AcknowledgePurchaseParams.newBuilder()
                .setPurchaseToken(purchaseToken)
                .build();
        billingClient.acknowledgePurchase(params, new AcknowledgePurchaseResponseListener() {
            @Override
            public void onAcknowledgePurchaseResponse(@NonNull BillingResult billingResult) {
                int responseCode = billingResult.getResponseCode();
                String debugMessage = billingResult.getDebugMessage();
                loadPurchaseHistory();
            }
        });
    }
}