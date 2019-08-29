package com.chemander.mydemo.data.model;


import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;

import com.chemander.mydemo.data.remote.StoryService;
import com.chemander.mydemo.utils.ApiUtils;

public class StoryInformationDataSourceFactory extends DataSource.Factory {

    private StoryInformationDataSource storyInformationDataSource;
    private StoryService storyService;
    private MutableLiveData<StoryInformationDataSource> mutableLiveData;

    public StoryInformationDataSourceFactory(){
        this.storyService = ApiUtils.getStoryService();
        mutableLiveData = new MutableLiveData<>();
    }

    @Override
    public DataSource create() {
        storyInformationDataSource = new StoryInformationDataSource();
        mutableLiveData.postValue(storyInformationDataSource);
        return storyInformationDataSource;
    }

    public MutableLiveData<StoryInformationDataSource> getMutableLiveData(){
        return mutableLiveData;
    }
}
