package com.chemander.mydemo.information;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chemander.mydemo.AdapterChapter;
import com.chemander.mydemo.R;
import com.chemander.mydemo.data.model.ChapterInformation;
import com.chemander.mydemo.data.model.GetChaptersInformation;
import com.chemander.mydemo.data.remote.StoryService;
import com.chemander.mydemo.reading.ReadingActivity;
import com.chemander.mydemo.utils.ApiUtils;
import com.chemander.mydemo.utils.SettingsManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListChaptersFragment extends DialogFragment {
    ArrayList<ChapterInformation> chapters;
    StoryService storyService;
    String storyId;
    AdapterChapter adapterChapter;
    RecyclerView recyclerView;
    SearchView searchView;
    ImageButton buttonBack;
    final static String ID_OF_STORY = "storyId";

    public static ListChaptersFragment newInstance(String data) {
        ListChaptersFragment dialog = new ListChaptersFragment();
        Bundle args = new Bundle();
        args.putString(ID_OF_STORY, data);
        dialog.setArguments(args);
        return dialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_DeviceDefault_DialogWhenLarge_NoActionBar);
//        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
//        getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        return inflater.inflate(R.layout.list_chapter_fragment, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = (RecyclerView) view.findViewById(R.id.list_chapters);
        buttonBack = (ImageButton) view.findViewById(R.id.image_button_back);
        searchView = (SearchView) view.findViewById(R.id.search_view_chapters);
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
//        searchView.setQueryHint("Tìm kiếm chương truyện");
        storyId = getArguments().getString(ID_OF_STORY, "");
        storyService = ApiUtils.getStoryService();
        chapters = new ArrayList<>();
        adapterChapter = new AdapterChapter(getContext(), chapters);
//        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        setComponents();
        getChapters();
    }

    void setComponents(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(),
                linearLayoutManager.getOrientation());
        LayoutAnimationController controller =
                AnimationUtils.loadLayoutAnimation(getContext(), R.anim.layout_animation_fall_down);

        recyclerView.setLayoutAnimation(controller);
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setAdapter(adapterChapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.getAdapter().notifyDataSetChanged();
        recyclerView.scheduleLayoutAnimation();

        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDialog().dismiss();
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                adapterChapter.getFilter().filter(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                String search = s.toLowerCase().trim();
                List<ChapterInformation> newList = new ArrayList<>();
                for(ChapterInformation chapterInformation: chapters){
                    if(chapterInformation.getChapterName().toLowerCase().contains(search)){
                        newList.add(chapterInformation);
                    }
                }

                adapterChapter.updateList(newList);
                return false;
            }
        });

        adapterChapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(getContext(), "-position-"+chapters.get(recyclerView.indexOfChild(view)).getId()+"-title"+adapterChapter.getChapterId(recyclerView.indexOfChild(view)),Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getContext(), ReadingActivity.class);
                intent.putExtra(SettingsManager.CHAPTERS_INFORMATION, storyId);
                //adapterChapter.getChapterId(recyclerView.indexOfChild(view)) = id of current position
                intent.putExtra(SettingsManager.CHAPTERS_ID, adapterChapter.getChapterId(recyclerView.indexOfChild(view)));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getContext().startActivity(intent);
            }
        });
    }
    private void getChapters(){
        storyService.getChapters(storyId, 1,2000).enqueue(new Callback<GetChaptersInformation>() {
            @Override
            public void onResponse(Call<GetChaptersInformation> call, Response<GetChaptersInformation> response) {
                if(response.isSuccessful()){
                    chapters.addAll(response.body().getData());
                    adapterChapter.notifyDataSetChanged();
                }else Log.d("Hung", "Total = "+response.code());
            }

            @Override
            public void onFailure(Call<GetChaptersInformation> call, Throwable t) {
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
