package com.chemander.mydemo.information;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.chemander.mydemo.R;
import com.chemander.mydemo.data.model.StoryInformation;
import com.chemander.mydemo.databinding.ItemStoryInformationDataBindBinding;

public class StoryPagedListAdapter extends PagedListAdapter<StoryInformation, StoryPagedListAdapter.StoryViewHolder> {
    private Context context;

    public StoryPagedListAdapter(Context context){
        super(StoryInformation.CALLBACK);
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
        StoryInformation storyInformation = getItem(position);
//        storyInformation.setStoryImgUrl(storyInformation.getStoryImgUrl());
        holder.itemStoryInformationDataBindBinding.setStory(storyInformation);
    }

    public class StoryViewHolder extends RecyclerView.ViewHolder {
        ItemStoryInformationDataBindBinding itemStoryInformationDataBindBinding;

        public StoryViewHolder(@NonNull ItemStoryInformationDataBindBinding itemStoryInformationDataBindBinding) {
            super(itemStoryInformationDataBindBinding.getRoot());
            this.itemStoryInformationDataBindBinding = itemStoryInformationDataBindBinding;
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
                    return true;
                }
            };
}
