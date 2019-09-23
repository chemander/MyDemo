package com.chemander.story.reading;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.chemander.story.R;
import com.chemander.story.data.model.ChapterInformation;

import java.util.ArrayList;
import java.util.List;

public class AdapterChapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {
    private List<ChapterInformation> chapters = new ArrayList<>();
    private Context context;

    private View.OnClickListener onClickListener;

    Filter chaptersFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<ChapterInformation> filteredList = new ArrayList<>();
            if(charSequence == null || charSequence.length() == 0){
                filteredList.addAll(chapters);
            }else {
                String filterPattern = charSequence.toString().toLowerCase().trim();
                for(ChapterInformation chapterInformation : chapters){
                    if(chapterInformation.getChapterName().toLowerCase().contains(filterPattern)){
                        filteredList.add(chapterInformation);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            chapters = new ArrayList<>();
            chapters.addAll((List)filterResults.values);
//            chapters = (List)filterResults.values;
            notifyDataSetChanged();
        }
    };

    public void updateList(List<ChapterInformation> newList){
        chapters = new ArrayList<>();
        chapters.addAll(newList);
//            chapters = (List)filterResults.values;
        notifyDataSetChanged();
    }
    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public AdapterChapter(Context context, List<ChapterInformation> chapters){
        this.context = context;
        this.chapters = chapters;
    }

    @Override
    public Filter getFilter() {
        return chaptersFilter;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView title;
        public RelativeLayout layout;
        public ViewHolder(View view){
            super(view);
            title = (TextView) view.findViewById(R.id.title_chapter);
            layout = (RelativeLayout) view.findViewById(R.id.layout_parent);
        }
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_home, parent, false);
        vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof ViewHolder) {
            ViewHolder view = (ViewHolder) holder;
            final ChapterInformation chapter = chapters.get(position);
            view.title.setText(chapter.getChapterName());
            view.layout.setOnClickListener(onClickListener);
//            view.layout.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    if (SettingsManager.preferenceCurrentChapter != position) {
//                        SettingsManager.preferenceCurrentChapter = position;
//                        SettingsManager.preferenceCurrentPosition = 0;
//                        SettingsManager.saveSettings(context);
//                    }
//
//                    Intent intent = new Intent(context, ReadingActivity.class);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    context.startActivity(intent);
//                }
//            });
        }
    }

    @Override
    public int getItemCount() {
        return chapters.size();
    }

    public String getChapterId(int position){
        return chapters.get(position).getId();
    }
    public ChapterInformation getChapter(int position){
        return chapters.get(position);
    }
}
