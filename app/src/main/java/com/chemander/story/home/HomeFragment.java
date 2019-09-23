package com.chemander.story.home;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chemander.story.R;
import com.chemander.story.data.model.GetHomeStoryInformation;
import com.chemander.story.data.model.StoryInformation;
import com.chemander.story.data.remote.StoryService;
import com.chemander.story.information.StoryInformationListActivity;
import com.chemander.story.utils.ApiUtils;
import com.chemander.story.utils.SettingsManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    private View mainView;
    private RecyclerView newsRecyclerView;
    private ImageButton newsButton;
    private HomeRecycleAdapter newsAdapter;
    private List<StoryInformation> newsList;

    private RecyclerView hotRecyclerView;
    private ImageButton hotButton;
    private HomeRecycleAdapter hotAdapter;
    private List<StoryInformation> hotList;

    private RecyclerView fullRecyclerView;
    private ImageButton fullButton;
    private HomeRecycleAdapter fullAdapter;
    private List<StoryInformation> fullList;
    private ProgressDialog progressDialog;
    private NestedScrollView nestedScrollView;
    private StoryService storyService;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mainView = inflater.inflate(R.layout.fragment_home_stories_lists, container, false);
//        setRetainInstance(true);
        progressDialog = SettingsManager.showLoadingDialog(getActivity());
        //Setup news list
        nestedScrollView = (NestedScrollView)mainView.findViewById(R.id.nested_scroll_view);
        newsRecyclerView = (RecyclerView)mainView.findViewById(R.id.recycler_news);
        newsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        newsButton = (ImageButton) mainView.findViewById(R.id.button_continue_news);
        newsList = new ArrayList<>();
        newsAdapter = new HomeRecycleAdapter(getActivity(), newsList);
        newsRecyclerView.setAdapter(newsAdapter);

        //Setup hot list
        hotRecyclerView = (RecyclerView)mainView.findViewById(R.id.recycler_hot);
        hotRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        hotButton = (ImageButton) mainView.findViewById(R.id.button_continue_hot);
        hotList = new ArrayList<>();
        hotAdapter = new HomeRecycleAdapter(getActivity(), hotList);
        hotRecyclerView.setAdapter(hotAdapter);

        //Setup full list
        fullRecyclerView = (RecyclerView)mainView.findViewById(R.id.recycler_full);
        fullRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        fullButton = (ImageButton) mainView.findViewById(R.id.button_continue_full);
        fullList = new ArrayList<>();
        fullAdapter = new HomeRecycleAdapter(getActivity(), fullList);
        fullRecyclerView.setAdapter(fullAdapter);

        /////////////
        storyService = ApiUtils.getStoryService();
        initComponent();
        nestedScrollView.setVisibility(View.INVISIBLE);
        return mainView;
    }


    public void initComponent(){

        newsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openListStoryActivity(SettingsManager.NEWS_STORY_LIST);
            }
        });

        hotButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openListStoryActivity(SettingsManager.HOT_STORY_LIST);
            }
        });

        fullButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openListStoryActivity(SettingsManager.FINISH_STORY_LIST);
            }
        });

        storyService.getHomeStoryInformation().enqueue(new Callback<GetHomeStoryInformation>() {
            @Override
            public void onResponse(Call<GetHomeStoryInformation> call, Response<GetHomeStoryInformation> response) {
                if(response.isSuccessful()){
                    newsList.addAll(response.body().getStoryNews());
                    hotList.addAll(response.body().getStoryHots());
                    fullList.addAll(response.body().getStoryFinish());
//                    newsAdapter.notifyDataSetChanged();
                    updateAdapter();
                }
            }

            @Override
            public void onFailure(Call<GetHomeStoryInformation> call, Throwable t) {
                if(progressDialog.isShowing()){
                    progressDialog.dismiss();
                }
            }
        });
    }

    public void openListStoryActivity(int type){
        Intent intent = new Intent(getContext(), StoryInformationListActivity.class);
        SettingsManager.typeOfListStoryInformation = type;
        intent.putExtra(SettingsManager.TITLE_STORY_LIST, type);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
    private void updateAdapter(){
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                newsAdapter.notifyDataSetChanged();
                hotAdapter.notifyDataSetChanged();
                fullAdapter.notifyDataSetChanged();
            }
        });
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                if(progressDialog.isShowing()){
                    progressDialog.dismiss();
                }
                nestedScrollView.setVisibility(View.VISIBLE);
            }
        }, 1000);
    }
}
