package com.chemander.mydemo.information;

import androidx.appcompat.app.AppCompatActivity;
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
import com.chemander.mydemo.data.model.ChapterInformation;
import com.chemander.mydemo.data.model.GetChaptersInformation;
import com.chemander.mydemo.data.model.GetStoriesInformation;
import com.chemander.mydemo.data.model.StoryInformation;
import com.chemander.mydemo.data.remote.StoryService;
import com.chemander.mydemo.model.Chapter;
import com.chemander.mydemo.utils.ApiUtils;
import com.chemander.mydemo.utils.SettingsManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
    private List<ChapterInformation> chapters;
    private StoryService storyService;

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
        Glide.with(this).load(storyInformation.getStoryImgUrl()).override(300,350).centerCrop().into(cover);
        title.setText(storyInformation.getStoryName());
        titleStoryBar.setText(storyInformation.getStoryName());
        description.setText("Mô tả:"+ storyInformation.getStoryDescription());
        author.setText("Tác giả: "+storyInformation.getStoryAuthor());
        status.setText("Thể loại: "+storyInformation.getStoryGenre());
        genre.setText("Trạng thái: "+storyInformation.getStoryStatus());
        chapters = new ArrayList<>();
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

        storyService = ApiUtils.getStoryService();
        storyService.getChapters(storyInformation.getStoryID(), 1,2000).enqueue(new Callback<GetChaptersInformation>() {
            @Override
            public void onResponse(Call<GetChaptersInformation> call, Response<GetChaptersInformation> response) {
                if(response.isSuccessful()){
                    Log.d("Hung", "Total = "+response.body().getData().size());
                    chapters.addAll(response.body().getData());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapterChapter.notifyDataSetChanged();

                        }
                    });
                }else Log.d("Hung", "Total = "+response.code());
            }

            @Override
            public void onFailure(Call<GetChaptersInformation> call, Throwable t) {
                Log.d("Hung", "Error = "+t.toString());
//                chapters.addAll(ReadJSON.readStoryInformationsFromJSONFile(getApplicationContext()));
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        adapterStory.notifyDataSetChanged();
//
//                    }
//                });
            }
        });
    }
}
