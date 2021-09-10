package com.stych.android.start.signup;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatButton;
import androidx.lifecycle.Observer;

import com.stych.android.BaseActivity;
import com.stych.android.R;
import com.stych.android.WebViewActivity;
import com.stych.android.dialog.OKDialog;
import com.stych.android.start.thankyouforsignup.ThankYouActivity;
import com.stych.android.util.UIUtil;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.android.AndroidInjection;

public class SignUpActivity extends BaseActivity implements View.OnClickListener {

    @Inject
    RegisterViewModel viewModel;
    private EditText emailEt, passwordEt, confirmPasswordEt;
    private TextView termsTv;
    private AppCompatButton btnSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        AndroidInjection.inject(this);
        initiate();
        defaultHeaderInitiate(getResources().getString(R.string.sign_up));
        observe();
    }

    private void initiate() {
        emailEt = findViewById(R.id.emailText);
        passwordEt = findViewById(R.id.passwordText);
        confirmPasswordEt = findViewById(R.id.confirmPasswordText);
        termsTv = findViewById(R.id.temsTv);
        btnSignUp = findViewById(R.id.btnSignUp);
        textWatcher(crateListOfEdittext(), btnSignUp);
        String html = "By signing up, you agree to the Stych\n" + "<b>" + termsTv.getText().toString() + "</b>";
        termsTv.setText(Html.fromHtml(html));
        btnSignUp.setOnClickListener(this);
        termsTv.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.btnSignUp:
                validateSignUp();
                break;
            case R.id.temsTv:
                startActivity(new Intent(this, WebViewActivity.class));
                break;
        }
    }

    private void validateSignUp() {
        UIUtil.hideKeyboard(emailEt);
        viewModel.setEmail(emailEt.getText().toString());
        viewModel.setPassword(passwordEt.getText().toString());
        viewModel.setConfirmPassword(confirmPasswordEt.getText().toString());
        viewModel.newRegister();
    }

    private List<EditText> crateListOfEdittext() {
        List<EditText> editTextList = new ArrayList<>();
        editTextList.add(emailEt);
        editTextList.add(passwordEt);
        editTextList.add(confirmPasswordEt);
        return editTextList;
    }

    private void observe() {
        viewModel.loading.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean) {
                    UIUtil.setViewsEnabled(false, btnSignUp, termsTv);
                    startLoader();
                } else {
                    UIUtil.setViewsEnabled(true, btnSignUp, termsTv);
                    stopLoader();
                }
            }
        });
        viewModel.error.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                stopLoader();
                OKDialog.show(SignUpActivity.this, s, null);
            }
        });
        viewModel.registerData.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                startActivity(new Intent(SignUpActivity.this, ThankYouActivity.class));
                finish();
            }
        });

    }

}