package com.chemander.story.data.model;

import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.BindingAdapter;
import androidx.databinding.library.baseAdapters.BR;
import androidx.recyclerview.widget.DiffUtil;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chemander.story.R;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Calendar;

@Entity(tableName = "recent")
public class StoryInformation extends BaseObservable implements Serializable {

    @SerializedName("storyCountView")
    @Expose
    private Integer storyCountView;
    @SerializedName("_id")
    @Expose
    @NonNull
    @PrimaryKey
    @ColumnInfo(name = "_id")
    private String id;
    @SerializedName("storyURL")
    @ColumnInfo(name = "storyURL")
    @Expose
    private String storyURL;
    @SerializedName("storyID")
    @ColumnInfo(name = "storyID")
    @Expose
    private String storyID;
    @SerializedName("storyImgUrl")
    @ColumnInfo(name = "storyImgUrl")
    @Expose
    private String storyImgUrl;
    @SerializedName("storyName")
    @ColumnInfo(name = "storyName")
    @Expose
    private String storyName;
    @SerializedName("storyDescription")
    @ColumnInfo(name = "storyDescription")
    @Expose
    private String storyDescription;
    @SerializedName("storyAuthor")
    @ColumnInfo(name = "storyAuthor")
    @Expose
    private String storyAuthor;
    @SerializedName("storyGenre")
    @ColumnInfo(name = "storyGenre")
    @Expose
    private String storyGenre;
    @SerializedName("storyStatus")
    @Ignore
//    @ColumnInfo(name = "storyStatus")
    @Expose
    private Object storyStatus;
    @SerializedName("storyUpdated")
    @ColumnInfo(name = "storyUpdated")
    @Expose
    private Integer storyUpdated;

    @ColumnInfo(name = "recentChapterId")
    private String recentChapterId = "";
    @ColumnInfo(name = "recentPosition")
    private int recentPosition = 0;
    @ColumnInfo(name = "isDownload")
    private boolean isDownload = false;
    @ColumnInfo(name = "isFavorite")
    private boolean isFavorite = false;
    @ColumnInfo(name = "recentTime")
    private long recentTime = 0;
    @ColumnInfo(name = "downloadTime")
    private long downloadTime = 0;
    @ColumnInfo(name = "favoriteTime")
    private long favoriteTime = 0;



    @BindingAdapter({ "storyImgUrl" })
    public static void loadImage(ImageView imageView, String imageURL) {
        RequestOptions ro = new RequestOptions();
//
//        ro.diskCacheStrategy(DiskCacheStrategy.NONE);
//        ro.skipMemoryCache(true);
//        ro.centerCrop();

        Glide.with(imageView.getContext())
//                .applyDefaultRequestOptions(ro)
                .load(imageURL)
//                .override(260,300)
//                .centerCrop()
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
        if(storyStatus == null)
            return "";
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

    public String getRecentChapterId() {
        return recentChapterId;
    }

    public void setRecentChapterId(String recentChapterId) {
        this.recentChapterId = recentChapterId;
    }

    public int getRecentPosition() {
        return recentPosition;
    }

    public void setRecentPosition(int recentPosition) {
        this.recentPosition = recentPosition;
        setRecentTime(Calendar.getInstance().getTimeInMillis());
    }

    public boolean isDownload() {
        return isDownload;
    }

    public void setDownload(boolean download) {
        isDownload = download;
        setDownloadTime(Calendar.getInstance().getTimeInMillis());
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
        setFavoriteTime(Calendar.getInstance().getTimeInMillis());
    }

    public long getRecentTime() {
        return recentTime;
    }

    public void setRecentTime(long recentTime) {
        this.recentTime = recentTime;
    }

    public long getDownloadTime() {
        return downloadTime;
    }

    public void setDownloadTime(long downloadTime) {
        this.downloadTime = downloadTime;
    }

    public long getFavoriteTime() {
        return favoriteTime;
    }

    public void setFavoriteTime(long favoriteTime) {
        this.favoriteTime = favoriteTime;
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