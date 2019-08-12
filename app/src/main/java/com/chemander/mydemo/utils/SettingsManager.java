package com.chemander.mydemo.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.chemander.mydemo.MainActivity;

public class SettingsManager {
    public static float preferenceFontSize;
    private static final String FONT_SIZE = "fontSize";
    public static String preferenceFontType;
    private static final String FONT_TYPE = "fontType";
    public static int preferenceCurrentChapter;
    private static final String CURRENT_CHAPTER = "currentChapter";
    public static int preferenceCurrentPosition;
    private static final String CURRENT_POSITION = "currentPosition";

    public static void loadSettings(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("Settings", Context.MODE_PRIVATE);
        if(sharedPreferences != null){
            preferenceFontSize = sharedPreferences.getFloat(FONT_SIZE, 16);
            preferenceFontType = sharedPreferences.getString(FONT_TYPE, "DejaVuSans");
            preferenceCurrentChapter = sharedPreferences.getInt(CURRENT_CHAPTER, 0);
            preferenceCurrentPosition = sharedPreferences.getInt(CURRENT_POSITION, 0);
        }else{
            preferenceFontSize = 16;
            preferenceFontType = "DejaVuSans";
            preferenceCurrentChapter = 0;
            preferenceCurrentPosition = 0;
        }
    }

    public static void saveSettings(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("Settings", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putFloat(FONT_SIZE, preferenceFontSize);
        editor.putString(FONT_TYPE, preferenceFontType);
        editor.putInt(CURRENT_CHAPTER, preferenceCurrentChapter);
        editor.putInt(CURRENT_POSITION, preferenceCurrentPosition);
//        editor.apply();
        editor.commit();
    }


}
