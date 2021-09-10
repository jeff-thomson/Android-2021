package com.stych.android.start.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatButton;
import androidx.lifecycle.Observer;

import com.stych.android.BaseActivity;
import com.stych.android.R;
import com.stych.android.dialog.OKDialog;
import com.stych.android.home.MainActivity;
import com.stych.android.start.forgotpassword.ForgotPasswordActivity;
import com.stych.android.util.UIUtil;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.android.AndroidInjection;

public class LoginActivity extends BaseActivity implements View.OnClickListener {

    @Inject
    LoginViewModel viewModel;
    private EditText emailEt, passwordEt;
    private AppCompatButton btnSignIn;
    private TextView forgotPasswordTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        AndroidInjection.inject(this);
        defaultHeaderInitiate(getResources().getString(R.string.sign_in));
        initiate();
        observer();
    }

    private void initiate() {
        emailEt = findViewById(R.id.emailText);
        passwordEt = findViewById(R.id.passwordText);
        forgotPasswordTv = findViewById(R.id.forgotPasswordTv);
        btnSignIn = findViewById(R.id.btnSignIn);
        textWatcher(crateListOfEdittext(), btnSignIn);

        btnSignIn.setOnClickListener(this);
        forgotPasswordTv.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.btnSignIn:
                UIUtil.hideKeyboard(emailEt);
                viewModel.setEmail(emailEt.getText().toString());
                viewModel.setPassword(passwordEt.getText().toString());
                viewModel.login();
                break;
            case R.id.forgotPasswordTv:
                startActivity(new Intent(this, ForgotPasswordActivity.class));
                break;

        }
    }

    private List<EditText> crateListOfEdittext() {
        List<EditText> editTextList = new ArrayList<>();
        editTextList.add(emailEt);
        editTextList.add(passwordEt);
        return editTextList;
    }

    private void observer() {
        viewModel.loading.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean) {
                    startLoader();
                    UIUtil.setViewsEnabled(false, btnSignIn);
                } else {
                    UIUtil.setViewsEnabled(true, btnSignIn);
                    stopLoader();
                }
            }
        });
        viewModel.error.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                stopLoader();
                OKDialog.show(LoginActivity.this, s, null);
            }
        });
        viewModel.loginData.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();
            }
        });
    }
}