package com.chemander.story.utils;

import androidx.room.TypeConverter;

public class DataConverters {
    @TypeConverter
    String fromObject(Object object){
        return object.toString();
    }
}
