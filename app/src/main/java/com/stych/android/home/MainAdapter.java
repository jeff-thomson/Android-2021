package com.stych.android.home;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.stych.android.BuildConfig;
import com.stych.android.JNIBridge;
import com.stych.android.R;
import com.stych.android.aws.S3Uploader;
import com.stych.android.data.remote.response.LifeFileVideo;
import com.stych.android.dialog.DialogCustomAddEditLifeFile;
import com.stych.android.dialog.OKCancelDialog;
import com.stych.android.dialog.OKDialog;
import com.stych.android.home.transferlifefile.TransferALifeFileActivity;
import com.stych.android.util.UtilClass;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int VIEW_TYPE_VIDEO = 0;
    private final int VIEW_TYPE_WELCOME_TILE = 1;

    private final MainViewModel mainViewModel;
    private List<LifeFileVideo> lifeFileVideos;
    private final List<Integer> welcomeTiles;

    public MainAdapter(MainViewModel mainViewModel) {
        this.mainViewModel = mainViewModel;
        this.welcomeTiles = createWelcomeList();
    }

    public void setLifeFileVideos(List<LifeFileVideo> lifeFileVideos) {
        this.lifeFileVideos = lifeFileVideos;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        int videoCount = lifeFileVideos != null ? lifeFileVideos.size() : 0;
        return position < videoCount ? VIEW_TYPE_VIDEO : VIEW_TYPE_WELCOME_TILE ;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        if(viewType == VIEW_TYPE_VIDEO) {
            View listItem = layoutInflater.inflate(R.layout.playmedia_list_cell, parent, false);
            return new VideoViewHolder(listItem);
        }
        View listItem = layoutInflater.inflate(R.layout.playmedia_list_cell, parent, false);
        return new WelcomeViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder, int position) {
        if(getItemViewType(position) == VIEW_TYPE_VIDEO) {
            final LifeFileVideo lifeFileVideo = lifeFileVideos.get(position);
            VideoViewHolder holder = (VideoViewHolder) viewHolder;
            holder.moreIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showVideoOptionDialog(view.getContext(), lifeFileVideo);
                }
            });

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mainViewModel.setVideoSegment(lifeFileVideo);
                }
            });

            holder.imageView.setImageBitmap(null);
            File file = new File(JNIBridge.getVideoThumbPath(lifeFileVideo.parent_life_file_id, lifeFileVideo.id));
            if (file.exists()) {
                Uri uri = Uri.fromFile(file);
                Glide.with(holder.itemView.getContext())
                        .load(uri)
                        .into(holder.imageView);
            } else {
                String thumbUrl = S3Uploader.getFileURL(lifeFileVideo.video_file.directory_path + ".jpg");
                Glide.with(holder.itemView.getContext())
                        .load(thumbUrl)
                        .into(holder.imageView);
            }
        } else {
            WelcomeViewHolder holder = (WelcomeViewHolder) viewHolder;

            holder.imageView.setImageDrawable(holder.itemView.getContext().getResources().getDrawable(welcomeTiles.get(position)));
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((MainActivity) holder.itemView.getContext()).openVideoPicker(false);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        int videoCount = lifeFileVideos != null ? lifeFileVideos.size() : 0;
        return videoCount > welcomeTiles.size() ? videoCount : welcomeTiles.size();
    }

    public static class VideoViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView, moreIv;

        public VideoViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            moreIv = itemView.findViewById(R.id.moreIv);
        }
    }

    public static class WelcomeViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView, checkbox;

        public WelcomeViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            checkbox = itemView.findViewById(R.id.checkbox);
        }
    }

    private void showVideoOptionDialog(final Context context, LifeFileVideo lifeFileVideo) {
        OKCancelDialog.show(
                context,
                context.getResources().getString(R.string.do_you_want_edit_life),
                context.getResources().getString(R.string.share_file),
                context.getResources().getString(R.string.delete),
                new OKCancelDialog.OnOKCancelClickListener() {
                    @Override
                    public void onOKClick() {
//                        String videoUrl = lifeFileVideo.getVideoURL(mainViewModel.getToken());
//                        Intent shareIntent = new Intent();
//                        shareIntent.setAction(Intent.ACTION_SEND);
//                        shareIntent.putExtra(Intent.EXTRA_STREAM, videoUrl);
//                        shareIntent.setType("video/hls");
//                        context.startActivity(Intent.createChooser(shareIntent, "Send to:"));
                        OKDialog.show(context, "This feature is coming soon.", null);
                    }

                    @Override
                    public void onCancelClick() {
                        lifeFileVideos.remove(lifeFileVideo);
                        notifyDataSetChanged();
                        mainViewModel.deleteLifeFileVideo(lifeFileVideo.id);
                    }
                });
    }

    public void showShareFileDialog(final Context context, LifeFileVideo lifeFileVideo) {
        DialogCustomAddEditLifeFile.show(context, context.getResources().getString(R.string.share_file), context.getResources().getString(R.string.share_file), context.getResources().getString(R.string.cancel), new DialogCustomAddEditLifeFile.OnOkButton() {
            @Override
            public void onCreateRename(String email) {
                if(UtilClass.isValidEmail(email)) {
                    mainViewModel.copyLifeFile(lifeFileVideo.video_file.id, email);
                } else {
                    mainViewModel._error.setValue("Invalid email address!");
                }
            }
        });
    }

    private List<Integer> createWelcomeList() {
        List<Integer> list = new ArrayList<>();
        list.add(R.mipmap.ic_welcome_1);
        list.add(R.mipmap.ic_welcome_2);
        list.add(R.mipmap.ic_welcome_3);
        list.add(R.mipmap.ic_welcome_4);
        list.add(R.mipmap.ic_welcome_5);
        list.add(R.mipmap.ic_welcome_6);
        list.add(R.mipmap.ic_welcome_7);
        list.add(R.mipmap.ic_welcome_8);
        list.add(R.mipmap.ic_welcome_9);
        list.add(R.mipmap.ic_welcome_1);
        list.add(R.mipmap.ic_welcome_2);
        list.add(R.mipmap.ic_welcome_3);
        list.add(R.mipmap.ic_welcome_4);
        list.add(R.mipmap.ic_welcome_5);
        list.add(R.mipmap.ic_welcome_6);
        list.add(R.mipmap.ic_welcome_7);
        list.add(R.mipmap.ic_welcome_8);
        list.add(R.mipmap.ic_welcome_9);
        return list;
    }

}