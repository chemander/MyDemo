package com.chemander.mydemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ImageButton;
import android.widget.TextView;

import com.chemander.mydemo.data.model.GetStoriesInformation;
import com.chemander.mydemo.data.model.StoryInformation;
import com.chemander.mydemo.data.remote.StoryService;
import com.chemander.mydemo.home.HomeFragment;
import com.chemander.mydemo.information.StoryAdapter;
import com.chemander.mydemo.model.Chapter;
import com.chemander.mydemo.reading.ReadingActivity;
import com.chemander.mydemo.search.SearchActivity;
import com.chemander.mydemo.utils.ApiUtils;
import com.chemander.mydemo.utils.SettingsManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private AdapterChapter adapterChapter;
    private StoryAdapter storyAdapter;
    private List<Chapter> chapters;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET)
                != PackageManager.PERMISSION_GRANTED) {
        }
        setContentView(R.layout.activity_main);
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

        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
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

//        initComponent();
    }



    public void initComponent(){
        buttonContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ReadingActivity.class);
                startActivity(intent);
            }
        });

        storyService = ApiUtils.getStoryService();
        storyService.getStories(1, 20, "", "", "").enqueue(new Callback<GetStoriesInformation>() {
            @Override
            public void onResponse(Call<GetStoriesInformation> call, Response<GetStoriesInformation> response) {
                if(response.isSuccessful()){
                    stories.addAll(response.body().getStories());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            storyAdapter.notifyDataSetChanged();

                        }
                    });
                }else Log.d("Hung", "Total = "+response.code());
            }

            @Override
            public void onFailure(Call<GetStoriesInformation> call, Throwable t) {
                Log.d("Hung", "Error = "+t.toString());
            }
        });

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId()){
                    case R.id.navigation_home:
                        mTitle.setText(menuItem.getTitle());
                        animateSearchBar(false);
                        return true;
                    case R.id.navigation_library:
                        mTitle.setText(menuItem.getTitle());
                        animateSearchBar(false);
                        return true;
                    case R.id.navigation_Download:
                        mTitle.setText(menuItem.getTitle());
                        animateSearchBar(false);
                        return true;
                    case R.id.navigation_account:
                        mTitle.setText(menuItem.getTitle());
                        animateSearchBar(false);
                        return true;
                }
                return false;
            }
        });

        LayoutAnimationController controller =
                AnimationUtils.loadLayoutAnimation(getApplication(), R.anim.layout_animation_fall_down);

        recyclerView.setLayoutAnimation(controller);
        recyclerView.getAdapter().notifyDataSetChanged();
        recyclerView.scheduleLayoutAnimation();
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
