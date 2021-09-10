package com.stych.android;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;

import com.stych.android.profile.contactus.ContactUsActivity;

import java.util.List;

public class BaseActivity extends AppCompatActivity {
    private final String TAG = "BaseActivity";
    public Toolbar toolBar;
    private boolean isMainMenu = false;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }


    public void toast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    public void defaultHeaderInitiate(String name) {
        toolBar = findViewById(R.id.toolBar);
        toolBar.setTitle("");
        setSupportActionBar(toolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        setLogoClickListener();
    }

    private void setLogoClickListener() {
        View headerLogoIv = findViewById(R.id.headerLogoIv);
        if(headerLogoIv != null && !(this instanceof ContactUsActivity)) {
            headerLogoIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(v.getContext(), ContactUsActivity.class));
                }
            });
        }
    }

    public void defaultHeaderInitiate(boolean isMainMenu) {
        this.isMainMenu = isMainMenu;
        toolBar = findViewById(R.id.toolBar);
        toolBar.setTitle("");
        setSupportActionBar(toolBar);

        setLogoClickListener();
    }

    public void textWatcher(List<EditText> editTextList, AppCompatButton button) {
        for (int i = 0; i < editTextList.size(); i++) {
            editTextList.get(i).addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    boolean enableButton = true;
                    for (int j = 0; j < editTextList.size(); j++) {
                        if (editTextList.get(j).getText().toString().isEmpty()) {
                            enableButton = false;
                        }
                    }
                    button.setEnabled(enableButton);
                }
            });
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void startLoader() {
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
    }

    public void stopLoader() {
        if (progressBar == null) {
            progressBar = findViewById(R.id.progressBar);
            progressBar.setVisibility(View.GONE);
        } else {
            progressBar = findViewById(R.id.progressBar);
            progressBar.setVisibility(View.GONE);
        }
    }

}
