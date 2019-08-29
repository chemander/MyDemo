package com.chemander.mydemo.data.model;

import android.widget.ImageView;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.BindingAdapter;
import androidx.databinding.library.baseAdapters.BR;
import androidx.recyclerview.widget.DiffUtil;

import com.bumptech.glide.Glide;
import com.chemander.mydemo.R;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class StoryInformation extends BaseObservable implements Serializable {

    @SerializedName("storyCountView")
    @Expose
    private Integer storyCountView;
    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("storyURL")
    @Expose
    private String storyURL;
    @SerializedName("storyID")
    @Expose
    private String storyID;
    @SerializedName("storyImgUrl")
    @Expose
    private String storyImgUrl;
    @SerializedName("storyName")
    @Expose
    private String storyName;
    @SerializedName("storyDescription")
    @Expose
    private String storyDescription;
    @SerializedName("storyAuthor")
    @Expose
    private String storyAuthor;
    @SerializedName("storyGenre")
    @Expose
    private String storyGenre;
    @SerializedName("storyStatus")
    @Expose
    private Object storyStatus;
    @SerializedName("storyUpdated")
    @Expose
    private Integer storyUpdated;


    @BindingAdapter({ "storyImgUrl" })
    public static void loadImage(ImageView imageView, String imageURL) {

        Glide.with(imageView.getContext())
                .load(imageURL)
                .override(260,300)
                .placeholder(R.drawable.ic_close)
                .into(imageView);
    }

    @Bindable
    public Integer getStoryCountView() {
        return storyCountView;
    }

    public void setStoryCountView(Integer storyCountView) {
        this.storyCountView = storyCountView;
        notifyPropertyChanged(BR.storyCountView);
    }

    @Bindable
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
        notifyPropertyChanged(BR.id);
    }

    @Bindable
    public String getStoryURL() {
        return storyURL;
    }

    public void setStoryURL(String storyURL) {
        this.storyURL = storyURL;
        notifyPropertyChanged(BR.storyURL);
    }

    @Bindable
    public String getStoryID() {
        return storyID;
    }

    public void setStoryID(String storyID) {
        this.storyID = storyID;
        notifyPropertyChanged(BR.storyID);
    }

    @Bindable
    public String getStoryImgUrl() {
        return storyImgUrl;
    }

    public void setStoryImgUrl(String storyImgUrl) {
        this.storyImgUrl = storyImgUrl;
        notifyPropertyChanged(BR.storyImgUrl);
    }

    @Bindable
    public String getStoryName() {
        return storyName;
    }

    public void setStoryName(String storyName) {
        this.storyName = storyName;
        notifyPropertyChanged(BR.storyName);
    }

    @Bindable
    public String getStoryDescription() {
        return storyDescription;
    }

    public void setStoryDescription(String storyDescription) {
        this.storyDescription = storyDescription;
        notifyPropertyChanged(BR.storyDescription);
    }

    @Bindable
    public String getStoryAuthor() {
        return storyAuthor;
    }

    public void setStoryAuthor(String storyAuthor) {
        this.storyAuthor = storyAuthor;
        notifyPropertyChanged(BR.storyAuthor);
    }

    @Bindable
    public String getStoryGenre() {
        return storyGenre;
    }

    public void setStoryGenre(String storyGenre) {
        this.storyGenre = storyGenre;
        notifyPropertyChanged(BR.storyGenre);
    }

    @Bindable
    public Object getStoryStatus() {
        return storyStatus;
    }

    public void setStoryStatus(Object storyStatus) {
        this.storyStatus = storyStatus;
        notifyPropertyChanged(BR.storyStatus);
    }

    @Bindable
    public Integer getStoryUpdated() {
        return storyUpdated;
    }

    public void setStoryUpdated(Integer storyUpdated) {
        this.storyUpdated = storyUpdated;
        notifyPropertyChanged(BR.storyUpdated);
    }

    public static final DiffUtil.ItemCallback<StoryInformation> CALLBACK = new DiffUtil.ItemCallback<StoryInformation>() {
        @Override
        public boolean areItemsTheSame(StoryInformation oldItem, StoryInformation newItem) {
            return oldItem.id == newItem.id;
        }

        @Override
        public boolean areContentsTheSame(StoryInformation oldItem, StoryInformation newItem) {
            return true;
        }
    };

}