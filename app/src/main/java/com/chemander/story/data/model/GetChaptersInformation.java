package com.chemander.story.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetChaptersInformation {
    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("data")
    @Expose
    private List<ChapterInformation> data = null;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public List<ChapterInformation> getData() {
        return data;
    }

    public void setData(List<ChapterInformation> data) {
        this.data = data;
    }
}
