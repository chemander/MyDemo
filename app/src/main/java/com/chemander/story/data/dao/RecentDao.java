package com.chemander.story.data.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.chemander.story.data.model.StoryInformation;

@Dao
public interface RecentDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertStoryInformation(StoryInformation... storyInformation);

    @Query("SELECT * FROM recent ORDER BY recentTime DESC LIMIT 1")
    StoryInformation loadLastestRecent();

    @Query("SELECT * FROM recent ORDER BY recentTime DESC")
    StoryInformation[] loadAllRecent();

    @Query("SELECT * FROM recent WHERE _id = :storyId")
    StoryInformation findRecent(String storyId);

    @Query("SELECT * FROM recent WHERE isFavorite = 1 ORDER BY favoriteTime DESC")
    StoryInformation[] loadAllFavorite();

    @Update
    void updateStoryInformation(StoryInformation... storyInformation);

    @Delete
    void deleteStoryInformation(StoryInformation... storyInformation);
}
