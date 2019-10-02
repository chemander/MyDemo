package com.chemander.story.reading;


import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.chemander.story.R;
import com.chemander.story.data.database.AppDatabase;
import com.chemander.story.data.model.ChapterDetail;
import com.chemander.story.data.model.ChapterInformation;
import com.chemander.story.data.model.StoryInformation;
import com.chemander.story.data.remote.StoryService;
import com.chemander.story.data.viewmodel.ChapterViewModel;
import com.chemander.story.utils.ApiUtils;
import com.chemander.story.utils.SettingsManager;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class ReadingActivity extends Fragment {
    private BottomSheetBehavior mBehavior;
    private BottomSheetDialog mBottomSheetDialog;
    private View bottom_sheet;
    private TextView mainContent;
    private TextView textTemp1;
    private TextView textTemp2;
    private TextView textTemp3;
    private TextView textTemp4;
    private TextView txtStoryTitle;
    private TextView txtChapterTitle;
    private ArrayList<ChapterInformation> chapters;
    private Spinner spinner;
    private Spinner spinnerFontType;
    private CardView cardViewPrevious;
    private CardView cardViewNext;
    private ChapterSpinnerAdapter spinnerAdapter;
    private ImageButton buttonSettings;
    private ImageButton buttonPrevious;
    private ImageButton buttonNext;
    private ImageButton buttonFavorite;
    private ImageButton buttonIncrease;
    private ImageButton buttonDecrease;
    private ImageButton buttonBack;
    private ImageButton buttonListChapter;
    private EditText editFontSize;
    private String[] fontTypes;
    ScrollView scrollView;
    private View chapter_bar;
    private View chapter_select;
    private View bottomAction;
    int currentPosition = 0;
    StoryService storyService;
    String chapterId;
    String storyId;
    ProgressDialog progressDialog;
    ChapterViewModel chapterViewModel;
    StoryInformation storyInformation;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        View view = inflater.inflate(R.layout.activity_reading, container,false);
        progressDialog = SettingsManager.showLoadingDialog(getActivity());
//        setContentView(R.layout.activity_reading);
        bottomAction = view.findViewById(R.id.bottom_action);
        bottom_sheet = view.findViewById(R.id.bottom_sheet);
        mBehavior = BottomSheetBehavior.from(bottom_sheet);
        mainContent = (TextView) view.findViewById(R.id.textviewMainContent);
        txtStoryTitle = (TextView) view.findViewById(R.id.reading_story_title);
        txtChapterTitle = (TextView) view.findViewById(R.id.reading_chapter_title);

        chapter_bar = (View) view.findViewById(R.id.chapter_bar);
        chapter_select = (View) view.findViewById(R.id.chapter_select);
        spinner = (Spinner) view.findViewById(R.id.spinner_chapter);
        scrollView = (ScrollView) view.findViewById(R.id.scrollReading);
        cardViewNext = (CardView)view.findViewById(R.id.card_view_next);
        cardViewPrevious = (CardView) view.findViewById(R.id.card_view_previous);

        buttonSettings = (ImageButton)view.findViewById(R.id.bt_settings);
        buttonPrevious = (ImageButton)chapter_select.findViewById(R.id.bt_previous);
        buttonFavorite = (ImageButton)chapter_select.findViewById(R.id.bt_favorite);
        buttonNext = (ImageButton)view.findViewById(R.id.bt_next);
        buttonBack = (ImageButton)view.findViewById(R.id.bt_back);
        buttonListChapter = (ImageButton)view.findViewById(R.id.image_view_list_chapters);
        chapterViewModel = ViewModelProviders.of(getActivity()).get(ChapterViewModel.class);

        scrollView.setVerticalScrollBarEnabled(false);
        scrollView.setSmoothScrollingEnabled(true);
        chapters = new ArrayList<>();
        storyInformation = chapterViewModel.getStoryInformation();
        storyId = storyInformation.getStoryID();
        currentPosition = storyInformation.getRecentPosition();
        txtStoryTitle.setText(storyInformation.getStoryName());
        chapterViewModel.getChapterId().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                chapterId = s;
            }
        });
        fontTypes = SettingsManager.getAsset(getContext());

        spinnerAdapter = new ChapterSpinnerAdapter(getContext(), R.layout.item_home, R.id.title_chapter, chapters);
        spinner.setAdapter(spinnerAdapter);
//        spinner.setIte
        storyService = ApiUtils.getStoryService();

        if(chapterViewModel.findStoryInformation(storyInformation.getId())==null){
            storyInformation.setRecentPosition(0);
            storyInformation.setRecentChapterId(chapterId);
            chapterViewModel.insertStoryInformationData(storyInformation);
        }

        getChapters();
        initContent();
        setListener();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                // Actions to do after 10 seconds
                animateChapterBar(true);
            }
        }, 200);
//        animateChapterBar(false);
//        chapter_bar.setVisibility(View.INVISIBLE);
//        chapter_select.setVisibility(View.INVISIBLE);

//        View decorView = getWindow().getDecorView();
//        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
        return view;
    }

    public void nextChapter(){
        bottomAction.setVisibility(View.INVISIBLE);
        int currentChapter = spinner.getSelectedItemPosition();
        currentPosition = 0;
        if(currentChapter == chapters.size()-1){
            return;
        }else{
            currentChapter++;
            spinner.setSelection(currentChapter);
            initContent();
        }
    }

    public void previousChapter(){
        bottomAction.setVisibility(View.INVISIBLE);
        int currentChapter = spinner.getSelectedItemPosition();
        currentPosition = 0;
        if(currentChapter == 0){
            return;
        }else{
            currentChapter--;
            spinner.setSelection(currentChapter);
            initContent();
        }
    }

    private int scrollBottom = 0;
    public void setListener(){
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });

        scrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction() == MotionEvent.ACTION_UP){
                    if (!scrollView.canScrollVertically(1)) {
                        if(scrollBottom > 2) {
                            nextChapter();
                            scrollBottom = 0;
                        }
                    }
                }

                if(motionEvent.getAction() == MotionEvent.ACTION_MOVE){
                    if (!scrollView.canScrollVertically(1)) {
                        scrollBottom++;
                    }
                }
                return false;
            }
        });
        /*scrollView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View view, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (!scrollView.canScrollVertically(1)) {
                    scrollBottom++;
//                    scrollView.post(new Runnable() {
//                        @Override
//                        public void run() {
//                            scrollView.scrollTo(0,currentPosition -10);
//                        }
//                    });
//                    Toast.makeText(getApplicationContext(), ""+scrollBottom, Toast.LENGTH_SHORT).show();
//                    if(scrollBottom == 2 && (scrollY == scrollView.getScrollY())){
//                        nextChapter();
//                        scrollBottom = 0;
//                    }
                }
            }
        });*/
//        scrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
//            @Override
//            public void onScrollChanged() {
//
//            }
//        });
        mainContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickMainContent();
            }
        });

        buttonSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBottomSheetDialog();
            }
        });


        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextChapter();
            }
        });

        buttonListChapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                ListChaptersFragment fragment = (ListChaptersFragment) getFragmentManager().findFragmentByTag(SettingsManager.CHAPTERS_FRAGMENT);
                try {
                    ListChaptersFragment fragment = ListChaptersFragment.newInstance(chapterViewModel.getStoryInformation());
                    getFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right).replace(R.id.frame_layout_chapters, fragment).commit();
                }catch (Exception e){
                    chapterViewModel.deleteStoryInformation(storyInformation);
                }

            }
        });

        //SetListener for Bottom Action
        ImageButton imageListChapters = bottomAction.findViewById(R.id.image_view_list_chapters);
        CardView cardViewBottomNext = bottomAction.findViewById(R.id.card_view_next);
        CardView cardViewBottomPrevious = bottomAction.findViewById(R.id.card_view_previous);
        imageListChapters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                ListChaptersFragment fragment = (ListChaptersFragment) getFragmentManager().findFragmentByTag(SettingsManager.CHAPTERS_FRAGMENT);
                try {
                    ListChaptersFragment fragment = ListChaptersFragment.newInstance(chapterViewModel.getStoryInformation());
                    getFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right).replace(R.id.frame_layout_chapters, fragment).commit();
                }catch (Exception e){
                    chapterViewModel.deleteStoryInformation(storyInformation);
                }

            }
        });

        cardViewBottomNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextChapter();
            }
        });

        cardViewBottomPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                previousChapter();
            }
        });
        //End setlistener for bottom action
        cardViewNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextChapter();
            }
        });
        buttonPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                previousChapter();
            }
        });

        cardViewPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                previousChapter();
            }
        });

        buttonFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickOnFavorite();
            }
        });
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                chapterId = chapters.get(position).getId();
                initContent();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private boolean isStoryFavorite = false;
    private void clickOnFavorite() {
        if(isStoryFavorite){
            isStoryFavorite = false;
            buttonFavorite.setImageResource(R.drawable.ic_unfavorite);
        }else {
            isStoryFavorite = true;
            buttonFavorite.setImageResource(R.drawable.ic_favorite);
        }
    }

    private void onClickMainContent() {
        if(isChapterBarHide) {
            animateChapterBar(false);
            getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            if(mBottomSheetDialog !=null)
                mBottomSheetDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }else {
            animateChapterBar(true);
            getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            if(mBottomSheetDialog !=null)
                mBottomSheetDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
    }


    @Override
    public void onPause() {
        currentPosition = scrollView.getScrollY();
        SettingsManager.saveSettings(getContext());
        storyInformation.setFavorite(isStoryFavorite);
        storyInformation.setRecentPosition(currentPosition);
        storyInformation.setRecentChapterId(chapterId);
        chapterViewModel.updateStoryInformationData(storyInformation);
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        SettingsManager.loadSettings(getContext());
        currentPosition = storyInformation.getRecentPosition();
        isStoryFavorite = storyInformation.isFavorite();
        if(!isStoryFavorite){
            buttonFavorite.setImageResource(R.drawable.ic_unfavorite);
        }else {
            buttonFavorite.setImageResource(R.drawable.ic_favorite);
        }
        initContent();
    }

    public void setColor(){
        scrollView.setBackgroundColor(Color.parseColor(SettingsManager.preferenceBackgroundColor));
        mainContent.setTextColor(Color.parseColor(SettingsManager.preferenceTextColor));
    }

    private void initContent(){
        try {
            progressDialog.show();
            bottomAction.setVisibility(View.INVISIBLE);
            getChapterContent();
            setTextSize();
            setFontType();
            setColor();
        }catch (Exception e){
            chapterViewModel.deleteStoryInformation(storyInformation);
        }
    }
    
    private void setTextSize(){
        mainContent.setTextSize(TypedValue.COMPLEX_UNIT_SP, SettingsManager.preferenceFontSize);
    }

    private void setFontType(){
        String fontType = fontTypes[SettingsManager.preferenceFontType];
        if(spinnerFontType != null) {
            fontType = spinnerFontType.getItemAtPosition(SettingsManager.preferenceFontType).toString();
        }
//        Log.d("Hung", fontType);
        Typeface type = Typeface.createFromAsset(getActivity().getAssets(),"fonts/"+fontType);
        mainContent.setTypeface(type);
    }

    public void setBottomSheetListener(){
        editFontSize.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                try {
                    float value = Float.valueOf(editable.toString());
                    SettingsManager.preferenceFontSize = value;
                    initContent();
                }catch (Exception e){

                }
            }
        });

        buttonDecrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SettingsManager.preferenceFontSize = Math.round(((SettingsManager.preferenceFontSize - 0.1f)*10f))/10f;
                String fontSize = String.valueOf(SettingsManager.preferenceFontSize);
                editFontSize.setText(fontSize);
                initContent();
            }
        });

        buttonIncrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SettingsManager.preferenceFontSize = Math.round(((SettingsManager.preferenceFontSize + 0.1f )*10f))/10f ;
                String fontSize = String.valueOf(SettingsManager.preferenceFontSize);
                editFontSize.setText(fontSize);
                initContent();
            }
        });

        spinnerFontType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                SettingsManager.preferenceFontType = position;
                setFontType();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
    private void showBottomSheetDialog() {
        if (mBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            mBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }

        final View view = getLayoutInflater().inflate(R.layout.bottom_sheet_reading, null);

        buttonIncrease = (ImageButton)view.findViewById(R.id.buttonIncrease);
        buttonDecrease = (ImageButton)view.findViewById(R.id.buttonDecrease);

        textTemp1 = (TextView) view.findViewById(R.id.textViewTemp1);
        textTemp2 = (TextView) view.findViewById(R.id.textViewTemp2);
        textTemp3 = (TextView) view.findViewById(R.id.textViewTemp3);
        textTemp4 = (TextView) view.findViewById(R.id.textViewTemp4);

        textTemp1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SettingsManager.preferenceBackgroundColor = "#FF424242";
                SettingsManager.preferenceTextColor = "#FFFFFFFF";
                setColor();
            }
        });

        textTemp2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SettingsManager.preferenceBackgroundColor = "#cccccc";
                SettingsManager.preferenceTextColor = "#FF424242";
                setColor();
            }
        });

        textTemp3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SettingsManager.preferenceBackgroundColor = "#e0d8c3";
                SettingsManager.preferenceTextColor = "#6a665b";
                setColor();
            }
        });

        textTemp4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SettingsManager.preferenceBackgroundColor = "#FAF7F7";
                SettingsManager.preferenceTextColor = "#000000";
                setColor();
            }
        });

        editFontSize = (EditText)view.findViewById(R.id.editTextSize);
        spinnerFontType  = (Spinner) view.findViewById(R.id.spinnerFontType);
        ArrayAdapter adapter = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_dropdown_item, fontTypes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFontType.setAdapter(adapter);
        spinnerFontType.setSelection(SettingsManager.preferenceFontType);
        editFontSize.setText(String.valueOf(SettingsManager.preferenceFontSize));
        setBottomSheetListener();
        (view.findViewById(R.id.bt_close)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBottomSheetDialog.hide();
//                mBottomSheetDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                onClickMainContent();
            }
        });

//        (view.findViewById(R.id.submit_rating)).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(getApplicationContext(), "Submit Rating", Toast.LENGTH_SHORT).show();
//            }
//        });

        if(mBottomSheetDialog == null)
        mBottomSheetDialog = new BottomSheetDialog(getContext());
        mBottomSheetDialog.setContentView(view);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mBottomSheetDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        // set background transparent
        ((View) view.getParent()).setBackgroundColor(getResources().getColor(android.R.color.transparent));
        mBottomSheetDialog.show();
        mBottomSheetDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
//                mBottomSheetDialog = null;
                onClickMainContent();
            }
        });

    }

    boolean isChapterBarHide = false;

    private void animateChapterBar(final boolean hide) {
        chapter_bar.setVisibility(View.VISIBLE);
        chapter_select.setVisibility(View.VISIBLE);
        if (isChapterBarHide && hide || !isChapterBarHide && !hide) return;
        isChapterBarHide = hide;
        int moveY = hide ? -(2 * chapter_bar.getHeight()) : 0;
        chapter_bar.animate().translationY(moveY).setStartDelay(100).setDuration(300).start();
        int moveYY = hide ? (2 * chapter_select.getHeight()) : 0;
        chapter_select.animate().translationY(moveYY).setStartDelay(100).setDuration(300).start();
    }

    private int findInChapters(){
        for(int i = 0; i < chapters.size(); i++){
            if(chapterId.equals(chapters.get(i).getId())){
                return i;
            }
        }
        return 0;
    }
    private void getChapterContent(){
        /*storyService.getChapterDetail(chapterId).enqueue(new Callback<GetChapterInformation>() {
            @Override
            public void onResponse(Call<GetChapterInformation> call, Response<GetChapterInformation> response) {
                    if(response.isSuccessful()){
                        ChapterDetail chapterDetail = response.body().getChapterDetail();
                        txtChapterTitle.setText("Chương "+chapterDetail.getChapterName());
                        String content = chapterDetail.getChapterContent();
                        content = content.replace("\n", "\n\n"+"    ");
                        mainContent.setText("   "+content);
                        scrollView.post(new Runnable() {
                            @Override
                            public void run() {
                                scrollView.scrollTo(0,currentPosition);
                            }
                        });
                    }else{
                        Toast.makeText(getContext(), "Dữ liệu chương bị lỗi", Toast.LENGTH_SHORT).show();
                    }
                dismiss();
            }

            @Override
            public void onFailure(Call<GetChapterInformation> call, Throwable t) {
                Toast.makeText(getContext(), "Không lấy được nội dụng của chương", Toast.LENGTH_SHORT).show();
                dismiss();
            }
        });*/

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                chapterViewModel.loadChapterContent(chapterId);
            }
        }, 500);
        chapterViewModel.getChapterDetail().observe(this, new Observer<ChapterDetail>() {
            @Override
            public void onChanged(ChapterDetail chapterDetail) {
                txtChapterTitle.setText("Chương "+chapterDetail.getChapterName());
                String content = chapterDetail.getChapterContent();
                content = content.replace("\n", "\n\n"+"    ");
                mainContent.setText("   "+content);
                scrollView.post(new Runnable() {
                    @Override
                    public void run() {
                        scrollView.scrollTo(0,currentPosition);
                    }
                });
                dismiss();
                bottomAction.setVisibility(View.VISIBLE);
//                setListener();
            }
        });
    }

    private void getChapters(){
        chapterViewModel.getMListLiveDataChapters().observe(this, new Observer<List<ChapterInformation>>() {
            @Override
            public void onChanged(List<ChapterInformation> chapterInformations) {
                chapters.addAll(chapterInformations);
                spinnerAdapter.notifyDataSetChanged();
                spinner.setSelection(findInChapters());
            }
        });
        /*storyService.getChapters(storyId, 1,2000).enqueue(new Callback<GetChaptersInformation>() {
            @Override
            public void onResponse(Call<GetChaptersInformation> call, Response<GetChaptersInformation> response) {
                if(response.isSuccessful()){
                    chapters.addAll(response.body().getData());
                    spinnerAdapter.notifyDataSetChanged();
                    spinner.setSelection(findInChapters());
                }else Log.d("Hung", "Total = "+response.code());
                dismiss();
            }

            @Override
            public void onFailure(Call<GetChaptersInformation> call, Throwable t) {
                dismiss();
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

    private void dismiss(){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                if(progressDialog.isShowing()){
                    progressDialog.dismiss();
                }
            }
        }, 500);
    }
}
