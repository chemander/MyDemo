package com.chemander.mydemo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.chemander.mydemo.data.ReadJSON;
import com.chemander.mydemo.data.model.GetInformation;
import com.chemander.mydemo.data.model.StoryInformation;
import com.chemander.mydemo.data.remote.StoryService;
import com.chemander.mydemo.model.Chapter;
import com.chemander.mydemo.reading.ReadingActivity;
import com.chemander.mydemo.utils.ApiUtils;
import com.chemander.mydemo.utils.SettingsManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private AdapterChapter adapterChapter;
    private AdapterStory adapterStory;
    private List<Chapter> chapters;
    private List<StoryInformation> stories = new ArrayList<>();
    private View search_bar;
    private ImageButton buttonContinue;

    private StoryService storyService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET)
                != PackageManager.PERMISSION_GRANTED) {
            Log.d("Hung", "Permission not granted");
        }
        setContentView(R.layout.activity_main);
        search_bar = (View) findViewById(R.id.search_bar);
        recyclerView = (RecyclerView)findViewById(R.id.recycler_home);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapterStory = new AdapterStory(getApplicationContext(), stories);
        recyclerView.setAdapter(adapterStory);
//        chapters = ReadJSON.readChapterFromJSONFile(getApplicationContext());
//        adapterChapter = new AdapterChapter(this, chapters);
        buttonContinue = (ImageButton)findViewById(R.id.bt_continue);

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

        buttonContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ReadingActivity.class);
                startActivity(intent);
            }
        });

        storyService = ApiUtils.getStoryService();
        storyService.getStories(1).enqueue(new Callback<GetInformation>() {
            @Override
            public void onResponse(Call<GetInformation> call, Response<GetInformation> response) {
                if(response.isSuccessful()){
                    Log.d("Hung", "Total = "+response.body().getData().size());
                    stories.addAll(response.body().getData());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapterStory.notifyDataSetChanged();

                        }
                    });
                }else Log.d("Hung", "Total = "+response.code());
            }

            @Override
            public void onFailure(Call<GetInformation> call, Throwable t) {
                Log.d("Hung", "Error = "+t.toString());
            }
        });
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
