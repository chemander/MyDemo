package com.chemander.mydemo.reading;


import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.chemander.mydemo.R;
import com.chemander.mydemo.data.ReadJSON;
import com.chemander.mydemo.model.Chapter;
import com.chemander.mydemo.utils.SettingsManager;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.List;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class ReadingActivity extends AppCompatActivity {
    private BottomSheetBehavior mBehavior;
    private BottomSheetDialog mBottomSheetDialog;
    private View bottom_sheet;
    private TextView mainContent;
    private TextView textTemp1;
    private TextView textTemp2;
    private TextView textTemp3;
    private TextView textTemp4;
    private List<Chapter> chapters;
    private Spinner spinner;
    private Spinner spinnerFontType;
    private ChapterSpinnerAdapter spinnerAdapter;
    private ImageButton buttonSettings;
    private ImageButton buttonPrevious;
    private ImageButton buttonNext;
    private ImageButton buttonIncrease;
    private ImageButton buttonDecrease;
    private ImageButton buttonBack;
    private EditText editFontSize;
    private String[] fontTypes;
    ScrollView scrollView;
    private View chapter_bar;
    private View chapter_select;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reading);
        bottom_sheet = findViewById(R.id.bottom_sheet);
        mBehavior = BottomSheetBehavior.from(bottom_sheet);
        mainContent = (TextView) findViewById(R.id.textviewMainContent);
        textTemp1 = (TextView) findViewById(R.id.textViewTemp1);
        textTemp2 = (TextView) findViewById(R.id.textViewTemp2);
        textTemp3 = (TextView) findViewById(R.id.textViewTemp3);
        textTemp4 = (TextView) findViewById(R.id.textViewTemp4);


        chapter_bar = (View) findViewById(R.id.chapter_bar);
        chapter_select = (View) findViewById(R.id.chapter_select);
        spinner = (Spinner) findViewById(R.id.spinner_chapter);
        scrollView = (ScrollView) findViewById(R.id.scrollReading);

        buttonSettings = (ImageButton)findViewById(R.id.bt_settings);
        buttonPrevious = (ImageButton)findViewById(R.id.bt_previous);
        buttonNext = (ImageButton)findViewById(R.id.bt_next);
        buttonBack = (ImageButton)findViewById(R.id.bt_back);

        scrollView.setVerticalScrollBarEnabled(false);
        scrollView.setSmoothScrollingEnabled(true);
        chapters = ReadJSON.readChapterFromJSONFile(this);
        fontTypes = SettingsManager.getAsset(getApplicationContext());

        spinnerAdapter = new ChapterSpinnerAdapter(this, R.layout.item_home, R.id.title_chapter, chapters);
        spinner.setAdapter(spinnerAdapter);
        initContent();
        setListener();
        animateChapterBar(true);
        chapter_bar.setVisibility(View.INVISIBLE);
        chapter_select.setVisibility(View.INVISIBLE);
    }

    public void nextChapter(){
        int currentChapter = SettingsManager.preferenceCurrentChapter;
        SettingsManager.preferenceCurrentPosition = 0;
        if(currentChapter == chapters.size()-1){
            return;
        }else{
            SettingsManager.preferenceCurrentChapter++;
            initContent();
        }
    }

    public void previousChapter(){
        int currentChapter = SettingsManager.preferenceCurrentChapter;
        SettingsManager.preferenceCurrentPosition = 0;
        if(currentChapter == 0){
            return;
        }else{
            SettingsManager.preferenceCurrentChapter--;
            initContent();
        }
    }

    private int scrollBottom = 0;
    public void setListener(){
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

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
        scrollView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View view, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (!scrollView.canScrollVertically(1)) {
                    scrollBottom++;
//                    scrollView.post(new Runnable() {
//                        @Override
//                        public void run() {
//                            scrollView.scrollTo(0,SettingsManager.preferenceCurrentPosition -10);
//                        }
//                    });
//                    Toast.makeText(getApplicationContext(), ""+scrollBottom, Toast.LENGTH_SHORT).show();
//                    if(scrollBottom == 2 && (scrollY == scrollView.getScrollY())){
//                        nextChapter();
//                        scrollBottom = 0;
//                    }
                }
            }
        });
//        scrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
//            @Override
//            public void onScrollChanged() {
//
//            }
//        });
        mainContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isChapterBarHide) {
                    animateChapterBar(false);
                }else {
                    animateChapterBar(true);
                }
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

        buttonPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                previousChapter();
            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                SettingsManager.preferenceCurrentChapter = position;
                initContent();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
    @Override
    protected void onPause() {
        SettingsManager.preferenceCurrentPosition = scrollView.getScrollY();
        SettingsManager.saveSettings(this);
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        SettingsManager.loadSettings(this);
    }

    public void setColor(){
        mainContent.setBackgroundColor(Color.parseColor(SettingsManager.preferenceBackgroundColor));
        mainContent.setTextColor(Color.parseColor(SettingsManager.preferenceTextColor));
    }
    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
    }
    private void initContent(){
        mainContent.setText(chapters.get(SettingsManager.preferenceCurrentChapter).getContent());
        setTextSize();
        setFontType();
        setColor();
        spinner.setSelection(SettingsManager.preferenceCurrentChapter);
        scrollView.post(new Runnable() {
            @Override
            public void run() {
                scrollView.scrollTo(0,SettingsManager.preferenceCurrentPosition);
            }
        });
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
        Typeface type = Typeface.createFromAsset(getAssets(),"fonts/"+fontType);
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

        editFontSize = (EditText)view.findViewById(R.id.editTextSize);
        spinnerFontType  = (Spinner) view.findViewById(R.id.spinnerFontType);
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, fontTypes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFontType.setAdapter(adapter);
        spinnerFontType.setSelection(SettingsManager.preferenceFontType);
        editFontSize.setText(String.valueOf(SettingsManager.preferenceFontSize));
        setBottomSheetListener();
        (view.findViewById(R.id.bt_close)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBottomSheetDialog.hide();
                animateChapterBar(true);
            }
        });

//        (view.findViewById(R.id.submit_rating)).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(getApplicationContext(), "Submit Rating", Toast.LENGTH_SHORT).show();
//            }
//        });

        mBottomSheetDialog = new BottomSheetDialog(this);
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
                mBottomSheetDialog = null;
                animateChapterBar(true);
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


}
