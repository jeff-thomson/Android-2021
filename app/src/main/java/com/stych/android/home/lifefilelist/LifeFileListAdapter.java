package com.stych.android.home.lifefilelist;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.stych.android.Constant;
import com.stych.android.JNIBridge;
import com.stych.android.R;
import com.stych.android.data.remote.response.LifeFileResponse;
import com.stych.android.dialog.OKCancelDialog;
import com.stych.android.home.MainViewModel;
import com.stych.android.home.transferlifefile.TransferALifeFileActivity;

import java.io.File;
import java.util.List;

public class LifeFileListAdapter extends RecyclerView.Adapter<LifeFileListAdapter.ViewHolder> {
    private final List<LifeFileResponse> listData;
    private final LifeFileListViewModel viewModel;

    public LifeFileListAdapter(List<LifeFileResponse> listData, LifeFileListViewModel viewModel) {
        this.listData = listData;
        this.viewModel = viewModel;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.lifefile_list_cell, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final LifeFileResponse lifeFile = listData.get(position);

        holder.deleteLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OKCancelDialog.show(view.getContext(), "Are you sure you want to delete this Life File?", "OK", new OKCancelDialog.OnOKClickListener() {
                    @Override
                    public void onOKClick() {
                        viewModel.deleteLifeFile(lifeFile.life_file_id);
                    }
                });
            }
        });
        holder.transferLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.getContext().startActivity(new Intent(view.getContext(), TransferALifeFileActivity.class).putExtra(Constant.TRANSFER_KEY, listData.get(position).life_file_id));
            }
        });

        holder.titleTv.setText(lifeFile.name);
        File file = JNIBridge.getVideoThumbPath(lifeFile.life_file_id);
        if(file != null) {
            Uri uri = Uri.fromFile(file);
            Glide.with(holder.itemView.getContext())
                    .load(uri)
                    .into(holder.imageView);
        } else {
            holder.imageView.setImageResource(R.drawable.ic_logo);
        }
    }

    public void addNewCell(LifeFileResponse data) {
        listData.add(data);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public LinearLayout deleteLl, transferLl;
        public TextView titleTv;

        public ViewHolder(View itemView) {
            super(itemView);
            titleTv = itemView.findViewById(R.id.titleTv);
            imageView = itemView.findViewById(R.id.imageView);
            deleteLl = itemView.findViewById(R.id.deleteLl);
            transferLl = itemView.findViewById(R.id.transferLl);
        }
    }
}