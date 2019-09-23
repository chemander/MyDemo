package com.chemander.story.data.model.source;


import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;

import com.chemander.story.data.remote.StoryService;
import com.chemander.story.utils.ApiUtils;
import com.chemander.story.utils.SettingsManager;

public class StoryInformationDataSourceFactory extends DataSource.Factory {

    private StoryInformationDataSource storyInformationDataSource;
    private StoryInformationNewsListDataSource storyInformationNewsListDataSource;
    private StoryInformationHotsListDataSource storyInformationHotsListDataSource;
    private StoryInformationFinishListDataSource storyInformationFinishListDataSource;
    private SearchListDataSource searchListDataSource;
    private StoryService storyService;
    private MutableLiveData<StoryInformationDataSource> mutableLiveData;
    private MutableLiveData<StoryInformationNewsListDataSource> mutableLiveDataNewsList;
    private MutableLiveData<StoryInformationHotsListDataSource> mutableLiveDataHotsList;
    private MutableLiveData<StoryInformationFinishListDataSource> mutableLiveDataFinishList;
    private MutableLiveData<SearchListDataSource> mutableLiveDataSearchList;

    public StoryInformationDataSourceFactory(){
        this.storyService = ApiUtils.getStoryService();
        switch (SettingsManager.typeOfListStoryInformation) {
            case SettingsManager.NEWS_STORY_LIST:
                mutableLiveDataNewsList = new MutableLiveData<>();
                break;
            case SettingsManager.HOT_STORY_LIST:
                mutableLiveDataHotsList = new MutableLiveData<>();
                break;
            case SettingsManager.FINISH_STORY_LIST:
                mutableLiveDataFinishList = new MutableLiveData<>();
                break;
            case SettingsManager.SEARCH_STORY_LIST:
                mutableLiveDataSearchList = new MutableLiveData<>();
                break;
            default: mutableLiveData = new MutableLiveData<>();
        }
    }

    @Override
    public DataSource create() {
        switch (SettingsManager.typeOfListStoryInformation) {
            case SettingsManager.NEWS_STORY_LIST:
                storyInformationNewsListDataSource = new StoryInformationNewsListDataSource();
                mutableLiveDataNewsList.postValue(storyInformationNewsListDataSource);
                return storyInformationNewsListDataSource;
            case SettingsManager.HOT_STORY_LIST:
                storyInformationHotsListDataSource = new StoryInformationHotsListDataSource();
                mutableLiveDataHotsList.postValue(storyInformationHotsListDataSource);
                return storyInformationHotsListDataSource;
            case SettingsManager.FINISH_STORY_LIST:
                storyInformationFinishListDataSource = new StoryInformationFinishListDataSource();
                mutableLiveDataFinishList.postValue(storyInformationFinishListDataSource);
                return storyInformationFinishListDataSource;
            case SettingsManager.SEARCH_STORY_LIST:
                searchListDataSource = new SearchListDataSource();
                mutableLiveDataSearchList.postValue(searchListDataSource);
                return searchListDataSource;
            default: storyInformationDataSource = new StoryInformationDataSource();
                mutableLiveData.postValue(storyInformationDataSource);
                return storyInformationDataSource;
        }
    }

    public MutableLiveData<StoryInformationDataSource> getMutableLiveData(){
        return mutableLiveData;
    }

    public MutableLiveData<StoryInformationNewsListDataSource> getMutableLiveDataNewsList() {
        return mutableLiveDataNewsList;
    }

    public MutableLiveData<StoryInformationHotsListDataSource> getMutableLiveDataHotsList() {
        return mutableLiveDataHotsList;
    }

    public MutableLiveData<StoryInformationFinishListDataSource> getMutableLiveDataFinishList() {
        return mutableLiveDataFinishList;
    }

    public MutableLiveData<SearchListDataSource> getMutableLiveDataSearchList() {
        return mutableLiveDataSearchList;
    }
}
