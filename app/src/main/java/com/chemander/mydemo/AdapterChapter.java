package com.chemander.mydemo;

import android.content.Context;
import android.content.Intent;
import android.graphics.LinearGradient;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.chemander.mydemo.model.Chapter;
import com.chemander.mydemo.reading.ReadingActivity;
import com.chemander.mydemo.utils.SettingsManager;

import java.util.ArrayList;
import java.util.List;

public class AdapterChapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private List<Chapter> chapters = new ArrayList<>();
    private Context context;

    private AdapterView.OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(AdapterView.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public AdapterChapter(Context context, List<Chapter> chapters){
        this.context = context;
        this.chapters = chapters;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView title;
        public RelativeLayout layout;
        public ViewHolder(View view){
            super(view);
            title = (TextView) view.findViewById(R.id.title_chapter);
            layout = (RelativeLayout) view.findViewById(R.id.layout_parent);
        }
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_home, parent, false);
        vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof ViewHolder) {
            ViewHolder view = (ViewHolder) holder;
            final Chapter chapter = chapters.get(position);
            view.title.setText(chapter.getTitle());
            view.layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (SettingsManager.preferenceCurrentChapter != position) {
                        SettingsManager.preferenceCurrentChapter = position;
                        SettingsManager.preferenceCurrentPosition = 0;
                        SettingsManager.saveSettings(context);
                    }
                    Intent intent = new Intent(context, ReadingActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return chapters.size();
    }

    public Chapter getChapter(int position){
        return chapters.get(position);
    }
}
