package com.chemander.mydemo.home;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.chemander.mydemo.R;
import com.chemander.mydemo.data.model.StoryInformation;
import com.chemander.mydemo.information.StoryInformationActivity;
import com.chemander.mydemo.utils.SettingsManager;

import java.util.ArrayList;
import java.util.List;

public class HomeRecycleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private List<StoryInformation> stories = new ArrayList<>();
    private Context context;

    private AdapterView.OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(AdapterView.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public HomeRecycleAdapter(Context context, List<StoryInformation> stories){
        this.context = context;
        this.stories = stories;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public ImageView cover;
        public TextView title;
//        public TextView author;
        public ConstraintLayout layout;
        public ViewHolder(View view){
            super(view);
            cover = (ImageView) view.findViewById(R.id.imageViewCover);
            title = (TextView) view.findViewById(R.id.textTitleStory);
//            author = (TextView) view.findViewById(R.id.textAuthor);
//            layout = (ConstraintLayout) view.findViewById(R.id.layout_constraint_story);
        }
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_home_hot_news_update, parent, false);
        vh = new HomeRecycleAdapter.ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof HomeRecycleAdapter.ViewHolder) {
            HomeRecycleAdapter.ViewHolder view = (HomeRecycleAdapter.ViewHolder) holder;
            final StoryInformation storyInformation = stories.get(position);
            Glide.with(context).load(storyInformation.getStoryImgUrl()).override(260,300).centerCrop().into(view.cover);
            view.title.setText(storyInformation.getStoryName());
//            view.author.setText("Tác giả: "+storyInformation.getStoryAuthor());
//            view.layout.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
////                    Bundle bundle = new Bundle();
////                    bundle.putSerializable(SettingsManager.STORY_INFORMATION, storyInformation);
//                    Intent intent = new Intent(context, StoryInformationActivity.class);
//                    intent.putExtra(SettingsManager.STORY_INFORMATION, storyInformation);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    context.startActivity(intent);
//                }
//            });
        }
    }

    @Override
    public int getItemCount() {
        return stories.size();
    }

    public StoryInformation getStory(int position){
        return stories.get(position);
    }
}

