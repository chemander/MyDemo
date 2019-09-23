package com.chemander.story.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetHomeStoryInformation {
    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("story_news")
    @Expose
    private List<StoryInformation> storyNews = null;
    @SerializedName("story_hots")
    @Expose
    private List<StoryInformation> storyHots = null;
    @SerializedName("story_finish")
    @Expose
    private List<StoryInformation> storyFinish = null;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public List<StoryInformation> getStoryNews() {
        return storyNews;
    }

    public void setStoryNews(List<StoryInformation> storyNews) {
        this.storyNews = storyNews;
    }

    public List<StoryInformation> getStoryHots() {
        return storyHots;
    }

    public void setStoryHots(List<StoryInformation> storyHots) {
        this.storyHots = storyHots;
    }

    public List<StoryInformation> getStoryFinish() {
        return storyFinish;
    }

    public void setStoryFinish(List<StoryInformation> storyFinish) {
        this.storyFinish = storyFinish;
    }

}
