package com.chemander.mydemo.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ChapterDetail {
    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("chapterUrl")
    @Expose
    private String chapterUrl;
    @SerializedName("storyID")
    @Expose
    private String storyID;
    @SerializedName("chapterID")
    @Expose
    private String chapterID;
    @SerializedName("chapterName")
    @Expose
    private String chapterName;
    @SerializedName("chapterContent")
    @Expose
    private String chapterContent;
    @SerializedName("chapterNum")
    @Expose
    private Integer chapterNum;
    @SerializedName("chapterUpdated")
    @Expose
    private Integer chapterUpdated;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getChapterUrl() {
        return chapterUrl;
    }

    public void setChapterUrl(String chapterUrl) {
        this.chapterUrl = chapterUrl;
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

    public String getChapterContent() {
        return chapterContent;
    }

    public void setChapterContent(String chapterContent) {
        this.chapterContent = chapterContent;
    }

    public Integer getChapterNum() {
        return chapterNum;
    }

    public void setChapterNum(Integer chapterNum) {
        this.chapterNum = chapterNum;
    }

    public Integer getChapterUpdated() {
        return chapterUpdated;
    }

    public void setChapterUpdated(Integer chapterUpdated) {
        this.chapterUpdated = chapterUpdated;
    }
}
