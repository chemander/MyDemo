package com.chemander.mydemo.data.remote;

import com.chemander.mydemo.data.model.Datum;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
public interface StoryService {
    @GET("stories")
    Call<List<Datum>> getStories();
}
