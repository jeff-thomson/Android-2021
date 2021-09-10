package com.stych.android.home.lifefilelist;

import android.os.Bundle;
import android.widget.TextView;

import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.stych.android.BaseActivity;
import com.stych.android.R;
import com.stych.android.data.remote.response.LifeFileResponse;
import com.stych.android.dialog.OKDialog;
import com.stych.android.home.MainViewModel;

import java.util.List;

import javax.inject.Inject;

import dagger.android.AndroidInjection;

public class LifeFileListActivity extends BaseActivity {

    @Inject
    LifeFileListViewModel viewModel;
    private RecyclerView recyclerView;
    private TextView noRecordTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lifefile_list);
        AndroidInjection.inject(this);
        defaultHeaderInitiate(getResources().getString(R.string.your_life_files));
        initiate();
        observer();
    }

    private void initiate() {
        recyclerView = findViewById(R.id.recyclerView);
        noRecordTv = findViewById(R.id.noRecordTv);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

    private void observer() {
        viewModel.loading.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean) {
                    startLoader();
                } else {
                    stopLoader();
                }
            }
        });
        viewModel.error.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                stopLoader();
                OKDialog.show(LifeFileListActivity.this, s, null);
            }
        });
        viewModel.listFileData.observe(this, new Observer<List<LifeFileResponse>>() {
            @Override
            public void onChanged(List<LifeFileResponse> lifeFileResponses) {
                recyclerView.setAdapter(new LifeFileListAdapter(lifeFileResponses, viewModel));
            }
        });
        viewModel.deleteFile.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if (s != null) {
                    OKDialog.show(LifeFileListActivity.this, s, null);
                }
            }
        });
    }
}