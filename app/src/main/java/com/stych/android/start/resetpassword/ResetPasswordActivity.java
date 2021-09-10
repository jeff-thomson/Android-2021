package com.stych.android.start.resetpassword;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.widget.AppCompatButton;
import androidx.lifecycle.Observer;

import com.stych.android.BaseActivity;
import com.stych.android.Constant;
import com.stych.android.R;
import com.stych.android.dialog.OKDialog;
import com.stych.android.start.forgotpassword.ForgotViewModel;
import com.stych.android.start.login.LoginActivity;
import com.stych.android.util.UIUtil;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.android.AndroidInjection;

public class ResetPasswordActivity extends BaseActivity implements View.OnClickListener {

    @Inject
    ForgotViewModel viewModel;
    private EditText codeText, passwordText, confirmPasswordText;
    private AppCompatButton verificationBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        AndroidInjection.inject(this);
        initiate();
        observer();
    }

    public void initiate() {
        codeText = findViewById(R.id.codeEt);
        passwordText = findViewById(R.id.passwordEt);
        confirmPasswordText = findViewById(R.id.confirmPasswordEt);
        verificationBtn = findViewById(R.id.verificationCodeSubmit);
        textWatcher(crateListOfEdittextVerfiy(), verificationBtn);

        verificationBtn.setOnClickListener(this);
    }

    private List<EditText> crateListOfEdittextVerfiy() {
        List<EditText> editTextList = new ArrayList<>();
        editTextList.add(codeText);
        editTextList.add(passwordText);
        editTextList.add(confirmPasswordText);
        return editTextList;
    }

    private void observer() {
        viewModel.loading.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean) {
                    UIUtil.setViewsEnabled(false, verificationBtn);
                    startLoader();
                } else {
                    UIUtil.setViewsEnabled(true, verificationBtn);
                    stopLoader();
                }
            }
        });
        viewModel.error.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                stopLoader();
                OKDialog.show(ResetPasswordActivity.this, s, null);
            }
        });

        viewModel.verifyCode.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                OKDialog.show(ResetPasswordActivity.this, s, new OKDialog.OnOKClickListener() {
                    @Override
                    public void onOKClick() {
                        startActivity(new Intent(ResetPasswordActivity.this, LoginActivity.class));
                        finish();
                    }
                });
            }
        });
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.verificationCodeSubmit) {
            try {
                UIUtil.hideKeyboard(passwordText);
                viewModel.setEmail(getIntent().getStringExtra(Constant.EMAIL_RESET));
                viewModel.setToken(codeText.getText().toString());
                viewModel.setPassword(passwordText.getText().toString());
                viewModel.setPasswordConfirm(confirmPasswordText.getText().toString());
                viewModel.sendTokenPassword();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}