package com.chemander.mydemo.data.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.chemander.mydemo.data.database.AppDatabase;
import com.chemander.mydemo.data.model.ChapterInformation;
import com.chemander.mydemo.data.model.GetChaptersInformation;
import com.chemander.mydemo.data.model.StoryInformation;
import com.chemander.mydemo.data.remote.StoryService;
import com.chemander.mydemo.utils.ApiUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChapterViewModel extends AndroidViewModel {
    private StoryService storyService;
    private StoryInformation storyInformation;
    private AppDatabase appDatabase;
    public ChapterViewModel(@NonNull Application application) {
        super(application);
        storyService = ApiUtils.getStoryService();
        appDatabase = AppDatabase.getAppDatabase(application);
    }

    public List<ChapterInformation> getChapters(StoryInformation storyInformation){
        List<ChapterInformation> chapters = new ArrayList<>();
        storyService.getChapters(storyInformation.getStoryID(), 1,2000).enqueue(new Callback<GetChaptersInformation>() {
            @Override
            public void onResponse(Call<GetChaptersInformation> call, Response<GetChaptersInformation> response) {
                if(response.isSuccessful()){
                    chapters.addAll(response.body().getData());
                }else Log.d("Hung", "Total = "+response.code());
            }

            @Override
            public void onFailure(Call<GetChaptersInformation> call, Throwable t) {
//                chapters.addAll(ReadJSON.readStoryInformationsFromJSONFile(getApplicationContext()));
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        adapterStory.notifyDataSetChanged();
//
//                    }
//                });
            }
        });
        return chapters;
    }

    public StoryInformation getStoryInformation() {
        return storyInformation;
    }

    public void setStoryInformation(StoryInformation storyInformation) {
        this.storyInformation = storyInformation;
    }

    public void updateStoryInformationData(StoryInformation storyInformation){
        appDatabase.recentDao().updateStoryInformation(storyInformation);
    }

    public void insertStoryInformationData(StoryInformation storyInformation){
        appDatabase.recentDao().insertStoryInformation(storyInformation);
    }

    public StoryInformation findStoryInformation(String storyId){
        return appDatabase.recentDao().findRecent(storyId);
    }
}
