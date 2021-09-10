package com.stych.android.home.transferlifefile;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.widget.AppCompatButton;
import androidx.lifecycle.Observer;

import com.stych.android.BaseActivity;
import com.stych.android.Constant;
import com.stych.android.R;
import com.stych.android.dialog.OKDialog;
import com.stych.android.home.MainViewModel;
import com.stych.android.util.UIUtil;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.android.AndroidInjection;

public class TransferALifeFileActivity extends BaseActivity {
    @Inject
    TransferLifeFileViewModel viewModel;
    private EditText emailText;
    private AppCompatButton btnSubmit;
    private String lifeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer_a_life_file);
        AndroidInjection.inject(this);
        defaultHeaderInitiate(getResources().getString(R.string.transfer_a_life_file));
        try {
            lifeId = getIntent().getStringExtra(Constant.TRANSFER_KEY);
        } catch (Exception e) {
            finish();
            e.printStackTrace();
        }
        emailText = findViewById(R.id.emailText);
        btnSubmit = findViewById(R.id.btnSubmit);
        List<EditText> editTextList = new ArrayList<>();
        editTextList.add(emailText);
        textWatcher(editTextList, btnSubmit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UIUtil.hideKeyboard(emailText);
                viewModel.setEmail(emailText.getText().toString());
                viewModel.transferLifeFile(lifeId);
            }
        });
        observer();
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
                OKDialog.show(TransferALifeFileActivity.this, s, new OKDialog.OnOKClickListener() {
                    @Override
                    public void onOKClick() {
                        //
                    }
                });

            }
        });

        viewModel.transferFileData.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                OKDialog.show(TransferALifeFileActivity.this, s, new OKDialog.OnOKClickListener() {
                    @Override
                    public void onOKClick() {
                        finish();
                    }
                });
            }
        });
    }
}