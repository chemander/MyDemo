package com.chemander.story.data.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
@Entity(tableName = "chapterDetail")
public class ChapterDetail {
    @SerializedName("_id")
    @Expose
    @NonNull
    @PrimaryKey
    @ColumnInfo(name = "_id")
    private String id;
    @SerializedName("chapterUrl")
    @ColumnInfo(name = "chapterUrl")
    @Expose
    private String chapterUrl;
    @SerializedName("storyID")
    @ColumnInfo(name = "storyID")
    @Expose
    private String storyID;
    @SerializedName("chapterID")
    @ColumnInfo(name = "chapterID")
    @Expose
    private String chapterID;
    @SerializedName("chapterName")
    @ColumnInfo(name = "chapterName")
    @Expose
    private String chapterName;
    @SerializedName("chapterContent")
    @ColumnInfo(name = "chapterContent")
    @Expose
    private String chapterContent;
    @SerializedName("chapterNum")
    @ColumnInfo(name = "chapterNum")
    @Expose
    private Integer chapterNum;
    @SerializedName("chapterNumText")
    @ColumnInfo(name = "chapterNumText")
    @Expose
    private String chapterNumText;
    @SerializedName("chapterUpdated")
    @ColumnInfo(name = "chapterUpdated")
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

    public String getChapterNumText() {
        return chapterNumText;
    }

    public void setChapterNumText(String chapterNumText) {
        this.chapterNumText = chapterNumText;
    }

    public Integer getChapterUpdated() {
        return chapterUpdated;
    }

    public void setChapterUpdated(Integer chapterUpdated) {
        this.chapterUpdated = chapterUpdated;
    }
}
