package com.chemander.mydemo.utils;

import com.chemander.mydemo.data.remote.RetrofitClient;
import com.chemander.mydemo.data.remote.StoryService;

public class ApiUtils {
    public static final String BASE_URL = "http://113.160.53.62:8084/api/";

    public static StoryService getStoryService() {
        return RetrofitClient.getClient(BASE_URL).create(StoryService.class);
    }
}
