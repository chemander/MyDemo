package com.chemander.story.reading;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.chemander.story.R;
import com.chemander.story.data.model.ChapterInformation;
import com.chemander.story.data.model.StoryInformation;
import com.chemander.story.data.viewmodel.ChapterViewModel;
import com.chemander.story.utils.SettingsManager;

import java.util.List;

public class ChaptersActivity extends AppCompatActivity {
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    StoryInformation storyInformation;
    ChapterViewModel chapterViewModel;
    AppCompatActivity appCompatActivity = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.grey_20));
        }
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapters);
        storyInformation = (StoryInformation) getIntent().getSerializableExtra(SettingsManager.STORY_INFORMATION);
        Boolean isReadNow = getIntent().getBooleanExtra(SettingsManager.READ_NOW, false);
        ListChaptersFragment listChaptersFragment = ListChaptersFragment.newInstance(storyInformation);
        chapterViewModel = ViewModelProviders.of(this).get(ChapterViewModel.class);
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        if(savedInstanceState==null) {
            if(!isReadNow) {
                fragmentTransaction.add(R.id.frame_layout_chapters, listChaptersFragment, SettingsManager.CHAPTERS_FRAGMENT);
                fragmentTransaction.commit();
            }else {
                openReadingFragment();
            }
        }
    }

    private void openReadingFragment(){
        chapterViewModel.setStoryInformation(storyInformation);
        ReadingActivity readingActivity = new ReadingActivity();
        if(storyInformation.getRecentChapterId()!= null && !storyInformation.getRecentChapterId().isEmpty()) {
            chapterViewModel.loadChapters(storyInformation);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    chapterViewModel.setChapterId(storyInformation.getRecentChapterId());
                    fragmentTransaction.replace(R.id.frame_layout_chapters, readingActivity, SettingsManager.READING_FRAGMENT);
                    fragmentTransaction.commit();
                }
            }, 500);
        }else{
            chapterViewModel.loadChapters(storyInformation);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    chapterViewModel.getMListLiveDataChapters().observe( appCompatActivity, new Observer<List<ChapterInformation>>() {
                        @Override
                        public void onChanged(List<ChapterInformation> chapterInformations) {
                            if(chapterInformations.size() > 0) {
                                chapterViewModel.setChapterId(chapterInformations.get(0).getChapterID());
                                fragmentTransaction.replace(R.id.frame_layout_chapters, readingActivity, SettingsManager.READING_FRAGMENT);
                                fragmentTransaction.commit();
                            }else {
                                Toast.makeText(getApplicationContext(), "Không tìm thấy dữ liệu chương",Toast.LENGTH_SHORT).show();
                                appCompatActivity.finish();
                            }
                        }
                    });
                }
            }, 500);

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
