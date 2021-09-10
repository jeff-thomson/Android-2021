package com.stych.android.profile.contactus;

import android.os.Bundle;

import com.stych.android.BaseActivity;
import com.stych.android.R;

public class ContactUsActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);
        defaultHeaderInitiate(getResources().getString(R.string.contact_us));
    }
}