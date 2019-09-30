package com.chemander.story.information;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.databinding.DataBindingUtil;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.chemander.story.R;
import com.chemander.story.data.model.StoryInformation;
import com.chemander.story.databinding.ItemStoryInformationDataBindBinding;
import com.chemander.story.utils.SettingsManager;

public class StoryPagedListAdapter extends PagedListAdapter<StoryInformation, StoryPagedListAdapter.StoryViewHolder> {
    private Context context;

    public StoryPagedListAdapter(Context context){
        super(DIFF_CALLBACK);
        this.context = context;
    }

    @NonNull
    @Override
    public StoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemStoryInformationDataBindBinding itemStoryInformationDataBindBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_story_information_data_bind, parent, false);
        return new StoryViewHolder(itemStoryInformationDataBindBinding);
    }


    @Override
    public void onBindViewHolder(@NonNull StoryViewHolder holder, int position) {
        if (holder instanceof StoryViewHolder) {
        StoryInformation storyInformation = getItem(position);
//        storyInformation.setStoryImgUrl(storyInformation.getStoryImgUrl());

        holder.itemStoryInformationDataBindBinding.setStory(storyInformation);
        holder.textAuthor.setText("Tác giả: "+storyInformation.getStoryAuthor());
        if(storyInformation.getStoryStatus()==null || storyInformation.getStoryStatus().isEmpty()){
            holder.textStatus.setText("Trạng thái: Đang cập nhật");
        }else {
            holder.textStatus.setText("Trạng thái: "+storyInformation.getStoryStatus());
        }
        holder.textGenre.setText("Thể loại: "+storyInformation.getStoryGenre());
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, StoryInformationActivity.class);
                intent.putExtra(SettingsManager.STORY_INFORMATION, storyInformation.getId());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
        }
    }

    public class StoryViewHolder extends RecyclerView.ViewHolder {
        ItemStoryInformationDataBindBinding itemStoryInformationDataBindBinding;
        ConstraintLayout layout;
        TextView textAuthor;
        TextView textStatus;
        TextView textGenre;

        public StoryViewHolder(@NonNull ItemStoryInformationDataBindBinding itemStoryInformationDataBindBinding) {
            super(itemStoryInformationDataBindBinding.getRoot());
            this.itemStoryInformationDataBindBinding = itemStoryInformationDataBindBinding;
            View view = itemStoryInformationDataBindBinding.getRoot();
            layout = view.findViewById(R.id.constraint_item_story);
            textAuthor = (TextView)view.findViewById(R.id.textAuthor);
            textStatus = (TextView)view.findViewById(R.id.textStatus);
            textGenre = (TextView)view.findViewById(R.id.textGenre);
        }
    }

    private static DiffUtil.ItemCallback<StoryInformation> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<StoryInformation>() {
                // Concert details may have changed if reloaded from the database,
                // but ID is fixed.
                @Override
                public boolean areItemsTheSame(StoryInformation oldConcert, StoryInformation
                        newConcert) {
                    return oldConcert.getId() == newConcert.getId();
                }

                @Override
                public boolean areContentsTheSame(StoryInformation oldConcert,
                                                  StoryInformation newConcert) {
                    return oldConcert.getId().equals(newConcert.getId());
                }
            };
}
