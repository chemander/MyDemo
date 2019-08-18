package com.chemander.mydemo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.chemander.mydemo.data.ReadJSON;
import com.chemander.mydemo.model.Chapter;
import com.chemander.mydemo.reading.ReadingActivity;
import com.chemander.mydemo.utils.SettingsManager;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private AdapterChapter adapterChapter;
    private List<Chapter> chapters;
    private View search_bar;
    private ImageButton buttonContinue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        search_bar = (View) findViewById(R.id.search_bar);
        recyclerView = (RecyclerView)findViewById(R.id.recycler_home);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        chapters = ReadJSON.readChapterFromJSONFile(getApplicationContext());
        adapterChapter = new AdapterChapter(this, chapters);
        buttonContinue = (ImageButton)findViewById(R.id.bt_continue);

        recyclerView.setAdapter(adapterChapter);

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
