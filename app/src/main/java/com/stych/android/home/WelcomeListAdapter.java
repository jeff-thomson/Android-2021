package com.stych.android.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.stych.android.R;

import java.util.ArrayList;
import java.util.List;

public class WelcomeListAdapter extends RecyclerView.Adapter<WelcomeListAdapter.ViewHolder> {
    private final List<Integer> welcomeItems;

    public WelcomeListAdapter() {
        this.welcomeItems = createWelcomeList();
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
        holder.imageView.setImageDrawable(holder.itemView.getContext().getResources().getDrawable(welcomeItems.get(position)));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) holder.itemView.getContext()).openVideoPicker(false);
            }
        });
    }

    @Override
    public int getItemCount() {
        return welcomeItems.size();
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
        return list;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView, checkbox;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            checkbox = itemView.findViewById(R.id.checkbox);
        }
    }

}