package com.stych.android.home;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.ui.PlayerControlView;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.upstream.RawResourceDataSource;
import com.google.android.exoplayer2.util.Util;
import com.google.android.exoplayer2.video.VideoListener;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.stych.android.BaseActivity;
import com.stych.android.LifeFileCache;
import com.stych.android.R;
import com.stych.android.data.UserData;
import com.stych.android.data.remote.response.LifeFileResponse;
import com.stych.android.data.remote.response.LifeFileVideo;
import com.stych.android.dialog.DialogCustomAddEditLifeFile;
import com.stych.android.dialog.OKDialog;
import com.stych.android.home.lifefilelist.LifeFileListActivity;
import com.stych.android.home.transparent.TransparentMainActivity;
import com.stych.android.profile.ProfileActivity;
import com.stych.android.profile.subscription.SubscriptionActivity;
import com.stych.android.start.splash.SplashActivity;
import com.stych.android.util.FilePath;
import com.stych.android.util.UIUtil;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import javax.inject.Inject;

import dagger.android.AndroidInjection;

public class MainActivity extends BaseActivity implements View.OnClickListener{
    private final String TAG = MainActivity.class.getSimpleName();

    private final String STATE_RESUME_WINDOW = "resumeWindow";
    private final String STATE_RESUME_POSITION = "resumePosition";
    private final String STATE_PLAYER_FULLSCREEN = "playerFullscreen";
    private static final int VIDEO_CAPTURE_ = 1002;
    private static final int SELECT_GALLERY_VIDEO = 1003;
    private final int isProfileCode = 100;
    public AppCompatButton btnSelectDemo;
    public boolean isSelectEnable;
    @Inject
    MainViewModel viewModel;
    private ImageView cameraIv, shareIv, profileIv;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private TextView progressTv;
    private SimpleExoPlayer player;
    private View includeHome;
    private Toolbar toolBar;
    private PlayerView playerView;
    private boolean mExoPlayerFullscreen;
    private ImageView mFullScreenIcon;
    private FrameLayout mFullScreenButton,mainFrame,dialogFrame;
    private LinearLayout bottomLl;
    private Dialog mFullScreenDialog;
    private int mResumeWindow;
    private long mResumePosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AndroidInjection.inject(this);

        initViews();
        if (savedInstanceState != null) {
            mResumeWindow = savedInstanceState.getInt(STATE_RESUME_WINDOW);
            mResumePosition = savedInstanceState.getLong(STATE_RESUME_POSITION);
            mExoPlayerFullscreen = savedInstanceState.getBoolean(STATE_PLAYER_FULLSCREEN);
        }
        defaultHeaderInitiate(true);
        setObservers();

        Dexter.withContext(this)
                .withPermissions(
                        Manifest.permission.CAMERA,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                ).withListener(new MultiplePermissionsListener() {
            @Override
            public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                /* ... */
            }

            @Override
            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                /* ... */
            }
        }).check();

        UserData userData = UserData.retrieve(getApplication());
        if (!userData.tutorialShown) {
            startActivity(new Intent(this, TransparentMainActivity.class));
            userData.tutorialShown = true;
            userData.save(getApplication());
        }
        viewModel.getGeneralUserData();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(STATE_RESUME_WINDOW, mResumeWindow);
        outState.putLong(STATE_RESUME_POSITION, mResumePosition);
        outState.putBoolean(STATE_PLAYER_FULLSCREEN, mExoPlayerFullscreen);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        invalidateOptionsMenu();
        viewModel.refreshCurrentLifeFileVideos();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (Util.SDK_INT >= 24) {
            releasePlayer();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(player != null && player.isPlaying()) {
            player.pause();
            wasPlaying = true;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(wasPlaying) {
            player.play();
            wasPlaying = false;
        }
    }

    private boolean playWhenReady = true;
    private int currentWindow = 0;
    private long playbackPosition = 0;
    private boolean wasPlaying = false;

    private void releasePlayer() {
        if (player != null) {
            playWhenReady = player.getPlayWhenReady();
//            playbackPosition = player.getCurrentPosition();
//            currentWindow = player.getCurrentWindowIndex();
            player.release();
            player = null;
        }
    }

    private void playHlsVideo(final String url) {
        Log.d(TAG, "playHlsVideo url=" + url);
        if(TextUtils.isEmpty(url)) {
            Uri uri = RawResourceDataSource.buildRawResourceUri(R.raw.welcome);
            setUpPlayerUri(uri);
            return;
        }

        releasePlayer();
        player = new SimpleExoPlayer.Builder(this).build();
        resizePlayerView();
        player.setVideoScalingMode(C.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING);
        playerView.setControllerAutoShow(true);
        playerView.setPlayer(player);

        MediaItem mediaItem = MediaItem.fromUri(url);
        if(url.endsWith("m3u8")) {
            DataSource.Factory dataSourceFactory = new DefaultHttpDataSourceFactory();
            HlsMediaSource hlsMediaSource = new HlsMediaSource.Factory(dataSourceFactory)
                    .setAllowChunklessPreparation(true)
                    .createMediaSource(mediaItem);
            player.setMediaSource(hlsMediaSource);
        } else {
            player.setMediaItem(mediaItem);
        }

        player.setPlayWhenReady(playWhenReady);
        player.prepare();
    }

    private void setResumeLifeFileListener(boolean lifeFile) {
        //Resume life file
        if(lifeFile) {
            player.seekTo(currentWindow, playbackPosition);
            Log.d(TAG, "setResumeLifeFileListener resuming at playbackPosition=" + playbackPosition + ", currentWindow=" + currentWindow);
        } else {
            player.addListener(new Player.EventListener() {
                @Override
                public void onPlaybackStateChanged(int state) {
                    if (state == 4) {
                        String lifeFileUrl = viewModel.getCurrentVideoURL();
                        Log.d(TAG, "setResumeLifeFileListener onPlaybackStateChanged lifeFileUrl=" + lifeFileUrl);
                        if (lifeFileUrl != null) {
                            playHlsVideo(lifeFileUrl);
                        }
                    }
                }
            });
        }
    }

    private void saveLifeFilePlayingStatus() {
        //Store life file playing status
        if(player != null /*&& wasPlayingLifeFile*/) {
            wasPlaying = player.isPlaying();
            playbackPosition = player.getCurrentPosition();
            currentWindow = player.getCurrentWindowIndex();
            Log.d(TAG, "saveLifeFilePlayingStatus wasPlaying=" + wasPlaying + ", playbackPosition=" + playbackPosition + ", currentWindow=" + currentWindow);
        }
    }

    private void setUpPlayerUri(Uri uri) {
        try {
            releasePlayer();
            DefaultBandwidthMeter defaultBandwidthMeter = new DefaultBandwidthMeter();
            DefaultDataSourceFactory dataSourceFactory = new DefaultDataSourceFactory(
                    this, Util.getUserAgent(this, getString(R.string.app_name)), defaultBandwidthMeter);
            ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
            MediaSource mediaSource = new ExtractorMediaSource(uri, dataSourceFactory, extractorsFactory, null, null);
            player = new SimpleExoPlayer.Builder(this).build();
            resizePlayerView();
            player.setVideoScalingMode(C.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING);
            playerView.setControllerAutoShow(false);
            playerView.hideController();
            playerView.setPlayer(player);
            player.prepare(mediaSource);
            player.setPlayWhenReady(true);
        } catch (Exception e) {
            Log.e("TAG", "Error : " + e.toString());
        }
    }

    private void resizePlayerView() {
        player.addVideoListener(new VideoListener() {
            @Override
            public void onVideoSizeChanged(int width, int height, int unappliedRotationDegrees, float pixelWidthHeightRatio) {
                ViewGroup.LayoutParams params = mainFrame.getLayoutParams();
                if(width >= height) {
                    int deviceWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
                    float aspectRatio = (float) width / (float) height;
                    params.height = (int) (deviceWidth / aspectRatio);
                    Log.d(TAG, "onVideoSizeChanged width=" + width + ", height=" + height + ", aspectRatio=" + aspectRatio + ", deviceWidth=" + deviceWidth);
                } else {
                    int deviceHeight = Resources.getSystem().getDisplayMetrics().heightPixels;
                    params.height = deviceHeight / 2;
                }
                mainFrame.setLayoutParams(params);
            }
        });
    }

    private void initViews() {
        toolBar = findViewById(R.id.toolBar);
        includeHome = findViewById(R.id.homeInclude);
        playerView = includeHome.findViewById(R.id.exoPlayer);
        View duration = playerView.getRootView().findViewById(R.id.exo_duration);
        if(duration != null) {
            duration.setVisibility(View.GONE);
        }
        cameraIv = includeHome.findViewById(R.id.cameraIV);
        shareIv = includeHome.findViewById(R.id.shareIv);
        profileIv = includeHome.findViewById(R.id.profileIv);
        swipeRefreshLayout = includeHome.findViewById(R.id.swipeRefreshLayout);
        recyclerView = includeHome.findViewById(R.id.recyclerView);
        progressTv = findViewById(R.id.progressTv);
        bottomLl = includeHome.findViewById(R.id.bottomLl);
        mainFrame= includeHome.findViewById(R.id.main_media_frame);

        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        recyclerView.setAdapter(new MainAdapter(viewModel));
        initFullscreenDialog();
        initFullscreenButton();

        cameraIv.setOnClickListener(this);
        shareIv.setOnClickListener(this);
        profileIv.setOnClickListener(this);
    }

    private void initFullscreenDialog() {
        mFullScreenDialog = new Dialog(this, android.R.style.Theme_Black_NoTitleBar_Fullscreen) {
            public void onBackPressed() {
                if (mExoPlayerFullscreen)
                    closeFullscreenDialog();
                super.onBackPressed();
            }
        };
    }

    private void openFullscreenDialog() {
        ((ViewGroup) playerView.getParent()).removeView(playerView);
        mFullScreenDialog.setContentView(R.layout.custom_full_screen_dialog);
        mFullScreenDialog.setCancelable(false);
        dialogFrame = mFullScreenDialog.findViewById(R.id.dialogContainer);
        dialogFrame.addView(playerView);
        mFullScreenIcon.setImageDrawable(ContextCompat.getDrawable(MainActivity.this, R.drawable.ic_fullscreen_skrink));
        mExoPlayerFullscreen = true;
        mFullScreenDialog.show();
    }

    private void closeFullscreenDialog() {
        ((ViewGroup) playerView.getParent()).removeView(playerView);
        ((FrameLayout) findViewById(R.id.main_media_frame)).addView(playerView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mExoPlayerFullscreen = false;
        mFullScreenDialog.dismiss();
        mFullScreenIcon.setImageDrawable(ContextCompat.getDrawable(MainActivity.this, R.drawable.ic_fullscreen_expand));
    }

    private void initFullscreenButton() {
        PlayerControlView controlView = playerView.findViewById(R.id.exo_controller);
        mFullScreenIcon = controlView.findViewById(R.id.exo_fullscreen_icon);
        mFullScreenButton = controlView.findViewById(R.id.exo_fullscreen_button);
        mFullScreenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mExoPlayerFullscreen) {
                    openFullscreenDialog();
                } else {
                    closeFullscreenDialog();
                }
            }
        });
    }

    @Override
    public void onConfigurationChanged(@NotNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            openFullscreenDialog();
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            closeFullscreenDialog();
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.cameraIV:
                openVideoPicker(true);
                break;
            case R.id.shareIv:
                openVideoPicker(false);
                break;
            case R.id.profileIv:
                startActivityForResult(new Intent(this, ProfileActivity.class), isProfileCode);
                break;/*
            case R.id.selectButton:
                viewModel.setEnableSelect(!isSelectEnable);
                break;*/

        }
    }

    private void setObservers() {
        viewModel.loading.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {

            }
        });
        viewModel.error.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                swipeRefreshLayout.setRefreshing(false);
                OKDialog.show(MainActivity.this, s, null);
            }
        });
        viewModel.enableSelect.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                isSelectEnable = aBoolean;
            }
        });

        viewModel.createFileData.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if (s != null) {
                    invalidateOptionsMenu();
//                    OKDialog.show(MainActivity.this, s, null);
                }
            }
        });

        viewModel.renameFileData.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if (s != null) {
                    invalidateOptionsMenu();
//                    OKDialog.show(MainActivity.this, s, null);
                }
            }
        });
        viewModel.currentLifeFile.observe(this, new Observer<LifeFileResponse>() {
            @Override
            public void onChanged(LifeFileResponse lifeFileResponse) {
                invalidateOptionsMenu();
                if (lifeFileResponse != null) {
                    viewModel.getLifeFileVideoList(lifeFileResponse.life_file_id);
                    swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                        @Override
                        public void onRefresh() {
                            viewModel.refreshCurrentLifeFileVideos();
                        }
                    });
                }
            }
        });
        viewModel.listVideo.observe(this, new Observer<List<LifeFileVideo>>() {
            @Override
            public void onChanged(List<LifeFileVideo> lifeFileVideos) {
                swipeRefreshLayout.setRefreshing(false);
                MainAdapter adapter = (MainAdapter) recyclerView.getAdapter();
                adapter.setLifeFileVideos(lifeFileVideos);
                playHlsVideo(viewModel.getCurrentVideoURL());
            }
        });

        viewModel.videoSegment.observe(this, new Observer<LifeFileVideo>() {
            @Override
            public void onChanged(LifeFileVideo lifeFileVideo) {
                player.seekTo(currentWindow, lifeFileVideo.videoStartMillis);
                Log.d(TAG, "videoSegment->onChanged videoStartMillis=" + lifeFileVideo.toString());
            }
        });

        LifeFileCache.getInstance().error.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                OKDialog.show(MainActivity.this, s, new OKDialog.OnOKClickListener() {
                    @Override
                    public void onOKClick() {
                        if(s != null && s.startsWith("Allowed limit")) {
                            startActivity(new Intent(MainActivity.this, SubscriptionActivity.class));
                        }
                    }
                });
            }
        });

        LifeFileCache.getInstance().progress.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                progressTv.setVisibility(TextUtils.isEmpty(s) ? View.GONE : View.VISIBLE);
                progressTv.setText(s);
            }
        });
    }

    public void openVideoPicker(boolean camera) {
        if (camera) {
            Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0);
            startActivityForResult(intent, VIDEO_CAPTURE_);
        } else {
            Intent intent;
            if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
                intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
            } else {
                intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Video.Media.INTERNAL_CONTENT_URI);
            }
            intent.setType("video/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            intent.putExtra("return-data", true);
            startActivityForResult(intent, SELECT_GALLERY_VIDEO);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        List<LifeFileResponse> lifeFiles = LifeFileCache.getInstance().getLifeFiles();
        if (lifeFiles != null && !lifeFiles.isEmpty()) {
            if(LifeFileCache.getInstance().getCurrent() != null) {
                menu.findItem(R.id.action_user).setTitle(LifeFileCache.getInstance().getCurrent().name);
                cameraIv.setEnabled(true);
                shareIv.setEnabled(true);
            }

            int i = 0;
            while (menu.findItem(i) != null) {
                menu.removeItem(i);
                i++;
            }
            i = 0;
            for (LifeFileResponse lifeFile : lifeFiles) {
                MenuItem item = menu.add(R.id.life_files, i, i, lifeFile.name);
                if (lifeFile.equals(LifeFileCache.getInstance().getCurrent())) {
                    item.setChecked(true);
                }
                i++;
            }
        } else {
            menu.findItem(R.id.action_user).setTitle(R.string.app_name);
            cameraIv.setEnabled(false);
            shareIv.setEnabled(false);
            MainAdapter adapter = (MainAdapter) recyclerView.getAdapter();
            adapter.setLifeFileVideos(null);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        List<LifeFileResponse> lifeFiles = LifeFileCache.getInstance().getLifeFiles();
        if (lifeFiles != null) {
            for (int i = 0; i < lifeFiles.size(); i++) {
                if (id == i) {
                    LifeFileResponse lifeFile = lifeFiles.get(i);
                    lifeFile.save(getApplication());
                    LifeFileCache.getInstance().setCurrent(lifeFile);
                    return super.onOptionsItemSelected(item);
                }
            }
        }

        switch (id) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.action_manage_life_file:
                startActivity(new Intent(this, LifeFileListActivity.class));
                break;
            case R.id.action_create_new_life_file:
                createLifeDialog();
                break;
            case R.id.action_rename_life_file:
                renameLifeDialog();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_GALLERY_VIDEO) {
                if (data.getData() != null) {
                    String filePath = FilePath.getPath(this, data.getData());
                    LifeFileCache.getInstance().addVideoFileToProcessQueue(getApplication(), filePath);
                } else {
                    Toast.makeText(getApplicationContext(), "Failed to select the video.", Toast.LENGTH_LONG).show();
                }
            } else if (requestCode == VIDEO_CAPTURE_) {
                Uri videoUri = data.getData();
                String filePath = FilePath.getPath(this, videoUri);
               LifeFileCache.getInstance().addVideoFileToProcessQueue(getApplication(), filePath);
            } else if (requestCode == isProfileCode) {
                startActivity(new Intent(MainActivity.this, SplashActivity.class));
                finish();
            }
        }
    }

    public void createLifeDialog() {
        DialogCustomAddEditLifeFile.show(this, getResources().getString(R.string.please_create_new_file), getResources().getString(R.string.create), getResources().getString(R.string.cancel), new DialogCustomAddEditLifeFile.OnOkButton() {
            @Override
            public void onCreateRename(String name) {
                UIUtil.hideKeyboard(cameraIv);
                viewModel.createLifeFile(name);
            }
        });
    }

    public void renameLifeDialog() {
        DialogCustomAddEditLifeFile.show(this, getResources().getString(R.string.rename_file), getResources().getString(R.string.rename), getResources().getString(R.string.cancel), new DialogCustomAddEditLifeFile.OnOkButton() {
            @Override
            public void onCreateRename(String name) {
                UIUtil.hideKeyboard(cameraIv);
                viewModel.renameLifeFile(name);
            }
        });
    }

}