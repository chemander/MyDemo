package com.chemander.mydemo.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ChapterInformation {
    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("storyID")
    @Expose
    private String storyID;
    @SerializedName("chapterID")
    @Expose
    private String chapterID;
    @SerializedName("chapterName")
    @Expose
    private String chapterName;
    @SerializedName("chapterNum")
    @Expose
    private Integer chapterNum;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStoryID() {
        return storyID;
    }

    public void setStoryID(String storyID) {
        this.storyID = storyID;
    }

    public String getChapterID() {
        return chapterID;
    }

    public void setChapterID(String chapterID) {
        this.chapterID = chapterID;
    }

    public String getChapterName() {
        return chapterName;
    }

    public void setChapterName(String chapterName) {
        this.chapterName = chapterName;
    }

    public Integer getChapterNum() {
        return chapterNum;
    }

    public void setChapterNum(Integer chapterNum) {
        this.chapterNum = chapterNum;
    }

}
