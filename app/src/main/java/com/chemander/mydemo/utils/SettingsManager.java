package com.chemander.mydemo.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.widget.TextView;

import com.chemander.mydemo.MainActivity;
import com.chemander.mydemo.R;

import java.io.IOException;

public class SettingsManager {
    //Intent keyword
    public final static String TITLE_STORY_LIST = "title";
    public final static String ID_OF_STORY = "storyId";
    public final static int SIZE_OF_PAGE = 20;
    public final static int STORY_LIST_INFORMATION = 0;
    public final static int NEWS_STORY_LIST = 1;
    public final static int HOT_STORY_LIST = 2;
    public final static int FINISH_STORY_LIST = 3;
    public final static int SEARCH_STORY_LIST = 4;
    public static int typeOfListStoryInformation = STORY_LIST_INFORMATION;
    public static String queryOfSearch = "";
    ////////
    public static final String STORY_INFORMATION = "storyInformation";
    public static final String CHAPTERS_INFORMATION = "chaptersInformation";
    public static final String CHAPTERS_ID = "chaptersID";
    ///////
    public static float preferenceFontSize;
    private static final String FONT_SIZE = "fontSize";
    public static int preferenceFontType;
    private static final String FONT_TYPE = "fontType";
    public static int preferenceCurrentChapter;
    private static final String CURRENT_CHAPTER = "currentChapter";
    public static int preferenceCurrentPosition;
    private static final String CURRENT_POSITION = "currentPosition";
    public static String preferenceBackgroundColor;
    private static final String BACKGROUND_COLOR = "backgroundColor";
    public static String preferenceTextColor;
    private static final String TEXT_COLOR = "textColor";

    public static void loadSettings(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("Settings", Context.MODE_PRIVATE);
        if(sharedPreferences != null){
            preferenceFontSize = sharedPreferences.getFloat(FONT_SIZE, 16);
            preferenceFontType = sharedPreferences.getInt(FONT_TYPE, 0);
            preferenceCurrentChapter = sharedPreferences.getInt(CURRENT_CHAPTER, 0);
            preferenceCurrentPosition = sharedPreferences.getInt(CURRENT_POSITION, 0);
            preferenceBackgroundColor = sharedPreferences.getString(BACKGROUND_COLOR, "#e6e6e6");
            preferenceTextColor = sharedPreferences.getString(TEXT_COLOR, "#ff000000");
        }else{
            preferenceFontSize = 16;
            preferenceFontType = 0;
            preferenceCurrentChapter = 0;
            preferenceCurrentPosition = 0;
            preferenceBackgroundColor = "#e6e6e6";
            preferenceTextColor = "#ff000000";
        }
    }

    public static void saveSettings(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("Settings", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putFloat(FONT_SIZE, preferenceFontSize);
        editor.putInt(FONT_TYPE, preferenceFontType);
        editor.putInt(CURRENT_CHAPTER, preferenceCurrentChapter);
        editor.putInt(CURRENT_POSITION, preferenceCurrentPosition);
        editor.putString(BACKGROUND_COLOR, preferenceBackgroundColor);
        editor.putString(TEXT_COLOR, preferenceTextColor);
//        editor.apply();
        editor.commit();
    }

    public static String[] getAsset(Context context){
        try {
            String[] listFont = context.getAssets().list("fonts");
//            String[] fontTypes = new String[listFont.length];
//            if(listFont.length > 0){
//
//            }
            return listFont;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ProgressDialog showLoadingDialog(Context context) {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.show();
        if (progressDialog.getWindow() != null) {
            progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        return progressDialog;
    }
}
