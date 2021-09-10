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
import com.stych.android.home.transferlifefile.TransferALifeFileActivity;
import com.stych.android.util.UtilClass;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class VideoListAdapter extends RecyclerView.Adapter<VideoListAdapter.ViewHolder> {
    private final List<LifeFileVideo> lifeFileVideos;
    private final MainViewModel mainViewModel;

    public VideoListAdapter(List<LifeFileVideo> lifeFileVideos, MainViewModel mainViewModel) {
        this.lifeFileVideos = lifeFileVideos;
        this.mainViewModel = mainViewModel;
    }

    @Override

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.playmedia_list_cell, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final LifeFileVideo lifeFileVideo = lifeFileVideos.get(position);

        holder.moreIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showVideoOptionDialog(view.getContext(), lifeFileVideo);
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String videoUrl = BuildConfig.BASE_URL + "/hls/life_files/"+lifeFileVideo.parent_life_file_id+"/videos/"+lifeFileVideo.id+"/"+mainViewModel.getToken().user.id+"/master.m3u8";
                mainViewModel.setVideoSegment(lifeFileVideo);
            }
        });

        holder.imageView.setImageBitmap(null);
        File file = new File(JNIBridge.getVideoThumbPath(lifeFileVideo.parent_life_file_id, lifeFileVideo.id));
        if(file.exists()) {
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
    }

    @Override
    public int getItemCount() {
        return lifeFileVideos.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView, moreIv;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            moreIv = itemView.findViewById(R.id.moreIv);
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
                        context.startActivity(new Intent(context, TransferALifeFileActivity.class));
//                        showShareFileDialog(context, lifeFileVideo);
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

}