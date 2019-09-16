package com.chemander.mydemo.data.viewmodel;

import android.app.Application;
import android.util.Log;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.databinding.BindingAdapter;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import com.chemander.mydemo.data.model.StoryInformation;
import com.chemander.mydemo.data.model.source.StoryInformationDataSourceFactory;
import com.chemander.mydemo.utils.SettingsManager;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class StoryInformationViewModel extends AndroidViewModel {

    private Executor executor;
    private LiveData<PagedList<StoryInformation>> storyPagedList;


    public StoryInformationViewModel(@NonNull Application application) {
        super(application);

//        StoryService storyService = ApiUtils.getStoryService();
        StoryInformationDataSourceFactory factory = new StoryInformationDataSourceFactory();

        PagedList.Config config = (new PagedList.Config.Builder())
                .setEnablePlaceholders(true)
                .setInitialLoadSizeHint(2)
                .setPrefetchDistance(4)
                .setPageSize(SettingsManager.SIZE_OF_PAGE)
                .build();
        executor = Executors.newFixedThreadPool(5);
        storyPagedList = (new LivePagedListBuilder<Integer, StoryInformation>(factory, config))
                .setFetchExecutor(executor)
                .build();
    }

    public void createNewStoryPagedList(){
        storyPagedList = null;
        StoryInformationDataSourceFactory factory = new StoryInformationDataSourceFactory();

        PagedList.Config config = (new PagedList.Config.Builder())
                .setEnablePlaceholders(true)
                .setInitialLoadSizeHint(2)
                .setPrefetchDistance(4)
                .setPageSize(SettingsManager.SIZE_OF_PAGE)
                .build();
        storyPagedList = (new LivePagedListBuilder<Integer, StoryInformation>(factory, config))
                .setFetchExecutor(executor)
                .build();
    }
    public LiveData<PagedList<StoryInformation>> getStoryPagedList(){
        return storyPagedList;
    }
}
