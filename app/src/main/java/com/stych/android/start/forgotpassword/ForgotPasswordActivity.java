package com.stych.android.start.forgotpassword;

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
import com.stych.android.start.resetpassword.ResetPasswordActivity;
import com.stych.android.util.UIUtil;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.android.AndroidInjection;

public class ForgotPasswordActivity extends BaseActivity implements View.OnClickListener {

    @Inject
    ForgotViewModel viewModel;
    private EditText emailText;
    private AppCompatButton submitBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        AndroidInjection.inject(this);
        defaultHeaderInitiate(getResources().getString(R.string.forget_password));
        initiate();
        observer();
    }

    private void initiate() {
        emailText = findViewById(R.id.emailText);
        submitBtn = findViewById(R.id.submit);

        textWatcher(crateListOfEdittext(), submitBtn);

        submitBtn.setOnClickListener(this);
    }

    private List<EditText> crateListOfEdittext() {
        List<EditText> editTextList = new ArrayList<>();
        editTextList.add(emailText);
        return editTextList;
    }


    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.submit) {
            UIUtil.hideKeyboard(emailText);
            viewModel.setEmail(emailText.getText().toString());
            viewModel.getEmailVerificationCodePassword();
        }
    }

    private void observer() {
        viewModel.loading.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean) {
                    UIUtil.setViewsEnabled(false, submitBtn);
                    startLoader();
                } else {
                    UIUtil.setViewsEnabled(true, submitBtn);
                    stopLoader();
                }
            }
        });
        viewModel.error.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                stopLoader();
                OKDialog.show(ForgotPasswordActivity.this, s, null);
            }
        });

        viewModel.emailCode.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                startActivity(new Intent(ForgotPasswordActivity.this, ResetPasswordActivity.class).
                        putExtra(Constant.EMAIL_RESET, emailText.getText().toString()));
            }
        });
    }

}