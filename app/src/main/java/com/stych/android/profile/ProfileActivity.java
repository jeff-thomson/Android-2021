package com.stych.android.profile;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatButton;
import androidx.lifecycle.Observer;

import com.stych.android.BaseActivity;
import com.stych.android.Constant;
import com.stych.android.LifeFileCache;
import com.stych.android.R;
import com.stych.android.WebViewActivity;
import com.stych.android.data.Token;
import com.stych.android.data.UserData;
import com.stych.android.data.remote.response.LifeFileResponse;
import com.stych.android.profile.changeemail.ChangeEmailActivity;
import com.stych.android.profile.changepassword.ChangePasswordActivity;
import com.stych.android.profile.contactus.ContactUsActivity;
import com.stych.android.profile.subscription.SubscriptionActivity;
import com.stych.android.util.UIUtil;

import javax.inject.Inject;

import dagger.android.AndroidInjection;

public class ProfileActivity extends BaseActivity implements View.OnClickListener {

    @Inject
    ProfileViewModel viewModel;
    private TextView emailUpdateTv, passwordUpdateTv, subscriptionUpdateTv, termsPrivacyTv, profileEmailTv, contactUsTv;
    private TextView lifeFileSizeTv, subscriptionTv, subscriptionLevelTv;
    private AppCompatButton btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        AndroidInjection.inject(this);
        defaultHeaderInitiate(getResources().getString(R.string.profile));
        initiate();
        observer();
    }

    private void initiate() {
        emailUpdateTv = findViewById(R.id.emailUpdateTv);
        passwordUpdateTv = findViewById(R.id.passwordUpdateTv);
        subscriptionUpdateTv = findViewById(R.id.subscriptionUpdate);
        termsPrivacyTv = findViewById(R.id.termsPrivacy);
        btnLogout = findViewById(R.id.btnLogout);
        profileEmailTv = findViewById(R.id.profileEmailTv);
        contactUsTv = findViewById(R.id.contactUs);
        lifeFileSizeTv = findViewById(R.id.lifeFileSizeTv);
        subscriptionTv = findViewById(R.id.subscriptionTv);
        subscriptionLevelTv = findViewById(R.id.subscriptionLevelTv);

        emailUpdateTv.setOnClickListener(this);
        passwordUpdateTv.setOnClickListener(this);
        subscriptionUpdateTv.setOnClickListener(this);
        termsPrivacyTv.setOnClickListener(this);
        btnLogout.setOnClickListener(this);
        contactUsTv.setOnClickListener(this);
    }

    private void setData() {
        Token token = (Token) Token.retrieve(getApplication());
        profileEmailTv.setText(token.user.email_address);

        UserData userData = UserData.retrieve(getApplication());
        lifeFileSizeTv.setText(userData.getLifeFileSize());
        subscriptionTv.setText(userData.getSubscriptionPlanStorageText());
        subscriptionLevelTv.setText(userData.getSubscriptionPlanStorage());
    }

    @Override
    protected void onResume() {
        super.onResume();
        setData();
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.emailUpdateTv:
                startActivity(new Intent(this, ChangeEmailActivity.class));
                break;
            case R.id.passwordUpdateTv:
                startActivity(new Intent(this, ChangePasswordActivity.class));
                break;

            case R.id.subscriptionUpdate:
                startActivity(new Intent(this, SubscriptionActivity.class));
                break;

            case R.id.termsPrivacy:
                startActivity(new Intent(this, WebViewActivity.class));
                break;
            case R.id.btnLogout:
                viewModel.logout();
                break;

            case R.id.contactUs:
                startActivity(new Intent(this, ContactUsActivity.class));
                break;
        }
    }

    private void observer() {
        viewModel.loading.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean) {
                    UIUtil.setViewsEnabled(false, btnLogout, emailUpdateTv, passwordUpdateTv);
                    startLoader();
                } else {
                    UIUtil.setViewsEnabled(true, btnLogout, emailUpdateTv, passwordUpdateTv);
                    stopLoader();
                }
            }
        });
        viewModel.error.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                stopLoader();
                toast(s);
            }
        });
        viewModel.logoutData.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Token.clear(getApplication());
                LifeFileCache.getInstance().setCurrent(null);
                LifeFileResponse.clear(getApplication());
                setResult(Activity.RESULT_OK, new Intent().putExtra(Constant.ISLOGOUT, true));
                finish();
            }
        });
    }
}