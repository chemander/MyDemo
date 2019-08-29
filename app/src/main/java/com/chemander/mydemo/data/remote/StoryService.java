package com.chemander.mydemo.data.remote;

import com.chemander.mydemo.data.model.GetChaptersInformation;
import com.chemander.mydemo.data.model.GetHomeStoryInformation;
import com.chemander.mydemo.data.model.GetStoriesInformation;
import com.chemander.mydemo.data.model.StoryInformation;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface StoryService {
    @GET("/api/stories")
    Call<GetStoriesInformation> getStories(@Query("page") int page, @Query("pagesize") int pagesize, @Query("storyName") String storyName, @Query("storyAuthor") String storyAuthor, @Query("storyGenre") String storyGenre);

    @GET("/api/stories/{storyid}")
    Call<StoryInformation> getStoryDetail(@Path("storyid") String chapterid);

    @GET("/api/chapters")
    Call<GetChaptersInformation> getChapters(@Query("storyid") String storyId, @Query("page") int page, @Query("pagesize") int pagesize);

    @GET("/api/chapters/{chapterid}")
    Call<StoryInformation> getChapterDetail(@Path("chapterid") String chapterid);

    @GET("/api/stories/list_story_home")
    Call<GetHomeStoryInformation> getHomeStoryInformation();

    @GET("/api/stories/list_story_news")
    Call<GetStoriesInformation> getNewsStories(@Query("page") int page, @Query("pagesize") int pagesize);

    @GET("/api/stories/list_story_hots")
    Call<GetStoriesInformation> getHotStories(@Query("page") int page, @Query("pagesize") int pagesize);

    @GET("/api/stories/list_story_finish")
    Call<GetStoriesInformation> getFinishStories(@Query("page") int page, @Query("pagesize") int pagesize);
}
