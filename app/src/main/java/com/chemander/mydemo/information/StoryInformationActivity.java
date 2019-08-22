package com.chemander.mydemo.information;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chemander.mydemo.AdapterChapter;
import com.chemander.mydemo.R;
import com.chemander.mydemo.data.ReadJSON;
import com.chemander.mydemo.data.model.StoryInformation;
import com.chemander.mydemo.model.Chapter;
import com.chemander.mydemo.utils.SettingsManager;

import java.util.List;

public class StoryInformationActivity extends AppCompatActivity {
    private StoryInformation storyInformation;
    public ImageView cover;
    public TextView title;
    public TextView description;
    public TextView author;
    public TextView genre;
    public TextView status;
    public TextView titleStoryBar;
    public ImageButton imageButtonBack;

    public RecyclerView recyclerView;
    private AdapterChapter adapterChapter;
    private List<Chapter> chapters;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        setContentView(R.layout.activity_story_information);
        cover = (ImageView) findViewById(R.id.imageViewCover);
        title = (TextView) findViewById(R.id.textTitleStory);
        titleStoryBar = (TextView)findViewById(R.id.text_story_information_title);
        description = (TextView) findViewById(R.id.story_description);
        author = (TextView) findViewById(R.id.textAuthor);
        status = (TextView) findViewById(R.id.textStatus);
        genre = (TextView) findViewById(R.id.textGenre);
        recyclerView = (RecyclerView) findViewById(R.id.list_chapters);
        imageButtonBack = (ImageButton) findViewById(R.id.bt_back);

        storyInformation = (StoryInformation) getIntent().getSerializableExtra(SettingsManager.STORY_INFORMATION);
        initComponents();
        setParameters();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }

    private void setParameters() {
        Glide.with(this).load(storyInformation.getImg()).override(300,350).centerCrop().into(cover);
        title.setText(storyInformation.getTitle());
        titleStoryBar.setText(storyInformation.getTitle());
        description.setText("Mô tả:"+ storyInformation.getDescription());
        author.setText("Tác giả: "+storyInformation.getAuthor());
        status.setText("Thể loại: "+storyInformation.getStatus());
        genre.setText("Trạng thái: "+storyInformation.getGenre());
        chapters = ReadJSON.readChapterFromJSONFile(getApplicationContext());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                linearLayoutManager.getOrientation());
        LayoutAnimationController controller =
                AnimationUtils.loadLayoutAnimation(getApplication(), R.anim.layout_animation_fall_down);

        recyclerView.setLayoutAnimation(controller);
        recyclerView.addItemDecoration(dividerItemDecoration);
        adapterChapter = new AdapterChapter(getApplicationContext(), chapters);
        recyclerView.setAdapter(adapterChapter);
        recyclerView.getAdapter().notifyDataSetChanged();
        recyclerView.scheduleLayoutAnimation();
    }

    private void initComponents() {
        imageButtonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
            }
        });
    }
}
