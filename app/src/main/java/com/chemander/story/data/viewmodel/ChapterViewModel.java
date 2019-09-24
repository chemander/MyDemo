package com.chemander.story.data.viewmodel;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.chemander.story.data.database.AppDatabase;
import com.chemander.story.data.model.ChapterDetail;
import com.chemander.story.data.model.ChapterInformation;
import com.chemander.story.data.model.GetChapterInformation;
import com.chemander.story.data.model.GetChaptersInformation;
import com.chemander.story.data.model.StoryInformation;
import com.chemander.story.data.remote.StoryService;
import com.chemander.story.utils.ApiUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
    private MutableLiveData<StoryInformation[]>stories;
    public ChapterViewModel(@NonNull Application application) {
        super(application);
        storyService = ApiUtils.getStoryService();
        appDatabase = AppDatabase.getAppDatabase(getApplication());
        mutableLiveDataChapters = new MutableLiveData<>();
        chapterId = new MutableLiveData<>();
        chapterDetail = new MutableLiveData<>();
        stories = new MutableLiveData<>();
    }

    public void loadChapters(StoryInformation storyInformation){
        List<ChapterInformation> chapters = new ArrayList<>();
                storyService.getChapters(storyInformation.getStoryID(), 1,10000).enqueue(new Callback<GetChaptersInformation>() {
                    @Override
                    public void onResponse(Call<GetChaptersInformation> call, Response<GetChaptersInformation> response) {
                        if(response.isSuccessful()){
                            List<ChapterInformation> temp = response.body().getData();
                            Collections.sort(temp, new Comparator<ChapterInformation>() {
                                @Override
                                public int compare(ChapterInformation chapterInformation, ChapterInformation t1) {
                                    return chapterInformation.getChapterID().compareToIgnoreCase(t1.getChapterID());
//                                    return chapterInformation.getChapterNum().compareToIgnoreCase(t1.getChapterNum());
                                }
                            });
                            chapters.addAll(temp);
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
//        Log.d("Hung","Update: "+storyInformation.getStoryID()+"-recent"+storyInformation.getRecentChapterId()+"-position="+storyInformation.getRecentPosition());
        appDatabase.recentDao().updateStoryInformation(storyInformation);
    }

    public void insertStoryInformationData(StoryInformation storyInformation){
//        Log.d("Hung","Insert: "+storyInformation.getStoryID()+"-recent"+storyInformation.getRecentChapterId()+"-position="+storyInformation.getRecentPosition());
        appDatabase.recentDao().insertStoryInformation(storyInformation);
    }

    public StoryInformation findStoryInformation(String storyId){
        Log.d("Hung", "Alldata"+appDatabase.recentDao().loadAllRecent().length);
        StoryInformation storyInformation = appDatabase.recentDao().findRecent(storyId);
        if(storyInformation!=null) {
            Log.d("Hung", "Find: " + storyInformation.getId() + "-recent" + storyInformation.getRecentChapterId() + "-position=" + storyInformation.getRecentPosition());
        }else {
            Log.d("Hung", "storyInformation - "+storyId+"= "+storyInformation);
        }
        return storyInformation;
    }

    public StoryInformation[] loadAllFavoriteStories(){
        StoryInformation[] stories = appDatabase.recentDao().loadAllFavorite();
//        Log.d("Hung", "loadAllFavoriteStories - "+stories.length+"= "+stories[0].getStoryName()+"= "+stories[1].getStoryName());
//        this.stories.setValue(stories);
        return stories;
    }

    public StoryInformation[] loadAllRecentStories(){
        StoryInformation[] stories = appDatabase.recentDao().loadAllRecent();
//        Log.d("Hung", "loadAllFavoriteStories - "+stories.length+"= "+stories[0].getStoryName()+"= "+stories[1].getStoryName());
//        this.stories.setValue(stories);
        return stories;
    }

    public StoryInformation loadRecentStory(){
        StoryInformation story = appDatabase.recentDao().loadLastestRecent();
//        this.stories.setValue(stories);
        return story;
    }

    public MutableLiveData<StoryInformation[]> getStories() {
        return stories;
    }

    public MutableLiveData<String> getChapterId() {
        return chapterId;
    }

    public void setChapterId(String chapterId) {
        this.chapterId.setValue(chapterId);
    }

    public void deleteStoryInformation(StoryInformation storyInformation){
        appDatabase.recentDao().deleteStoryInformation(storyInformation);
    }
}
