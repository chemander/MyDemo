package com.chemander.story.data.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.chemander.story.data.model.ChapterDetail;
import com.chemander.story.data.model.ChapterInformation;
import com.chemander.story.data.model.StoryInformation;

import java.util.List;

@Dao
public interface RecentDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertStoryInformation(StoryInformation... storyInformation);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long insertChapterDetail(ChapterDetail chapterDetail);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long[] insertChapterInformation(List<ChapterInformation> chapterInformations);

    @Query("SELECT * FROM chapters WHERE storyID = :storyId ORDER BY chapterNum ASC")
    ChapterInformation[] loadAllChapterInformations(String storyId);

    @Query("SELECT * FROM chapterDetail WHERE storyID = :storyId")
    ChapterDetail[] loadAllChapterDetails(String storyId);

    @Query("SELECT * FROM chapterDetail WHERE _id = :chapterID")
    ChapterDetail findChapterDetail(String chapterID);

    @Query("SELECT * FROM recent ORDER BY recentTime DESC LIMIT 1")
    StoryInformation loadLastestRecent();

    @Query("SELECT * FROM recent ORDER BY recentTime DESC")
    StoryInformation[] loadAllRecent();

    @Query("SELECT * FROM recent WHERE _id = :storyId")
    StoryInformation findRecent(String storyId);

    @Query("SELECT * FROM recent WHERE isDownload = 1 ORDER BY downloadTime DESC")
    StoryInformation[] loadAllDownload();

    @Query("SELECT * FROM recent WHERE isFavorite = 1 ORDER BY favoriteTime DESC")
    StoryInformation[] loadAllFavorite();

    @Update
    void updateStoryInformation(StoryInformation... storyInformation);

    @Delete
    void deleteStoryInformation(StoryInformation... storyInformation);
}
