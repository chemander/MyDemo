package com.chemander.story.information;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chemander.story.data.viewmodel.ChapterViewModel;
import com.chemander.story.reading.AdapterChapter;
import com.chemander.story.R;
import com.chemander.story.data.model.ChapterInformation;
import com.chemander.story.data.model.GetChaptersInformation;
import com.chemander.story.data.model.GetStoryInformation;
import com.chemander.story.data.model.StoryInformation;
import com.chemander.story.data.remote.StoryService;
import com.chemander.story.reading.ChaptersActivity;
import com.chemander.story.utils.ApiUtils;
import com.chemander.story.utils.SettingsManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StoryInformationActivity extends AppCompatActivity {
    private String storyId;
    private StoryInformation storyInformation;
    public ImageView cover;
    public ImageView imgBackground;
    private Button btReadNow;
    private Button btFavorite;
    public TextView title;
    public TextView description;
    public TextView author;
    public TextView genre;
    public TextView status;
    public ChapterViewModel viewModel;
//    public TextView titleStoryBar;
//    public ImageButton imageButtonBack;
    CardView cardView;
    ProgressDialog progressDialog;

    public RecyclerView recyclerView;
    private AdapterChapter adapterChapter;
    private List<ChapterInformation> chapters;
    private StoryService storyService;
    private Toolbar toolbar;
    private int[] background = {R.drawable.cover1, R.drawable.cover2, R.drawable.cover3, R.drawable.cover4, R.drawable.cover5};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.grey_20));
        }
        progressDialog = SettingsManager.showLoadingDialog(this);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        setContentView(R.layout.activity_story_information);
        cover = (ImageView) findViewById(R.id.imageViewCover);
        title = (TextView) findViewById(R.id.textTitleStory);
        imgBackground = (ImageView)findViewById(R.id.image_view_background);
//        titleStoryBar = (TextView)findViewById(R.id.text_story_information_title);
        description = (TextView) findViewById(R.id.story_description);
        author = (TextView) findViewById(R.id.textAuthor);
        status = (TextView) findViewById(R.id.textStatus);
        genre = (TextView) findViewById(R.id.textGenre);

        btFavorite = (Button)findViewById(R.id.bt_favorite);
        btReadNow = (Button)findViewById(R.id.bt_read_now);
//        recyclerView = (RecyclerView) findViewById(R.id.list_chapters);
//        imageButtonBack = (ImageButton) findViewById(R.id.bt_back);
        cardView = (CardView)findViewById(R.id.card_view_show_list_chapter);

//        storyInformation = (StoryInformation) getIntent().getSerializableExtra(SettingsManager.STORY_INFORMATION);
        storyId = getIntent().getStringExtra(SettingsManager.STORY_INFORMATION);
        chapters = new ArrayList<>();

        viewModel = ViewModelProviders.of(this).get(ChapterViewModel.class);
        storyService = ApiUtils.getStoryService();
        initComponents();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    private void setParameters() {
        if(storyInformation != null) {
            Glide.with(this).load(storyInformation.getStoryImgUrl()).into(cover);
            title.setText(storyInformation.getStoryName());
//            titleStoryBar.setText(storyInformation.getStoryName());
            String descriptionContent = storyInformation.getStoryDescription();
            descriptionContent = descriptionContent.replace("\n", "\n\n"+"    ");
            description.setText("   "+descriptionContent);
            author.setText("Tác giả: "+storyInformation.getStoryAuthor());
            if(storyInformation.getStoryGenre()!= null && !storyInformation.getStoryStatus().toString().equals("")){
                status.setText("Trạng thái: "+storyInformation.getStoryGenre());
            }else status.setText("Trạng thái: Đang cập nhật");

            genre.setText("Thể loại: "+storyInformation.getStoryStatus().toString());

            toolbar.setTitle(storyInformation.getStoryName());
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(progressDialog.isShowing()){
                    progressDialog.dismiss();
                }
            }
        },500);
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
//        recyclerView.setLayoutManager(linearLayoutManager);
//        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
//                linearLayoutManager.getOrientation());
//        LayoutAnimationController controller =
//                AnimationUtils.loadLayoutAnimation(getApplication(), R.anim.layout_animation_fall_down);
//
//        recyclerView.setLayoutAnimation(controller);
//        recyclerView.addItemDecoration(dividerItemDecoration);
//        adapterChapter = new AdapterChapter(getApplicationContext(), chapters);
//        recyclerView.setAdapter(adapterChapter);
//        recyclerView.getAdapter().notifyDataSetChanged();
//        recyclerView.scheduleLayoutAnimation();
    }

    private void initComponents() {
//        imageButtonBack.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                finish();
//                overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
//            }
//        });

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(null);
//        MenuItem item = (MenuItem)findViewById(android.R.id.home);
        toolbar.getNavigationIcon().setTint(getResources().getColor(R.color.colorPrimary));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                FragmentManager fragmentManager = getSupportFragmentManager();
//                ListChaptersFragment listChaptersFragment = ListChaptersFragment.newInstance(storyInformation);
//                listChaptersFragment.show(fragmentManager, null);
                Intent intent = new Intent(getApplicationContext(), ChaptersActivity.class);
                intent.putExtra(SettingsManager.STORY_INFORMATION, storyInformation);
                intent.putExtra(SettingsManager.READ_NOW, false);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        btReadNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                viewModel.insertStoryInformationData(storyInformation);
                updateStoryInformation();
                Intent intent = new Intent(getApplicationContext(), ChaptersActivity.class);
                intent.putExtra(SettingsManager.STORY_INFORMATION, storyInformation);
                intent.putExtra(SettingsManager.READ_NOW, true);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        btFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickOnFavorite();
            }
        });
        setBackground();

        StoryInformation story = viewModel.findStoryInformation(storyId);
        Log.d("Hung", ""+story);
        if(story!=null){
            storyInformation = story;
            setParameters();
//            initButtonFavorite();
        }else {
            getStoryInformation();
        }
    }

    private void initButtonFavorite(){

        if(storyInformation.isFavorite()){
            Drawable drawable = getResources().getDrawable(R.drawable.ic_favorite);
            btFavorite.setCompoundDrawablesWithIntrinsicBounds(null, drawable, null, null);
            isStoryFavorite = true;
        }else{
            isStoryFavorite = false;
            Drawable drawable = getResources().getDrawable(R.drawable.ic_unfavorite);
            btFavorite.setCompoundDrawablesWithIntrinsicBounds(null, drawable, null, null);
        }
    }

    private boolean isStoryFavorite = false;
    private void clickOnFavorite() {
        if(isStoryFavorite){
            isStoryFavorite = false;
            Drawable drawable = getResources().getDrawable(R.drawable.ic_unfavorite);
            btFavorite.setCompoundDrawablesWithIntrinsicBounds(null, drawable, null, null);
            viewModel.findStoryInformation(storyId);
        }else {
            isStoryFavorite = true;
//            btFavorite.setBackgroundResource(R.drawable.ic_favorite);
            Drawable drawable = getResources().getDrawable(R.drawable.ic_favorite);
            btFavorite.setCompoundDrawablesWithIntrinsicBounds(null, drawable, null, null);
        }
        storyInformation.setFavorite(isStoryFavorite);
    }
    private void setBackground() {
        int randomPosition = SettingsManager.getRandomNumber(background.length-1);
        imgBackground.setImageResource(background[randomPosition]);
        imgBackground.setColorFilter(Color.DKGRAY, PorterDuff.Mode.OVERLAY);
    }

    private void getStoryInformation(){

        storyService.getStoryDetail(storyId).enqueue(new Callback<GetStoryInformation>() {
            @Override
            public void onResponse(Call<GetStoryInformation> call, Response<GetStoryInformation> response) {
                if(response.isSuccessful()){
                    storyInformation = response.body().getDataStoryInformation();
                    setParameters();
                    initButtonFavorite();
//                    getChapters();
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            adapterChapter.notifyDataSetChanged();
//                        }
//                    });
                }else Log.d("Hung", "Total = "+response.code());
            }

            @Override
            public void onFailure(Call<GetStoryInformation> call, Throwable t) {
                Log.d("Hung", "Error = "+t.toString());
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        StoryInformation story = viewModel.findStoryInformation(storyId);
        if(story!=null){
            storyInformation = story;
            initButtonFavorite();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        updateStoryInformation();
        super.onDestroy();
    }



    private void updateStoryInformation() {
        StoryInformation story = viewModel.findStoryInformation(storyId);
        if(story == null){
            viewModel.insertStoryInformationData(storyInformation);
        }else {
            story.setFavorite(isStoryFavorite);
            viewModel.updateStoryInformationData(story);
        }
    }

    private void getChapters(){
        storyService.getChapters(storyInformation.getStoryID(), 1,2000).enqueue(new Callback<GetChaptersInformation>() {
            @Override
            public void onResponse(Call<GetChaptersInformation> call, Response<GetChaptersInformation> response) {
                if(response.isSuccessful()){
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