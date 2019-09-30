package com.chemander.story;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.chemander.story.data.model.StoryInformation;
import com.chemander.story.data.remote.StoryService;
import com.chemander.story.data.viewmodel.ChapterViewModel;
import com.chemander.story.home.HomeFragment;
import com.chemander.story.home.LibraryFragment;
import com.chemander.story.home.RecentFragment;
import com.chemander.story.information.StoryAdapter;
import com.chemander.story.reading.AdapterChapter;
import com.chemander.story.reading.ChaptersActivity;
import com.chemander.story.search.SearchActivity;
import com.chemander.story.utils.SettingsManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private AdapterChapter adapterChapter;
    private StoryAdapter storyAdapter;
    private List<StoryInformation> stories = new ArrayList<>();
    private View search_bar;
    private ImageButton buttonContinue;
    private ImageButton buttonSearch;
    private BottomNavigationView bottomNavigationView;
    private TextView mTitle;
    private StoryService storyService;
    private HomeFragment homeFragment;

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    ChapterViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET)
                != PackageManager.PERMISSION_GRANTED) {
        }
        setContentView(R.layout.activity_main);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.grey_20));
        }
        mTitle = (TextView)findViewById(R.id.search_text);
        search_bar = (View) findViewById(R.id.search_bar);
        bottomNavigationView = (BottomNavigationView)findViewById(R.id.bottom_navigation);
//        recyclerView = (RecyclerView)findViewById(R.id.recycler_home);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        storyAdapter = new StoryAdapter(getApplicationContext(), stories);
//        recyclerView.setAdapter(storyAdapter);

        //init fragment
        homeFragment = new HomeFragment();
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        if(savedInstanceState==null) {
            fragmentTransaction.add(R.id.main_fragment, homeFragment);
            fragmentTransaction.commit();
        }
//        chapters = ReadJSON.readChapterFromJSONFile(getApplicationContext());
//        adapterChapter = new AdapterChapter(this, chapters);
        buttonContinue = (ImageButton)findViewById(R.id.bt_continue);
        buttonSearch = (ImageButton)findViewById(R.id.bt_search);
        viewModel = ViewModelProviders.of(this).get(ChapterViewModel.class);
//        recyclerView.setAdapter(adapterChapter);

        NestedScrollView nested_content = (NestedScrollView) findViewById(R.id.nested_scroll_view);
        nested_content.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY < oldScrollY) { // up
//                    animateNavigation(false);
                    animateSearchBar(false);
                }
                if (scrollY > oldScrollY) { // down
//                    animateNavigation(true);
                    animateSearchBar(true);
                }
            }
        });

        initComponent();
    }



    public void initComponent(){
        buttonContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            StoryInformation storyInformation = viewModel.loadRecentStory();
            if(storyInformation != null) {
                Intent intent = new Intent(getApplicationContext(), ChaptersActivity.class);
                intent.putExtra(SettingsManager.STORY_INFORMATION, storyInformation);
                intent.putExtra(SettingsManager.READ_NOW, true);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
            }
        });

        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        mTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
//        storyService = ApiUtils.getStoryService();
//        storyService.getStories(1, 20, "", "", "").enqueue(new Callback<GetStoriesInformation>() {
//            @Override
//            public void onResponse(Call<GetStoriesInformation> call, Response<GetStoriesInformation> response) {
//                if(response.isSuccessful()){
//                    stories.addAll(response.body().getStories());
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            storyAdapter.notifyDataSetChanged();
//
//                        }
//                    });
//                }else Log.d("Hung", "Total = "+response.code());
//            }
//
//            @Override
//            public void onFailure(Call<GetStoriesInformation> call, Throwable t) {
//                Log.d("Hung", "Error = "+t.toString());
//            }
//        });

        bottomNavigationView.getMenu().removeItem(R.id.navigation_Download);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                fragmentTransaction = fragmentManager.beginTransaction();
                switch (menuItem.getItemId()){
                    case R.id.navigation_home:
                        animateSearchBar(false);
                        mTitle.setText("Tìm kiếm");
                        fragmentTransaction.replace(R.id.main_fragment, homeFragment);
                        fragmentTransaction.commit();
                        return true;
                    case R.id.navigation_library:
                        animateSearchBar(false);
                        mTitle.setText("Mục yêu thích");
                        LibraryFragment libraryFragment = new LibraryFragment();
                        fragmentTransaction.replace(R.id.main_fragment, libraryFragment);
                        fragmentTransaction.commit();
                        return true;
                    /*case R.id.navigation_Download:
                        mTitle.setText(menuItem.getTitle());
                        animateSearchBar(false);
                        return true;*/
                    case R.id.navigation_account:
                        animateSearchBar(false);
                        mTitle.setText("Truyện đã đọc");
                        RecentFragment recentFragment = new RecentFragment();
                        fragmentTransaction.replace(R.id.main_fragment, recentFragment);
                        fragmentTransaction.commit();
                        return true;
                }
                return false;
            }
        });

//        LayoutAnimationController controller =
//                AnimationUtils.loadLayoutAnimation(getApplication(), R.anim.layout_animation_fall_down);
//
//        recyclerView.setLayoutAnimation(controller);
//        recyclerView.getAdapter().notifyDataSetChanged();
//        recyclerView.scheduleLayoutAnimation();
    }

    boolean isSearchBarHide = false;

    private void animateSearchBar(final boolean hide) {
        if (isSearchBarHide && hide || !isSearchBarHide && !hide) return;
        isSearchBarHide = hide;
        int moveY = hide ? -(2 * search_bar.getHeight()) : 0;
        search_bar.animate().translationY(moveY).setStartDelay(100).setDuration(300).start();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        SettingsManager.loadSettings(this);
        super.onResume();
    }
}
