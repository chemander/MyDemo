package com.chemander.story.data.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

@Entity(tableName = "chapters")
public class ChapterInformation implements Serializable {
    @SerializedName("_id")
    @NonNull
    @PrimaryKey
    @ColumnInfo(name = "_id")
    @Expose
    private String id;
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
    @SerializedName("chapterNum")
    @ColumnInfo(name = "chapterNum")
    @Expose
    private Integer chapterNum;
    @SerializedName("chapterNumText")
    @ColumnInfo(name = "chapterNumText")
    @Expose
    private String chapterNumText;

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

    public String getChapterNumText() {
        return chapterNumText;
    }

    public void setChapterNumText(String chapterNumText) {
        this.chapterNumText = chapterNumText;
    }

}
