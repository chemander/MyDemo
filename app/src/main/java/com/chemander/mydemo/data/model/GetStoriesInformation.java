package com.chemander.mydemo.data.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetStoriesInformation {

    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("data")
    @Expose
    private List<StoryInformation> data = null;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public List<StoryInformation> getStories() {
        return data;
    }

    public void setData(List<StoryInformation> data) {
        this.data = data;
    }

}