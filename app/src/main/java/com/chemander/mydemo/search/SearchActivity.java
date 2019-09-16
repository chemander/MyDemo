package com.chemander.mydemo.search;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.provider.SearchRecentSuggestions;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.Toast;

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
    private ProgressDialog progressDialog;
    private Context context;
    private SearchManager searchManager;

    //Binding
    StoryInformationViewModel storyInformationViewModel;
    ActivitySearchBinding activitySearchBinding;
    StoryPagedListAdapter storyPagedListAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        SettingsManager.typeOfListStoryInformation = SettingsManager.SEARCH_STORY_LIST;
        activitySearchBinding = DataBindingUtil.setContentView(this, R.layout.activity_search);
        imageButtonBack = (ImageButton) findViewById(R.id.image_button_back);
        searchView = (SearchView) findViewById(R.id.search_view_story);
        searchManager = (SearchManager)getSystemService(Context.SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false);
        searchView.setQueryRefinementEnabled(true);
//        View v = searchView.findViewById(android.widget.);
//        v.setBackgroundColor(Color.parseColor("here give actionbar color code"));
//        storyInformationViewModel = ViewModelProviders.of(this).get(StoryInformationViewModel.class);
        progressDialog = SettingsManager.showLoadingDialog(this);

        if (progressDialog.isShowing()){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            progressDialog.dismiss();
        }
        storyInformationViewModel = ViewModelProviders.of(this).get(StoryInformationViewModel.class);
        context = this;
        initComponents();
//        showOnRecyclerView();
    }

    private void initComponents() {
        listStoryRecycle = activitySearchBinding.recyclerListStory;
        listStoryRecycle.setLayoutManager(new LinearLayoutManager(this));
//        listStoryRecycle.setHasFixedSize(true);
        storyPagedListAdapter = new StoryPagedListAdapter(this);
        listStoryRecycle.setAdapter(storyPagedListAdapter);
        searchView.requestFocus();
        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                listStoryRecycle.setVisibility(View.INVISIBLE);
            }
        });


        searchView.setOnSuggestionListener(new SearchView.OnSuggestionListener() {
            @Override
            public boolean onSuggestionSelect(int i) {
//                Toast.makeText(context, "onSuggestionSelect"+i, Toast.LENGTH_SHORT).show();
                return false;
            }

            @Override
            public boolean onSuggestionClick(int i) {
                String suggestQuery = searchView.getSuggestionsAdapter().convertToString(searchView.getSuggestionsAdapter().getCursor()).toString();
                searchView.setQuery(suggestQuery, false);
                return true;
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                if(s != null && !s.isEmpty()) {
//                    searchView.getQuery();
                    progressDialog.show();
                    SettingsManager.queryOfSearch = s;
                    if(storyPagedListAdapter.getCurrentList() != null) {
//                    storyPagedListAdapter.submitList(listOf<>());
//                        storyPagedListAdapter.getCurrentList().clear();
                        storyInformationViewModel.createNewStoryPagedList();
//                        storyPagedListAdapter.notifyDataSetChanged();
                    }

//                    listStoryRecycle.notifyAll();
                    saveQuery(s);
                    showOnRecyclerView();
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            // Actions to do after 10 seconds

                            if(progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }
                            searchView.clearFocus();
                            imageButtonBack.clearFocus();
//                            getCurrentFocus().clearFocus();
                            listStoryRecycle.setVisibility(View.VISIBLE);
                        }
                    }, 500);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });

        imageButtonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void showOnRecyclerView(){
        storyInformationViewModel.getStoryPagedList().observe(this, new Observer<PagedList<StoryInformation>>() {
            @Override
            public void onChanged(PagedList<StoryInformation> storyInformations) {
                storyPagedListAdapter.submitList(storyInformations);
                storyPagedListAdapter.notifyDataSetChanged();
//                if(progressDialog.isShowing()) {
//                    try {
//                        Thread.sleep(300);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                    progressDialog.dismiss();
//                }
            }
        });

//        try {
//            Thread.sleep(1000);
//            android.os.SystemClock.sleep(1000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
    }

    public void saveQuery(String query){
//        Intent intent  = getIntent();
//
//        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
//            String query = intent.getStringExtra(SearchManager.QUERY);
            SearchRecentSuggestions suggestions = new SearchRecentSuggestions(this,
                    StorySuggestionProvider.AUTHORITY, StorySuggestionProvider.MODE);
            suggestions.saveRecentQuery(query, null);
//        }
    }
}
