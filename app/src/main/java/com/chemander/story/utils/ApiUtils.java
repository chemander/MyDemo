package com.chemander.story.utils;

import com.chemander.story.data.remote.RetrofitClient;
import com.chemander.story.data.remote.StoryService;

public class ApiUtils {
    public static final String BASE_URL = "http://113.160.53.62:8084/";

    public static StoryService getStoryService() {
        return RetrofitClient.getClient(BASE_URL).create(StoryService.class);
    }
}
