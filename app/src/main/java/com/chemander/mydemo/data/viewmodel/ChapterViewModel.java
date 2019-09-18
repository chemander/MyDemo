package com.chemander.mydemo.data.viewmodel;

import android.app.Application;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.chemander.mydemo.data.database.AppDatabase;
import com.chemander.mydemo.data.model.ChapterDetail;
import com.chemander.mydemo.data.model.ChapterInformation;
import com.chemander.mydemo.data.model.GetChapterInformation;
import com.chemander.mydemo.data.model.GetChaptersInformation;
import com.chemander.mydemo.data.model.StoryInformation;
import com.chemander.mydemo.data.remote.StoryService;
import com.chemander.mydemo.utils.ApiUtils;
import com.chemander.mydemo.utils.SettingsManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChapterViewModel extends AndroidViewModel {
    private StoryService storyService;
    private StoryInformation storyInformation;
    private AppDatabase appDatabase;
    private MutableLiveData<List<ChapterInformation>> mutableLiveDataChapters;
    private MutableLiveData<ChapterDetail> chapterDetail;
    private MutableLiveData<String> chapterId;
    public ChapterViewModel(@NonNull Application application) {
        super(application);
        storyService = ApiUtils.getStoryService();
        appDatabase = AppDatabase.getAppDatabase(getApplication());
        mutableLiveDataChapters = new MutableLiveData<>();
        chapterId = new MutableLiveData<>();
        chapterDetail = new MutableLiveData<>();
    }

    public void loadChapters(StoryInformation storyInformation){
        List<ChapterInformation> chapters = new ArrayList<>();
                storyService.getChapters(storyInformation.getStoryID(), 1,2000).enqueue(new Callback<GetChaptersInformation>() {
                    @Override
                    public void onResponse(Call<GetChaptersInformation> call, Response<GetChaptersInformation> response) {
                        if(response.isSuccessful()){
                            chapters.addAll(response.body().getData());
                            mutableLiveDataChapters.setValue(chapters);
                        }else Log.d("Hung", "Total = "+response.code());
                    }

                    @Override
                    public void onFailure(Call<GetChaptersInformation> call, Throwable t) {
//
                    }
                });
    }

    public void loadChapterContent(String chapterId){
        storyService.getChapterDetail(chapterId).enqueue(new Callback<GetChapterInformation>() {
            @Override
            public void onResponse(Call<GetChapterInformation> call, Response<GetChapterInformation> response) {
                if(response.isSuccessful()){
                    chapterDetail.setValue(response.body().getChapterDetail());
                }else{
                    Toast.makeText(getApplication(), "Dữ liệu chương bị lỗi", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GetChapterInformation> call, Throwable t) {
            }
        });
    }

    public MutableLiveData<ChapterDetail> getChapterDetail() {
        return chapterDetail;
    }

    public LiveData<List<ChapterInformation>> getMListLiveDataChapters(){
        return mutableLiveDataChapters;
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

    public MutableLiveData<String> getChapterId() {
        return chapterId;
    }

    public void setChapterId(String chapterId) {
        this.chapterId.setValue(chapterId);
    }
}
