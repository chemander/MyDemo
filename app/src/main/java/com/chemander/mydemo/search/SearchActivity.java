package com.chemander.mydemo.search;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SearchView;

import com.chemander.mydemo.R;
import com.chemander.mydemo.data.model.StoryInformation;
import com.chemander.mydemo.data.remote.StoryService;
import com.chemander.mydemo.data.viewmodel.StoryInformationViewModel;
import com.chemander.mydemo.databinding.ActivitySearchBinding;
import com.chemander.mydemo.information.StoryAdapter;
import com.chemander.mydemo.information.StoryPagedListAdapter;
import com.chemander.mydemo.utils.ApiUtils;
import com.chemander.mydemo.utils.SettingsManager;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {
    private RecyclerView listStoryRecycle;
    private ImageButton imageButtonBack;
    private SearchView searchView;
    private StoryService storyService;

    //Binding
    StoryInformationViewModel storyInformationViewModel;
    ActivitySearchBinding activitySearchBinding;
    StoryPagedListAdapter storyPagedListAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        SettingsManager.typeOfListStoryInformation = SettingsManager.SEARCH_STORY_LIST;
        initComponents();
        showOnRecyclerView();
    }

    private void initComponents() {

//        listStoryRecycle.setAdapter(storyAdapter);
//        storyService = ApiUtils.getStoryService();

        activitySearchBinding = DataBindingUtil.setContentView(this, R.layout.activity_search);
        imageButtonBack = activitySearchBinding.imageButtonBack;
        searchView = activitySearchBinding.searchViewChapters;
//        storyInformationViewModel = ViewModelProviders.of(this).get(StoryInformationViewModel.class);
        storyInformationViewModel = ViewModelProviders.of(this).get(StoryInformationViewModel.class);

        imageButtonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        SettingsManager.queryOfSearch = "a";
    }

    private void showOnRecyclerView(){
        listStoryRecycle = activitySearchBinding.recyclerListStory;
        listStoryRecycle.setLayoutManager(new LinearLayoutManager(this));
//        listStoryRecycle.setHasFixedSize(true);
        storyPagedListAdapter = new StoryPagedListAdapter(this);
        storyInformationViewModel.getStoryPagedList().observe(this, new Observer<PagedList<StoryInformation>>() {
            @Override
            public void onChanged(PagedList<StoryInformation> storyInformations) {
                storyPagedListAdapter.submitList(storyInformations);
                listStoryRecycle.setAdapter(storyPagedListAdapter);
                storyPagedListAdapter.notifyDataSetChanged();
            }
        });
    }
}
