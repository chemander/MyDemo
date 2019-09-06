package com.chemander.mydemo.reading;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.chemander.mydemo.R;
import com.chemander.mydemo.data.model.ChapterInformation;
import com.chemander.mydemo.model.Chapter;

import java.util.ArrayList;
import java.util.List;

public class ChapterSpinnerAdapter extends BaseAdapter {
    private List<ChapterInformation> chapters = new ArrayList<>();
    private Context context;
    private int layout;
    private int title;
    public ChapterSpinnerAdapter(Context context, int layout, int title, List<ChapterInformation> chapters){
//        super(context, layout, title, chapters);
        this.context = context;
        this.layout = layout;
        this.title = title;
        this.chapters = chapters;
    }

    @Override
    public int getCount() {
        return chapters.size();
    }

    @Override
    public ChapterInformation getItem(int position) {
        return chapters.get(position);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        convertView = inflater.inflate(layout, null);
        TextView title = (TextView) convertView.findViewById(this.title);
        title.setText("Chương "+chapters.get(position).getChapterName());
//        title.setTextColor(Color.parseColor("#ff000000"));
        return convertView;
    }
}
