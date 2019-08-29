package com.chemander.mydemo.data.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class StoryInformationViewModelFactory implements ViewModelProvider.Factory {
    private Application mApplication;
    private String mParam;

    public StoryInformationViewModelFactory(Application application){
        this.mApplication = application;
    }
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T)new StoryInformationViewModel(mApplication);
    }
}
