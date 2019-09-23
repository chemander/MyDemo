package com.chemander.story.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetStoryInformation {

    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("data")
    @Expose
    private StoryInformation data;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public StoryInformation getDataStoryInformation() {
        return data;
    }

    public void setData(StoryInformation data) {
        this.data = data;
    }

}