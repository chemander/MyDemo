package com.chemander.story.reading;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageButton;
import android.widget.SearchView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chemander.story.R;
import com.chemander.story.data.model.ChapterInformation;
import com.chemander.story.data.model.StoryInformation;
import com.chemander.story.data.viewmodel.ChapterViewModel;
import com.chemander.story.utils.SettingsManager;

import java.util.ArrayList;
import java.util.List;

public class ListChaptersFragment extends Fragment {
    ArrayList<ChapterInformation> chapters;
//    StoryService storyService;
    String storyId;
    AdapterChapter adapterChapter;
    RecyclerView recyclerView;
    SearchView searchView;
    ImageButton buttonBack;
    StoryInformation storyInformation;
    ChapterViewModel chapterViewModel;
    final static String ID_OF_STORY = "storyId";

    public static ListChaptersFragment newInstance(StoryInformation data) {
        ListChaptersFragment dialog = new ListChaptersFragment();
        Bundle args = new Bundle();
        args.putSerializable(ID_OF_STORY, data);
        dialog.setArguments(args);
        return dialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_DeviceDefault_DialogWhenLarge_NoActionBar);
//        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
//        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
//        getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        View view = inflater.inflate(R.layout.list_chapter_fragment, container,false);
        recyclerView = (RecyclerView) view.findViewById(R.id.list_chapters);
        buttonBack = (ImageButton) view.findViewById(R.id.image_button_back);
        searchView = (SearchView) view.findViewById(R.id.search_view_chapters);
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
//        searchView.setQueryHint("Tìm kiếm chương truyện");
        storyInformation = (StoryInformation)getArguments().getSerializable(ID_OF_STORY);
        storyId = storyInformation.getStoryID();
//        storyService = ApiUtils.getStoryService();
        chapters = new ArrayList<>();
        adapterChapter = new AdapterChapter(getContext(), chapters);
        chapterViewModel = ViewModelProviders.of(getActivity()).get(ChapterViewModel.class);
//        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        try {
            setComponents();
            getChapters();
        }catch (Exception e){
            chapterViewModel.deleteStoryInformation(storyInformation);
        }
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
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
                getActivity().onBackPressed();
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
//                Toast.makeText(getContext(), "-position-"+chapters.get(recyclerView.indexOfChild(view)).getChapterName()+"-title"+, Toast.LENGTH_SHORT).show();
                String chapterId = adapterChapter.getChapterId(recyclerView.getChildAdapterPosition(view));
                storyInformation.setRecentChapterId(chapterId);
                storyInformation.setRecentPosition(0);
                searchView.clearFocus();
                chapterViewModel.setChapterId(chapterId);
                chapterViewModel.setStoryInformation(storyInformation);
                ReadingActivity readingActivity = new ReadingActivity();
                FragmentManager manager = getFragmentManager();
                if (manager.getBackStackEntryCount() > 0 ){
                    manager.popBackStack();
                }
                FragmentTransaction fragmentTransaction = manager.beginTransaction();
                fragmentTransaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
                fragmentTransaction.replace(R.id.frame_layout_chapters, readingActivity, SettingsManager.READING_FRAGMENT);
                fragmentTransaction.commit();
            }
        });
    }
    private void getChapters(){

                chapterViewModel.loadChapters(storyInformation);
                chapterViewModel.getMListLiveDataChapters().observe(this, new Observer<List<ChapterInformation>>() {
                    @Override
                    public void onChanged(List<ChapterInformation> chapterInformations) {
                        chapters.addAll(chapterInformations);
                        adapterChapter.notifyDataSetChanged();
                        searchView.clearFocus();
                    }
                });

                chapterViewModel.setStoryInformation(storyInformation);
        /*storyService.getChapters(storyId, 1,2000).enqueue(new Callback<GetChaptersInformation>() {
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
        });*/
    }
}
