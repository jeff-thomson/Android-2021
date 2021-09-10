package com.stych.android.start.splash;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.widget.AppCompatButton;

import com.stych.android.BaseActivity;
import com.stych.android.LifeFileCache;
import com.stych.android.R;
import com.stych.android.data.Token;
import com.stych.android.data.remote.Repository;
import com.stych.android.data.remote.response.LifeFileResponse;
import com.stych.android.home.MainActivity;
import com.stych.android.start.login.LoginActivity;
import com.stych.android.start.signup.SignUpActivity;

import javax.inject.Inject;

import dagger.android.AndroidInjection;

public class SplashActivity extends BaseActivity implements View.OnClickListener {
    @Inject
    Repository repository;
    private AppCompatButton btnSignUp, btnSignIn;
    private LinearLayout buttonsParentLl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        AndroidInjection.inject(this);

        initViews();
    }

    @Override
    protected void onStart() {
        super.onStart();
        displayButtonLayout();
    }

    private void initViews() {
        btnSignIn = findViewById(R.id.btnSignIn);
        btnSignUp = findViewById(R.id.btnSignUp);
        buttonsParentLl = findViewById(R.id.buttonsParentLl);

        btnSignUp.setOnClickListener(this);
        btnSignIn.setOnClickListener(this);
    }

    private void displayButtonLayout() {
        btnSignIn.postDelayed(new Runnable() {
            @Override
            public void run() {
                Token token = Token.retrieve(getApplication());
                LifeFileCache.getInstance().init(LifeFileResponse.retrieve(getApplication()), token, repository);
                try {
                    if (token.isLogin) {
                        LifeFileCache.getInstance().getLifeFilesFromServer();
                        startActivity(new Intent(SplashActivity.this, MainActivity.class));
                        finish();
                    } else {
                        buttonsParentLl.setVisibility(View.VISIBLE);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    buttonsParentLl.setVisibility(View.VISIBLE);
                }
            }
        }, 1500);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.btnSignIn:
                startActivity(new Intent(this, LoginActivity.class));
                break;
            case R.id.btnSignUp:
                startActivity(new Intent(this, SignUpActivity.class));
                break;
        }
    }
}