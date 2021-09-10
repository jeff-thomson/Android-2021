package com.stych.android.profile.changeemail;

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

public class ChangeEmailActivity extends BaseActivity {

    @Inject
    ProfileViewModel viewModel;
    private AppCompatButton btnSubmit;
    private EditText emailText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_email);
        AndroidInjection.inject(this);
        defaultHeaderInitiate(getResources().getString(R.string.change_email));
        emailText = findViewById(R.id.emailText);
        btnSubmit = findViewById(R.id.btnSubmit);
        observer();

        List<EditText> editTextList = new ArrayList<>();
        editTextList.add(emailText);
        textWatcher(editTextList, btnSubmit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UIUtil.hideKeyboard(emailText);
                viewModel.setEmail(emailText.getText().toString());
                viewModel.changeEmail();
            }
        });
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
                OKDialog.show(ChangeEmailActivity.this, s, new OKDialog.OnOKClickListener() {
                    @Override
                    public void onOKClick() {
                        //
                    }
                });

            }
        });
        viewModel.changeEmailData.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                toast(s);
                finish();
            }
        });
    }
}