package com.stych.android.profile.changepassword;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.widget.AppCompatButton;
import androidx.lifecycle.Observer;

import com.stych.android.BaseActivity;
import com.stych.android.R;
import com.stych.android.dialog.OKDialog;
import com.stych.android.profile.ProfileViewModel;
import com.stych.android.util.UIUtil;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.android.AndroidInjection;

public class ChangePasswordActivity extends BaseActivity implements View.OnClickListener {

    @Inject
    ProfileViewModel viewModel;
    private EditText oldPasswordEt, passwordEt, confirmPasswordEt;
    private AppCompatButton btnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        AndroidInjection.inject(this);
        defaultHeaderInitiate(getResources().getString(R.string.change_password));
        initiate();
        observer();
    }

    private void initiate() {
        oldPasswordEt = findViewById(R.id.oldPasswordText);
        passwordEt = findViewById(R.id.passwordText);
        confirmPasswordEt = findViewById(R.id.confirmPasswordText);
        btnSubmit = findViewById(R.id.btnSubmit);
        textWatcher(crateListOfEditText(), btnSubmit);

        btnSubmit.setOnClickListener(this);
    }

    private List<EditText> crateListOfEditText() {
        List<EditText> editTextList = new ArrayList<>();
        editTextList.add(oldPasswordEt);
        editTextList.add(passwordEt);
        editTextList.add(confirmPasswordEt);
        return editTextList;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btnSubmit) {
            UIUtil.hideKeyboard(passwordEt);
            viewModel.setOldPassword(oldPasswordEt.getText().toString());
            viewModel.setPassword(passwordEt.getText().toString());
            viewModel.setConfirmPassword(confirmPasswordEt.getText().toString());
            viewModel.changePassword();
        }
    }

    private void observer() {
        viewModel.loading.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean) {
                    UIUtil.setViewsEnabled(false, btnSubmit);
                    startLoader();
                } else {
                    UIUtil.setViewsEnabled(true, btnSubmit);
                    stopLoader();
                }
            }
        });
        viewModel.error.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                stopLoader();
                OKDialog.show(ChangePasswordActivity.this, s, new OKDialog.OnOKClickListener() {
                    @Override
                    public void onOKClick() {
                        //
                    }
                });

            }
        });
        viewModel.changePasswordData.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                toast(s);
                finish();
            }
        });
    }
}