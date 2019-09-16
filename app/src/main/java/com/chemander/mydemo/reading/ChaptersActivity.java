package com.chemander.mydemo.reading;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;

import com.chemander.mydemo.R;
import com.chemander.mydemo.data.model.StoryInformation;
import com.chemander.mydemo.utils.SettingsManager;

public class ChaptersActivity extends AppCompatActivity {
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapters);
        StoryInformation storyInformation = (StoryInformation) getIntent().getSerializableExtra(SettingsManager.STORY_INFORMATION);
        Log.d("Hung", "storyid"+storyInformation.getStoryID());
        ListChaptersFragment listChaptersFragment = ListChaptersFragment.newInstance(storyInformation);
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        if(savedInstanceState==null) {
            fragmentTransaction.add(R.id.frame_layout_chapters, listChaptersFragment);
            fragmentTransaction.commit();
        }
    }
}
