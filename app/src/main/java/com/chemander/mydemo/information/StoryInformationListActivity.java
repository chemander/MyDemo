package com.chemander.mydemo.information;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chemander.mydemo.R;
import com.chemander.mydemo.data.model.GetStoriesInformation;
import com.chemander.mydemo.data.model.StoryInformation;
import com.chemander.mydemo.data.remote.StoryService;
import com.chemander.mydemo.data.viewmodel.StoryInformationViewModel;
import com.chemander.mydemo.data.viewmodel.StoryInformationViewModelFactory;
import com.chemander.mydemo.databinding.ActivityStoryInformationListBinding;
import com.chemander.mydemo.utils.ApiUtils;
import com.chemander.mydemo.utils.SettingsManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StoryInformationListActivity extends AppCompatActivity {
    private RecyclerView listStoryRecycle;
    private TextView titleOfList;
    private ImageButton imageButtonBack;
    private StoryAdapter storyAdapter;
    private List<StoryInformation> storyInformationList;
    private int typeOfList;
    private StoryService storyService;

    //Binding
    StoryInformationViewModel storyInformationViewModel;
    ActivityStoryInformationListBinding activityStoryInformationListBinding;
    StoryPagedListAdapter storyPagedListAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_information_list);
        titleOfList = (TextView)findViewById(R.id.text_story_information_title);
        typeOfList = getIntent().getIntExtra(SettingsManager.TITLE_STORY_LIST, 1);
        imageButtonBack = (ImageButton) findViewById(R.id.bt_back);

        listStoryRecycle = (RecyclerView) findViewById(R.id.recycler_list_story);
        listStoryRecycle.setLayoutManager(new LinearLayoutManager(this));
        storyInformationList = new ArrayList<>();
        storyAdapter = new StoryAdapter(getApplicationContext(), storyInformationList);

        listStoryRecycle.setAdapter(storyAdapter);
        storyService = ApiUtils.getStoryService();

        activityStoryInformationListBinding = DataBindingUtil.setContentView(this, R.layout.activity_story_information_list);
//        storyInformationViewModel = ViewModelProviders.of(this).get(StoryInformationViewModel.class);
        storyInformationViewModel = ViewModelProviders.of(this, new StoryInformationViewModelFactory(getApplication())).get(StoryInformationViewModel.class);
//        initComponents();
        showOnRecyclerView();
    }

    private void initComponents() {
        switch (typeOfList){
            case SettingsManager.NEWS_STORY_LIST:
                titleOfList.setText("Truuyện mới cập nhật");
                break;
            case SettingsManager.HOT_STORY_LIST:
                titleOfList.setText("Truuyện theo xu hướng");
                break;
            case SettingsManager.FINISH_STORY_LIST:
                titleOfList.setText("Truuyện đã hoàn thành");
                break;
                default: return;
        }

        getListStories(1);
        imageButtonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void showOnRecyclerView(){
        listStoryRecycle = activityStoryInformationListBinding.recyclerListStory;
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

    private void getListStories(int page){
        if(typeOfList == SettingsManager.HOT_STORY_LIST){
            storyService.getHotStories(page, SettingsManager.SIZE_OF_PAGE).enqueue(new Callback<GetStoriesInformation>() {
                @Override
                public void onResponse(Call<GetStoriesInformation> call, Response<GetStoriesInformation> response) {
                    if(response.isSuccessful()){
                        storyInformationList.addAll(response.body().getData());
                        storyAdapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onFailure(Call<GetStoriesInformation> call, Throwable t) {

                }
            });
        }

        if(typeOfList == SettingsManager.NEWS_STORY_LIST){
            storyService.getNewsStories(page, SettingsManager.SIZE_OF_PAGE).enqueue(new Callback<GetStoriesInformation>() {
                @Override
                public void onResponse(Call<GetStoriesInformation> call, Response<GetStoriesInformation> response) {
                    if(response.isSuccessful()){
                        storyInformationList.addAll(response.body().getData());
                        storyAdapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onFailure(Call<GetStoriesInformation> call, Throwable t) {

                }
            });
        }

        if(typeOfList == SettingsManager.FINISH_STORY_LIST){
            storyService.getFinishStories(page, SettingsManager.SIZE_OF_PAGE).enqueue(new Callback<GetStoriesInformation>() {
                @Override
                public void onResponse(Call<GetStoriesInformation> call, Response<GetStoriesInformation> response) {
                    if(response.isSuccessful()){
                        storyInformationList.addAll(response.body().getData());
                        storyAdapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onFailure(Call<GetStoriesInformation> call, Throwable t) {

                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }
}
