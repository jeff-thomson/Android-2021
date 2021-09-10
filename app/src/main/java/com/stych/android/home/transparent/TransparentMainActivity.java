package com.stych.android.home.transparent;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.stych.android.R;
import com.stych.android.data.Token;

public class TransparentMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transparent_main);
        findViewById(R.id.main).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Token token = (Token) Token.retrieve(getApplication());
                token.isLogin = true;
                token.save(getApplication());
                finish();
            }
        });
    }
}