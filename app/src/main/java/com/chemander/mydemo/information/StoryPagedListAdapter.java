package com.chemander.mydemo.information;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.paging.PagedListAdapter;
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
        storyInformation.setStoryImgUrl(storyInformation.getStoryURL());
        holder.itemStoryInformationDataBindBinding.setStory(storyInformation);
    }

    public class StoryViewHolder extends RecyclerView.ViewHolder {
        ItemStoryInformationDataBindBinding itemStoryInformationDataBindBinding;

        public StoryViewHolder(@NonNull ItemStoryInformationDataBindBinding itemStoryInformationDataBindBinding) {
            super(itemStoryInformationDataBindBinding.getRoot());
            this.itemStoryInformationDataBindBinding = itemStoryInformationDataBindBinding;
        }
    }
}
