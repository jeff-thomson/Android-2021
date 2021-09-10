package com.stych.android.start.thankyouforsignup;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.widget.AppCompatButton;
import androidx.lifecycle.Observer;

import com.stych.android.BaseActivity;
import com.stych.android.R;
import com.stych.android.dialog.OKDialog;
import com.stych.android.home.MainActivity;
import com.stych.android.home.MainViewModel;
import com.stych.android.util.UIUtil;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.android.AndroidInjection;

public class ThankYouActivity extends BaseActivity implements View.OnClickListener {

    @Inject
    ThankYouViewModel viewModel;
    private AppCompatButton btnSubmit;
    private EditText etName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thank_you);
        AndroidInjection.inject(this);
        defaultHeaderInitiate(getResources().getString(R.string.thank_you_for_signing_up));
        initiate();
        observer();
    }

    private void initiate() {
        btnSubmit = findViewById(R.id.btnSubmit);
        etName = findViewById(R.id.nameTv);
        btnSubmit.setOnClickListener(this);
        List<EditText> editTextList = new ArrayList<>();
        editTextList.add(etName);
        textWatcher(editTextList, btnSubmit);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btnSubmit) {
            UIUtil.hideKeyboard(etName);
            viewModel.createLifeFile(etName.getText().toString());
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
                OKDialog.show(ThankYouActivity.this, s, null);
            }
        });
        viewModel.createFileData.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                finish();
                startActivity(new Intent(ThankYouActivity.this, MainActivity.class));
            }
        });
    }
}