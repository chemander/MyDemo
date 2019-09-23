package com.chemander.story.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetChapterInformation {

    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("data")
    @Expose
    private ChapterDetail data;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public ChapterDetail getChapterDetail() {
        return data;
    }

    public void setChapterDetail(ChapterDetail data) {
        this.data = data;
    }

}