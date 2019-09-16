package com.chemander.mydemo.data.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.chemander.mydemo.data.model.StoryInformation;

@Dao
public interface RecentDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertStoryInformation(StoryInformation... storyInformation);

    @Query("SELECT * FROM recent")
    StoryInformation[] loadAllRecent();

    @Query("SELECT * FROM recent WHERE storyID = :storyId")
    StoryInformation findRecent(String storyId);

    @Update
    void updateStoryInformation(StoryInformation... storyInformation);
}
