package com.chemander.mydemo.data.remote;

import com.chemander.mydemo.data.model.GetInformation;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface StoryService {
    @GET("/api/stories")
    Call<GetInformation> getStories(@Query("page") int page);
}
